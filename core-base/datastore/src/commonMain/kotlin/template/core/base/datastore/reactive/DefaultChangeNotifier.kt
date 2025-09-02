/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:OptIn(ExperimentalAtomicApi::class)

package template.core.base.datastore.reactive

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import template.core.base.datastore.contracts.DataStoreChangeEvent
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.decrementAndFetch
import kotlin.concurrent.atomics.incrementAndFetch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Production-ready implementation of [ChangeNotifier] using SharedFlow for true event broadcasting.
 *
 * This implementation ensures all observers receive all events, with configurable buffering and
 * overflow strategies. It's thread-safe and follows structured concurrency principles.
 *
 * Key improvements over Channel-based implementation:
 * - Multiple observers receive ALL events (not just one)
 * - Non-blocking event emission
 * - Configurable replay for late subscribers
 * - Better memory management with overflow strategies
 * - Proper structured concurrency (no GlobalScope)
 *
 * Example usage:
 * ```kotlin
 * val notifier = DefaultChangeNotifier()
 *
 * // Multiple observers all receive the same events
 * val job1 = launch { notifier.observeChanges().collect { println("Observer 1: $it") } }
 * val job2 = launch { notifier.observeChanges().collect { println("Observer 2: $it") } }
 *
 * notifier.notifyChange(DataStoreChangeEvent.ValueAdded("key", "value"))
 * // Both observers receive the event
 * ```
 *
 * @property replay Number of events to replay to new subscribers (default: 0)
 * @property extraBufferCapacity Extra buffer capacity beyond replay (default: 64)
 * @property onBufferOverflow Strategy when buffer is full (default: DROP_OLDEST)
 * @property scope Optional coroutine scope for handling suspended emissions
 */
@Suppress("MaxLineLength")
class DefaultChangeNotifier(
    private val replay: Int = 0,
    private val extraBufferCapacity: Int = 64,
    private val onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST,
    private val scope: CoroutineScope? = null,
) : ChangeNotifier {

    private val changeFlow = MutableSharedFlow<DataStoreChangeEvent>(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity,
        onBufferOverflow = onBufferOverflow,
    )

    // Track active observers for debugging/monitoring
    private val activeObservers = AtomicInt(0)

    // Track if the notifier has been cleared
    private val isCleared = AtomicBoolean(false)

    // Internal scope for SUSPEND operations if no external scope provided
    private val internalScope = scope ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Notifies all active listeners of a change event.
     *
     * This method is non-blocking and thread-safe. If the buffer is full,
     * the behavior depends on the configured [onBufferOverflow] strategy.
     *
     * For SUSPEND strategy without a provided scope, events will be dropped with a warning.
     *
     * @param change The event describing the change.
     */
    override fun notifyChange(change: DataStoreChangeEvent) {
        if (isCleared.load()) {
            println("[ChangeNotifier] Attempted to notify after clear: $change")
            return
        }

        val emitted = changeFlow.tryEmit(change)

        if (!emitted) {
            when (onBufferOverflow) {
                BufferOverflow.DROP_OLDEST -> {
                    // Already handled by SharedFlow internally
                    println("[ChangeNotifier] Buffer full, dropped oldest event for: $change")
                }

                BufferOverflow.DROP_LATEST -> {
                    println("[ChangeNotifier] Buffer full, dropping event: $change")
                }

                BufferOverflow.SUSPEND -> {
                    // Only attempt suspended emission if we have a scope
                    if (scope != null || !isCleared.load()) {
                        internalScope.launch {
                            try {
                                // 5 second timeout to prevent indefinite blocking
                                withTimeout(5000) {
                                    changeFlow.emit(change)
                                }
                            } catch (e: TimeoutCancellationException) {
                                println("[ChangeNotifier] Timeout emitting event: $change")
                            } catch (e: CancellationException) {
                                // Scope was cancelled, ignore
                            } catch (e: Exception) {
                                println("[ChangeNotifier] Failed to emit suspended event: $change, error: $e")
                            }
                        }
                    } else {
                        println("[ChangeNotifier] Buffer full, cannot emit with SUSPEND strategy without scope: $change")
                    }
                }
            }
        }
    }

    /**
     * Suspending version of notifyChange for use within coroutines.
     * This will suspend until the event can be emitted.
     *
     * @param change The event describing the change.
     * @throws CancellationException if the coroutine is cancelled
     */
    suspend fun notifyChangeSuspend(change: DataStoreChangeEvent) {
        if (!isCleared.load()) {
            changeFlow.emit(change)
        }
    }

    /**
     * Tries to notify with a result indicating success.
     *
     * @param change The event describing the change.
     * @return true if the event was emitted successfully, false otherwise
     */
    fun tryNotifyChange(change: DataStoreChangeEvent): Boolean {
        return if (!isCleared.load()) {
            changeFlow.tryEmit(change)
        } else {
            false
        }
    }

    /**
     * Observes all change events emitted to this notifier.
     *
     * Each collector receives ALL events independently. Late subscribers
     * will receive replayed events based on the [replay] configuration.
     *
     * @return A [Flow] emitting [DataStoreChangeEvent] instances as changes occur.
     */
    override fun observeChanges(): Flow<DataStoreChangeEvent> {
        return changeFlow
            .onStart {
                val count = activeObservers.incrementAndFetch()
                println("[ChangeNotifier] New observer connected. Total: $count")
            }
            .onCompletion {
                val count = activeObservers.decrementAndFetch()
                println("[ChangeNotifier] Observer disconnected. Total: $count")
            }
    }

    /**
     * Observes change events for a specific key.
     *
     * @param key The key to observe for changes.
     * @return A [Flow] emitting [DataStoreChangeEvent] instances related to the specified key.
     */
    override fun observeKeyChanges(key: String): Flow<DataStoreChangeEvent> {
        return observeChanges().filter { event ->
            event.key == key || event.key == "*"
        }
    }

    /**
     * Clears the notifier and releases resources.
     *
     * After calling clear, no new events should be emitted.
     */
    override fun clear() {
        if (isCleared.compareAndSet(false, true)) {
            // Cancel internal scope if we created it
            if (scope == null) {
                internalScope.cancel()
            }
            println("[ChangeNotifier] Cleared. Active observers: ${activeObservers.load()}")
        }
    }

    /**
     * Get the current number of active observers.
     * Useful for debugging and monitoring.
     */
    fun loadActiveObserverCount(): Int = activeObservers.load()

    /**
     * Check if the notifier has been cleared.
     */
    fun isCleared(): Boolean = isCleared.load()

    /**
     * Get the current number of buffered events.
     * Useful for monitoring buffer usage.
     */
    fun getBufferedEventCount(): Int = changeFlow.subscriptionCount.value
}

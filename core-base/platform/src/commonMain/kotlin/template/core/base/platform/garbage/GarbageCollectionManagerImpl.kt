/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.garbage

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("UnusedPrivateProperty")
class GarbageCollectionManagerImpl(
    private val dispatcher: CoroutineDispatcher,
    private val collector: () -> Unit = garbageCollector,
) : GarbageCollectionManager {
    private val unconfinedScope = CoroutineScope(dispatcher)
    private var collectionJob: Job = Job().apply { complete() }

    override fun tryCollect() {
        collectionJob.cancel()
        collectionJob = unconfinedScope.launch {
            delay(timeMillis = GARBAGE_COLLECTION_INITIAL_DELAY_MS)
            repeat(times = GARBAGE_COLLECTION_ATTEMPTS) {
                delay(timeMillis = GARBAGE_COLLECTION_BASE_BACKOFF_MS * it)
                garbageCollector()
            }
        }
    }
}

private const val GARBAGE_COLLECTION_ATTEMPTS: Int = 10

/**
 * The base delay, in milliseconds, between a garbage collection attempt. The duration will be
 * multiplied by the number of attempts made thus far.
 */
private const val GARBAGE_COLLECTION_BASE_BACKOFF_MS: Long = 100L

/**
 * The initial delay, in milliseconds, before the first garbage collection attempt.
 */
private const val GARBAGE_COLLECTION_INITIAL_DELAY_MS: Long = 100L

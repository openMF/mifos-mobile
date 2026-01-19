/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.analytics

import kotlin.time.Clock

/** Performance tracking utilities for analytics */

/** Performance tracker that automatically logs performance metrics */
class PerformanceTracker(
    private val analytics: AnalyticsHelper,
    private val enableAutomaticLogging: Boolean = true,
    private val slowThresholdMs: Long = 1000L,
    private val verySlowThresholdMs: Long = 5000L,
) {

    private val activeTimers = mutableMapOf<String, Long>()
    private val performanceMetrics = mutableMapOf<String, MutableList<Long>>()

    /** Start timing an operation */
    fun startTimer(operationName: String, context: Map<String, String> = emptyMap()): String {
        val timerId = "${operationName}_$currentTime"
        activeTimers[timerId] = currentTime

        if (enableAutomaticLogging) {
            analytics.logEvent(
                "performance_timer_started",
                mapOf("operation" to operationName, "timer_id" to timerId) + context,
            )
        }

        return timerId
    }

    /** Stop timing an operation and log the result */
    fun stopTimer(
        timerId: String,
        success: Boolean = true,
        additionalContext: Map<String, String> = emptyMap(),
    ): Long? {
        val startTime = activeTimers.remove(timerId) ?: return null
        val duration = currentTime - startTime

        // Extract operation name from timer ID
        val operationName = timerId.substringBeforeLast("_")

        // Store metric for analysis
        performanceMetrics.getOrPut(operationName) { mutableListOf() }.add(duration)

        if (enableAutomaticLogging) {
            val performanceLevel = when {
                duration > verySlowThresholdMs -> "very_slow"
                duration > slowThresholdMs -> "slow"
                else -> "normal"
            }

            analytics.logEvent(
                "performance_timer_stopped",
                mapOf(
                    "operation" to operationName,
                    "timer_id" to timerId,
                    ParamKeys.DURATION to "${duration}ms",
                    ParamKeys.SUCCESS to success.toString(),
                    "performance_level" to performanceLevel,
                ) + additionalContext,
            )
        }

        return duration
    }

    /** Time a suspend function execution */
    suspend inline fun <T> timeOperation(
        operationName: String,
        context: Map<String, String> = emptyMap(),
        crossinline operation: suspend () -> T,
    ): T {
        val timerId = startTimer(operationName, context)
        return try {
            val result = operation()
            stopTimer(timerId, success = true)
            result
        } catch (e: Exception) {
            stopTimer(
                timerId,
                success = false,
                mapOf(ParamKeys.ERROR_MESSAGE to (e.message ?: "Unknown error")),
            )
            throw e
        }
    }

    /** Time a regular function execution */
    inline fun <T> timeOperationSync(
        operationName: String,
        context: Map<String, String> = emptyMap(),
        operation: () -> T,
    ): T {
        val timerId = startTimer(operationName, context)
        return try {
            val result = operation()
            stopTimer(timerId, success = true)
            result
        } catch (e: Exception) {
            stopTimer(
                timerId,
                success = false,
                mapOf(ParamKeys.ERROR_MESSAGE to (e.message ?: "Unknown error")),
            )
            throw e
        }
    }

    /** Get performance statistics for an operation */
    @Suppress("ReturnCount")
    fun getPerformanceStats(operationName: String): PerformanceStats? {
        val durations = performanceMetrics[operationName] ?: return null
        if (durations.isEmpty()) return null

        val sorted = durations.sorted()
        return PerformanceStats(
            operationName = operationName,
            count = durations.size,
            averageMs = durations.average(),
            medianMs = sorted[sorted.size / 2].toDouble(),
            p95Ms = sorted[(sorted.size * 0.95).toInt().coerceAtMost(sorted.size - 1)].toDouble(),
            p99Ms = sorted[(sorted.size * 0.99).toInt().coerceAtMost(sorted.size - 1)].toDouble(),
            minMs = sorted.first().toDouble(),
            maxMs = sorted.last().toDouble(),
        )
    }

    /** Log performance summary for an operation */
    fun logPerformanceSummary(operationName: String) {
        val stats = getPerformanceStats(operationName) ?: return

        analytics.logEvent(
            "performance_summary",
            mapOf(
                "operation" to operationName,
                "count" to stats.count.toString(),
                "average_ms" to stats.averageMs.toInt().toString(),
                "median_ms" to stats.medianMs.toInt().toString(),
                "p95_ms" to stats.p95Ms.toInt().toString(),
                "p99_ms" to stats.p99Ms.toInt().toString(),
                "min_ms" to stats.minMs.toInt().toString(),
                "max_ms" to stats.maxMs.toInt().toString(),
            ),
        )
    }

    /** Clear performance metrics */
    fun clearMetrics() {
        performanceMetrics.clear()
        activeTimers.clear()
    }

    /** Get all active timers */
    fun getActiveTimers(): Map<String, Long> = activeTimers.toMap()
}

/** Performance statistics for an operation */
data class PerformanceStats(
    val operationName: String,
    val count: Int,
    val averageMs: Double,
    val medianMs: Double,
    val p95Ms: Double,
    val p99Ms: Double,
    val minMs: Double,
    val maxMs: Double,
)

/** App lifecycle performance tracker */
class AppLifecycleTracker(private val analytics: AnalyticsHelper) {

    private var appStartTime: Long? = null
    private var lastForegroundTime: Long? = null
    private var backgroundTime: Long? = null

    /** Mark app launch start */
    fun markAppLaunchStart() {
        appStartTime = currentTime
        analytics.logEvent(
            Types.APP_LAUNCH,
            mapOf("launch_start_time" to appStartTime.toString()),
        )
    }

    /** Mark app launch complete */
    fun markAppLaunchComplete() {
        val startTime = appStartTime ?: return
        val launchDuration = currentTime - startTime

        analytics.logEvent(
            "app_launch_completed",
            mapOf(
                "launch_duration_ms" to launchDuration.toString(),
                "launch_performance" to when {
                    launchDuration < 1000 -> "fast"
                    launchDuration < 3000 -> "normal"
                    else -> "slow"
                },
            ),
        )
    }

    /** Mark app going to background */
    fun markAppBackground() {
        backgroundTime = currentTime
        val foregroundTime = lastForegroundTime

        analytics.logEvent(
            Types.APP_BACKGROUND,
            if (foregroundTime != null) {
                mapOf("foreground_duration_ms" to (backgroundTime!! - foregroundTime).toString())
            } else {
                emptyMap()
            },
        )
    }

    /** Mark app coming to foreground */
    fun markAppForeground() {
        val currentTime = currentTime
        lastForegroundTime = currentTime
        val bgTime = backgroundTime

        analytics.logEvent(
            Types.APP_FOREGROUND,
            if (bgTime != null) {
                mapOf("background_duration_ms" to (currentTime - bgTime).toString())
            } else {
                emptyMap()
            },
        )
    }
}

/** Extension functions for AnalyticsHelper to add performance tracking */

/** Create a performance tracker */
fun AnalyticsHelper.performanceTracker(
    enableAutomaticLogging: Boolean = true,
    slowThresholdMs: Long = 1000L,
    verySlowThresholdMs: Long = 5000L,
): PerformanceTracker =
    PerformanceTracker(
        analytics = this,
        enableAutomaticLogging = enableAutomaticLogging,
        slowThresholdMs = slowThresholdMs,
        verySlowThresholdMs = verySlowThresholdMs,
    )

/** Create an app lifecycle tracker */
fun AnalyticsHelper.lifecycleTracker(): AppLifecycleTracker = AppLifecycleTracker(this)

private val currentTime = Clock.System.now().toEpochMilliseconds()

/** Quick performance timing for suspend functions */
suspend inline fun <T> AnalyticsHelper.timePerformance(
    operationName: String,
    context: Map<String, String> = emptyMap(),
    crossinline operation: suspend () -> T,
): T {
    return performanceTracker().timeOperation(operationName, context, operation)
}

/** Quick performance timing for regular functions */
inline fun <T> AnalyticsHelper.timePerformanceSync(
    operationName: String,
    context: Map<String, String> = emptyMap(),
    operation: () -> T,
): T {
    return performanceTracker().timeOperationSync(operationName, context, operation)
}

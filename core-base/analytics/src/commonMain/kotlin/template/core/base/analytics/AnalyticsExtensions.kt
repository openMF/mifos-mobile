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
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Extension functions for enhanced analytics functionality
 */

/**
 * Create a screen view event with builder pattern
 */
fun AnalyticsEvent.screenView(
    screenName: String,
    sourceScreen: String? = null,
    additionalParams: Map<String, String> = emptyMap(),
): AnalyticsEvent {
    val params = mutableListOf(Param(ParamKeys.SCREEN_NAME, screenName))
    sourceScreen?.let { params.add(Param(ParamKeys.SOURCE_SCREEN, it)) }
    additionalParams.forEach { (key, value) -> params.add(Param(key, value)) }
    return AnalyticsEvent(Types.SCREEN_VIEW, params)
}

/**
 * Create a button click event with builder pattern
 */
fun AnalyticsEvent.buttonClick(
    buttonName: String,
    screenName: String? = null,
    elementId: String? = null,
): AnalyticsEvent {
    val params = mutableListOf(Param(ParamKeys.BUTTON_NAME, buttonName))
    screenName?.let { params.add(Param(ParamKeys.SCREEN_NAME, it)) }
    elementId?.let { params.add(Param(ParamKeys.ELEMENT_ID, it)) }
    return AnalyticsEvent(Types.BUTTON_CLICK, params)
}

/**
 * Create an error event with builder pattern
 */
fun AnalyticsEvent.error(
    message: String,
    errorCode: String? = null,
    screen: String? = null,
    apiEndpoint: String? = null,
): AnalyticsEvent {
    val params = mutableListOf(Param(ParamKeys.ERROR_MESSAGE, message))
    errorCode?.let { params.add(Param(ParamKeys.ERROR_CODE, it)) }
    screen?.let { params.add(Param(ParamKeys.SCREEN_NAME, it)) }
    apiEndpoint?.let { params.add(Param(ParamKeys.API_ENDPOINT, it)) }
    return AnalyticsEvent(Types.ERROR_OCCURRED, params)
}

/**
 * Create a search event with builder pattern
 */
fun AnalyticsEvent.search(
    searchTerm: String,
    resultCount: Int? = null,
    screen: String? = null,
): AnalyticsEvent {
    val params = mutableListOf(Param(ParamKeys.SEARCH_TERM, searchTerm))
    resultCount?.let { params.add(Param(ParamKeys.RESULT_COUNT, it.toString())) }
    screen?.let { params.add(Param(ParamKeys.SCREEN_NAME, it)) }
    return AnalyticsEvent(Types.SEARCH_PERFORMED, params)
}

/**
 * Create a form event with builder pattern
 */
fun AnalyticsEvent.formEvent(
    // FORM_STARTED, FORM_COMPLETED, FORM_ABANDONED
    eventType: String,
    formName: String,
    completionTime: Duration? = null,
    fieldName: String? = null,
): AnalyticsEvent {
    val params = mutableListOf(Param(ParamKeys.FORM_NAME, formName))
    completionTime?.let { params.add(Param(ParamKeys.COMPLETION_TIME, "${it.toDouble(DurationUnit.SECONDS)}s")) }
    fieldName?.let { params.add(Param(ParamKeys.FIELD_NAME, it)) }
    return AnalyticsEvent(eventType, params)
}

/**
 * Create a loading time event
 */
fun AnalyticsEvent.loadingTime(
    screen: String,
    loadingTimeMs: Long,
    success: Boolean = true,
): AnalyticsEvent {
    val params = listOf(
        Param(ParamKeys.SCREEN_NAME, screen),
        Param(ParamKeys.LOADING_TIME_MS, loadingTimeMs.toString()),
        Param(ParamKeys.SUCCESS, success.toString()),
    )
    return AnalyticsEvent(Types.LOADING_TIME, params)
}

/**
 * Extension functions for AnalyticsHelper to add timing functionality
 */
class TimedEvent internal constructor(
    private val analytics: AnalyticsHelper,
    private val eventType: String,
    private val baseParams: List<Param>,
) {
    private val startTime = Clock.System.now().toEpochMilliseconds()

    fun complete(additionalParams: Map<String, String> = emptyMap()) {
        val duration = Clock.System.now().toEpochMilliseconds() - startTime
        val params = baseParams +
            Param(ParamKeys.DURATION, duration.toString()) +
            additionalParams.map { Param(it.key, it.value) }
        analytics.logEvent(AnalyticsEvent(eventType, params))
    }
}

/**
 * Start timing an event - call complete() when done
 */
fun AnalyticsHelper.startTiming(eventType: String, vararg params: Pair<String, String>): TimedEvent {
    val baseParams = params.map { Param(it.first, it.second) }
    return TimedEvent(this, eventType, baseParams)
}

/**
 * Time a block of code execution
 */
inline fun <T> AnalyticsHelper.timeExecution(
    eventType: String,
    vararg params: Pair<String, String>,
    block: () -> T,
): T {
    val startTime = Clock.System.now().toEpochMilliseconds()
    return try {
        val result = block()
        val duration = Clock.System.now().toEpochMilliseconds() - startTime
        logEvent(
            eventType,
            *params,
            ParamKeys.DURATION to duration.toString(),
            ParamKeys.SUCCESS to "true",
        )
        result
    } catch (e: Exception) {
        val duration = Clock.System.now().toEpochMilliseconds() - startTime
        logEvent(
            eventType,
            *params,
            ParamKeys.DURATION to duration.toString(),
            ParamKeys.SUCCESS to "false",
            ParamKeys.ERROR_MESSAGE to (e.message ?: "Unknown error"),
        )
        throw e
    }
}

/**
 * Batch analytics events for better performance
 */
class AnalyticsBatch internal constructor(private val analytics: AnalyticsHelper) {
    private val events = mutableListOf<AnalyticsEvent>()

    fun add(event: AnalyticsEvent): AnalyticsBatch {
        events.add(event)
        return this
    }

    fun add(type: String, vararg params: Pair<String, String>): AnalyticsBatch {
        events.add(AnalyticsEvent(type, params.map { Param(it.first, it.second) }))
        return this
    }

    fun flush() {
        events.forEach { analytics.logEvent(it) }
        events.clear()
    }
}

/**
 * Create a batch for logging multiple events efficiently
 */
fun AnalyticsHelper.batch(): AnalyticsBatch = AnalyticsBatch(this)

/**
 * Safe parameter creation that handles validation
 */
@Suppress("ReturnCount")
fun createParam(key: String, value: Any?): Param? {
    return try {
        val stringValue = value?.toString() ?: return null
        if (key.isBlank() || stringValue.isBlank()) return null
        Param(key.take(40), stringValue.take(100))
    } catch (e: Exception) {
        null // Return null for invalid parameters
    }
}

/**
 * Create parameters from a map, filtering out invalid ones
 */
fun createParams(params: Map<String, Any?>): List<Param> {
    return params.mapNotNull { (key, value) -> createParam(key, value) }
}

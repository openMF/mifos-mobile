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

/** Testing utilities for analytics functionality */

/**
 * Test implementation of AnalyticsHelper that captures events for
 * verification
 */
class TestAnalyticsHelper : AnalyticsHelper {
    private val _loggedEvents = mutableListOf<AnalyticsEvent>()
    private val _userProperties = mutableMapOf<String, String>()
    private var _userId: String? = null

    /** Get all logged events */
    val loggedEvents: List<AnalyticsEvent> get() = _loggedEvents.toList()

    /** Get all set user properties */
    val userProperties: Map<String, String> get() = _userProperties.toMap()

    /** Get the current user ID */
    val userId: String? get() = _userId

    override fun logEvent(event: AnalyticsEvent) {
        _loggedEvents.add(event)
    }

    override fun setUserProperty(name: String, value: String) {
        _userProperties[name] = value
    }

    override fun setUserId(userId: String) {
        _userId = userId
    }

    /** Clear all captured data */
    fun clear() {
        _loggedEvents.clear()
        _userProperties.clear()
        _userId = null
    }

    /** Get events by type */
    fun getEventsByType(type: String): List<AnalyticsEvent> {
        return _loggedEvents.filter { it.type == type }
    }

    /** Get the last logged event */
    fun getLastEvent(): AnalyticsEvent? = _loggedEvents.lastOrNull()

    /** Get events containing a specific parameter */
    fun getEventsWithParam(key: String, value: String? = null): List<AnalyticsEvent> {
        return _loggedEvents.filter { event ->
            event.extras.any { param ->
                param.key == key && (value == null || param.value == value)
            }
        }
    }

    /** Verify that an event was logged */
    fun hasEvent(type: String, params: Map<String, String> = emptyMap()): Boolean {
        return _loggedEvents.any { event ->
            event.type == type && params.all { (key, value) ->
                event.extras.any { it.key == key && it.value == value }
            }
        }
    }

    /** Get count of events by type */
    fun getEventCount(type: String): Int {
        return _loggedEvents.count { it.type == type }
    }

    /** Get all unique event types logged */
    fun getUniqueEventTypes(): Set<String> {
        return _loggedEvents.map { it.type }.toSet()
    }

    /** Verify screen view was logged */
    fun hasScreenView(screenName: String): Boolean {
        return hasEvent(Types.SCREEN_VIEW, mapOf(ParamKeys.SCREEN_NAME to screenName))
    }

    /** Verify button click was logged */
    fun hasButtonClick(buttonName: String): Boolean {
        return hasEvent(Types.BUTTON_CLICK, mapOf(ParamKeys.BUTTON_NAME to buttonName))
    }

    /** Verify error was logged */
    fun hasError(errorMessage: String): Boolean {
        return hasEvent(Types.ERROR_OCCURRED, mapOf(ParamKeys.ERROR_MESSAGE to errorMessage))
    }

    /** Get all parameters for a specific event type */
    fun getParametersForEventType(type: String): List<Map<String, String>> {
        return _loggedEvents.filter { it.type == type }
            .map { event -> event.extras.associate { it.key to it.value } }
    }

    /** Print all logged events (useful for debugging) */
    fun printEvents() {
        if (_loggedEvents.isEmpty()) {
            println("No analytics events logged")
            return
        }

        println("Analytics Events Logged:")
        _loggedEvents.forEachIndexed { index, event ->
            println("${index + 1}. ${event.type}")
            event.extras.forEach { param ->
                println("   ${param.key}: ${param.value}")
            }
        }

        if (_userProperties.isNotEmpty()) {
            println("\nUser Properties:")
            _userProperties.forEach { (key, value) ->
                println("   $key: $value")
            }
        }

        _userId?.let {
            println("\nUser ID: $it")
        }
    }
}

/** Create a test analytics helper for testing */
fun createTestAnalyticsHelper(): TestAnalyticsHelper = TestAnalyticsHelper()

/** Extension for asserting events in tests */
fun TestAnalyticsHelper.assertEventLogged(
    type: String,
    params: Map<String, String> = emptyMap(),
    message: String? = null,
) {
    val found = hasEvent(type, params)
    if (!found) {
        val errorMessage = message ?: "Expected event '$type' with params $params was not logged"
        val actualEvents = getEventsByType(type)
        if (actualEvents.isEmpty()) {
            throw AssertionError("$errorMessage. No events of type '$type' were logged.")
        } else {
            throw AssertionError(
                "$errorMessage. Events of type '$type' found: ${
                    actualEvents.map {
                        it.extras.associate { p -> p.key to p.value }
                    }
                }",
            )
        }
    }
}

/** Extension for asserting event count */
fun TestAnalyticsHelper.assertEventCount(
    type: String,
    expectedCount: Int,
    message: String? = null,
) {
    val actualCount = getEventCount(type)
    if (actualCount != expectedCount) {
        val errorMessage =
            message ?: "Expected $expectedCount events of type '$type', but found $actualCount"
        throw AssertionError(errorMessage)
    }
}

/** Extension for asserting user property was set */
fun TestAnalyticsHelper.assertUserProperty(
    name: String,
    expectedValue: String,
    message: String? = null,
) {
    val actualValue = userProperties[name]
    if (actualValue != expectedValue) {
        val errorMessage = message
            ?: "Expected user property '$name' to be '$expectedValue', but was '$actualValue'"
        throw AssertionError(errorMessage)
    }
}

/** Mock analytics helper that simulates network delays and failures */
class MockAnalyticsHelper(
    private val simulateFailures: Boolean = false,
    private val failureRate: Float = 0.1f,
) : AnalyticsHelper {

    private val testHelper = TestAnalyticsHelper()
    private var eventCount = 0

    override fun logEvent(event: AnalyticsEvent) {
        eventCount++

        if (simulateFailures && (eventCount * failureRate).toInt() > 0 && eventCount % (1 / failureRate).toInt() == 0) {
            // Simulate failure - don't log the event
            return
        }

//        if (simulateNetworkDelay) {
//            // Simulate network delay (in a real implementation, this would be async)
//            delay((50..200).random().toLong())
//        }

        testHelper.logEvent(event)
    }

    override fun setUserProperty(name: String, value: String) {
        testHelper.setUserProperty(name, value)
    }

    override fun setUserId(userId: String) {
        testHelper.setUserId(userId)
    }

    // Delegate test helper methods
    val loggedEvents: List<AnalyticsEvent> get() = testHelper.loggedEvents
    val userProperties: Map<String, String> get() = testHelper.userProperties
    val userId: String? get() = testHelper.userId

    fun clear() = testHelper.clear()
    fun hasEvent(type: String, params: Map<String, String> = emptyMap()) =
        testHelper.hasEvent(type, params)

    fun getEventCount(type: String) = testHelper.getEventCount(type)
}

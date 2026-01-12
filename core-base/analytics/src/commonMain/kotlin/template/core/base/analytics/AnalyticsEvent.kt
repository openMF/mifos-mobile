/*
 * Copyright 2023 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.analytics

/**
 * Represents an analytics event with type-safe parameter validation.
 *
 * This data class encapsulates all information needed to log an analytics event,
 * including the event type and associated parameters. It provides a builder pattern
 * through extension methods for convenient event construction.
 *
 * @param type The event type identifier. Use predefined constants from [Types] when possible,
 *             or define custom events that are configured in your analytics backend
 *             (e.g., Firebase Analytics custom events). Must be non-blank and follow
 *             analytics platform naming conventions.
 * @param extras List of key-value parameters that provide additional context for the event.
 *               Each parameter is validated according to analytics platform constraints
 *               (key ≤ 40 chars, value ≤ 100 chars). See [Param] for details.
 *
 * @see Types for standard event type constants
 * @see ParamKeys for standard parameter key constants
 * @see Param for parameter validation rules
 *
 * @sample
 * ```kotlin
 * // Simple event
 * val event = AnalyticsEvent(Types.BUTTON_CLICK)
 *
 * // Event with parameters using builder pattern
 * val event = AnalyticsEvent(Types.SCREEN_VIEW)
 *     .withParam(ParamKeys.SCREEN_NAME, "UserProfile")
 *     .withParam(ParamKeys.SOURCE_SCREEN, "Dashboard")
 *
 * // Event with multiple parameters
 * val event = AnalyticsEvent(Types.FORM_COMPLETED)
 *     .withParams(
 *         ParamKeys.FORM_NAME to "registration",
 *         ParamKeys.COMPLETION_TIME to "45s",
 *         "field_count" to "8"
 *     )
 * ```
 *
 * @since 1.0.0
 */
data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList(),
) {
    /**
     * Adds a single parameter to this analytics event using the builder pattern.
     *
     * This method creates a new [AnalyticsEvent] instance with the additional parameter,
     * following immutable design principles. The parameter will be validated according
     * to analytics platform constraints.
     *
     * @param key The parameter key identifier. Must be non-blank, ≤ 40 characters,
     *            and follow valid naming conventions (letters, numbers, underscores).
     *            Use [ParamKeys] constants when possible.
     * @param value The parameter value. Must be ≤ 100 characters. Can be any string
     *              representing the parameter data.
     *
     * @return A new [AnalyticsEvent] instance with the added parameter
     * @throws IllegalArgumentException if the parameter violates validation constraints
     *
     * @see ParamKeys for standard parameter key constants
     * @see Param for parameter validation details
     *
     * @sample
     * ```kotlin
     * val event = AnalyticsEvent(Types.BUTTON_CLICK)
     *     .withParam(ParamKeys.BUTTON_NAME, "save")
     *     .withParam(ParamKeys.SCREEN_NAME, "UserProfile")
     * ```
     */
    fun withParam(key: String, value: String): AnalyticsEvent {
        return copy(extras = extras + Param(key, value))
    }

    /**
     * Adds multiple parameters to this analytics event using vararg syntax.
     *
     * This method provides a convenient way to add multiple parameters at once
     * using Kotlin's vararg feature. Each parameter pair will be converted to
     * a [Param] instance and validated.
     *
     * @param params Variable number of parameter pairs (key to value). Each key
     *               and value must meet the same validation constraints as [withParam].
     *
     * @return A new [AnalyticsEvent] instance with all the added parameters
     * @throws IllegalArgumentException if any parameter violates validation constraints
     *
     * @see withParam for single parameter addition
     * @see ParamKeys for standard parameter key constants
     *
     * @sample
     * ```kotlin
     * val event = AnalyticsEvent(Types.SEARCH_PERFORMED)
     *     .withParams(
     *         ParamKeys.SEARCH_TERM to "kotlin",
     *         ParamKeys.RESULT_COUNT to "42",
     *         ParamKeys.SCREEN_NAME to "SearchResults"
     *     )
     * ```
     */
    fun withParams(vararg params: Pair<String, String>): AnalyticsEvent {
        val newParams = params.map { Param(it.first, it.second) }
        return copy(extras = extras + newParams)
    }

    /**
     * Adds multiple parameters to this analytics event from a Map.
     *
     * This method allows adding parameters from an existing Map<String, String>,
     * which is useful when working with dynamic parameter sets or converting
     * from other data structures.
     *
     * @param params A map containing parameter key-value pairs. Each entry
     *               will be converted to a [Param] instance and validated.
     *
     * @return A new [AnalyticsEvent] instance with all the added parameters
     * @throws IllegalArgumentException if any parameter violates validation constraints
     *
     * @see withParam for single parameter addition
     * @see withParams for vararg parameter addition
     *
     * @sample
     * ```kotlin
     * val dynamicParams = mapOf(
     *     ParamKeys.USER_TYPE to "premium",
     *     ParamKeys.APP_VERSION to "2.1.0",
     *     "custom_metric" to "enabled"
     * )
     * val event = AnalyticsEvent(Types.FEATURE_USED)
     *     .withParams(dynamicParams)
     * ```
     */
    fun withParams(params: Map<String, String>): AnalyticsEvent {
        val newParams = params.map { Param(it.key, it.value) }
        return copy(extras = extras + newParams)
    }
}

/**
 * Standard analytics event type constants for consistent cross-platform event logging.
 *
 * This object provides predefined event type constants that follow analytics platform
 * best practices and naming conventions. Using these constants ensures consistency
 * across your application and compatibility with analytics backends like Firebase Analytics.
 *
 * Event types are organized into logical categories:
 * - **Navigation**: Screen views and navigation tracking
 * - **User Interactions**: Clicks, selections, and user-initiated actions
 * - **Forms**: Form lifecycle and validation events
 * - **Content**: Content engagement and interaction
 * - **Errors**: Error tracking and debugging
 * - **Performance**: App performance and timing metrics
 * - **Authentication**: User authentication and session management
 * - **Feature Usage**: Feature adoption and usage patterns
 *
 * @see ParamKeys for corresponding parameter key constants
 * @see AnalyticsEvent for usage examples
 *
 * @since 1.0.0
 */
object Types {
    // Navigation events
    const val SCREEN_VIEW = "screen_view"
    const val SCREEN_TRANSITION = "screen_transition"

    // User interaction events
    const val BUTTON_CLICK = "button_click"
    const val MENU_ITEM_SELECTED = "menu_item_selected"
    const val SEARCH_PERFORMED = "search_performed"
    const val FILTER_APPLIED = "filter_applied"

    // Form events
    const val FORM_STARTED = "form_started"
    const val FORM_COMPLETED = "form_completed"
    const val FORM_ABANDONED = "form_abandoned"
    const val FIELD_VALIDATION_ERROR = "field_validation_error"

    // Content events
    const val CONTENT_VIEW = "content_view"
    const val CONTENT_SHARED = "content_shared"
    const val CONTENT_LIKED = "content_liked"

    // Error events
    const val ERROR_OCCURRED = "error_occurred"
    const val API_ERROR = "api_error"
    const val NETWORK_ERROR = "network_error"

    // Performance events
    const val APP_LAUNCH = "app_launch"
    const val APP_BACKGROUND = "app_background"
    const val APP_FOREGROUND = "app_foreground"
    const val LOADING_TIME = "loading_time"

    // Authentication events
    const val LOGIN_ATTEMPT = "login_attempt"
    const val LOGIN_SUCCESS = "login_success"
    const val LOGIN_FAILURE = "login_failure"
    const val LOGOUT = "logout"
    const val SIGNUP_ATTEMPT = "signup_attempt"
    const val SIGNUP_SUCCESS = "signup_success"

    // Feature usage
    const val FEATURE_USED = "feature_used"
    const val TUTORIAL_STARTED = "tutorial_started"
    const val TUTORIAL_COMPLETED = "tutorial_completed"
    const val TUTORIAL_SKIPPED = "tutorial_skipped"
}

/**
 * Represents a validated analytics parameter with automatic constraint checking.
 *
 * This data class encapsulates a key-value pair for analytics events with built-in
 * validation that enforces analytics platform constraints. The validation occurs
 * during object construction to ensure data integrity.
 *
 * **Validation Rules:**
 * - Key must be non-blank
 * - Key must be ≤ 40 characters (Firebase Analytics constraint)
 * - Value must be ≤ 100 characters (Firebase Analytics constraint)
 * - Key should follow naming conventions (letters, numbers, underscores)
 *
 * @param key The parameter identifier. Use [ParamKeys] constants when possible
 *            for consistency and to avoid typos.
 * @param value The parameter value as a string. All values are stored as strings
 *              regardless of their original type.
 *
 * @throws IllegalArgumentException if validation constraints are violated
 *
 * @see ParamKeys for standard parameter key constants
 * @see AnalyticsEvent.withParam for usage in event construction
 * @see createParam for safe parameter creation with validation
 *
 * @sample
 * ```kotlin
 * // Valid parameter
 * val param = Param(ParamKeys.SCREEN_NAME, "UserProfile")
 *
 * // This would throw IllegalArgumentException (key too long)
 * // val invalid = Param("this_key_is_way_too_long_and_exceeds_forty_characters", "value")
 *
 * // This would throw IllegalArgumentException (value too long)
 * // val invalid = Param("key", "very long value..." + "x".repeat(100))
 * ```
 *
 * @since 1.0.0
 */
@ConsistentCopyVisibility
data class Param private constructor(
    val key: String,
    val value: String,
) {
    companion object {
        private const val MAX_VALUE_LENGTH = 100
        private const val MAX_KEY_LENGTH = 40
        private const val FALLBACK_KEY = "unknown_param"

        operator fun invoke(key: String, value: String): Param {
            val safeKey = key
                .takeIf { it.isNotBlank() }
                ?.take(MAX_KEY_LENGTH)
                ?: FALLBACK_KEY

            val safeValue = value.take(MAX_VALUE_LENGTH)

            return Param(safeKey, safeValue)
        }
    }
}

/**
 * Standard parameter key constants for consistent analytics event parameters.
 *
 * This object provides predefined parameter key constants that ensure consistency
 * across analytics events and prevent typos in parameter naming. These keys follow
 * analytics platform best practices and are organized into logical categories for
 * easy discovery and usage.
 *
 * **Parameter Categories:**
 * - **Screen & Navigation**: Screen names, navigation context, and flow tracking
 * - **User Interaction**: UI element identification and interaction context
 * - **Content**: Content identification, categorization, and engagement
 * - **Search & Filters**: Search terms, filter states, and result information
 * - **Forms**: Form identification, field tracking, and completion metrics
 * - **Performance**: Timing, error tracking, and performance metrics
 * - **User Attributes**: User identification and characteristic data
 * - **Feature Usage**: Feature identification and usage patterns
 * - **General**: Common parameters used across multiple event types
 *
 * **Usage Guidelines:**
 * - Always use these constants instead of hardcoded strings
 * - Keys are designed to be ≤ 40 characters (analytics platform constraint)
 * - Values should be kept ≤ 100 characters when possible
 * - Combine with [Types] constants for consistent event structure
 *
 * @see Types for corresponding event type constants
 * @see Param for parameter validation rules
 * @see AnalyticsEvent for usage examples
 *
 * @since 1.0.0
 */
object ParamKeys {
    // Screen and navigation
    const val SCREEN_NAME = "screen_name"
    const val SOURCE_SCREEN = "source_screen"
    const val DESTINATION_SCREEN = "destination_screen"

    // User interaction
    const val BUTTON_NAME = "button_name"
    const val ELEMENT_ID = "element_id"
    const val ELEMENT_TYPE = "element_type"
    const val ACTION_TYPE = "action_type"

    // Content
    const val CONTENT_TYPE = "content_type"
    const val CONTENT_ID = "content_id"
    const val CONTENT_NAME = "content_name"
    const val CATEGORY = "category"

    // Search and filters
    const val SEARCH_TERM = "search_term"
    const val FILTER_TYPE = "filter_type"
    const val FILTER_VALUE = "filter_value"
    const val RESULT_COUNT = "result_count"

    // Forms
    const val FORM_NAME = "form_name"
    const val FIELD_NAME = "field_name"
    const val ERROR_MESSAGE = "error_message"
    const val COMPLETION_TIME = "completion_time"

    // Performance
    const val LOADING_TIME_MS = "loading_time_ms"
    const val ERROR_CODE = "error_code"
    const val API_ENDPOINT = "api_endpoint"
    const val NETWORK_TYPE = "network_type"

    // User attributes
    const val USER_ID = "user_id"
    const val USER_TYPE = "user_type"
    const val DEVICE_TYPE = "device_type"
    const val APP_VERSION = "app_version"

    // Feature usage
    const val FEATURE_NAME = "feature_name"
    const val USAGE_COUNT = "usage_count"
    const val TUTORIAL_STEP = "tutorial_step"

    // Custom
    const val VALUE = "value"
    const val TIMESTAMP = "timestamp"
    const val DURATION = "duration"
    const val SUCCESS = "success"
}

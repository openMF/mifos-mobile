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
 * Platform-agnostic interface for logging analytics events with comprehensive utility methods.
 *
 * This interface provides the core contract for analytics tracking across all platforms
 * in a Kotlin Multiplatform project. It abstracts the underlying analytics implementation
 * (Firebase Analytics, custom backends, etc.) and provides convenient methods for common
 * analytics operations.
 *
 * **Key Features:**
 * - **Type-Safe Event Logging**: Uses [AnalyticsEvent] for validated event structure
 * - **Convenience Methods**: Simplified APIs for common events (screen views, button clicks, errors)
 * - **User Management**: Support for user properties and user ID tracking
 * - **Platform Abstraction**: Works seamlessly across Android, iOS, Desktop, and Web
 * - **Flexible Parameter Handling**: Multiple ways to add event parameters
 *
 * **Available Implementations:**
 * - [FirebaseAnalyticsHelper]: Production implementation using Firebase Analytics
 * - [StubAnalyticsHelper]: Development implementation that logs to console
 * - [NoOpAnalyticsHelper]: No-operation implementation for testing/previews
 * - [TestAnalyticsHelper]: Test implementation that captures events for verification
 *
 * **Usage Patterns:**
 * ```kotlin
 * // Dependency injection (Koin)
 * val analytics: AnalyticsHelper = koinInject()
 *
 * // Simple event logging
 * analytics.logEvent(Types.BUTTON_CLICK, ParamKeys.BUTTON_NAME to "save")
 *
 * // Complex event with builder pattern
 * val event = AnalyticsEvent(Types.FORM_COMPLETED)
 *     .withParam(ParamKeys.FORM_NAME, "registration")
 *     .withParam(ParamKeys.COMPLETION_TIME, "45s")
 * analytics.logEvent(event)
 *
 * // Convenience methods
 * analytics.logScreenView("UserProfile", sourceScreen = "Dashboard")
 * analytics.logButtonClick("edit_profile", screenName = "UserProfile")
 * analytics.logError("Network timeout", "NET_001", "UserProfile")
 * ```
 *
 * @see AnalyticsEvent for event structure and validation
 * @see Types for standard event type constants
 * @see ParamKeys for standard parameter key constants
 * @see FirebaseAnalyticsHelper for production implementation
 * @see StubAnalyticsHelper for development implementation
 * @see TestAnalyticsHelper for testing implementation
 *
 * @since 1.0.0
 */
interface AnalyticsHelper {
    /**
     * Logs an analytics event to the underlying analytics platform.
     *
     * This is the core method that all other logging methods ultimately call.
     * The event will be validated and sent to the configured analytics backend
     * (e.g., Firebase Analytics, custom analytics service).
     *
     * @param event The [AnalyticsEvent] to log. Must have a valid event type and
     *              parameters that meet platform constraints.
     *
     * @see AnalyticsEvent for event construction and validation
     * @see Types for standard event types
     * @see ParamKeys for standard parameter keys
     *
     * @since 1.0.0
     */
    fun logEvent(event: AnalyticsEvent)

    /**
     * Logs a simple analytics event with type and optional parameters using vararg syntax.
     *
     * This convenience method provides a more concise way to log events without
     * explicitly creating an [AnalyticsEvent] instance. The parameters will be
     * automatically converted to [Param] instances and validated.
     *
     * @param type The event type identifier. Use constants from [Types] when possible.
     * @param params Variable number of parameter pairs (key to value). Each parameter
     *               must meet the same validation constraints as [Param].
     *
     * @throws IllegalArgumentException if the event type or any parameter violates
     *                                  validation constraints
     *
     * @see Types for standard event type constants
     * @see ParamKeys for standard parameter key constants
     *
     * @sample
     * ```kotlin
     * analytics.logEvent(Types.BUTTON_CLICK,
     *     ParamKeys.BUTTON_NAME to "save",
     *     ParamKeys.SCREEN_NAME to "UserProfile"
     * )
     * ```
     *
     * @since 1.0.0
     */
    fun logEvent(type: String, vararg params: Pair<String, String>) {
        val event = AnalyticsEvent(type, params.map { Param(it.first, it.second) })
        logEvent(event)
    }

    /**
     * Logs a simple analytics event with type and parameters from a Map.
     *
     * This convenience method allows logging events with parameters from an existing
     * Map<String, String>, which is useful when working with dynamic parameter sets
     * or converting from other data structures.
     *
     * @param type The event type identifier. Use constants from [Types] when possible.
     * @param params A map containing parameter key-value pairs. Each entry will be
     *               converted to a [Param] instance and validated.
     *
     * @throws IllegalArgumentException if the event type or any parameter violates
     *                                  validation constraints
     *
     * @see Types for standard event type constants
     * @see ParamKeys for standard parameter key constants
     *
     * @sample
     * ```kotlin
     * val eventParams = mapOf(
     *     ParamKeys.SEARCH_TERM to "kotlin",
     *     ParamKeys.RESULT_COUNT to "42"
     * )
     * analytics.logEvent(Types.SEARCH_PERFORMED, eventParams)
     * ```
     *
     * @since 1.0.0
     */
    fun logEvent(type: String, params: Map<String, String>) {
        val event = AnalyticsEvent(type, params.map { Param(it.key, it.value) })
        logEvent(event)
    }

    /**
     * Logs a screen view event for navigation tracking.
     *
     * This convenience method automatically creates a properly formatted screen view
     * event, which is essential for understanding user navigation patterns and
     * screen engagement metrics.
     *
     * @param screenName The name/identifier of the screen being viewed. Should be
     *                   descriptive and consistent across the app (e.g., "UserProfile",
     *                   "Settings", "ProductDetails").
     * @param sourceScreen Optional name of the previous screen that led to this view.
     *                     Useful for understanding navigation flows and user journeys.
     *
     * @see Types.SCREEN_VIEW for the generated event type
     * @see ParamKeys.SCREEN_NAME for the screen name parameter
     * @see ParamKeys.SOURCE_SCREEN for the source screen parameter
     *
     * @sample
     * ```kotlin
     * // Simple screen view
     * analytics.logScreenView("UserProfile")
     *
     * // Screen view with navigation context
     * analytics.logScreenView("ProductDetails", sourceScreen = "ProductList")
     * ```
     *
     * @since 1.0.0
     */
    fun logScreenView(screenName: String, sourceScreen: String? = null) {
        val params = mutableListOf(Param(ParamKeys.SCREEN_NAME, screenName))
        sourceScreen?.let { params.add(Param(ParamKeys.SOURCE_SCREEN, it)) }
        logEvent(AnalyticsEvent(Types.SCREEN_VIEW, params))
    }

    /**
     * Logs a button click event for user interaction tracking.
     *
     * This convenience method tracks user interactions with buttons and other
     * clickable elements, helping understand feature usage and user engagement
     * patterns.
     *
     * @param buttonName The identifier or label of the button clicked. Should be
     *                   descriptive and consistent (e.g., "save", "edit_profile",
     *                   "submit_form").
     * @param screenName Optional name of the screen where the button was clicked.
     *                   Provides context for understanding interaction patterns.
     *
     * @see Types.BUTTON_CLICK for the generated event type
     * @see ParamKeys.BUTTON_NAME for the button name parameter
     * @see ParamKeys.SCREEN_NAME for the screen name parameter
     *
     * @sample
     * ```kotlin
     * // Simple button click
     * analytics.logButtonClick("save")
     *
     * // Button click with screen context
     * analytics.logButtonClick("edit_profile", screenName = "UserProfile")
     * ```
     *
     * @since 1.0.0
     */
    fun logButtonClick(buttonName: String, screenName: String? = null) {
        val params = mutableListOf(Param(ParamKeys.BUTTON_NAME, buttonName))
        screenName?.let { params.add(Param(ParamKeys.SCREEN_NAME, it)) }
        logEvent(AnalyticsEvent(Types.BUTTON_CLICK, params))
    }

    /**
     * Logs an error event for debugging and monitoring.
     *
     * This convenience method tracks errors and exceptions that occur in the
     * application, providing valuable information for debugging, monitoring
     * app stability, and improving user experience.
     *
     * @param errorMessage A descriptive message about the error. Should be clear
     *                     and actionable for debugging purposes.
     * @param errorCode Optional error code or identifier that can help categorize
     *                  and track specific types of errors (e.g., "NET_001", "DB_ERROR").
     * @param screen Optional name of the screen where the error occurred. Helps
     *               identify problematic areas of the app.
     *
     * @see Types.ERROR_OCCURRED for the generated event type
     * @see ParamKeys.ERROR_MESSAGE for the error message parameter
     * @see ParamKeys.ERROR_CODE for the error code parameter
     * @see ParamKeys.SCREEN_NAME for the screen name parameter
     *
     * @sample
     * ```kotlin
     * // Simple error logging
     * analytics.logError("Network connection failed")
     *
     * // Error with code and context
     * analytics.logError(
     *     errorMessage = "API request timeout",
     *     errorCode = "NET_001",
     *     screen = "UserProfile"
     * )
     * ```
     *
     * @since 1.0.0
     */
    fun logError(errorMessage: String, errorCode: String? = null, screen: String? = null) {
        val params = mutableListOf(Param(ParamKeys.ERROR_MESSAGE, errorMessage))
        errorCode?.let { params.add(Param(ParamKeys.ERROR_CODE, it)) }
        screen?.let { params.add(Param(ParamKeys.SCREEN_NAME, it)) }
        logEvent(AnalyticsEvent(Types.ERROR_OCCURRED, params))
    }

    /**
     * Logs a feature usage event for feature adoption tracking.
     *
     * This convenience method tracks when users interact with specific features
     * or functionality, helping understand feature adoption, usage patterns,
     * and user engagement with different parts of the application.
     *
     * @param featureName The identifier of the feature being used. Should be
     *                    descriptive and consistent (e.g., "dark_mode", "export_data",
     *                    "voice_input").
     * @param screen Optional name of the screen where the feature was used.
     *               Provides context for understanding feature usage patterns.
     *
     * @see Types.FEATURE_USED for the generated event type
     * @see ParamKeys.FEATURE_NAME for the feature name parameter
     * @see ParamKeys.SCREEN_NAME for the screen name parameter
     *
     * @sample
     * ```kotlin
     * // Simple feature usage
     * analytics.logFeatureUsed("dark_mode")
     *
     * // Feature usage with screen context
     * analytics.logFeatureUsed("export_data", screen = "Settings")
     * ```
     *
     * @since 1.0.0
     */
    fun logFeatureUsed(featureName: String, screen: String? = null) {
        val params = mutableListOf(Param(ParamKeys.FEATURE_NAME, featureName))
        screen?.let { params.add(Param(ParamKeys.SCREEN_NAME, it)) }
        logEvent(AnalyticsEvent(Types.FEATURE_USED, params))
    }

    /**
     * Sets a user property for analytics user profiling and segmentation.
     *
     * User properties allow you to describe segments of your user base, such as
     * language preference, geographic location, or user type. These properties
     * are attached to all subsequent events and can be used for analytics
     * filtering and audience creation.
     *
     * **Note:** This is a default implementation that does nothing. Platform-specific
     * implementations may override this to provide actual functionality.
     *
     * @param name The property name identifier. Must be non-blank and ≤ 24 characters
     *             (Firebase Analytics constraint). Should use consistent naming
     *             conventions across the app.
     * @param value The property value. Must be ≤ 36 characters (Firebase Analytics
     *              constraint). Should be descriptive and useful for segmentation.
     *
     * @see setUserId for setting user identification
     *
     * @sample
     * ```kotlin
     * // Set user characteristics
     * analytics.setUserProperty("user_type", "premium")
     * analytics.setUserProperty("preferred_language", "en")
     * analytics.setUserProperty("app_theme", "dark")
     * ```
     *
     * @since 1.0.0
     */
    fun setUserProperty(name: String, value: String) {
        // Default implementation does nothing - can be overridden by implementations that support it
    }

    /**
     * Sets the user ID for analytics user tracking and identification.
     *
     * The user ID is a unique identifier for a user that persists across sessions
     * and devices. It enables you to connect user behavior across multiple sessions
     * and understand the user journey more comprehensively.
     *
     * **Note:** This is a default implementation that does nothing. Platform-specific
     * implementations may override this to provide actual functionality.
     *
     * **Privacy Considerations:**
     * - Ensure the user ID does not contain personally identifiable information (PII)
     * - Consider using hashed or obfuscated identifiers
     * - Follow privacy regulations and platform guidelines
     *
     * @param userId The unique identifier for the user. Must be non-blank and
     *               ≤ 256 characters (Firebase Analytics constraint). Should be
     *               consistent across sessions and not contain PII.
     *
     * @see setUserProperty for setting user characteristics
     *
     * @sample
     * ```kotlin
     * // Set user ID (use hashed or obfuscated IDs for privacy)
     * analytics.setUserId("user_${hashedUserId}")
     *
     * // Clear user ID on logout
     * analytics.setUserId("")
     * ```
     *
     * @since 1.0.0
     */
    fun setUserId(userId: String) {
        // Default implementation does nothing - can be overridden by implementations that support it
    }
}

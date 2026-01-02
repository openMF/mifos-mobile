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

import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.logEvent

/**
 * Production implementation of [AnalyticsHelper] that sends events to Firebase Analytics.
 *
 * This implementation provides a complete analytics solution using Firebase Analytics
 * as the backend service. It handles automatic parameter validation, length truncation
 * according to Firebase constraints, and provides full user tracking capabilities.
 *
 * **Features:**
 * - **Automatic Truncation**: Parameters are automatically truncated to Firebase limits
 * - **User Tracking**: Full support for user properties and user ID tracking
 * - **Cross-Platform**: Works on Android, iOS, and other supported Firebase platforms
 * - **Real-time Processing**: Events are sent to Firebase for real-time analytics
 * - **Integration**: Seamlessly integrates with Firebase Console and other Firebase services
 *
 * **Firebase Analytics Constraints:**
 * - Event names: ≤ 40 characters
 * - Parameter keys: ≤ 40 characters
 * - Parameter values: ≤ 100 characters
 * - User property names: ≤ 24 characters
 * - User property values: ≤ 36 characters
 * - Maximum parameters per event: 25
 *
 * **Setup Requirements:**
 * - Firebase project configured with Analytics enabled
 * - Platform-specific Firebase SDK configuration
 * - Google Services configuration files (google-services.json, GoogleService-Info.plist)
 *
 * @param firebaseAnalytics The Firebase Analytics instance to use for logging events.
 *                         This should be properly configured for the target platform.
 *
 * @see AnalyticsHelper for the complete interface contract
 * @see StubAnalyticsHelper for development/debugging implementation
 * @see NoOpAnalyticsHelper for no-operation implementation
 *
 * @sample
 * ```kotlin
 * // Typical DI setup for production builds
 * val analyticsModule = module {
 *     single<AnalyticsHelper> {
 *         FirebaseAnalyticsHelper(Firebase.analytics)
 *     }
 * }
 * ```
 *
 * @since 1.0.0
 */
internal class FirebaseAnalyticsHelper(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsHelper {

    /**
     * Logs an analytics event to Firebase Analytics with automatic parameter truncation.
     *
     * This method sends the event to Firebase Analytics, automatically truncating
     * parameter keys and values to meet Firebase's length constraints. The event
     * will appear in the Firebase Console within a few hours for standard events.
     *
     * @param event The analytics event to log. Parameters will be automatically
     *              truncated if they exceed Firebase limits.
     */
    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.type) {
            for (extra in event.extras) {
                // Truncate parameter keys and values according to firebase maximum length values.
                param(
                    key = extra.key.take(40),
                    value = extra.value.take(100),
                )
            }
        }
    }

    /**
     * Sets a user property in Firebase Analytics with automatic length truncation.
     *
     * User properties are attributes you define to describe segments of your user base.
     * They're useful for creating audiences and can be used as filters in Firebase reports.
     * Properties are automatically truncated to Firebase's length limits.
     *
     * @param name The user property name (≤ 24 characters after truncation)
     * @param value The user property value (≤ 36 characters after truncation)
     */
    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name.take(24), value.take(36))
    }

    /**
     * Sets the user ID in Firebase Analytics for cross-session user tracking.
     *
     * The user ID enables you to associate events with specific users across
     * sessions and devices. This helps create a more complete picture of user
     * behavior and enables advanced analytics features.
     *
     * **Privacy Note**: Ensure the user ID doesn't contain personally identifiable
     * information and complies with privacy regulations.
     *
     * @param userId The unique user identifier. Should not contain PII and must
     *               be consistent across sessions for the same user.
     */
    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }
}

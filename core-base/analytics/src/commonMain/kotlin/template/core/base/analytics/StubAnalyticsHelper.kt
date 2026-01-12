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

import co.touchlab.kermit.Logger

private const val TAG = "StubAnalyticsHelper"

/**
 * Development implementation of [AnalyticsHelper] that logs events to console output.
 *
 * This implementation provides a lightweight analytics solution for development and
 * debugging purposes. Instead of sending events to a remote analytics service, it
 * logs all events to the console using Kermit logging, making it easy to verify
 * that analytics events are being generated correctly during development.
 *
 * **Use Cases:**
 * - **Development Builds**: Debug app builds where you want to see analytics events
 * - **Local Testing**: Manual testing where you want to verify event generation
 * - **Debugging**: Troubleshooting analytics implementation issues
 * - **Offline Development**: Working without network connectivity to analytics services
 *
 * **Output Format:**
 * Events are logged with the full event structure including type and parameters,
 * making it easy to verify the correct data is being tracked.
 *
 * @see AnalyticsHelper for the complete interface contract
 * @see NoOpAnalyticsHelper for a no-operation implementation
 * @see FirebaseAnalyticsHelper for the production implementation
 * @see TestAnalyticsHelper for a testing implementation with capture capabilities
 *
 * @sample
 * ```kotlin
 * // Typically used in platform-specific DI modules for debug builds
 * val analyticsModule = module {
 *     single<AnalyticsHelper> { StubAnalyticsHelper() }
 * }
 * ```
 *
 * @since 1.0.0
 */
internal class StubAnalyticsHelper : AnalyticsHelper {
    /**
     * Logs the analytics event to console output using Kermit logger.
     *
     * The event is logged at ERROR level to ensure visibility in most logging
     * configurations. The log includes the complete event structure with type
     * and all parameters.
     *
     * @param event The analytics event to log to console
     */
    override fun logEvent(event: AnalyticsEvent) {
        Logger.e(TAG, null, "Received analytics event: $event")
    }
}

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
 * No-operation implementation of [AnalyticsHelper] that discards all analytics events.
 *
 * This implementation provides a complete no-op analytics solution that can be used
 * in scenarios where analytics tracking should be disabled or is not desired. It's
 * particularly useful for:
 *
 * - **Testing**: Unit tests where analytics calls should not interfere
 * - **Previews**: Jetpack Compose previews that need an analytics implementation
 * - **Debug Builds**: Development builds where analytics tracking is disabled
 * - **Privacy Mode**: Special app modes where analytics is intentionally disabled
 * - **Fallback**: Default implementation when no specific analytics provider is configured
 *
 * All methods in this implementation are safe to call and will not throw exceptions,
 * making it a reliable fallback option.
 *
 * @see AnalyticsHelper for the complete interface contract
 * @see StubAnalyticsHelper for a development implementation that logs events
 * @see TestAnalyticsHelper for a testing implementation that captures events
 *
 * @sample
 * ```kotlin
 * // Use in tests
 * val analytics: AnalyticsHelper = NoOpAnalyticsHelper()
 *
 * // Use as default in CompositionLocal
 * val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
 *     NoOpAnalyticsHelper()
 * }
 * ```
 *
 * @since 1.0.0
 */
class NoOpAnalyticsHelper : AnalyticsHelper {
    /**
     * Discards the analytics event without any processing.
     *
     * @param event The analytics event to discard (ignored)
     */
    override fun logEvent(event: AnalyticsEvent) = Unit
}

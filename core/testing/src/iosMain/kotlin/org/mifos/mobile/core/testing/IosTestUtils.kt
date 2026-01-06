/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing

/**
 * iOS-specific test utilities.
 *
 * This file provides iOS-specific testing helpers for Kotlin/Native.
 * The common testing utilities in commonMain (TestTags, Fixtures, Fakes)
 * work across all platforms.
 *
 * For iOS-specific UI testing, use XCTest framework directly
 * with accessibility identifiers matching [org.mifos.mobile.core.testing.util.TestTags].
 */
object IosTestUtils {

    /**
     * Converts a TestTag to an iOS accessibility identifier format.
     * TestTags use format: "feature:component:element"
     * iOS accessibility identifiers typically use: "feature_component_element"
     */
    fun testTagToAccessibilityId(testTag: String): String {
        return testTag.replace(":", "_")
    }

    /**
     * Default test timeout duration for iOS tests (in seconds).
     */
    const val DEFAULT_TEST_TIMEOUT: Double = 10.0

    /**
     * Network test timeout duration for iOS tests (in seconds).
     */
    const val NETWORK_TEST_TIMEOUT: Double = 30.0
}

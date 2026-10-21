/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.location.permission

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Verifies when the pre-authentication gate raises the location permission prompt. */
class LocationPermissionRequestTest {

    /** The prompt is raised while onboarding is the active root state. */
    @Test
    fun givenGateEnabledAndPermissionDenied_thenPermissionIsRequested() {
        val shouldRequest = shouldRequestLocationPermission(
            enabled = true,
            status = LocationPermissionStatus.Denied,
        )

        assertTrue(shouldRequest)
    }

    /** An already granted permission never triggers a redundant system prompt. */
    @Test
    fun givenGateEnabledAndPermissionGranted_thenPermissionIsNotRequested() {
        val shouldRequest = shouldRequestLocationPermission(
            enabled = true,
            status = LocationPermissionStatus.Granted,
        )

        assertFalse(shouldRequest)
    }

    /** Nothing is requested once the user reaches the authenticated part of the application. */
    @Test
    fun givenGateDisabled_thenPermissionIsNotRequested() {
        val denied = shouldRequestLocationPermission(
            enabled = false,
            status = LocationPermissionStatus.Denied,
        )
        val granted = shouldRequestLocationPermission(
            enabled = false,
            status = LocationPermissionStatus.Granted,
        )

        assertFalse(denied)
        assertFalse(granted)
    }

    /** Platforms without a runtime permission report granted, so the gate stays quiet. */
    @Test
    fun givenUnsupportedPlatformReportsGranted_thenPermissionIsNotRequested() {
        val shouldRequest = shouldRequestLocationPermission(
            enabled = true,
            status = LocationPermissionStatus.Granted,
        )

        assertFalse(shouldRequest)
    }
}

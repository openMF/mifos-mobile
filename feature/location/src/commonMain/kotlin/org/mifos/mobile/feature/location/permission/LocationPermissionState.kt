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

import androidx.compose.runtime.Composable

/**
 * Observable state of the device location permission.
 *
 * Every platform provides its own implementation because requesting a runtime permission is a
 * platform specific operation. Platforms that do not expose a runtime location permission, such
 * as desktop and web, report [LocationPermissionStatus.Granted] so that callers can treat the
 * permission as always available and skip prompting altogether.
 */
interface LocationPermissionState {

    /** Current authorization status of the location permission. */
    val status: LocationPermissionStatus

    /**
     * Asks the user for access to the device location.
     *
     * On Android this shows the system "precise or approximate location" dialog. On iOS this shows
     * the system "when in use" prompt. Platforms without a runtime permission do nothing.
     */
    fun requestLocationPermission()

    /** Opens the platform settings screen so the user can review or change the decision. */
    fun goToSettings()
}

/** Remembers the location permission state for the current platform. */
@Composable
expect fun rememberLocationPermissionState(): LocationPermissionState

/** Authorization status of the location permission. */
enum class LocationPermissionStatus {

    /** The user has not granted access to the device location yet. */
    Denied,

    /** The application is allowed to access the device location. */
    Granted,
}

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
 * The wasm web target has no runtime location permission, so the permission is treated as always
 * granted and no prompt is shown.
 */
@Composable
actual fun rememberLocationPermissionState(): LocationPermissionState {
    return object : LocationPermissionState {
        override val status: LocationPermissionStatus
            get() = LocationPermissionStatus.Granted

        override fun requestLocationPermission() {
            println("Location permission requested isn't supported on web yet.")
        }

        override fun goToSettings() {
            println("Go to settings isn't supported on web yet.")
        }
    }
}

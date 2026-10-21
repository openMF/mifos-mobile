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
import androidx.compose.runtime.LaunchedEffect

/**
 * Requests the device location permission while [enabled] is true and the permission has not been
 * granted yet.
 *
 * This composable emits no UI of its own. It only triggers the platform permission prompt, which is
 * rendered by the operating system, so it can be hosted anywhere in the composition. On platforms
 * without a runtime location permission the status is always
 * [LocationPermissionStatus.Granted] and the call is a no-op.
 *
 * @param enabled whether the prompt is currently allowed to be shown.
 */
@Composable
fun LocationPermissionRequest(enabled: Boolean) {
    val permissionState = rememberLocationPermissionState()
    val status = permissionState.status

    LaunchedEffect(enabled, status) {
        if (shouldRequestLocationPermission(enabled, status)) {
            permissionState.requestLocationPermission()
        }
    }
}

/**
 * Decides whether the pre-authentication gate should ask the user for location access.
 *
 * The prompt is only raised when the gate is [enabled] and the permission is still [status], so an
 * already granted permission never triggers a redundant request.
 */
fun shouldRequestLocationPermission(
    enabled: Boolean,
    status: LocationPermissionStatus,
): Boolean = enabled && status == LocationPermissionStatus.Denied

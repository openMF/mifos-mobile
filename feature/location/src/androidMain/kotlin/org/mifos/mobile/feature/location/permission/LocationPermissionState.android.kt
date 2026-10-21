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

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * Both the precise and the approximate location permissions are requested together so that the
 * system dialog lets the user pick between "precise" and "approximate" on Android 12 and above.
 */
private val LocationPermissions = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun rememberLocationPermissionState(): LocationPermissionState {
    val permissionsState = rememberMultiplePermissionsState(LocationPermissions)
    val context = LocalContext.current

    val wrapper = remember(permissionsState, context) {
        AndroidLocationPermissionState(permissionsState, context)
    }

    return wrapper
}

@OptIn(ExperimentalPermissionsApi::class)
private class AndroidLocationPermissionState(
    private val permissionsState: MultiplePermissionsState,
    private val context: Context,
) : LocationPermissionState {

    override val status: LocationPermissionStatus
        get() {
            val isGranted = hasLocationAccess(context) || permissionsState.allPermissionsGranted
            return if (isGranted) {
                LocationPermissionStatus.Granted
            } else {
                LocationPermissionStatus.Denied
            }
        }

    override fun requestLocationPermission() {
        permissionsState.launchMultiplePermissionRequest()
    }

    override fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        ContextCompat.startActivity(context, intent, null)
    }
}

/**
 * Falls back to the coarse permission so that a user who chose "approximate" location still counts
 * as granted, which keeps the reported status consistent with what the user actually selected.
 */
private fun hasLocationAccess(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED

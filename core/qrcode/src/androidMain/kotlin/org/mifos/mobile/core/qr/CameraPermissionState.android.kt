/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun rememberCameraPermissionState(): CameraPermissionState {
    val accPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val context = LocalContext.current
    val wrapper = remember(accPermissionState) {
        AccompanistPermissionWrapper(accPermissionState, context)
    }

    return wrapper
}

@OptIn(ExperimentalPermissionsApi::class)
class AccompanistPermissionWrapper(
    private val permissionState: PermissionState,
    private val context: Context,
) : CameraPermissionState {
    override val status: CameraPermissionStatus
        get() = permissionState.status.toCameraPermissionStatus()

    override fun requestCameraPermission() {
        permissionState.launchPermissionRequest()
    }

    override fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        ContextCompat.startActivity(context, intent, null)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionStatus.toCameraPermissionStatus(): CameraPermissionStatus {
    return when (this) {
        is PermissionStatus.Denied -> CameraPermissionStatus.Denied
        PermissionStatus.Granted -> CameraPermissionStatus.Granted
    }
}

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

import androidx.compose.runtime.Composable

interface CameraPermissionState {
    val status: CameraPermissionStatus
    fun requestCameraPermission()
    fun goToSettings()
}

@Composable
expect fun rememberCameraPermissionState(): CameraPermissionState

enum class CameraPermissionStatus {
    Denied,
    Granted,
}

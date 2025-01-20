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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp

@Composable
expect fun QrCodeScanner(
    types: List<CodeType>,
    modifier: Modifier = Modifier,
    onScanned: (String) -> Boolean,
)

@Composable
fun QrScannerWithPermissions(
    types: List<CodeType>,
    modifier: Modifier = Modifier,
    permissionText: String = "Camera is required for QR Code scanning",
    openSettingsLabel: String = "Open Settings",
    onScanned: (String) -> Boolean,
) {
    QrScannerWithPermissions(
        types = types,
        modifier = modifier.clipToBounds(),
        onScanned = onScanned,
        permissionDeniedContent = { permissionState ->
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = permissionText,
                )
                Button(
                    onClick = permissionState::goToSettings,
                ) {
                    Text(openSettingsLabel)
                }
            }
        },
    )
}

@Composable
fun QrScannerWithPermissions(
    types: List<CodeType>,
    modifier: Modifier = Modifier,
    onScanned: (String) -> Boolean,
    permissionDeniedContent: @Composable (CameraPermissionState) -> Unit,
) {
    val permissionState = rememberCameraPermissionState()

    LaunchedEffect(Unit) {
        if (permissionState.status == CameraPermissionStatus.Denied) {
            permissionState.requestCameraPermission()
        }
    }

    if (permissionState.status == CameraPermissionStatus.Granted) {
        QrCodeScanner(types = types, modifier, onScanned = onScanned)
    } else {
        permissionDeniedContent(permissionState)
    }
}

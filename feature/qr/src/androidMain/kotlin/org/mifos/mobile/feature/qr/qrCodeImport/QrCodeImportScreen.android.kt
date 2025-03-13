/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeImport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.attafitamim.krop.core.crop.CropError
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.cropSrc
import com.attafitamim.krop.core.crop.rememberImageCropper
import kotlinx.coroutines.launch
import org.mifos.mobile.feature.qr.CropContent
import org.mifos.mobile.feature.qr.CropErrorDialog
import org.mifos.mobile.feature.qr.rememberImagePicker

@Composable
actual fun QrCodeImagePicker(
    onProceed: (bitmap: ImageBitmap) -> Unit,
    modifier: Modifier,
) {
    val imageCropper = rememberImageCropper()
    val scope = rememberCoroutineScope()
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var error by remember { mutableStateOf<CropError?>(null) }
    val imagePicker = rememberImagePicker(onImage = { imageSrc ->
        scope.launch {
            when (val result = imageCropper.cropSrc(imageSrc)) {
                CropResult.Cancelled -> {}
                is CropError -> error = result
                is CropResult.Success -> {
                    onProceed(result.bitmap)
                    selectedImage = result.bitmap
                }
            }
        }
    })
    CropContent(
        cropState = imageCropper.cropState,
        loadingStatus = imageCropper.loadingStatus,
        selectedImage = selectedImage,
        onPick = { imagePicker.pick() },
        modifier = Modifier,
    )
    error?.let { CropErrorDialog(it, onDismiss = { error = null }) }
}

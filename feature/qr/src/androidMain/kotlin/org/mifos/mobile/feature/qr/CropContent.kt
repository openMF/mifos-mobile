/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.attafitamim.krop.core.crop.AspectRatio
import com.attafitamim.krop.core.crop.CircleCropShape
import com.attafitamim.krop.core.crop.CropState
import com.attafitamim.krop.core.crop.CropperLoading
import com.attafitamim.krop.core.crop.RectCropShape
import com.attafitamim.krop.core.crop.StarCropShape
import com.attafitamim.krop.core.crop.TriangleCropShape
import com.attafitamim.krop.core.crop.cropperStyle
import com.attafitamim.krop.ui.ImageCropperDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.theme.DesignToken

@Composable
fun CropContent(
    cropState: CropState?,
    loadingStatus: CropperLoading?,
    selectedImage: ImageBitmap?,
    onPick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (cropState != null) {
        ImageCropperDialog(
            state = cropState,
            style = cropperStyle(
                shapes = listOf(RectCropShape, CircleCropShape, TriangleCropShape, StarCropShape),
                aspects = listOf(AspectRatio(16, 9), AspectRatio(1, 1)),
            ),
        )
    }
    if (cropState == null && loadingStatus != null) {
        LoadingDialog(
            status = loadingStatus,
            modifier = modifier,
        )
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (selectedImage != null) {
                Image(
                    bitmap = selectedImage,
                    contentDescription = null,
                    modifier = Modifier.weight(1f),
                )
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                    Text("No image selected !")
                }
            }

            MifosButton(
                modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                onClick = onPick,
                shape = DesignToken.shapes.medium,
            ) {
                Text(
                    text = "Choose Image",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

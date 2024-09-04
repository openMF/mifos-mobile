/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.CropperLoading
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.qr.R

@Composable
fun CropContent(
    cropState: CropState?,
    loadingStatus: CropperLoading?,
    selectedImage: ImageBitmap?,
    onPick: () -> Unit,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (cropState != null) {
        ImageCropperDialog(state = cropState)
    }

    if (cropState == null && loadingStatus != null) {
        LoadingDialog(status = loadingStatus)
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (selectedImage != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.seleted_qr_image),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                bitmap = selectedImage,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(16.dp))

            MifosOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textResId = R.string.import_another_qr,
                onClick = onPick,
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textResId = R.string.proceed,
                onClick = onProceed,
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f),
            ) {
                Text("No QR Code selected!")
            }

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textResId = R.string.import_qr,
                onClick = onPick,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun CropContentPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        CropContent(
            cropState = rememberImageCropper().cropState,
            modifier = modifier,
            loadingStatus = null,
            selectedImage = null,
            onPick = {},
            onProceed = {},
        )
    }
}

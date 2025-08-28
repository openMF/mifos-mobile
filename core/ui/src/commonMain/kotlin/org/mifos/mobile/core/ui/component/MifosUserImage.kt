/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosUserImage(
    bitmap: ByteArray?,
    modifier: Modifier = Modifier,
    username: String? = null,
) {
    val context = LocalPlatformContext.current

    if (bitmap == null) {
        MifosTextUserImage(
            text = username?.firstOrNull()?.toString() ?: "M",
            modifier = modifier,
        )
    } else {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(bitmap)
                .build(),
            imageLoader = ImageLoader(context),
        )
        Image(
            modifier = modifier
                .clip(CircleShape)
                .fillMaxSize(),
            painter = painter,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
        )
    }
}

@DevicePreview
@Composable
fun MifosUserImagePreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosUserImage(
            bitmap = null,
            modifier = modifier,
            username = "John Doe",
        )
    }
}

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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun MifosUserImage(
    bitmap: ImageBitmap?,
    modifier: Modifier = Modifier,
    username: String? = null,
) {
    if (bitmap == null) {
        MifosTextUserImage(
            text = username?.firstOrNull()?.toString() ?: "M",
            modifier = modifier,
        )
    } else {
        Image(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            bitmap = bitmap,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
        )
    }
}

@DevicePreviews
@Composable
private fun MifosUserImagePreview(
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

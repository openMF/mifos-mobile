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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview
import kotlin.math.min

@Composable
fun MifosTextUserImage(
    text: String,
    modifier: Modifier = Modifier,
) {
    var boxSize by remember { mutableStateOf(Size.Zero) }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
            .onGloballyPositioned { coordinates ->
                boxSize = Size(
                    coordinates.size.width.toFloat(),
                    coordinates.size.height.toFloat(),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = with(LocalDensity.current) {
                (min(boxSize.width, boxSize.height) / 2).toSp()
            },
        )
    }
}

@DevicePreview
@Composable
fun MifosTextUserImagePreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosTextUserImage(
            text = "A",
            modifier = modifier,
        )
    }
}

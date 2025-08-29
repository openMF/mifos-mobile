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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import mifos_mobile.core.ui.generated.resources.Res
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.LottieConstants

@Composable
fun MifosProgressIndicator(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(LottieConstants.LOADING_ANIMATION).decodeToString(),
        )
    }
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress },
            ),
            contentDescription = "Lottie animation",
        )
    }
}

@Composable
fun MifosProgressIndicatorOverlay(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/loading_animation.json").decodeToString(),
        )
    }
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            .clickable(
                enabled = false,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress },
            ),
            contentDescription = "Loading animation",
        )
    }
}

@Preview
@Composable
private fun Loading_Preview() {
    MifosMobileTheme {
        MifosProgressIndicator()
    }
}

@Preview
@Composable
private fun Overlay_Loading_Preview() {
    MifosMobileTheme {
        MifosProgressIndicatorOverlay()
    }
}

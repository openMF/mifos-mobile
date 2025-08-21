/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.back_online
import mifos_mobile.core.ui.generated.resources.no_internet
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun NetworkBanner(
    bannerState: NetworkBannerState?,
    modifier: Modifier = Modifier,
) {
    val bannerColor: Color
    val bannerText: String

    when (bannerState) {
        NetworkBannerState.Offline -> {
            bannerColor = MaterialTheme.colorScheme.error
            bannerText = stringResource(Res.string.no_internet)
        }
        NetworkBannerState.BackOnline -> {
            bannerColor = AppColors.customEnable
            bannerText = stringResource(Res.string.back_online)
        }
        NetworkBannerState.None, null -> return
    }

    val visible = bannerState != NetworkBannerState.None

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 500),
        ) + fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 500),
        ) + fadeOut(animationSpec = tween(durationMillis = 500)),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bannerColor)
                .padding(DesignToken.padding.extraSmall),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = bannerText,
                color = Color.White,
                style = MifosTypography.bodySmallEmphasized,
            )
        }
    }
}

enum class NetworkBannerState {
    None,
    Offline,
    BackOnline,
}

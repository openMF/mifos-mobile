/*
 * Copyright 2025 Mifos Initiative
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_powered_logo
import mifos_mobile.core.ui.generated.resources.powered_by
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@Composable
fun MifosPoweredCard(
    modifier: Modifier = Modifier,
    text: String? = stringResource(Res.string.powered_by),
    icon: DrawableResource? = Res.drawable.ic_icon_powered_logo,
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = DesignToken.elevation.elevation,
                shape = DesignToken.shapes.bottomSheet,
                ambientColor = KptTheme.colorScheme.onSurface,
                spotColor = KptTheme.colorScheme.onSurface,
                clip = false,
            )
            .background(KptTheme.colorScheme.surface, shape = DesignToken.shapes.bottomSheet),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DesignToken.padding.medium),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = text ?: "",
                style = MifosTypography.tag,
                color = KptTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(KptTheme.spacing.xs))
            if (icon != null) {
                Image(
                    painter = painterResource(
                        icon,
                    ),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Mifos_Powered_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            MifosPoweredCard(
                text = "Powered Card",
                icon = Res.drawable.ic_icon_powered_logo,
            )
        }
    }
}

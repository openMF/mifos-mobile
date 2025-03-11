/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.ic_qrcode_scan_gray_dark
import mifos_mobile.feature.beneficiary.generated.resources.scan
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

/**
 * this is a reusable composable function that is made up of a text and icon composable
 * some of the intake parameters are
 * @param[image]
 * @param[iconDescription]
 * @param[text]
 * @param[iconClick]
 *
 * */
@Composable
internal fun RenderIconAndText(
    image: DrawableResource,
    text: String,
    iconDescription: String,
    iconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            tint = MaterialTheme.colorScheme.onSurface,
            painter = painterResource(image),
            contentDescription = iconDescription,
            modifier = Modifier
                .height(85.dp)
                .width(85.dp)
                .clickable(
                    onClick = iconClick,
                ),
        )

        Text(text)
    }
}

@Preview
@Composable
private fun IconsAndTextPreview() {
    MifosMobileTheme {
        Surface {
            RenderIconAndText(
                image = Res.drawable.ic_qrcode_scan_gray_dark,
                text = stringResource(Res.string.scan),
                iconDescription = stringResource(Res.string.scan),
                iconClick = { },
            )
        }
    }
}

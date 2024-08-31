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

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.beneficiary.R

/**
 * this is a reusable composable function that is made up of a text and icon composable
 * some of the intake parameters are
 * @param[icon]
 * @param[iconDescription]
 * @param[text]
 * @param[iconClick]
 *
 * */
@Composable
internal fun RenderIconAndText(
    @DrawableRes icon: Int,
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
            painter = painterResource(id = icon),
            contentDescription = iconDescription,
            modifier = Modifier
                .height(85.dp)
                .width(85.dp)
                .clickable(
                    onClick = iconClick,
                ),
        )

        Text(
            text,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@DevicePreviews
@Composable
private fun IconsAndTextPreview() {
    MifosMobileTheme {
        Surface {
            RenderIconAndText(
                icon = R.drawable.ic_qrcode_scan_gray_dark,
                text = stringResource(id = R.string.scan),
                iconDescription = stringResource(id = R.string.scan),
                iconClick = {},
            )
        }
    }
}

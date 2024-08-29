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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun AboutUsItemCard(
    title: String,
    @StringRes subtitle: Int?,
    @DrawableRes iconUrl: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(16.dp),
    ) {
        iconUrl?.let { painterResource(id = it) }?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 8.dp),
            )
            if (subtitle != null) {
                Text(
                    text = stringResource(id = subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun AboutUsItemCardPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        AboutUsItemCard(
            title = "About Us",
            modifier = modifier,
            subtitle = null,
            iconUrl = null,
        )
    }
}

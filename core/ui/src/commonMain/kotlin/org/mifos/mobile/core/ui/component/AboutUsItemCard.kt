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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun AboutUsItemCard(
    title: String,
    subtitle: StringResource?,
    iconUrl: DrawableResource?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(16.dp),
    ) {
        iconUrl?.let { painterResource(it) }?.let {
            Image(
                painter = it,
                contentDescription = "About Us Icon URL",
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
                    text = stringResource(subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
    }
}

@DevicePreview
@Composable
fun AboutUsItemCardPreview(
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

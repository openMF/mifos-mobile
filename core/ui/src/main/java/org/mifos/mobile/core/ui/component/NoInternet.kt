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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.R
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun NoInternet(
    @StringRes error: Int,
    modifier: Modifier = Modifier,
    isRetryEnabled: Boolean = true,
    @DrawableRes icon: Int? = null,
    retry: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            imageVector = if (icon != null) {
                ImageVector.vectorResource(id = icon)
            } else {
                MifosIcons.WifiOff
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary,
        )

        Text(
            text = stringResource(id = error),
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary,
        )

        Spacer(modifier = Modifier.height(12.dp))
        if (isRetryEnabled) {
            FilledTonalButton(onClick = { retry.invoke() }) {
                Text(text = "Retry")
            }
        }
    }
}

@DevicePreviews
@Composable
private fun NoInternetPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        NoInternet(
            error = R.string.no_internet,
            modifier = modifier,
            isRetryEnabled = true,
            icon = R.drawable.core_common_circular_background,
            retry = {},
        )
    }
}

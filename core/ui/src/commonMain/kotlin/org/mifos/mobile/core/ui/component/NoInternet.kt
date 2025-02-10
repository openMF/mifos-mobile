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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.no_internet
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun NoInternet(
    error: StringResource,
    modifier: Modifier = Modifier,
    isRetryEnabled: Boolean = true,
    icon: ImageVector = MifosIcons.WifiOff,
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
            imageVector = icon,
            contentDescription = "No Internet Icon",
            tint = MaterialTheme.colorScheme.onSecondary,
        )

        Text(
            text = stringResource(error),
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

@DevicePreview
@Composable
fun NoInternetPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        NoInternet(
            error = Res.string.no_internet,
            modifier = modifier,
            isRetryEnabled = true,
            retry = {},
        )
    }
}

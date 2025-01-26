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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.no_data
import mifos_mobile.core.ui.generated.resources.no_internet
import mifos_mobile.core.ui.generated.resources.retry
import mifos_mobile.core.ui.generated.resources.something_went_wrong
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun MifosErrorComponent(
    modifier: Modifier = Modifier,
    isNetworkConnected: Boolean = true,
    message: String? = null,
    isEmptyData: Boolean = false,
    isRetryEnabled: Boolean = false,
    onRetry: () -> Unit = {},
) {
    when {
        !isNetworkConnected -> NoInternetComponent(isRetryEnabled = isRetryEnabled) { onRetry() }
        else -> EmptyDataComponent(
            modifier = modifier,
            isEmptyData = isEmptyData,
            message = message,
            isRetryEnabled = isRetryEnabled,
            onRetry = onRetry,
        )
    }
}

@Composable
fun NoInternetComponent(
    modifier: Modifier = Modifier,
    isRetryEnabled: Boolean = false,
    onRetry: () -> Unit = {},
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
            imageVector = MifosIcons.WifiOff,
            contentDescription = "Wifi Icon",
            tint = MaterialTheme.colorScheme.onSecondary,
        )

        Text(
            text = stringResource(Res.string.no_internet),
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary,
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isRetryEnabled) {
            FilledTonalButton(onClick = { onRetry.invoke() }) {
                Text(text = stringResource(Res.string.retry))
            }
        }
    }
}

@Composable
fun EmptyDataComponent(
    modifier: Modifier = Modifier,
    isEmptyData: Boolean = false,
    message: String? = null,
    isRetryEnabled: Boolean = false,
    onRetry: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            imageVector = MifosIcons.Info,
            contentDescription = "Info Icon",
            tint = MaterialTheme.colorScheme.onSecondary,
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = message ?: if (isEmptyData) {
                stringResource(Res.string.no_data)
            } else {
                stringResource(Res.string.something_went_wrong)
            },
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
        )

        if (isRetryEnabled) {
            FilledTonalButton(
                modifier = Modifier.padding(top = 8.dp),
                onClick = { onRetry.invoke() },
            ) {
                Text(text = stringResource(Res.string.retry))
            }
        }
    }
}

@Composable
fun EmptyDataComponentWithModifiedMessageAndIcon(
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isEmptyData: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            imageVector = if (isEmptyData) icon else MifosIcons.Info,
            contentDescription = "Info Icon",
            tint = MaterialTheme.colorScheme.onSecondary,
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = if (isEmptyData) message else stringResource(Res.string.something_went_wrong),
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@DevicePreviews
@Composable
fun NoInternetPreview() {
    MifosMobileTheme {
        NoInternetComponent()
    }
}

@DevicePreviews
@Composable
fun EmptyDataPreview() {
    MifosMobileTheme {
        EmptyDataComponent()
    }
}

@DevicePreviews
@Composable
private fun EmptyDataComponentWithModifiedMessageAndIconPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        EmptyDataComponentWithModifiedMessageAndIcon(
            message = "No data found",
            icon = MifosIcons.Error,
            modifier = modifier,
            isEmptyData = true,
        )
    }
}

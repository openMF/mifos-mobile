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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.no_data
import mifos_mobile.core.ui.generated.resources.no_internet
import mifos_mobile.core.ui.generated.resources.retry
import mifos_mobile.core.ui.generated.resources.something_went_wrong
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.utils.DevicePreview
import template.core.base.designsystem.theme.KptTheme

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
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(DesignToken.sizes.avatarMedium),
            imageVector = MifosIcons.WifiOff,
            contentDescription = "Wifi Icon",
        )

        Text(
            text = stringResource(Res.string.no_internet),
            style = MifosTypography.titleSmallEmphasized,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        if (isRetryEnabled) {
            MifosButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(DesignToken.sizes.buttonHeight)
                    .align(Alignment.CenterHorizontally),
                onClick = { onRetry.invoke() },
                shape = KptTheme.shapes.medium,
            ) {
                Text(
                    text = stringResource(Res.string.retry),
                    style = MifosTypography.titleMedium,
                )
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
        modifier = modifier.fillMaxSize()
            .padding(KptTheme.spacing.md),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(DesignToken.sizes.avatarMedium),
            imageVector = MifosIcons.ErrorCircle,
            contentDescription = "Info Icon",
        )

        Text(
            text = message ?: if (isEmptyData) {
                stringResource(Res.string.no_data)
            } else {
                stringResource(Res.string.something_went_wrong)
            },
            style = MifosTypography.titleSmallEmphasized,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        if (isRetryEnabled) {
            MifosButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(DesignToken.sizes.inputHeight)
                    .align(Alignment.CenterHorizontally),
                onClick = { onRetry.invoke() },
                shape = KptTheme.shapes.medium,
            ) {
                Text(
                    text = stringResource(Res.string.retry),
                    style = MifosTypography.titleMedium,
                )
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
                .size(DesignToken.sizes.iconDp100)
                .padding(bottom = DesignToken.padding.medium),
            imageVector = if (isEmptyData) icon else MifosIcons.Info,
            contentDescription = "Info Icon",
        )

        Text(
            modifier = Modifier.padding(horizontal = DesignToken.padding.largeIncreased),
            text = if (isEmptyData) message else stringResource(Res.string.something_went_wrong),
            style = TextStyle(fontSize = 20.sp),
            textAlign = TextAlign.Center,
            color = KptTheme.colorScheme.error,

        )
    }
}

@DevicePreview
@Composable
fun NoInternetPreview() {
    MifosMobileTheme {
        NoInternetComponent()
    }
}

@DevicePreview
@Composable
fun EmptyDataPreview() {
    MifosMobileTheme {
        EmptyDataComponent()
    }
}

@DevicePreview
@Composable
fun EmptyDataComponentWithModifiedMessageAndIconPreview(
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

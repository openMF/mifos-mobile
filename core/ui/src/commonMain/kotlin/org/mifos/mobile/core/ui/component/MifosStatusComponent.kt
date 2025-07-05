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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_error
import mifos_mobile.core.ui.generated.resources.ic_icon_success
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun MifosStatusComponent(
    icon: DrawableResource,
    title: String,
    subTitle: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .height(96.dp)
                .width(96.dp),
            painter = painterResource(icon),
            contentDescription = "Status icon",
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        Text(
            text = title,
            style = MifosTypography.headlineSmallEmphasized,
            color = AppColors.customBlack,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        Text(
            text = subTitle,
            style = MifosTypography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

        MifosButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonHeight),
            shape = DesignToken.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            text = {
                Text(
                    text = buttonText,
                    style = MifosTypography.titleMedium,
                    color = AppColors.customWhite,
                )
            },
            onClick = onClick,
        )
    }
}

@Composable
@Preview
private fun Mifos_Status_Component() {
    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.Center,
        ) {
            MifosStatusComponent(
                icon = Res.drawable.ic_icon_success,
                title = "User Successfully Registered",
                subTitle = "You can now log in with your username (phone number) and password.",
                buttonText = "Next",
                onClick = {},
            )
            Spacer(modifier = Modifier.height(DesignToken.spacing.extraExtraLarge))
            MifosStatusComponent(
                icon = Res.drawable.ic_icon_error,
                title = "Failed To Register The User",
                subTitle = "There is an error to register the user. Please try again.",
                buttonText = "Try Again",
                onClick = {},
            )
        }
    }
}

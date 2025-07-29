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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun TransactionScreenItem(
    title: String,
    date: String,
    time: String,
    transactionAmount: String,
    isCredited: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = DesignToken.padding.medium),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector =
                if (isCredited) {
                    MifosIcons.DrawerAdd
                } else {
                    MifosIcons.DrawerSubtract
                },
                contentDescription = "Symbol",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
                        shape = CircleShape,
                    )
                    .padding(DesignToken.padding.small),
            )

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(DesignToken.padding.extraSmall),
            ) {
                Text(
                    text = title,
                    style = MifosTypography.titleSmallEmphasized,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = if (time.isNotEmpty()) "$time; $date" else date,
                    style = MifosTypography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Text(
                text = if (isCredited) {
                    "+ $transactionAmount"
                } else {
                    "- $transactionAmount"
                },
                style = MifosTypography.labelSmall,
                color = if (isCredited) {
                    AppColors.customEnable
                } else {
                    MaterialTheme.colorScheme.error
                },
            )
        }
    }
}

@Preview
@Composable
private fun TransactionScreenItem_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
        ) {
            TransactionScreenItem(
                title = "Add-Money Bank Card",
                date = "20-03-2020",
                time = "5:10",
                transactionAmount = "87289",
                isCredited = true,
            )
            TransactionScreenItem(
                title = "Add-Money Bank Card",
                date = "20-03-2020",
                time = "5:10",
                transactionAmount = "87289",
                isCredited = false,
            )
        }
    }
}

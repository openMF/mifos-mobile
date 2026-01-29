/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import template.core.base.designsystem.theme.KptTheme

@Composable
fun MifosLabelValueCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        variant = CardVariant.OUTLINED,
        modifier = modifier
            .border(
                DesignToken.strokes.thin,
                KptTheme.colorScheme.secondaryContainer,
                KptTheme.shapes.medium,
            ),
        shape = KptTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent,
            contentColor = KptTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(DesignToken.padding.medium),
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = label,
                style = KptTheme.typography.bodySmall,
                color = KptTheme.colorScheme.secondary,

            )

            BasicText(
                text = value,
                style = KptTheme.typography.bodyMedium.copy(color = color),
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 10.sp,
                    maxFontSize = 60.sp,
                    stepSize = 10.sp,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun Mifos_Label_Card_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(KptTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
        ) {
            MifosLabelValueCard(
                label = "Account Number",
                value = "268978976666",
                color = KptTheme.colorScheme.onBackground,

            )

            MifosLabelValueCard(
                label = "Product Type",
                value = "Education Loan",
                color = AppColors.customEnable,

            )
        }
    }
}

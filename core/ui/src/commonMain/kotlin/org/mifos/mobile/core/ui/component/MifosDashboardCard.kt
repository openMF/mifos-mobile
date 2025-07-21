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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import mifos_mobile.core.ui.generated.resources.powered_by
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MifosDashboardCard(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    isLoanApplied: Boolean = true,
    onLoanApplyClick: () -> Unit = {},
    isSingleLine: Boolean = false,
    loanAccount: StringResource? = null,
    loanAmount: Double? = null,
    savingsAccount: StringResource? = null,
    savingsAmount: Double? = null,
    currency: String? = null,
    onVisibilityToggle: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(if (isSingleLine) 76.dp else 128.dp)
            .fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier
                .matchParentSize(),
            painter = painterResource(Res.drawable.ic_icon_dashboard),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        if (isLoanApplied) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DesignToken.padding.small),
                horizontalArrangement = Arrangement.spacedBy(
                    DesignToken.spacing.medium,
                    Alignment.End,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(DesignToken.padding.medium),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (loanAccount != null) {
                        Column {
                            Text(
                                text = stringResource(loanAccount),
                                style = MifosTypography.bodySmall,
//                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                                color = AppColors.customWhite.copy(alpha = 0.5f),
                            )
                            AnimatedContent(
                                targetState = isVisible,
                                transitionSpec = {
                                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                                },
                                label = "Loan Amount Animation",
                            ) { visible ->
                                Text(
                                    text = if (visible) "$currency $loanAmount" else "$currency •••••••••",
                                    style = MifosTypography.titleMediumEmphasized,
                                    color = AppColors.customWhite,
                                )
                            }
                        }
                    }

                    if (savingsAccount != null) {
                        Column {
                            Text(
                                text = stringResource(savingsAccount),
                                style = MifosTypography.bodySmall,
//                            color = MaterialTheme.colorScheme.secondary,
                                color = AppColors.customWhite.copy(alpha = 0.5f),
                            )
                            AnimatedContent(
                                targetState = isVisible,
                                transitionSpec = {
                                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                                },
                                label = "Savings Amount Animation",
                            ) { visible ->
                                Text(
                                    text = if (visible) "$currency $savingsAmount" else "$currency •••••••••",
                                    style = MifosTypography.titleMediumEmphasized,
                                    color = AppColors.customWhite,
                                )
                            }
                        }
                    }
                }
            }

            IconButton(
                onClick = onVisibilityToggle,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
            ) {
                Icon(
                    imageVector = if (isVisible) MifosIcons.Eye else MifosIcons.EyeOff,
                    contentDescription = "Toggle Visibility",
                    tint = Color.White,
                )
            }
        } else {
            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight)
                    .align(Alignment.Center),
                onClick = onLoanApplyClick,
                content = {
                    Text(
                        text = "Apply Loan",
                        style = MifosTypography.bodySmall,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun MifosDashboardCard() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MifosDashboardCard(
                isVisible = true,
                loanAccount = Res.string.powered_by,
                savingsAccount = Res.string.powered_by,
                loanAmount = 900.00,
                savingsAmount = 1000.00,
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isSingleLine = true,
                isVisible = true,
                loanAccount = Res.string.powered_by,
                loanAmount = 900.00,
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isVisible = false,
                loanAccount = Res.string.powered_by,
                savingsAccount = Res.string.powered_by,
                loanAmount = 900.00,
                savingsAmount = 1000.00,
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isSingleLine = true,
                isVisible = false,
                loanAccount = Res.string.powered_by,
                loanAmount = 900.00,
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isLoanApplied = false,
                isVisible = true,
            )
        }
    }
}

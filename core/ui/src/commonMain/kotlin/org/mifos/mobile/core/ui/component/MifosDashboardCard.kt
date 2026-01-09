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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.feature_dashboard_no_accounts_description
import mifos_mobile.core.ui.generated.resources.feature_dashboard_no_accounts_title
import mifos_mobile.core.ui.generated.resources.feature_dashboard_open_account
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import mifos_mobile.core.ui.generated.resources.powered_by
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MifosDashboardCard(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = false,
    loanAccount: StringResource? = null,
    loanAmount: String? = null,
    savingsAccount: StringResource? = null,
    savingsAmount: String? = null,
    currency: String? = null,
    onVisibilityToggle: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(KptTheme.shapes.large)
            .height(if (isSingleLine) DesignToken.sizes.boxDp76 else DesignToken.sizes.boxDp128)
            .fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier
                .matchParentSize(),
            painter = painterResource(Res.drawable.ic_icon_dashboard),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(KptTheme.spacing.sm),
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
//                            color = KptTheme.colorScheme.secondary.copy(alpha = 0.7f),
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
                                text = if (visible) "$loanAmount" else "$currency •••••••••",
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
//                            color = KptTheme.colorScheme.secondary,
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
                                text = if (visible) "$savingsAmount" else "$currency •••••••••",
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
                .padding(DesignToken.padding.medium),
        ) {
            Icon(
                imageVector = if (isVisible) MifosIcons.Eye else MifosIcons.EyeOff,
                contentDescription = "Toggle Visibility",
                tint = Color.White,
            )
        }
    }
}

@Composable
fun MifosAccountApplyDashboard(
    onOpenAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        modifier = modifier
            .padding(horizontal = DesignToken.padding.largeIncreased)
            .border(
                DesignToken.strokes.dpPoint5,
                KptTheme.colorScheme.primary,
                KptTheme.shapes.medium,
            ),
        variant = CardVariant.OUTLINED,
        enabled = false,
        onClick = onOpenAccountClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = KptTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .background(
                    KptTheme.colorScheme.onSurface.copy(alpha = 0.01f),
                )
                .fillMaxWidth()
                .padding(DesignToken.padding.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            Icon(
                imageVector = MifosIcons.AddColor,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(DesignToken.sizes.iconExtraLarge),
            )

            Text(
                text = stringResource(Res.string.feature_dashboard_no_accounts_title),
                style = MifosTypography.titleMediumEmphasized,
                color = KptTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(Res.string.feature_dashboard_no_accounts_description),
                style = MifosTypography.bodySmall,
                color = KptTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )

            MifosButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(DesignToken.sizes.avatarMedium),
                onClick = onOpenAccountClick,
                shape = DesignToken.shapes.circle,
                text = {
                    Text(
                        text = stringResource(Res.string.feature_dashboard_open_account),
                        style = MifosTypography.titleSmallEmphasized,
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
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
        ) {
            MifosDashboardCard(
                isVisible = true,
                loanAccount = Res.string.powered_by,
                savingsAccount = Res.string.powered_by,
                loanAmount = "900.00",
                savingsAmount = "1000.00",
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isSingleLine = true,
                isVisible = true,
                loanAccount = Res.string.powered_by,
                loanAmount = "900.00",
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isVisible = false,
                loanAccount = Res.string.powered_by,
                savingsAccount = Res.string.powered_by,
                loanAmount = "900.00",
                savingsAmount = "1000.00",
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosDashboardCard(
                isSingleLine = true,
                isVisible = false,
                loanAccount = Res.string.powered_by,
                loanAmount = "900.00",
                currency = "$",
                onVisibilityToggle = {},
            )

            MifosAccountApplyDashboard(
                onOpenAccountClick = {},
            )
        }
    }
}

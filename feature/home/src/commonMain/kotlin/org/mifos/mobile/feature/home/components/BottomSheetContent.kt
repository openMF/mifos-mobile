/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.feature_home_loan_account
import mifos_mobile.feature.home.generated.resources.feature_home_loan_tip
import mifos_mobile.feature.home.generated.resources.feature_home_saving_account
import mifos_mobile.feature.home.generated.resources.feature_home_savings_tip
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.feature.home.HomeAction

@Composable
internal fun BottomSheetContent(
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
) {
    AnimatedContent(
        targetState = isVisible,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300, delayMillis = 100),
            ) togetherWith slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            ) + fadeOut(
                animationSpec = tween(durationMillis = 200),
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            BottomSheetIconContainer(
                onClick = {
                    when (it) {
                        BottomSheetItemType.LOAN -> onAction(HomeAction.OnNavigate(Constants.APPLY_LOAN))
                        BottomSheetItemType.SAVINGS -> onAction(HomeAction.OnNavigate(Constants.APPLY_SAVINGS))
                    }
                },
            )
        }
    }
}

@Composable
internal fun BottomSheetIconContainer(
    onClick: (BottomSheetItemType) -> Unit,
) {
    BottomSheetItemType.entries.forEach {
        MifosCustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondaryContainer,
                    DesignToken.shapes.medium,
                ),
            onClick = { onClick(it) },
            variant = CardVariant.OUTLINED,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(DesignToken.padding.large),
            ) {
                Box(
                    modifier = Modifier.size(DesignToken.sizes.inputHeight)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.secondaryContainer,
                            DesignToken.shapes.medium,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier.size(DesignToken.sizes.iconMedium),
                        imageVector = when (it) {
                            BottomSheetItemType.LOAN -> MifosIcons.LoanAccountColor
                            BottomSheetItemType.SAVINGS -> MifosIcons.SavingsAccountColor
                        },
                        contentDescription = null,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
                    modifier = Modifier.padding(horizontal = DesignToken.padding.large),
                ) {
                    Text(
                        text = when (it) {
                            BottomSheetItemType.LOAN -> stringResource(Res.string.feature_home_loan_account)
                            BottomSheetItemType.SAVINGS -> stringResource(Res.string.feature_home_saving_account)
                        },
                        style = MifosTypography.bodyMediumEmphasized,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = when (it) {
                            BottomSheetItemType.LOAN -> stringResource(Res.string.feature_home_loan_tip)
                            BottomSheetItemType.SAVINGS -> stringResource(Res.string.feature_home_savings_tip)
                        },
                        style = MifosTypography.bodySmallEmphasized,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

enum class BottomSheetItemType {
    SAVINGS,
    LOAN,
}

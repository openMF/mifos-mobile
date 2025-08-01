/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_number_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_status_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_product_type_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_scheme_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun AccountSummaryCard(
    keyValuePairs: Map<StringResource, String?>,
    modifier: Modifier = Modifier,
    title: String = "",
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    MifosCustomCard(
        variant = CardVariant.OUTLINED,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondaryContainer,
                DesignToken.shapes.medium,
            ),
        shape = DesignToken.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(DesignToken.padding.large),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = title,
                    style = MifosTypography.titleSmallEmphasized,
                    color = AppColors.customBlack,
                )
                Icon(
                    modifier = Modifier.size(DesignToken.sizes.iconSmall),
                    imageVector = if (isExpanded) MifosIcons.CaretUp else MifosIcons.CaretDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut(),
            ) {
                Column(modifier = Modifier.padding(top = DesignToken.padding.medium)) {
                    keyValuePairs.forEach { (key, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = DesignToken.padding.small),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "${stringResource(key)} :",
                                style = MifosTypography.labelMediumEmphasized,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            Text(
                                text = value ?: "",
                                style = MifosTypography.labelMedium,
                                textAlign = TextAlign.Right,
                                color = if (key == Res.string.feature_loan_account_status_label) {
                                    AppColors.customEnable
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Account_Card_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignToken.padding.large),
        ) {
            AccountSummaryCard(
                keyValuePairs = mapOf(
                    Res.string.feature_loan_account_number_label to "John Miller",
                    Res.string.feature_loan_product_type_label to "2-05-2025",
                    Res.string.feature_loan_scheme_label to "268978976666",
                    Res.string.feature_loan_account_status_label to "Wallet",
                ),
            )
        }
    }
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_account_number_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_client_name_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_product_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_submission_date_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun AccountDetailsCard(
    keyValuePairs: Map<StringResource, String?>,
    modifier: Modifier = Modifier,
) {
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
        Column(modifier = Modifier.padding(DesignToken.padding.large)) {
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
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = value ?: "",
                        style = MifosTypography.labelMedium,
                        textAlign = TextAlign.Right,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
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
            AccountDetailsCard(
                keyValuePairs = mapOf(
                    Res.string.feature_savings_update_client_name_label to "John Miller",
                    Res.string.feature_savings_update_submission_date_label to "2-05-2025",
                    Res.string.feature_savings_update_account_number_label to "268978976666",
                    Res.string.feature_savings_update_product_label to "Wallet",
                ),
            )
        }
    }
}

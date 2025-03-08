/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountTransaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.help_line_number
import mifos_mobile.feature.savings.generated.resources.need_help
import mifos_mobile.feature.savings.generated.resources.savings_account_transaction
import mifos_mobile.feature.savings.generated.resources.string_and_string
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.savings.Currency
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
internal fun SavingsAccountTransactionContent(
    currencyCode: String,
    transactionList: List<Transactions>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        LazyColumn {
            items(items = transactionList) {
                SavingsAccountTransactionListItem(currencyCode,it)
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = stringResource(Res.string.need_help),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = stringResource(Res.string.help_line_number),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun SavingsAccountTransactionListItem(
    currencyCode:String,
    transaction: Transactions,
    modifier: Modifier = Modifier,
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
    ) {
        Image(
            painter = painterResource(
                getTransactionTriangleResId(transaction.transactionType),
            ),
            contentDescription = stringResource(Res.string.savings_account_transaction),
            modifier = Modifier
                .size(56.dp)
                .padding(4.dp),
        )
        Column(
            modifier = Modifier.padding(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = DateHelper.getDateAsString(transaction.date),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = CurrencyFormatter
                            .format(balance = transaction.amount,
                                currencyCode = currencyCode, maximumFractionDigits = 3)
                    ,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = transaction.transactionType?.value ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = CurrencyFormatter
                            .format(
                                balance = transaction.runningBalance,
                                currencyCode =currencyCode ,
                                maximumFractionDigits = 5)
                    ,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = transaction.paymentDetailData?.paymentType?.name.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun SavingsAccountTransactionContentPreview() {
    MifosMobileTheme {
        SavingsAccountTransactionContent(currencyCode = "USD",transactionList = listOf())
    }
}

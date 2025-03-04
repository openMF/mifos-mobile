/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_account_active
import mifos_mobile.feature.savings_account.generated.resources.feature_account_approved
import mifos_mobile.feature.savings_account.generated.resources.feature_account_closed
import mifos_mobile.feature.savings_account.generated.resources.feature_account_matured
import mifos_mobile.feature.savings_account.generated.resources.feature_account_string_and_string
import mifos_mobile.feature.savings_account.generated.resources.feature_account_submitted
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.savingsaccount.component.AccountCard

@Composable
internal fun SavingsAccountScreenContent(
    accountList: List<SavingAccount>,
    onAccountSelected: (AccountType, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(
            items = accountList,
            key = { account -> account.id },
        ) { savingAccount ->
            SavingsAccountListItem(
                savingAccount = savingAccount,
                onAccountSelected = onAccountSelected,
            )
        }
    }
}

@Composable
private fun SavingsAccountListItem(
    savingAccount: SavingAccount,
    onAccountSelected: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (indicatorColor, statusDescription, balanceTextColor) = when {
        savingAccount.status?.active == true -> {
            Triple(
                first = MaterialTheme.colorScheme.primary,
                second = stringResource(Res.string.feature_account_active) +
                    (
                        savingAccount.lastActiveTransactionDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        } ?: ""
                        ),
                third = MaterialTheme.colorScheme.primary,
            )
        }

        savingAccount.status?.approved == true -> {
            Triple(
                first = MaterialTheme.colorScheme.secondaryContainer,
                second = stringResource(Res.string.feature_account_approved) +
                    (
                        savingAccount.timeLine?.approvedOnDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        } ?: ""
                        ),
                third = null,
            )
        }

        savingAccount.status?.submittedAndPendingApproval == true -> {
            Triple(
                first = MaterialTheme.colorScheme.tertiaryContainer,
                second = stringResource(Res.string.feature_account_submitted) +
                    (
                        savingAccount.timeLine?.submittedOnDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        } ?: ""
                        ),
                third = null,
            )
        }

        savingAccount.status?.matured == true -> {
            Triple(
                first = MaterialTheme.colorScheme.errorContainer,
                second = stringResource(Res.string.feature_account_matured) +
                    (
                        savingAccount.lastActiveTransactionDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        } ?: ""
                        ),
                third = MaterialTheme.colorScheme.errorContainer,
            )
        }

        else -> {
            Triple(
                first = MaterialTheme.colorScheme.surfaceVariant,
                second = stringResource(Res.string.feature_account_closed) + (
                    savingAccount.timeLine?.closedOnDate?.let {
                        DateHelper.getDateAsString(
                            it,
                        )
                    }
                        ?: ""
                    ),
                third = null,
            )
        }
    }

    val currencySymbolOrCode =
        savingAccount.currency?.displaySymbol ?: savingAccount.currency?.code ?: ""

    val formattedBalance = CurrencyFormatter.format(
        balance = savingAccount.accountBalance,
        currencyCode = savingAccount.currency?.code,
        maximumFractionDigits = 2,
    )

    val amountAndCurrency = stringResource(
        Res.string.feature_account_string_and_string,
        formattedBalance,
        currencySymbolOrCode,
    )

    AccountCard(
        savingAccount = savingAccount,
        statusString = statusDescription,
        balance = amountAndCurrency,
        indicatorColor = indicatorColor,
        textColor = balanceTextColor,
        onClick = {
            onAccountSelected(AccountType.SAVINGS, savingAccount.id)
        },
        modifier = modifier,
    )
}

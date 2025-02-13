/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.account.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.account.generated.resources.Res
import mifos_mobile.feature.account.generated.resources.feature_account_approved
import mifos_mobile.feature.account.generated.resources.feature_account_closed
import mifos_mobile.feature.account.generated.resources.feature_account_string_and_string
import mifos_mobile.feature.account.generated.resources.feature_account_submitted
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.feature.account.account.utils.AccountTypeItemIndicator

@Composable
internal fun SavingsAccountContent(
    accountsList: List<SavingAccount>,
    isSearching: Boolean,
    isFiltered: Boolean,
    getUpdatedSearchList: (accountsList: List<SavingAccount>) -> List<SavingAccount>,
    getUpdatedFilterList: (accountsList: List<SavingAccount>) -> List<SavingAccount>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    var accounts by rememberSaveable { mutableStateOf(accountsList) }

    accounts = when {
        isFiltered && isSearching -> {
            getUpdatedSearchList(getUpdatedFilterList(accountsList))
        }

        isSearching -> {
            getUpdatedSearchList(accountsList)
        }

        isFiltered -> {
            getUpdatedFilterList(accountsList)
        }

        else -> {
            accountsList
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyColumnState,
    ) {
        items(items = accounts) { savingAccount ->
            AccountScreenSavingsListItem(
                savingAccount = savingAccount,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun AccountScreenSavingsListItem(
    savingAccount: SavingAccount,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (color, stringResource, numColor) = when {
        savingAccount.status?.active == true -> {
            Triple(
                MaterialTheme.colorScheme.primary,
                savingAccount.lastActiveTransactionDate?.let { DateHelper.getDateAsString(it) },
                MaterialTheme.colorScheme.primary,
            )
        }

        savingAccount.status?.approved == true -> {
            Triple(
                MaterialTheme.colorScheme.secondaryContainer,
                stringResource(resource = Res.string.feature_account_approved) +
                    savingAccount.timeLine?.approvedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        savingAccount.status?.submittedAndPendingApproval == true -> {
            Triple(
                MaterialTheme.colorScheme.tertiaryContainer,
                stringResource(resource = Res.string.feature_account_submitted) +
                    savingAccount.timeLine?.submittedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        savingAccount.status?.matured == true -> {
            Triple(
                MaterialTheme.colorScheme.errorContainer,
                savingAccount.lastActiveTransactionDate?.let { DateHelper.getDateAsString(it) },
                MaterialTheme.colorScheme.errorContainer,
            )
        }

        else -> {
            Triple(
                MaterialTheme.colorScheme.surfaceVariant,
                stringResource(resource = Res.string.feature_account_closed) +
                    savingAccount.timeLine?.closedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }
    }

    Row(
        modifier = modifier.clickable {
            onItemClick.invoke(Constants.SAVINGS_ACCOUNTS, savingAccount.id)
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all = 12.dp)) {
            savingAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            savingAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (stringResource != null) {
                Text(
                    text = stringResource,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        numColor?.let { color ->
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

            Text(
                text = amountAndCurrency,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp),
                color = color,
            )
        }
    }
}

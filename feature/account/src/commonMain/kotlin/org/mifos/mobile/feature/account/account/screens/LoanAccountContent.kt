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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.account.generated.resources.Res
import mifos_mobile.feature.account.generated.resources.feature_account_approved
import mifos_mobile.feature.account.generated.resources.feature_account_closed
import mifos_mobile.feature.account.generated.resources.feature_account_disbursement
import mifos_mobile.feature.account.generated.resources.feature_account_submitted
import mifos_mobile.feature.account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.feature.account.account.utils.AccountCard

@Composable
internal fun LoanAccountContent(
    isSearching: Boolean,
    isFiltered: Boolean,
    accountsList: List<LoanAccount>,
    getUpdatedSearchList: (accountsList: List<LoanAccount>) -> List<LoanAccount>,
    getUpdatedFilterList: (accountsList: List<LoanAccount>) -> List<LoanAccount>,
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
        modifier = modifier.fillMaxSize().padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyColumnState,
    ) {
        items(items = accounts) { loanAccount ->
            AccountScreenLoanListItem(
                loanAccount = loanAccount,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun AccountScreenLoanListItem(
    loanAccount: LoanAccount,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (color, stringResource, numColor) = when {
        loanAccount.status?.active == true && loanAccount.inArrears == true -> {
            Triple(
                MaterialTheme.colorScheme.error,
                stringResource(resource = Res.string.feature_account_disbursement) +
                    loanAccount.timeline?.actualDisbursementDate?.let {
                        DateHelper.getDateAsString(it)
                    },
                MaterialTheme.colorScheme.error,
            )
        }

        loanAccount.status?.active == true -> {
            Triple(
                MaterialTheme.colorScheme.primary,
                stringResource(resource = Res.string.feature_account_disbursement) +
                    loanAccount.timeline?.actualDisbursementDate?.let {
                        DateHelper.getDateAsString(it)
                    },
                MaterialTheme.colorScheme.primary,
            )
        }

        loanAccount.status?.waitingForDisbursal == true -> {
            Triple(
                MaterialTheme.colorScheme.secondary,
                stringResource(resource = Res.string.feature_account_approved) +
                    loanAccount.timeline?.approvedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        loanAccount.status?.pendingApproval == true -> {
            Triple(
                MaterialTheme.colorScheme.tertiary,
                stringResource(resource = Res.string.feature_account_submitted) +
                    loanAccount.timeline?.submittedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        loanAccount.status?.overpaid == true -> {
            Triple(
                MaterialTheme.colorScheme.tertiaryContainer,
                stringResource(resource = Res.string.feature_account_approved) +
                    loanAccount.timeline?.actualDisbursementDate?.let {
                        DateHelper.getDateAsString(it)
                    },
                MaterialTheme.colorScheme.tertiaryContainer,
            )
        }

        loanAccount.status?.closed == true -> {
            Triple(
                MaterialTheme.colorScheme.onSurface,
                stringResource(resource = Res.string.feature_account_closed) +
                    loanAccount.timeline?.closedOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }

        else -> {
            Triple(
                MaterialTheme.colorScheme.outline,
                stringResource(resource = Res.string.feature_account_withdrawn) +
                    loanAccount.timeline?.withdrawnOnDate?.let { DateHelper.getDateAsString(it) },
                null,
            )
        }
    }

    AccountCard(
        accountNo = loanAccount.accountNo,
        productName = loanAccount.productName,
        statusString = stringResource,
        balance = CurrencyFormatter.format(
            balance = loanAccount.loanBalance,
            currencyCode = loanAccount.currency?.code,
            maximumFractionDigits = 2,
        ),
        indicatorColor = color,
        textColor = numColor,
        onClick = {
            onItemClick.invoke(Constants.LOAN_ACCOUNTS, loanAccount.id)
        },
        modifier = modifier,
    )
}

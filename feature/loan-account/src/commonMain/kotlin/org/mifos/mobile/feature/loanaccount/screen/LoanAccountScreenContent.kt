/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.screen

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
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_active
import mifos_mobile.feature.loan_account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.loan_account.generated.resources.feature_account_closed
import mifos_mobile.feature.loan_account.generated.resources.feature_account_disburse
import mifos_mobile.feature.loan_account.generated.resources.feature_account_in_arrears
import mifos_mobile.feature.loan_account.generated.resources.feature_account_overpaid
import mifos_mobile.feature.loan_account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.loanaccount.component.AccountCard

@Composable
internal fun LoanAccountScreenContent(
    accountList: List<LoanAccount>,
    onAccountSelected: (AccountType, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(
            items = accountList,
            key = { account -> account.id },
        ) { loanAccount ->
            LoanAccountListItem(
                loanAccount = loanAccount,
                onAccountSelected = onAccountSelected,
            )
        }
    }
}

@Composable
private fun LoanAccountListItem(
    loanAccount: LoanAccount,
    onAccountSelected: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (indicatorColor, accountStatus, balanceTextColor) = when {
        loanAccount.status?.active == true && loanAccount.inArrears == true -> {
            Triple(
                first = MaterialTheme.colorScheme.error,
                second = stringResource(resource = Res.string.feature_account_in_arrears) +
                    (
                        loanAccount.timeline?.actualDisbursementDate?.let {
                            DateHelper.getDateAsString(it)
                        } ?: ""
                        ),
                third = MaterialTheme.colorScheme.error,
            )
        }

        loanAccount.status?.active == true -> {
            Triple(
                first = MaterialTheme.colorScheme.primary,
                second = stringResource(resource = Res.string.feature_account_active) +
                    (
                        loanAccount.timeline?.actualDisbursementDate?.let {
                            DateHelper.getDateAsString(it)
                        } ?: ""
                        ),
                third = MaterialTheme.colorScheme.primary,
            )
        }

        loanAccount.status?.waitingForDisbursal == true -> {
            Triple(
                first = MaterialTheme.colorScheme.secondary,
                second = stringResource(resource = Res.string.feature_account_disburse) +
                    (
                        loanAccount.timeline?.approvedOnDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        }
                            ?: ""
                        ),
                third = null,
            )
        }

        loanAccount.status?.pendingApproval == true -> {
            Triple(
                first = MaterialTheme.colorScheme.tertiary,
                second = stringResource(resource = Res.string.feature_account_approval_pending) +
                    (
                        loanAccount.timeline?.submittedOnDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        }
                            ?: ""
                        ),
                third = null,
            )
        }

        loanAccount.status?.overpaid == true -> {
            Triple(
                first = MaterialTheme.colorScheme.tertiaryContainer,
                second = stringResource(resource = Res.string.feature_account_overpaid) +
                    (
                        loanAccount.timeline?.actualDisbursementDate?.let {
                            DateHelper.getDateAsString(it)
                        } ?: ""
                        ),
                third = MaterialTheme.colorScheme.tertiaryContainer,
            )
        }

        loanAccount.status?.closed == true -> {
            Triple(
                first = MaterialTheme.colorScheme.onSurface,
                second = stringResource(resource = Res.string.feature_account_closed) +
                    (
                        loanAccount.timeline?.closedOnDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        }
                            ?: ""
                        ),
                third = null,
            )
        }

        else -> {
            Triple(
                first = MaterialTheme.colorScheme.outline,
                second = stringResource(resource = Res.string.feature_account_withdrawn) +
                    (
                        loanAccount.timeline?.withdrawnOnDate?.let {
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

    val formattedBalance = CurrencyFormatter.format(
        balance = loanAccount.loanBalance,
        currencyCode = loanAccount.currency?.code,
        maximumFractionDigits = 2,
    )

    AccountCard(
        loanAccount = loanAccount,
        accountStatus = accountStatus,
        balance = formattedBalance,
        indicatorColor = indicatorColor,
        textColor = balanceTextColor,
        onClick = {
            onAccountSelected(AccountType.LOAN, loanAccount.id)
        },
        modifier = modifier,
    )
}

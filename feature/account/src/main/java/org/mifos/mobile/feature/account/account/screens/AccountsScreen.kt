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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mifos.library.pullrefresh.PullRefreshIndicator
import com.mifos.library.pullrefresh.pullRefresh
import com.mifos.library.pullrefresh.rememberPullRefreshState
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.feature.account.R
import org.mifos.mobile.feature.account.utils.AccountState
import org.mifos.mobile.feature.account.viewmodel.AccountsViewModel

@Composable
internal fun AccountsScreen(
    accountType: String,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val uiState by viewModel.accountsUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val isFiltered by viewModel.isFiltered.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadAccounts(accountType)
    }

    AccountsScreen(
        uiState = uiState,
        isRefreshing = isRefreshing,
        isFiltered = isFiltered,
        isSearching = isSearching,
        onRefresh = { viewModel.refresh(accountType) },
        onRetry = { viewModel.loadAccounts(accountType) },
        modifier = modifier,
        searchSavingsAccountList = { accountsList ->
            viewModel.searchInSavingsList(
                accountsList,
                searchQuery,
            )
        },
        searchLoanAccountList = { accountsList ->
            viewModel.searchInLoanList(
                accountsList,
                searchQuery,
            )
        },
        searchShareAccountList = { accountsList ->
            viewModel.searchInSharesList(
                accountsList,
                searchQuery,
            )
        },
        filterSavingsAccountList = { accountsList ->
            viewModel.getFilterSavingsAccountList(
                accountsList = accountsList,
                filterList = filterList,
                context = context,
            )
        },
        filterLoanAccountList = { accountsList ->
            viewModel.getFilterLoanAccountList(
                accountsList = accountsList,
                filterList = filterList,
                context = context,
            )
        },
        filterShareAccountList = { accountsList ->
            viewModel.getFilterShareAccountList(
                accountsList = accountsList,
                filterList = filterList,
                context = context,
            )
        },
        onItemClick = onItemClick,
    )
}

@Composable
private fun AccountsScreen(
    uiState: AccountState,
    isRefreshing: Boolean,
    isFiltered: Boolean,
    isSearching: Boolean,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    searchSavingsAccountList: (accountsList: List<SavingAccount>) -> List<SavingAccount>,
    searchLoanAccountList: (accountsList: List<LoanAccount>) -> List<LoanAccount>,
    searchShareAccountList: (accountsList: List<ShareAccount>) -> List<ShareAccount>,
    filterSavingsAccountList: (accountsList: List<SavingAccount>) -> List<SavingAccount>,
    filterLoanAccountList: (accountsList: List<LoanAccount>) -> List<LoanAccount>,
    filterShareAccountList: (accountsList: List<ShareAccount>) -> List<ShareAccount>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            when (uiState) {
                is AccountState.Error -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isRetryEnabled = true,
                        onRetry = onRetry,
                    )
                }

                is AccountState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is AccountState.ShowSavingsAccounts -> {
                    if ((uiState.savingAccounts.isNullOrEmpty())) {
                        EmptyDataView(
                            icon = R.drawable.feature_account_error_black,
                            error = R.string.feature_account_empty_savings_accounts,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        SavingsAccountContent(
                            accountsList = uiState.savingAccounts,
                            isSearching = isSearching,
                            isFiltered = isFiltered,
                            getUpdatedSearchList = searchSavingsAccountList,
                            onItemClick = onItemClick,
                            getUpdatedFilterList = filterSavingsAccountList,
                        )
                    }
                }

                is AccountState.ShowLoanAccounts -> {
                    if ((uiState.loanAccounts.isNullOrEmpty())) {
                        EmptyDataView(
                            icon = R.drawable.feature_account_error_black,
                            error = R.string.feature_account_empty_loan_accounts,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        LoanAccountContent(
                            accountsList = uiState.loanAccounts,
                            isSearching = isSearching,
                            isFiltered = isFiltered,
                            getUpdatedSearchList = searchLoanAccountList,
                            onItemClick = onItemClick,
                            getUpdatedFilterList = filterLoanAccountList,
                        )
                    }
                }

                is AccountState.ShowShareAccounts -> {
                    if ((uiState.shareAccounts.isNullOrEmpty())) {
                        EmptyDataView(
                            icon = R.drawable.feature_account_error_black,
                            error = R.string.feature_account_empty_share_accounts,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        AccountScreenShareContent(
                            accountsList = uiState.shareAccounts,
                            isSearching = isSearching,
                            isFiltered = isFiltered,
                            getUpdatedSearchList = searchShareAccountList,
                            getUpdatedFilterList = filterShareAccountList,
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

internal class AccountsScreenPreviewProvider : PreviewParameterProvider<AccountState> {

    override val values: Sequence<AccountState>
        get() = sequenceOf(
            AccountState.Loading,
            AccountState.Error,
            AccountState.ShowLoanAccounts(listOf()),
            AccountState.ShowShareAccounts(listOf()),
            AccountState.ShowSavingsAccounts(listOf()),
        )
}

@Preview(showSystemUi = true)
@Composable
private fun AccountSavingsScreenPreview(
    @PreviewParameter(AccountsScreenPreviewProvider::class) accountUiState: AccountState,
) {
    MifosMobileTheme {
        AccountsScreen(
            uiState = accountUiState,
            isRefreshing = true,
            isFiltered = true,
            isSearching = true,
            onRefresh = { },
            onRetry = { },
            searchSavingsAccountList = { _ -> listOf() },
            searchLoanAccountList = { _ -> listOf() },
            searchShareAccountList = { _ -> listOf() },
            filterSavingsAccountList = { _ -> listOf() },
            filterLoanAccountList = { _ -> listOf() },
            filterShareAccountList = { _ -> listOf() },
            onItemClick = { _, _ -> },
        )
    }
}

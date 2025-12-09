/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.utils

import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions

/**
 * Defines the different types of transaction filters available to the user.
 */
enum class TransactionFilterType {
    ALL,
    DEBIT,
    CREDIT,
}

/**
 * Represents the complete UI state for the RecentTransactionScreen.
 *
 * @param clientId The ID of the current user.
 * @param viewState The current display state (Loading, Content, Empty, Error).
 * @param transactions The list of transactions currently displayed.
 * @param accounts The full list of available savings accounts for the filter.
 * @param selectedAccount The currently selected account in the filter.
 * @param filterType The currently selected transaction type filter (ALL, DEBIT, CREDIT).
 * @param showFilter True if the filter bottom sheet should be visible.
 * @param isRefreshing True if a pull-to-refresh action is in progress.
 * @param isPaginating True if more data is being loaded at the end of the list.
 * @param canPaginate True if there is more data to be loaded via pagination.
 * @param isNetworkAvailable True if the device has an active network connection.
 */
data class RecentTransactionUiState(
    val clientId: Long? = null,
    val viewState: ViewState,
    val transactions: List<Transactions> = emptyList(),
    val accounts: List<SavingAccount> = emptyList(),
    val selectedAccount: SavingAccount? = null,
    val filterType: TransactionFilterType = TransactionFilterType.ALL,
    val showFilter: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val canPaginate: Boolean = false,
    val isNetworkAvailable: Boolean = false,
) {
    /**
     * Defines the specific view to be displayed within the screen's main content area.
     */
    sealed interface ViewState {
        data object Loading : ViewState
        data object Empty : ViewState
        data class Error(val message: String?) : ViewState
        data class Content(val list: List<Transactions>) : ViewState
    }
}

/**
 * Defines all possible user interactions and internal events for the RecentTransactionScreen.
 */
sealed interface RecentTransactionAction {
    // User-initiated actions
    data object LoadInitial : RecentTransactionAction
    data object Refresh : RecentTransactionAction
    data class LoadMore(val offset: Int) : RecentTransactionAction
    data object ToggleFilter : RecentTransactionAction
    data class ApplyFilter(val account: SavingAccount, val type: TransactionFilterType) : RecentTransactionAction
    data object ClearFilter : RecentTransactionAction

    // Internal events for handling async operations
    sealed interface Internal : RecentTransactionAction {
        data class AccountsLoaded(val accounts: List<SavingAccount>) : Internal
        data class TransactionsLoaded(val items: List<Transactions>) : Internal
        data class LoadFailed(val error: Throwable?) : Internal
    }
}

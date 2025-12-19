/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionAction
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionAction.Internal
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionEvent
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionUiState
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionUiState.ViewState
import org.mifos.mobile.feature.recent.transaction.utils.TransactionFilterType

/**
 * Manages the UI state and data logic for the Recent Transactions screen.
 *
 * This ViewModel is responsible for fetching all savings accounts for a client,
 * loading transaction history for a selected account,
 * and applying filters (e.g., by credit or debit)
 * to the displayed transaction list. It uses a MVI-style
 *  architecture with [RecentTransactionAction]
 * to process user intents and data-loading events.
 *
 * @param accountsRepositoryImpl Repository for fetching client accounts.
 * @param savingsAccountRepositoryImpl Repository for fetching savings
 * account details and transactions.
 * @param networkMonitor Monitors the device's network connectivity status.
 * @param userPreferencesRepository Repository for accessing stored user data like client ID.
 */
class RecentTransactionViewModel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val savingsAccountRepositoryImpl: SavingsAccountRepository,
    networkMonitor: NetworkMonitor,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RecentTransactionUiState(viewState = ViewState.Loading),
    )
    val uiState = _uiState.asStateFlow()

    /**
     * Stores the complete, unfiltered list of transactions for the selected account.
     * This is used as the source of truth when applying different filters locally.
     */
    private var originalTransactionList: List<Transactions> = emptyList()

    init {
        networkMonitor.isOnline
            .onEach { isOnline ->
                _uiState.update { it.copy(isNetworkAvailable = isOnline) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            userPreferencesRepository.clientId.collect { clientId ->
                if (clientId != null) {
                    _uiState.update { it.copy(clientId = clientId) }
                    handleAction(RecentTransactionAction.LoadInitial)
                }
            }
        }
    }

    /**
     * Central handler for all UI actions and internal data events.
     *
     * @param action The [RecentTransactionAction] to be processed.
     */
    fun handleAction(action: RecentTransactionAction) {
        when (action) {
            is RecentTransactionAction.LoadInitial -> fetchAccounts()
            is RecentTransactionAction.Refresh -> loadTransactions(isRefreshing = true)
            is RecentTransactionAction.LoadMore -> {
                if (_uiState.value.canPaginate && !_uiState.value.isPaginating) {
                    loadTransactions(isPaginating = true)
                }
            }
            is RecentTransactionAction.ToggleFilter -> {
                _uiState.update { it.copy(showFilter = !it.showFilter) }
            }
            is RecentTransactionAction.ApplyFilter -> {
                val previousAccount = _uiState.value.selectedAccount
                val newAccount = action.account
                val newType = action.type

                _uiState.update {
                    it.copy(
                        selectedAccount = newAccount,
                        filterType = newType,
                        showFilter = false,
                    )
                }

                if (previousAccount?.id != newAccount.id) {
                    loadTransactions(isRefreshing = true)
                } else {
                    applyLocalFilters()
                }
            }
            is RecentTransactionAction.ClearFilter -> {
                val firstAccount = _uiState.value.accounts.firstOrNull()
                _uiState.update {
                    it.copy(
                        selectedAccount = firstAccount,
                        filterType = TransactionFilterType.ALL,
                        showFilter = false,
                    )
                }
                loadTransactions(isRefreshing = true)
            }
            is Internal.AccountsLoaded -> {
                val defaultAccount = action.accounts.firstOrNull()
                _uiState.update {
                    it.copy(
                        accounts = action.accounts,
                        selectedAccount = defaultAccount,
                    )
                }
                loadTransactions()
            }
            is Internal.TransactionsLoaded -> {
                originalTransactionList = action.items
                applyLocalFilters()
            }
            is Internal.LoadFailed -> handleLoadFailed(action)

            is RecentTransactionAction.OnTransactionClick -> {
                val transaction = action.transaction
                val selectedAccount = _uiState.value.selectedAccount

                if (transaction.id != null && selectedAccount?.id != null) {
                    viewModelScope.launch {
                        eventChannel.send(
                            RecentTransactionEvent.NavigateToDetails(
                                transactionId = transaction.id.toString(),
                                accountType = Constants.SAVINGS_ACCOUNT,
                                accountId = selectedAccount.id,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun fetchAccounts() {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = _uiState.value.clientId,
                accountType = Constants.SAVINGS_ACCOUNTS,
            ).collect { dataState ->
                if (dataState is DataState.Success) {
                    val savingsAccounts = dataState.data.savingsAccounts.orEmpty().filter { it.status?.active == true }
                    handleAction(Internal.AccountsLoaded(savingsAccounts))
                } else if (dataState is DataState.Error) {
                    handleAction(Internal.LoadFailed(dataState.exception))
                }
            }
        }
    }

    /**
     * Loads the transaction history for the currently selected savings account.
     *
     * @param isRefreshing Indicates if the load is a pull-to-refresh action.
     * @param isPaginating Indicates if more items are being loaded for pagination.
     */
    private fun loadTransactions(
        isRefreshing: Boolean = false,
        isPaginating: Boolean = false,
    ) {
        val currentState = _uiState.value
        val selectedAccount = currentState.selectedAccount

        if (selectedAccount == null) {
            _uiState.update { it.copy(viewState = ViewState.Empty) }
            return
        }

        _uiState.update {
            it.copy(
                isRefreshing = isRefreshing,
                isPaginating = isPaginating,
                viewState = if (!isRefreshing && !isPaginating) ViewState.Loading else it.viewState,
            )
        }

        viewModelScope.launch {
            savingsAccountRepositoryImpl.getSavingsWithAssociations(
                accountId = selectedAccount.id,
                associationType = Constants.TRANSACTIONS,
            )
                .catch { e -> handleAction(Internal.LoadFailed(e)) }
                .onCompletion {
                    _uiState.update { it.copy(isRefreshing = false, isPaginating = false) }
                }
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Success -> {
                            val transactions = dataState.data.transactions ?: emptyList()
                            handleAction(Internal.TransactionsLoaded(transactions))
                        }
                        is DataState.Error -> {
                            handleAction(Internal.LoadFailed(dataState.exception))
                        }
                        is DataState.Loading -> { }
                    }
                }
        }
    }

    /**
     * Applies the selected filter ([TransactionFilterType]) to the `originalTransactionList`
     * and updates the UI state with the filtered results.
     */
    private fun applyLocalFilters() {
        val currentType = _uiState.value.filterType

        val filteredList = if (currentType == TransactionFilterType.ALL) {
            originalTransactionList
        } else {
            originalTransactionList.filter { transaction ->
                val isCredit = isTransactionCreditLogic(transaction)
                if (currentType == TransactionFilterType.CREDIT) isCredit else !isCredit
            }
        }

        _uiState.update {
            it.copy(
                transactions = filteredList,
                viewState = if (filteredList.isEmpty()) ViewState.Empty else ViewState.Content(filteredList),
                canPaginate = false,
            )
        }
    }

    /**
     * Updates the UI state to show an error message when a data loading operation fails.
     *
     * @param action The internal action containing the error details.
     */
    private fun handleLoadFailed(action: Internal.LoadFailed) {
        _uiState.update {
            it.copy(
                isRefreshing = false,
                viewState = ViewState.Error(action.error?.message),
            )
        }
    }

    /**
     * Determines if a transaction is a credit (deposit) based on its type information.
     *
     * @param transaction The transaction to analyze.
     * @return `true` if the transaction is a credit, `false` otherwise.
     */
    private fun isTransactionCreditLogic(transaction: Transactions): Boolean {
        val type = transaction.transactionType?.value?.lowercase().orEmpty()

        return when {
            transaction.transactionType?.deposit == true -> true
            transaction.transactionType?.withdrawal == true -> false
            type.contains("deposit") -> true
            type.contains("interest") -> true
            type.contains("withdrawal") -> false
            type.contains("fee") -> false
            else -> false
        }
    }
}

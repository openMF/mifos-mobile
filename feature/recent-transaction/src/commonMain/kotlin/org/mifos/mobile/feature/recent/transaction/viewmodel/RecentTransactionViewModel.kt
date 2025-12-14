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
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionAction
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionAction.Internal
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionEvent
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionUiState
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionUiState.ViewState
import org.mifos.mobile.feature.recent.transaction.utils.TransactionFilterType

class RecentTransactionViewModel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val savingsAccountRepositoryImpl: SavingsAccountRepository,
    networkMonitor: NetworkMonitor,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(
        RecentTransactionUiState(viewState = ViewState.Loading),
    )
    val uiState = mutableUiState.asStateFlow()

    private val eventChannel = Channel<RecentTransactionEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    private var originalTransactionList: List<Transactions> = emptyList()

    init {
        networkMonitor.isOnline
            .onEach { isOnline ->
                mutableUiState.update { it.copy(isNetworkAvailable = isOnline) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            userPreferencesRepository.clientId.collect { clientId ->
                if (clientId != null) {
                    mutableUiState.update { it.copy(clientId = clientId) }
                    handleAction(RecentTransactionAction.LoadInitial)
                }
            }
        }
    }

    fun handleAction(action: RecentTransactionAction) {
        when (action) {
            is RecentTransactionAction.LoadInitial -> fetchAccounts()
            is RecentTransactionAction.Refresh -> loadTransactions(isRefreshing = true)
            is RecentTransactionAction.LoadMore -> {
                if (mutableUiState.value.canPaginate && !mutableUiState.value.isPaginating) {
                    loadTransactions(isPaginating = true)
                }
            }
            is RecentTransactionAction.ToggleFilter -> {
                mutableUiState.update { it.copy(showFilter = !it.showFilter) }
            }
            is RecentTransactionAction.ApplyFilter -> {
                val previousAccount = mutableUiState.value.selectedAccount
                val newAccount = action.account
                val newType = action.type

                mutableUiState.update {
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
                val firstAccount = mutableUiState.value.accounts.firstOrNull()
                mutableUiState.update {
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
                mutableUiState.update {
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
                val selectedAccount = mutableUiState.value.selectedAccount

                if (transaction.id != null && selectedAccount?.id != null) {
                    viewModelScope.launch {
                        eventChannel.send(
                            RecentTransactionEvent.NavigateToDetails(
                                transactionId = transaction.id.toString(),
                                accountType = determineAccountType(selectedAccount),
                                accountId = selectedAccount.id,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun determineAccountType(account: Any): String {
        return when (account) {
            is SavingAccount -> Constants.SAVINGS_ACCOUNT
            is LoanAccount -> Constants.LOAN_ACCOUNT
            else -> Constants.SAVINGS_ACCOUNT // Default fallback
        }
    }

    private fun fetchAccounts() {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = mutableUiState.value.clientId,
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

    private fun loadTransactions(
        isRefreshing: Boolean = false,
        isPaginating: Boolean = false,
    ) {
        val currentState = mutableUiState.value
        val selectedAccount = currentState.selectedAccount

        if (selectedAccount == null) {
            mutableUiState.update { it.copy(viewState = ViewState.Empty) }
            return
        }

        mutableUiState.update {
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
                    mutableUiState.update { it.copy(isRefreshing = false, isPaginating = false) }
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

    private fun applyLocalFilters() {
        val currentType = mutableUiState.value.filterType

        val filteredList = if (currentType == TransactionFilterType.ALL) {
            originalTransactionList
        } else {
            originalTransactionList.filter { transaction ->
                val isCredit = isTransactionCreditLogic(transaction)
                if (currentType == TransactionFilterType.CREDIT) isCredit else !isCredit
            }
        }

        mutableUiState.update {
            it.copy(
                transactions = filteredList,
                viewState = if (filteredList.isEmpty()) ViewState.Empty else ViewState.Content(filteredList),
                canPaginate = false,
            )
        }
    }

    private fun handleLoadFailed(action: Internal.LoadFailed) {
        mutableUiState.update {
            it.copy(
                isRefreshing = false,
                viewState = ViewState.Error(action.error?.message),
            )
        }
    }

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

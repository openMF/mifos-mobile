/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * ViewModel for managing the state and business logic of the Recent Transactions feature.
 *
 * Handles account fetching, transaction synchronization, and complex filtering logic
 * based on transaction type (Credit/Debit) and specific savings accounts.
 *
 * @property accountsRepositoryImpl Accesses general account listing data.
 * @property savingsAccountRepositoryImpl Fetches detailed savings associations and transactions.
 * @property networkMonitor Observes real-time connectivity status.
 * @property userPreferencesRepository Retrieves stored user-specific identifiers like Client ID.
 */
internal class RecentTransactionViewModel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val savingsAccountRepositoryImpl: SavingsAccountRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<RecentTransactionUiState, RecentTransactionEvent, RecentTransactionAction>(
    initialState = run {
        val clientId = userPreferencesRepository.clientId.value
        RecentTransactionUiState(
            viewState = ScreenUiState.Loading,
            clientId = clientId.takeIf { it != null },
        )
    },
) {

    init {
        observeNetworkStatus()
    }

    /**
     * Observes the network connectivity status and updates the UI state accordingly.
     * If the network is unavailable, it sets the `networkStatus` flag in the state
     * and shows a network-related dialog.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(RecentTransactionAction.ReceiveNetworkResult(isOnline = isOnline))
                }
        }
    }

    private fun updateState(state: (RecentTransactionUiState) -> RecentTransactionUiState) {
        mutableStateFlow.update(state)
    }

    /**
     * Handles the network result by updating the network status and UI state.
     *
     * @param isOnline Boolean indicating whether the network is online.
     */
    private fun handleNetworkResult(isOnline: Boolean) {
        updateState {
            it.copy(networkStatus = isOnline)
        }
        if (!isOnline) {
            updateState { current ->
                if (current.viewState is ScreenUiState.Loading ||
                    current.viewState is ScreenUiState.Error ||
                    current.viewState is ScreenUiState.Empty ||
                    current.viewState is ScreenUiState.Network
                ) {
                    current.copy(viewState = ScreenUiState.Network)
                } else {
                    current
                }
            }
        } else {
            fetchAccounts()
        }
    }

    /**
     * Handles the refresh action by checking the network status and loading transactions.
     */
    private fun handleRefresh() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(viewState = ScreenUiState.Network) }
            } else {
                fetchAccounts()
            }
        }
    }

    private fun loadTransactionsAfterFilter() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(viewState = ScreenUiState.Network) }
            } else {
                loadTransactions()
            }
        }
    }

    /**
     * Handles incoming [RecentTransactionAction] from the UI.
     * Includes navigation requests, filter applications, and data refresh triggers.
     */
    override fun handleAction(action: RecentTransactionAction) {
        when (action) {
            is RecentTransactionAction.OnNavigateBackClick ->
                sendEvent(RecentTransactionEvent.NavigateBack)

            is RecentTransactionAction.ReceiveNetworkResult -> handleNetworkResult(action.isOnline)

            is RecentTransactionAction.Refresh -> handleRefresh()

            is RecentTransactionAction.ToggleFilter -> {
                updateState { it.copy(dialogState = RecentTransactionUiState.DialogState.Filters) }
            }

            is RecentTransactionAction.DismissFilter -> updateState {
                it.copy(
                    dialogState = null,
                    filterAccount = null,
                )
            }
            is RecentTransactionAction.ApplyFilter -> {
                val previousAccount = state.selectedAccount
                val newAccount = action.account
                val newType = action.type

                updateState {
                    it.copy(
                        selectedAccount = newAccount,
                        filterAccount = newAccount,
                        filterType = newType,
                        dialogState = null,
                    )
                }

                if (previousAccount?.id != newAccount.id) {
                    loadTransactionsAfterFilter()
                } else {
                    applyLocalFilters()
                }
            }
            is RecentTransactionAction.ClearFilter -> {
                val firstAccount = state.accounts.firstOrNull()
                updateState {
                    it.copy(
                        selectedAccount = firstAccount,
                        filterType = TransactionFilterType.ALL,
                        dialogState = null,
                    )
                }
                loadTransactionsAfterFilter()
            }

            is RecentTransactionAction.Internal.AccountsLoaded -> {
                val defaultAccount = action.accounts.firstOrNull()
                updateState {
                    it.copy(
                        accounts = action.accounts,
                        selectedAccount = defaultAccount,
                    )
                }
                loadTransactionsAfterFilter()
            }

            is RecentTransactionAction.Internal.HandleTransactions -> handleTransactions(action.dataState)

            is RecentTransactionAction.OnTransactionClick -> {
                val transactionId = action.transactionId
                val selectedAccount = state.selectedAccount

                if (selectedAccount?.id != null) {
                    viewModelScope.launch {
                        sendEvent(
                            RecentTransactionEvent.NavigateToDetails(
                                transactionId = transactionId.toString(),
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
        if (state.clientId == null) return

        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId,
                accountType = Constants.SAVINGS_ACCOUNTS,
            ).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        val savingsAccounts =
                            dataState.data.savingsAccounts.orEmpty().filter {
                                it.status?.active == true
                            }
                        handleAction(RecentTransactionAction.Internal.AccountsLoaded(savingsAccounts))
                    }
                    is DataState.Loading -> {
                        updateState {
                            it.copy(viewState = ScreenUiState.Loading)
                        }
                    }
                    is DataState.Error -> {
                        updateState {
                            it.copy(
                                viewState = ScreenUiState.ErrorString(
                                    dataState.exception.message ?: "Something went wrong",
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadTransactions() {
        val selectedAccount = state.selectedAccount
        if (selectedAccount == null) {
            updateState { it.copy(viewState = ScreenUiState.Empty) }
            return
        }

        updateState { it.copy(viewState = ScreenUiState.Loading) }

        viewModelScope.launch {
            savingsAccountRepositoryImpl
                .getSavingsWithAssociations(
                    accountId = selectedAccount.id,
                    associationType = Constants.TRANSACTIONS,
                )
                .catch { e ->
                    updateState {
                        it.copy(
                            viewState = ScreenUiState.ErrorString(
                                e.message ?: "Something went wrong",
                            ),
                        )
                    }
                }
                .collect { dataState ->
                    sendAction(RecentTransactionAction.Internal.HandleTransactions(dataState))
                }
        }
    }

    private fun handleTransactions(dataState: DataState<SavingsWithAssociations>) {
        when (dataState) {
            is DataState.Success -> {
                val transactions = dataState.data.transactions
                    .map { it.toUiTransaction() }

                val grouped = transactions.groupBy {
                    DateHelper.getFormattedDateWithPrefix(it.date)
                }

                updateState {
                    it.copy(
                        transactions = transactions,
                        groupedTransactions = grouped,
                        viewState = if (grouped.isEmpty()) {
                            ScreenUiState.Empty
                        } else {
                            ScreenUiState.Success
                        },
                    )
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        viewState = ScreenUiState.ErrorString(
                            dataState.exception.message ?: "Something went wrong",
                        ),
                    )
                }
            }

            is DataState.Loading -> {
                updateState { it.copy(viewState = ScreenUiState.Loading) }
            }
        }
    }

    private fun applyLocalFilters() {
        val currentType = state.filterType

        val filteredTransactions = when (currentType) {
            TransactionFilterType.ALL ->
                state.transactions

            TransactionFilterType.CREDIT ->
                state.transactions.filter { it.isCredit }

            TransactionFilterType.DEBIT ->
                state.transactions.filter { !it.isCredit }
        }

        val groupedTransactions = filteredTransactions.groupBy { transaction ->
            DateHelper.getFormattedDateWithPrefix(transaction.date)
        }

        updateState {
            it.copy(
                groupedTransactions = groupedTransactions,
                viewState = if (groupedTransactions.isEmpty()) {
                    ScreenUiState.Empty
                } else {
                    ScreenUiState.Success
                },
            )
        }
    }

    /**
     * Converts a domain `Transactions` object to a UI-friendly `UiTransaction` object.
     *
     * @return A `UiTransaction` object.
     */
    fun Transactions.toUiTransaction() = UiTransaction(
        id = id?.toLong(),
        date = date,
        amount = amount,
        type = transactionType,
        typeValue = transactionType?.value,
        isCredit = transactionType.isCredit(),
        currency = currency?.code ?: "USD",
    )

    /**
     * Determines if a transaction type represents a credit.
     *
     * @return `true` if the transaction is a credit, `false` otherwise.
     */
    internal fun TransactionType?.isCredit(): Boolean {
        return when {
            this?.deposit == true -> true
            this?.interestPosting == true -> true
            this?.rejectTransfer == true -> true

            this?.dividendPayout == true -> false
            this?.withdrawal == true -> false
            this?.feeDeduction == true -> false
            this?.initiateTransfer == true -> false
            this?.approveTransfer == true -> false
            this?.withdrawTransfer == true -> false
            this?.overdraftFee == true -> false

            else -> false
        }
    }
}

/**
 * A data class representing a transaction for the UI.
 *
 * @property id The unique ID of the transaction.
 * @property date A list of integers representing the date (e.g., [year, month, day]).
 * @property amount The transaction amount.
 * @property type The transaction type from the domain model.
 * @property typeValue The string value of the transaction type.
 * @property isCredit A boolean indicating if the transaction is a credit (true) or debit (false).
 * @property currency The currency code (e.g., "USD").
 */
internal data class UiTransaction(
    val id: Long?,
    val date: List<Int>,
    val amount: Double?,
    val type: TransactionType? = null,
    val typeValue: String? = null,
    val isCredit: Boolean,
    val currency: String,
)

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
 * @param isRefreshing True if a pull-to-refresh action is in progress.
 * @param networkStatus True if the device has an active network connection.
 */
internal data class RecentTransactionUiState(
    val clientId: Long? = null,
    val viewState: ScreenUiState,
    val dialogState: DialogState? = null,
    val transactions: List<UiTransaction> = emptyList(),
    val groupedTransactions: Map<String, List<UiTransaction>> = emptyMap(),
    val accounts: List<SavingAccount> = emptyList(),
    val selectedAccount: SavingAccount? = null,
    val filterAccount: SavingAccount? = null,
    val filterType: TransactionFilterType = TransactionFilterType.ALL,
    val isRefreshing: Boolean = false,
    val networkStatus: Boolean = false,
) {
    /**
     * Sealed interface representing the different states of the dialog.
     */
    sealed interface DialogState {
        data object Filters : DialogState
    }

    val hasActiveFilters: Boolean
        get() = filterType != TransactionFilterType.ALL ||
            filterAccount != null
}

/**
 * Defines all possible user interactions and internal events for the RecentTransactionScreen.
 */
sealed interface RecentTransactionAction {
    data object OnNavigateBackClick : RecentTransactionAction
    data object Refresh : RecentTransactionAction
    data object ToggleFilter : RecentTransactionAction
    data object DismissFilter : RecentTransactionAction
    data class ApplyFilter(val account: SavingAccount, val type: TransactionFilterType) : RecentTransactionAction
    data class ReceiveNetworkResult(val isOnline: Boolean) : RecentTransactionAction
    data object ClearFilter : RecentTransactionAction

    data class OnTransactionClick(val transactionId: Long) : RecentTransactionAction

    sealed interface Internal : RecentTransactionAction {
        data class AccountsLoaded(val accounts: List<SavingAccount>) : Internal
        data class HandleTransactions(val dataState: DataState<SavingsWithAssociations>) : Internal
    }
}

/**
 * Events sent from ViewModel to UI
 */
sealed interface RecentTransactionEvent {
    data object NavigateBack : RecentTransactionEvent

    data class NavigateToDetails(
        val transactionId: String,
        val accountType: String,
        val accountId: Long,
    ) : RecentTransactionEvent
}

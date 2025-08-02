/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccount

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.loanaccount.utils.FilterUtil
import kotlin.collections.firstOrNull

/**
 * ViewModel responsible for managing loan account UI state, fetching, filtering,
 * and reacting to user actions and network changes.
 *
 * @param accountsRepositoryImpl Provides access to account data.
 * @param networkMonitor Observes network connectivity.
 * @param userPreferencesRepositoryImpl Provides user-specific data such as client ID.
 */
class LoanAccountsViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<LoanAccountsState, LoanAccountsEvent, LoanAccountsAction>(
    initialState = LoanAccountsState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        dialogState = null,
        loanAccounts = emptyList(),
    ),
) {

    init {
        observeNetwork()
        handleAction(LoanAccountsAction.LoadAccounts(emptyList()))
    }

    override fun handleAction(action: LoanAccountsAction) {
        when (action) {
            is LoanAccountsAction.OnDismissDialog -> handleDismissDialog()

            is LoanAccountsAction.OnNavigateBack -> sendEvent(LoanAccountsEvent.NavigateBack)

            is LoanAccountsAction.ToggleAmountVisible -> handleAmountVisible()

            is LoanAccountsAction.LoadAccounts -> {
                loadAccounts(action.filters)
            }

            is LoanAccountsAction.OnRetry -> {
                loadAccounts(action.filters)
            }

            is LoanAccountsAction.OnAccountClicked ->
                sendEvent(LoanAccountsEvent.AccountClicked(action.accountId, action.accountType))

            is LoanAccountsAction.Internal.ReceiveLoanAccounts -> {
                handleReceivedAccounts(action.dataState, action.filters)
            }
        }
    }

    /**
     * Toggles visibility of the total loan amount in UI.
     */
    private fun handleAmountVisible() {
        mutableStateFlow.update {
            it.copy(isAmountVisible = !state.isAmountVisible)
        }
    }

    /**
     * Dismisses any active dialog in the UI.
     */
    private fun handleDismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    /**
     * Observes the network connectivity status and updates state accordingly.
     */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                mutableStateFlow.update {
                    it.copy(networkConnection = isConnected)
                }
            }
        }
    }

    /**
     * Fetches accounts from the repository and applies filters.
     * If cached data is available, it uses it directly.
     *
     * @param selectedFilters List of selected filters to apply.
     */
    private fun loadAccounts(
        selectedFilters: List<StringResource?>,
    ) {
        val cached = state.originalAccounts

        if (cached != null) {
            val filtered = filterAccounts(selectedFilters, cached)
            getTotalLoanAmount(filtered)

            mutableStateFlow.update {
                it.copy(
                    loanAccounts = filtered,
                    selectedFilters = selectedFilters,
                    dialogState = null,
                    isFilteredEmpty = filtered.isEmpty(),
                )
            }
            sendEvent(LoanAccountsEvent.LoadingCompleted)
            return
        }

        viewModelScope.launch {
            mutableStateFlow.update { it.copy(dialogState = LoanAccountsState.DialogState.Loading) }

            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId ?: return@launch,
                accountType = Constants.LOAN_ACCOUNTS,
            ).catch {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = LoanAccountsState.DialogState.Error("Something went wrong"),
                    )
                }
            }.collect { clientAccounts ->
                sendAction(
                    LoanAccountsAction.Internal.ReceiveLoanAccounts(
                        filters = selectedFilters,
                        dataState = clientAccounts,
                    ),
                )
            }
        }
    }

    /**
     * Handles the result of the repository call and updates the state.
     *
     * @param dataState Result of fetching loan accounts (Success, Error, Loading).
     * @param selectedFilters Filters applied to the list.
     */
    private fun handleReceivedAccounts(
        dataState: DataState<ClientAccounts>,
        selectedFilters: List<StringResource?>,
    ) {
        when (dataState) {
            is DataState.Error -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = LoanAccountsState.DialogState.Error("Something went wrong"),
                    )
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(dialogState = LoanAccountsState.DialogState.Loading)
                }
            }

            is DataState.Success -> {
                val loanAccounts = dataState.data.loanAccounts
                val filtered = filterAccounts(selectedFilters, loanAccounts)
                getTotalLoanAmount(dataState.data.loanAccounts)
                mutableStateFlow.update {
                    it.copy(
                        items = filtered.size,
                        isEmpty = loanAccounts.isEmpty(),
                        isFilteredEmpty = filtered.isEmpty(),
                        loanAccounts = filtered,
                        originalAccounts = loanAccounts,
                        currency = loanAccounts.firstOrNull()?.currency?.displaySymbol,
                        dialogState = null,
                        selectedFilters = selectedFilters,
                    )
                }
            }
        }
    }

    /**
     * Filters the accounts based on the selected filters (status).
     *
     * @param selectedFilters List of selected labels for filtering.
     * @param accounts Original unfiltered list of accounts.
     * @return List of accounts that match the applied filters.
     */
    private fun filterAccounts(
        selectedFilters: List<StringResource?>,
        accounts: List<LoanAccount>,
    ): List<LoanAccount> {
        val filteredByStatus = if (selectedFilters.isNotEmpty()) {
            selectedFilters
                .mapNotNull { FilterUtil.fromLabel(it) }
                .flatMap { filter -> accounts.filter(filter.matchCondition) }
        } else {
            accounts
        }

        return filteredByStatus.distinct()
    }

    /**
     * Calculates the total loan balance and updates state.
     *
     * @param accounts List of [LoanAccount] to compute totals from.
     */
    private fun getTotalLoanAmount(accounts: List<LoanAccount>?) {
        var amount = 0.0
        var items = 0
        if (accounts != null) {
            for (account in accounts) {
                amount += account.loanBalance
                items++
            }
        }

        mutableStateFlow.update {
            it.copy(totalLoanAmount = amount, items = items)
        }
    }
}

/**
 * State holder for the Loan Account screen.
 * Contains all values needed to render the UI and manage logic.
 */
data class LoanAccountsState(
    val loanAccounts: List<LoanAccount>?,
    val originalAccounts: List<LoanAccount>? = null,
    val isEmpty: Boolean = false,
    val isFilteredEmpty: Boolean = false,

    /** Number of filtered accounts */
    val items: Int? = 0,

    /** Total loan amount computed from accounts */
    val totalLoanAmount: Double? = 0.0,

    /** Currency symbol (e.g., ₹, $, etc.) */
    val currency: String? = "",

    /** Network connectivity status */
    val networkConnection: Boolean? = true,

    /** Current client ID from user preferences */
    val clientId: Long?,

    /** Currently active dialog (Loading/Error) */
    val dialogState: DialogState?,

    /** Filters currently applied */
    val selectedFilters: List<StringResource?> = emptyList(),

    /** Controls whether account balances are visible */
    val isAmountVisible: Boolean = false,
) {

    /**
     * Sealed class representing possible dialog states.
     */
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
    }
}

/**
 * Represents user or system actions for the Loan Account screen.
 */
sealed interface LoanAccountsAction {

    /** Dismiss any open dialog */
    data object OnDismissDialog : LoanAccountsAction

    /** Navigate back from the screen */
    data object OnNavigateBack : LoanAccountsAction

    /** Toggle visibility of loan amount */
    data object ToggleAmountVisible : LoanAccountsAction

    data class OnRetry(
        val filters: List<StringResource?>,
    ) : LoanAccountsAction

    /** Load loan accounts with applied filters */
    data class LoadAccounts(
        val filters: List<StringResource?>,
    ) : LoanAccountsAction

    /** Navigate to a selected account's detail page */
    data class OnAccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : LoanAccountsAction

    /**
     * Internal-only actions triggered by repository/data flow.
     */
    sealed interface Internal : LoanAccountsAction {

        /** Called when account data is received from repository */
        data class ReceiveLoanAccounts(
            val filters: List<StringResource?>,
            val dataState: DataState<ClientAccounts>,
        ) : LoanAccountsAction
    }
}

/**
 * One-time UI events for the Loan Account screen.
 */
sealed interface LoanAccountsEvent {

    /** Trigger navigation to selected account's detail screen */
    data class AccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : LoanAccountsEvent

    /** Signals the UI that loading is complete */
    data object LoadingCompleted : LoanAccountsEvent

    /** Navigates back to the previous screen */
    data object NavigateBack : LoanAccountsEvent
}

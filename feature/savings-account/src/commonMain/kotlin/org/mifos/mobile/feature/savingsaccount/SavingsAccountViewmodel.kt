/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount

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
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.savingsaccount.utils.FilterUtil
import kotlin.collections.orEmpty

/**
 * ViewModel responsible for managing savings account UI state, fetching, filtering,
 * and reacting to user actions and network changes.
 *
 * @param accountsRepositoryImpl Provides access to account data.
 * @param networkMonitor Observes network connectivity.
 * @param userPreferencesRepositoryImpl Provides user-specific data such as client ID.
 */
class SavingsAccountViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<SavingsAccountState, SavingsAccountsEvent, SavingsAccountAction>(
    initialState = SavingsAccountState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        dialogState = null,
        savingsAccount = emptyList(),
    ),
) {

    init {
        observeNetwork()
        handleAction(SavingsAccountAction.LoadAccounts(emptyList()))
    }

    override fun handleAction(action: SavingsAccountAction) {
        when (action) {
            is SavingsAccountAction.OnDismissDialog -> handleDismissDialog()

            is SavingsAccountAction.OnNavigateBack -> sendEvent(SavingsAccountsEvent.NavigateBack)

            is SavingsAccountAction.ToggleAmountVisible -> handleAmountVisible()

            is SavingsAccountAction.LoadAccounts -> {
                loadAccounts(action.filters)
            }

            is SavingsAccountAction.OnAccountClicked ->
                sendEvent(SavingsAccountsEvent.AccountClicked(action.accountId, action.accountType))

            is SavingsAccountAction.Internal.ReceiveSavingsAccounts -> {
                handleReceivedAccounts(action.dataState, action.filters)
            }
        }
    }

    /**
     * Toggles visibility of the total savings amount in UI.
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
            getTotalSavingAmount(filtered)

            mutableStateFlow.update {
                it.copy(
                    savingsAccount = filtered,
                    selectedFilters = selectedFilters,
                    dialogState = null,
                )
            }
            sendEvent(SavingsAccountsEvent.LoadingCompleted)
            return
        }

        viewModelScope.launch {
            mutableStateFlow.update { it.copy(dialogState = SavingsAccountState.DialogState.Loading) }

            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId ?: return@launch,
                accountType = Constants.SAVINGS_ACCOUNTS,
            ).catch {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = SavingsAccountState.DialogState.Error("Something went wrong"),
                    )
                }
            }.collect { clientAccounts ->
                sendAction(
                    SavingsAccountAction.Internal.ReceiveSavingsAccounts(
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
     * @param dataState Result of fetching savings accounts (Success, Error, Loading).
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
                        dialogState = SavingsAccountState.DialogState.Error("Something went wrong"),
                    )
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(dialogState = SavingsAccountState.DialogState.Loading)
                }
            }

            is DataState.Success -> {
                val allSavings = dataState.data.savingsAccounts.orEmpty()
                val filtered = filterAccounts(selectedFilters, allSavings)
                getTotalSavingAmount(dataState.data.savingsAccounts)
                mutableStateFlow.update {
                    it.copy(
                        items = filtered.size,
                        savingsAccount = filtered,
                        originalAccounts = allSavings,
                        currency = allSavings.firstOrNull()?.currency?.displaySymbol,
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
        accounts: List<SavingAccount>,
    ): List<SavingAccount> {
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
     * Calculates the total savings balance and updates state.
     *
     * @param accounts List of [SavingAccount] to compute totals from.
     */
    private fun getTotalSavingAmount(accounts: List<SavingAccount>?) {
        var amount = 0.0
        var items = 0
        if (accounts != null) {
            for (account in accounts) {
                amount += account.accountBalance
                items++
            }
        }

        mutableStateFlow.update {
            it.copy(totalSavingAmount = amount, items = items)
        }
    }
}

/**
 * State holder for the Savings Account screen.
 * Contains all values needed to render the UI and manage logic.
 */
data class SavingsAccountState(
    val savingsAccount: List<SavingAccount>?,
    val originalAccounts: List<SavingAccount>? = null,

    /** Number of filtered accounts */
    val items: Int? = 0,

    /** Total savings amount computed from accounts */
    val totalSavingAmount: Double? = 0.0,

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
 * Represents user or system actions for the Savings Account screen.
 */
sealed interface SavingsAccountAction {

    /** Dismiss any open dialog */
    data object OnDismissDialog : SavingsAccountAction

    /** Navigate back from the screen */
    data object OnNavigateBack : SavingsAccountAction

    /** Toggle visibility of savings amount */
    data object ToggleAmountVisible : SavingsAccountAction

    /** Load savings accounts with applied filters */
    data class LoadAccounts(
        val filters: List<StringResource?>,
    ) : SavingsAccountAction

    /** Navigate to a selected account's detail page */
    data class OnAccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : SavingsAccountAction

    /**
     * Internal-only actions triggered by repository/data flow.
     */
    sealed interface Internal : SavingsAccountAction {

        /** Called when account data is received from repository */
        data class ReceiveSavingsAccounts(
            val filters: List<StringResource?>,
            val dataState: DataState<ClientAccounts>,
        ) : SavingsAccountAction
    }
}

/**
 * One-time UI events for the Savings Account screen.
 */
sealed interface SavingsAccountsEvent {

    /** Trigger navigation to selected account's detail screen */
    data class AccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : SavingsAccountsEvent

    /** Signals the UI that loading is complete */
    data object LoadingCompleted : SavingsAccountsEvent

    /** Navigates back to the previous screen */
    data object NavigateBack : SavingsAccountsEvent
}

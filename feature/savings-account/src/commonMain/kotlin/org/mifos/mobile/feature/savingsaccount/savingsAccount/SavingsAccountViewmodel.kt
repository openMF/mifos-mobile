/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccount

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_generic_error_server
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.SavingStatus
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
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
    }

    /** Observes the network connectivity status and updates state accordingly. */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(SavingsAccountAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handles changes in network connectivity.
     *
     * It updates the `networkStatus` state. If the network is offline, it sets the
     * `uiState` to [ScreenUiState.Network]. If the network is online, it
     * automatically triggers a data fetch to refresh the content.
     *
     * @param isOnline A boolean indicating the current network status.
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is ScreenUiState.Loading ||
                        current.uiState is ScreenUiState.Error ||
                        current.uiState is ScreenUiState.Empty ||
                        current.uiState is ScreenUiState.Network
                    ) {
                        current.copy(uiState = ScreenUiState.Network)
                    } else {
                        current
                    }
                }
            } else {
                sendAction(SavingsAccountAction.LoadAccounts(emptyList()))
            }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (SavingsAccountState) -> SavingsAccountState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: SavingsAccountAction) {
        when (action) {
            is SavingsAccountAction.OnDismissDialog -> handleDismissDialog()

            is SavingsAccountAction.OnNavigateBack -> sendEvent(SavingsAccountsEvent.NavigateBack)

            is SavingsAccountAction.ToggleAmountVisible -> handleAmountVisible()

            is SavingsAccountAction.LoadAccounts -> {
                loadAccounts(action.filters)
            }

            is SavingsAccountAction.OnFirstLaunched -> {
                updateState {
                    it.copy(firstLaunch = false)
                }
            }

            is SavingsAccountAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is SavingsAccountAction.OnAccountClicked ->
                sendEvent(SavingsAccountsEvent.AccountClicked(action.accountId, action.accountType))

            is SavingsAccountAction.Internal.ReceiveSavingsAccounts -> {
                handleReceivedAccounts(action.dataState, action.filters)
            }

            SavingsAccountAction.OnRetry -> retry()
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                handleAction(SavingsAccountAction.LoadAccounts(emptyList()))
            }
        }
    }

    /**
     * Toggles visibility of total savings amount in UI.
     * */
    private fun handleAmountVisible() {
        mutableStateFlow.update {
            it.copy(isAmountVisible = !state.isAmountVisible)
        }
    }

    /** Dismisses any active dialog. */
    private fun handleDismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
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
        viewModelScope.launch {
            updateState { it.copy(uiState = ScreenUiState.Loading) }
            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId ?: return@launch,
                accountType = Constants.SAVINGS_ACCOUNTS,
            ).collect { clientAccounts ->
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
        sendEvent(SavingsAccountsEvent.LoadingCompleted)
        when (dataState) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = if (dataState.exception.cause is IOException) {
                            ScreenUiState.Network
                        } else {
                            ScreenUiState.Error(Res.string.feature_generic_error_server)
                        },
                    )
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(uiState = ScreenUiState.Loading)
                }
            }

            is DataState.Success -> {
                val allSavings = dataState.data.savingsAccounts.orEmpty()
                val filtered = filterAccounts(selectedFilters, allSavings)
                val sortedAccounts = sortAccountsByStatus(filtered)
                updateState {
                    it.copy(
                        decimals = sortedAccounts.firstOrNull()?.currency?.decimalPlaces
                            ?: allSavings.firstOrNull()?.currency?.decimalPlaces ?: 2,
                    )
                }

                if (allSavings.isNotEmpty()) {
                    getTotalSavingAmount(dataState.data.savingsAccounts)
                }

                updateState {
                    val isEmptyAccounts = allSavings.isEmpty()
                    val isFilteredEmpty = sortedAccounts.isEmpty()

                    it.copy(
                        items = sortedAccounts.size,
                        isFilteredEmpty = isFilteredEmpty,
                        savingsAccount = sortedAccounts,
                        originalAccounts = allSavings,
                        selectedFilters = selectedFilters,
                        currency = allSavings.firstOrNull()?.currency?.displaySymbol,
                        uiState = if (isEmptyAccounts) {
                            ScreenUiState.Empty
                        } else {
                            ScreenUiState.Success
                        },
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
    private fun sortAccountsByStatus(accounts: List<SavingAccount>): List<SavingAccount> {
        return accounts.sortedBy {
            runCatching {
                SavingStatus.fromStatus(it.status?.value.orEmpty()).sortOrder
            }.getOrElse { Int.MAX_VALUE }
        }
    }

    /** Calculates total savings balance and updates state. */
    private fun getTotalSavingAmount(accounts: List<SavingAccount>?) {
        var amount = 0.0
        var items = 0
        if (accounts != null) {
            for (account in accounts) {
                amount += account.accountBalance
                items++
            }
        }
        val balance = CurrencyFormatter.format(
            amount,
            accounts?.firstOrNull()?.currency?.code,
            state.decimals,
        )

        mutableStateFlow.update {
            it.copy(totalSavingAmount = balance, items = items)
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

    val firstLaunch: Boolean = true,

    /** Number of filtered accounts */
    val items: Int? = 0,

    /** Total savings amount computed from accounts */
    val totalSavingAmount: String? = "",

    /** Currency symbol (e.g., ₹, $, etc.) */
    val currency: String? = "",

    /** Decimals to display amount*/
    val decimals: Int? = 2,

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

    val isFilteredEmpty: Boolean = false,

    val uiState: ScreenUiState? = ScreenUiState.Loading,

    val networkStatus: Boolean = false,

    /** Order of statuses for consistent sorting */
    val statusOrder: List<String> = listOf(
        SavingStatus.ACTIVE.status,
        SavingStatus.SUBMIT_AND_PENDING_APPROVAL.status,
        SavingStatus.CLOSED.status,
        SavingStatus.INACTIVE.status,
    ),
) {

    /**
     * Sealed class representing possible dialog states.
     */
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

/**
 * Represents user or system actions for the Savings Account screen.
 */
sealed interface SavingsAccountAction {
    data object OnFirstLaunched : SavingsAccountAction

    data object OnRetry : SavingsAccountAction

    /** Dismiss any open dialog */
    data object OnDismissDialog : SavingsAccountAction

    /** Navigate back from the screen */
    data object OnNavigateBack : SavingsAccountAction

    /** Toggle visibility of savings amount */
    data object ToggleAmountVisible : SavingsAccountAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : SavingsAccountAction

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

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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_generic_error_server
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.AccountUiModel
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.loanaccount.utils.FilterUtil
import kotlin.collections.firstOrNull

/**
 * ViewModel for the loan accounts screen.
 * It is responsible for fetching and filtering loan accounts, and for handling user actions.
 *
 * @param accountsRepositoryImpl The repository for fetching account data.
 * @param networkMonitor The network monitor to observe network connectivity.
 * @param userPreferencesRepositoryImpl The repository for user preferences.
 */
class LoanAccountsViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<LoanAccountsState, LoanAccountsEvent, LoanAccountsAction>(
    initialState = LoanAccountsState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
//        loanAccounts = emptyList(),
    ),
) {

    init {
        observeNetwork()
    }

    /**
     * Observes the network connectivity status and updates state accordingly.
     */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(LoanAccountsAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    override fun handleAction(action: LoanAccountsAction) {
        when (action) {
            is LoanAccountsAction.OnDismissDialog -> handleDismissDialog()

            is LoanAccountsAction.OnNavigateBack -> sendEvent(LoanAccountsEvent.NavigateBack)

            is LoanAccountsAction.ToggleAmountVisible -> handleAmountVisible()

            is LoanAccountsAction.LoadAccounts -> {
                loadAccounts(action.filters)
            }

            is LoanAccountsAction.OnFirstLaunched -> {
                updateState {
                    it.copy(firstLaunch = false)
                }
            }

            is LoanAccountsAction.OnRetry -> retry()

            is LoanAccountsAction.OnAccountClicked ->
                sendEvent(LoanAccountsEvent.AccountClicked(action.accountId, action.accountType))

            is LoanAccountsAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (LoanAccountsState) -> LoanAccountsState) {
        mutableStateFlow.update(update)
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
                sendAction(LoanAccountsAction.LoadAccounts(emptyList()))
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `loadAccounts` `fetchClient`,
     * `fetchLonPurpose` function.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                handleAction(LoanAccountsAction.LoadAccounts(emptyList()))
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
     * Fetches accounts from the repository and applies filters.
     * If cached data is available, it uses it directly.
     *
     */
    private fun loadAccounts(
        selectedFilters: List<StringResource?>,
    ) {

        val clientId = state.clientId ?: return

        val flow =
            accountsRepositoryImpl
                .loadAccounts(
                    clientId = clientId,
                    accountType = Constants.LOAN_ACCOUNTS
                )
                .map { pagingData ->
                    pagingData.filter { account ->
                        filterAccounts(account, selectedFilters)
                    }
                }
                .cachedIn(viewModelScope)

        updateState {
            it.copy(
                accountsFlow = flow,
                selectedFilters = selectedFilters
            )
        }
    }

    fun onVisibleItemsChanged(items: List<AccountUiModel>) {
        val loans = items.filterIsInstance<AccountUiModel.Loan>()
        if (loans.isEmpty()) return

        val amount = loans.sumOf { it.data.loanBalance }

        val currency = loans.first().data.currency
        val formatted = CurrencyFormatter.format(
            amount,
            currency?.code,
            currency?.decimalPlaces?.toInt() ?: 2
        )

        updateState {
            it.copy(
                totalLoanAmount = formatted,
                currency = currency?.displaySymbol,
                decimals = currency?.decimalPlaces?.toInt() ?: 2
            )
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
        account: AccountUiModel,
        selectedFilters: List<StringResource?>
    ): Boolean {
        if (selectedFilters.isEmpty()) return true

        return when (account) {
            is AccountUiModel.Loan ->
                selectedFilters
                    .mapNotNull { FilterUtil.fromLabel(it) }
                    .any { it.matchCondition(account.data) }

            else -> false
        }
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

        val balance = CurrencyFormatter.format(
            amount,
            accounts?.firstOrNull()?.currency?.code,
            state.decimals,
        )

        mutableStateFlow.update {
            it.copy(totalLoanAmount = balance, items = items)
        }
    }
}

/**
 * Represents the state of the loan accounts screen.
 *
 * @property loanAccounts The list of loan accounts to display.
 * @property originalAccounts The original, unfiltered list of loan accounts.
 * @property isFilteredEmpty Whether the filtered list of accounts is empty.
 * @property firstLaunch Whether this is the first time the screen is launched.
 * @property items The number of filtered accounts.
 * @property totalLoanAmount The total loan amount computed from the accounts.
 * @property currency The currency symbol (e.g., ₹, $, etc.).
 * @property decimals The number of decimals to display for the amount.
 * @property clientId The current client ID from user preferences.
 * @property dialogState The currently active dialog (Error).
 * @property selectedFilters The filters currently applied.
 * @property isAmountVisible Whether the account balances are visible.
 * @property networkStatus The network connectivity status.
 * @property uiState The overall state of the screen.
 */
data class LoanAccountsState(

    val accountsFlow: Flow<PagingData<AccountUiModel>>? = null,

//    val loanAccounts: List<LoanAccount>?,
//    val originalAccounts: List<LoanAccount>? = null,
    val isFilteredEmpty: Boolean = false,

    val firstLaunch: Boolean = true,

    /** Number of filtered accounts */
    val items: Int? = 0,

    /** Total loan amount computed from accounts */
    val totalLoanAmount: String? = "",

    /** Currency symbol (e.g., ₹, $, etc.) */
    val currency: String? = "",

    /** Decimals to display amount*/
    val decimals: Int? = 2,

    /** Current client ID from user preferences */
    val clientId: Long?,

    /** Currently active dialog (Error) */
    val dialogState: DialogState? = null,

    /** Filters currently applied */
    val selectedFilters: List<StringResource?> = emptyList(),

    /** Controls whether account balances are visible */
    val isAmountVisible: Boolean = false,

    /** Network connectivity status */
    val networkStatus: Boolean = false,

    /** Hold the state of the screen */
    val uiState: ScreenUiState? = ScreenUiState.Loading,

) {

    /**
     * Sealed class representing possible dialog states.
     */
    sealed interface DialogState {
        /**
         * An error dialog with a message.
         *
         * @param message The error message to display.
         */
        data class Error(val message: String) : DialogState
    }
}

/**
 * Represents the actions that can be taken on the loan accounts screen.
 */
sealed interface LoanAccountsAction {

    /**
     * Action to indicate that the screen is being launched for the first time.
     */
    data object OnFirstLaunched : LoanAccountsAction

    /**
     * Action to dismiss any open dialog.
     */
    data object OnDismissDialog : LoanAccountsAction

    /**
     * Action to navigate back from the screen.
     */
    data object OnNavigateBack : LoanAccountsAction

    /**
     * Action to toggle the visibility of the loan amount.
     */
    data object ToggleAmountVisible : LoanAccountsAction

    /**
     * Action to retry a failed operation.
     */
    data object OnRetry : LoanAccountsAction

    /**
     * Action to load the loan accounts with the given filters.
     *
     * @param filters The filters to apply.
     */
    data class LoadAccounts(
        val filters: List<StringResource?>,
    ) : LoanAccountsAction

    /**
     * Action to handle a click on a loan account.
     *
     * @param accountId The ID of the clicked account.
     * @param accountType The type of the clicked account.
     */
    data class OnAccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : LoanAccountsAction

    /**
     * Action to receive the network status.
     *
     * @param isOnline Whether the device is online.
     */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : LoanAccountsAction

}

/**
 * Represents the one-time events that can be sent from the ViewModel to the UI.
 */
sealed interface LoanAccountsEvent {

    /**
     * Event to trigger navigation to the selected account's detail screen.
     *
     * @param accountId The ID of the clicked account.
     * @param accountType The type of the clicked account.
     */
    data class AccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : LoanAccountsEvent

    /**
     * Event to signal that the loading is complete.
     */
    data object LoadingCompleted : LoanAccountsEvent

    /**
     * Event to navigate back to the previous screen.
     */
    data object NavigateBack : LoanAccountsEvent
}

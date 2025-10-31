/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.shareAccount

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_generic_error_server
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.shareaccount.utils.FilterUtil

// TODO: Refactor according to figma design
/**
 * ViewModel responsible for managing share account UI state, fetching,
 * filtering, and reacting to user actions and network changes.
 *
 * @param accountsRepositoryImpl Provides access to account data.
 * @param networkMonitor Observes network connectivity.
 * @param userPreferencesRepositoryImpl Provides user-specific data such as client ID.
 */
class ShareAccountsViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<ShareAccountsState, ShareAccountsEvent, ShareAccountsAction>(
    initialState = ShareAccountsState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        shareAccounts = emptyList(),
    ),
) {

    init {
        observeNetwork()
    }

    override fun handleAction(action: ShareAccountsAction) {
        when (action) {
            is ShareAccountsAction.OnDismissDialog -> handleDismissDialog()

            is ShareAccountsAction.OnNavigateBack -> sendEvent(ShareAccountsEvent.NavigateBack)

            is ShareAccountsAction.ToggleAmountVisible -> handleAmountVisible()

            is ShareAccountsAction.LoadAccounts -> loadAccounts(action.filters)

            is ShareAccountsAction.OnFirstLaunched -> {
                updateState {
                    it.copy(firstLaunch = false)
                }
            }

            is ShareAccountsAction.OnRetry -> retry()

            is ShareAccountsAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is ShareAccountsAction.OnAccountClicked -> sendEvent(
                ShareAccountsEvent.AccountClicked(action.accountId, action.accountType),
            )

            is ShareAccountsAction.Internal.ReceiveShareAccounts -> {
                handleReceivedAccounts(action.dataState, action.filters)
            }
        }
    }

    /**
     * Toggles visibility of the total share amount in the UI.
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
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(ShareAccountsAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (ShareAccountsState) -> ShareAccountsState) {
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
                sendAction(ShareAccountsAction.LoadAccounts(emptyList()))
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `loadAccounts` function.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                handleAction(ShareAccountsAction.LoadAccounts(emptyList()))
            }
        }
    }

    /**
     * Fetches share accounts from the repository and applies filters.
     * If cached data is available, it uses it directly.
     *
     * @param selectedFilters List of selected filters to apply.
     */
    private fun loadAccounts(
        selectedFilters: List<StringResource?>,
    ) {
        viewModelScope.launch {
            mutableStateFlow.update { it.copy(uiState = ScreenUiState.Loading) }

            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId ?: return@launch,
                accountType = Constants.SHARE_ACCOUNTS,
            ).collect { clientAccounts ->
                sendAction(
                    ShareAccountsAction.Internal.ReceiveShareAccounts(
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
     * @param dataState Result of fetching share accounts (Success, Error, Loading).
     * @param selectedFilters Filters applied to the list.
     */
    private fun handleReceivedAccounts(
        dataState: DataState<ClientAccounts>,
        selectedFilters: List<StringResource?>,
    ) {
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
                updateState {
                    it.copy(uiState = ScreenUiState.Loading)
                }
            }

            is DataState.Success -> {
                val shareAccounts = dataState.data.shareAccounts
                val filtered = filterAccounts(selectedFilters, shareAccounts)
                mutableStateFlow.update {
                    it.copy(
                        decimals = filtered.firstOrNull()?.currency?.decimalPlaces ?: 2,
                    )
                }

                if (shareAccounts.isNotEmpty()) {
                    getTotalShareAmount(shareAccounts)
                }

                updateState {
                    val isEmptyAccounts = shareAccounts.isEmpty()
                    val isFilteredEmpty = filtered.isEmpty()

                    it.copy(
                        items = filtered.size,
                        isFilteredEmpty = isFilteredEmpty,
                        currency = shareAccounts.firstOrNull()?.currency?.displaySymbol,
                        shareAccounts = filtered,
                        originalAccounts = shareAccounts,
                        selectedFilters = selectedFilters,
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
     * Filters the share accounts based on the selected filters (status).
     *
     * @param selectedFilters List of selected labels for filtering.
     * @param accounts Original unfiltered list of share accounts.
     * @return List of share accounts that match the applied filters.
     */
    private fun filterAccounts(
        selectedFilters: List<StringResource?>,
        accounts: List<ShareAccount>,
    ): List<ShareAccount> {
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
     * Calculates the total share balance and updates state.
     *
     * @param accounts List of [ShareAccount] to compute totals from.
     */
    @Suppress("UnusedParameter")
    private fun getTotalShareAmount(accounts: List<ShareAccount>?) {
        var amount = 0.0
        var items = 0

        val balance = CurrencyFormatter.format(
            amount,
            accounts?.firstOrNull()?.currency?.code,
            state.decimals,
        )
        // Future logic to calculate approved share amount can be added here

        mutableStateFlow.update {
            it.copy(totalLoanAmount = balance, items = items)
        }
    }
}

/**
 * State holder for the Share Accounts screen.
 * Contains all values needed to render the UI and manage logic.
 *
 * @property shareAccounts The list of share accounts.
 * @property originalAccounts The original list of share accounts.
 * @property isFilteredEmpty A flag indicating if the filtered list is empty.
 * @property firstLaunch A flag indicating if this is the first launch of the screen.
 * @property items The number of filtered accounts.
 * @property totalLoanAmount The total share amount computed from accounts.
 * @property currency The currency symbol (e.g., ₹, $, etc.).
 * @property decimals The number of decimals to display for the amount.
 * @property networkConnection The network connectivity status.
 * @property clientId The current client ID from user preferences.
 * @property dialogState The currently active dialog (Error).
 * @property selectedFilters The filters currently applied.
 * @property isAmountVisible A flag to control whether account balances are visible.
 * @property uiState The current UI state of the screen.
 * @property networkStatus The network connectivity status.
 */
data class ShareAccountsState(
    val shareAccounts: List<ShareAccount>?,
    val originalAccounts: List<ShareAccount>? = null,
    val isFilteredEmpty: Boolean = false,
    val firstLaunch: Boolean = true,
    val items: Int? = 0,
    val totalLoanAmount: String? = "",
    val currency: String? = "",
    val decimals: Int? = 2,
    val networkConnection: Boolean? = true,
    val clientId: Long?,
    val dialogState: DialogState? = null,
    val selectedFilters: List<StringResource?> = emptyList(),
    val isAmountVisible: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
    val networkStatus: Boolean = false,
) {
    /**
     * Represents UI dialog states.
     */
    sealed interface DialogState {
        /**
         * An error dialog state.
         *
         * @property message The error message to display.
         */
        data class Error(val message: String) : DialogState
    }
}

/**
 * Represents user or system actions for the Share Accounts screen.
 */
sealed interface ShareAccountsAction {
    /**
     * Action triggered on the first launch of the screen.
     */
    data object OnFirstLaunched : ShareAccountsAction

    /**
     * Action to dismiss any open dialog.
     */
    data object OnDismissDialog : ShareAccountsAction

    /**
     * Action to navigate back from the screen.
     */
    data object OnNavigateBack : ShareAccountsAction

    /**
     * Action to toggle the visibility of the share amount.
     */
    data object ToggleAmountVisible : ShareAccountsAction

    /**
     * Action to load share accounts with applied filters.
     *
     * @property filters The list of filters to apply.
     */
    data class LoadAccounts(val filters: List<StringResource?>) : ShareAccountsAction

    /**
     * Action to retry loading with the same filters.
     */
    data object OnRetry : ShareAccountsAction

    /**
     * Action triggered when an account is clicked.
     *
     * @property accountId The ID of the clicked account.
     * @property accountType The type of the clicked account.
     */
    data class OnAccountClicked(val accountId: Long, val accountType: String) : ShareAccountsAction

    /**
     * Action to observe the network status.
     *
     * @property isOnline A boolean indicating if the device is online.
     */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : ShareAccountsAction

    /**
     * Internal-only actions triggered by repository/data flow.
     */
    sealed interface Internal : ShareAccountsAction {

        /**
         * Called when share account data is received from the repository.
         *
         * @property filters The list of filters applied.
         * @property dataState The result of fetching the share accounts.
         */
        data class ReceiveShareAccounts(
            val filters: List<StringResource?>,
            val dataState: DataState<ClientAccounts>,
        ) : Internal
    }
}

/**
 * One-time UI events for the Share Accounts screen.
 */
sealed interface ShareAccountsEvent {

    /**
     * Trigger navigation to the selected share account's detail screen.
     *
     * @property accountId The ID of the clicked account.
     * @property accountType The type of the clicked account.
     */
    data class AccountClicked(val accountId: Long, val accountType: String) : ShareAccountsEvent

    /**
     * Signals the UI that loading is complete.
     */
    data object LoadingCompleted : ShareAccountsEvent

    /**
     * Navigates back to the previous screen.
     */
    data object NavigateBack : ShareAccountsEvent
}

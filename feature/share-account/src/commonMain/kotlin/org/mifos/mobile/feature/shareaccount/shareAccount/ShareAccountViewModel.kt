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
import org.mifos.mobile.core.ui.utils.ScreenUiState.Network
import org.mifos.mobile.feature.shareaccount.utils.FilterUtil
import kotlin.collections.firstOrNull

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
     * a network error dialog. Otherwise, it triggers the `loadAccounts` `fetchClient`,
     * `fetchLonPurpose` function.
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
 */
data class ShareAccountsState(
    val shareAccounts: List<ShareAccount>?,
    val originalAccounts: List<ShareAccount>? = null,
    val isFilteredEmpty: Boolean = false,
    val firstLaunch: Boolean = true,

    /** Number of filtered accounts */
    val items: Int? = 0,

    /** Total share amount computed from accounts */
    val totalLoanAmount: String? = "",

    /** Currency symbol (e.g., ₹, $, etc.) */
    val currency: String? = "",

    /** Decimals to display amount*/
    val decimals: Int? = 2,

    /** Network connectivity status */
    val networkConnection: Boolean? = true,

    /** Current client ID from user preferences */
    val clientId: Long?,

    /** Currently active dialog (Error) */
    val dialogState: DialogState? = null,

    /** Filters currently applied */
    val selectedFilters: List<StringResource?> = emptyList(),

    /** Controls whether account balances are visible */
    val isAmountVisible: Boolean = false,

    val uiState: ScreenUiState? = ScreenUiState.Loading,

    val networkStatus: Boolean = false,
) {
    /**
     * Represents UI dialog states.
     */
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

/**
 * Represents user or system actions for the Share Accounts screen.
 */
sealed interface ShareAccountsAction {
    data object OnFirstLaunched : ShareAccountsAction

    /** Dismiss any open dialog */
    data object OnDismissDialog : ShareAccountsAction

    /** Navigate back from the screen */
    data object OnNavigateBack : ShareAccountsAction

    /** Toggle visibility of share amount */
    data object ToggleAmountVisible : ShareAccountsAction

    /** Load share accounts with applied filters */
    data class LoadAccounts(val filters: List<StringResource?>) : ShareAccountsAction

    /** Retry loading with same filters */
    data object OnRetry : ShareAccountsAction

    /** Navigate to a selected account's detail page */
    data class OnAccountClicked(val accountId: Long, val accountType: String) : ShareAccountsAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : ShareAccountsAction

    /**
     * Internal-only actions triggered by repository/data flow.
     */
    sealed interface Internal : ShareAccountsAction {

        /** Called when share account data is received from repository */
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

    /** Trigger navigation to selected share account's detail screen */
    data class AccountClicked(val accountId: Long, val accountType: String) : ShareAccountsEvent

    /** Signals the UI that loading is complete */
    data object LoadingCompleted : ShareAccountsEvent

    /** Navigates back to the previous screen */
    data object NavigateBack : ShareAccountsEvent
}

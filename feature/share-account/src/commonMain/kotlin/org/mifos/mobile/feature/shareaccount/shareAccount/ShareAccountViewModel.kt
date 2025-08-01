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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
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
        dialogState = null,
        shareAccounts = emptyList(),
    ),
) {

    init {
        observeNetwork()
        handleAction(ShareAccountsAction.LoadAccounts(emptyList()))
    }

    override fun handleAction(action: ShareAccountsAction) {
        when (action) {
            is ShareAccountsAction.OnDismissDialog -> handleDismissDialog()
            is ShareAccountsAction.OnNavigateBack -> sendEvent(ShareAccountsEvent.NavigateBack)
            is ShareAccountsAction.ToggleAmountVisible -> handleAmountVisible()
            is ShareAccountsAction.LoadAccounts -> loadAccounts(action.filters)
            is ShareAccountsAction.OnRetry -> loadAccounts(action.filters)
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
            networkMonitor.isOnline.collect { isConnected ->
                mutableStateFlow.update {
                    it.copy(networkConnection = isConnected)
                }
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
        val cached = state.originalAccounts

        if (cached != null) {
            val filtered = filterAccounts(selectedFilters, cached)
            getTotalShareAmount(filtered)

            mutableStateFlow.update {
                it.copy(
                    shareAccounts = filtered,
                    selectedFilters = selectedFilters,
                    dialogState = null,
                )
            }
            sendEvent(ShareAccountsEvent.LoadingCompleted)
            return
        }

        viewModelScope.launch {
            mutableStateFlow.update { it.copy(dialogState = ShareAccountsState.DialogState.Loading) }

            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId ?: return@launch,
                accountType = Constants.SHARE_ACCOUNTS,
            ).catch {
                mutableStateFlow.update {
                    it.copy(dialogState = ShareAccountsState.DialogState.Error("Something went wrong"))
                }
            }.collect { clientAccounts ->
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
                mutableStateFlow.update {
                    it.copy(dialogState = ShareAccountsState.DialogState.Error("Something went wrong"))
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(dialogState = ShareAccountsState.DialogState.Loading)
                }
            }

            is DataState.Success -> {
                val shareAccounts = dataState.data.shareAccounts
                val filtered = filterAccounts(selectedFilters, shareAccounts)
                getTotalShareAmount(shareAccounts)

                mutableStateFlow.update {
                    it.copy(
                        items = filtered.size,
                        isEmpty = filtered.isEmpty(),
                        shareAccounts = filtered,
                        originalAccounts = shareAccounts,
                        currency = shareAccounts.firstOrNull()?.currency?.displaySymbol,
                        dialogState = null,
                        selectedFilters = selectedFilters,
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

        // Future logic to calculate approved share amount can be added here

        mutableStateFlow.update {
            it.copy(totalLoanAmount = amount, items = items)
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
    val isEmpty: Boolean = false,

    /** Number of filtered accounts */
    val items: Int? = 0,

    /** Total share amount computed from accounts */
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
     * Represents UI dialog states.
     */
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
    }
}

/**
 * Represents user or system actions for the Share Accounts screen.
 */
sealed interface ShareAccountsAction {

    /** Dismiss any open dialog */
    data object OnDismissDialog : ShareAccountsAction

    /** Navigate back from the screen */
    data object OnNavigateBack : ShareAccountsAction

    /** Toggle visibility of share amount */
    data object ToggleAmountVisible : ShareAccountsAction

    /** Load share accounts with applied filters */
    data class LoadAccounts(val filters: List<StringResource?>) : ShareAccountsAction

    /** Retry loading with same filters */
    data class OnRetry(val filters: List<StringResource?>) : ShareAccountsAction

    /** Navigate to a selected account's detail page */
    data class OnAccountClicked(val accountId: Long, val accountType: String) : ShareAccountsAction

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

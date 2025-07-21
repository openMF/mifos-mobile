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
internal class SavingsAccountViewmodel(
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
        handleAction(SavingsAccountAction.LoadAccounts("", emptyList()))
    }

    override fun handleAction(action: SavingsAccountAction) {
        when (action) {
            is SavingsAccountAction.OnDismissDialog -> handleDismissDialog()

            is SavingsAccountAction.OnNavigateBack -> sendEvent(SavingsAccountsEvent.NavigateBack)

            is SavingsAccountAction.ToggleAmountVisible -> handleAmountVisible()

            is SavingsAccountAction.Refresh -> {
                mutableStateFlow.update { it.copy(isRefreshing = true) }
                handleAction(SavingsAccountAction.LoadAccounts(state.searchQuery, state.selectedFilters))
            }

            is SavingsAccountAction.LoadAccounts -> {
                loadAccounts(action.searchQuery, action.filters)
            }

            is SavingsAccountAction.Internal.ReceiveSavingsAccounts -> {
                handleReceivedAccounts(action.dataState, action.searchQuery, action.filters)
            }
        }
    }

    /**
     * Toggle the amount visibility
     */
    private fun handleAmountVisible() {
        mutableStateFlow.update {
            it.copy(isAmountVisible = !state.isAmountVisible)
        }
    }

    /**
     * Dismiss the dialog
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
     * Fetches accounts from the repository and dispatches result to state handler.
     *
     * @param searchQuery Query to filter by account number or name.
     * @param selectedFilters List of selected filters to apply.
     */
    private fun loadAccounts(
        searchQuery: String,
        selectedFilters: List<StringResource?>,
    ) {
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
                        searchQuery = searchQuery,
                        filters = selectedFilters,
                        dataState = clientAccounts,
                    ),
                )
            }
        }
    }

    /**
     * Updates UI state based on data result from repository.
     *
     * @param dataState Result of fetching savings accounts (Success, Error, Loading).
     * @param searchQuery Current search term used to filter accounts.
     * @param selectedFilters Filters applied to the list.
     */
    private fun handleReceivedAccounts(
        dataState: DataState<ClientAccounts>,
        searchQuery: String,
        selectedFilters: List<StringResource?>,
    ) {
        when (dataState) {
            is DataState.Error -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = SavingsAccountState.DialogState.Error("Something went wrong"),
                        isRefreshing = false,
                    )
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(dialogState = SavingsAccountState.DialogState.Loading)
                }
            }

            is DataState.Success -> {
                val savings = dataState.data.savingsAccounts.orEmpty()
                val filtered = filterAccounts(searchQuery, selectedFilters, savings)
                getTotalSavingAmount(dataState.data.savingsAccounts)
                mutableStateFlow.update {
                    it.copy(
                        savingsAccount = filtered,
                        currency = savings.firstOrNull()?.currency?.displayLabel,
                        isRefreshing = false,
                        dialogState = null,
                    )
                }
            }
        }
    }

    /**
     * Applies search and filter conditions on the account list.
     *
     * @param searchQuery Text entered by the user to filter by name or number.
     * @param selectedFilters List of filters applied (status, etc).
     * @param accounts Complete list of savings accounts to be filtered.
     * @return Filtered list based on search and filters.
     */
    private fun filterAccounts(
        searchQuery: String,
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

        val searched = if (searchQuery.isNotBlank()) {
            val query = searchQuery.lowercase()
            filteredByStatus.filter {
                listOf(it.productName, it.accountNo).any { text ->
                    text?.lowercase()?.contains(query) == true
                }
            }
        } else {
            filteredByStatus
        }

        return searched.distinct()
    }

    /**
     * Calculates the total savings amount by summing the `accountBalance` of each
     * [SavingAccount] and counts the number of accounts that client holds.
     *
     * This function iterates through the provided list of savings accounts and adds up their balances.
     * It then updates the [SavingsAccountState.totalSavingAmount] property in the
     * [SavingsAccountState] with the result.
     *
     * @param accounts The list of [SavingAccount]s whose balances need to be aggregated.
     * If `null`, the total is assumed to be 0.0.
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

internal data class SavingsAccountState(
    val savingsAccount: List<SavingAccount>?,
    val items: Int? = 0,
    val totalSavingAmount: Double? = 0.0,
    val currency: String? = "",
    val networkConnection: Boolean? = true,
    val clientId: Long?,
    val isRefreshing: Boolean? = false,
    val dialogState: DialogState?,
    val searchQuery: String = "",
    val selectedFilters: List<StringResource?> = emptyList(),
    val isAmountVisible: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState

        data object Loading : DialogState
    }
}

sealed interface SavingsAccountAction {
    data object OnDismissDialog : SavingsAccountAction

    data object OnNavigateBack : SavingsAccountAction

    data object ToggleAmountVisible : SavingsAccountAction

    data class LoadAccounts(
        val searchQuery: String,
        val filters: List<StringResource?>,
    ) : SavingsAccountAction

    data object Refresh : SavingsAccountAction

    sealed interface Internal : SavingsAccountAction {
        data class ReceiveSavingsAccounts(
            val searchQuery: String,
            val filters: List<StringResource?>,
            val dataState: DataState<ClientAccounts>,
        ) : SavingsAccountAction
    }
}

sealed interface SavingsAccountsEvent {
    data object NavigateBack : SavingsAccountsEvent
}

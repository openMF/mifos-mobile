/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.loanaccount.utils.AccountState
import org.mifos.mobile.feature.loanaccount.utils.FilterUtil

/**
 * ViewModel responsible for managing loan accounts and their states.
 *
 * This ViewModel interacts with repositories to fetch, filter, and manage loan accounts.
 * It also monitors network connectivity and manages UI state updates.
 *
 * @property accountsRepositoryImpl Repository responsible for fetching loan accounts.
 * @property networkMonitor Monitors the network status.
 * @property userPreferencesRepository Stores user-related preferences, including client ID.
 */
class LoanAccountViewmodel(
    private val accountsRepositoryImpl: AccountsRepository,
    networkMonitor: NetworkMonitor,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    /** Client ID retrieved from user preferences. */
    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)

    /**
     * Tracks whether a refresh operation is in progress.
     *
     * Used by [PullToRefreshBox] to indicate whether the list is currently refreshing.
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    /** Tracks network availability using [NetworkMonitor]. */
    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /** Holds the current state of loan accounts UI. */
    @Suppress("PropertyName")
    private val _accountsUiState = MutableStateFlow<AccountState>(AccountState.Loading)
    val accountUiState: StateFlow<AccountState> = _accountsUiState.asStateFlow()

    init {
        loadSavingsAccounts(
            searchQuery = "",
            selectedCheckboxLabels = emptyList(),
        )
    }

    /**
     * Filters loan accounts based on the search query.
     *
     * @param accounts List of loan accounts.
     * @param searchQuery Search query string.
     * @return Filtered list of loan accounts that match the search query.
     */
    private fun filterAccountsBySearchQuery(
        accounts: List<LoanAccount?>?,
        searchQuery: String?,
    ): List<LoanAccount> {
        val searchTerm = searchQuery?.lowercase().orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.run {
                listOf(productName, accountNo).any {
                    it?.lowercase()?.contains(searchTerm) == true
                }
            } ?: false
        }.filterNotNull()
    }

    /**
     * Filters loan accounts based on selected filter labels.
     *
     * @param accounts List of loan accounts.
     * @param selectedCheckboxLabels List of selected filter labels.
     * @return List of loan accounts that match the selected filters.
     */
    private fun filterAccountsByStatus(
        accounts: List<LoanAccount>,
        selectedCheckboxLabels: List<StringResource?>,
    ): List<LoanAccount> {
        return selectedCheckboxLabels
            .mapNotNull { label -> FilterUtil.fromLabel(label) }
            .flatMap { filterUtil ->
                accounts.filter(filterUtil.matchCondition)
            }
            .distinctBy { it.accountNo ?: it.loanProductId.toString() }
    }

    /**
     * Retrieves loan accounts based on search query and selected filters.
     *
     * This function applies both the search query and status filters, if provided.
     *
     * @param searchQuery The search term entered by the user.
     * @param selectedCheckboxLabels List of selected filter labels.
     * @param accounts The list of all loan accounts.
     * @return A filtered list of loan accounts based on the applied filters.
     */
    private fun getFilteredAccounts(
        searchQuery: String,
        selectedCheckboxLabels: List<StringResource?>,
        accounts: List<LoanAccount>,
    ): List<LoanAccount> {
        val filteredByStatus = if (selectedCheckboxLabels.isNotEmpty()) {
            filterAccountsByStatus(accounts, selectedCheckboxLabels)
        } else {
            accounts
        }

        return if (searchQuery.isNotBlank()) {
            filterAccountsBySearchQuery(filteredByStatus, searchQuery)
        } else {
            filteredByStatus
        }
    }

    /**
     * Triggers a refresh operation when the user pulls down to refresh.
     *
     * This function is called by [PullToRefreshBox] to reload loan accounts.
     *
     * @param searchQuery The current search query input by the user.
     * @param selectedCheckboxLabels List of currently selected filter labels.
     */
    fun refresh(
        searchQuery: String,
        selectedCheckboxLabels: List<StringResource?>,
    ) {
        _isRefreshing.value = true
        loadSavingsAccounts(
            searchQuery = searchQuery,
            selectedCheckboxLabels = selectedCheckboxLabels,
        )
    }

    /**
     * Loads loan accounts for the client and updates the UI state.
     *
     * This function fetches loan accounts from the repository, applies filtering,
     * and updates the UI accordingly. If an error occurs during fetching, it updates
     * the UI state to [AccountState.Error].
     *
     * Once accounts are successfully loaded, [_isRefreshing] is reset to false.
     *
     * @param searchQuery The search query to filter accounts.
     * @param selectedCheckboxLabels List of selected filter labels for filtering accounts.
     */
    fun loadSavingsAccounts(
        searchQuery: String,
        selectedCheckboxLabels: List<StringResource?>,
    ) {
        viewModelScope.launch {
            _accountsUiState.value = AccountState.Loading
            accountsRepositoryImpl.loadAccounts(
                clientId = clientId,
                accountType = AccountType.LOAN.name,
            ).catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                val loanAccounts = clientAccounts.data?.loanAccounts
                _accountsUiState.value = if (loanAccounts.isNullOrEmpty()) {
                    AccountState.Empty
                } else {
                    val filteredAccounts = getFilteredAccounts(
                        searchQuery = searchQuery,
                        selectedCheckboxLabels = selectedCheckboxLabels,
                        accounts = loanAccounts,
                    )
                    AccountState.Success(filteredAccounts)
                }
                _isRefreshing.value = false
            }
        }
    }
}

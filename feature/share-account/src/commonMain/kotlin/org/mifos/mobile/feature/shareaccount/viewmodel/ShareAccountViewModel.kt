/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.viewmodel

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
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.shareaccount.utils.AccountState
import org.mifos.mobile.feature.shareaccount.utils.FilterUtil

/**
 * ViewModel responsible for managing share accounts and their states.
 *
 * This ViewModel interacts with repositories to fetch, filter, and manage share accounts.
 * It also monitors network connectivity and manages UI state updates.
 *
 * @property accountsRepositoryImpl Repository responsible for fetching share accounts.
 * @property networkMonitor Monitors the network status.
 * @property userPreferencesRepository Stores user-related preferences, including client ID.
 */
class ShareAccountViewModel(
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
    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    /** Tracks network availability using [NetworkMonitor]. */
    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /** Holds the current state of share accounts UI. */
    @Suppress("PropertyName")
    private val _accountsUiState = MutableStateFlow<AccountState>(AccountState.Loading)
    val accountUiState: StateFlow<AccountState> = _accountsUiState.asStateFlow()

    /**
     * Initializes the ViewModel by loading share accounts with default parameters.
     */
    init {
        loadSavingsAccounts(
            searchQuery = "",
            selectedCheckboxLabels = emptyList(),
        )
    }

    /**
     * Filters share accounts based on the search query.
     *
     * @param accounts List of share accounts.
     * @param searchQuery Search query string.
     * @return Filtered list of share accounts that match the search query.
     */
    private fun filterAccountsBySearchQuery(
        accounts: List<ShareAccount?>?,
        searchQuery: String?,
    ): List<ShareAccount> {
        val searchTerm = searchQuery?.lowercase().orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.run {
                listOf(productName, accountNo)
                    .any { it?.lowercase()?.contains(searchTerm) == true }
            } ?: false
        }.filterNotNull()
    }

    /**
     * Filters share accounts based on selected filter labels using [FilterUtil].
     *
     * @param accounts List of share accounts.
     * @param selectedLabels List of selected filter labels.
     * @return List of share accounts that match the selected filters.
     */
    private fun filterAccountsByStatus(
        accounts: List<ShareAccount>,
        selectedLabels: List<StringResource?>,
    ): List<ShareAccount> {
        return selectedLabels
            .mapNotNull { label -> FilterUtil.fromLabel(label) }
            .flatMap { filterUtil ->
                accounts.filter(filterUtil.matchCondition)
            }
            .distinct()
    }

    /**
     * Retrieves share accounts based on search query and selected filters.
     *
     * This function applies both the search query and status filters, if provided.
     *
     * @param searchQuery The search term entered by the user.
     * @param selectedCheckboxLabels List of selected filter labels.
     * @param accounts The list of all share accounts.
     * @return A filtered list of share accounts based on the applied filters.
     */
    private fun getFilteredAccounts(
        searchQuery: String,
        selectedCheckboxLabels: List<StringResource?>,
        accounts: List<ShareAccount>,
    ): List<ShareAccount> {
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
     * This function is called by [PullToRefreshBox] to reload share accounts.
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
     * Loads share accounts for the client and updates the UI state.
     *
     * This function fetches share accounts from the repository, applies filtering,
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
                accountType = AccountType.SHARE.name,
            ).catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                val shareAccounts = clientAccounts.data?.shareAccounts
                _accountsUiState.value = if (shareAccounts.isNullOrEmpty()) {
                    AccountState.Empty
                } else {
                    val filteredAccounts = getFilteredAccounts(
                        searchQuery = searchQuery,
                        selectedCheckboxLabels = selectedCheckboxLabels,
                        accounts = shareAccounts,
                    )
                    AccountState.Success(filteredAccounts)
                }
                _isRefreshing.value = false
            }
        }
    }
}

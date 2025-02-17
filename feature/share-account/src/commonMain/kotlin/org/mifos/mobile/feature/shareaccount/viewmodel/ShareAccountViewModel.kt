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
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.shareaccount.model.CheckboxStatus
import org.mifos.mobile.feature.shareaccount.utils.AccountState
import org.mifos.mobile.feature.shareaccount.utils.FilterUtil
import org.mifos.mobile.feature.shareaccount.utils.StatusUtil
import org.mifos.mobile.feature.shareaccount.utils.getAccountsFilterLabels

class ShareAccountViewModel(
    private val accountsRepositoryImpl: AccountsRepository,
    networkMonitor: NetworkMonitor,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)

    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    @Suppress("PropertyName")
    private val _accountsUiState = MutableStateFlow<AccountState>(AccountState.Loading)
    val accountUiState: StateFlow<AccountState> = _accountsUiState.asStateFlow()

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

    private fun filterAccountsByStatus(
        accountsList: List<ShareAccount?>,
        filterList: List<CheckboxStatus>,
    ): List<ShareAccount> {
        val savingsAccountFilterCriteria = getAccountsFilterLabels()

        return filterList
            .filter { it.isChecked }
            .flatMap { selectedCheckboxStatus ->
                getAccountsMatchingStatus(
                    accounts = accountsList,
                    checkboxStatus = selectedCheckboxStatus,
                    statusCriteria = savingsAccountFilterCriteria,
                )
            }
            .distinct()
    }

    private fun getAccountsMatchingStatus(
        accounts: List<ShareAccount?>?,
        checkboxStatus: CheckboxStatus?,
        statusCriteria: FilterUtil,
    ): List<ShareAccount> {
        return accounts.orEmpty().filter { account ->
            when (checkboxStatus?.status) {
                statusCriteria.activeString -> account?.status?.active == true
                statusCriteria.approvedString -> account?.status?.approved == true
                statusCriteria.approvalPendingString -> account?.status?.submittedAndPendingApproval == true
                statusCriteria.closedString -> account?.status?.closed == true
                else -> false
            }
        }.filterNotNull()
    }

    fun getUpdatedFilteredAccountList(
        searchQuery: String,
        isFiltered: Boolean,
        isSearchActive: Boolean,
        accountList: List<ShareAccount>,
    ): List<ShareAccount> {
        return when {
            isFiltered && isSearchActive -> {
                val updatedFilterList = filterAccountsByStatus(
                    accountsList = accountList,
                    filterList = StatusUtil.getShareAccountsStatusList(),
                )

                filterAccountsBySearchQuery(
                    accounts = updatedFilterList,
                    searchQuery = searchQuery,
                )
            }

            isSearchActive -> {
                filterAccountsBySearchQuery(
                    accounts = accountList,
                    searchQuery = searchQuery,
                )
            }

            isFiltered -> {
                filterAccountsByStatus(
                    accountsList = accountList,
                    filterList = StatusUtil.getShareAccountsStatusList(),
                )
            }

            else -> {
                accountList
            }
        }
    }

    fun loadSavingsAccounts() {
        viewModelScope.launch {
            _accountsUiState.value = AccountState.Loading
            accountsRepositoryImpl.loadAccounts(
                clientId = clientId,
                accountType = AccountType.SHARE.name,
            ).catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                _accountsUiState.value =
                    AccountState.Success(clientAccounts.data?.shareAccounts)
            }
        }
    }
}

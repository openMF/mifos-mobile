/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.account.account.utils.AccountsFilterUtil
import org.mifos.mobile.feature.account.utils.AccountState
import org.mifos.mobile.feature.account.utils.StatusUtils
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountsRepositoryImp: AccountsRepository,
    private val homeRepositoryImp: HomeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _accountsUiState = MutableStateFlow<AccountState>(AccountState.Loading)
    val accountsUiState: StateFlow<AccountState> = _accountsUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> get() = _isSearching.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isFiltered = MutableStateFlow(false)
    val isFiltered: StateFlow<Boolean> get() = _isFiltered.asStateFlow()

    private val _filterList = MutableStateFlow(emptyList<CheckboxStatus>())
    val filterList: StateFlow<List<CheckboxStatus>> = _filterList.asStateFlow()

    private val accountTypeString = savedStateHandle.getStateFlow(
        key = Constants.ACCOUNT_TYPE,
        initialValue = AccountType.SAVINGS.name,
    )

    val accountType: StateFlow<AccountType?> = accountTypeString
        .flatMapLatest { accountTypeString ->
            flowOf(AccountType.valueOf(accountTypeString))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    fun refresh(accountType: String?) {
        when (accountType) {
            Constants.SAVINGS_ACCOUNTS -> {
                _isRefreshing.value = true
                loadAccounts(Constants.SAVINGS_ACCOUNTS)
            }

            Constants.LOAN_ACCOUNTS -> {
                _isRefreshing.value = true
                loadAccounts(Constants.LOAN_ACCOUNTS)
            }

            Constants.SHARE_ACCOUNTS -> {
                _isRefreshing.value = true
                loadAccounts(Constants.SHARE_ACCOUNTS)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _isSearching.update { true }
        _searchQuery.update { query }
    }

    fun stoppedSearching() {
        _searchQuery.update { "" }
        _isSearching.update { false }
    }

    fun setFilterList(
        checkBoxList: List<CheckboxStatus>,
        currentPage: Int,
        context: Context,
    ) {
        if (checkBoxList.isEmpty()) {
            when (currentPage) {
                0 -> {
                    _isFiltered.update { false }
                    _filterList.update { StatusUtils.getSavingsAccountStatusList(context) }
                }

                1 -> {
                    _isFiltered.update { false }
                    _filterList.update { StatusUtils.getLoanAccountStatusList(context) }
                }

                2 -> {
                    _isFiltered.update { false }
                    _filterList.update { StatusUtils.getShareAccountStatusList(context) }
                }
            }
        } else {
            var isChanged = false
            for (checkBox in checkBoxList) {
                if (checkBox.isChecked) {
                    isChanged = true
                }
            }
            if (isChanged) {
                _isFiltered.update { true }
                _filterList.update { checkBoxList }
            } else {
                _isFiltered.update { false }
                _filterList.update { checkBoxList }
            }
        }
    }

    fun getFilterLoanAccountList(
        accountsList: List<LoanAccount?>,
        filterList: List<CheckboxStatus>,
        context: Context,
    ): List<LoanAccount> {
        val accountsFilterUtil = AccountsFilterUtil.getFilterStrings(context)

        return filterList
            .filter { it.isChecked }
            .flatMap { filter ->
                getFilteredLoanAccount(accountsList, filter, accountsFilterUtil)
            }
            .distinctBy { getUniqueIdentifierForLoanAccount(it) }
    }

    private fun getUniqueIdentifierForLoanAccount(account: LoanAccount): String {
        return account.accountNo ?: account.loanProductId.toString()
    }

    fun getFilterSavingsAccountList(
        accountsList: List<SavingAccount?>,
        filterList: List<CheckboxStatus>,
        context: Context,
    ): List<SavingAccount> {
        val accountsFilterUtil = AccountsFilterUtil.getFilterStrings(context)

        return filterList
            .filter { it.isChecked }
            .flatMap { filter ->
                getFilteredSavingsAccount(accountsList, filter, accountsFilterUtil)
            }
            .distinct()
    }

    fun getFilterShareAccountList(
        accountsList: List<ShareAccount?>,
        filterList: List<CheckboxStatus>,
        context: Context,
    ): List<ShareAccount> {
        val accountsFilterUtil = AccountsFilterUtil.getFilterStrings(context)

        return filterList
            .filter { it.isChecked }
            .flatMap { filter ->
                getFilteredShareAccount(accountsList, filter, accountsFilterUtil)
            }
            .distinct()
    }

    /**
     * Loads savings, loan and share accounts associated with the Client from the server
     * and notifies the view to display it. And in case of any error during fetching the required
     * details it notifies the view.
     */
    fun loadClientAccounts() {
        viewModelScope.launch {
            _accountsUiState.value = AccountState.Loading
            homeRepositoryImp.clientAccounts().catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                _accountsUiState.value =
                    AccountState.ShowSavingsAccounts(clientAccounts.savingsAccounts)
                _accountsUiState.value =
                    AccountState.ShowLoanAccounts(clientAccounts.loanAccounts)
                _accountsUiState.value =
                    AccountState.ShowShareAccounts(clientAccounts.shareAccounts)
            }
        }
    }

    /**
     * Loads savings, loan or share account depending upon `accountType` provided from the
     * server and notifies the view to display it.And in case of any error during fetching the
     * required details it notifies the view.
     * @param accountType Type of account for which we need to fetch details
     */
    fun loadAccounts(accountType: String?) {
        viewModelScope.launch {
            _accountsUiState.value = AccountState.Loading
            accountsRepositoryImp.loadAccounts(accountType).catch {
                _accountsUiState.value = AccountState.Error
            }.collect { clientAccounts ->
                when (accountType) {
                    Constants.SAVINGS_ACCOUNTS ->
                        _accountsUiState.value =
                            AccountState.ShowSavingsAccounts(clientAccounts.savingsAccounts)

                    Constants.LOAN_ACCOUNTS ->
                        _accountsUiState.value =
                            AccountState.ShowLoanAccounts(clientAccounts.loanAccounts)

                    Constants.SHARE_ACCOUNTS ->
                        _accountsUiState.value =
                            AccountState.ShowShareAccounts(clientAccounts.shareAccounts)
                }
                _isRefreshing.emit(false)
            }
        }
    }

    /**
     * Filters [List] of [SavingAccount]
     * @param accounts [List] of [SavingAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [SavingAccount] according to the `input`
     * provided.
     */
    fun searchInSavingsList(
        accounts: List<SavingAccount?>?,
        input: String?,
    ): List<SavingAccount> {
        val searchTerm = input?.lowercase(Locale.ROOT).orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.let {
                it.productName?.lowercase(Locale.ROOT)?.contains(searchTerm) == true ||
                    it.accountNo?.lowercase(Locale.ROOT)?.contains(searchTerm) == true
            } ?: false
        }.filterNotNull()
    }

    /**
     * Filters [List] of [LoanAccount]
     * @param accounts [List] of [LoanAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [LoanAccount] according to the `input`
     * provided.
     */
    fun searchInLoanList(
        accounts: List<LoanAccount?>?,
        input: String?,
    ): List<LoanAccount> {
        val searchTerm = input?.lowercase(Locale.ROOT).orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.let {
                it.productName?.lowercase(Locale.ROOT)?.contains(searchTerm) == true ||
                    it.accountNo?.lowercase(Locale.ROOT)?.contains(searchTerm) == true
            } ?: false
        }.filterNotNull()
    }

    /**
     * Filters [List] of [ShareAccount]
     * @param accounts [List] of [ShareAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [ShareAccount] according to the `input`
     * provided.
     */
    fun searchInSharesList(
        accounts: Collection<ShareAccount?>?,
        input: String?,
    ): List<ShareAccount> {
        val searchTerm = input?.lowercase(Locale.ROOT).orEmpty()

        return accounts.orEmpty().filter { account ->
            account?.let {
                it.productName?.lowercase(Locale.ROOT)?.contains(searchTerm) == true ||
                    it.accountNo?.lowercase(Locale.ROOT)?.contains(searchTerm) == true
            } ?: false
        }.filterNotNull()
    }

    /**
     * Filters [List] of [CheckboxStatus]
     * @param statusModelList [List] of [CheckboxStatus]
     * @return Returns [List] of [CheckboxStatus] which have
     * `checkboxStatus.isChecked()` as true.
     */
    fun getCheckedStatus(statusModelList: List<CheckboxStatus?>?): List<CheckboxStatus?>? {
        return statusModelList?.filter { it?.isChecked == true }
    }

    /**
     * Filters [List] of [SavingAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [SavingAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [SavingAccount] according to the
     * `status` provided.
     */
    private fun getFilteredSavingsAccount(
        accounts: List<SavingAccount?>?,
        status: CheckboxStatus?,
        accountsFilterUtil: AccountsFilterUtil,
    ): List<SavingAccount> {
        return accounts.orEmpty().filter { account ->
            when {
                status?.status == accountsFilterUtil.activeString && account?.status?.active == true -> true
                status?.status == accountsFilterUtil.approvedString && account?.status?.approved == true -> true
                status?.status == accountsFilterUtil.approvalPendingString &&
                    account?.status?.submittedAndPendingApproval == true -> true

                status?.status == accountsFilterUtil.maturedString && account?.status?.matured == true -> true
                status?.status == accountsFilterUtil.closedString && account?.status?.closed == true -> true
                else -> false
            }
        }.filterNotNull()
    }

    /**
     * Filters [List] of [LoanAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [LoanAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [LoanAccount] according to the
     * `status` provided.
     */
    private fun getFilteredLoanAccount(
        accounts: List<LoanAccount?>?,
        status: CheckboxStatus?,
        accountsFilterUtil: AccountsFilterUtil,
    ): List<LoanAccount> {
        return accounts.orEmpty().filter { account ->
            when (status?.status) {
                accountsFilterUtil.inArrearsString -> account?.inArrears == true
                accountsFilterUtil.activeString -> account?.status?.active == true
                accountsFilterUtil.waitingForDisburseString -> account?.status?.waitingForDisbursal == true
                accountsFilterUtil.approvalPendingString -> account?.status?.pendingApproval == true
                accountsFilterUtil.overpaidString -> account?.status?.overpaid == true
                accountsFilterUtil.closedString -> account?.status?.closed == true
                accountsFilterUtil.withdrawnString -> account?.status?.isLoanTypeWithdrawn() == true
                else -> false
            }
        }.filterNotNull()
    }

    /**
     * Filters [List] of [ShareAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [ShareAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [ShareAccount] according to the
     * `status` provided.
     */
    private fun getFilteredShareAccount(
        accounts: List<ShareAccount?>?,
        status: CheckboxStatus?,
        accountsFilterUtil: AccountsFilterUtil,
    ): List<ShareAccount> {
        return accounts.orEmpty().filter { account ->
            when (status?.status) {
                accountsFilterUtil.activeString -> account?.status?.active == true
                accountsFilterUtil.approvedString -> account?.status?.approved == true
                accountsFilterUtil.approvalPendingString -> account?.status?.submittedAndPendingApproval == true
                accountsFilterUtil.closedString -> account?.status?.closed == true
                else -> false
            }
        }.filterNotNull()
    }
}

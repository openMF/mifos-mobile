/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.utils.StatusUtils

/**
 * ViewModel responsible for managing search and filtering functionality for accounts.
 *
 * This ViewModel maintains search queries, filtering states, and dynamically updates
 * checkbox filter options based on the selected account type.
 *
 * @param savedStateHandle Stores and retrieves UI-related state information across process death and recreation.
 */
class AccountsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    /** Holds the current search query input. */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /** Holds the list of checkbox options for filtering accounts. */
    private val _checkboxOptions = MutableStateFlow(emptyList<CheckboxStatus>())
    internal val checkboxOptions: StateFlow<List<CheckboxStatus>> = _checkboxOptions.asStateFlow()

    /** StateFlow to track the selected account type from [savedStateHandle]. */
    private val accountTypeString = savedStateHandle.getStateFlow(
        key = Constants.ACCOUNT_TYPE,
        initialValue = AccountType.SAVINGS.name,
    )

    /**
     * A StateFlow that holds the currently selected [AccountType].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val accountType: StateFlow<AccountType?> = accountTypeString
        .flatMapLatest { accountTypeString ->
            flowOf(AccountType.valueOf(accountTypeString))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    /**
     * Updates the search query and marks the search state as active.
     *
     * @param query The new search query entered by the user.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    /**
     * Clears the search query and marks the search state as inactive.
     */
    fun stoppedSearching() {
        _searchQuery.update { "" }
    }

    /**
     * Updates the list of selected checkbox filters and determines if any filters are applied.
     *
     * If no checkboxes are selected, the default list for the given page is set.
     *
     * @param checkBoxList The list of currently selected checkboxes.
     * @param currentPage The current page index representing the account type.
     */
    internal fun setCheckboxFilterList(
        checkBoxList: List<CheckboxStatus>,
        currentPage: Int,
    ) {
        if (checkBoxList.isEmpty()) {
            _checkboxOptions.value = getAccountCheckboxes(currentPage)
        } else {
            _checkboxOptions.value = checkBoxList
        }
    }

    /**
     * Retrieves the default checkbox filter list based on the selected account type.
     *
     * @param currentPage The index representing the account type.
     * @return The list of default [CheckboxStatus] options for the selected account type.
     */
    private fun getAccountCheckboxes(currentPage: Int): List<CheckboxStatus> {
        return when (currentPage) {
            0 -> StatusUtils.getSavingsAccountCheckboxes()
            1 -> StatusUtils.getLoanAccountCheckboxes()
            2 -> StatusUtils.getShareAccountCheckboxes()
            else -> emptyList()
        }
    }
}

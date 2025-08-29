/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.accounts

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.model.FilterType
import org.mifos.mobile.feature.accounts.utils.StatusUtils

/**
 * ViewModel responsible for managing the account screen state,
 * handling filter operations, navigation, and refreshing logic.
 *
 * It controls filtering of different account types (Savings, Loan, Share),
 * tracks selected filters, and handles user interactions.
 *
 * @param savedStateHandle Navigation arguments for determining account type.
 */
internal class AccountsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<AccountsState, AccountsEvent, AccountsAction>(
    initialState = AccountsState(dialogState = null),
) {

    init {
        observeAccountTypeAndInitCheckboxes()
    }

    override fun handleAction(action: AccountsAction) {
        when (action) {
            is AccountsAction.SetCheckboxFilterList -> {
                val updatedList = action.checkBoxList.ifEmpty {
                    getAccountCheckboxes(action.accountType)
                }

                mutableStateFlow.update {
                    it.copy(checkboxOptions = updatedList)
                }
            }

            is AccountsAction.ResetFilters -> handleResetFilters()

            is AccountsAction.ToggleFilter -> handleToggleFilterDialog()

            is AccountsAction.Refresh -> {
                mutableStateFlow.update {
                    it.copy(
                        isRefreshing = true,
                        refreshSignal = Clock.System.now().epochSeconds,
                    )
                }
                handleConfirmFilterDialog()
            }

            is AccountsAction.RefreshCompleted -> {
                mutableStateFlow.update { it.copy(isRefreshing = false) }
            }

            AccountsAction.GetFilterResults -> handleConfirmFilterDialog()

            is AccountsAction.DismissDialog -> handleDismissDialog()

            is AccountsAction.OnAccountClicked ->
                sendEvent(AccountsEvent.AccountClicked(action.accountId, action.accountType))

            is AccountsAction.OnNavigateBack -> sendEvent(AccountsEvent.NavigateBack)

            is AccountsAction.ToggleCheckbox -> toggleCheckbox(action.label, action.type)
        }
    }

    /**
     * Shows the filter dialog by updating dialog state.
     */
    private fun handleToggleFilterDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = AccountsState.DialogState.Filters,
            )
        }
    }

    /**
     * Resets all filter checkboxes and filter counters.
     */
    private fun handleResetFilters() {
        mutableStateFlow.update {
            it.copy(
                checkboxOptions = it.checkboxOptions.map { cb -> cb.copy(isChecked = false) },
                selectedFilters = emptyList(),
                accountStatusFiltersCount = 0,
                accountTypeFiltersCount = 0,
            )
        }
    }

    /**
     * Applies the selected checkboxes as filters, sets refresh signal,
     * and dismisses the filter dialog.
     */
    private fun handleConfirmFilterDialog() {
        val selectedFilters = state.checkboxOptions.filter { it.isChecked }

        mutableStateFlow.update {
            it.copy(
                selectedFilters = selectedFilters,
                refreshSignal = Clock.System.now().epochSeconds,
            )
        }
        handleDismissDialog()
    }

    /**
     * Toggles the checkbox selection state for a given label and type.
     * Also updates the selected filters count.
     */
    private fun toggleCheckbox(label: StringResource, type: FilterType) {
        val updatedCheckboxes = mutableStateFlow.value.checkboxOptions.map {
            if (it.statusLabel == label && it.type == type) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }

        val typeCount = updatedCheckboxes.count { it.isChecked && it.type == FilterType.ACCOUNT_TYPE }
        val statusCount = updatedCheckboxes.count { it.isChecked && it.type == FilterType.ACCOUNT_STATUS }

        mutableStateFlow.update {
            it.copy(
                checkboxOptions = updatedCheckboxes,
                accountTypeFiltersCount = typeCount,
                accountStatusFiltersCount = statusCount,
            )
        }
    }

    /**
     * Hides any currently active dialog.
     */
    private fun handleDismissDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = null,
            )
        }
    }

    /**
     * Reads route from navigation and initializes checkbox options
     * based on the account type.
     */
    private fun observeAccountTypeAndInitCheckboxes() {
        val route = savedStateHandle.toRoute<AccountNavRoute>()
// TODO use enum class AccountType instead of Constants
        val type = when (route.accountType) {
            Constants.SAVINGS_ACCOUNT -> AccountType.SAVINGS
            Constants.LOAN_ACCOUNT -> AccountType.LOAN
            Constants.SHARE_ACCOUNTS -> AccountType.SHARE
            else -> AccountType.SAVINGS
        }
        val checkboxes = getAccountCheckboxes(type.name)

        mutableStateFlow.update {
            it.copy(
                accountType = type,
                checkboxOptions = checkboxes,
                selectedFilters = emptyList(),
                isRefreshing = false,
            )
        }
    }

    /**
     * Returns the list of checkbox options based on the selected account type.
     */
    private fun getAccountCheckboxes(accountType: String): List<CheckboxStatus> {
        return when (accountType) {
            AccountType.SAVINGS.name -> StatusUtils.getSavingsAccountCheckboxes()
            AccountType.LOAN.name -> StatusUtils.getLoanAccountCheckboxes()
            AccountType.SHARE.name -> StatusUtils.getShareAccountCheckboxes()
            else -> emptyList()
        }
    }
}

/**
 * UI state for the Accounts screen, containing filter options, dialog visibility,
 * current account type, and refresh signals.
 */
internal data class AccountsState
constructor(
    val isRefreshing: Boolean = false,

    /** Current filter checkboxes shown in the dialog */
    val checkboxOptions: List<CheckboxStatus> = emptyList(),

    /** Confirmed filters applied to the data */
    val selectedFilters: List<CheckboxStatus> = emptyList(),

    /** Selected account type (Savings, Loan, Share) */
    val accountType: AccountType = AccountType.SAVINGS,

    /** Whether filter dialog is visible */
    val toggleFilterDialog: Boolean = false,

    /** Count of selected account-type filters */
    val accountTypeFiltersCount: Int? = 0,

    /** Count of selected status filters */
    val accountStatusFiltersCount: Int? = 0,

    /** Used to trigger data refresh downstream */
    val refreshSignal: Long = Clock.System.now().epochSeconds,

    /** Current dialog being shown (loading, filters, error) */
    val dialogState: DialogState?,
) {

    /**
     * Defines various dialog states in the Accounts screen.
     */
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
        data object Filters : DialogState
    }

    /** True if any checkbox is selected */
    val isAnyFilterSelected = checkboxOptions.any { it.isChecked }
}

/**
 * Defines all user or internal actions that can be triggered in the Accounts screen.
 */
internal sealed interface AccountsAction {

    /** Opens or closes the filter dialog */
    data object ToggleFilter : AccountsAction

    /** Clears all filters and selections */
    data object ResetFilters : AccountsAction

    /** Dismisses any open dialog */
    data object DismissDialog : AccountsAction

    /** Applies filter and triggers refresh */
    data object GetFilterResults : AccountsAction

    /** Handles back navigation */
    data object OnNavigateBack : AccountsAction

    /** Initiates refresh of data */
    data object Refresh : AccountsAction

    /** Signals that refresh is done */
    data object RefreshCompleted : AccountsAction

    /** When an account card is clicked */
    data class OnAccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : AccountsAction

    /** Sets the checkbox list based on account type */
    data class SetCheckboxFilterList(
        val checkBoxList: List<CheckboxStatus>,
        val accountType: String,
    ) : AccountsAction

    /** Toggles a specific checkbox */
    data class ToggleCheckbox(
        val label: StringResource,
        val type: FilterType,
    ) : AccountsAction
}

/**
 * Defines one-time events sent from AccountsViewModel to UI.
 */
sealed interface AccountsEvent {

    /** Navigate back to previous screen */
    data object NavigateBack : AccountsEvent

    /** Navigate to account detail screen */
    data class AccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : AccountsEvent
}

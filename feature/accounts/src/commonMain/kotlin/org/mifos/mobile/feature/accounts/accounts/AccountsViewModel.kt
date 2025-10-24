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
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.model.FilterType
import org.mifos.mobile.feature.accounts.utils.StatusUtils
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
    initialState = AccountsState(),
) {

    init {
        observeAccountTypeAndInitCheckboxes()
    }

    @OptIn(ExperimentalTime::class)
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
    @OptIn(ExperimentalTime::class)
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
                uiState = ScreenUiState.Success,
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
 *
 * @property isRefreshing Indicates if the screen is currently refreshing data.
 * @property checkboxOptions List of checkboxes for filter options.
 * @property selectedFilters List of currently selected filters.
 * @property accountType Current account type filter.
 * @property toggleFilterDialog Whether the filter dialog is open.
 * @property accountTypeFiltersCount Count of selected account type filters.
 * @property accountStatusFiltersCount Count of selected account status filters.
 * @property refreshSignal Signal to trigger data refresh.
 * @property dialogState Current dialog state (Error or Filters).
 * @property uiState Overall UI state of the screen (Loading, Success, Error).
 *
 */
internal data class AccountsState
@OptIn(ExperimentalTime::class)
constructor(
    val isRefreshing: Boolean = false,
    val checkboxOptions: List<CheckboxStatus> = emptyList(),
    val selectedFilters: List<CheckboxStatus> = emptyList(),
    val accountType: AccountType = AccountType.SAVINGS,
    val toggleFilterDialog: Boolean = false,
    val accountTypeFiltersCount: Int? = 0,
    val accountStatusFiltersCount: Int? = 0,
    val refreshSignal: Long = Clock.System.now().epochSeconds,
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState = ScreenUiState.Loading,
) {

    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Filters : DialogState
    }

    val isAnyFilterSelected = checkboxOptions.any { it.isChecked }
}

/**
 * Defines all user or internal actions that can be triggered in the Accounts screen.
 *
 * @property ToggleFilter Opens or closes the filter dialog
 * @property ResetFilters Clears all filters and selections
 * @property DismissDialog Dismisses any open dialog
 * @property GetFilterResults Applies filter and triggers refresh
 * @property OnNavigateBack Handles back navigation
 * @property Refresh Initiates refresh of data
 * @property RefreshCompleted Signals that refresh is done
 * @property OnAccountClicked When an account card is clicked
 * @property SetCheckboxFilterList Sets the checkbox list based on account type
 * @property ToggleCheckbox Toggles a specific checkbox in the dialog
 */
internal sealed interface AccountsAction {

    data object ToggleFilter : AccountsAction

    data object ResetFilters : AccountsAction

    data object DismissDialog : AccountsAction

    data object GetFilterResults : AccountsAction

    data object OnNavigateBack : AccountsAction

    data object Refresh : AccountsAction

    data object RefreshCompleted : AccountsAction

    data class OnAccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : AccountsAction

    data class SetCheckboxFilterList(
        val checkBoxList: List<CheckboxStatus>,
        val accountType: String,
    ) : AccountsAction

    data class ToggleCheckbox(
        val label: StringResource,
        val type: FilterType,
    ) : AccountsAction
}

/**
 * Defines one-time events sent from AccountsViewModel to UI.
 * @property NavigateBack Navigate back to previous screen
 * @property AccountClicked Navigate to account detail screen
 */
sealed interface AccountsEvent {

    data object NavigateBack : AccountsEvent

    data class AccountClicked(
        val accountId: Long,
        val accountType: String,
    ) : AccountsEvent
}

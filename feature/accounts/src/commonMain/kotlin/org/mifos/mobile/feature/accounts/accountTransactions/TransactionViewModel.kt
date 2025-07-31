/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.accountTransactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_credit
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_debit
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_1_year
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_2_years
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_3_months
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_6_months
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_month
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.accounts.model.TransactionCheckboxStatus
import org.mifos.mobile.feature.accounts.model.TransactionFilterType
import org.mifos.mobile.feature.accounts.utils.StatusUtils
import kotlin.collections.map

internal class AccountsTransactionViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<AccountTransactionState, AccountTransactionEvent, AccountTransactionAction>(
    initialState = AccountTransactionState(dialogState = null),
) {
    init {
        loadTransactions()
        observeAccountTypeAndInitCheckboxes()
    }

    override fun handleAction(action: AccountTransactionAction) {
        when (action) {
            AccountTransactionAction.DismissDialog -> handleDismissDialog()
            AccountTransactionAction.OnNavigateBackClick -> {
                sendEvent(AccountTransactionEvent.OnNavigateBack)
            }

            AccountTransactionAction.GetFilterResults -> handleConfirmFilterDialog()
            AccountTransactionAction.Refresh -> {
                loadTransactions()
                mutableStateFlow.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
                handleConfirmFilterDialog()
            }
            AccountTransactionAction.ResetFilters -> handleResetFilters()
            is AccountTransactionAction.ToggleCheckbox -> toggleCheckbox(action.label, action.type)
            AccountTransactionAction.ToggleFilter -> handleToggleFilterDialog()
            is AccountTransactionAction.ToggleRadioButton -> {
                mutableStateFlow.update {
                    it.copy(
                        selectedRadioButton = action.label,
                    )
                }
            }
        }
    }

    private fun handleDismissDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = null,
            )
        }
    }

    private fun handleToggleFilterDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = AccountTransactionState.DialogState.Filters,
            )
        }
    }

    private fun handleConfirmFilterDialog() {
        val selectedFilters = state.checkboxOptions.filter { it.isChecked }
        val filteredRecords = applyTransactionFilters(selectedFilters)
        mutableStateFlow.update {
            it.copy(
                selectedFilters = selectedFilters,
                filteredData = filteredRecords,
                isRefreshing = false,
                isEmpty = filteredRecords.isEmpty(),
            )
        }
        handleDismissDialog()
    }

    private fun handleResetFilters() {
        mutableStateFlow.update {
            it.copy(
                checkboxOptions = it.checkboxOptions.map { cb -> cb.copy(isChecked = false) },
                selectedFilters = emptyList(),
                accountDurationFiltersCount = 0,
                accountTypeFiltersCount = 0,
                selectedRadioButton = null,
            )
        }
    }

    private fun toggleCheckbox(label: StringResource, type: TransactionFilterType) {
        val updatedCheckboxes = mutableStateFlow.value.checkboxOptions.map {
            if (it.statusLabel == label && it.type == type) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }

        val typeCount = updatedCheckboxes.count { it.isChecked && it.type == TransactionFilterType.TRANSACTION_TYPE }
        val durationCount = updatedCheckboxes.count { it.isChecked && it.type == TransactionFilterType.DURATION }

        mutableStateFlow.update {
            it.copy(
                checkboxOptions = updatedCheckboxes,
                accountTypeFiltersCount = typeCount,
                accountDurationFiltersCount = durationCount,
            )
        }
    }

    fun loadTransactions() {
        val route = savedStateHandle.toRoute<AccountTransactionsNavRoute>()
        when (route.accountType) {
            Constants.SAVINGS_ACCOUNT -> loadSavingsWithAssociations(route.accountId)
            else -> {}
        }
    }

    private fun observeAccountTypeAndInitCheckboxes() {
        val checkboxes = StatusUtils.getTransactionCheckboxes()
        mutableStateFlow.update {
            it.copy(
                checkboxOptions = checkboxes,
                selectedFilters = emptyList(),
                isRefreshing = false,
            )
        }
    }

    fun loadSavingsWithAssociations(accountId: Long) {
        viewModelScope.launch {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                accountId,
                Constants.TRANSACTIONS,
            ).collect {
                    dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        mutableStateFlow.update {
                            it.copy(
                                dialogState = AccountTransactionState.DialogState.Error(dataState.message),
                            )
                        }
                    }

                    DataState.Loading -> {
                        mutableStateFlow.update {
                            it.copy(dialogState = AccountTransactionState.DialogState.Loading)
                        }
                    }

                    is DataState.Success -> {
                        val transactions = dataState.data.transactions
                        val groupedTransactions = transactions.groupBy { transaction ->
                            DateHelper.getFormattedDateWithPrefix(transaction.date)
                        }
                        mutableStateFlow.update {
                            it.copy(
                                dialogState = null,
                                data = transactions,
                                filteredData = groupedTransactions,
                                isEmpty = groupedTransactions.isEmpty(),
                            )
                        }
                    }
                }
            }
        }
    }

    internal fun applyTransactionFilters(
        selectedFilters: List<TransactionCheckboxStatus>,
    ): Map<String, List<Transactions>> {
        val allTransactions = state.data
        val typeFilters = selectedFilters.filter { it.type == TransactionFilterType.TRANSACTION_TYPE }

        val typeFiltered = when {
            typeFilters.any { it.statusLabel == Res.string.feature_transaction_filter_credit } &&
                typeFilters.none { it.statusLabel == Res.string.feature_transaction_filter_debit } ->
                allTransactions.filter { getTransactionCreditStatus(it.transactionType) }

            typeFilters.any { it.statusLabel == Res.string.feature_transaction_filter_debit } &&
                typeFilters.none { it.statusLabel == Res.string.feature_transaction_filter_credit } ->
                allTransactions.filter { !getTransactionCreditStatus(it.transactionType) }

            typeFilters.any { it.statusLabel == Res.string.feature_transaction_filter_credit } &&
                typeFilters.any { it.statusLabel == Res.string.feature_transaction_filter_debit } ->
                allTransactions

            else -> allTransactions
        }

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dateRanges = when (state.selectedRadioButton) {
            Res.string.feature_transaction_filter_past_month ->
                now.minus(1, DateTimeUnit.MONTH) to now
            Res.string.feature_transaction_filter_past_3_months ->
                now.minus(3, DateTimeUnit.MONTH) to now
            Res.string.feature_transaction_filter_past_6_months ->
                now.minus(6, DateTimeUnit.MONTH) to now
            Res.string.feature_transaction_filter_past_1_year ->
                now.minus(1, DateTimeUnit.YEAR) to now
            Res.string.feature_transaction_filter_past_2_years ->
                now.minus(2, DateTimeUnit.YEAR) to now
            else -> null
        }

        val durationFiltered = if (dateRanges == null) {
            typeFiltered
        } else {
            typeFiltered.filter { transaction ->
                val dateList = transaction.date
                val transactionDate = LocalDate(dateList[0], dateList[1], dateList[2])
                transactionDate in dateRanges.first..dateRanges.second
            }
        }

        return durationFiltered.groupBy { transaction ->
            DateHelper.getFormattedDateWithPrefix(transaction.date)
        }
    }
}

internal data class AccountTransactionState(
    val isRefreshing: Boolean = false,
    val data: List<Transactions> = emptyList(),
    val filteredData: Map<String, List<Transactions>> = emptyMap(),
    val dialogState: DialogState?,
    val checkboxOptions: List<TransactionCheckboxStatus> = emptyList(),
    val selectedFilters: List<TransactionCheckboxStatus> = emptyList(),
    val toggleFilterDialog: Boolean = false,
    val accountTypeFiltersCount: Int? = 0,
    val accountDurationFiltersCount: Int? = 0,
    val selectedRadioButton: StringResource? = null,
    val isEmpty: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
        data object Filters : DialogState
    }
    val isAnyFilterSelected = checkboxOptions.any { it.isChecked } || selectedRadioButton != null
}

internal sealed interface AccountTransactionAction {
    data object Refresh : AccountTransactionAction
    data object DismissDialog : AccountTransactionAction
    data object OnNavigateBackClick : AccountTransactionAction
    data object ToggleFilter : AccountTransactionAction
    data object ResetFilters : AccountTransactionAction
    data object GetFilterResults : AccountTransactionAction
    data class ToggleCheckbox(
        val label: StringResource,
        val type: TransactionFilterType,
    ) : AccountTransactionAction
    data class ToggleRadioButton(
        val label: StringResource,
    ) : AccountTransactionAction
}

sealed interface AccountTransactionEvent {
    data object OnNavigateBack : AccountTransactionEvent
}

internal fun getTransactionCreditStatus(transactionType: TransactionType?): Boolean {
    return transactionType?.run {
        when {
            deposit == true -> true
            dividendPayout == true -> false
            withdrawal == true -> false
            interestPosting == true -> true
            feeDeduction == true -> false
            initiateTransfer == true -> false
            approveTransfer == true -> false
            withdrawTransfer == true -> false
            rejectTransfer == true -> true
            overdraftFee == true -> false
            else -> true
        }
    } ?: false
}

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
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.accounts.model.TransactionCheckboxStatus
import org.mifos.mobile.feature.accounts.model.TransactionFilterType
import org.mifos.mobile.feature.accounts.utils.StatusUtils
import kotlin.collections.map
/**
 * ViewModel for managing the state and logic of the account transactions screen.
 *
 * This ViewModel handles fetching transactions for both savings and loan accounts, applying filters
 * based on transaction type and date range, and managing the UI state for the screen, including
 * dialogs and loading indicators.
 *
 * @property savingsAccountRepositoryImpl The repository for savings account data.
 * @property loanAccountRepositoryImpl The repository for loan account data.
 * @property savedStateHandle A handle to saved state data, used to retrieve navigation arguments.
 */
internal class AccountsTransactionViewModel(
    private val savingsAccountRepositoryImpl: SavingsAccountRepository,
    private val loanAccountRepositoryImpl: LoanRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<AccountTransactionState, AccountTransactionEvent, AccountTransactionAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<AccountTransactionsNavRoute>()
        AccountTransactionState(
            dialogState = AccountTransactionState.DialogState.Loading,
            accountId = route.accountId,
            accountType = route.accountType,
        )
    },
) {
    init {
        loadTransactions()
        observeAccountTypeAndInitCheckboxes()
    }

    /**
     * Handles incoming actions from the UI, updating the state accordingly.
     *
     * @param action The [AccountTransactionAction] to be handled.
     */
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

    /**
     * Dismisses any active dialog by setting the dialog state to null.
     */
    private fun handleDismissDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = null,
            )
        }
    }

    /**
     * Shows the filter dialog by updating the dialog state.
     */
    private fun handleToggleFilterDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = AccountTransactionState.DialogState.Filters,
            )
        }
    }

    /**
     * Applies the selected filters and updates the UI state with the filtered data.
     *
     * This method filters the transactions based on the checked checkboxes and selected radio button,
     * updates the `filteredData` and `selectedFilters` in the state, and then dismisses the filter dialog.
     */
    private fun handleConfirmFilterDialog() {
        val selectedFilters = state.checkboxOptions.filter { it.isChecked }
        val filteredRecords = applyTransactionFilters(selectedFilters)
        mutableStateFlow.update {
            it.copy(
                selectedFilters = selectedFilters,
                filteredData = filteredRecords,
                isRefreshing = false,
                isFilteredRecordsEmpty = filteredRecords.isEmpty(),
            )
        }
        handleDismissDialog()
    }

    /**
     * Resets all filters to their default state.
     *
     * This method clears all selected checkboxes and radio buttons and resets the filter counts.
     */
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

    /**
     * Toggles the checked state of a specific checkbox and updates the filter counts.
     *
     * @param label The string resource ID of the checkbox label.
     * @param type The [TransactionFilterType] of the checkbox.
     */
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

    /**
     * Initiates the process of loading transactions based on the account type.
     */
    fun loadTransactions() {
        when (state.accountType) {
            Constants.SAVINGS_ACCOUNT -> loadSavingsWithAssociations()
            Constants.LOAN_ACCOUNT -> loadLoanTransactions()
            else -> {}
        }
    }

    /**
     * Observes the account type and initializes the checkbox options for filtering.
     */
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

    /**
     * Loads savings account transactions and their associations.
     */
    fun loadSavingsWithAssociations() {
        viewModelScope.launch {
            savingsAccountRepositoryImpl.getSavingsWithAssociations(
                state.accountId,
                Constants.TRANSACTIONS,
            ).collect { dataState ->
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
                            .map { it.toUiTransaction() }

                        val groupedTransactions = transactions.groupBy { transaction ->
                            DateHelper.getFormattedDateWithPrefix(transaction.date)
                        }

                        mutableStateFlow.update {
                            it.copy(
                                dialogState = null,
                                data = transactions,
                                filteredData = groupedTransactions,
                                isEmpty = transactions.isEmpty(),
                                isFilteredRecordsEmpty = groupedTransactions.isEmpty(),
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Loads loan account transactions.
     */
    private fun loadLoanTransactions() {
        viewModelScope.launch {
            loanAccountRepositoryImpl.getLoanWithAssociations(
                loanId = state.accountId,
                associationType = Constants.TRANSACTIONS,
            ).collect { dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        mutableStateFlow.update {
                            it.copy(dialogState = AccountTransactionState.DialogState.Error(dataState.message))
                        }
                    }

                    DataState.Loading -> {
                        mutableStateFlow.update {
                            it.copy(dialogState = AccountTransactionState.DialogState.Loading)
                        }
                    }

                    is DataState.Success -> {
                        val transactions = dataState.data?.transactions
                            ?.mapNotNull { it?.toUiTransaction() } ?: emptyList()

                        val grouped = transactions.groupBy {
                            DateHelper.getFormattedDateWithPrefix(it.date)
                        }

                        mutableStateFlow.update {
                            it.copy(
                                dialogState = null,
                                data = transactions,
                                filteredData = grouped,
                                isFilteredRecordsEmpty = grouped.isEmpty(),
                                isEmpty = transactions.isEmpty(),
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Applies filters to the transaction list based on selected criteria.
     *
     * @param selectedFilters A list of [TransactionCheckboxStatus] representing the active checkbox filters.
     * @return A map of filtered transactions grouped by date.
     */
    internal fun applyTransactionFilters(
        selectedFilters: List<TransactionCheckboxStatus>,
    ): Map<String, List<UiTransaction>> {
        val allTransactions = state.data
        val typeFilters = selectedFilters.filter { it.type == TransactionFilterType.TRANSACTION_TYPE }

        val typeFiltered = when {
            typeFilters.any { it.statusLabel == Res.string.feature_transaction_filter_credit } &&
                typeFilters.none { it.statusLabel == Res.string.feature_transaction_filter_debit } ->
                allTransactions.filter { getTransactionCreditStatus(it.type) }

            typeFilters.any { it.statusLabel == Res.string.feature_transaction_filter_debit } &&
                typeFilters.none { it.statusLabel == Res.string.feature_transaction_filter_credit } ->
                allTransactions.filter { !getTransactionCreditStatus(it.type) }

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

    /**
     * Converts a domain `Transactions` object to a UI-friendly `UiTransaction` object.
     *
     * @return A `UiTransaction` object.
     */
    fun Transactions.toUiTransaction() = UiTransaction(
        id = id?.toLong(),
        date = date,
        amount = amount,
        type = transactionType,
        typeValue = transactionType?.value,
        isCredit = getTransactionCreditStatus(transactionType),
        currency = currency?.code ?: "USD",
    )

    /**
     * Converts a domain `Transaction` object to a UI-friendly `UiTransaction` object.
     *
     * @return A `UiTransaction` object.
     */
    fun Transaction.toUiTransaction() = UiTransaction(
        id = id,
        date = date,
        amount = amount,
        typeValue = type.value,
        isCredit = when (type.value?.lowercase()) {
            "disbursement", "repayment" -> false
            else -> true
        },
        currency = currency?.code ?: "USD",
    )
}

/**
 * A data class representing a transaction for the UI.
 *
 * @property id The unique ID of the transaction.
 * @property date A list of integers representing the date (e.g., [year, month, day]).
 * @property amount The transaction amount.
 * @property type The transaction type from the domain model.
 * @property typeValue The string value of the transaction type.
 * @property isCredit A boolean indicating if the transaction is a credit (true) or debit (false).
 * @property currency The currency code (e.g., "USD").
 */
data class UiTransaction(
    val id: Long?,
    val date: List<Int>,
    val amount: Double?,
    val type: TransactionType? = null,
    val typeValue: String? = null,
    val isCredit: Boolean?,
    val currency: String,
)

/**
 * The state of the account transactions screen.
 *
 * @property accountType The type of account (e.g., "SAVINGS_ACCOUNT", "LOAN_ACCOUNT").
 * @property accountId The ID of the account.
 * @property isRefreshing A boolean indicating if the data is currently being refreshed.
 * @property data The original list of all transactions.
 * @property filteredData The map of filtered transactions, grouped by date.
 * @property dialogState The current state of any dialog being shown.
 * @property checkboxOptions The list of all available checkbox filter options.
 * @property selectedFilters The list of currently active checkbox filters.
 * @property toggleFilterDialog A boolean to show or hide the filter dialog.
 * @property accountTypeFiltersCount The number of active transaction type filters.
 * @property accountDurationFiltersCount The number of active duration filters.
 * @property selectedRadioButton The string resource ID of the selected radio button filter.
 * @property isEmpty A boolean indicating if the filtered data is empty.
 */
internal data class AccountTransactionState(
    val accountType: String,
    val accountId: Long,
    val isRefreshing: Boolean = false,
    val data: List<UiTransaction> = emptyList(),
    val filteredData: Map<String, List<UiTransaction>> = emptyMap(),
    val dialogState: DialogState?,
    val checkboxOptions: List<TransactionCheckboxStatus> = emptyList(),
    val selectedFilters: List<TransactionCheckboxStatus> = emptyList(),
    val toggleFilterDialog: Boolean = false,
    val accountTypeFiltersCount: Int? = 0,
    val accountDurationFiltersCount: Int? = 0,
    val selectedRadioButton: StringResource? = null,
    val isEmpty: Boolean = false,
    val isFilteredRecordsEmpty: Boolean = false,
) {
    /**
     * Sealed interface representing the different states of the dialog.
     */
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
        data object Filters : DialogState
    }

    /**
     * A computed property to check if any filter is currently selected.
     */
    val isAnyFilterSelected = checkboxOptions.any { it.isChecked } || selectedRadioButton != null
}

/**
 * Sealed interface representing actions that can be performed on the account transactions screen.
 */
internal sealed interface AccountTransactionAction {
    data object Refresh : AccountTransactionAction
    data object DismissDialog : AccountTransactionAction
    data object OnNavigateBackClick : AccountTransactionAction
    data object ToggleFilter : AccountTransactionAction
    data object ResetFilters : AccountTransactionAction
    data object GetFilterResults : AccountTransactionAction

    /**
     * Action to toggle a specific checkbox filter.
     *
     * @property label The string resource ID of the checkbox label.
     * @property type The [TransactionFilterType] of the checkbox.
     */
    data class ToggleCheckbox(
        val label: StringResource,
        val type: TransactionFilterType,
    ) : AccountTransactionAction

    /**
     * Action to toggle a specific radio button filter.
     *
     * @property label The string resource ID of the radio button label.
     */
    data class ToggleRadioButton(
        val label: StringResource,
    ) : AccountTransactionAction
}

/**
 * Sealed interface representing one-time events to be sent to the UI.
 */
sealed interface AccountTransactionEvent {
    data object OnNavigateBack : AccountTransactionEvent
}

/**
 * Determines if a transaction type represents a credit.
 *
 * @param transactionType The transaction type from the domain model.
 * @return `true` if the transaction is a credit, `false` otherwise.
 */
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

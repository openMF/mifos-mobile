/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountDetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_number_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_currency_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_due_date_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installment_amount_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_outstanding_balance_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_product_type_label
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.LoanStatus
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.qr.getAccountDetailsInString
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.loanaccount.component.LoanActionItems
import org.mifos.mobile.feature.loanaccount.component.loanAccountActions
/**
 * ViewModel for managing the state and logic of the Loan Account Details screen.
 *
 * @param loanAccountRepositoryImp Repository for fetching loan account data.
 * @param savedStateHandle Used to retrieve route arguments such as accountId.
 */
internal class LoanAccountDetailsViewModel(
    private val loanAccountRepositoryImp: LoanRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanAccountDetailsState, LoanAccountDetailsEvent, LoanAccountDetailsAction>(
    initialState = run {
        val accountId = savedStateHandle.toRoute<LoanAccountDetailsRoute>().accountId
        LoanAccountDetailsState(
            accountId = accountId,
            dialogState = LoanAccountDetailsState.DialogState.Loading,
            items = loanAccountActions,
        )
    },
) {

    init {
        // Automatically fetch savings account info on ViewModel creation
        fetchLoanAccount()
    }

    private fun fetchLoanAccount() {
        viewModelScope.launch {
            loanAccountRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                state.accountId,
            ).collect { result ->
                sendAction(LoanAccountDetailsAction.Internal.LoanResultReceived(result))
            }
        }
    }

    /**
     * Handles incoming UI actions.
     */
    override fun handleAction(action: LoanAccountDetailsAction) {
        when (action) {
            LoanAccountDetailsAction.OnNavigateBack -> sendEvent(LoanAccountDetailsEvent.NavigateBack)

            LoanAccountDetailsAction.OnRetry -> fetchLoanAccount()

            is LoanAccountDetailsAction.OnNavigateToAction ->
                sendEvent(LoanAccountDetailsEvent.NavigateToAction(action.route))

            is LoanAccountDetailsAction.Internal.LoanResultReceived ->
                handleLoanAccountResult(action.dataState)

            LoanAccountDetailsAction.DismissDialog -> handleDismissDialog()
        }
    }

    /**
     * Handles the dismissal of dialog state.
     */
    private fun handleDismissDialog() {
        mutableStateFlow.update { it.copy(dialogState = null) }
    }

    fun getQrString(): String {
        val officeName = userPreferencesRepository.userInfo.value.officeName
        return if (officeName.isNotEmpty()) {
            return getAccountDetailsInString(
                state.accountId.toInt(),
                officeName,
                AccountType.LOAN.name,
            )
        } else {
            ""
        }
    }

    /**
     * Processes the loan account result from the repository.
     */
    private fun handleLoanAccountResult(dataState: DataState<LoanWithAssociations?>) {
        when (dataState) {
            is DataState.Error -> {
                mutableStateFlow.update {
                    it.copy(dialogState = LoanAccountDetailsState.DialogState.Error(dataState.message))
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(dialogState = LoanAccountDetailsState.DialogState.Loading)
                }
            }

            is DataState.Success -> {
                extractDetails(dataState.data)
            }
        }
    }

    /**
     * Extracts relevant loan account details and updates the UI state.
     */
    private fun extractDetails(loan: LoanWithAssociations?) {
        val displayItems = listOf(
            LabelValueItem(Res.string.feature_loan_account_number_label, loan?.accountNo ?: "N/A"),
            LabelValueItem(
                Res.string.feature_loan_outstanding_balance_label,
                loan?.summary?.totalOutstanding?.let {
                    CurrencyFormatter.format(it, loan.currency?.code, loan.currency?.decimalPlaces?.toInt())
                } ?: "N/A",
            ),
            LabelValueItem(
                Res.string.feature_loan_product_type_label,
                loan?.loanProductName ?: "N/A",
            ),
            LabelValueItem(
                Res.string.feature_loan_currency_label,
                loan?.currency?.displayLabel ?: "N/A",
            ),
        )

        val transactions = loan?.transactions?.firstOrNull()?.let { txn ->
            listOf(
                LabelValueItem(
                    Res.string.feature_loan_installment_amount_label,
                    txn.amount.toString(),
                ),
                LabelValueItem(
                    Res.string.feature_loan_due_date_label,
                    DateHelper.getDateAsString(txn.submittedOnDate),
                ),
            )
        }

        mutableStateFlow.update {
            it.copy(
                accountId = loan?.id?.toLong() ?: -1L,
                isActive = loan?.status?.value == LoanStatus.ACTIVE.status,
                accountNumber = loan?.accountNo,
                clientName = loan?.clientName,
                product = loan?.loanProductName,
                submissionDate = DateHelper.getDateAsString(loan?.timeline?.submittedOnDate ?: emptyList()),
                displayItems = displayItems,
                transactionList = transactions,
                dialogState = null,
                totalOutStandingBalance = loan?.summary?.totalOutstanding,
            )
        }
    }
}

/**
 * UI State for the Loan Account Details screen.
 *
 * @property accountId Unique ID for the loan account.
 * @property displayItems List of account metadata to be displayed.
 * @property transactionList List of most recent transaction details.
 * @property isActive True if the loan is active.
 * @property items List of quick action items (e.g., Repay, Foreclose).
 * @property isUpdatable Whether the loan is editable (e.g., in a pending state).
 * @property dialogState State representing UI dialogs like loading or error.
 */
@Immutable
internal data class LoanAccountDetailsState(
    val accountId: Long = -1L,
    val totalOutStandingBalance: Double? = null,
    val isEmpty: Boolean = true,
    val clientName: String? = "",
    val submissionDate: String? = "",
    val accountNumber: String? = "",
    val product: String? = "",
    val displayItems: List<LabelValueItem> = emptyList(),
    val transactionList: List<LabelValueItem>? = emptyList(),
    val isActive: Boolean = false,
    val items: ImmutableList<LoanActionItems>,
    val isUpdatable: Boolean = false,
    val dialogState: DialogState?,
) {
    /**
     * Represents UI dialog states.
     */
    sealed interface DialogState {
        /** Shown when an error occurs. */
        data class Error(val message: String) : DialogState

        /** Shown during loading state. */
        data object Loading : DialogState
    }
}

/**
 * One-time navigation or effect events for the Loan Account Details screen.
 */
sealed interface LoanAccountDetailsEvent {
    /** Trigger navigation back. */
    data object NavigateBack : LoanAccountDetailsEvent

    /** Trigger navigation to a specific loan action screen. */
    data class NavigateToAction(val route: String) : LoanAccountDetailsEvent
}

/**
 * Actions triggered from the UI layer to the ViewModel.
 */
sealed interface LoanAccountDetailsAction {
    /** User tapped back. */
    data object OnNavigateBack : LoanAccountDetailsAction

    /** User tapped on a quick action (e.g., Repay). */
    data class OnNavigateToAction(val route: String) : LoanAccountDetailsAction

    /** User dismissed a dialog. */
    data object DismissDialog : LoanAccountDetailsAction

    /** When user retry */
    data object OnRetry : LoanAccountDetailsAction

    /**
     * Internal-only actions such as results from repository calls.
     */
    sealed interface Internal : LoanAccountDetailsAction {
        /** Result of the loan account data fetch. */
        data class LoanResultReceived(val dataState: DataState<LoanWithAssociations?>) : Internal
    }
}

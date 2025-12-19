/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.transactionDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_generic_error_server
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.TransactionDetails
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

// State
data class TransactionDetailsState(
    val transactionId: Long,
    val accountId: Long,
    val accountType: String,
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val transaction: UiTransactionDetails? = null,
)

// Actions
sealed interface TransactionDetailsAction {
    data object OnNavigateBack : TransactionDetailsAction
    data object Retry : TransactionDetailsAction
}

// Events
sealed interface TransactionDetailsEvent {
    data object NavigateBack : TransactionDetailsEvent
}

class TransactionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val savingsRepository: SavingsAccountRepository,
    private val loanRepository: LoanRepository,
) : BaseViewModel<TransactionDetailsState, TransactionDetailsEvent, TransactionDetailsAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<TransactionDetailsNavRoute>()
        TransactionDetailsState(
            transactionId = route.transactionId.toLongOrNull() ?: -1L,
            accountType = route.accountType,
            accountId = route.accountId,
        )
    },
) {

    init {
        fetchTransactionDetails()
    }

    override fun handleAction(action: TransactionDetailsAction) {
        when (action) {
            TransactionDetailsAction.OnNavigateBack -> sendEvent(TransactionDetailsEvent.NavigateBack)
            TransactionDetailsAction.Retry -> fetchTransactionDetails()
        }
    }

    private fun fetchTransactionDetails() {
        updateState { it.copy(uiState = ScreenUiState.Loading) }

        viewModelScope.launch {
            if (state.transactionId == -1L) {
                updateState { it.copy(uiState = ScreenUiState.Error(Res.string.feature_generic_error_server)) }
                return@launch
            }

            when (state.accountType) {
                Constants.SAVINGS_ACCOUNT -> loadSavingsTransaction()
                Constants.LOAN_ACCOUNT -> loadLoanTransaction()
                else -> {
                    updateState { it.copy(uiState = ScreenUiState.Error(Res.string.feature_generic_error_server)) }
                }
            }
        }
    }

    private suspend fun loadSavingsTransaction() {
        savingsRepository.getSavingsAccountTransactionDetails(state.accountId, state.transactionId)
            .catch { exception ->
                emit(DataState.Error(exception))
            }
            .collect { dataState ->
                handleDataState(dataState)
            }
    }

    private suspend fun loadLoanTransaction() {
        loanRepository.getLoanTransactionDetails(state.accountId, state.transactionId)
            .catch { exception ->
                emit(DataState.Error(exception))
            }
            .collect { dataState ->
                handleDataState(dataState)
            }
    }

    private fun handleDataState(dataState: DataState<TransactionDetails>) {
        when (dataState) {
            is DataState.Success -> {
                updateState {
                    it.copy(
                        uiState = ScreenUiState.Success,
                        transaction = dataState.data.toUiTransaction(),
                    )
                }
            }
            is DataState.Error -> {
                updateState { it.copy(uiState = ScreenUiState.Error(Res.string.feature_generic_error_server)) }
            }
            DataState.Loading -> {
                updateState { it.copy(uiState = ScreenUiState.Loading) }
            }
        }
    }

    private fun TransactionDetails.toUiTransaction(): UiTransactionDetails {
        val statusKey = if (this.isReversed) "reversed" else "success"

        // Flatten balances for the UI
        val balance = this.balances.running

        return UiTransactionDetails(
            id = this.id,
            date = this.date,
            amount = this.amount,
            typeValue = this.transactionName,
            isCredit = this.isCredit,
            currency = this.currencyCode,
            accountNo = this.accountNo,
            status = statusKey,
            outstandingBalance = balance,
            principal = this.balances.principal,
            interest = this.balances.interest,
            fees = this.balances.fee,
            penalties = this.balances.penalty,
        )
    }

    private fun updateState(update: (TransactionDetailsState) -> TransactionDetailsState) {
        mutableStateFlow.update(update)
    }
}

data class UiTransactionDetails(
    val id: Long?,
    val date: List<Int>,
    val amount: Double?,
    val status: String,
    val typeValue: String? = null,
    val isCredit: Boolean?,
    val currency: String,
    val accountNo: String? = null,
    val principal: Double? = null,
    val interest: Double? = null,
    val fees: Double? = null,
    val penalties: Double? = null,
    val outstandingBalance: Double? = null,
)

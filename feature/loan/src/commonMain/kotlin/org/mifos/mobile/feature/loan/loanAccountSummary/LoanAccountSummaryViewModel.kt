/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountSummary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class LoanAccountSummaryViewModel(
    private val loanRepositoryImp: LoanRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanAccountSummaryState, LoanAccountSummaryEvent, LoanAccountSummaryAction>(
    initialState = LoanAccountSummaryState(
        dialogState = null,
        loanId = savedStateHandle.getStateFlow<Long?>(Constants.LOAN_ID, null).value,
    ),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
            }
        }
        loadLoanAccountSummary()
    }

    override fun handleAction(action: LoanAccountSummaryAction) {
        when (action) {
            is LoanAccountSummaryAction.LoanIdChanged -> {
                updateState { it.copy(loanId = action.loanId) }
            }

            LoanAccountSummaryAction.BackPress -> {
                sendEvent(LoanAccountSummaryEvent.NavigateBack)
            }

            is LoanAccountSummaryAction.Internal.ReceiveLoanSummaryResult -> {
                handleLoanSummaryResult(action)
            }
        }
    }

    private fun updateState(update: (LoanAccountSummaryState) -> LoanAccountSummaryState) {
        mutableStateFlow.update(update)
    }

    private fun loadLoanAccountSummary() {
        updateState { it.copy(dialogState = LoanAccountSummaryState.DialogState.Loading) }
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, state.loanId)
                .catch { exception ->
                    sendAction(
                        LoanAccountSummaryAction.Internal.ReceiveLoanSummaryResult(
                            DataState.Error(exception),
                        ),
                    )
                }
                .collect { result ->
                    sendAction(LoanAccountSummaryAction.Internal.ReceiveLoanSummaryResult(result))
                }
        }
    }

    private fun handleLoanSummaryResult(action: LoanAccountSummaryAction.Internal.ReceiveLoanSummaryResult) {
        updateState {
            when (val result = action.loanSummaryResult) {
                is DataState.Error -> it.copy(
                    dialogState = LoanAccountSummaryState.DialogState.Error(
                        result.exception.message ?: "An error occurred",
                    ),
                )

                is DataState.Loading -> it.copy(dialogState = LoanAccountSummaryState.DialogState.Loading)

                is DataState.Success -> {
                    val loan = result.data
                    if (loan == null) {
                        it.copy(dialogState = LoanAccountSummaryState.DialogState.Error("Loan details not found"))
                    } else {
                        it.copy(loanAccountAssociations = loan, dialogState = null)
                    }
                }
            }
        }
    }
}

@Parcelize
data class LoanAccountSummaryState(
    val loanId: Long? = null,
    val dialogState: DialogState?,
    val loanAccountAssociations: LoanWithAssociations? = null,
    val isOnline: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface LoanAccountSummaryEvent {
    data object NavigateBack : LoanAccountSummaryEvent
}

sealed interface LoanAccountSummaryAction {
    data class LoanIdChanged(val loanId: Long) : LoanAccountSummaryAction
    data object BackPress : LoanAccountSummaryAction

    sealed class Internal : LoanAccountSummaryAction {
        data class ReceiveLoanSummaryResult(
            val loanSummaryResult: DataState<LoanWithAssociations?>,
        ) : Internal()
    }
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanRepaymentSchedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.repayment_schedule
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class LoanRepaymentScheduleViewModel(
    private val loanRepositoryImp: LoanRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanRepaymentScheduleState, LoanRepaymentScheduleEvent, LoanRepaymentScheduleAction>(
    initialState = LoanRepaymentScheduleState(
        dialogState = null,
        loanId = savedStateHandle.getStateFlow<Long?>(
            Constants.LOAN_ID,
            initialValue = null,
        ).value,
    ),
) {
    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
            }
        }
        fetchLoanWithAssociations()
    }

    private fun updateState(update: (LoanRepaymentScheduleState) -> LoanRepaymentScheduleState) {
        mutableStateFlow.update(update)
    }

    private fun fetchLoanWithAssociations() {
        updateState { it.copy(dialogState = LoanRepaymentScheduleState.DialogState.Loading) }
        viewModelScope.launch {
            val errorMessage = getString(Res.string.repayment_schedule)
            state.loanId?.let { loanId ->
                loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, loanId)
                    .catch {
                        updateState {
                            it.copy(
                                dialogState = LoanRepaymentScheduleState.DialogState.Error(errorMessage),
                            )
                        }
                    }
                    .collect { loanData ->
                        updateState {
                            it.copy(
                                loanWithAssociations = loanData.data,
                                dialogState = null,
                            )
                        }
                    }
            } ?: updateState { it.copy(dialogState = null) }
        }
    }

    override fun handleAction(action: LoanRepaymentScheduleAction) {
        when (action) {
            LoanRepaymentScheduleAction.RetryClicked -> {
                fetchLoanWithAssociations()
            }
            LoanRepaymentScheduleAction.BackPress -> sendEvent(LoanRepaymentScheduleEvent.NavigateBack)
        }
    }
}

@Parcelize
data class LoanRepaymentScheduleState(
    val loanId: Long? = null,
    val loanWithAssociations: LoanWithAssociations? = null,
    val dialogState: DialogState?,
    val isOnline: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface LoanRepaymentScheduleEvent {
    data object NavigateBack : LoanRepaymentScheduleEvent
}

sealed interface LoanRepaymentScheduleAction {
    data object RetryClicked : LoanRepaymentScheduleAction
    data object BackPress : LoanRepaymentScheduleAction
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountWithdraw

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.error_loan_account_withdraw
import mifos_mobile.feature.loan.generated.resources.loan_application_withdrawn_successfully
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class LoanAccountWithdrawViewModel(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanAccountWithdrawState, LoanAccountWithdrawEvent, LoanAccountWithdrawAction>(
    initialState = LoanAccountWithdrawState(
        dialogState = null,
        loanId = savedStateHandle.getStateFlow<Long?>(
            key = Constants.LOAN_ID,
            initialValue = null,
        ).value,
    ),
) {

    init {
        loanWithAssociations()
    }

    private fun updateState(update: (LoanAccountWithdrawState) -> LoanAccountWithdrawState) {
        mutableStateFlow.update(update)
    }

    fun loanWithAssociations() {
        updateState { it.copy(dialogState = LoanAccountWithdrawState.DialogState.Loading) }
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(Constants.TRANSACTIONS, state.loanId)
                .catch { error ->
                    updateState {
                        it.copy(dialogState = LoanAccountWithdrawState.DialogState.Error(error.message.toString()))
                    }
                }.collect { loanAssociations ->
                    updateState {
                        it.copy(
                            dialogState = null,
                            loanWithAssociations = loanAssociations.data,
                        )
                    }
                }
        }
    }

    private fun withdrawLoanAccount() {
        val loanWithdraw = LoanWithdraw(
            withdrawnOnDate = DateHelper.getDateAsStringFromLong(
                Clock.System.now().toEpochMilliseconds(),
            ),
            note = state.loanReason,
            dateFormat = DateHelper.SHORT_MONTH,
            locale = "en",
        )
        updateState {
            it.copy(dialogState = LoanAccountWithdrawState.DialogState.Loading)
        }

        viewModelScope.launch {
            val errorMessage = getString(Res.string.error_loan_account_withdraw)
            val successMessage = getString(Res.string.loan_application_withdrawn_successfully)
            val response = loanRepositoryImp.withdrawLoanAccount(
                loanId = state.loanId,
                loanWithdraw = loanWithdraw,
            )
            when (response) {
                is DataState.Loading -> {
                    updateState {
                        it.copy(dialogState = LoanAccountWithdrawState.DialogState.Loading)
                    }
                }
                is DataState.Success -> {
                    updateState {
                        it.copy(dialogState = null)
                    }
                    sendEvent(LoanAccountWithdrawEvent.ShowToast(successMessage))
                    delay(1500)
                    sendEvent(LoanAccountWithdrawEvent.NavigateBack)
                }
                is DataState.Error -> {
                    updateState {
                        it.copy(dialogState = null)
                    }
                    sendEvent(
                        LoanAccountWithdrawEvent.ShowToast(
                            response.exception.message ?: errorMessage,
                        ),
                    )
                }
            }
        }
    }

    override fun handleAction(action: LoanAccountWithdrawAction) {
        when (action) {
            LoanAccountWithdrawAction.BackPress -> sendEvent(LoanAccountWithdrawEvent.NavigateBack)
            is LoanAccountWithdrawAction.LoanReasonChanged -> {
                updateState {
                    it.copy(loanReason = action.loanReason)
                }
            }
            is LoanAccountWithdrawAction.WithDrawClicked -> {
                withdrawLoanAccount()
            }
        }
    }
}

@Parcelize
data class LoanAccountWithdrawState(
    val loanId: Long? = null,
    val loanReason: String = "",
    val reason: String = "",
    val dialogState: DialogState?,
    val loanWithAssociations: LoanWithAssociations? = null,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface LoanAccountWithdrawEvent {
    data object NavigateBack : LoanAccountWithdrawEvent
    data class ShowToast(val message: String) : LoanAccountWithdrawEvent
}

sealed interface LoanAccountWithdrawAction {
    data class LoanReasonChanged(val loanReason: String) : LoanAccountWithdrawAction
    data object BackPress : LoanAccountWithdrawAction
    data object WithDrawClicked : LoanAccountWithdrawAction
}

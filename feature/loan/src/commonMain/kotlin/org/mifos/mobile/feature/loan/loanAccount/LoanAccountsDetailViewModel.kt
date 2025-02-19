/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.internet_not_connected
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.utils.BaseViewModel
// import org.mifos.mobile.core.model.enums.AccountType
// import org.mifos.mobile.core.datastore.UserPreferencesRepository

internal class LoanAccountsDetailViewModel(
    private val loanRepositoryImp: LoanRepository,
//    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanAccountsState, LoanAccountsEvent, LoanAccountAction>(
    initialState = LoanAccountsState(dialogState = null),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
                if (!isConnected) {
                    sendEvent(LoanAccountsEvent.ShowToast(getString(Res.string.internet_not_connected)))
                }
            }
        }

        val initialLoanId = savedStateHandle.getStateFlow<Long?>(LOAN_ID, null)

        initialLoanId.value?.let { loanId ->
            updateState {
                it.copy(loanId = loanId)
            }
        }

        loadLoanAccountDetails()
    }

    private fun updateState(update: (LoanAccountsState) -> LoanAccountsState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: LoanAccountAction) {
        when (action) {
            is LoanAccountAction.LoanIdChanged -> {
                updateState { it.copy(loanId = state.loanId) }
            }

            LoanAccountAction.BackPress -> {
                sendEvent(LoanAccountsEvent.NavigateBack)
            }

            is LoanAccountAction.Internal.ReceiveLoanAccountsResult -> {
                handleLoanAccountsResult(action)
            }

            LoanAccountAction.MakePaymentClicked -> {
                sendEvent(
                    LoanAccountsEvent.MakePayment(
                        state.loanId,
                        state.loanAccountAssociations?.summary?.totalOutstanding ?: 1.00,
                        TRANSFER_PAY_TO,
                    ),
                )
            }

            LoanAccountAction.UpdateLoanClicked -> {
                sendEvent(LoanAccountsEvent.UpdateLoan(state.loanId))
            }

            LoanAccountAction.ViewCharges -> {
                sendEvent(LoanAccountsEvent.ViewCharges)
            }

            LoanAccountAction.ViewGuarantorClicked -> {
                sendEvent(LoanAccountsEvent.ViewGuarantor(state.loanId))
            }

            LoanAccountAction.ViewLoanSummaryClicked -> {
                sendEvent(LoanAccountsEvent.ViewLoanSummary(state.loanId))
            }

            LoanAccountAction.ViewQRClicked -> {
                sendEvent(LoanAccountsEvent.ViewQr(state.loanId))
            }

            LoanAccountAction.ViewRepaymentScheduleClicked -> {
                sendEvent(LoanAccountsEvent.ViewRepaymentSchedule(state.loanId))
            }

            LoanAccountAction.ViewTransactionsClicked -> {
                sendEvent(LoanAccountsEvent.ViewTransactions(state.loanId))
            }

            LoanAccountAction.WithDrawLoanClicked -> {
                sendEvent(LoanAccountsEvent.WithDrawLoan(state.loanId))
            }

            LoanAccountAction.RetryConnectionClicked -> {
                loadLoanAccountDetails()
            }
        }
    }

    private fun loadLoanAccountDetails() {
        viewModelScope.launch {
            updateState {
                it.copy(dialogState = LoanAccountsState.DialogState.Loading)
            }
            loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, state.loanId)
                .catch { exception ->
                    sendAction(
                        LoanAccountAction.Internal.ReceiveLoanAccountsResult(
                            DataState.Error(
                                exception,
                            ),
                        ),
                    )
                }
                .collect { result ->
                    sendAction(LoanAccountAction.Internal.ReceiveLoanAccountsResult(result))
                }
        }
    }

    private fun handleLoanAccountsResult(action: LoanAccountAction.Internal.ReceiveLoanAccountsResult) {
        updateState {
            when (val result = action.loanAccountsResult) {
                is DataState.Error -> it.copy(
                    dialogState = LoanAccountsState.DialogState.Error(
                        result.exception.message ?: "An error occurred",
                    ),
                )

                is DataState.Loading -> it.copy(dialogState = LoanAccountsState.DialogState.Loading)

                is DataState.Success -> {
                    val loan = result.data
                    when {
                        loan == null -> it.copy(dialogState = LoanAccountsState.DialogState.Error("Accounts not found"))
                        loan.status?.active == true -> it.copy(
                            loanAccountAssociations = loan,
                            dialogState = null,
                        )

                        loan.status?.pendingApproval == true ->
                            it.copy(
                                dialogState = LoanAccountsState.DialogState.ApprovalPending,
                            )
                        loan.status?.waitingForDisbursal == true ->
                            it.copy(
                                dialogState = LoanAccountsState.DialogState.WaitingForDisburse,
                            )
                        else -> it.copy(loanAccountAssociations = loan, dialogState = null)
                    }
                }
            }
        }
    }

//    fun getQrString(): String {
//        return QrCodeGenerator.getAccountDetailsInString(
//            loanWithAssociations?.accountNo,
//            preferencesHelper.officeName,
//            AccountType.LOAN,
//        )
//    }
}

@Parcelize
data class LoanAccountsState(
    val loanId: Long = -1L,
    val dialogState: DialogState?,
    val loanAccountAssociations: LoanWithAssociations? = null,
    val isOnline: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object ApprovalPending : DialogState

        @Parcelize
        data object WaitingForDisburse : DialogState

        @Parcelize
        data class Success(val loanWithAssociations: LoanWithAssociations) : DialogState
    }
}

sealed interface LoanAccountsEvent {
    data object NavigateBack : LoanAccountsEvent
    data class ViewGuarantor(val loanId: Long) : LoanAccountsEvent
    data class UpdateLoan(val loanId: Long) : LoanAccountsEvent
    data class WithDrawLoan(val loanId: Long) : LoanAccountsEvent
    data class ViewLoanSummary(val loanId: Long) : LoanAccountsEvent
    data object ViewCharges : LoanAccountsEvent
    data class ViewRepaymentSchedule(val loanId: Long) : LoanAccountsEvent
    data class ViewTransactions(val loanId: Long) : LoanAccountsEvent
    data class ViewQr(val loanId: Long) : LoanAccountsEvent
    data class MakePayment(val loanId: Long, val outStanding: Double, val transferTo: String) : LoanAccountsEvent
    data class ShowToast(val message: String) : LoanAccountsEvent
}

sealed interface LoanAccountAction {
    data class LoanIdChanged(val loanId: Long) : LoanAccountAction
    data object BackPress : LoanAccountAction
    data object ViewGuarantorClicked : LoanAccountAction
    data object UpdateLoanClicked : LoanAccountAction
    data object WithDrawLoanClicked : LoanAccountAction
    data object ViewLoanSummaryClicked : LoanAccountAction
    data object ViewCharges : LoanAccountAction
    data object ViewRepaymentScheduleClicked : LoanAccountAction
    data object ViewTransactionsClicked : LoanAccountAction
    data object ViewQRClicked : LoanAccountAction
    data object MakePaymentClicked : LoanAccountAction
    data object RetryConnectionClicked : LoanAccountAction
    sealed class Internal : LoanAccountAction {
        data class ReceiveLoanAccountsResult(
            val loanAccountsResult: DataState<LoanWithAssociations?>,
        ) : Internal()
    }
}

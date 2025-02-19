/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanReview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOANS_PAYLOAD
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class ReviewLoanApplicationViewModel(
    private val reviewLoanApplicationRepository: ReviewLoanApplicationRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ReviewLoanApplicationState, ReviewLoanApplicationEvent, ReviewLoanApplicationAction>(
    initialState = ReviewLoanApplicationState(dialogState = null),
) {

    private val loanId = savedStateHandle.getStateFlow<Long?>(Constants.LOAN_ID, null)
    private val loanState = savedStateHandle.getStateFlow(Constants.LOAN_STATE, LoanState.CREATE)
    private val loanName = savedStateHandle.getStateFlow<String?>(Constants.LOAN_NAME, null)
    private val accountNo = savedStateHandle.getStateFlow<String?>(Constants.ACCOUNT_NUMBER, null)
    private val loansPayloadString = savedStateHandle.getStateFlow<String?>(LOANS_PAYLOAD, null)

    private val loansPayload: StateFlow<LoansPayload?> = loansPayloadString.map { jsonString ->
        jsonString?.let { Json.decodeFromString<LoansPayload>(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        observeNetworkStatus()
        collectReviewLoanApplicationUiData()
    }

    private fun updateState(update: (ReviewLoanApplicationState) -> ReviewLoanApplicationState) {
        mutableStateFlow.update(update)
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { connected ->
                updateState { it.copy(isOnline = connected) }
            }
        }
    }

    private fun collectReviewLoanApplicationUiData() {
        viewModelScope.launch {
            combine(
                loanId,
                loanState,
                loanName,
                accountNo,
                loansPayload) {
                              loanId,
                              loanState,
                              loanName,
                              accountNo,
                              loansPayload ->
                ReviewLoanApplicationUiData(
                    loanState = loanState,
                    loanName = loanName,
                    accountNo = accountNo,
                    loanProduct = loansPayload?.productName,
                    loanPurpose = loansPayload?.loanPurpose,
                    principal = loansPayload?.principal,
                    currency = loansPayload?.currency,
                    submissionDate = loansPayload?.submittedOnDate,
                    disbursementDate = loansPayload?.expectedDisbursementDate,
                    loanId = loanId ?: 0,
                )
            }.collectLatest { data ->
                updateState { it.copy(reviewLoanApplicationUiData = data) }
            }
        }
    }

    override fun handleAction(action: ReviewLoanApplicationAction) {
        when (action) {
            is ReviewLoanApplicationAction.SubmitLoan -> submitLoan()
            is ReviewLoanApplicationAction.NavigateBack ->
                sendEvent(ReviewLoanApplicationEvent.NavigateBack(action.isSuccess))
        }
    }

    private fun submitLoan() {
        viewModelScope.launch {
            updateState { it.copy(dialogState = ReviewLoanApplicationState.DialogState.Loading) }
            try {
                val result = reviewLoanApplicationRepository.submitLoan(
                    loanState = state.reviewLoanApplicationUiData.loanState,
                    loansPayload = loansPayload.value ?: LoansPayload(),
                    loanId = state.reviewLoanApplicationUiData.loanId,
                )
                when (result) {
                    DataState.Loading -> updateState {
                        it.copy(
                            dialogState =
                            ReviewLoanApplicationState.DialogState.Loading,
                        )
                    }
                    is DataState.Success -> {
                        sendEvent(
                            ReviewLoanApplicationEvent.ShowToast(result.data),
                        )
                        sendEvent(ReviewLoanApplicationEvent.NavigateBack(true))
                    }
                    is DataState.Error -> {
                        updateState {
                            it.copy(
                                dialogState = ReviewLoanApplicationState
                                    .DialogState.Error(result.message),
                            )
                        }
                    }
                }
            } catch (error: Exception) {
                updateState {
                    it.copy(
                        dialogState = ReviewLoanApplicationState
                            .DialogState.Error(error.message ?: "An error occurred"),
                    )
                }
                updateState { it.copy(dialogState = null) }
            }
        }
    }
}

@Parcelize
data class ReviewLoanApplicationState(
    val isOnline: Boolean = false,
    val dialogState: DialogState?,
    val reviewLoanApplicationUiData: ReviewLoanApplicationUiData = ReviewLoanApplicationUiData(),
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface ReviewLoanApplicationAction {
    data object SubmitLoan : ReviewLoanApplicationAction
    data class NavigateBack(val isSuccess: Boolean) : ReviewLoanApplicationAction
}

sealed interface ReviewLoanApplicationEvent {
    data class NavigateBack(val isSuccess: Boolean) : ReviewLoanApplicationEvent
    data class ShowToast(val message: String) : ReviewLoanApplicationEvent
}

data class ReviewLoanApplicationUiData(
    val loanId: Long = 0,
    val loanState: LoanState = LoanState.CREATE,
    val accountNo: String? = null,
    val loanName: String? = null,
    val disbursementDate: String? = null,
    val submissionDate: String? = null,
    val currency: String? = null,
    val principal: Double? = null,
    val loanPurpose: String? = null,
    val loanProduct: String? = null,
)

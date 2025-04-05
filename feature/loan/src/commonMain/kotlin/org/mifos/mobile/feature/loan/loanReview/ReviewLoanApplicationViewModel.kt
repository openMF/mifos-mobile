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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.loan.navigation.LoanReviewArgs
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_REVIEW_ARGS

internal class ReviewLoanApplicationViewModel(
    private val reviewLoanApplicationRepository: ReviewLoanApplicationRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ReviewLoanApplicationState, ReviewLoanApplicationEvent, ReviewLoanApplicationAction>(
    initialState = ReviewLoanApplicationState(
        dialogState = null,
    ),
) {

    private val loanReviewArgsJson = savedStateHandle.getStateFlow<String?>(LOAN_REVIEW_ARGS, null)

    private val loanReviewArgs: StateFlow<LoanReviewArgs?> = loanReviewArgsJson.map { jsonString ->
        jsonString?.let { Json.decodeFromString<LoanReviewArgs>(it) }
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
            loanReviewArgs.collectLatest { args ->
                args?.loanApplicationArgs?.let { loanAppArgs ->
                    val loanDetails = loanAppArgs.loanDetails
                    val loansPayload = loanAppArgs.loansPayload

                    updateState {
                        it.copy(
                            reviewLoanApplicationUiData = ReviewLoanApplicationUiData(
                                loanState = loanDetails.loanState,
                                loanName = loanDetails.loanName,
                                accountNo = loanDetails.accountNo,
                                loanProduct = loanDetails.loanProduct,
                                loanPurpose = loanDetails.loanPurpose,
                                principal = loansPayload.principal,
                                currency = loanDetails.currency,
                                submissionDate = loansPayload.submittedOnDate,
                                disbursementDate = loansPayload.expectedDisbursementDate,
                                loanId = loanDetails.loanId ?: 0,
                            ),
                        )
                    }
                }
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
                    loansPayload = loanReviewArgs.value?.loanApplicationArgs?.loansPayload
                        ?: LoansPayload(),
                    loanId = state.reviewLoanApplicationUiData.loanId,
                )
                when (result) {
                    DataState.Loading -> showLoadingDialog()
                    is DataState.Success -> {
                        sendEvent(
                            ReviewLoanApplicationEvent.ShowToast(result.data),
                        )
                        delay(1500)
                        sendEvent(ReviewLoanApplicationEvent.NavigateBack(true))
                    }
                    is DataState.Error -> {
                        sendEvent(
                            ReviewLoanApplicationEvent.ShowToast(result.message),
                        )
                        clearDialog()
                    }
                }
            } catch (error: Exception) {
                showErrorDialog(error.message ?: "An error occurred")
                clearDialog()
            }
        }
    }

    private fun showLoadingDialog() = updateState {
        it.copy(dialogState = ReviewLoanApplicationState.DialogState.Loading)
    }

    private fun showErrorDialog(message: String) = updateState {
        it.copy(dialogState = ReviewLoanApplicationState.DialogState.Error(message))
    }

    private fun clearDialog() = updateState {
        it.copy(dialogState = null)
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

@Parcelize
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
) : Parcelable

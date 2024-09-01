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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOANS_PAYLOAD
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.loanReview.ReviewLoanApplicationUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class ReviewLoanApplicationViewModel @Inject constructor(
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mUiState = MutableStateFlow<ReviewLoanApplicationUiState>(Loading)
    val uiState: StateFlow<ReviewLoanApplicationUiState> = mUiState.asStateFlow()

    private val loanId =
        savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)
    private val loanState =
        savedStateHandle.getStateFlow(key = Constants.LOAN_STATE, initialValue = LoanState.CREATE)
    private val loanName =
        savedStateHandle.getStateFlow<String?>(key = Constants.LOAN_NAME, initialValue = null)
    private val accountNo =
        savedStateHandle.getStateFlow<String?>(key = Constants.ACCOUNT_NUMBER, initialValue = null)
    private val loansPayloadString =
        savedStateHandle.getStateFlow<String?>(key = LOANS_PAYLOAD, initialValue = null)

    private val loansPayload: StateFlow<LoansPayload?> = loansPayloadString
        .map { jsonString ->
            jsonString?.let {
                Gson().fromJson(it, LoansPayload::class.java)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    val reviewLoanApplicationUiData: StateFlow<ReviewLoanApplicationUiData> = combine(
        loanId,
        loanState,
        loanName,
        accountNo,
        loansPayload,
    ) { loanId, loanState, loanName, accountNo, loansPayload ->
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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ReviewLoanApplicationUiData(),
    )

    fun submitLoan() = viewModelScope.launch(Dispatchers.IO) {
        mUiState.value = Loading
        reviewLoanApplicationRepositoryImpl.submitLoan(
            loanState = reviewLoanApplicationUiData.value.loanState,
            loansPayload = loansPayload.value ?: LoansPayload(),
            loanId = reviewLoanApplicationUiData.value.loanId,
        ).catch {
            mUiState.value = ReviewLoanApplicationUiState.Error(it.message)
        }.collect {
            mUiState.value =
                ReviewLoanApplicationUiState.Success(reviewLoanApplicationUiData.value.loanState)
        }
    }
}

internal sealed class ReviewLoanApplicationUiState {
    data object ReviewLoanUiReady : ReviewLoanApplicationUiState()
    data object Loading : ReviewLoanApplicationUiState()
    data class Error(val throwable: String?) : ReviewLoanApplicationUiState()
    data class Success(val loanState: LoanState) : ReviewLoanApplicationUiState()
}

internal class ReviewLoanApplicationUiData(
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

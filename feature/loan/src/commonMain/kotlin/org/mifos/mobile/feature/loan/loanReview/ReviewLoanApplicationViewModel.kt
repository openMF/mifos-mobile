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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOANS_PAYLOAD
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.loanReview.ReviewLoanApplicationUiState.Loading

internal class ReviewLoanApplicationViewModel(
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mUiState = MutableStateFlow<ReviewLoanApplicationUiState>(Loading)
    val uiState: StateFlow<ReviewLoanApplicationUiState> = mUiState.asStateFlow()

    private val isOnline = MutableStateFlow(false)
    val onlineStatus: StateFlow<Boolean> = isOnline.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { connected ->
                isOnline.value = connected
                mUiState.value = ReviewLoanApplicationUiState.IsOnline(connected)
            }
        }
    }

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
            jsonString?.let { Json.decodeFromString<LoansPayload>(it) }
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

    fun submitLoan() = viewModelScope.launch {
        try {
            mUiState.value = Loading

            val result = reviewLoanApplicationRepositoryImpl.submitLoan(
                loanState = reviewLoanApplicationUiData.value.loanState,
                loansPayload = loansPayload.value ?: LoansPayload(),
                loanId = reviewLoanApplicationUiData.value.loanId,
            )

            when (result) {
                DataState.Loading -> mUiState.value = Loading

                is DataState.Success -> {
                    mUiState.value =
                        ReviewLoanApplicationUiState.Success(reviewLoanApplicationUiData.value.loanState)
                }

                is DataState.Error -> {
                    mUiState.value = ReviewLoanApplicationUiState.Error(result.message)
                }
            }
        } catch (error: Exception) {
            mUiState.value = ReviewLoanApplicationUiState.Error(error.message ?: "An error occurred")
        }
    }
}

internal sealed class ReviewLoanApplicationUiState {
    data object ReviewLoanUiReady : ReviewLoanApplicationUiState()
    data object Loading : ReviewLoanApplicationUiState()
    data class IsOnline(val connected: Boolean) : ReviewLoanApplicationUiState()
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

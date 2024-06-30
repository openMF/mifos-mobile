package org.mifos.mobile.feature.loan.loan_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.ReviewLoanApplicationRepository
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import javax.inject.Inject

@HiltViewModel
class ReviewLoanApplicationViewModel @Inject constructor(
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository
) : ViewModel() {

    private val _reviewLoanApplicationUiState =
        MutableStateFlow<ReviewLoanApplicationUiState>(ReviewLoanApplicationUiState.Loading)
    val reviewLoanApplicationUiState: StateFlow<ReviewLoanApplicationUiState> = _reviewLoanApplicationUiState

    private var _reviewLoanApplicationUiData: MutableStateFlow<ReviewLoanApplicationUiData> = MutableStateFlow(ReviewLoanApplicationUiData())
    val reviewLoanApplicationUiData get() = _reviewLoanApplicationUiData

    private lateinit var loansPayload: LoansPayload

    fun insertData(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanName: String?,
        accountNo: String?,
        loanId: Long? = null,
    ) {
        this.loansPayload = loansPayload
        _reviewLoanApplicationUiData.value = ReviewLoanApplicationUiData(
            loanState = loanState,
            loanName = loanName,
            accountNo = accountNo,
            loanProduct = loansPayload.productName,
            loanPurpose = loansPayload.loanPurpose,
            principal = loansPayload.principal,
            currency = loansPayload.currency,
            submissionDate = loansPayload.submittedOnDate,
            disbursementDate = loansPayload.expectedDisbursementDate,
            loanId = loanId ?: 0
        )
        _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.ReviewLoanUiReady
    }


    fun submitLoan() = viewModelScope.launch(Dispatchers.IO) {
        _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Loading
        reviewLoanApplicationRepositoryImpl.submitLoan(
            loanState = reviewLoanApplicationUiData.value.loanState,
            loansPayload = loansPayload,
            loanId = reviewLoanApplicationUiData.value.loanId
        ).catch {
            _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Error(it)
        }.collect {
            _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Success(reviewLoanApplicationUiData.value.loanState)
        }
    }

}

sealed class ReviewLoanApplicationUiState {
    data object ReviewLoanUiReady : ReviewLoanApplicationUiState()
    data object Loading : ReviewLoanApplicationUiState()
    data class Error(val throwable: Throwable) : ReviewLoanApplicationUiState()
    data class Success(val loanState: LoanState) : ReviewLoanApplicationUiState()
}

class ReviewLoanApplicationUiData(
    val loanId: Long = 0,
    val loanState: LoanState = LoanState.CREATE,
    val accountNo: String? = null,
    val loanName: String? = null,
    val disbursementDate: String? = null,
    val submissionDate: String? = null,
    val currency: String? = null,
    val principal: Double? = null,
    val loanPurpose: String? = null,
    val loanProduct: String? = null
)

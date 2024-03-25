package org.mifos.mobile.ui.loan_review

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.repositories.ReviewLoanApplicationRepository
import org.mifos.mobile.ui.enums.LoanState
import javax.inject.Inject

@HiltViewModel
class ReviewLoanApplicationViewModel @Inject constructor(
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository
) :
    ViewModel() {

    private val _reviewLoanApplicationUiState =
        mutableStateOf<ReviewLoanApplicationUiState>(ReviewLoanApplicationUiState.ShowContent(ReviewLoanApplicationUiData()))
    val reviewLoanApplicationUiState: State<ReviewLoanApplicationUiState> = _reviewLoanApplicationUiState

    private lateinit var loansPayload: LoansPayload
    private var reviewLoanApplicationUiData: ReviewLoanApplicationUiData =
        ReviewLoanApplicationUiData()

    fun insertData(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanName: String?,
        accountNo: String?,
        loanId: Long? = null,
    ) {
        this.loansPayload = loansPayload
        reviewLoanApplicationUiData = ReviewLoanApplicationUiData(
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
        _reviewLoanApplicationUiState.value =
            ReviewLoanApplicationUiState.ShowContent(reviewLoanApplicationUiData = reviewLoanApplicationUiData)
    }


    fun submitLoan() = viewModelScope.launch(Dispatchers.IO) {
        _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Loading
        reviewLoanApplicationRepositoryImpl.submitLoan(
            loanState = reviewLoanApplicationUiData.loanState,
            loansPayload = loansPayload,
            loanId = reviewLoanApplicationUiData.loanId
        ).catch {
            _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Error(it)
        }.collect {
            _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Success(reviewLoanApplicationUiData.loanState)
        }
    }

}

sealed class ReviewLoanApplicationUiState {
    data class ShowContent(val reviewLoanApplicationUiData: ReviewLoanApplicationUiData) : ReviewLoanApplicationUiState()
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

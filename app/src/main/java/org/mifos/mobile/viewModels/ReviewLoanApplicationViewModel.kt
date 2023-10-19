package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.repositories.ReviewLoanApplicationRepository
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.ReviewLoanApplicationUiState
import javax.inject.Inject

@HiltViewModel
class ReviewLoanApplicationViewModel @Inject constructor(
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository
) :
    ViewModel() {

    private lateinit var loansPayload: LoansPayload
    private lateinit var loanState: LoanState
    private lateinit var loanName: String
    private lateinit var accountNo: String
    private var loanId: Long = 0

    fun insertData(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanName: String,
        accountNo: String
    ) {
        this.loanState = loanState
        this.loansPayload = loansPayload
        this.loanName = loanName
        this.accountNo = accountNo
    }

    fun insertData(
        loanState: LoanState,
        loanId: Long,
        loansPayload: LoansPayload,
        loanName: String,
        accountNo: String
    ) {
        this.loanState = loanState
        this.loanId = loanId
        this.loansPayload = loansPayload
        this.loanName = loanName
        this.accountNo = accountNo
    }

    fun getLoanProduct() = loansPayload.productName

    fun getLoanPurpose() = loansPayload.loanPurpose

    fun getPrincipal() = loansPayload.principal

    fun getCurrency() = loansPayload.currency

    fun getSubmissionDate() = loansPayload.submittedOnDate

    fun getDisbursementDate() = loansPayload.expectedDisbursementDate

    fun getLoanName() = loanName

    fun getAccountNo() = accountNo

    fun getLoanState() = loanState

    private val _reviewLoanApplicationUiState =
        MutableStateFlow<ReviewLoanApplicationUiState>(ReviewLoanApplicationUiState.Initial)
    val reviewLoanApplicationUiState: StateFlow<ReviewLoanApplicationUiState> =
        _reviewLoanApplicationUiState

    fun submitLoan() = viewModelScope.launch(Dispatchers.IO) {
        loansPayload.productName = null
        loansPayload.loanPurpose = null
        loansPayload.currency = null

        _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Loading
        reviewLoanApplicationRepositoryImpl.submitLoan(loanState, loansPayload, loanId).catch {
            _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Error(it)
        }.collect {
            _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.ReviewSuccess(it)
        }
    }

}

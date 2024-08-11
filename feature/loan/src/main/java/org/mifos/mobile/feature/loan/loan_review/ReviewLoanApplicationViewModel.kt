package org.mifos.mobile.feature.loan.loan_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOANS_PAYLOAD
import org.mifos.mobile.core.data.repositories.ReviewLoanApplicationRepository
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.model.enums.TransferType
import javax.inject.Inject

@HiltViewModel
class ReviewLoanApplicationViewModel @Inject constructor(
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _reviewLoanApplicationUiState = MutableStateFlow<ReviewLoanApplicationUiState>(ReviewLoanApplicationUiState.Loading)
    val reviewLoanApplicationUiState: StateFlow<ReviewLoanApplicationUiState> = _reviewLoanApplicationUiState

    private val loanId = savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)
    private val loanState = savedStateHandle.getStateFlow(key = Constants.LOAN_STATE, initialValue = LoanState.CREATE)
    private val loanName = savedStateHandle.getStateFlow<String?>(key = Constants.LOAN_NAME, initialValue = null)
    private val accountNo = savedStateHandle.getStateFlow<String?>(key = Constants.ACCOUNT_NUMBER, initialValue = null)
    private val loansPayloadString = savedStateHandle.getStateFlow<String?>(key = LOANS_PAYLOAD, initialValue = null)

    private val loansPayload: StateFlow<LoansPayload?> = loansPayloadString
        .map { jsonString ->
            jsonString?.let {
                Gson().fromJson(it, LoansPayload::class.java)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val reviewLoanApplicationUiData: StateFlow<ReviewLoanApplicationUiData> = combine(
        loanId,
        loanState,
        loanName,
        accountNo,
        loansPayload
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
            loanId = loanId ?: 0
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ReviewLoanApplicationUiData()
    )

    fun submitLoan() = viewModelScope.launch(Dispatchers.IO) {
        _reviewLoanApplicationUiState.value = ReviewLoanApplicationUiState.Loading
        reviewLoanApplicationRepositoryImpl.submitLoan(
            loanState = reviewLoanApplicationUiData.value.loanState,
            loansPayload = loansPayload.value ?: LoansPayload(),
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

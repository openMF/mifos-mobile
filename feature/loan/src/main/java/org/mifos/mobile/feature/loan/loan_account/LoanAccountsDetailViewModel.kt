package org.mifos.mobile.feature.loan.loan_account

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import javax.inject.Inject

@HiltViewModel
class LoanAccountsDetailViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState =
        mutableStateOf<LoanAccountDetailUiState>(LoanAccountDetailUiState.Loading)
    val loanUiState: State<LoanAccountDetailUiState> get() = _loanUiState

    private var _loanId: Long? = 0
    val loanId: Long? get() = _loanId

    private var _loanWithAssociations: LoanWithAssociations? = null
    val loanWithAssociations get() = _loanWithAssociations

    fun loadLoanAccountDetails(loanId: Long?) {
        viewModelScope.launch {
            _loanUiState.value = LoanAccountDetailUiState.Loading
            loanRepositoryImp.getLoanWithAssociations(org.mifos.mobile.core.common.Constants.REPAYMENT_SCHEDULE, loanId)
                ?.catch { _loanUiState.value = LoanAccountDetailUiState.Error }
                ?.collect { processLoanDetailsResponse(it) }
        }
    }

    private fun processLoanDetailsResponse(loanWithAssociations: LoanWithAssociations?) {
        _loanWithAssociations = loanWithAssociations
        val uiState = when {
            loanWithAssociations == null -> LoanAccountDetailUiState.Error
            loanWithAssociations.status?.active == true -> LoanAccountDetailUiState.Success(loanWithAssociations)
            loanWithAssociations.status?.pendingApproval == true -> LoanAccountDetailUiState.ApprovalPending
            loanWithAssociations.status?.waitingForDisbursal == true -> LoanAccountDetailUiState.WaitingForDisburse
            else -> LoanAccountDetailUiState.Success(loanWithAssociations)
        }
        _loanUiState.value = uiState
    }

    fun setLoanId(id: Long?) {
        _loanId = id
    }
}

sealed class LoanAccountDetailUiState {
    data object Loading : LoanAccountDetailUiState()
    data object Error : LoanAccountDetailUiState()
    data object ApprovalPending : LoanAccountDetailUiState()
    data object WaitingForDisburse : LoanAccountDetailUiState()
    data class Success(val loanWithAssociations: LoanWithAssociations) : LoanAccountDetailUiState()
}

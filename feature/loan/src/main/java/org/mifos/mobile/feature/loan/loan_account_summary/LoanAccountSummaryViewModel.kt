package org.mifos.mobile.feature.loan.loan_account_summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repositories.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.feature.loan.loan_account.LoanAccountDetailUiState
import javax.inject.Inject

@HiltViewModel
class LoanAccountSummaryViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loanUiState = MutableStateFlow<LoanAccountSummaryUiState>(LoanAccountSummaryUiState.Loading)
    val loanUiState: StateFlow<LoanAccountSummaryUiState> get() = _loanUiState

    val loanId = savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)

    fun loadLoanAccountDetails() {
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, loanId.value)
                .catch { _loanUiState.value = LoanAccountSummaryUiState.Error }
                .collect { _loanUiState.value = LoanAccountSummaryUiState.Success(it) }
        }
    }
}

sealed class LoanAccountSummaryUiState {
    data object Loading : LoanAccountSummaryUiState()
    data object Error : LoanAccountSummaryUiState()
    data class Success(var loanWithAssociations: LoanWithAssociations?): LoanAccountSummaryUiState()
}

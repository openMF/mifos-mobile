package org.mifos.mobile.feature.loan.loan_repayment_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repositories.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.feature.loan.R
import javax.inject.Inject

@HiltViewModel
class LoanRepaymentScheduleViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val loanUiState: StateFlow<LoanUiState> get() = _loanUiState
    private val _loanUiState = MutableStateFlow<LoanUiState>(LoanUiState.Loading)

    val loanId = savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)

    fun loanLoanWithAssociations() {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                loanId.value,
            ).catch {
                _loanUiState.value =
                    LoanUiState.ShowError(R.string.repayment_schedule)
            }.collect {
                if (it?.repaymentSchedule?.periods?.isNotEmpty() == true) {
                    _loanUiState.value = LoanUiState.ShowLoan(it)
                } else {
                    _loanUiState.value = it?.let { it1 -> LoanUiState.ShowEmpty(it1) }!!
                }
            }
        }
    }

}

sealed class LoanUiState {
    data object Loading : LoanUiState()
    data class ShowError(val message: Int) : LoanUiState()
    data class ShowLoan(val loanWithAssociations: LoanWithAssociations) : LoanUiState()
    data class ShowEmpty(val loanWithAssociations: LoanWithAssociations) : LoanUiState()
}
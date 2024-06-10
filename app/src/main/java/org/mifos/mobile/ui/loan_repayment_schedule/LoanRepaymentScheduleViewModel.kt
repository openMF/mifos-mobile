package org.mifos.mobile.ui.loan_repayment_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanRepaymentScheduleViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState = MutableStateFlow<LoanUiState>(LoanUiState.Loading)
    val loanUiState: StateFlow<LoanUiState> get() = _loanUiState

    fun loanLoanWithAssociations(loanId: Long?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                loanId,
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
package org.mifos.mobile.viewModels

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
class LoanAccountTransactionViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState = MutableStateFlow<LoanUiState>(LoanUiState.Loading)
    val loanUiState: StateFlow<LoanUiState> = _loanUiState

    fun loadLoanAccountDetails(loanId: Long?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                loanId,
            )?.catch {
                _loanUiState.value =
                    LoanUiState.ShowError(R.string.loan_account_details)
            }?.collect {
                if (it?.transactions != null &&
                    it.transactions?.isNotEmpty() == true
                ) {
                    _loanUiState.value = it.let { LoanUiState.ShowLoan(it) }
                } else {
                    _loanUiState.value = LoanUiState.ShowEmpty(it!!)
                }
            }
        }
    }

}
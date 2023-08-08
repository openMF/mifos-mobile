package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanAccountWithdrawViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {


    private val _loanUiState = MutableLiveData<LoanUiState>()
    val loanUiState: LiveData<LoanUiState> get() = _loanUiState

    fun withdrawLoanAccount(loanId: Long?, loanWithdraw: LoanWithdraw?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            val response = loanRepositoryImp.withdrawLoanAccount(loanId, loanWithdraw)
            if (response?.isSuccessful == true) {
                _loanUiState.value = LoanUiState.WithdrawSuccess
            } else {
                _loanUiState.value = LoanUiState.ShowError(R.string.error_loan_account_withdraw)
            }
        }
    }

}
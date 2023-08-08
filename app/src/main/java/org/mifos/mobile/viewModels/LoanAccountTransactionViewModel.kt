package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanAccountTransactionViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState = MutableLiveData<LoanUiState>()
    val loanUiState: LiveData<LoanUiState> get() = _loanUiState

    fun loadLoanAccountDetails(loanId: Long?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            try {
                val response = loanRepositoryImp.getLoanWithAssociations(
                    Constants.TRANSACTIONS,
                    loanId,
                )
                if (response?.isSuccessful == true) {
                    if (response.body()?.transactions != null &&
                        response.body()!!.transactions?.isNotEmpty() == true
                    ) {
                        _loanUiState.value = response.body()?.let { LoanUiState.ShowLoan(it) }
                    } else {
                        _loanUiState.value = response.body()?.let { LoanUiState.ShowEmpty(it) }
                    }
                } else {
                    _loanUiState.value =
                        LoanUiState.ShowError(R.string.loan_account_details)
                }
            } catch (e: Throwable) {
                _loanUiState.value =
                    LoanUiState.ShowError(R.string.loan_account_details)
            }

        }
    }

}
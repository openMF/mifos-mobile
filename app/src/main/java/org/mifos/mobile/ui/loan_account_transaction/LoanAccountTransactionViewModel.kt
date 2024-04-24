package org.mifos.mobile.ui.loan_account_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.asResult
import org.mifos.mobile.utils.Result
import javax.inject.Inject

@HiltViewModel
class LoanAccountTransactionViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    var loanUiState: StateFlow<LoanAccountTransactionUiState> = MutableStateFlow(LoanAccountTransactionUiState.Loading)

    private var _loanId: Long? = 0
    private val loanId get() = _loanId

    fun loadLoanAccountDetails() {
        loanUiState = loanRepositoryImp.getLoanWithAssociations(Constants.TRANSACTIONS, loanId)
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> LoanAccountTransactionUiState.Success(result.data)
                    is Result.Loading -> LoanAccountTransactionUiState.Loading
                    is Result.Error -> LoanAccountTransactionUiState.Error
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LoanAccountTransactionUiState.Loading
            )
    }

    fun setLoanId(id: Long?) {
        _loanId = id
        loadLoanAccountDetails()
    }
}

sealed class LoanAccountTransactionUiState() {
    data object Loading : LoanAccountTransactionUiState()
    data object Error : LoanAccountTransactionUiState()
    data class Success(val loanWithAssociations: LoanWithAssociations?) : LoanAccountTransactionUiState()

}
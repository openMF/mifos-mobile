package org.mifos.mobile.ui.loan_account_withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanAccountWithdrawViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState = MutableStateFlow<LoanAccountWithdrawUiState>(LoanAccountWithdrawUiState.WithdrawUiReady)
    val loanUiState: StateFlow<LoanAccountWithdrawUiState> = _loanUiState

    val loanWithAssociations: StateFlow<LoanWithAssociations?> get() = _loanWithAssociations
    private var _loanWithAssociations: MutableStateFlow<LoanWithAssociations?> = MutableStateFlow(null)

    fun withdrawLoanAccount(loanReason: String?) {
        val loanWithdraw = LoanWithdraw().apply {
            note = loanReason
            withdrawnOnDate = DateHelper
                .getDateAsStringFromLong(System.currentTimeMillis())
        }

        viewModelScope.launch {
            _loanUiState.value = LoanAccountWithdrawUiState.Loading
            loanRepositoryImp.withdrawLoanAccount(loanWithAssociations.value?.id?.toLong(), loanWithdraw)
                ?.catch {
                    _loanUiState.value = LoanAccountWithdrawUiState.Error(R.string.error_loan_account_withdraw)
                }?.collect {
                    _loanUiState.value = LoanAccountWithdrawUiState.Success
                }
        }
    }

    fun setLoanContent(loanWithAssociations: LoanWithAssociations?) {
        _loanWithAssociations.value = loanWithAssociations
    }
}

sealed class LoanAccountWithdrawUiState {
    data object WithdrawUiReady: LoanAccountWithdrawUiState()
    data object Loading: LoanAccountWithdrawUiState()
    data object Success: LoanAccountWithdrawUiState()
    data class Error(val messageId: Int): LoanAccountWithdrawUiState()
}
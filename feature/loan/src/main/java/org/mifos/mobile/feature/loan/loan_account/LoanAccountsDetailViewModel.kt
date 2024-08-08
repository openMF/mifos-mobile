package org.mifos.mobile.feature.loan.loan_account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.data.repositories.LoanRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.qr.utils.QrCodeGenerator
import javax.inject.Inject

@HiltViewModel
class LoanAccountsDetailViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _loanUiState = MutableStateFlow<LoanAccountDetailUiState>(LoanAccountDetailUiState.Loading)
    val loanUiState: StateFlow<LoanAccountDetailUiState> get() = _loanUiState

    val loanId = savedStateHandle.getStateFlow<Long>(key = LOAN_ID, initialValue = -1L)

    private var _loanWithAssociations: LoanWithAssociations? = null
    val loanWithAssociations get() = _loanWithAssociations

    fun loadLoanAccountDetails() {
        viewModelScope.launch {
            _loanUiState.value = LoanAccountDetailUiState.Loading
            loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, loanId.value)
                .catch { _loanUiState.value = LoanAccountDetailUiState.Error }
                .collect { processLoanDetailsResponse(it) }
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

    fun getQrString(): String {
        return QrCodeGenerator.getAccountDetailsInString(
            loanWithAssociations?.accountNo,
            preferencesHelper.officeName,
            AccountType.LOAN,
        )
    }
}

sealed class LoanAccountDetailUiState {
    data object Loading : LoanAccountDetailUiState()
    data object Error : LoanAccountDetailUiState()
    data object ApprovalPending : LoanAccountDetailUiState()
    data object WaitingForDisburse : LoanAccountDetailUiState()
    data class Success(val loanWithAssociations: LoanWithAssociations) : LoanAccountDetailUiState()
}

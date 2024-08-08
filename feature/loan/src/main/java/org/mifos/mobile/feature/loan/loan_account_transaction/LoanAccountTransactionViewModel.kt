package org.mifos.mobile.feature.loan.loan_account_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repositories.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.core.network.Result
import javax.inject.Inject

@HiltViewModel
class LoanAccountTransactionViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val loanId = savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)

    var loanUiState = loanId
        .flatMapLatest {
            loanRepositoryImp.getLoanWithAssociations(Constants.TRANSACTIONS, it)
        }
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

sealed class LoanAccountTransactionUiState() {
    data object Loading : LoanAccountTransactionUiState()
    data object Error : LoanAccountTransactionUiState()
    data class Success(val loanWithAssociations: LoanWithAssociations?) : LoanAccountTransactionUiState()
}
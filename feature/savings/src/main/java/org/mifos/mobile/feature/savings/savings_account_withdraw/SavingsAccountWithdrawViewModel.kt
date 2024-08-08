package org.mifos.mobile.feature.savings.savings_account_withdraw

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repositories.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.feature.savings.savings_account.SavingsAccountDetailUiState
import javax.inject.Inject

@HiltViewModel
class SavingsAccountWithdrawViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val savingsAccountWithdrawUiState: StateFlow<SavingsAccountWithdrawUiState> get() = _savingsAccountWithdrawUiState
    private val _savingsAccountWithdrawUiState = MutableStateFlow<SavingsAccountWithdrawUiState>(
        SavingsAccountWithdrawUiState.WithdrawUiReady
    )

    val savingsId: StateFlow<Long> = savedStateHandle.getStateFlow<Long>(
        key = Constants.SAVINGS_ID,
        initialValue = -1L
    )

    val savingsWithAssociations = savingsId
        .flatMapLatest {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId.value, Constants.TRANSACTIONS,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun submitWithdrawSavingsAccount(remark: String) {
        val payload = SavingsAccountWithdrawPayload()
        payload.note = remark
        payload.withdrawnOnDate = getTodayFormatted()

        viewModelScope.launch {
            _savingsAccountWithdrawUiState.value = SavingsAccountWithdrawUiState.Loading
            savingsAccountRepositoryImp.submitWithdrawSavingsAccount(savingsWithAssociations.value?.accountNo, payload)
                .catch { e ->
                    _savingsAccountWithdrawUiState.value =
                        SavingsAccountWithdrawUiState.Error(e.message)
                }.collect {
                    _savingsAccountWithdrawUiState.value =
                        SavingsAccountWithdrawUiState.Success
                }
        }
    }
}

sealed class SavingsAccountWithdrawUiState {
    data object WithdrawUiReady: SavingsAccountWithdrawUiState()
    data object Loading: SavingsAccountWithdrawUiState()
    data object Success: SavingsAccountWithdrawUiState()
    data class Error(val message: String?): SavingsAccountWithdrawUiState()
}

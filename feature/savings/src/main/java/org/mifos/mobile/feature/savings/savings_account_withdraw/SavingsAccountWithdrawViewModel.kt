package org.mifos.mobile.feature.savings.savings_account_withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.common.utils.getTodayFormatted
import javax.inject.Inject

@HiltViewModel
class SavingsAccountWithdrawViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository
) : ViewModel() {
    val savingsAccountWithdrawUiState: StateFlow<SavingsAccountWithdrawUiState> get() = _savingsAccountWithdrawUiState
    private val _savingsAccountWithdrawUiState = MutableStateFlow<SavingsAccountWithdrawUiState>(
        SavingsAccountWithdrawUiState.WithdrawUiReady
    )

    val savingsWithAssociations: StateFlow<SavingsWithAssociations?> get() = _savingsWithAssociations
    private var _savingsWithAssociations: MutableStateFlow<SavingsWithAssociations?> = MutableStateFlow(null)

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

    fun setContent(savingsWithAssociations: SavingsWithAssociations) {
        _savingsWithAssociations.value = savingsWithAssociations
    }
}

sealed class SavingsAccountWithdrawUiState {
    data object WithdrawUiReady: SavingsAccountWithdrawUiState()
    data object Loading: SavingsAccountWithdrawUiState()
    data object Success: SavingsAccountWithdrawUiState()
    data class Error(val message: String?): SavingsAccountWithdrawUiState()
}

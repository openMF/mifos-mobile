package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingsAccountWithdrawViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingsAccountWithdrawUiState =
        MutableStateFlow<SavingsAccountUiState>(SavingsAccountUiState.Initial)
    val savingsAccountWithdrawUiState: StateFlow<SavingsAccountUiState> get() = _savingsAccountWithdrawUiState

    fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?,
    ) {
        viewModelScope.launch {
            _savingsAccountWithdrawUiState.value = SavingsAccountUiState.Loading
            savingsAccountRepositoryImp.submitWithdrawSavingsAccount(accountId, payload)
                .catch { e ->
                    _savingsAccountWithdrawUiState.value = SavingsAccountUiState.ErrorMessage(e)
                }.collect {
                _savingsAccountWithdrawUiState.value =
                    SavingsAccountUiState.SavingsAccountWithdrawSuccess
            }
        }
    }
}
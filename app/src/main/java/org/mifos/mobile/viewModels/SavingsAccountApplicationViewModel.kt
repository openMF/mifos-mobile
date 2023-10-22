package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingsAccountApplicationViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingsAccountApplicationUiState =
        MutableStateFlow<SavingsAccountUiState>(SavingsAccountUiState.Initial)
    val savingsAccountApplicationUiState: StateFlow<SavingsAccountUiState> get() = _savingsAccountApplicationUiState

    fun loadSavingsAccountApplicationTemplate(
        clientId: Long?,
        state: SavingsAccountState?,
    ) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(clientId).catch { e ->
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }.collect {
                if (state === SavingsAccountState.CREATE) {
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountUiState.ShowUserInterfaceSavingAccountApplication(it)

                } else {
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountUiState.ShowUserInterfaceSavingAccountUpdate(it)
                }
            }
        }
    }

    fun submitSavingsAccountApplication(payload: SavingsAccountApplicationPayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
            savingsAccountRepositoryImp.submitSavingAccountApplication(payload).catch { e ->
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }.onCompletion {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountUiState.SavingsAccountApplicationSuccess
            }.collect {
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.HideProgress
            }
        }
    }

    fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
            savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload).catch { e ->
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }.onCompletion {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountUiState.SavingsAccountUpdateSuccess
            }.collect {
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.HideProgress
            }
        }
    }
}
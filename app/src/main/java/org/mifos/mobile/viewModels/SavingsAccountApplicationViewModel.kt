package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.utils.SavingsAccountUiState
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SavingsAccountApplicationViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingsAccountApplicationUiState = MutableLiveData<SavingsAccountUiState>()
    val savingsAccountApplicationUiState: LiveData<SavingsAccountUiState> get() = _savingsAccountApplicationUiState

    fun loadSavingsAccountApplicationTemplate(
        clientId: Long?,
        state: SavingsAccountState?,
    ) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
            try {
                val response =
                    savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(clientId)
                if (response?.isSuccessful == true) {
                    if (state === SavingsAccountState.CREATE) {
                        _savingsAccountApplicationUiState.value = response.body()?.let {
                            SavingsAccountUiState.ShowUserInterfaceSavingAccountApplication(it)
                        }
                    } else {
                        _savingsAccountApplicationUiState.value = response.body()
                            ?.let { SavingsAccountUiState.ShowUserInterfaceSavingAccountUpdate(it) }
                    }
                }
            } catch (e: Throwable) {
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }
        }
    }

    fun submitSavingsAccountApplication(payload: SavingsAccountApplicationPayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
            try {
                val response = savingsAccountRepositoryImp.submitSavingAccountApplication(payload)
                if (response?.isSuccessful == true) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.HideProgress
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountUiState.SavingsAccountApplicationSuccess
                } else {
                    _savingsAccountApplicationUiState.value =
                        response?.let { HttpException(it) }?.let {
                            SavingsAccountUiState.ErrorMessage(
                                it
                            )
                        }
                }
            } catch (e: Throwable) {
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }
        }
    }

    fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
            try {
                val response = savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload)
                if (response?.isSuccessful == true) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.HideProgress
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountUiState.SavingsAccountUpdateSuccess
                } else {
                    _savingsAccountApplicationUiState.value =
                        response?.let { HttpException(it) }?.let {
                            SavingsAccountUiState.ErrorMessage(
                                it
                            )
                        }
                }
            } catch (e: Throwable) {
                _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }
        }
    }
}
package org.mifos.mobile.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.SavingsAccountUiState
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SavingsAccountWithdrawViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingsAccountWithdrawUiState = MutableLiveData<SavingsAccountUiState>()
    val savingsAccountWithdrawUiState: LiveData<SavingsAccountUiState> get() = _savingsAccountWithdrawUiState

    fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?,
    ) {
        viewModelScope.launch {
            _savingsAccountWithdrawUiState.value = SavingsAccountUiState.Loading
            try {
                val response =
                    savingsAccountRepositoryImp.submitWithdrawSavingsAccount(accountId, payload)
                if (response?.isSuccessful == true) {
                    _savingsAccountWithdrawUiState.value =
                        SavingsAccountUiState.SavingsAccountWithdrawSuccess
                } else {
                    _savingsAccountWithdrawUiState.value = response?.let { HttpException(it) }?.let {
                        SavingsAccountUiState.ErrorMessage(
                            it
                        )
                    }
                }
            } catch (e: Throwable) {
                _savingsAccountWithdrawUiState.value = SavingsAccountUiState.ErrorMessage(e)
            }
        }
    }
}
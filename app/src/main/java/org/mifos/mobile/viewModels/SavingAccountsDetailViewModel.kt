package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingAccountsDetailViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingAccountsDetailUiState = MutableLiveData<SavingsAccountUiState>()
    val savingAccountsDetailUiState: LiveData<SavingsAccountUiState> get() = _savingAccountsDetailUiState

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param accountId Id of Savings Account
     */
    fun loadSavingsWithAssociations(accountId: Long?) {
        viewModelScope.launch {
            _savingAccountsDetailUiState.value = SavingsAccountUiState.Loading
            try {
                val response = savingsAccountRepositoryImp.getSavingsWithAssociations(
                    accountId,
                    Constants.TRANSACTIONS,
                )
                if (response?.isSuccessful == true) {
                    _savingAccountsDetailUiState.value = response.body()
                        ?.let { SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(it) }
                }
            } catch (e: Throwable) {
                _savingAccountsDetailUiState.value = SavingsAccountUiState.Error
            }
        }
    }
}
package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingAccountsDetailViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingAccountsDetailUiState =
        MutableStateFlow<SavingsAccountUiState>(SavingsAccountUiState.Initial)
    val savingAccountsDetailUiState: StateFlow<SavingsAccountUiState> get() = _savingAccountsDetailUiState

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
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                accountId,
                Constants.TRANSACTIONS,
            ).catch {
                _savingAccountsDetailUiState.value = SavingsAccountUiState.Error
            }.collect {
                _savingAccountsDetailUiState.value =
                    SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(it)
            }
        }
    }
}
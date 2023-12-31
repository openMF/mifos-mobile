package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.ClientChargeRepository
import org.mifos.mobile.utils.ClientChargeUiState
import javax.inject.Inject

@HiltViewModel
class ClientChargeViewModel @Inject constructor(private val clientChargeRepositoryImp: ClientChargeRepository) :
    ViewModel() {

    private val _clientChargeUiState =
        MutableStateFlow<ClientChargeUiState>(ClientChargeUiState.Initial)

    val clientChargeUiState: StateFlow<ClientChargeUiState> get() = _clientChargeUiState

    fun loadClientCharges(clientId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.getClientCharges(clientId).catch {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.ShowClientCharges(it.pageItems)
            }
        }
    }

    fun loadLoanAccountCharges(loanId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.getLoanCharges(loanId).catch {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.ShowClientCharges(it)
            }
        }
    }

    fun loadSavingsAccountCharges(savingsId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.getSavingsCharges(savingsId).catch {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.ShowClientCharges(it)
            }
        }
    }

    fun loadClientLocalCharges() {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            clientChargeRepositoryImp.clientLocalCharges().catch {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }.collect {
                _clientChargeUiState.value = ClientChargeUiState.ShowClientCharges(it.pageItems)
            }
        }
    }
}
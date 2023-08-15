package org.mifos.mobile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.ClientChargeRepository
import org.mifos.mobile.utils.ClientChargeUiState
import javax.inject.Inject

@HiltViewModel
class ClientChargeViewModel @Inject constructor(private val clientChargeRepositoryImp: ClientChargeRepository) :
    ViewModel() {

    private val _clientChargeUiState = MutableLiveData<ClientChargeUiState>()
    val clientChargeUiState get() = _clientChargeUiState

    fun loadClientCharges(clientId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            try {
                val response = clientChargeRepositoryImp.getClientCharges(clientId)
                if (response?.isSuccessful == true) {
                    _clientChargeUiState.value =
                        response.body()?.pageItems?.let { ClientChargeUiState.ShowClientCharges(it) }
                } else {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }
            } catch (e: Throwable) {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }
        }
    }

    fun loadLoanAccountCharges(loanId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            try {
                val response = clientChargeRepositoryImp.getLoanCharges(loanId)
                if (response?.isSuccessful == true) {
                    _clientChargeUiState.value = response.body()
                        ?.let { ClientChargeUiState.ShowClientCharges(it) }
                } else {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }
            } catch (e: Throwable) {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }
        }
    }

    fun loadSavingsAccountCharges(savingsId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            try {
                val response = clientChargeRepositoryImp.getSavingsCharges(savingsId)
                if (response?.isSuccessful == true) {
                    _clientChargeUiState.value = response.body()
                        ?.let { ClientChargeUiState.ShowClientCharges(it) }
                } else {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }
            } catch (e: Throwable) {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }
        }
    }

    fun loadClientLocalCharges() {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeUiState.Loading
            try {
                val response = clientChargeRepositoryImp.clientLocalCharges()
                if (response.isSuccessful) {
                    _clientChargeUiState.value =
                        response.body()?.pageItems?.let { ClientChargeUiState.ShowClientCharges(it) }
                } else {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }
            } catch (e: Throwable) {
                _clientChargeUiState.value =
                    ClientChargeUiState.ShowError(R.string.client_charges)
            }
        }
    }
}
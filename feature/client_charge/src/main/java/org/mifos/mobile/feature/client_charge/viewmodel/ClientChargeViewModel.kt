package org.mifos.mobile.feature.client_charge.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repositories.ClientChargeRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.datastore.model.Charge
import org.mifos.mobile.core.model.entity.client.ClientType
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.client_charge.utils.ClientChargeState
import javax.inject.Inject

@HiltViewModel
class ClientChargeViewModel @Inject constructor(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    val preferencesHelper: PreferencesHelper,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _clientChargeUiState = MutableStateFlow<ClientChargeState>(ClientChargeState.Loading)
    val clientChargeUiState: StateFlow<ClientChargeState> get() = _clientChargeUiState

    private val clientId = preferencesHelper.clientId
    private val chargeTypeString = savedStateHandle.getStateFlow<String?>(key = Constants.CHARGE_TYPE, initialValue = null)

    init {
        loadCharges()
    }

    fun loadCharges() {
        clientId?.let { clientId ->
            val chargeType = chargeTypeString.value?.let { ChargeType.valueOf(it) }
            when (chargeType) {
                ChargeType.CLIENT -> loadClientCharges(clientId)
                ChargeType.SAVINGS -> loadSavingsAccountCharges(clientId)
                ChargeType.LOAN -> loadLoanAccountCharges(clientId)
                null -> Unit
            }
        }
    }

    private fun loadClientCharges(clientId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeState.Loading
            clientChargeRepositoryImp.getClientCharges(clientId).catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeState.Success(it.pageItems)
            }
        }
    }

    private fun loadLoanAccountCharges(loanId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeState.Loading
            clientChargeRepositoryImp.getLoanCharges(loanId).catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeState.Success(it)
            }
        }
    }

    private fun loadSavingsAccountCharges(savingsId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeState.Loading
            clientChargeRepositoryImp.getSavingsCharges(savingsId).catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeState.Success(it)
            }
        }
    }

    fun loadClientLocalCharges() {
        viewModelScope.launch {
            _clientChargeUiState.value = ClientChargeState.Loading
            clientChargeRepositoryImp.clientLocalCharges().catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value =
                    ClientChargeState.Success(it.pageItems.filterNotNull())
            }
        }
    }
}
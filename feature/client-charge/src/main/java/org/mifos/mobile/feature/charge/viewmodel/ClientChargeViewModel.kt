/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.viewmodel

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
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.charge.utils.ClientChargeState
import org.mifos.mobile.feature.charge.utils.ClientChargeState.Loading
import javax.inject.Inject

@HiltViewModel
internal class ClientChargeViewModel @Inject constructor(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    preferencesHelper: PreferencesHelper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _clientChargeUiState = MutableStateFlow<ClientChargeState>(Loading)
    val clientChargeUiState: StateFlow<ClientChargeState> get() = _clientChargeUiState

    private val clientId = preferencesHelper.clientId
    private val chargeTypeString = savedStateHandle.getStateFlow<String?>(
        key = Constants.CHARGE_TYPE,
        initialValue = null,
    )

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
            _clientChargeUiState.value = Loading
            clientChargeRepositoryImp.getClientCharges(clientId).catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                Log.e("selfServiceDatabase", it.toString())
                clientChargeRepositoryImp.syncCharges(it)
                _clientChargeUiState.value = ClientChargeState.Success(it.pageItems)
            }
        }
    }

    private fun loadLoanAccountCharges(loanId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = Loading
            clientChargeRepositoryImp.getLoanCharges(loanId).catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeState.Success(it)
            }
        }
    }

    private fun loadSavingsAccountCharges(savingsId: Long) {
        viewModelScope.launch {
            _clientChargeUiState.value = Loading
            clientChargeRepositoryImp.getSavingsCharges(savingsId).catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeState.Success(it)
            }
        }
    }

    fun loadClientLocalCharges() {
        viewModelScope.launch {
            _clientChargeUiState.value = Loading
            clientChargeRepositoryImp.clientLocalCharges().catch {
                _clientChargeUiState.value = ClientChargeState.Error(it.message)
            }.collect {
                _clientChargeUiState.value = ClientChargeState.Success(it.pageItems)
            }
        }
    }
}

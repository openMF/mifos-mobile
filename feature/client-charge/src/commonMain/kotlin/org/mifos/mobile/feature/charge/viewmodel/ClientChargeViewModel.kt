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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.charges
import mifos_mobile.feature.client_charge.generated.resources.client_charges
import mifos_mobile.feature.client_charge.generated.resources.internet_not_connected
import mifos_mobile.feature.client_charge.generated.resources.loan_charges
import mifos_mobile.feature.client_charge.generated.resources.savings_charges
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.ChargeCalculationType
import org.mifos.mobile.core.model.entity.ChargeTimeType
import org.mifos.mobile.core.model.entity.Currency
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.charge.navigation.ClientChargesRoute

internal class ClientChargeViewModel(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ClientChargeState, ClientChargeEvent, ClientChargeAction>(
    initialState = ClientChargeState(
        data = ClientChargeState.ChargesState.Loading,
        isOnline = false,
    ),
) {

    private val chargeType = toChargeType(savedStateHandle.toRoute<ClientChargesRoute>().chargeType)
    private val chargeTypeId = savedStateHandle.toRoute<ClientChargesRoute>().chargeTypeId
    private val clientId = userPreferencesRepositoryImpl.clientId.value
    init {
        updateTopBarTitle()
        viewModelScope.launch {
            val message = getString(Res.string.internet_not_connected)
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
                if (!isConnected) {
                    sendEvent(ClientChargeEvent.ShowToast(message))
                    updateState {
                        it.copy(
                            data = ClientChargeState.ChargesState.Error(message),
                        )
                    }
                }
            }
        }
        loadCharges()
    }

    private fun updateState(update: (ClientChargeState) -> ClientChargeState) {
        mutableStateFlow.update(update)
    }

    private fun updateTopBarTitle() {
        viewModelScope.launch {
            val topBarId = when (chargeType) {
                ChargeType.CLIENT -> Res.string.client_charges
                ChargeType.SAVINGS -> Res.string.savings_charges
                ChargeType.LOAN -> Res.string.loan_charges
            }
            updateState {
                it.copy(
                    topBarTitleResId = topBarId,
                )
            }
        }
    }

    override fun handleAction(action: ClientChargeAction) {
        when (action) {
            ClientChargeAction.RefreshCharges -> refreshCharges()
            ClientChargeAction.OnNavigate -> {
                sendEvent(ClientChargeEvent.Navigate)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadCharges() {
        viewModelScope.launch {
            when (chargeType) {
                ChargeType.CLIENT -> processClientCharges()
                ChargeType.LOAN, ChargeType.SAVINGS -> processLoanOrSavingsCharges()
            }
        }
    }

    private fun refreshCharges() {
        loadCharges()
    }

    private fun processClientCharges() {
        viewModelScope.launch {
            clientChargeRepositoryImp.getCharges(chargeTypeId).collect { result ->
                when (result) {
                    DataState.Loading -> {
                        mutableStateFlow.update {
                            it.copy(data= ClientChargeState.ChargesState.Loading)
                        }
                    }

                    is DataState.Error -> {
                        mutableStateFlow.update {
                            it.copy(
                                data = ClientChargeState
                                    .ChargesState
                                    .Error(result.exception.message ?: "An Error Occurred"),
                            )
                        }
                    }

                    is DataState.Success -> {
                        if(result.data.pageItems.isEmpty()){
                            mutableStateFlow.update {
                                it.copy(
                                    data = ClientChargeState.ChargesState.Empty
                                )
                            }
                        }else{
                            mutableStateFlow.update {
                                it.copy(
                                    charges = result.data.pageItems,
                                    data= null
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun processLoanOrSavingsCharges() {
        viewModelScope.launch {
            val type = chargeType
            val id = if (chargeTypeId == -1L) {
                clientId ?: -1
            } else {
                chargeTypeId
            }

            clientChargeRepositoryImp.getLoanOrSavingsCharges(type, id).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        mutableStateFlow.update {
                            it.copy(
                                data= ClientChargeState.ChargesState.Loading
                            )
                        }
                    }

                    is DataState.Error -> {
                        mutableStateFlow.update {
                            it.copy(
                                data = ClientChargeState
                                    .ChargesState
                                    .Error(result.exception.message ?: "An Error Occurred"),
                            )
                        }
                    }

                    is DataState.Success -> {
                        if(result.data.isEmpty()){
                            mutableStateFlow.update {
                                it.copy(
                                    data = ClientChargeState.ChargesState.Empty
                                )
                            }
                        }else{
                            mutableStateFlow.update {
                                it.copy(
                                    charges = result.data,
                                    data = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Parcelize
data class ClientChargeState(
    val isOnline: Boolean,
    @IgnoredOnParcel
    val topBarTitleResId: StringResource = Res.string.charges,
    val data: ChargesState?,
    @IgnoredOnParcel
    val charges:List<Charge> =emptyList()
) : Parcelable {
    sealed interface ChargesState : Parcelable {
        @Parcelize
        data class Error(val message: String) : ChargesState

        @Parcelize
        data object Loading : ChargesState

        @Parcelize
        data object Empty:ChargesState
    }
}

sealed interface ClientChargeEvent {
    data class ShowToast(val message: String) : ClientChargeEvent
    data object Navigate : ClientChargeEvent
}

sealed interface ClientChargeAction {
    data object RefreshCharges : ClientChargeAction
    data object OnNavigate : ClientChargeAction
}

fun toChargeType(value: String?): ChargeType {
    return try {
        value?.let { ChargeType.valueOf(it) } ?: ChargeType.CLIENT
    } catch (e: IllegalArgumentException) {
        ChargeType.CLIENT
    }
}

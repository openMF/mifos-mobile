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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.FileUtils.Companion.logger
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class ClientChargeViewModel(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ClientChargeState, ClientChargeEvent, ClientChargeAction>(
    initialState = ClientChargeState(
        chargeDialog = null,
        isOnline = false,
    ),
) {

    private val refreshTrigger = MutableStateFlow(false)

    private val chargeTypeString = savedStateHandle.getStateFlow<String?>(
        key = Constants.CHARGE_TYPE,
        initialValue = null,
    )

    private val clientId = userPreferencesRepositoryImpl.clientId.value

    private val chargeTypeId: StateFlow<Long?> = savedStateHandle.getStateFlow(
        key = Constants.CHARGE_TYPE_ID,
        initialValue = clientId,
    ).map { if (it == -1L) clientId else it }
        .stateIn(viewModelScope, SharingStarted.Lazily, clientId)

    private val chargeType: StateFlow<ChargeType> = chargeTypeString
        .map { it?.let { ChargeType.valueOf(it) } ?: ChargeType.CLIENT }
        .stateIn(viewModelScope, SharingStarted.Lazily, ChargeType.CLIENT)

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
                            chargeDialog = ClientChargeState.ChargeDialogState.Error(message),
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
            chargeType.collectLatest { type ->
                updateState {
                    it.copy(
                        topBarTitleResId = when (type) {
                            ChargeType.CLIENT -> Res.string.client_charges
                            ChargeType.SAVINGS -> Res.string.savings_charges
                            ChargeType.LOAN -> Res.string.loan_charges
                        },
                    )
                }
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
        updateState {
            it.copy(chargeDialog = ClientChargeState.ChargeDialogState.Loading)
        }
        viewModelScope.launch {
            combine(chargeType, chargeTypeId, refreshTrigger) { type, id, _ ->
                Pair(type, id ?: clientId ?: -1L)
            }.flatMapLatest { (type, id) ->
                when (type) {
                    ChargeType.CLIENT -> clientChargeRepositoryImp.getCharges(id)
                        .onEach { result ->
                            processClientCharges(result)
                        }

                    ChargeType.LOAN, ChargeType.SAVINGS -> clientChargeRepositoryImp.getLoanOrSavingsCharges(type, id)
                        .onEach { result ->
                            processLoanOrSavingsCharges(result)
                        }
                }
            }
                .catch { exception ->
                    updateState {
                        it.copy(
                            chargeDialog = ClientChargeState.ChargeDialogState.Error(
                                exception.message ?: "An error occurred",
                            ),
                        )
                    }
                }
                .collect { }
        }
    }

    private fun refreshCharges() {
        viewModelScope.launch {
            refreshTrigger.tryEmit(!refreshTrigger.value)
        }
    }

    private fun processClientCharges(result: DataState<Page<Charge>>) {
        updateState {
            when (result) {
                DataState.Loading -> it.copy(chargeDialog = ClientChargeState.ChargeDialogState.Loading)

                is DataState.Error -> it.copy(
                    chargeDialog = ClientChargeState.ChargeDialogState.Error(
                        result.exception.message ?: "An Error Occurred",
                    ),
                )

                is DataState.Success -> it.copy(
                    chargeDialog = null,
                    charges = result.data.pageItems,
                )
            }
        }
    }

    private fun processLoanOrSavingsCharges(result: DataState<List<Charge>>) {
        logger.d { "KtorClient getting in function ${result.data}" }
        updateState {
            when (result) {
                DataState.Loading -> it.copy(chargeDialog = ClientChargeState.ChargeDialogState.Loading)

                is DataState.Error -> it.copy(
                    chargeDialog = ClientChargeState.ChargeDialogState.Error(
                        result.exception.message ?: "An Error Occurred",
                    ),
                )

                is DataState.Success -> it.copy(
                    chargeDialog = null,
                    charges = result.data,
                )
            }
        }
    }
}

@Parcelize
data class ClientChargeState(
    val isOnline: Boolean,
    @IgnoredOnParcel
    val topBarTitleResId: StringResource = Res.string.charges,
    @IgnoredOnParcel
    val charges: List<Charge> = emptyList(),
    val chargeDialog: ChargeDialogState?,
) : Parcelable {
    sealed interface ChargeDialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : ChargeDialogState

        @Parcelize
        data object Loading : ChargeDialogState
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

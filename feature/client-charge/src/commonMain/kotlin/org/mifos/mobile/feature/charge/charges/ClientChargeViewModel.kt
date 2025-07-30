/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.charges

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class ClientChargeViewModel(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ClientChargeState, ClientChargeEvent, ClientChargeAction>(
    initialState = run {
        val chargeRoute = savedStateHandle.toRoute<ClientChargesRoute>()
        val topBarId = when (chargeRoute.chargeType) {
            ChargeType.CLIENT.type -> Res.string.client_charges
            ChargeType.SAVINGS.type -> Res.string.savings_charges
            ChargeType.LOAN.type -> Res.string.loan_charges
            else -> Res.string.client_charges
        }
        ClientChargeState(
            dialogState = ClientChargeState.DialogState.Loading,
            chargeType = ChargeType.valueOf(chargeRoute.chargeType),
            chargeTypeId = chargeRoute.chargeTypeId,
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            topBarTitleResId = topBarId,
            isOnline = false,
        )
    },
) {

//    private val chargeType = toChargeType(savedStateHandle.toRoute<ClientChargesRoute>().chargeType)
//    private val chargeTypeId = savedStateHandle.toRoute<ClientChargesRoute>().chargeTypeId
//    private val clientId = userPreferencesRepositoryImpl.clientId.value
    init {
//        updateTopBarTitle()
        viewModelScope.launch {
            val message = getString(Res.string.internet_not_connected)
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
                if (!isConnected) {
                    sendEvent(ClientChargeEvent.ShowToast(message))
                    updateState {
                        it.copy(
                            dialogState = ClientChargeState.DialogState.Error(message),
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

//    private fun updateTopBarTitle() {
//        viewModelScope.launch {
//            val topBarId = when (chargeType) {
//                ChargeType.CLIENT -> Res.string.client_charges
//                ChargeType.SAVINGS -> Res.string.savings_charges
//                ChargeType.LOAN -> Res.string.loan_charges
//            }
//            updateState {
//                it.copy(
//                    topBarTitleResId = topBarId,
//                )
//            }
//        }
//    }

    override fun handleAction(action: ClientChargeAction) {
        when (action) {
            is ClientChargeAction.RefreshCharges -> refreshCharges()

            is ClientChargeAction.OnNavigate -> {
                sendEvent(ClientChargeEvent.Navigate)
            }

            is ClientChargeAction.OnDismissDialog -> dismissDialog()

            is ClientChargeAction.Internal.ReceiveClientChargesResult ->
                handleClientChargesResult(action.result)

            is ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult ->
                handleLoanOrSavingsChargesResult(action.result)

            is ClientChargeAction.OnChargeClick ->
                sendEvent(ClientChargeEvent.OnChargeClick(action.charge))
        }
    }

    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    private fun handleLoanOrSavingsChargesResult(
        result: DataState<List<Charge>>,
    ) {
        when (result) {
            is DataState.Loading -> {
                updateState {
                    it.copy(dialogState = ClientChargeState.DialogState.Loading)
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(dialogState = ClientChargeState.DialogState.Error(
                        result.exception.message ?: "An Error Occurred"
                    ))
                }
            }

            is DataState.Success -> {
                if (result.data.isEmpty()) {
                    updateState {
                        it.copy(
                            isEmpty = true,
                            dialogState = null,
                        )
                    }
                } else {
                    updateState {
                        it.copy(
                            charges = result.data,
                            dialogState = null,
                        )
                    }
                }
            }
        }
    }
    private fun handleClientChargesResult(
        result: DataState<Page<Charge>>,
    ) {
        when (result) {
            DataState.Loading -> {
                updateState {
                    it.copy(dialogState = ClientChargeState.DialogState.Loading)
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        dialogState = ClientChargeState.DialogState.Error(
                            result.exception.message ?: "An Error Occurred"
                        ),
                    )
                }
            }

            is DataState.Success -> {
                if (result.data.pageItems.isEmpty()) {
                    updateState {
                        it.copy(
                            isEmpty = true,
                            dialogState = null,
                        )
                    }
                } else {
                    updateState {
                        it.copy(
                            charges = result.data.pageItems,
                            dialogState = null,
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadCharges() {
        viewModelScope.launch {
            when (state.chargeType) {
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
            clientChargeRepositoryImp.getCharges(state.clientId).collect { result ->
                sendAction(ClientChargeAction.Internal.ReceiveClientChargesResult(result))
            }
        }
    }

    private fun processLoanOrSavingsCharges() {
        viewModelScope.launch {
//            val type = chargeType
//            val id = if (chargeTypeId == -1L) {
//                clientId ?: -1
//            } else {
//                chargeTypeId
//            }

            clientChargeRepositoryImp.getLoanOrSavingsCharges(
                state.chargeType,
                state.chargeTypeId ?: -1L,
            ).collect {
                    result ->
                sendAction(ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult(result))
            }
        }
    }
}

data class ClientChargeState(
    val clientId: Long,
    val chargeType: ChargeType,
    val chargeTypeId: Long?,
    val isOnline: Boolean,
    val isEmpty: Boolean = false,
    val topBarTitleResId: StringResource = Res.string.charges,
    val dialogState: DialogState? = null,
    val charges: List<Charge> = emptyList(),
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState

        data object Loading : DialogState
    }
}

sealed interface ClientChargeEvent {
    data class ShowToast(val message: String) : ClientChargeEvent
    data object Navigate : ClientChargeEvent
    data class OnChargeClick(val charge: Charge) : ClientChargeEvent
}

sealed interface ClientChargeAction {
    data object RefreshCharges : ClientChargeAction
    data object OnNavigate : ClientChargeAction
    data object OnDismissDialog : ClientChargeAction
    data class OnChargeClick(val charge: Charge) : ClientChargeAction
    sealed class Internal : ClientChargeAction {
        data class ReceiveLoanOrSavingsChargesResult(
            val result: DataState<List<Charge>>,
        ) : Internal()
        data class ReceiveClientChargesResult(
            val result: DataState<Page<Charge>>,
        ) : Internal()
    }
}

// fun toChargeType(value: String?): ChargeType {
//    return try {
//        value?.let { ChargeType.valueOf(it) } ?: ChargeType.CLIENT
//    } catch (e: IllegalArgumentException) {
//        ChargeType.CLIENT
//    }
// }

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
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ClientChargeState, ClientChargeEvent, ClientChargeAction>(
    initialState = ClientChargeState(
        chargeDialog = null,
        isOnline = false,
    ),
) {

    private val chargeType = toChargeType(savedStateHandle.toRoute<ClientChargesRoute>().chargeType)
    private val chargeTypeId = savedStateHandle.toRoute<ClientChargesRoute>().chargeTypeId
    private val refreshTrigger = MutableStateFlow(false)
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
        updateState {
            it.copy(chargeDialog = ClientChargeState.ChargeDialogState.Loading)
        }

        viewModelScope.launch {
            refreshTrigger
                .flatMapLatest {
                    val type = chargeType
                    val id = chargeTypeId

                    when (type) {
                        ChargeType.CLIENT -> clientChargeRepositoryImp.getCharges(id)
                            .onEach { result -> processClientCharges(result) }

                        ChargeType.LOAN, ChargeType.SAVINGS -> clientChargeRepositoryImp.getLoanOrSavingsCharges(type, id)
                            .onEach { result -> processLoanOrSavingsCharges(result) }
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
                .collect {}
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

fun toChargeType(value: String?): ChargeType {
    return try {
        value?.let { ChargeType.valueOf(it) } ?: ChargeType.CLIENT
    } catch (e: IllegalArgumentException) {
        ChargeType.CLIENT
    }
}

val dummyCharges = listOf(
    Charge(
        clientId = 101,
        chargeId = 201,
        name = "Processing Fee",
        dueDate = arrayListOf(2025, 8, 15),
        chargeTimeType = ChargeTimeType(),
        chargeCalculationType = ChargeCalculationType(),
        currency = Currency(
            code = "INR",
            name = "Indian Rupee",
            decimalPlaces = 2,
            inMultiplesOf = 1.0,
            displaySymbol = "₹",
            nameCode = "currency.INR",
            displayLabel = "Indian Rupee (₹)",
        ),
        amount = 100.0,
        amountPaid = 50.0,
        amountOutstanding = 50.0,
        isActive = true,
    ),
    Charge(
        clientId = 102,
        chargeId = 202,
        name = "Late Payment Fee",
        dueDate = arrayListOf(2025, 9, 1),
        chargeTimeType = ChargeTimeType(),
        chargeCalculationType = ChargeCalculationType(),
        currency = Currency(
            code = "INR",
            name = "Indian Rupee",
            decimalPlaces = 2,
            inMultiplesOf = 1.0,
            displaySymbol = "₹",
            nameCode = "currency.INR",
            displayLabel = "Indian Rupee (₹)",
        ),
        amount = 200.0,
        amountPaid = 200.0,
        isChargePaid = true,
        paid = true,
    ),
    Charge(
        clientId = 103,
        chargeId = 203,
        name = "Service Charge",
        dueDate = arrayListOf(2025, 10, 5),
        chargeTimeType = ChargeTimeType(),
        chargeCalculationType = ChargeCalculationType(),
        currency = Currency(
            code = "INR",
            name = "Indian Rupee",
            decimalPlaces = 2,
            inMultiplesOf = 1.0,
            displaySymbol = "₹",
            nameCode = "currency.INR",
            displayLabel = "Indian Rupee (₹)",
        ),
        amount = 200.0,
        amountWaived = 150.0,
        waived = true,
        isChargeWaived = true,
    ),
    Charge(
        clientId = 104,
        chargeId = 204,
        name = "Insurance Fee",
        dueDate = arrayListOf(2025, 11, 20),
        chargeTimeType = ChargeTimeType(),
        chargeCalculationType = ChargeCalculationType(),
        currency = Currency(
            code = "INR",
            name = "Indian Rupee",
            decimalPlaces = 2,
            inMultiplesOf = 1.0,
            displaySymbol = "₹",
            nameCode = "currency.INR",
            displayLabel = "Indian Rupee (₹)",
        ),
        amount = 300.0,
        amountPaid = 0.0,
        amountOutstanding = 300.0,
        penalty = true,
    ),
    Charge(
        clientId = 105,
        chargeId = 205,
        name = "Loan Setup Fee",
        dueDate = arrayListOf(2025, 12, 10),
        chargeTimeType = ChargeTimeType(),
        chargeCalculationType = ChargeCalculationType(),
        currency = Currency(
            code = "INR",
            name = "Indian Rupee",
            decimalPlaces = 2,
            inMultiplesOf = 1.0,
            displaySymbol = "₹",
            nameCode = "currency.INR",
            displayLabel = "Indian Rupee (₹)",
        ),
        amount = 120.0,
        amountPaid = 100.0,
        amountOutstanding = 20.0,
    ),
)

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

/**
 * ViewModel responsible for managing the state of client, loan, and savings charges.
 *
 * Handles:
 * - Fetching charges based on charge type (CLIENT, LOAN, SAVINGS)
 * - Displaying loading or error states
 * - Listening to network status updates
 * - Emitting UI events (toast, navigation)
 *
 * @property clientChargeRepositoryImp Repository for retrieving charge data
 * @property networkMonitor Used to observe current network connectivity
 * @property userPreferencesRepositoryImpl Provides client-specific information like clientId
 * @property savedStateHandle Retrieves navigation arguments via `ClientChargesRoute`
 */
internal class ClientChargeViewModel(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ClientChargeState, ClientChargeEvent, ClientChargeAction>(
    initialState = run {
        val chargeRoute = savedStateHandle.toRoute<ClientChargesRoute>()
        val chargeType = ChargeType.valueOf(chargeRoute.chargeType.uppercase())

        val topBarId = when (chargeType) {
            ChargeType.CLIENT -> Res.string.client_charges
            ChargeType.SAVINGS -> Res.string.savings_charges
            ChargeType.LOAN -> Res.string.loan_charges
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

    init {
        // Observe network connectivity
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

        // Load initial data
        loadCharges()
    }

    /**
     * Updates the UI state by applying a transformation.
     */
    private fun updateState(update: (ClientChargeState) -> ClientChargeState) {
        mutableStateFlow.update(update)
    }

    /**
     * Handles all dispatched actions.
     */
    override fun handleAction(action: ClientChargeAction) {
        when (action) {
            is ClientChargeAction.RefreshCharges -> refreshCharges()
            is ClientChargeAction.OnNavigate -> sendEvent(ClientChargeEvent.Navigate)
            is ClientChargeAction.OnDismissDialog -> dismissDialog()
            is ClientChargeAction.OnChargeClick -> sendEvent(ClientChargeEvent.OnChargeClick(action.charge))
            is ClientChargeAction.Internal.ReceiveClientChargesResult ->
                handleClientChargesResult(action.result)
            is ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult ->
                handleLoanOrSavingsChargesResult(action.result)
        }
    }

    /**
     * Clears any active dialog.
     */
    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    /**
     * Handles result of loan/savings charge API.
     */
    private fun handleLoanOrSavingsChargesResult(result: DataState<List<Charge>>) {
        when (result) {
            is DataState.Loading -> updateState {
                it.copy(dialogState = ClientChargeState.DialogState.Loading)
            }

            is DataState.Error -> updateState {
                it.copy(
                    dialogState = ClientChargeState.DialogState.Error(
                        result.exception.message ?: "An Error Occurred",
                    ),
                )
            }

            is DataState.Success -> updateState {
                if (result.data.isEmpty()) {
                    it.copy(isEmpty = true, dialogState = null)
                } else {
                    it.copy(charges = result.data, dialogState = null)
                }
            }
        }
    }

    /**
     * Handles result of client charge API.
     */
    private fun handleClientChargesResult(result: DataState<Page<Charge>>) {
        when (result) {
            is DataState.Loading -> updateState {
                it.copy(dialogState = ClientChargeState.DialogState.Loading)
            }

            is DataState.Error -> updateState {
                it.copy(
                    dialogState = ClientChargeState.DialogState.Error(
                        result.exception.message ?: "An Error Occurred",
                    ),
                )
            }

            is DataState.Success -> updateState {
                val items = result.data.pageItems
                if (items.isEmpty()) {
                    it.copy(isEmpty = true, dialogState = null)
                } else {
                    it.copy(charges = items, dialogState = null)
                }
            }
        }
    }

    /**
     * Starts loading charges based on the charge type.
     */
    private fun loadCharges() {
        viewModelScope.launch {
            when (state.chargeType) {
                ChargeType.CLIENT -> processClientCharges()
                ChargeType.LOAN, ChargeType.SAVINGS -> processLoanOrSavingsCharges()
            }
        }
    }

    /**
     * Reloads charge list on refresh.
     */
    private fun refreshCharges() = loadCharges()

    /**
     * Processes charges when type is CLIENT.
     */
    private fun processClientCharges() {
        viewModelScope.launch {
            clientChargeRepositoryImp.getCharges(state.clientId)
                .collect { result ->
                    sendAction(ClientChargeAction.Internal.ReceiveClientChargesResult(result))
                }
        }
    }

    /**
     * Processes charges when type is LOAN or SAVINGS.
     */
    private fun processLoanOrSavingsCharges() {
        viewModelScope.launch {
            clientChargeRepositoryImp.getLoanOrSavingsCharges(
                state.chargeType,
                state.chargeTypeId ?: -1L,
            ).collect { result ->
                sendAction(ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult(result))
            }
        }
    }
}

/**
 * Represents the UI state of the Client Charges screen.
 *
 * @property clientId ID of the current client.
 * @property chargeType Type of charge (CLIENT, LOAN, SAVINGS).
 * @property chargeTypeId Optional ID used for LOAN or SAVINGS charge types.
 * @property isOnline Whether the device is currently connected to the internet.
 * @property isEmpty Whether there are no charges to display.
 * @property topBarTitleResId Title shown in the app bar.
 * @property dialogState Dialog state used for showing loading or error.
 * @property charges List of fetched charges.
 */
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
    /**
     * Represents the possible dialog states in the UI.
     */
    sealed interface DialogState {
        /** Error dialog with a message */
        data class Error(val message: String) : DialogState

        /** Loading dialog shown while fetching data */
        data object Loading : DialogState
    }
}

/**
 * UI events emitted from the ViewModel to be handled by the UI layer.
 */
sealed interface ClientChargeEvent {
    /**
     * Shows a toast message.
     * @param message Message to display.
     */
    data class ShowToast(val message: String) : ClientChargeEvent

    /** Navigates to the charge creation screen. */
    data object Navigate : ClientChargeEvent

    /**
     * Triggered when a charge item is clicked.
     * @param charge The clicked charge.
     */
    data class OnChargeClick(val charge: Charge) : ClientChargeEvent
}

/**
 * Actions dispatched from the UI or internal processes.
 */
sealed interface ClientChargeAction {

    /** Refreshes the list of charges. */
    data object RefreshCharges : ClientChargeAction

    /** Navigates to the charge creation screen. */
    data object OnNavigate : ClientChargeAction

    /** Dismisses any open dialog (error/loading). */
    data object OnDismissDialog : ClientChargeAction

    /**
     * Triggered when a user clicks on a charge item.
     * @param charge The clicked charge.
     */
    data class OnChargeClick(val charge: Charge) : ClientChargeAction

    /**
     * Internal actions used by the ViewModel.
     */
    sealed class Internal : ClientChargeAction {

        /**
         * Result from fetching loan or savings charges.
         * @param result DataState containing a list of charges.
         */
        data class ReceiveLoanOrSavingsChargesResult(
            val result: DataState<List<Charge>>,
        ) : Internal()

        /**
         * Result from fetching client charges.
         * @param result DataState containing a paginated response.
         */
        data class ReceiveClientChargesResult(
            val result: DataState<Page<Charge>>,
        ) : Internal()
    }
}

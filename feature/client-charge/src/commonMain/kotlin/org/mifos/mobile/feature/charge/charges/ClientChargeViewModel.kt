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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.charges
import mifos_mobile.feature.client_charge.generated.resources.client_charges
import mifos_mobile.feature.client_charge.generated.resources.feature_generic_error_server
import mifos_mobile.feature.client_charge.generated.resources.loan_charges
import mifos_mobile.feature.client_charge.generated.resources.savings_charges
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

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
            chargeType = ChargeType.valueOf(chargeRoute.chargeType),
            chargeTypeId = chargeRoute.chargeTypeId,
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            topBarTitleResId = topBarId,
            isOnline = false,
        )
    },
) {

    init {
        observeNetworkStatus()
    }

    /**
     * Observes the network connectivity status and updates the UI state accordingly.
     * If the network is unavailable, it sets the `networkStatus` flag in the state
     * and shows a network-related dialog.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->

                    sendAction(ClientChargeAction.ReceiveNetworkResult(isOnline = isOnline))
                }
        }
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

            is ClientChargeAction.ReceiveNetworkResult -> handleNetworkResult(action.isOnline)

            is ClientChargeAction.Retry -> retry()

            is ClientChargeAction.Internal.ReceiveClientChargesResult ->
                handleClientChargesResult(action.result)

            is ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult ->
                handleLoanOrSavingsChargesResult(action.result)
        }
    }

    private fun handleNetworkResult(isOnline: Boolean) {
        updateState {
            it.copy(networkStatus = isOnline)
        }
        if (!isOnline) {
            updateState { current ->
                if (current.uiState is ScreenUiState.Loading ||
                    current.uiState is ScreenUiState.Error ||
                    current.uiState is ScreenUiState.Empty ||
                    current.uiState is ScreenUiState.Network
                ) {
                    current.copy(uiState = ScreenUiState.Network)
                } else {
                    current
                }
            }
        } else {
            loadCharges()
        }
    }

    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                loadCharges()
            }
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
                it.copy(uiState = ScreenUiState.Loading)
            }

            is DataState.Error -> updateState {
                it.copy(
                    uiState = if (result.exception.cause is IOException) {
                        ScreenUiState.Network
                    } else {
                        ScreenUiState.Error(Res.string.feature_generic_error_server)
                    },
                )
            }

            is DataState.Success -> updateState {
                if (result.data.isEmpty()) {
                    it.copy(uiState = ScreenUiState.Empty)
                } else {
                    it.copy(uiState = ScreenUiState.Success)
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
                it.copy(uiState = ScreenUiState.Loading)
            }

            is DataState.Error -> updateState {
                it.copy(
                    uiState = if (result.exception.cause is IOException) {
                        ScreenUiState.Network
                    } else {
                        ScreenUiState.Error(Res.string.feature_generic_error_server)
                    },
                )
            }

            is DataState.Success -> updateState {
                if (result.data.pageItems.isEmpty()) {
                    it.copy(uiState = ScreenUiState.Empty)
                } else {
                    it.copy(uiState = ScreenUiState.Success)
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
 * @property uiState holds the state of screen
 */
data class ClientChargeState(
    val networkStatus: Boolean = false,
    val clientId: Long,
    val chargeType: ChargeType,
    val chargeTypeId: Long?,
    val isOnline: Boolean,
    val isEmpty: Boolean = false,
    val topBarTitleResId: StringResource = Res.string.charges,
    val charges: List<Charge> = emptyList(),

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * Represents the possible dialog states in the UI.
     */
    sealed interface DialogState {
        /** Error dialog with a message */
        data class Error(val message: String) : DialogState
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

    /** Receive the network status */
    data class ReceiveNetworkResult(val isOnline: Boolean) : ClientChargeAction

    /** Action to trigger to refetch the charges */
    data object Retry : ClientChargeAction

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

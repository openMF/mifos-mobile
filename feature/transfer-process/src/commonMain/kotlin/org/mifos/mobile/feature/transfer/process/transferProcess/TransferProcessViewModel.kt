/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.transferProcess

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.number
import mifos_mobile.core.ui.generated.resources.internal_server_error
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.back_to_accounts
import mifos_mobile.feature.transfer_process.generated.resources.transfer_failed
import mifos_mobile.feature.transfer_process.generated.resources.transfer_successful
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.DateHelper.currentDate
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.ScreenUiState.Network
import org.mifos.mobile.core.ui.utils.observe
import kotlin.coroutines.cancellation.CancellationException
import mifos_mobile.core.ui.generated.resources.Res as UiRes

/**
 * ViewModel responsible for managing the transfer process logic.
 *
 * This ViewModel handles the entire lifecycle of a transfer after the initial details
 * have been gathered. It includes:
 * - Initialization of transfer payload from navigation arguments.
 * - Orchestrating user authentication before proceeding with the transfer.
 * - Executing the transfer call via the [TransferRepository].
 * - Updating the UI with the current state of the transfer (loading, success, error).
 * - Emitting navigation events based on the outcome of the transfer.
 *
 * @param transferRepository Repository used to execute the transfer operation.
 * @param savedStateHandle Handle to saved state, used for retrieving navigation arguments
 *                          such as transfer details.
 * @param navigator Utility to observe results from other parts of the application,
 *                  specifically used here to get the authentication result.
 */
internal class TransferProcessViewModel(
    private val transferRepository: TransferRepository,
    savedStateHandle: SavedStateHandle,
    private val networkMonitor: NetworkMonitor,
    private val navigator: ResultNavigator,
) : BaseViewModel<TransferProcessState, TransferProcessEvent, TransferProcessAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<TransferProcessRoute>()
        val transferDate = listOf(
            currentDate.day,
            currentDate.month.number,
            currentDate.year,
        )
        println("TransferProcessViewModel: route = ${route.transferSuccessDestination}")
        TransferProcessState(
            transferDestination = route.transferSuccessDestination,
            transferType = enumValueOf<TransferType>(route.transferType),
            transferPayload = TransferPayload(
                fromAccountId = route.fromAccountId,
                fromClientId = route.fromClientId,
                fromAccountType = route.fromAccountType,
                fromOfficeId = route.fromOfficeId,
                toOfficeId = route.toOfficeId,
                toAccountId = route.toAccountId,
                toClientId = route.toClientId,
                toAccountType = route.toAccountType,
                transferDate = DateHelper.getDateMonthYearString(transferDate),
                transferAmount = route.transferAmount?.toDouble(),
                transferDescription = route.transferDescription,
                dateFormat = "dd MMMM yyyy",
                locale = "en",
            ),
            fromClientName = route.fromClientName,
            toClientName = route.toClientName,
        )
    },
) {

    init {
        observeNetworkStatus()
        observeAuthResult()
    }

    /**
     * Handles incoming actions from the UI or internal events within the ViewModel.
     *
     * @param action The [TransferProcessAction] to be processed.
     */
    override fun handleAction(action: TransferProcessAction) {
        when (action) {
            is TransferProcessAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is TransferProcessAction.RequestTransfer -> sendEvent(TransferProcessEvent.NavigateToAuthenticate())

            is TransferProcessAction.OnNavigate -> sendEvent(TransferProcessEvent.Navigate)

            is TransferProcessAction.Internal.ReceiveAuthenticationResult -> {
                if (action.result) {
                    makeTransfer()
                }
            }

            TransferProcessAction.Internal.MakeTransfer -> makeTransfer()
        }
    }

    /**
     * Helper function to update the [TransferProcessState].
     *
     * @param update A lambda function that takes the current state and returns an updated state.
     */
    private fun updateState(update: (TransferProcessState) -> TransferProcessState) {
        mutableStateFlow.update(update)
    }

    /**
     * Observes the result of a passcode authentication flow.
     * If authentication is successful, it triggers the [TransferProcessAction.Internal.MakeTransfer] action.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            navigator.observe<AuthResult>()
                .collect { result ->
                    if (result.success) {
                        sendAction(TransferProcessAction.Internal.ReceiveAuthenticationResult(result.success))
                    }
                }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(TransferProcessAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handles changes in network connectivity.
     *
     * It updates the `networkStatus` state. If the network is offline, it sets the
     * `uiState` to [ScreenUiState.Network]. If the network is online, it
     * automatically triggers a data fetch to refresh the content.
     *
     * @param isOnline A boolean indicating the current network status.
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }
        if (!isOnline) {
            updateState { current ->
                if (current.showOverlay) return@updateState current

                if (current.uiState is ScreenUiState.Loading ||
                    current.uiState is ScreenUiState.Error ||
                    current.uiState is ScreenUiState.Network
                ) {
                    current.copy(uiState = ScreenUiState.Network)
                } else {
                    current
                }
            }
        }
    }

    /**
     * Initiates the transfer process by calling the [transferRepository] with the current
     * [TransferProcessState.transferPayload].
     * Updates the UI state to reflect loading and processes the result.
     */
    private fun makeTransfer() {
        val currentState = state
        if (currentState.showOverlay ||
            currentState.transferPayload == null ||
            currentState.transferType == null
        ) {
            return
        }
        updateState { it.copy(showOverlay = true) }

        viewModelScope.launch {
            try {
                val result = transferRepository.makeTransfer(
                    currentState.transferPayload,
                    currentState.transferType,
                )
                processTransferResult(result)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                processTransferResult(DataState.Error(e))
            }
        }
    }

    /**
     * Handles the response from the transfer API call.
     * Updates the UI state (e.g., stops loading) and emits navigation events based on
     * whether the transfer was successful or resulted in an error.
     *
     * @param response The [DataState] containing the result of the transfer operation.
     */
    private suspend fun processTransferResult(response: DataState<String>) {
        when (response) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        showOverlay = false,
                    )
                }

                val errorMsg = if (response.exception.cause is ServerResponseException) {
                    getString(UiRes.string.internal_server_error)
                } else {
                    response.message
                }

                sendEvent(
                    TransferProcessEvent.NavigateToStatus(
                        eventType = if (response.exception.cause is ServerResponseException) {
                            EventType.SERVER_EXCEPTION.name
                        } else {
                            EventType.FAILURE.name
                        },
                        eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                        title = getString(Res.string.transfer_failed),
                        subtitle = errorMsg,
                        buttonText = getString(Res.string.back_to_accounts),
                    ),
                )
            }

            DataState.Loading -> {
                updateState { it.copy(showOverlay = true) }
            }

            is DataState.Success -> {
                updateState { it.copy(showOverlay = false) }
                sendEvent(
                    TransferProcessEvent.NavigateToStatus(
                        eventType = EventType.SUCCESS.name,
                        eventDestination = state.transferDestination ?: "",
                        title = getString(Res.string.transfer_successful),
                        subtitle = "Transfer Id: ${response.data}",
                        buttonText = getString(Res.string.back_to_accounts),
                    ),
                )
            }
        }
    }
}

/**
 * Represents the UI state for the transfer process screen.
 *
 * @property transferDestination A string identifier for the screen to navigate to after a successful transfer.
 * @property transferType The [TransferType] (e.g., SELF, TPT, LOAN_REPAYMENT) of the current transfer.
 * @property transferPayload The [TransferPayload] containing all necessary data for the transfer API call.
 */
data class TransferProcessState(
    val transferDestination: String? = null,
    val transferType: TransferType? = null,
    val transferPayload: TransferPayload? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
    val fromClientName: String? = null,
    val toClientName: String? = null,
)

/**
 * Defines the events that the [TransferProcessViewModel] can send to the UI.
 * These events are typically used to trigger navigation or show one-time messages.
 */
sealed interface TransferProcessEvent {

    /** Generic navigation event, purpose might need further clarification or more specific events. */
    data object Navigate : TransferProcessEvent

    /**
     * Event to trigger navigation to an authentication screen (e.g., passcode).
     * @param status A status string, defaults to [EventType.SUCCESS.name]
     */
    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : TransferProcessEvent

    /**
     * Event to navigate to a generic status screen (e.g., success or failure screen).
     * @param eventType The type of event (e.g., "SUCCESS", "FAILURE").
     * @param eventDestination The destination identifier for navigation after the status screen.
     * @param title The title to be displayed on the status screen.
     * @param subtitle The subtitle or description for the status screen.
     * @param buttonText The text for the primary action button on the status screen.
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : TransferProcessEvent
}

/**
 * Defines the actions that can be dispatched to the [TransferProcessViewModel].
 * These actions can originate from the UI or be used internally by the ViewModel.
 */
sealed interface TransferProcessAction {
    /** Action dispatched when the user requests to initiate the transfer (e.g., clicks a confirm button). */
    data object RequestTransfer : TransferProcessAction

    /** Action dispatched for generic navigation, its specific use case should be clear from context. */
    data object OnNavigate : TransferProcessAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : TransferProcessAction

    /**
     * Sealed interface for internal actions used within the [TransferProcessViewModel].
     * These are not directly triggered by the UI but are part of the ViewModel's internal logic flow.
     */
    sealed interface Internal : TransferProcessAction {
        /**
         * Internal action dispatched when the authentication result is received.
         * @param result Boolean indicating whether authentication was successful (true) or not (false).
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /** Internal action to proceed with making the transfer call. */
        data object MakeTransfer : Internal
    }
}

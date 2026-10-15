/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.core.ui.generated.resources.internal_server_error
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.account_number_label
import mifos_mobile.feature.beneficiary.generated.resources.account_type_label
import mifos_mobile.feature.beneficiary.generated.resources.account_type_loan
import mifos_mobile.feature.beneficiary.generated.resources.account_type_savings
import mifos_mobile.feature.beneficiary.generated.resources.account_type_share
import mifos_mobile.feature.beneficiary.generated.resources.add_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.back_to_home
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_created_successfully
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_created_successfully_account
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_creation_failed
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_name_label
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_updated_successfully
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_updated_successfully_account
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_updation_failed
import mifos_mobile.feature.beneficiary.generated.resources.office_label
import mifos_mobile.feature.beneficiary.generated.resources.transfer_limit_label
import mifos_mobile.feature.beneficiary.generated.resources.try_again
import mifos_mobile.feature.beneficiary.generated.resources.update_beneficiary
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.observe
import mifos_mobile.core.ui.generated.resources.Res as UiRes

/**
 * ViewModel for confirming beneficiary details before final submission.
 * Handles authentication result, network state, form submission, and error states.
 */
internal class BeneficiaryApplicationConfirmationViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
    private val navigator: ResultNavigator,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<
    BeneficiaryApplicationConfirmationState,
    BeneficiaryApplicationConfirmationEvent,
    BeneficiaryApplicationConfirmationAction,
    >(
    initialState = run {
        val route = savedStateHandle.toRoute<BeneficiaryApplicationConfirmationNavRoute>()
        BeneficiaryApplicationConfirmationState(
            beneficiaryId = route.beneficiaryId,
            beneficiaryState = enumValueOf<BeneficiaryState>(route.beneficiaryState),
            name = route.name,
            officeName = route.officeName,
            accountType = route.accountType,
            accountNumber = route.accountNumber,
            transferLimit = route.transferLimit,
        )
    },
) {

    /**
     * Initializes observers for network status and authentication result,
     * and sets the top bar title based on state.
     */
    init {
        viewModelScope.launch {
            observeNetwork()
            observeAuthResult()
            initializeMapDetails()
            getTopBarTitle()
        }
    }

    /**
     * Updates the ViewModel state using the provided transformation.
     */
    private fun updateState(
        update: (BeneficiaryApplicationConfirmationState) -> BeneficiaryApplicationConfirmationState,
    ) {
        mutableStateFlow.update(update)
    }

    /**
     * Handles user actions such as navigation, form submission, and internal authentication results.
     */
    override fun handleAction(action: BeneficiaryApplicationConfirmationAction) {
        when (action) {
            BeneficiaryApplicationConfirmationAction.OnNavigate -> sendEvent(
                BeneficiaryApplicationConfirmationEvent.Navigate,
            )

            is BeneficiaryApplicationConfirmationAction.Internal.ReceiveAuthenticationResult -> {
                if (action.result) {
                    if (state.beneficiaryState == BeneficiaryState.UPDATE) {
                        val payload = BeneficiaryUpdatePayload(
                            name = state.name,
                            transferLimit = state.transferLimit,
                        )
                        updateBeneficiary(state.beneficiaryId, payload)
                    } else {
                        val payload = BeneficiaryPayload(
                            name = state.name,
                            accountNumber = state.accountNumber,
                            transferLimit = state.transferLimit,
                            officeName = state.officeName,
                            accountType = state.accountType,
                            locale = "en",
                        )
                        createBeneficiary(payload)
                    }
                }
            }

            is BeneficiaryApplicationConfirmationAction.ReceiveNetworkStatus ->
                handleNetworkStatus(action.isOnline)

            BeneficiaryApplicationConfirmationAction.SubmitBeneficiary -> {
                sendEvent(BeneficiaryApplicationConfirmationEvent.NavigateToAuthenticate())
            }

            is BeneficiaryApplicationConfirmationAction.Internal.ReceiveSubmitBeneficiary -> {
                processSubmitBeneficiaryResult(action.result)
            }
            is BeneficiaryApplicationConfirmationAction.Internal.ReceiveUpdateBeneficiary -> {
                processUpdateBeneficiaryResult(action.result)
            }
        }
    }

    /**
     * Initiates the API call to create a new beneficiary and handles success or failure events.
     */
    private fun createBeneficiary(payload: BeneficiaryPayload?) {
        updateState {
            it.copy(showOverlay = true)
        }
        viewModelScope.launch {
            val response = beneficiaryRepositoryImp.createBeneficiary(payload)
            sendAction(
                BeneficiaryApplicationConfirmationAction
                    .Internal
                    .ReceiveSubmitBeneficiary(response),
            )
        }
    }

/**
     * Handles the result of the beneficiary creation API call.
     * If the API call is successful, navigates the user to the beneficiary status screen.
     * If the API call fails, navigates the user to the previous screen with an error message.
     */
    private fun processSubmitBeneficiaryResult(response: DataState<String>) {
        viewModelScope.launch {
            when (response) {
                is DataState.Error -> {
                    updateState {
                        it.copy(showOverlay = false)
                    }
                    val errorMsg = if (response.exception.cause is ServerResponseException) {
                        getString(UiRes.string.internal_server_error)
                    } else {
                        response.message
                    }
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = if (response.exception.cause is ServerResponseException) {
                                EventType.SERVER_EXCEPTION.name
                            } else {
                                EventType.FAILURE.name
                            },
                            eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                            title = getString(Res.string.beneficiary_creation_failed),
                            subtitle = errorMsg,
                            buttonText = getString(Res.string.try_again),
                        ),
                    )
                }

                DataState.Loading -> updateState {
                    it.copy(showOverlay = true)
                }

                is DataState.Success -> {
                    updateState {
                        it.copy(showOverlay = false)
                    }
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = EventType.SUCCESS.name,
                            eventDestination = StatusNavigationDestination.BENEFICIARY.name,
                            title = getString(Res.string.beneficiary_created_successfully),
                            subtitle = getString(
                                Res.string.beneficiary_created_successfully_account,
                                state.accountNumber,
                                state.name,
                            ),
                            buttonText = getString(Res.string.back_to_home),
                        ),
                    )
                }
            }
        }
    }

    /**
     * (Optional) Updates an existing beneficiary with the given payload.
     * Currently not called in logic but reserved for future use.
     */
    private fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        updateState {
            it.copy(showOverlay = true)
        }
        viewModelScope.launch {
            val response = beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload)
            sendAction(
                BeneficiaryApplicationConfirmationAction
                    .Internal
                    .ReceiveUpdateBeneficiary(response),
            )
        }
    }

    /**
     * Processes the result of an update beneficiary API call.
     * Handles success, failure and loading states.
     * Shows an overlay during the loading state.
     * Shows a success or failure message on the status screen depending on the result.
     */
    private fun processUpdateBeneficiaryResult(response: DataState<String>) {
        viewModelScope.launch {
            when (response) {
                is DataState.Error -> {
                    updateState {
                        it.copy(showOverlay = false)
                    }

                    val errorMsg = if (response.exception.cause is ServerResponseException) {
                        getString(UiRes.string.internal_server_error)
                    } else {
                        response.message
                    }

                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = if (response.exception.cause is ServerResponseException) {
                                EventType.SERVER_EXCEPTION.name
                            } else {
                                EventType.FAILURE.name
                            },
                            eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                            title = getString(Res.string.beneficiary_updation_failed),
                            subtitle = errorMsg,
                            buttonText = getString(Res.string.try_again),
                        ),
                    )
                }

                DataState.Loading -> {
                    updateState {
                        it.copy(showOverlay = true)
                    }
                }

                is DataState.Success -> {
                    updateState {
                        it.copy(showOverlay = false)
                    }
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = EventType.SUCCESS.name,
                            eventDestination = StatusNavigationDestination.BENEFICIARY.name,
                            title = getString(Res.string.beneficiary_updated_successfully),
                            subtitle = getString(
                                Res.string.beneficiary_updated_successfully_account,
                                state.name,
                                state.transferLimit,
                            ),
                            buttonText = getString(Res.string.back_to_home),
                        ),
                    )
                }
            }
        }
    }

    /**
     * Observes the authentication result from a separate authentication screen,
     * and triggers submission if authentication succeeds.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            navigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(
                        BeneficiaryApplicationConfirmationAction
                            .Internal.ReceiveAuthenticationResult(result.success),
                    )
                }
        }
    }

    /**
     * Observe network and dispatch status changes into actions
     */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(BeneficiaryApplicationConfirmationAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handle network state changes like LoanAccountDetailsViewModel
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is ScreenUiState.Loading ||
                        current.uiState is ScreenUiState.Error ||
                        current.uiState is ScreenUiState.Network
                    ) {
                        current.copy(
                            uiState = ScreenUiState.Network,
                        )
                    } else {
                        current
                    }
                }
            }
        }
    }

    /**
     * Initializes the map details state variable with the route details.
     *
     * @param savedStateHandle the SavedStateHandle containing the route details.
     */
    private suspend fun initializeMapDetails() {
        val route = savedStateHandle.toRoute<BeneficiaryApplicationConfirmationNavRoute>()
        val details = mapOf(
            Res.string.beneficiary_name_label to route.name,
            Res.string.office_label to route.officeName,
            Res.string.account_type_label to when (route.accountType) {
                0 -> getString(Res.string.account_type_share)
                1 -> getString(Res.string.account_type_loan)
                2 -> getString(Res.string.account_type_savings)
                else -> ""
            },
            Res.string.account_number_label to route.accountNumber,
            Res.string.transfer_limit_label to route.transferLimit.toString(),
        )
        updateState {
            it.copy(
                details = details,
            )
        }
    }

    /**
     * Updates the top bar title depending on whether the user is updating or adding a beneficiary.
     */
    private fun getTopBarTitle() {
        val update = Res.string.update_beneficiary
        val add = Res.string.add_beneficiary
        updateState {
            it.copy(
                topBarTitle = when (state.beneficiaryState) {
                    BeneficiaryState.UPDATE -> update
                    else -> add
                },
            )
        }
    }
}

/**
 * Represents the state of the Beneficiary Application Confirmation screen.
 *
 * @param details the map of beneficiary details to display on the screen.
 * @param topBarTitle the title to display on the top bar of the screen.
 * @param beneficiaryId the ID of the beneficiary being updated or added.
 * @param name the name of the beneficiary.
 * @param officeName the name of the office where the beneficiary is registered.
 * @param accountType the type of account the beneficiary has (e.g. savings, loan).
 * @param accountNumber the account number of the beneficiary.
 * @param transferLimit the transfer limit of the beneficiary.
 * @param networkUnavailable indicates whether the network is unavailable.
 * @param beneficiaryState the state of the beneficiary (e.g. updating, adding).
 * @param dialogState the state of any dialogs to display on the screen.
 * @param uiState the current UI state of the screen (e.g. loading, error, success).
 * @param showOverlay indicates whether to show an overlay on the screen.
 * @param networkStatus indicates whether the network is available.
 */

data class BeneficiaryApplicationConfirmationState(
    val details: Map<StringResource, String> = emptyMap(),
    val topBarTitle: StringResource = Res.string.add_beneficiary,
    val beneficiaryId: Long,
    val name: String,
    val officeName: String,
    val accountType: Int,
    val accountNumber: String,
    val transferLimit: Int,
    val networkUnavailable: Boolean = false,
    val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_MANUAL,

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
    val networkStatus: Boolean = false,
) {
    sealed interface DialogState {

        data object Network : DialogState
    }
}

/*
* Represents the events that can occur on the Beneficiary Application Confirmation screen.
*/

sealed interface BeneficiaryApplicationConfirmationEvent {
    /**
     * Represents the event of navigating back to the previous screen.
     */
    data object Navigate : BeneficiaryApplicationConfirmationEvent

    /**
     * Represents the event of navigating to the Beneficiary Status screen.
     *
     * @param eventType the type of event (e.g. success, error).
     * @param eventDestination the destination to navigate to.
     * @param title the title to display on the screen.
     * @param subtitle the subtitle to display on the screen.
     * @param buttonText the text to display on the button.
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,

        val buttonText: String,
    ) : BeneficiaryApplicationConfirmationEvent

    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : BeneficiaryApplicationConfirmationEvent
}

/**
 * Represents the actions that can be performed on the Beneficiary Application Confirmation screen.
 *
 * @param SubmitBeneficiary the action of submitting the beneficiary application.
 * @param ReceiveNetworkStatus the action of receiving the network status.
 * @param OnNavigate the action of navigating back to the previous screen.
 */

sealed interface BeneficiaryApplicationConfirmationAction {

    data object SubmitBeneficiary : BeneficiaryApplicationConfirmationAction

    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryApplicationConfirmationAction

    data object OnNavigate : BeneficiaryApplicationConfirmationAction

    /**
     * Represents the internal actions that can be performed on the Beneficiary Application Confirmation screen.
     *
     * @param ReceiveAuthenticationResult the action of receiving the authentication result.
     * @param ReceiveSubmitBeneficiary the action of receiving the result of submitting the beneficiary application.
     * @param ReceiveUpdateBeneficiary the action of receiving the result of updating the beneficiary application.
     */
    sealed interface Internal : BeneficiaryApplicationConfirmationAction {
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        data class ReceiveSubmitBeneficiary(val result: DataState<String>) : Internal

        data class ReceiveUpdateBeneficiary(val result: DataState<String>) : Internal
    }
}

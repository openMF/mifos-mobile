/*
 * Copyright 2025 Mifos Initiative
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.observe

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
            dialogState = null,
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
            initializeMapDetails()
            observeNetworkStatus()
            observeAuthResult()
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
     * Updates only the dialog state in the ViewModel.
     */
    private fun setDialogState(dialogState: BeneficiaryApplicationConfirmationState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
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
        setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)
        viewModelScope.launch {
            val response = beneficiaryRepositoryImp.createBeneficiary(payload)
            sendAction(
                BeneficiaryApplicationConfirmationAction
                    .Internal
                    .ReceiveSubmitBeneficiary(response),
            )
        }
    }

    private fun processSubmitBeneficiaryResult(response: DataState<String>) {
        viewModelScope.launch {
            when (response) {
                is DataState.Error -> {
                    setDialogState(null)
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = EventType.FAILURE.name,
                            eventDestination = "",
                            title = getString(Res.string.beneficiary_creation_failed),
                            subtitle = response.message,
                            buttonText = getString(Res.string.try_again),
                        ),
                    )
                }

                DataState.Loading -> setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)

                is DataState.Success -> {
                    setDialogState(null)
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = EventType.SUCCESS.name,
                            eventDestination = "",
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
        setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)
        viewModelScope.launch {
            val response = beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload)
            sendAction(
                BeneficiaryApplicationConfirmationAction
                    .Internal
                    .ReceiveUpdateBeneficiary(response),
            )
        }
    }

    private fun processUpdateBeneficiaryResult(response: DataState<String>) {
        viewModelScope.launch {
            when (response) {
                is DataState.Error -> {
                    setDialogState(null)
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = EventType.FAILURE.name,
                            eventDestination = "",
                            title = getString(Res.string.beneficiary_updation_failed),
                            subtitle = response.message,
                            buttonText = getString(Res.string.try_again),
                        ),
                    )
                }
                DataState.Loading -> setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)
                is DataState.Success -> {
                    setDialogState(null)
                    sendEvent(
                        BeneficiaryApplicationConfirmationEvent.NavigateToStatus(
                            eventType = EventType.SUCCESS.name,
                            eventDestination = "",
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
     * Observes network connectivity status and updates UI accordingly.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .map(Boolean::not)
                .distinctUntilChanged()
                .collect { isOffline ->
                    updateState {
                        it.copy(
                            networkUnavailable = isOffline,
                            dialogState = if (isOffline) {
                                BeneficiaryApplicationConfirmationState.DialogState.Network
                            } else {
                                null
                            },
                        )
                    }
                }
        }
    }

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
    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data object Loading : DialogState

        data object Network : DialogState
    }
}

sealed interface BeneficiaryApplicationConfirmationEvent {
    data object Navigate : BeneficiaryApplicationConfirmationEvent
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

sealed interface BeneficiaryApplicationConfirmationAction {

    data object SubmitBeneficiary : BeneficiaryApplicationConfirmationAction

    data object OnNavigate : BeneficiaryApplicationConfirmationAction

    sealed interface Internal : BeneficiaryApplicationConfirmationAction {
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal
        data class ReceiveSubmitBeneficiary(val result: DataState<String>) : Internal
        data class ReceiveUpdateBeneficiary(val result: DataState<String>) : Internal
    }
}

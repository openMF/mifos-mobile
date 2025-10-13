/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.delete_beneficiary_confirmation
import mifos_mobile.feature.beneficiary.generated.resources.feature_generic_error_server
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * A view model for the beneficiary detail screen.
 *
 * @param beneficiaryRepositoryImp The repository for beneficiary data.
 * @param networkMonitor The network monitor to use for network status.
 * @param savedStateHandle The saved state handle to use for navigation.
 */
internal class BeneficiaryDetailViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<BeneficiaryDetailState, BeneficiaryDetailEvent, BeneficiaryDetailAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<BeneficiaryDetailNavRoute>()
        BeneficiaryDetailState(
            beneficiaryId = route.beneficiaryId,
        )
    },
) {

    /**
     * Initialize the view model.
     */
    init {
        observeNetwork()
    }

    /**
     * Observe network and dispatch status changes into actions
     */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(BeneficiaryDetailAction.ReceiveNetworkStatus(isOnline))
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
            } else {
                loadBeneficiary()
            }
        }
    }

    /**
     * Updates the view model state using the provided lambda transformation.
     * @param update The lambda transformation to apply to the current state.
     */
    private fun updateState(update: (BeneficiaryDetailState) -> BeneficiaryDetailState) {
        mutableStateFlow.update(update)
    }

    /**
     * Updates the beneficiary dialog state in the view model state.
     *
     * @param dialogState The new dialog state to set. If null, the dialog state will be cleared.
     */
    private fun setDialogState(dialogState: BeneficiaryDetailState.DialogState?) {
        updateState { it.copy(beneficiaryDialog = dialogState) }
    }

    /*
     * Update the view model state to show a loading state and then
     * an internal action to handle the list is sent.
     * */
    private fun loadBeneficiary() {
        updateState {
            it.copy(
                uiState = ScreenUiState.Loading,
            )
        }
        viewModelScope.launch {
            beneficiaryRepositoryImp.beneficiaryList().collect {
                sendAction(BeneficiaryDetailAction.Internal.ReceiveBeneficiaryResult(it))
            }
        }
    }

    /**
     * Handles the response from the beneficiary list API and updates the view model state accordingly.
     * If the response is successful, the view model state is updated to show a success state and
     * the beneficiary is updated to the one with the matching ID.
     *
     * @param beneficiary The response from the beneficiary list API.
     */
    private fun handleResponse(beneficiary: DataState<List<Beneficiary>>) {
        when (beneficiary) {
            DataState.Loading -> {
                updateState {
                    it.copy(
                        uiState = ScreenUiState.Loading,
                    )
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = if (beneficiary.exception is IOException) {
                            ScreenUiState.Network
                        } else {
                            ScreenUiState.Error(Res.string.feature_generic_error_server)
                        },
                    )
                }
            }

            is DataState.Success -> {
                updateState { currentState ->
                    currentState.copy(
                        uiState = ScreenUiState.Success,
                        beneficiary = beneficiary.data.find { it.id == currentState.beneficiaryId },
                    )
                }
            }
        }
    }

    /**
     * Deletes a beneficiary with the given ID.
     *
     * This function updates the view model state to show an overlay and then
     * sends an internal action to handle the response from the delete beneficiary API.
     *
     * @param beneficiaryId The ID of the beneficiary to delete.
     */
    private fun deleteBeneficiary(beneficiaryId: Long?) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    showOverlay = true,
                )
            }
            val response = beneficiaryRepositoryImp.deleteBeneficiary(beneficiaryId)
            sendAction(
                BeneficiaryDetailAction
                    .Internal
                    .ReceiveDeleteBeneficiary(response),
            )
        }
    }

    /**
     * Processes the result of the delete beneficiary API.
     *
     * If the response is loading, the view model state is updated to show an overlay.
     * If the response is an error, the view model state is updated to show an error state and a dialog
     * is shown with a generic error message.
     * If the response is successful, the view model state is updated to navigate back to the previous screen.
     *
     * @param response The response from the delete beneficiary API.
     */
    private fun processDeleteBeneficiaryResult(response: DataState<String>) {
        viewModelScope.launch {
            when (response) {
                DataState.Loading -> {
                    updateState {
                        it.copy(
                            showOverlay = true,
                        )
                    }
                }
                is DataState.Error -> {
                    updateState {
                        it.copy(
                            showOverlay = false,
                        )
                    }
                    setDialogState(
                        BeneficiaryDetailState.DialogState.Error(
                            Res.string.feature_generic_error_server,
                        ),
                    )
                }
                is DataState.Success -> {
                    sendEvent(BeneficiaryDetailEvent.NavigateBack)
                }
            }
        }
    }

    /**
     * Handles actions from the UI.
     *
     * @param action The action to handle.
     */
    override fun handleAction(action: BeneficiaryDetailAction) {
        when (action) {
            is BeneficiaryDetailAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is BeneficiaryDetailAction.DeleteBeneficiary -> deleteBeneficiary(state.beneficiaryId)

            is BeneficiaryDetailAction.OnUpdateBeneficiary -> sendEvent(
                BeneficiaryDetailEvent.UpdateBeneficiary(state.beneficiaryId),
            )

            BeneficiaryDetailAction.OnNavigate -> sendEvent(BeneficiaryDetailEvent.NavigateBack)

            is BeneficiaryDetailAction.ErrorDialogDismiss -> updateState { it.copy(beneficiaryDialog = null) }

            BeneficiaryDetailAction.ShowDeleteConfirmation -> showDeleteConfirmation()

            BeneficiaryDetailAction.OnRefresh -> loadBeneficiary()

            is BeneficiaryDetailAction.Internal.ReceiveBeneficiaryResult -> {
                handleResponse(action.result)
            }

            is BeneficiaryDetailAction.Internal.ReceiveDeleteBeneficiary -> {
                processDeleteBeneficiaryResult(action.result)
            }
        }
    }

    /**
     * Shows a confirmation dialog before deleting a beneficiary.
     */
    private fun showDeleteConfirmation() {
        viewModelScope.launch {
            val message = getString(Res.string.delete_beneficiary_confirmation)
            setDialogState(
                BeneficiaryDetailState.DialogState.Confirmation(
                    message,
                ),
            )
        }
    }
}

/**
 * Represents the state of the Beneficiary Detail screen.
 *
 * @property beneficiaryId The ID of the beneficiary to display.
 * @property beneficiary The beneficiary to display.
 * @property beneficiaryDialog The dialog state of the beneficiary detail screen.
 * @property networkStatus The current network status of the device.
 * @property uiState The current UI state of the beneficiary detail screen.
 * @property showOverlay Whether to show the overlay or not.
 */
data class BeneficiaryDetailState(
    val beneficiaryId: Long = -1L,
    val beneficiary: Beneficiary? = null,
    val beneficiaryDialog: DialogState? = null,

    val networkStatus: Boolean = false,
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: StringResource) : DialogState

        data class Confirmation(val message: String) : DialogState
    }
}

/*
* Represents the events that can be triggered from the Beneficiary Detail screen.
*
* @property NavigateBack Navigates back to the previous screen.
* @property UpdateBeneficiary Updates the beneficiary with the given ID.
* */

sealed interface BeneficiaryDetailEvent {

    data object NavigateBack : BeneficiaryDetailEvent

    data class UpdateBeneficiary(val beneficiaryId: Long) : BeneficiaryDetailEvent
}

/*
* Represents the actions that can be triggered from the Beneficiary Detail screen.
*
* @property OnRefresh Refreshes the beneficiary list.
* @property OnUpdateBeneficiary Updates the beneficiary with the given ID.
* @property DeleteBeneficiary Deletes the beneficiary with the given ID.
* @property OnNavigate Navigates to the beneficiary list screen.
* @property ErrorDialogDismiss Dismisses the error dialog.
* @property ShowDeleteConfirmation Shows the delete confirmation dialog.
* @property ReceiveNetworkStatus Receives the network status.
* @property Internal Internal actions.
* */
sealed interface BeneficiaryDetailAction {

    data object OnRefresh : BeneficiaryDetailAction

    data object OnUpdateBeneficiary : BeneficiaryDetailAction

    data object DeleteBeneficiary : BeneficiaryDetailAction

    data object OnNavigate : BeneficiaryDetailAction

    data object ErrorDialogDismiss : BeneficiaryDetailAction

    data object ShowDeleteConfirmation : BeneficiaryDetailAction

    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryDetailAction

    sealed interface Internal : BeneficiaryDetailAction {

        data class ReceiveBeneficiaryResult(val result: DataState<List<Beneficiary>>) : Internal

        data class ReceiveDeleteBeneficiary(val result: DataState<String>) : Internal
    }
}

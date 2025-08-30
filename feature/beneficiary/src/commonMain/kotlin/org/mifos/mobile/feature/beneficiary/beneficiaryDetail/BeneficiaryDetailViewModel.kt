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

    private fun updateState(update: (BeneficiaryDetailState) -> BeneficiaryDetailState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: BeneficiaryDetailState.DialogState?) {
        updateState { it.copy(beneficiaryDialog = dialogState) }
    }

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

sealed interface BeneficiaryDetailEvent {
    data object NavigateBack : BeneficiaryDetailEvent
    data class UpdateBeneficiary(val beneficiaryId: Long) : BeneficiaryDetailEvent
}

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

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.transferred_successfully
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants.PAYLOAD
import org.mifos.mobile.core.common.Constants.TRANSFER_SUCCESS_DESTINATION
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.TransferResponse
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class TransferProcessViewModel(
    private val transferRepository: TransferRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<TransferProcessState, TransferProcessEvent, TransferProcessAction>(
    initialState = TransferProcessState(
        dialogState = null,
        transferPayloadString = savedStateHandle.getStateFlow<String?>(
            key = PAYLOAD,
            initialValue = null,
        ).value,
        transferType = savedStateHandle.getStateFlow(
            key = TRANSFER_TYPE,
            initialValue = TransferType.SELF.name,
        ).value.let { TransferType.valueOf(it) },
        transferDestination = savedStateHandle.getStateFlow(
            key = TRANSFER_SUCCESS_DESTINATION,
            initialValue = TransferSuccessDestination.HOME.name,
        ).value.let { TransferSuccessDestination.valueOf(it) },
    ),
) {

    init {
        updateState {
            it.copy(
                transferPayload = state.transferPayloadString?.let { jsonString ->
                    jsonString.let { Json.decodeFromString<TransferPayload>(jsonString) }
                },
            )
        }
    }

    override fun handleAction(action: TransferProcessAction) {
        when (action) {
            is TransferProcessAction.MakeTransfer -> makeTransfer()
            TransferProcessAction.OnNavigate -> sendEvent(TransferProcessEvent.Navigate)
        }
    }

    private fun updateState(update: (TransferProcessState) -> TransferProcessState) {
        mutableStateFlow.update(update)
    }

    private fun makeTransfer() {
        state.transferPayload?.let { payload ->
            updateState { it.copy(dialogState = TransferProcessState.DialogState.Loading) }

            viewModelScope.launch {
                try {
                    val successMessage = getString(Res.string.transferred_successfully)
                    val response = transferRepository.makeTransfer(payload, state.transferType)
                    processTransferResult(response, successMessage)
                } catch (e: Exception) {
                    updateState {
                        it.copy(
                            dialogState = TransferProcessState.DialogState.Error(
                                e.message ?: "An error occurred",
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun processTransferResult(
        response: DataState<TransferResponse>,
        message: String,
    ) {
        when (response) {
            is DataState.Error -> {
                updateState {
                    it.copy(dialogState = TransferProcessState.DialogState.Error(response.message))
                }
            }
            DataState.Loading -> TransferProcessState.DialogState.Loading
            is DataState.Success -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(
                    TransferProcessEvent.ShowToast(
                        message + "with ID ${response.data.resourceId}",
                    ),
                )
                viewModelScope.launch {
                    delay(1500)
                    sendEvent(
                        TransferProcessEvent.TransferSuccess(
                            TransferSuccessDestination.HOME,
                        ),
                    )
                }
            }
        }
    }
}

@Parcelize
data class TransferProcessState(
    val isOnline: Boolean = false,
    val transferPayloadString: String? = null,
    @IgnoredOnParcel
    val transferDestination: TransferSuccessDestination? = null,
    @IgnoredOnParcel
    val transferType: TransferType? = null,
    @IgnoredOnParcel
    val transferPayload: TransferPayload? = null,
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface TransferProcessEvent {
    data class ShowToast(val message: String) : TransferProcessEvent
    data class TransferSuccess(val destination: TransferSuccessDestination) : TransferProcessEvent
    data object Navigate : TransferProcessEvent
}

sealed interface TransferProcessAction {
    data object MakeTransfer : TransferProcessAction
    data object OnNavigate : TransferProcessAction
}

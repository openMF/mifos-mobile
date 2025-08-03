/*
 * Copyright 2024 Mifos Initiative
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.transferred_successfully
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.DateHelper.currentDate
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class TransferProcessViewModel(
    private val transferRepository: TransferRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<TransferProcessState, TransferProcessEvent, TransferProcessAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<TransferProcessRoute>()
        val transferDate = listOf(
            currentDate.dayOfMonth,
            currentDate.monthNumber,
            currentDate.year,
        )
        TransferProcessState(
            transferDestination = enumValueOf<TransferSuccessDestination>(route.transferSuccessDestination),
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
                transferAmount = route.transferAmount,
                transferDescription = route.transferDescription,
                dateFormat = "dd MMMM yyyy",
                locale = "en",
            ),
            dialogState = null,
        )
    },
) {

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
                    sendEvent(
                        TransferProcessEvent.ShowToast(
                            "${e.message}",
                        ),
                    )
                }
            }
        }
    }

    private fun processTransferResult(
        response: DataState<String>,
        message: String,
    ) {
        when (response) {
            is DataState.Error -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(
                    TransferProcessEvent.ShowToast(
                        response.message,
                    ),
                )
            }
            DataState.Loading -> TransferProcessState.DialogState.Loading
            is DataState.Success -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(
                    TransferProcessEvent.ShowToast(
                        message + "with ID ${response.data}",
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

data class TransferProcessState(
    val transferDestination: TransferSuccessDestination? = null,
    val transferType: TransferType? = null,
    val transferPayload: TransferPayload? = null,
    val dialogState: DialogState?,
)  {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState

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

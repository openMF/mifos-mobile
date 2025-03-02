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

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants.PAYLOAD
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.transfer.process.TransferProcessUiState.Initial
import javax.inject.Inject

@HiltViewModel
internal class TransferProcessViewModel @Inject constructor(
    private val transferRepositoryImp: TransferRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _transferUiState = MutableStateFlow<TransferProcessUiState>(Initial)
    val transferUiState = _transferUiState.asStateFlow()

    private val transferPayloadString = savedStateHandle.getStateFlow<String?>(
        key = PAYLOAD,
        initialValue = null,
    )
    private val transferType = savedStateHandle.getStateFlow<TransferType?>(
        key = TRANSFER_TYPE,
        initialValue = null,
    )

    val transferPayload: StateFlow<TransferPayload?> = transferPayloadString
        .map { jsonString ->
            Log.d("Tag-TransferProcessViewModel", "transferPayload: $jsonString")
            jsonString?.let {
                Gson().fromJson(it, TransferPayload::class.java)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    fun makeTransfer() {
        transferPayload.value?.let { payload ->
            viewModelScope.launch {
                _transferUiState.value = TransferProcessUiState.Loading
                transferRepositoryImp.makeTransfer(
                    fromOfficeId = payload.fromOfficeId,
                    fromClientId = payload.fromClientId,
                    fromAccountType = payload.fromAccountType,
                    fromAccountId = payload.fromAccountId,
                    toOfficeId = payload.toOfficeId,
                    toClientId = payload.toClientId,
                    toAccountType = payload.toAccountType,
                    toAccountId = payload.toAccountId,
                    transferDate = payload.transferDate,
                    transferAmount = payload.transferAmount,
                    transferDescription = payload.transferDescription,
                    dateFormat = payload.dateFormat,
                    locale = payload.locale,
//                    fromAccountNumber = payload.fromAccountNumber,
//                    toAccountNumber = payload.toAccountNumber,
                    transferType = transferType.value,
                ).catch { e ->
                    _transferUiState.value = TransferProcessUiState.Error(e.message)
                }.collect {
                    _transferUiState.value = TransferProcessUiState.Success
                }
            }
        }
    }
}

internal sealed class TransferProcessUiState {
    data object Initial : TransferProcessUiState()
    data object Loading : TransferProcessUiState()
    data object Success : TransferProcessUiState()
    data class Error(val errorMessage: String?) : TransferProcessUiState()
}

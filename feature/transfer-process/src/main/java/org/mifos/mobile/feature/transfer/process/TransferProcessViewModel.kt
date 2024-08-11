package org.mifos.mobile.feature.transfer.process

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.PAYLOAD
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.data.repositories.TransferRepository
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import javax.inject.Inject

@HiltViewModel
class TransferProcessViewModel @Inject constructor(
    private val transferRepositoryImp: TransferRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _transferUiState = MutableStateFlow<TransferProcessUiState>(TransferProcessUiState.Initial)
    val transferUiState: StateFlow<TransferProcessUiState> get() = _transferUiState

    private val transferPayloadString = savedStateHandle.getStateFlow<String?>(key = PAYLOAD, initialValue = null)
    private val transferType = savedStateHandle.getStateFlow<TransferType?>(key = TRANSFER_TYPE, initialValue = null)

    val transferPayload: StateFlow<TransferPayload?> = transferPayloadString
        .map { jsonString ->
            jsonString?.let {
                Gson().fromJson(it, TransferPayload::class.java)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
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
                    fromAccountNumber = payload.fromAccountNumber,
                    toAccountNumber = payload.toAccountNumber,
                    transferType = transferType.value
                ).catch { e ->
                    _transferUiState.value = TransferProcessUiState.Error(e.message)
                }.collect {
                    _transferUiState.value = TransferProcessUiState.Success
                }
            }
        }
    }
}

sealed class TransferProcessUiState {
    data object Initial : TransferProcessUiState()
    data object Loading : TransferProcessUiState()
    data object Success : TransferProcessUiState()
    data class Error(val errorMessage: String?) : TransferProcessUiState()
}

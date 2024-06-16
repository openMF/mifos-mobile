package org.mifos.mobile.ui.transfer_process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.repositories.TransferRepository
import org.mifos.mobile.ui.enums.TransferType
import javax.inject.Inject

@HiltViewModel
class TransferProcessViewModel @Inject constructor(private val transferRepositoryImp: TransferRepository) :
    ViewModel() {

    private val _transferUiState = MutableStateFlow<TransferProcessUiState>(TransferProcessUiState.Initial)
    val transferUiState: StateFlow<TransferProcessUiState> get() = _transferUiState

    private var _transferPayload: MutableStateFlow<TransferPayload?> = MutableStateFlow(null)
    val transferPayload: StateFlow<TransferPayload?> get() = _transferPayload

    private var _transferType: MutableStateFlow<TransferType?> = MutableStateFlow(null)
    val transferType: StateFlow<TransferType?> get() = _transferType

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

    fun setContent(payload: TransferPayload) {
        _transferPayload.value = payload
    }

    fun setTransferType(transferType: TransferType) {
        _transferType.value = transferType
    }
}

sealed class TransferProcessUiState {
    data object Initial : TransferProcessUiState()
    data object Loading : TransferProcessUiState()
    data object Success : TransferProcessUiState()
    data class Error(val errorMessage: String?) : TransferProcessUiState()
}

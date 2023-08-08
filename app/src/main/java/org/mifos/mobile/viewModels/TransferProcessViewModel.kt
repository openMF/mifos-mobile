package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.TransferRepository
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.utils.TransferUiState
import javax.inject.Inject

@HiltViewModel
class TransferProcessViewModel @Inject constructor(private val transferRepositoryImp: TransferRepository) :
    ViewModel() {

    private val _transferUiState = MutableLiveData<TransferUiState>()
    val transferUiState: LiveData<TransferUiState> get() = _transferUiState

    fun makeTransfer(
        fromOfficeId: Int?,
        fromClientId: Long?,
        fromAccountType: Int?,
        fromAccountId: Int?,
        toOfficeId: Int?,
        toClientId: Long?,
        toAccountType: Int?,
        toAccountId: Int?,
        transferDate: String?,
        transferAmount: Double?,
        transferDescription: String?,
        dateFormat: String = "dd MMMM yyyy",
        locale: String = "en",
        fromAccountNumber: String?,
        toAccountNumber: String?,
        transferType: TransferType?
    ) {
        viewModelScope.launch {
            _transferUiState.value = TransferUiState.Loading
            val response = transferRepositoryImp.makeTransfer(
                fromOfficeId,
                fromClientId,
                fromAccountType,
                fromAccountId,
                toOfficeId,
                toClientId,
                toAccountType,
                toAccountId,
                transferDate,
                transferAmount,
                transferDescription,
                dateFormat,
                locale,
                fromAccountNumber,
                toAccountNumber,
                transferType
            )
            try {
                if (response?.isSuccessful == true) {
                    _transferUiState.value = TransferUiState.TransferSuccess
                }
            } catch (e: Throwable) {
                _transferUiState.value = TransferUiState.Error(e)
            }
        }
    }
}
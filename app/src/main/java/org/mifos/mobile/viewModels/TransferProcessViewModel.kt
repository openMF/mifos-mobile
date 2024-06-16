//package org.mifos.mobile.viewModels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.launch
//import org.mifos.mobile.repositories.TransferRepository
//import org.mifos.mobile.ui.enums.TransferType
//import org.mifos.mobile.utils.TransferUiState
//import javax.inject.Inject
//
//@HiltViewModel
//class TransferProcessViewModel @Inject constructor(private val transferRepositoryImp: TransferRepository) :
//    ViewModel() {
//
//    private val _transferUiState = MutableStateFlow<TransferUiState>(TransferUiState.Initial)
//    val transferUiState: StateFlow<TransferUiState> get() = _transferUiState
//
//    fun makeTransfer(
//        fromOfficeId: Int?,
//        fromClientId: Long?,
//        fromAccountType: Int?,
//        fromAccountId: Int?,
//        toOfficeId: Int?,
//        toClientId: Long?,
//        toAccountType: Int?,
//        toAccountId: Int?,
//        transferDate: String?,
//        transferAmount: Double?,
//        transferDescription: String?,
//        dateFormat: String = "dd MMMM yyyy",
//        locale: String = "en",
//        fromAccountNumber: String?,
//        toAccountNumber: String?,
//        transferType: TransferType?
//    ) {
//        viewModelScope.launch {
//            _transferUiState.value = TransferUiState.Loading
//            transferRepositoryImp.makeTransfer(
//                fromOfficeId,
//                fromClientId,
//                fromAccountType,
//                fromAccountId,
//                toOfficeId,
//                toClientId,
//                toAccountType,
//                toAccountId,
//                transferDate,
//                transferAmount,
//                transferDescription,
//                dateFormat,
//                locale,
//                fromAccountNumber,
//                toAccountNumber,
//                transferType
//            ).catch { e ->
//                _transferUiState.value = TransferUiState.Error(e)
//            }.collect {
//                _transferUiState.value = TransferUiState.TransferSuccess
//            }
//        }
//    }
//}
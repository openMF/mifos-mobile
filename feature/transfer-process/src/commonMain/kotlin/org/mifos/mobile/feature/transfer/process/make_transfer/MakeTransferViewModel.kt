package org.mifos.mobile.feature.transfer.process.make_transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class MakeTransferViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
    private val accountsRepositoryImpl: AccountsRepository,
    userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<MakeTransferState, MakeTransferEvent, MakeTransferAction>(
    initialState = MakeTransferState(
        toAccounts = mapOf(
            "2345678" to "Savings Account"
        ),
        payFromAccounts = listOf(
            "267282972" to "$ 23,786.00",
            "6572992762" to "$ 123,786.00",
            "52682926" to "$ 78,786.00",
            "678292726" to "$ 923,786.00",
        )
    )
) {

    override fun handleAction(action: MakeTransferAction) {
        when (action) {
            is MakeTransferAction.OnToAccountSelected -> updateState {
                it.copy(selectedToAccount = action.accountNo)
            }

            is MakeTransferAction.OnFromAccountSelected -> updateState {
                it.copy(selectedFromAccount = action.account)
            }

            is MakeTransferAction.OnAmountChanged -> updateState {
                it.copy(amount = action.amount)
            }

            is MakeTransferAction.OnRemarksChanged -> updateState {
                it.copy(remarks = action.remarks)
            }

            MakeTransferAction.OnMakeTransferClicked -> {
                viewModelScope.launch {
                    sendAction(MakeTransferAction.Internal.PerformTransfer)
                }
            }

            MakeTransferAction.DismissDialog -> updateState {
                it.copy(dialogState = null)
            }

            is MakeTransferAction.Internal.PerformTransfer -> performTransfer()

            is MakeTransferAction.Internal.ReceiveTransferResult -> {
                handleTransferResult(action.dataState)
            }

            MakeTransferAction.NavigateBack -> {
                sendEvent(MakeTransferEvent.NavigateBack)
            }
        }
    }

    private fun updateState(update: (MakeTransferState) -> MakeTransferState) {
        mutableStateFlow.update(update)
    }

    private fun performTransfer() {
        updateState { it.copy(dialogState = MakeTransferState.DialogState.Loading) }

        viewModelScope.launch {
//            val result = transferRepository.makeTransfer(
//                fromAccount = state.selectedFromAccount?.first.orEmpty(),
//                toAccount = state.selectedToAccount,
//                amount = state.amount,
//                remarks = state.remarks
//            )

            sendAction(MakeTransferAction.Internal.ReceiveTransferResult(DataState.Success("")))
        }
    }

    private fun handleTransferResult(dataState: DataState<String>) {
        when (dataState) {
            is DataState.Loading -> updateState {
                it.copy(dialogState = MakeTransferState.DialogState.Loading)
            }

            is DataState.Success -> {
                sendEvent(MakeTransferEvent.NavigateToStatus)
            }

            is DataState.Error -> updateState {
                it.copy(dialogState = MakeTransferState.DialogState.Error(dataState.message))
            }
        }
    }
}

internal data class MakeTransferState(
    val toAccounts: Map<String, String> = emptyMap(),
    val selectedToAccount: String = "hello",
    val payFromAccounts: List<Pair<String, String>> = emptyList(),
    val selectedFromAccount: String? = null,
    val amount: String = "",
    val amountError:String = "",
    val remarks: String = "",
    val dialogState: DialogState? = null
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
    }
}

internal sealed interface MakeTransferAction {
    data class OnToAccountSelected(val accountNo: String) : MakeTransferAction
    data class OnFromAccountSelected(val account: String) : MakeTransferAction
    data class OnAmountChanged(val amount: String) : MakeTransferAction
    data class OnRemarksChanged(val remarks: String) : MakeTransferAction
    data object OnMakeTransferClicked : MakeTransferAction
    data object DismissDialog : MakeTransferAction
    data object NavigateBack : MakeTransferAction

    sealed interface Internal : MakeTransferAction {
        data object PerformTransfer : Internal
        data class ReceiveTransferResult(val dataState: DataState<String>) : Internal
    }
}

internal sealed interface MakeTransferEvent {
    data object NavigateBack : MakeTransferEvent
    data object NavigateToStatus : MakeTransferEvent
}
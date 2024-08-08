package org.mifos.mobile.feature.savings.savings_make_transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.data.repositories.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.core.network.Result
import javax.inject.Inject


@HiltViewModel
class SavingsMakeTransferViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val accountId = savedStateHandle.getStateFlow(key = Constants.ACCOUNT_ID, initialValue = -1L)

    private val transferType: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Constants.TRANSFER_TYPE,
        initialValue = TRANSFER_PAY_TO
    )

    private val outstandingBalance: StateFlow<Double?> = savedStateHandle.getStateFlow<String?>(
        key = Constants.OUTSTANDING_BALANCE,
        initialValue = null
    ).map { balanceString ->
        balanceString?.toDoubleOrNull() ?: 0.0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0.0,
    )

    private val _savingsMakeTransferUiData = MutableStateFlow(SavingsMakeTransferUiData())
    val savingsMakeTransferUiData: StateFlow<SavingsMakeTransferUiData> get() = _savingsMakeTransferUiData

    val savingsMakeTransferUiState = accountId
        .flatMapLatest { id ->
            savingsAccountRepositoryImp.accountTransferTemplate(accountId = id, accountType = 2L)
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> SavingsMakeTransferUiState.ShowUI
                    .also {
                        _savingsMakeTransferUiData.value = _savingsMakeTransferUiData.value
                            .copy(
                                accountOptionsTemplate = result.data,
                                transferType = transferType.value,
                                outstandingBalance = if(outstandingBalance.value == 0.0) null else outstandingBalance.value,
                                accountId = accountId.value
                            )
                    }
                is Result.Loading -> SavingsMakeTransferUiState.Loading
                is Result.Error -> SavingsMakeTransferUiState.Error(result.exception.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavingsMakeTransferUiState.Loading,
        )

}

sealed class SavingsMakeTransferUiState {
    data object Loading : SavingsMakeTransferUiState()
    data class Error(val errorMessage: String?) : SavingsMakeTransferUiState()
    data object ShowUI : SavingsMakeTransferUiState()
}

data class SavingsMakeTransferUiData(
    var accountId: Long? = null,
    var transferType: String? = null,
    var outstandingBalance: Double? = null,
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var toAccountOptionPrefilled: AccountOption? = null,
    var fromAccountOptionPrefilled: AccountOption? = null
)

data class ReviewTransferPayload(
    var payToAccount: AccountOption? = null,
    var payFromAccount: AccountOption? = null,
    var amount: String = "",
    var review: String = ""
)
/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.makeTransfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.error_description
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class MakeTransferViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
    private val accountsRepositoryImpl: AccountsRepository,
    userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<MakeTransferState, MakeTransferEvent, MakeTransferAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<MakeTransferRoute>()
        MakeTransferState(
            accountId = route.accountId,
            outstandingBalance = route.outstandingBalance,
            transferTarget = if (route.transferTarget != null) {
                enumValueOf<TransferType>(route.transferTarget)
            } else {
                null
            },
            transferType = route.transferType,
            transferSuccessDestination = if (route.transferSuccessDestination != null) {
                enumValueOf<TransferSuccessDestination>(route.transferSuccessDestination)
            } else {
                null
            },
        )
    },
) {

    init {
        fetchAccountOptions()
    }

    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)
    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    override fun handleAction(action: MakeTransferAction) {
        when (action) {
            is MakeTransferAction.OnToAccountSelected -> {
                val accountNo = action.accountNo
                val toAccountSelected = state.accountOptionsTemplate.toAccountOptions
                    .find { it.accountNo == accountNo }
                val fromAccounts = state.accountOptionsTemplate.fromAccountOptions.filter {
                    it.accountNo != accountNo
                }
                updateState {
                    it.copy(
                        toAccount = toAccountSelected,
                        fromAccountOptions = fromAccounts,
                    )
                }
            }

            is MakeTransferAction.OnFromAccountSelected -> {
                val accountNo = action.accountNo
                val fromAccountSelected = state.accountOptionsTemplate.fromAccountOptions
                    .find { it.accountNo == accountNo }
                val toAccounts = state.accountOptionsTemplate.toAccountOptions.filter {
                    it.accountNo != accountNo
                }
                updateState {
                    it.copy(
                        fromAccount = fromAccountSelected,
                        toAccountOptions = toAccounts,
                    )
                }
            }

            is MakeTransferAction.OnAmountChanged -> updateState {
                it.copy(amount = action.amount)
            }

            is MakeTransferAction.OnRemarksChanged -> updateState {
                it.copy(remarks = action.remarks)
            }

            MakeTransferAction.OnMakeTransferClicked -> {
                val isError = state.amount.any {
                    !it.isDigit()
                }
                updateState {
                    it.copy(amountError = isError)
                }
                if (!isError) {
                    viewModelScope.launch {
                        sendAction(MakeTransferAction.Internal.PerformTransfer)
                    }
                }
            }

            MakeTransferAction.DismissDialog -> updateState {
                it.copy(dialogState = null)
            }

            is MakeTransferAction.Internal.PerformTransfer -> {
                val payload = ReviewTransferPayload(
                    payToAccount = state.toAccount,
                    payFromAccount = state.fromAccount,
                    amount = state.amount,
                    review = state.remarks,
                )
                sendEvent(MakeTransferEvent.NavigateToTransferScreen(payload))
            }

            is MakeTransferAction.Internal.ReceiveAccountOptionsTemplateResult -> {
                handleTransferResult(action.dataState)
            }

            MakeTransferAction.NavigateBack -> {
                sendEvent(MakeTransferEvent.NavigateBack)
            }

            MakeTransferAction.OnRetry -> {
                fetchAccountOptions()
            }
        }
    }

    private fun fetchActiveAccount() {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = clientId,
                accountType = Constants.LOAN_ACCOUNTS,
            ).collect { result ->
                when (result) {
                    is DataState.Success -> {
                        val activeAccount = result.data.loanAccounts.firstOrNull { it.status?.active == true }
                        activeAccount?.let { activeAccount ->
                            updateState {
                                state.copy(
                                    accountId = activeAccount.id,
                                )
                            }
                        }
                    }

                    is DataState.Error -> {
                        updateState {
                            it.copy(
                                accountId = -1L,
                            )
                        }
                    }

                    DataState.Loading -> {}
                }
            }
        }
    }

    private fun updateState(update: (MakeTransferState) -> MakeTransferState) {
        mutableStateFlow.update(update)
    }

    private fun fetchAccountOptions() {
        if (state.accountId == -1L) {
            fetchActiveAccount()
        } else {
            viewModelScope.launch {
                savingsAccountRepositoryImp
                    .accountTransferTemplate(accountId = state.accountId, accountType = 2L)
                    .collect { result ->
                        sendAction(MakeTransferAction.Internal.ReceiveAccountOptionsTemplateResult(result))
                    }
            }
        }
    }

    private fun handleTransferResult(dataState: DataState<AccountOptionsTemplate>) {
        when (dataState) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        dialogState = MakeTransferState.DialogState.Error(dataState.message),
                    )
                }
            }
            DataState.Loading -> {
                updateState {
                    it.copy(
                        dialogState = MakeTransferState.DialogState.Loading,
                    )
                }
            }
            is DataState.Success -> {
                updateState {
                    it.copy(
                        accountOptionsTemplate = dataState.data,
                        fromAccountOptions = dataState.data.fromAccountOptions,
                        toAccountOptions = dataState.data.toAccountOptions,
                        dialogState = null,
                    )
                }
            }
        }
    }
}

internal data class MakeTransferState(
    val accountId: Long = -1L,
    val outstandingBalance: Double? = null,
    val transferType: String? = null,
    val transferTarget: TransferType? = null,
    val transferSuccessDestination: TransferSuccessDestination? = null,
    val amount: String = "",
    val amountError: Boolean = false,
    val remarks: String = "",
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var fromAccountOptions: List<AccountOption> = emptyList(),
    var toAccountOptions: List<AccountOption> = emptyList(),
    val fromAccount: AccountOption? = null,
    val toAccount: AccountOption? = null,
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
    }

    val isEnabled: Boolean = fromAccount != null && toAccount != null && amount.isNotBlank()
}

internal sealed interface MakeTransferAction {
    data class OnToAccountSelected(val accountNo: String) : MakeTransferAction
    data class OnFromAccountSelected(val accountNo: String) : MakeTransferAction
    data class OnAmountChanged(val amount: String) : MakeTransferAction
    data class OnRemarksChanged(val remarks: String) : MakeTransferAction
    data object OnMakeTransferClicked : MakeTransferAction
    data object DismissDialog : MakeTransferAction
    data object NavigateBack : MakeTransferAction
    data object OnRetry : MakeTransferAction

    sealed interface Internal : MakeTransferAction {
        data object PerformTransfer : Internal
        data class ReceiveAccountOptionsTemplateResult(val dataState: DataState<AccountOptionsTemplate>) : Internal
    }
}

internal sealed interface MakeTransferEvent {
    data object NavigateBack : MakeTransferEvent
    data class NavigateToTransferScreen(val reviewTransferPayload: ReviewTransferPayload) : MakeTransferEvent
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class LoanAccountTransactionViewModel(
    private val loanRepositoryImp: LoanRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<
    LoanAccountTransactionState,
    LoanAccountTransactionEvent,
    LoanAccountTransactionAction,
    >(
    initialState = LoanAccountTransactionState(
        dialogState = null,
        loanId = savedStateHandle.getStateFlow<Long?>(
            key = Constants.LOAN_ID,
            initialValue = null,
        ).value,
    ),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
            }
        }
        getLoanTransactionResult()
    }

    private fun updateState(update: (LoanAccountTransactionState) -> LoanAccountTransactionState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: LoanAccountTransactionAction) {
        when (action) {
            LoanAccountTransactionAction.BackPress -> sendEvent(LoanAccountTransactionEvent.NavigateBack)
            is LoanAccountTransactionAction.Internal.ReceiveLoanTransactionResult -> {
                handleLoanTransactionResult(action)
            }
        }
    }

    private fun getLoanTransactionResult() {
        updateState {
            it.copy(dialogState = LoanAccountTransactionState.DialogState.Loading)
        }
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                state
                    .loanId,
            ).catch { exception ->
                sendAction(
                    LoanAccountTransactionAction.Internal.ReceiveLoanTransactionResult(
                        DataState.Error(exception),
                    ),
                )
            }.collect { result ->
                sendAction(LoanAccountTransactionAction.Internal.ReceiveLoanTransactionResult(result))
            }
        }
    }

    private fun handleLoanTransactionResult(
        action: LoanAccountTransactionAction.Internal
            .ReceiveLoanTransactionResult,
    ) {
        updateState {
            when (action.loanAccountsTransactionResult) {
                is DataState.Error -> it.copy(
                    dialogState = LoanAccountTransactionState
                        .DialogState.Error(action.loanAccountsTransactionResult.message),
                )
                is DataState.Loading -> it.copy(
                    dialogState = LoanAccountTransactionState
                        .DialogState.Loading,
                )
                is DataState.Success -> it.copy(
                    loanWithAssociations = action
                        .loanAccountsTransactionResult.data,
                    dialogState = null,
                )
            }
        }
    }
}

@Parcelize
data class LoanAccountTransactionState(
    val loanId: Long? = null,
    val dialogState: DialogState?,
    val loanWithAssociations: LoanWithAssociations? = null,
    val isOnline: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface LoanAccountTransactionEvent {
    data object NavigateBack : LoanAccountTransactionEvent
}

sealed interface LoanAccountTransactionAction {
    data object BackPress : LoanAccountTransactionAction
    sealed class Internal : LoanAccountTransactionAction {
        data class ReceiveLoanTransactionResult(
            val loanAccountsTransactionResult: DataState<LoanWithAssociations?>,
        ) : Internal()
    }
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.accountTransactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class AccountsTransactionViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<AccountTransactionState, AccountTransactionEvent, AccountTransactionAction>(
    initialState = AccountTransactionState(dialogState = null),
) {
    init {
        loadTransactions()
    }

    override fun handleAction(action: AccountTransactionAction) {
        when (action) {
            AccountTransactionAction.DismissDialog -> handleDismissDialog()
            AccountTransactionAction.OnNavigateBackClick -> {
                sendEvent(AccountTransactionEvent.OnNavigateBack)
            }
            else -> {}
        }
    }

    private fun handleDismissDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = null,
            )
        }
    }

    fun loadTransactions() {
        val route = savedStateHandle.toRoute<AccountTransactionsNavRoute>()
        when (route.accountType) {
            Constants.SAVINGS_ACCOUNT -> loadSavingsWithAssociations(route.accountId)
            else -> {}
        }
    }

    fun loadSavingsWithAssociations(accountId: Long) {
        viewModelScope.launch {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                accountId,
                Constants.TRANSACTIONS,
            ).collect {
                    dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        mutableStateFlow.update {
                            it.copy(
                                dialogState = AccountTransactionState.DialogState.Error(dataState.message),
                            )
                        }
                    }

                    DataState.Loading -> {
                        mutableStateFlow.update {
                            it.copy(dialogState = AccountTransactionState.DialogState.Loading)
                        }
                    }

                    is DataState.Success -> {
                        val transactions = dataState.data.transactions
                        val groupedTransactions = transactions.groupBy { transaction ->
                            DateHelper.getFormattedDateWithPrefix(transaction.date)
                        }
                        mutableStateFlow.update {
                            it.copy(
                                dialogState = null,
                                data = groupedTransactions,
                            )
                        }
                    }
                }
            }
        }
    }
}

internal data class AccountTransactionState(
    val isRefreshing: Boolean = false,
    val data: Map<String, List<Transactions>> = emptyMap(),
    val dialogState: DialogState?,

) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
        data object Filters : DialogState
    }
}

internal sealed interface AccountTransactionAction {
    data object Refresh : AccountTransactionAction
    data object DismissDialog : AccountTransactionAction
    data object OnNavigateBackClick : AccountTransactionAction
    data object FilterClicked : AccountTransactionAction
}

sealed interface AccountTransactionEvent {
    data object OnNavigateBack : AccountTransactionEvent
}

internal fun getTransactionCreditStatus(transactionType: TransactionType?): Boolean {
    return transactionType?.run {
        when {
            deposit == true -> true
            dividendPayout == true -> false
            withdrawal == true -> false
            interestPosting == true -> true
            feeDeduction == true -> false
            initiateTransfer == true -> false
            approveTransfer == true -> false
            withdrawTransfer == true -> false
            rejectTransfer == true -> true
            overdraftFee == true -> false
            else -> true
        }
    } ?: false
}

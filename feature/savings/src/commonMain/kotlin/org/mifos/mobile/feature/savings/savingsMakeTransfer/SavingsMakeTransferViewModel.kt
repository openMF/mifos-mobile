/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsMakeTransfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate

internal class SavingsMakeTransferViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val accountId = savedStateHandle.getStateFlow(key = Constants.ACCOUNT_ID, initialValue = -1L)

    private val transferType: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Constants.TRANSFER_TYPE,
        initialValue = TRANSFER_PAY_TO,
    )

    val transferSuccessDestination: StateFlow<TransferSuccessDestination> = savedStateHandle.getStateFlow(
        key = Constants.TRANSFER_SUCCESS_DESTINATION,
        initialValue = TransferSuccessDestination.SAVINGS_ACCOUNT,
    )

    private val outstandingBalance: StateFlow<Double?> = savedStateHandle.getStateFlow<String?>(
        key = Constants.OUTSTANDING_BALANCE,
        initialValue = null,
    ).map { balanceString ->
        balanceString?.toDoubleOrNull() ?: 0.0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0.0,
    )

    private val _savingsMakeTransferUiData = MutableStateFlow(SavingsMakeTransferUiData())
    val savingsMakeTransferUiData: StateFlow<SavingsMakeTransferUiData> get() = _savingsMakeTransferUiData

    @OptIn(ExperimentalCoroutinesApi::class)
    val savingsMakeTransferUiState = accountId
        .flatMapLatest { id ->
            savingsAccountRepositoryImp.accountTransferTemplate(accountId = id, accountType = 2L)
        }
        .map { result ->
            when (result) {

                is DataState.Error -> SavingsMakeTransferUiState.Error(result.exception.message)
                DataState.Loading -> SavingsMakeTransferUiState.Loading
                is DataState.Success ->
                    SavingsMakeTransferUiState.ShowUI
                        .also {
                            _savingsMakeTransferUiData.value = _savingsMakeTransferUiData.value
                                .copy(
                                    accountOptionsTemplate = result.data,
                                    transferType = transferType.value,
                                    outstandingBalance = if (outstandingBalance.value == 0.0) {
                                        null
                                    } else {
                                        outstandingBalance.value
                                    },
                                    accountId = accountId.value,
                                )
                        }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavingsMakeTransferUiState.Loading,
        )
}

internal sealed class SavingsMakeTransferUiState {
    data object Loading : SavingsMakeTransferUiState()
    data class Error(val errorMessage: String?) : SavingsMakeTransferUiState()
    data object ShowUI : SavingsMakeTransferUiState()
}

internal data class SavingsMakeTransferUiData(
    var accountId: Long? = null,
    var transferType: String? = null,
    var outstandingBalance: Double? = null,
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var toAccountOptionPrefilled: AccountOption? = null,
    var fromAccountOptionPrefilled: AccountOption? = null,
)

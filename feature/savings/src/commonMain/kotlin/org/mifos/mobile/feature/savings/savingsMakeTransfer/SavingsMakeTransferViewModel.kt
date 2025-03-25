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
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.entity.TransferArgs
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.feature.savings.navigation.SAVINGS_MAKE_TRANSFER_ARGS

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

    private val transferArgsJson: StateFlow<String?> = savedStateHandle.getStateFlow(
        key = SAVINGS_MAKE_TRANSFER_ARGS,
        initialValue = null,
    )

    private val transferArgs: StateFlow<TransferArgs?> = transferArgsJson
        .map { json -> json?.let { TransferArgs.fromJson(it) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val transferPayload: StateFlow<AccountDetails?> = transferArgs
        .map { it?.transferPayload }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val accountId: StateFlow<Long> = transferPayload
        .map { it?.accountId ?: -1L }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = -1L,
        )

    private val transferType: StateFlow<String> = transferPayload
        .map { it?.transferType ?: TRANSFER_PAY_TO }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TRANSFER_PAY_TO,
        )

    val transferSuccessDestination: StateFlow<TransferSuccessDestination> = transferPayload
        .map { it?.transferSuccessDestination ?: TransferSuccessDestination.SAVINGS_ACCOUNT }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TransferSuccessDestination.SAVINGS_ACCOUNT,
        )

    private val outstandingBalance: StateFlow<Double?> = transferPayload
        .map { it?.outstandingBalance ?: 0.0 }
        .stateIn(
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

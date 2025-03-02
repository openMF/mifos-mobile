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

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.data.repository.ThirdPartyTransferRepository
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import javax.inject.Inject

@HiltViewModel
internal class SavingsMakeTransferViewModel @Inject constructor(
    private val transferRepository: ThirdPartyTransferRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val accountId = savedStateHandle.getStateFlow(key = Constants.ACCOUNT_ID, initialValue = -1L)

    private val transferType: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Constants.TRANSFER_TYPE,
        initialValue = TRANSFER_PAY_TO,
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

    private val _savingsMakeTransferUiState =
        MutableStateFlow<SavingsMakeTransferUiState>(SavingsMakeTransferUiState.Loading)
    val savingsMakeTransferUiState: StateFlow<SavingsMakeTransferUiState> get() = _savingsMakeTransferUiState

    init {
        fetchTemplate()
    }

    private fun fetchTemplate() {
        viewModelScope.launch {
            transferRepository.thirdPartyTransferTemplate()
                .asResult()
                .collect { result ->
                    _savingsMakeTransferUiState.value = when (result) {
                        is Result.Success -> {
                            Log.d(
                                "TAG-SavingsMakeTransferViewModel",
                                "savingsMakeTransferUiState: Success ${result.data}",
                            )
                            SavingsMakeTransferUiState.ShowUI(
                                data = SavingsMakeTransferUiData(
                                    accountId = accountId.value,
                                    transferType = transferType.value,
                                    outstandingBalance = if (outstandingBalance.value == 0.0) {
                                        null
                                    } else {
                                        outstandingBalance.value
                                    },
                                    fromAccountOptions = result.data.fromAccountOptions,
                                    toAccountOptions = result.data.toAccountOptions,
                                ),
                            )
                        }

                        is Result.Loading -> {
                            Log.d(
                                "TAG-SavingsMakeTransferViewModel",
                                "savingsMakeTransferUiState: Loading",
                            )
                            SavingsMakeTransferUiState.Loading
                        }

                        is Result.Error -> {
                            Log.d(
                                "TAG-SavingsMakeTransferViewModel",
                                "savingsMakeTransferUiState: Error",
                            )
                            SavingsMakeTransferUiState.Error(result.exception.message)
                        }
                    }
                }
        }
    }

    internal sealed class SavingsMakeTransferUiState {
        data object Loading : SavingsMakeTransferUiState()
        data class Error(val errorMessage: String?) : SavingsMakeTransferUiState()
        data class ShowUI(val data: SavingsMakeTransferUiData) : SavingsMakeTransferUiState()
    }

    internal data class SavingsMakeTransferUiData(
        var accountId: Long? = null,
        var transferType: String? = null,
        var outstandingBalance: Double? = null,
        var fromAccountOptions: List<AccountOption> = ArrayList(),
        var toAccountOptions: List<AccountOption> = ArrayList(),
        var toAccountOptionPrefilled: AccountOption? = null,
        var fromAccountOptionPrefilled: AccountOption? = null,
    )
}

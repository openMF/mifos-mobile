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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import javax.inject.Inject

@HiltViewModel
internal class SavingsMakeTransferViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
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

    private val _savingsMakeTransferUiData = MutableStateFlow(SavingsMakeTransferUiData())
    val savingsMakeTransferUiData: StateFlow<SavingsMakeTransferUiData> get() = _savingsMakeTransferUiData

    val savingsMakeTransferUiState = accountId
        .flatMapLatest { id ->
            savingsAccountRepositoryImp.accountTransferTemplate(accountId = id, accountType = 2L)
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success ->
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

                is Result.Loading -> SavingsMakeTransferUiState.Loading
                is Result.Error -> SavingsMakeTransferUiState.Error(result.exception.message)
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

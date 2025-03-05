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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import javax.inject.Inject

@HiltViewModel
internal class SavingsMakeTransferViewModel @Inject constructor(
    savingsAccountRepositoryImp: SavingsAccountRepository,
//    thirdPartyTransferRepository: ThirdPartyTransferRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val accountId =
        savedStateHandle.getStateFlow(key = Constants.ACCOUNT_ID, initialValue = -1L)

    private val transferType: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Constants.TRANSFER_TYPE,
        initialValue = TRANSFER_PAY_TO,
    )

    private val transferTarget: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Constants.TRANSFER_TARGET,
        initialValue = TransferType.TPT.name,
    )

    private val outstandingBalance: StateFlow<Double?> = savedStateHandle.getStateFlow<String?>(
        key = Constants.OUTSTANDING_BALANCE,
        initialValue = null,
    ).map { balanceString ->
        balanceString?.toDoubleOrNull()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    val savingsMakeTransferUiState: StateFlow<SavingsMakeTransferUiState> =
        transferTarget.flatMapLatest { target ->
            savingsAccountRepositoryImp.accountTransferTemplate()
//            when (target) {
//                TransferType.TPT.name -> thirdPartyTransferRepository.thirdPartyTransferTemplate()
//                else -> savingsAccountRepositoryImp.accountTransferTemplate()
//            }
        }
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        SavingsMakeTransferUiState.ShowUI(
                            data = SavingsMakeTransferUiData(
                                accountId = accountId.value,
                                transferType = transferType.value,
                                outstandingBalance = outstandingBalance.value,
                                fromAccountOptions = result.data.fromAccountOptions,
                                toAccountOptions = result.data.toAccountOptions,
//                                fromAccountOptionPrefilled = result.data.fromAccountOptions.find {
//                                    accountId.value == it.accountId?.toLong()
//                                },
                                toAccountOptionPrefilled = result.data.toAccountOptions.find {
                                    accountId.value == it.accountId?.toLong()
                                },
                            ),
                        )
                    }

                    is Result.Loading -> {
                        SavingsMakeTransferUiState.Loading
                    }

                    is Result.Error -> {
                        SavingsMakeTransferUiState.Error(result.exception.message)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SavingsMakeTransferUiState.Loading,
            )
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

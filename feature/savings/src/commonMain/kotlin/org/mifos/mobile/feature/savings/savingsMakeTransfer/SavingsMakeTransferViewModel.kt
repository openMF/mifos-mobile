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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.entity.TransferArgs
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.savings.navigation.SAVINGS_MAKE_TRANSFER_ARGS

internal class SavingsMakeTransferViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
    private val accountsRepositoryImpl: AccountsRepository,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)

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

    private val fallbackTransferPayload = MutableStateFlow(
        AccountDetails(
            accountId = -1L,
            transferType = TRANSFER_PAY_TO,
            transferTarget = TransferType.SELF,
            transferSuccessDestination = TransferSuccessDestination.HOME,
        ),
    )

    private val transferArgs: StateFlow<TransferArgs?> = transferArgsJson
        .map { json ->
            json?.let {
                try {
                    TransferArgs.fromJson(it)
                } catch (e: Exception) {
                    handleNoTransferArgs()
                    null
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val transferPayload: StateFlow<AccountDetails> = transferArgs
        .map { it?.transferPayload ?: fallbackTransferPayload.value }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = fallbackTransferPayload.value,
        )

    private val _accountId = MutableStateFlow(
        transferPayload.value.accountId.takeIf { it != -1L } ?: -1L,
    )
    private val accountId: StateFlow<Long> = _accountId.asStateFlow()

    val transferSuccessDestination: StateFlow<TransferSuccessDestination> = transferPayload
        .map { it.transferSuccessDestination }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = fallbackTransferPayload.value.transferSuccessDestination,
        )

    private val outstandingBalance: StateFlow<Double?> = transferPayload
        .map { it.outstandingBalance }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    @Suppress("ktlint:standard:property-naming")
    private val _savingsMakeTransferUiData = MutableStateFlow(SavingsMakeTransferUiData())
    val savingsMakeTransferUiData: StateFlow<SavingsMakeTransferUiData> get() = _savingsMakeTransferUiData

    @OptIn(ExperimentalCoroutinesApi::class)
    val savingsMakeTransferUiState = accountId
        .flatMapLatest { id ->
            if (id == -1L) {
                fetchActiveAccount()
                MutableStateFlow(SavingsMakeTransferUiState.Error("Fetching active account..."))
            } else {
                savingsAccountRepositoryImp.accountTransferTemplate(accountId = id, accountType = 2L)
                    .map { result ->
                        when (result) {
                            is DataState.Error -> SavingsMakeTransferUiState.Error(result.exception.message)
                            DataState.Loading -> SavingsMakeTransferUiState.Loading
                            is DataState.Success -> SavingsMakeTransferUiState.ShowUI.also {
                                _savingsMakeTransferUiData.value = _savingsMakeTransferUiData.value.copy(
                                    accountOptionsTemplate = result.data,
                                    transferType = transferPayload.value.transferType,
                                    outstandingBalance = outstandingBalance.value,
                                    accountId = accountId.value,
                                )
                            }
                        }
                    }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavingsMakeTransferUiState.Loading,
        )

    private fun fetchActiveAccount() {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = clientId,
                accountType = Constants.LOAN_ACCOUNTS,
            ).collect { result ->
                when (result) {
                    is DataState.Success -> {
                        val activeAccount = result.data.loanAccounts.firstOrNull { it.status?.active == true }
                        activeAccount?.let {
                            _accountId.value = it.id
                        }
                    }

                    is DataState.Error -> {
                        _accountId.value = -1L
                    }

                    DataState.Loading -> {}
                }
            }
        }
    }

    private fun handleNoTransferArgs() {
        fallbackTransferPayload.value = AccountDetails(
            accountId = -1L,
            transferType = TRANSFER_PAY_TO,
            transferTarget = TransferType.SELF,
            transferSuccessDestination = TransferSuccessDestination.HOME,
        )
    }
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

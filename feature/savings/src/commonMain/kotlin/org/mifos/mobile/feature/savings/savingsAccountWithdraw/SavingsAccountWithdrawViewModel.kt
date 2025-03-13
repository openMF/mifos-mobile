/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountWithdraw

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.feature.savings.savingsAccountWithdraw.SavingsAccountWithdrawUiState.WithdrawUiReady

internal class SavingsAccountWithdrawViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mUiState = MutableStateFlow<SavingsAccountWithdrawUiState>(WithdrawUiReady)
    val uiState = mUiState.asStateFlow()

    private val mSnackbar = MutableStateFlow<String>("")
    val snackbarMessage = mSnackbar.asStateFlow()

    private val savingsId: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = Constants.SAVINGS_ID,
        initialValue = -1L,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val savingsWithAssociations = savingsId
        .flatMapLatest {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId.value,
                Constants.TRANSACTIONS,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun submitWithdrawSavingsAccount(remark: String) {
        val payload = SavingsAccountWithdrawPayload(
            note = remark,
            withdrawnOnDate = DateHelper.formattedFullDate,
        )

        viewModelScope.launch {
            mUiState.value = SavingsAccountWithdrawUiState.Loading
            val response = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                savingsWithAssociations.value?.data?.accountNo,
                payload,
            )
            when (response) {
                is DataState.Error -> {
                    mUiState.value =
                        SavingsAccountWithdrawUiState.Error(response.message)
                }
                DataState.Loading -> {
                    SavingsAccountWithdrawUiState.Loading
                }
                is DataState.Success -> {
                    mUiState.value = SavingsAccountWithdrawUiState.Success
                }
            }
        }
    }
}

internal sealed interface SavingsAccountWithdrawUiState {
    data object WithdrawUiReady : SavingsAccountWithdrawUiState
    data object Loading : SavingsAccountWithdrawUiState
    data object Success : SavingsAccountWithdrawUiState
    data class Error(val message: String?) : SavingsAccountWithdrawUiState
}

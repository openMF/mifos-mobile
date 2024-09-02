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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.feature.savings.savingsAccountWithdraw.SavingsAccountWithdrawUiState.WithdrawUiReady
import javax.inject.Inject

@HiltViewModel
internal class SavingsAccountWithdrawViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mUiState = MutableStateFlow<SavingsAccountWithdrawUiState>(WithdrawUiReady)
    val uiState = mUiState.asStateFlow()

    private val savingsId: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = Constants.SAVINGS_ID,
        initialValue = -1L,
    )

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
        val payload = SavingsAccountWithdrawPayload()
        payload.note = remark
        payload.withdrawnOnDate = getTodayFormatted()

        viewModelScope.launch {
            mUiState.value = SavingsAccountWithdrawUiState.Loading
            savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                savingsWithAssociations.value?.accountNo,
                payload,
            )
                .catch { e ->
                    mUiState.value =
                        SavingsAccountWithdrawUiState.Error(e.message)
                }.collect {
                    mUiState.value =
                        SavingsAccountWithdrawUiState.Success
                }
        }
    }
}

internal sealed class SavingsAccountWithdrawUiState {
    data object WithdrawUiReady : SavingsAccountWithdrawUiState()
    data object Loading : SavingsAccountWithdrawUiState()
    data object Success : SavingsAccountWithdrawUiState()
    data class Error(val message: String?) : SavingsAccountWithdrawUiState()
}

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.savings_account_withdraw_successful
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations

internal class SavingsAccountWithdrawViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mUiState = MutableStateFlow<SavingsAccountWithdrawUiState>(SavingsAccountWithdrawUiState.Loading)
    val uiState = mUiState.asStateFlow()

    private val savingsId: Long = savedStateHandle[Constants.SAVINGS_ID] ?: -1L

    private val _savingsWithAssociations = MutableStateFlow<SavingsWithAssociations?>(null)
    val savingsWithAssociations = _savingsWithAssociations.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SavingsAccountWithdrawUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchSavingsAccountWithAssociations()
    }

    private fun fetchSavingsAccountWithAssociations() {
        viewModelScope.launch {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId,
                Constants.TRANSACTIONS,
            ).collect { result ->
                when (result) {
                    is DataState.Success -> {
                        _savingsWithAssociations.value = result.data
                        mUiState.value = SavingsAccountWithdrawUiState.Ready
                    }
                    is DataState.Error -> {
                        _savingsWithAssociations.value = null
                        mUiState.value = SavingsAccountWithdrawUiState.Error
                    }
                    is DataState.Loading -> {
                        mUiState.value = SavingsAccountWithdrawUiState.Loading
                    }
                }
            }
        }
    }

    fun submitWithdrawSavingsAccount(remark: String) {
        val accountNo = savingsWithAssociations.value?.accountNo ?: return

        val payload = SavingsAccountWithdrawPayload(
            note = remark,
            withdrawnOnDate = DateHelper.formattedFullDate,
            dateFormat = "dd MMMM yyyy",
            locale = "en",
        )

        viewModelScope.launch {
            mUiState.value = SavingsAccountWithdrawUiState.Loading
            when (val response = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(accountNo, payload)) {
                is DataState.Success -> {
                    val message = getString(Res.string.savings_account_withdraw_successful)
                    _eventFlow.emit(SavingsAccountWithdrawUiEvent.ShowSnackbar(message))
                    delay(1500)
                    _eventFlow.emit(SavingsAccountWithdrawUiEvent.NavigateBack(true))
                }
                is DataState.Error -> mUiState.value = SavingsAccountWithdrawUiState.Message(response.message)
                is DataState.Loading -> mUiState.value = SavingsAccountWithdrawUiState.Loading
            }
        }
    }
}

internal sealed interface SavingsAccountWithdrawUiState {
    data object Ready : SavingsAccountWithdrawUiState
    data object Loading : SavingsAccountWithdrawUiState
    data object Success : SavingsAccountWithdrawUiState
    data object Error : SavingsAccountWithdrawUiState
    data class Message(val message: String?) : SavingsAccountWithdrawUiState
}

internal sealed interface SavingsAccountWithdrawUiEvent {
    data class ShowSnackbar(val message: String) : SavingsAccountWithdrawUiEvent
    data class NavigateBack(val withDrawSuccess: Boolean) : SavingsAccountWithdrawUiEvent
}

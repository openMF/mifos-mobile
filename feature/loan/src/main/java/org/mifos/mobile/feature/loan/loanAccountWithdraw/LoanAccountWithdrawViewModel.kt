/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountWithdraw

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.feature.loan.R
import javax.inject.Inject

@HiltViewModel
internal class LoanAccountWithdrawViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _loanUiState =
        MutableStateFlow<LoanAccountWithdrawUiState>(LoanAccountWithdrawUiState.WithdrawUiReady)
    val loanUiState: StateFlow<LoanAccountWithdrawUiState> = _loanUiState

    val loanId = savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)

    val loanWithAssociations: StateFlow<LoanWithAssociations?> = loanId
        .flatMapLatest {
            loanRepositoryImp.getLoanWithAssociations(Constants.TRANSACTIONS, it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    fun withdrawLoanAccount(loanReason: String?) {
        val loanWithdraw = LoanWithdraw().apply {
            note = loanReason
            withdrawnOnDate = DateHelper
                .getDateAsStringFromLong(System.currentTimeMillis())
        }

        viewModelScope.launch {
            _loanUiState.value = LoanAccountWithdrawUiState.Loading
            loanRepositoryImp.withdrawLoanAccount(
                loanWithAssociations.value?.id?.toLong(),
                loanWithdraw,
            )
                ?.catch {
                    _loanUiState.value =
                        LoanAccountWithdrawUiState.Error(R.string.error_loan_account_withdraw)
                }?.collect {
                    _loanUiState.value = LoanAccountWithdrawUiState.Success
                }
        }
    }
}

internal sealed class LoanAccountWithdrawUiState {
    data object WithdrawUiReady : LoanAccountWithdrawUiState()
    data object Loading : LoanAccountWithdrawUiState()
    data object Success : LoanAccountWithdrawUiState()
    data class Error(val messageId: Int) : LoanAccountWithdrawUiState()
}

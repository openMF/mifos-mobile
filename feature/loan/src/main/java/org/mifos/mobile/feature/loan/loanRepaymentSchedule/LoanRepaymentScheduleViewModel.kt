/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanRepaymentSchedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.feature.loan.R
import javax.inject.Inject

@HiltViewModel
internal class LoanRepaymentScheduleViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val loanId = savedStateHandle.getStateFlow<Long?>(
        key = Constants.LOAN_ID,
        null,
    )

    private val mOnRetry = MutableStateFlow(false)

    val loanUiState = loanId.combine(mOnRetry) { loanId, onRetry ->
        loanId to onRetry
    }.flatMapLatest {
        loanRepositoryImp.getLoanWithAssociations(
            Constants.REPAYMENT_SCHEDULE,
            loanId.value,
        )
    }.catch {
        LoanUiState.ShowError(R.string.repayment_schedule)
    }.map {
        if (it?.repaymentSchedule?.periods?.isNotEmpty() == true) {
            LoanUiState.ShowLoan(it)
        } else {
            it?.let { it1 -> LoanUiState.ShowEmpty(it1) }!!
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = LoanUiState.Loading,
        started = SharingStarted.WhileSubscribed(5000),
    )

    fun loanLoanWithAssociations() {
        mOnRetry.update { !it }
    }
}

internal sealed class LoanUiState {
    data object Loading : LoanUiState()
    data class ShowError(val message: Int) : LoanUiState()
    data class ShowLoan(val loanWithAssociations: LoanWithAssociations) : LoanUiState()
    data class ShowEmpty(val loanWithAssociations: LoanWithAssociations) : LoanUiState()
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountSummary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.feature.loan.loanAccountSummary.LoanAccountSummaryUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class LoanAccountSummaryViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val loanUiState = savedStateHandle.getStateFlow<Long?>(
        key = Constants.LOAN_ID,
        initialValue = null,
    ).flatMapLatest {
        loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, it)
    }.catch {
        LoanAccountSummaryUiState.Error
    }.map {
        LoanAccountSummaryUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(),
    )
}

internal sealed class LoanAccountSummaryUiState {
    data object Loading : LoanAccountSummaryUiState()
    data object Error : LoanAccountSummaryUiState()
    data class Success(val loanWithAssociations: LoanWithAssociations?) :
        LoanAccountSummaryUiState()
}

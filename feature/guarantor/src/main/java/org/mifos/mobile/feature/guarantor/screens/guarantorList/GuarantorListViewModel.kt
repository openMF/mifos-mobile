/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
internal class GuarantorListViewModel @Inject constructor(
    private val guarantorRepositoryImp: GuarantorRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _loanId = savedStateHandle.getStateFlow<String?>(key = LOAN_ID, initialValue = null)

    val loanId: StateFlow<Long> = _loanId
        .flatMapLatest { flowOf(it?.toLongOrNull() ?: -1L) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, -1L)

    val guarantorUiState = loanId
        .flatMapLatest { loanId ->
            guarantorRepositoryImp.getGuarantorList(loanId = loanId)
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> GuarantorListUiState.Success(result.data?.filter { it?.status == true })
                is Result.Loading -> GuarantorListUiState.Loading
                is Result.Error -> GuarantorListUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GuarantorListUiState.Loading,
        )
}

internal sealed class GuarantorListUiState {
    data object Loading : GuarantorListUiState()
    data object Error : GuarantorListUiState()
    data class Success(val list: List<GuarantorPayload?>?) : GuarantorListUiState()
}

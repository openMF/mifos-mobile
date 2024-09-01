/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.feature.guarantor.R
import org.mifos.mobile.feature.guarantor.screens.guarantorDetails.GuarantorDetailUiState.Loading
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
internal class GuarantorDetailViewModel @Inject constructor(
    private val guarantorRepositoryImp: GuarantorRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val index = savedStateHandle.getStateFlow(key = Constants.INDEX, initialValue = -1)
    val loanId = savedStateHandle.getStateFlow<Long>(key = Constants.LOAN_ID, initialValue = -1)

    private val _guarantorDeleteState = MutableStateFlow<GuarantorDetailUiState>(Loading)
    private val guarantorDeleteState: StateFlow<GuarantorDetailUiState> = _guarantorDeleteState

    private var guarantorItem = loanId
        .flatMapLatest { loanId ->
            guarantorRepositoryImp.getGuarantorList(loanId = loanId)
        }.asResult().map { result ->
            when (result) {
                is Result.Success -> GuarantorDetailUiState.ShowDetail(
                    guarantorItem = result.data?.filter { it?.status == true }
                        ?.get(index = index.value),
                )

                is Result.Loading -> Loading
                is Result.Error -> GuarantorDetailUiState.Error(result.exception.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(300),
            initialValue = Loading,
        )

    val guarantorUiState: StateFlow<GuarantorDetailUiState> = merge(
        guarantorItem,
        guarantorDeleteState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(300),
        initialValue = Loading,
    )

    fun deleteGuarantor(guarantorId: Long) {
        viewModelScope.launch {
            _guarantorDeleteState.value = Loading
            guarantorRepositoryImp.deleteGuarantor(
                loanId = loanId.value,
                guarantorId = guarantorId,
            ).catch { e ->
                _guarantorDeleteState.value = GuarantorDetailUiState.Error(e.message)
            }.collect { response ->
                _guarantorDeleteState.value =
                    GuarantorDetailUiState.GuarantorDeletedSuccessfully(R.string.guarantor_deleted_successfully)
            }
        }
    }
}

internal sealed class GuarantorDetailUiState {
    data class GuarantorDeletedSuccessfully(val messageResId: Int) : GuarantorDetailUiState()
    data class Error(val message: String?) : GuarantorDetailUiState()
    data class ShowDetail(val guarantorItem: GuarantorPayload?) : GuarantorDetailUiState()
    data object Loading : GuarantorDetailUiState()
}

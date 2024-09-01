/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorAdd

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
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.feature.guarantor.R
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
internal class AddGuarantorViewModel @Inject constructor(
    private val guarantorRepositoryImp: GuarantorRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val index = savedStateHandle.getStateFlow(key = Constants.INDEX, initialValue = -1)
    private val loanId = savedStateHandle.getStateFlow<Long>(
        key = Constants.LOAN_ID,
        initialValue = -1,
    )

    private val mGuarantorState = MutableStateFlow<GuarantorAddUiState>(GuarantorAddUiState.Loading)
    var guarantorItem: StateFlow<GuarantorPayload?> = MutableStateFlow(null)

    init {
        fetchGuarantorItem()
    }

    private val guarantorTemplate = loanId.flatMapLatest { id ->
        guarantorRepositoryImp.getGuarantorTemplate(id)
    }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> GuarantorAddUiState.Template(result.data)
                is Result.Loading -> GuarantorAddUiState.Loading
                is Result.Error -> GuarantorAddUiState.Error(result.exception.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GuarantorAddUiState.Loading,
        )

    val guarantorUiState = merge(guarantorTemplate, mGuarantorState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GuarantorAddUiState.Loading,
        )

    fun createGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {
            mGuarantorState.value = GuarantorAddUiState.Loading
            guarantorRepositoryImp.createGuarantor(payload = payload, loanId = loanId.value)
                .catch { e ->
                    mGuarantorState.value = GuarantorAddUiState.Error(e.message)
                }.collect {
                    mGuarantorState.value =
                        GuarantorAddUiState.Success(R.string.guarantor_created_successfully)
                }
        }
    }

    private fun fetchGuarantorItem() {
        if (index.value > -1) {
            guarantorItem = loanId
                .flatMapLatest { loanId ->
                    guarantorRepositoryImp.getGuarantorList(loanId = loanId)
                }.asResult().map { result ->
                    when (result) {
                        is Result.Success -> result.data?.filter { it?.status == true }
                            ?.get(index = index.value)

                        else -> null
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null,
                )
        }
    }

    fun updateGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {
            mGuarantorState.value = GuarantorAddUiState.Loading
            guarantorRepositoryImp.updateGuarantor(payload, loanId.value, guarantorItem.value?.id)
                .catch { e ->
                    mGuarantorState.value = GuarantorAddUiState.Error(e.message)
                }.collect {
                    mGuarantorState.value =
                        GuarantorAddUiState.Success(R.string.guarantor_updated_successfully)
                }
        }
    }
}

internal sealed class GuarantorAddUiState {
    data object Loading : GuarantorAddUiState()
    data class Error(val message: String?) : GuarantorAddUiState()
    data class Template(val guarantorTemplatePayload: GuarantorTemplatePayload?) :
        GuarantorAddUiState()

    data class Success(val messageResId: Int) : GuarantorAddUiState()
}

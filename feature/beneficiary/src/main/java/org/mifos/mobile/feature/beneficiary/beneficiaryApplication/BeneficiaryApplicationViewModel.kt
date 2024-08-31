/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplication

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants.BENEFICIARY_STATE
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.R
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.BeneficiaryApplicationUiState.Loading
import org.mifos.mobile.feature.beneficiary.navigation.BENEFICIARY_ID
import javax.inject.Inject

@HiltViewModel
internal class BeneficiaryApplicationViewModel @Inject constructor(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _beneficiaryUiState = MutableStateFlow<BeneficiaryApplicationUiState>(Loading)
    val beneficiaryUiState: StateFlow<BeneficiaryApplicationUiState> get() = _beneficiaryUiState

    private val beneficiaryId = savedStateHandle.getStateFlow<Int?>(
        key = BENEFICIARY_ID,
        initialValue = null,
    )

    val beneficiaryState = savedStateHandle.getStateFlow(
        key = BENEFICIARY_STATE,
        initialValue = BeneficiaryState.CREATE_QR,
    )

    val beneficiary: StateFlow<Beneficiary?> = beneficiaryId
        .flatMapLatest {
            beneficiaryRepositoryImp.beneficiaryList()
        }.map { beneficiaryList ->
            beneficiaryId.value?.let { beneficiaryId ->
                beneficiaryList.find { it.id == beneficiaryId }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    init {
        loadBeneficiaryTemplate()
    }

    fun loadBeneficiaryTemplate() {
        viewModelScope.launch {
            _beneficiaryUiState.value = Loading
            beneficiaryRepositoryImp.beneficiaryTemplate().catch {
                _beneficiaryUiState.value =
                    BeneficiaryApplicationUiState.Error(R.string.error_fetching_beneficiary_template)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Template(it)
            }
        }
    }

    fun submitBeneficiary(beneficiaryPayload: BeneficiaryPayload) {
        when (beneficiaryState.value) {
            BeneficiaryState.UPDATE -> updateBeneficiary(
                beneficiaryId = beneficiary.value?.id?.toLong(),
                payload = BeneficiaryUpdatePayload(
                    name = beneficiaryPayload.name,
                    transferLimit = beneficiaryPayload.transferLimit ?: 0F,
                ),
            )

            else -> createBeneficiary(payload = beneficiaryPayload)
        }
    }

    private fun createBeneficiary(payload: BeneficiaryPayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = Loading
            beneficiaryRepositoryImp.createBeneficiary(payload).catch {
                _beneficiaryUiState.value =
                    BeneficiaryApplicationUiState.Error(R.string.error_creating_beneficiary)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Success
            }
        }
    }

    private fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = Loading
            beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload).catch {
                _beneficiaryUiState.value =
                    BeneficiaryApplicationUiState.Error(R.string.error_updating_beneficiary)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Success
            }
        }
    }
}

internal sealed class BeneficiaryApplicationUiState {
    data class Template(val beneficiaryTemplate: BeneficiaryTemplate) :
        BeneficiaryApplicationUiState()

    data object Loading : BeneficiaryApplicationUiState()
    data object Success : BeneficiaryApplicationUiState()
    data class Error(val errorResId: Int) : BeneficiaryApplicationUiState()
}

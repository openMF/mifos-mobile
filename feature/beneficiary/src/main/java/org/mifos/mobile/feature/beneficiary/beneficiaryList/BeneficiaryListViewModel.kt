/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.feature.beneficiary.beneficiaryList.BeneficiaryListUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class BeneficiaryListViewModel @Inject constructor(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
) : ViewModel() {

    private val _beneficiaryListUiState = MutableStateFlow<BeneficiaryListUiState>(Loading)
    val beneficiaryListUiState: StateFlow<BeneficiaryListUiState> get() = _beneficiaryListUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    init {
        loadBeneficiaries()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            loadBeneficiaries()
        }
    }

    fun loadBeneficiaries() {
        viewModelScope.launch {
            _beneficiaryListUiState.value = Loading
            beneficiaryRepositoryImp.beneficiaryList().catch {
                _beneficiaryListUiState.value = BeneficiaryListUiState.Error(it.message)
            }.collect { beneficiaryList ->
                _beneficiaryListUiState.value = BeneficiaryListUiState.Success(beneficiaryList)
                _isRefreshing.emit(false)
            }
        }
    }
}

internal sealed class BeneficiaryListUiState {
    data object Loading : BeneficiaryListUiState()
    data class Error(val message: String?) : BeneficiaryListUiState()
    data class Success(val beneficiaries: List<Beneficiary>) : BeneficiaryListUiState()
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.repository.ThirdPartyTransferRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.feature.third.party.transfer.ThirdPartyTransferUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class ThirdPartyTransferViewModel @Inject constructor(
    private val transferRepository: ThirdPartyTransferRepository,
    private val beneficiaryRepository: BeneficiaryRepository,
) : ViewModel() {

    private val mUiState = MutableStateFlow<ThirdPartyTransferUiState>(Loading)
    val uiState = mUiState.asStateFlow()

    private val _thirdPartyTransferUiData = MutableStateFlow(ThirdPartyTransferUiData())
    val thirdPartyTransferUiData = _thirdPartyTransferUiData.asStateFlow()

    init {
        fetchTemplate()
    }

    private fun fetchTemplate() {
        viewModelScope.launch {
            combine(
                transferRepository.thirdPartyTransferTemplate(),
                beneficiaryRepository.beneficiaryList(),
            ) { templateResult, beneficiariesResult ->
                _thirdPartyTransferUiData.update {
                    it.copy(
                        fromAccountDetail = templateResult.fromAccountOptions,
                        toAccountOption = templateResult.toAccountOptions,
                        beneficiaries = beneficiariesResult,
                    )
                }
            }.catch {
                mUiState.value =
                    ThirdPartyTransferUiState.Error(errorMessage = it.message)
            }.collect {
                mUiState.value = ThirdPartyTransferUiState.ShowUI
            }
        }
    }
}

internal sealed class ThirdPartyTransferUiState {
    data object Loading : ThirdPartyTransferUiState()
    data class Error(val errorMessage: String?) : ThirdPartyTransferUiState()
    data object ShowUI : ThirdPartyTransferUiState()
}

internal data class ThirdPartyTransferUiData(
    val fromAccountDetail: List<AccountOption> = listOf(),
    val toAccountOption: List<AccountOption> = listOf(),
    val beneficiaries: List<Beneficiary> = listOf(),
)

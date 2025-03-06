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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.repository.ThirdPartyTransferRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.feature.third.party.transfer.ThirdPartyTransferUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class ThirdPartyTransferViewModel @Inject constructor(
    transferRepository: ThirdPartyTransferRepository,
    beneficiaryRepository: BeneficiaryRepository,
) : ViewModel() {

    //    in third part transfer is possible from savings to savings/loan
    //    cause of that we filter fromAccount only have saings.
    val uiState: StateFlow<ThirdPartyTransferUiState> =
        combine(
            transferRepository.thirdPartyTransferTemplate(),
            beneficiaryRepository.beneficiaryList(),
        ) { templateResult, beneficiariesResult ->
            ThirdPartyTransferUiState.ShowUI(
                ThirdPartyTransferUiData(
                    fromAccountDetail = templateResult.fromAccountOptions
                        .filter { it.accountType?.value == "Savings Account" },
                    toAccountOption = templateResult.toAccountOptions,
                    beneficiaries = beneficiariesResult,
                ),
            )
        }.catch {
            ThirdPartyTransferUiState.Error(errorMessage = it.message)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Loading,
        )
}

internal sealed class ThirdPartyTransferUiState {
    data object Loading : ThirdPartyTransferUiState()
    data class Error(val errorMessage: String?) : ThirdPartyTransferUiState()
    data class ShowUI(val data: ThirdPartyTransferUiData) : ThirdPartyTransferUiState()
}

internal data class ThirdPartyTransferUiData(
    val fromAccountDetail: List<AccountOption> = emptyList(),
    val toAccountOption: List<AccountOption> = emptyList(),
    val beneficiaries: List<Beneficiary> = emptyList(),
)

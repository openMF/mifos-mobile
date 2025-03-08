/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccount

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.active
import mifos_mobile.feature.savings.generated.resources.closed
import mifos_mobile.feature.savings.generated.resources.matured
import mifos_mobile.feature.savings.generated.resources.need_approval
import mifos_mobile.feature.savings.generated.resources.pending
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.designsystem.theme.Blue
import org.mifos.mobile.core.designsystem.theme.DepositGreen
import org.mifos.mobile.core.designsystem.theme.LightYellow
import org.mifos.mobile.core.designsystem.theme.RedLight
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status

internal class SavingAccountsDetailViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
//    private var preferencesHelper: UserPreferencesDataSource,
) : ViewModel() {

    val savingsId =
        savedStateHandle.getStateFlow<Long?>(key = Constants.SAVINGS_ID, initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val savingAccountsDetailUiState = savingsId
        .flatMapLatest {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId.value,
                Constants.TRANSACTIONS,
            )
        }
        .map { result ->
            when (result) {
                is DataState.Error -> SavingsAccountDetailUiState.Error
                DataState.Loading -> SavingsAccountDetailUiState.Loading
                is DataState.Success -> SavingsAccountDetailUiState.Success(result.data)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavingsAccountDetailUiState.Loading,
        )

//    fun getQrString(savingsWithAssociations: SavingsWithAssociations?): String {
//        return QrCodeGenerator.getAccountDetailsInString(
//            savingsWithAssociations?.accountNo,
//            preferencesHelper.officeName,
//            AccountType.SAVINGS,
//        )
//    }
}

internal sealed class SavingsAccountDetailUiState {
    data object Loading : SavingsAccountDetailUiState()
    data object Error : SavingsAccountDetailUiState()
    data class Success(val savingAccount: SavingsWithAssociations) : SavingsAccountDetailUiState()
}

internal fun Status.getStatusColorAndText(): Pair<Color, StringResource> {
    return when {
        this.active == true -> Pair(DepositGreen, Res.string.active)
        this.approved == true -> Pair(Blue, Res.string.need_approval)
        this.submittedAndPendingApproval == true -> Pair(LightYellow, Res.string.pending)
        this.matured == true -> Pair(RedLight, Res.string.matured)
        else -> Pair(Color.Black, Res.string.closed)
    }
}

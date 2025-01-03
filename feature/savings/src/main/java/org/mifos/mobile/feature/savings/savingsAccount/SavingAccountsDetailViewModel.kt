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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.designsystem.theme.Blue
import org.mifos.mobile.core.designsystem.theme.DepositGreen
import org.mifos.mobile.core.designsystem.theme.LightYellow
import org.mifos.mobile.core.designsystem.theme.RedLight
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.core.qr.QrCodeGenerator
import org.mifos.mobile.feature.savings.R
import javax.inject.Inject

@HiltViewModel
internal class SavingAccountsDetailViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    private var preferencesHelper: PreferencesHelper,
) : ViewModel() {

    val savingsId =
        savedStateHandle.getStateFlow<Long?>(key = Constants.SAVINGS_ID, initialValue = null)

    val savingAccountsDetailUiState = savingsId
        .flatMapLatest {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId.value,
                Constants.TRANSACTIONS,
            )
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> SavingsAccountDetailUiState.Success(result.data)
                is Result.Loading -> SavingsAccountDetailUiState.Loading
                is Result.Error -> SavingsAccountDetailUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavingsAccountDetailUiState.Loading,
        )

    fun getQrString(savingsWithAssociations: SavingsWithAssociations?): String {
        return QrCodeGenerator.getAccountDetailsInString(
            savingsWithAssociations?.accountNo,
            preferencesHelper.officeName,
            AccountType.SAVINGS,
        )
    }
}

internal sealed class SavingsAccountDetailUiState {
    data object Loading : SavingsAccountDetailUiState()
    data object Error : SavingsAccountDetailUiState()
    data class Success(val savingAccount: SavingsWithAssociations) : SavingsAccountDetailUiState()
}

internal fun Status.getStatusColorAndText(): Pair<Color, Int> {
    return when {
        this.active == true -> Pair(DepositGreen, R.string.active)
        this.approved == true -> Pair(Blue, R.string.need_approval)
        this.submittedAndPendingApproval == true -> Pair(LightYellow, R.string.pending)
        this.matured == true -> Pair(RedLight, R.string.matured)
        else -> Pair(Color.Black, R.string.closed)
    }
}

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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status

// TODO: getQrString should be implemented once QR module is finished
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
                is DataState.Success -> {
                    val account = result.data
                    if (account.status?.submittedAndPendingApproval == true) {
                        SavingsAccountDetailUiState.Empty
                    } else {
                        SavingsAccountDetailUiState.Success(account)
                    }
                }
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
    data object Empty : SavingsAccountDetailUiState()
    data class Success(val savingAccount: SavingsWithAssociations) : SavingsAccountDetailUiState()
}

@Composable
internal fun Status.getStatusColorAndText(): Pair<Color, StringResource> {
    return when {
        this.active == true ->
            Pair(MaterialTheme.colorScheme.primary, Res.string.active)
        this.approved == true ->
            Pair(MaterialTheme.colorScheme.secondaryContainer, Res.string.need_approval)
        this.submittedAndPendingApproval == true ->
            Pair(MaterialTheme.colorScheme.tertiaryContainer, Res.string.pending)
        this.matured == true ->
            Pair(MaterialTheme.colorScheme.errorContainer, Res.string.matured)
        else ->
            Pair(MaterialTheme.colorScheme.surfaceVariant, Res.string.closed)
    }
}

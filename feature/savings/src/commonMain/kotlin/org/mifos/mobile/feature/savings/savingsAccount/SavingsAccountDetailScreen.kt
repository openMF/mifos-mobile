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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.approval_pending
import mifos_mobile.feature.savings.generated.resources.ic_assignment_turned_in_black_24dp
import mifos_mobile.feature.savings.generated.resources.saving_account_details
import mifos_mobile.feature.savings.generated.resources.update_savings_account
import mifos_mobile.feature.savings.generated.resources.withdraw_savings_account
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosDropdownMenu
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay

@Composable
internal fun SavingsAccountDetailScreen(
    navigateBack: () -> Unit,
    updateSavingsAccount: (Long) -> Unit,
    withdrawSavingsAccount: (Long) -> Unit,
    makeTransfer: (Long) -> Unit,
    viewTransaction: (Long) -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (String) -> Unit,
    callUs: () -> Unit,
    deposit: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingAccountsDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.savingAccountsDetailUiState.collectAsStateWithLifecycle()
    val savingsId = viewModel.savingsId.collectAsStateWithLifecycle().value ?: -1L

    SavingsAccountDetailScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        modifier = modifier,
        updateSavingsAccount = { updateSavingsAccount(savingsId) },
        withdrawSavingsAccount = { withdrawSavingsAccount(savingsId) },
        makeTransfer = { makeTransfer(savingsId) },
        viewTransaction = { viewTransaction(savingsId) },
        viewCharges = viewCharges,
//        viewQrCode = { viewQrCode(viewModel.getQrString(null)) },  should be used once QR migration is done
        viewQrCode = { viewQrCode("") },
        callUs = callUs,
        deposit = { deposit(savingsId) },
    )
}

@Composable
private fun SavingsAccountDetailScreen(
    uiState: SavingsAccountDetailUiState,
    navigateBack: () -> Unit,
    updateSavingsAccount: () -> Unit,
    withdrawSavingsAccount: () -> Unit,
    makeTransfer: () -> Unit,
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (SavingsWithAssociations) -> Unit,
    callUs: () -> Unit,
    deposit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBar = {
            MifosTopAppBar(
                topBarTitle = stringResource(Res.string.saving_account_details),
                backPress = navigateBack,
                actions = {
                    MifosDropdownMenu(
                        menuItems = listOf(
                            stringResource(Res.string.update_savings_account) to updateSavingsAccount,
                            stringResource(Res.string.withdraw_savings_account) to withdrawSavingsAccount,
                        ),
                    )
                },
            )
        },
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (uiState) {
                is SavingsAccountDetailUiState.Error -> MifosErrorComponent()

                is SavingsAccountDetailUiState.Loading -> MifosProgressIndicatorOverlay()

                is SavingsAccountDetailUiState.Success -> {
                    SavingsAccountDetailContent(
                        savingsAccount = uiState.savingAccount,
                        makeTransfer = makeTransfer,
                        viewCharges = viewCharges,
                        viewTransaction = viewTransaction,
                        viewQrCode = viewQrCode,
                        callUs = callUs,
                        deposit = deposit,
                    )
                }

                SavingsAccountDetailUiState.Empty -> {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize(),
                        image = Res.drawable.ic_assignment_turned_in_black_24dp,
                        error = Res.string.approval_pending,
                    )
                }
            }
        }
    }
}

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.savings.R

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
    viewModel: SavingAccountsDetailViewModel = hiltViewModel(),
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
        viewQrCode = { viewQrCode(viewModel.getQrString(it)) },
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
            SavingsAccountDetailTopBar(
                navigateBack = navigateBack,
                updateSavingsAccount = updateSavingsAccount,
                withdrawSavingsAccount = withdrawSavingsAccount,
            )
        },
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (uiState) {
                is SavingsAccountDetailUiState.Error -> MifosErrorComponent()

                is SavingsAccountDetailUiState.Loading -> MifosProgressIndicatorOverlay()

                is SavingsAccountDetailUiState.Success -> {
                    if (uiState.savingAccount.status?.submittedAndPendingApproval == true) {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            icon = R.drawable.ic_assignment_turned_in_black_24dp,
                            error = R.string.approval_pending,
                        )
                    } else {
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
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun SavingsAccountDetailScreenPreview() {
    MifosMobileTheme {
        SavingsAccountDetailScreen(
            uiState = SavingsAccountDetailUiState.Loading,
            navigateBack = {},
            updateSavingsAccount = {},
            withdrawSavingsAccount = {},
            makeTransfer = {},
            viewTransaction = {},
            viewCharges = {},
            viewQrCode = {},
            callUs = {},
            deposit = {},
        )
    }
}

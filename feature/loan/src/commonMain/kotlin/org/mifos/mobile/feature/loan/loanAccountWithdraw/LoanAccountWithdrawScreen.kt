/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountWithdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_number
import mifos_mobile.feature.loan.generated.resources.client_name
import mifos_mobile.feature.loan.generated.resources.error_loan_account_withdraw
import mifos_mobile.feature.loan.generated.resources.withdraw_loan
import mifos_mobile.feature.loan.generated.resources.withdraw_loan_reason
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosTextField
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTitleDescSingleLineEqual
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LoanAccountWithdrawScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountWithdrawViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LoanAccountWithdrawEvent.NavigateBack -> navigateBack.invoke()
            is LoanAccountWithdrawEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LoanAccountWithdrawScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun LoanAccountWithDrawDialog(
    dialogState: LoanAccountWithdrawState.DialogState?,
) {
    when (dialogState) {
        is LoanAccountWithdrawState.DialogState.Loading -> {
            MifosProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
            )
        }

        is LoanAccountWithdrawState.DialogState.Error -> {
            MifosErrorComponent(message = stringResource(Res.string.error_loan_account_withdraw))
        }
        null -> Unit
    }
}

@Composable
private fun LoanAccountWithdrawScreen(
    state: LoanAccountWithdrawState,
    onAction: (LoanAccountWithdrawAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MifosTopAppBar(
            backPress = { onAction(LoanAccountWithdrawAction.BackPress) },
            topBarTitle = stringResource(Res.string.withdraw_loan),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f).padding(16.dp)) {
            LoanAccountWithdrawContent(
                loanWithAssociations = state.loanWithAssociations,
                state = state,
                onAction = onAction,
            )
        }
    }
    LoanAccountWithDrawDialog(
        dialogState = state.dialogState,
    )
}

@Composable
private fun LoanAccountWithdrawContent(
    loanWithAssociations: LoanWithAssociations?,
    state: LoanAccountWithdrawState,
    onAction: (LoanAccountWithdrawAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MifosTitleDescSingleLineEqual(
            title = stringResource(Res.string.client_name),
            description = loanWithAssociations?.clientName ?: "",
        )

        MifosTitleDescSingleLineEqual(
            title = stringResource(Res.string.account_number),
            description = loanWithAssociations?.accountNo ?: "",
        )

        MifosTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.loanReason,
            label = stringResource(Res.string.withdraw_loan_reason),
            onValueChange = { onAction(LoanAccountWithdrawAction.LoanReasonChanged(it)) },
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(LoanAccountWithdrawAction.WithDrawClicked) },
            content = {
                Text(
                    text = stringResource(Res.string.withdraw_loan),
                    style = MaterialTheme.typography.titleSmall,
                )
            },
        )
    }
}

@Preview
@Composable
private fun LoanAccountWithdrawPreview() {
    MifosMobileTheme {
        LoanAccountWithdrawScreen(
            state = LoanAccountWithdrawState(dialogState = null),
            modifier = Modifier,
            onAction = {},
        )
    }
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountWithdraw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_account_topbar_title
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_remarks_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_request_withdraw
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun AccountWithdrawScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: AccountWithdrawViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is AccountWithdrawEvent.NavigateBack -> navigateBack.invoke()
            is AccountWithdrawEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is AccountWithdrawEvent.NavigateToAuthenticate -> navigateToAuthenticateScreen.invoke()
        }
    }

    AccountWithdrawScreenContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountWithdrawDialog(
        dialogState = uiState.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun AccountWithdrawDialog(
    dialogState: AccountWithdrawState.DialogState?,
    onAction: (AccountWithdrawAction) -> Unit,
) {
    when (dialogState) {
        is AccountWithdrawState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = { onAction(AccountWithdrawAction.DismissDialog) },
        )

        is AccountWithdrawState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )

        null -> Unit
    }
}

@Composable
internal fun AccountWithdrawScreenContent(
    state: AccountWithdrawState,
    onAction: (AccountWithdrawAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountWithdrawAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_savings_withdraw_account_topbar_title),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.medium),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
        ) {
            MifosDetailsCard(
                keyValuePairs = state.details,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
            ) {
                MifosOutlinedTextField(
                    value = state.remark,
                    onValueChange = {
                        onAction(AccountWithdrawAction.RemarkChange(it))
                    },
                    label = stringResource(Res.string.feature_savings_withdraw_remarks_label),
                    shape = DesignToken.shapes.medium,
                    textStyle = MifosTypography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                    ),
                )
                MifosButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignToken.sizes.buttonHeight),
                    onClick = { onAction(AccountWithdrawAction.RequestWithdraw) },
                    text = {
                        Text(
                            text = stringResource(Res.string.feature_savings_withdraw_request_withdraw),
                            style = MifosTypography.titleMedium,
                        )
                    },

                    enabled = state.remark.isNotBlank(),
                    shape = DesignToken.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Account_Update_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(DesignToken.padding.large),
        ) {
            AccountWithdrawScreenContent(
                state = AccountWithdrawState(accountId = -1L, dialogState = null),
                onAction = {},
            )
        }
    }
}

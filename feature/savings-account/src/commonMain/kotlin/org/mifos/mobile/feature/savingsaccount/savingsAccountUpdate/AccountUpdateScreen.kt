/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_new_product_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_savings_product_empty
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_product_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_topbar_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * A stateful composable that serves as the entry point for the "Account Update" screen.
 *
 * This screen allows a user to modify certain details of a savings account, such as changing
 * the associated product. It connects to the [AccountUpdateViewModel] to manage state and
 * handle one-time events for navigation.
 *
 * @param navigateBack A lambda function to handle back navigation events.
 * @param navigateToStatusScreen A lambda to navigate to a generic status/result screen after
 *   the update operation is complete, passing along relevant details.
 * @param navigateToAuthenticateScreen A lambda to navigate to an authentication screen,
 *   typically required before performing a sensitive action.
 * @param viewModel The ViewModel responsible for this screen's logic and state.
 */
@Composable
internal fun AccountUpdateScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: AccountUpdateViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is AccountUpdateEvent.NavigateBack -> navigateBack.invoke()
            is AccountUpdateEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is AccountUpdateEvent.NavigateToAuthenticate -> navigateToAuthenticateScreen.invoke()
        }
    }

    AccountUpdateScreenContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountUpdateDialog(
        dialogState = uiState.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * A composable responsible for displaying dialogs based on the [AccountUpdateState.DialogState].
 *
 * It currently handles the display of a basic error dialog.
 *
 * @param dialogState The current dialog state from the [AccountUpdateState].
 * @param onAction A callback to send actions, such as dismissing the dialog, to the ViewModel.
 */
@Composable
internal fun AccountUpdateDialog(
    dialogState: AccountUpdateState.DialogState?,
    onAction: (AccountUpdateAction) -> Unit,
) {
    when (dialogState) {
        is AccountUpdateState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = { onAction(AccountUpdateAction.DismissDialog) },
        )

        null -> Unit
    }
}

/**
 * A stateless composable that renders the main UI for the "Account Update" screen.
 *
 * It conditionally displays content based on the [ScreenUiState] (e.g., loading, error, success).
 * In the success state, it shows account details, a dropdown for product selection, and a submit button.
 *
 * @param state The current [AccountUpdateState] to render.
 * @param onAction A callback to send user actions to the ViewModel.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
internal fun AccountUpdateScreenContent(
    state: AccountUpdateState,
    onAction: (AccountUpdateAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountUpdateAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_savings_update_topbar_title),
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
        when (state.uiState) {
            ScreenUiState.Empty -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(Res.string.feature_savings_savings_product_empty),
                    onRetry = { onAction(AccountUpdateAction.Retry) },
                )
            }

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(AccountUpdateAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(AccountUpdateAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
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
                        MifosOutlineDropdown(
                            selectedText = state.selectedProduct,
                            items = state.productOptions,
                            onItemSelected = { id, product ->
                                onAction(AccountUpdateAction.OnProductSelected(id, product))
                            },
                            label = stringResource(Res.string.feature_savings_update_product_label),
                        )

                        MifosButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DesignToken.sizes.buttonHeight),
                            onClick = { onAction(AccountUpdateAction.RequestUpdate) },
                            text = {
                                Text(
                                    text = stringResource(Res.string.feature_savings_new_product_label),
                                    style = MifosTypography.titleMedium,
                                )
                            },

                            enabled = state.selectedProductId != null,
                            shape = DesignToken.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        )
                    }
                }

                if (state.showOverlay) {
                    MifosProgressIndicatorOverlay()
                }
            }

            else -> { }
        }
    }
}

/**
 * A Jetpack Compose preview for the [AccountUpdateScreenContent].
 *
 * This provides a design-time visualization of the account update UI in Android Studio,
 * configured with a default initial state.
 */
@Preview
@Composable
private fun Account_Update_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(DesignToken.padding.large),
        ) {
            AccountUpdateScreenContent(
                state = AccountUpdateState(clientId = 1, accountId = -1L, dialogState = null),
                onAction = {},
            )
        }
    }
}

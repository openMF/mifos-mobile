/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.confirmDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_button_apply
import mifos_mobile.feature.savings_application.generated.resources.feature_savings_application_savings_confirmation_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme

/**
 * Entry point for the Savings Confirmation screen.
 * Orchestrates the ViewModel state, handles navigation side effects, and delegates UI rendering.
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToStatusScreen Callback to navigate to the final status/result screen.
 * @param navigateToAuthenticateScreen Callback to proceed to the authentication flow.
 * @param viewModel The view model managing the business logic for this screen.
 */
@Composable
internal fun SavingsConfirmDetailsScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: SavingsConfirmDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SavingsConfirmDetailsEvent.NavigateBack -> navigateBack.invoke()
            is SavingsConfirmDetailsEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is SavingsConfirmDetailsEvent.NavigateToAuthenticate -> navigateToAuthenticateScreen.invoke()
        }
    }

    SavingsConfirmDetailsScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    SavingsConfirmDetailsDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * Displays modal dialogs (such as error alerts) based on the current dialog state.
 *
 * @param dialogState The current state determining which dialog (if any) to show.
 * @param onAction Callback to handle dialog interactions, such as dismissing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SavingsConfirmDetailsDialog(
    dialogState: SavingsConfirmDetailsState.SavingsConfirmDetailsDialogState?,
    onAction: (SavingsConfirmDetailsAction) -> Unit,
) {
    when (dialogState) {
        is SavingsConfirmDetailsState.SavingsConfirmDetailsDialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(SavingsConfirmDetailsAction.DismissDialog) },
            )
        }

        null -> {}
    }
}

/**
 * Renders the visual layout for the confirmation screen, handling Loading, Error, and Success states.
 * Displays the savings summary card and the final confirmation button when data is successfully loaded.
 *
 * @param state The current UI state containing savings details and loading status.
 * @param onAction Callback to handle user intent (e.g., clicking 'Confirm' or 'Back').
 */
@Composable
internal fun SavingsConfirmDetailsScreenContent(
    state: SavingsConfirmDetailsState,
    onAction: (SavingsConfirmDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(SavingsConfirmDetailsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_savings_application_savings_confirmation_title),
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
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = false,
                    message = stringResource(state.uiState.message),
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = false,
                    isRetryEnabled = false,
                )
            }

            ScreenUiState.Success -> {
                Column(
                    modifier = modifier
                        .padding(KptTheme.spacing.md)
                        .padding(top = DesignToken.padding.medium)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraLarge),
                ) {
                    MifosDetailsCard(state.details)

                    MifosButton(
                        modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                        onClick = {
                            onAction(SavingsConfirmDetailsAction.NavigateToAuthenticate)
                        },
                        shape = KptTheme.shapes.medium,
                    ) {
                        Text(
                            text = stringResource(Res.string.feature_apply_savings_button_apply),
                            style = MifosTypography.titleMedium,
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

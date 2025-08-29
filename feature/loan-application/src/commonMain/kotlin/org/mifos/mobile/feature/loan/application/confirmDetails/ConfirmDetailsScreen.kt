/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.confirmDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_info_confirm_details
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun ConfirmDetailsScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: ConfirmDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ConfirmDetailsEvent.NavigateBack -> navigateBack.invoke()
            is ConfirmDetailsEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is ConfirmDetailsEvent.NavigateToAuthenticate -> navigateToAuthenticateScreen.invoke()
        }
    }

    ConfirmDetailsScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ConfirmDetailsDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConfirmDetailsDialog(
    dialogState: ConfirmDetailsDialogState?,
    onAction: (ConfirmDetailsAction) -> Unit,
) {
    when (dialogState) {
        is ConfirmDetailsDialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(ConfirmDetailsAction.DismissDialog) },
            )
        }

        ConfirmDetailsDialogState.Loading -> MifosProgressIndicator()

        ConfirmDetailsDialogState.OverlayLoading -> MifosProgressIndicatorOverlay()

        null -> {}
    }
}

@Composable
internal fun ConfirmDetailsScreenContent(
    state: ConfirmDetailsState,
    onAction: (ConfirmDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(ConfirmDetailsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_apply_loan_info_confirm_details),
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
            modifier = modifier
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraLarge),
        ) {
            MifosDetailsCard(state.details)

            MifosButton(
                modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                onClick = {
                    onAction(ConfirmDetailsAction.NavigateToAuthenticate)
                },
                shape = DesignToken.shapes.medium,
            ) {
                Text(
                    text = stringResource(Res.string.feature_apply_loan_title),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

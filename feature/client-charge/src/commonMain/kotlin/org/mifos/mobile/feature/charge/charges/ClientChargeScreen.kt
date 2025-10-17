/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.charges

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.database_warning
import mifos_mobile.feature.client_charge.generated.resources.error_no_charge
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.charge.components.ClientChargeItem
/**
 * Composable function that displays the Client Charges Screen.
 *
 * @param navigateBack A lambda function that is called when the user navigates back.
 * @param onChargeClick A lambda function that is called when the user clicks on a charge.
 * @param modifier Modifier to be applied to the layout.
 * @param viewModel ViewModel that provides the state and actions for the screen.
 */
@Composable
internal fun ClientChargeScreen(
    navigateBack: () -> Unit,
    onChargeClick: (charge: Charge) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClientChargeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ClientChargeEvent.Navigate -> navigateBack.invoke()

            is ClientChargeEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is ClientChargeEvent.OnChargeClick -> {
                onChargeClick(event.charge)
            }
        }
    }
    ClientChargeScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ClientChargeDialogs(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(ClientChargeAction.OnDismissDialog) }
        },
    )
}

/**
 * Composable function that displays the Client Charges Screen.
 *
 * @param state State of the screen.
 * @param modifier Modifier to be applied to the layout.
 * @param onAction A lambda function that is called when the user performs an action.
 */
@Composable
private fun ClientChargeScreen(
    state: ClientChargeState,
    modifier: Modifier = Modifier,
    onAction: (ClientChargeAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(state.topBarTitleResId),
        onNavigateBack = { onAction(ClientChargeAction.OnNavigate) },
        modifier = modifier,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Empty -> {
                EmptyDataView(
                    modifier = Modifier.fillMaxSize(),
                    image = Res.drawable.database_warning,
                    error = Res.string.error_no_charge,

                )
            }

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(ClientChargeAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(ClientChargeAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                ClientChargeContent(
                    modifier = Modifier.padding(DesignToken.padding.large),
                    chargesList = state.charges,
                    onChargeClick = {
                        onAction(ClientChargeAction.OnChargeClick(it))
                    },
                )
            }
            else -> { }
        }
    }
}

/**
 * Composable function that displays the content of the Client Charges Screen.
 *
 * @param chargesList List of charges to be displayed.
 * @param onChargeClick A lambda function that is called when the user clicks on a charge.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
private fun ClientChargeContent(
    chargesList: List<Charge>,
    onChargeClick: (charge: Charge) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = chargesList) { charge ->
            ClientChargeItem(charge = charge, onChargeClick = { onChargeClick(charge) })
        }
    }
}

/**
 * Composable function that displays the dialogs of the Client Charges Screen.
 *
 * @param dialogState Dialog state used for showing loading or error.
 * @param onDismissRequest A lambda function that is called when the user dismisses the dialog.
 */
@Composable
private fun ClientChargeDialogs(
    dialogState: ClientChargeState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is ClientChargeState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = dialogState.message,
                ),
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Preview
@Composable
private fun ClientChargeScreenPreview() {
    MifosMobileTheme {
        ClientChargeScreen(
            modifier = Modifier,
            state = ClientChargeState(
                dialogState = null,
                isOnline = false,
                clientId = 1L,
                chargeType = ChargeType.CLIENT,
                chargeTypeId = 1L,
            ),
            onAction = { },
        )
    }
}

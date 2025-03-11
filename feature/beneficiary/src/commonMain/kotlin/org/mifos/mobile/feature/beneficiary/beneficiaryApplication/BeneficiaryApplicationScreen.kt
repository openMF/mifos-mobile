/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.error_fetching_beneficiary_template
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BeneficiaryApplicationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryApplicationViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is BeneficiaryApplicationEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            BeneficiaryApplicationEvent.Navigate -> navigateBack.invoke()
        }
    }

    BeneficiaryApplicationScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun BeneficiaryApplicationDialogs(
    state: BeneficiaryApplicationState,
    onAction: (BeneficiaryApplicationAction) -> Unit,
) {
    when (state.dialogState) {
        BeneficiaryApplicationState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is BeneficiaryApplicationState.DialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.isOnline,
                isRetryEnabled = true,
                onRetry = { onAction(BeneficiaryApplicationAction.OnRetry) },
                message = stringResource(Res.string.error_fetching_beneficiary_template),
            )
        }
        null -> Unit
    }
}

@Composable
private fun BeneficiaryApplicationScreen(
    state: BeneficiaryApplicationState,
    onAction: (BeneficiaryApplicationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = state.topBarTitle,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        backPress = { onAction(BeneficiaryApplicationAction.OnNavigate) },
        modifier = modifier,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
            ) {
                if (state.template != null) {
                    BeneficiaryApplicationContent(
                        state = state,
                        onAction = onAction,
                    )
                }
            }
        },
    )
    BeneficiaryApplicationDialogs(
        state = state,
        onAction = onAction,
    )
}

@Preview
@Composable
private fun BeneficiaryApplicationScreenPreview() {
    MifosMobileTheme {
        BeneficiaryApplicationScreen(
            state = BeneficiaryApplicationState(
                dialogState = null,
                beneficiaryState = BeneficiaryState.CREATE_QR,
            ),
            onAction = { },
            modifier = Modifier,
            snackbarHostState = SnackbarHostState(),
        )
    }
}

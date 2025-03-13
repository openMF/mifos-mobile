/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.beneficiaries
import mifos_mobile.feature.beneficiary.generated.resources.error_fetching_beneficiaries
import mifos_mobile.feature.beneficiary.generated.resources.ic_add_white_24dp
import mifos_mobile.feature.beneficiary.generated.resources.ic_error_black_24dp
import mifos_mobile.feature.beneficiary.generated.resources.no_beneficiary_found_please_add
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.FloatingActionButtonContent
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BeneficiaryListScreen(
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryListViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.trySendAction(BeneficiaryListAction.LoadBeneficiaries)
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryListEvent.Navigate -> navigateBack.invoke()
            BeneficiaryListEvent.AddBeneficiaryClicked -> addBeneficiaryClicked()
            is BeneficiaryListEvent.BeneficiaryItemClick -> onBeneficiaryItemClick(event.position)
            is BeneficiaryListEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    BeneficiaryListScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun BeneficiaryListDialog(
    state: BeneficiaryListState,
    onAction: (BeneficiaryListAction) -> Unit,
) {
    when (state.dialogState) {
        BeneficiaryListState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is BeneficiaryListState.DialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.isOnline,
                isRetryEnabled = true,
                onRetry = {
                    onAction(
                        BeneficiaryListAction.RefreshBeneficiaries,
                    )
                },
                message = stringResource(Res.string.error_fetching_beneficiaries),
            )
        }

        null -> Unit
    }
}

@Composable
private fun BeneficiaryListScreen(
    state: BeneficiaryListState,
    onAction: (BeneficiaryListAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.beneficiaries),
        backPress = { onAction(BeneficiaryListAction.OnNavigate) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = {
                onAction(
                    BeneficiaryListAction.OnAddBeneficiaryClicked,
                )
            },
            contentColor = MaterialTheme.colorScheme.onBackground,
            content = {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_white_24dp),
                    contentDescription = null,
                )
            },
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            if (state.dialogState == null) {
                if (state.beneficiaries.isEmpty()) {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize(),
                        image = Res.drawable.ic_error_black_24dp,
                        error = Res.string.no_beneficiary_found_please_add,
                    )
                } else {
                    ShowBeneficiary(
                        beneficiaryList = state.beneficiaries,
                        onClick = { position ->
                            onAction(BeneficiaryListAction.OnBeneficiaryItemClick(position))
                        },
                    )
                }
            }
        }
    }
    BeneficiaryListDialog(
        state = state,
        onAction = onAction,
    )
}

@Preview
@Composable
private fun PreviewBeneficiaryListScreen() {
    MifosMobileTheme {
        BeneficiaryListScreen(
            state = BeneficiaryListState(dialogState = null),
            onAction = { },
            modifier = Modifier,
            snackbarHostState = SnackbarHostState(),
        )
    }
}

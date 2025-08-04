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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.error_fetching_beneficiaries
import mifos_mobile.feature.beneficiary.generated.resources.ic_error_black_24dp
import mifos_mobile.feature.beneficiary.generated.resources.manage_beneficiaries
import mifos_mobile.feature.beneficiary.generated.resources.no_beneficiary_found_please_add
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosBeneficiariesCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
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
fun ShowBeneficiary(
    beneficiaryList: List<Beneficiary>,
    onClick: (beneficiaryId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LazyColumn(modifier = Modifier) {
            items(beneficiaryList) { beneficiary ->
                MifosBeneficiariesCard(
                    beneficiary = beneficiary,
                    onBeneficiaryClick = { onClick(beneficiary.id ?: -1) },
                )
            }
        }
    }
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
    MifosElevatedScaffold(
        onNavigateBack = { onAction(BeneficiaryListAction.OnNavigate) },
        topBarTitle = stringResource(Res.string.manage_beneficiaries),
        snackbarHost = { snackbarHostState },
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
        Box(
            modifier = Modifier
                .fillMaxSize(),
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
fun PreviewBeneficiaryListScreen() {
    MifosMobileTheme {
        BeneficiaryListScreen(
            state = BeneficiaryListState(dialogState = null),
            onAction = { },
            modifier = Modifier,
            snackbarHostState = SnackbarHostState(),
        )
    }
}

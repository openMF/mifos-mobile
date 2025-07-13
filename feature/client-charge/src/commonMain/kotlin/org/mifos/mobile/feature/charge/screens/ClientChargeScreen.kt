/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.amount_due
import mifos_mobile.feature.client_charge.generated.resources.amount_outstanding
import mifos_mobile.feature.client_charge.generated.resources.amount_paid
import mifos_mobile.feature.client_charge.generated.resources.amount_waived
import mifos_mobile.feature.client_charge.generated.resources.error_no_charge
import mifos_mobile.feature.client_charge.generated.resources.ic_charges
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeAction
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeEvent
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeState
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeViewModel

@Composable
internal fun ClientChargeScreen(
    navigateBack: () -> Unit,
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
        }
    }
    ClientChargeScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },

    )
}

@Composable
private fun ClientChargeDialog(
    state: ClientChargeState,
    onAction: (ClientChargeAction) -> Unit,
) {
    when (state.chargeDialog) {
        is ClientChargeState.ChargeDialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.isOnline,
                isRetryEnabled = true,
                onRetry = { onAction(ClientChargeAction.RefreshCharges) },
            )
        }

        is ClientChargeState.ChargeDialogState.Loading -> {
            MifosProgressIndicator()
        }

        null -> Unit
    }
}

@Composable
private fun ClientChargeScreen(
    state: ClientChargeState,
    modifier: Modifier = Modifier,
    onAction: (ClientChargeAction) -> Unit,
) {
    MifosScaffold(
        topBarTitle = stringResource(state.topBarTitleResId),
        onNavigationIconClick = { onAction(ClientChargeAction.OnNavigate) },
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                when {
                    state.charges.isEmpty() && state.chargeDialog == null -> {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            image = Res.drawable.ic_charges,
                            error = Res.string.error_no_charge,
                        )
                    }

                    state.charges.isNotEmpty() -> {
                        ClientChargeContent(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            chargesList = state.charges,
                        )
                    }
                }
            }
        },
    )
    ClientChargeDialog(
        state = state,
        onAction = onAction,
    )
}

@Composable
private fun ClientChargeContent(
    chargesList: List<Charge>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = chargesList) { charge ->
            ClientChargeItem(charge = charge)
        }
    }
}

@Composable
private fun ClientChargeItem(
    charge: Charge,
    modifier: Modifier = Modifier,
) {
    val currencyRepresentation = charge.currency?.code ?: ""

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
        ) {
            VerticalDivider(
                thickness = 5.dp,
                color = if (charge.isChargePaid || charge.isChargeWaived ||
                    charge.paid || charge.waived
                ) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.fillMaxHeight(),
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = charge.name ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = if (charge.dueDate.isNotEmpty()) {
                        DateHelper.getDateAsString(charge.dueDate.mapNotNull { it })
                    } else {
                        ""
                    },
                    style = MaterialTheme.typography.bodyLarge,
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.amount_due),
                    description = CurrencyFormatter.format(
                        charge.amount,
                        currencyRepresentation,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.amount_paid),
                    description = CurrencyFormatter.format(
                        charge.amountPaid,
                        currencyRepresentation,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.amount_waived),
                    description = CurrencyFormatter.format(
                        charge.amountWaived,
                        currencyRepresentation,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.amount_outstanding),
                    description = CurrencyFormatter.format(
                        charge.amountOutstanding,
                        currencyRepresentation,
                        2,
                    ),

                )
            }
        }
    }
}

@Preview
@Composable
private fun ClientChargeScreenPreview() {
    MifosMobileTheme {
        ClientChargeScreen(
            modifier = Modifier,
            state = ClientChargeState(chargeDialog = null, isOnline = false),
            onAction = { },
        )
    }
}

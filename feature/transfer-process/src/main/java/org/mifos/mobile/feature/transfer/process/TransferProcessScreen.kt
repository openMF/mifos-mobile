/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
internal fun TransferProcessScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransferProcessViewModel = hiltViewModel(),
) {
    val uiState by viewModel.transferUiState.collectAsStateWithLifecycle()
    val payload by viewModel.transferPayload.collectAsStateWithLifecycle()

    TransferProcessScreen(
        uiState = uiState,
        transfer = viewModel::makeTransfer,
        payload = payload,
        navigateBack = navigateBack,
        modifier = modifier,
    )
}

@Composable
private fun TransferProcessScreen(
    uiState: TransferProcessUiState,
    payload: TransferPayload?,
    transfer: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = R.string.transfer,
        navigateBack = navigateBack,
        modifier = modifier,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                TransferProcessContent(
                    payload = payload,
                    transfer = transfer,
                    cancelClicked = navigateBack,
                )

                when (uiState) {
                    is TransferProcessUiState.Loading -> MifosProgressIndicatorOverlay()

                    is TransferProcessUiState.Success -> {
                        Toast.makeText(
                            context,
                            R.string.transferred_successfully,
                            Toast.LENGTH_SHORT,
                        ).show()
                        navigateBack()
                    }

                    is TransferProcessUiState.Error -> {
                        MifosErrorComponent(isNetworkConnected = Network.isConnected(context))
                    }

                    is TransferProcessUiState.Initial -> Unit
                }
            }
        },
    )
}

@Composable
private fun TransferProcessContent(
    payload: TransferPayload?,
    transfer: () -> Unit,
    cancelClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            border = BorderStroke(1.dp, Color.LightGray),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.amount),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Text(text = payload?.transferAmount.toString())
                }

                Text(
                    text = stringResource(id = R.string.transfer_from_savings),
                    fontWeight = FontWeight(500),
                    color = Color.Gray,
                )

                Text(
                    text = stringResource(id = R.string.pay_to),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = payload?.toAccountNumber.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Text(
                    text = stringResource(id = R.string.pay_from),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = payload?.fromAccountNumber.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Text(
                    text = stringResource(id = R.string.date),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = payload?.transferDate.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Text(
                    text = stringResource(id = R.string.remark),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = payload?.transferDescription.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                    ) {
                        MifosButton(
                            textResId = R.string.cancel,
                            onClick = cancelClicked,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                        )
                        MifosButton(
                            textResId = R.string.transfer,
                            onClick = transfer,
                        )
                    }
                }
            }
        }
    }
}

internal class UiStatesParameterProvider : PreviewParameterProvider<TransferProcessUiState> {
    override val values: Sequence<TransferProcessUiState>
        get() = sequenceOf(
            TransferProcessUiState.Initial,
            TransferProcessUiState.Loading,
            TransferProcessUiState.Error(null),
            TransferProcessUiState.Success,
        )
}

@DevicePreviews
@Composable
private fun TransferProcessScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    transferUiState: TransferProcessUiState,
) {
    MifosMobileTheme {
        TransferProcessScreen(
            uiState = transferUiState,
            payload = TransferPayload(
                transferAmount = 100.0,
                fromAccountNumber = "1234567890",
                toAccountNumber = "0987654321",
                transferDate = "2021-09-01",
                transferDescription = "Transfer Description",
            ),
            transfer = {},
            navigateBack = {},
        )
    }
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.transferProcess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.download_pdf
import mifos_mobile.feature.transfer_process.generated.resources.go_to_home
import mifos_mobile.feature.transfer_process.generated.resources.pdf_generated_successfully
import mifos_mobile.feature.transfer_process.generated.resources.transfer_successful
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BillScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BillViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val pdfSuccessMessage = stringResource(Res.string.pdf_generated_successfully)

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is BillEvent.PdfDownloaded -> {
                snackbarHostState.showSnackbar(message = pdfSuccessMessage)
            }
        }
    }

    BillScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        navigateToHome = navigateToHome,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@Composable
private fun BillScreen(
    state: BillState,
    onAction: (BillAction) -> Unit,
    navigateToHome: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.transfer_successful),
        onNavigateBack = { /* No back navigation */ },
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
        ) {
            // Transaction ID
            Column {
                Text(
                    text = "Transaction ID",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = state.transferId,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Amount
            Column {
                Text(
                    text = "Amount Transferred",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = state.amount,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            // From Account
            Column {
                Text(
                    text = "From Account",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = state.fromAccount,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // To Account
            Column {
                Text(
                    text = "To Account",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = state.toAccount,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Date
            Column {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = state.date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Remark (if present)
            if (state.remark.isNotEmpty()) {
                Column {
                    Text(
                        text = "Remark",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = state.remark,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            // Buttons section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DesignToken.padding.large),
                verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
            ) {
                MifosButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignToken.sizes.buttonHeight),
                    text = { Text(text = stringResource(Res.string.download_pdf)) },
                    onClick = { onAction(BillAction.DownloadPdf) },
                )

                MifosOutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignToken.sizes.buttonHeight),
                    onClick = navigateToHome,
                ) {
                    Text(text = stringResource(Res.string.go_to_home))
                }
            }
        }
    }
}

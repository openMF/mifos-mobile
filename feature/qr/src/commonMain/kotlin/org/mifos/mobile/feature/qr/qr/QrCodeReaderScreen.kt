/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.qr.CodeType
import org.mifos.mobile.core.qr.QrScannerWithPermissions
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun QrCodeReaderScreen(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeReaderViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            QrCodeReaderEvent.Navigate -> navigateBack.invoke()
            is QrCodeReaderEvent.NavigateToBeneficiary -> {
                openBeneficiaryApplication(event.beneficiary, event.beneficiaryState)
            }
        }
    }

    QrCodeReaderContent(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun QrCodeReaderDialog(
    state: QrCodeReaderState,
    onAction: (QrCodeReaderAction) -> Unit,
) {
    when (state.dialogState) {
        is QrCodeReaderState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.dialogState.message,
                ),
                onDismissRequest = { onAction(QrCodeReaderAction.OnDismiss) },
            )
        }
        null -> Unit
    }
}

@Composable
private fun QrCodeReaderContent(
    state: QrCodeReaderState,
    modifier: Modifier = Modifier,
    onAction: (QrCodeReaderAction) -> Unit,
) {
    MifosScaffold(
        topBarTitle = null,
        onNavigationIconClick = { onAction(QrCodeReaderAction.OnNavigate) },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            QrScannerWithPermissions(
                types = listOf(CodeType.QR),
                modifier = Modifier,
                onScanned = {
                    onAction(QrCodeReaderAction.ScanQrCode(it))
                    true
                },
            )
        }
    }

    QrCodeReaderDialog(
        state = state,
        onAction = onAction,
    )
}

@Preview
@Composable
private fun QrCodeReaderScreenPreview() {
    MifosMobileTheme {
        QrCodeReaderScreen(
            openBeneficiaryApplication = { _, _ -> },
            navigateBack = {},
        )
    }
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeImport

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.qr_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun QrCodeImportScreen(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeImportViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            QrCodeImportEvent.Navigate -> navigateBack.invoke()
            is QrCodeImportEvent.OpenBeneficiaryApplication -> {
                openBeneficiaryApplication(
                    event.beneficiary,
                    event.beneficiaryState,
                )
            }
        }
    }

    QrCodeImportScreenContent(
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },

    )

    QrCodeDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun QrCodeDialog(
    state: QrCodeImportState,
    onAction: (QrCodeImportAction) -> Unit,
) {
    when (state.dialogState) {
        QrCodeImportState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is QrCodeImportState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(state.dialogState.message),
                ),
                onDismissRequest = { onAction(QrCodeImportAction.DismissDialog) },
            )
        }
        null -> Unit
    }
}

@Composable
private fun QrCodeImportScreenContent(
    modifier: Modifier = Modifier,
    onAction: (QrCodeImportAction) -> Unit,
) {
    MifosElevatedScaffold(

        onNavigateBack = { onAction(QrCodeImportAction.OnNavigate) },
        topBarTitle = stringResource(Res.string.qr_code),
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
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                QrCodeImportContent(proceedClicked = { imageBitmap ->
                    onAction(
                        QrCodeImportAction.Proceed(imageBitmap),
                    )
                })
            }
        }
    }
}

@Composable
expect fun QrCodeImagePicker(
    onProceed: (bitmap: ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
)

@Composable
private fun QrCodeImportContent(
    proceedClicked: (bitmap: ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    QrCodeImagePicker(
        onProceed = { bitmap ->
            proceedClicked.invoke(bitmap)
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun QrCodeImportPreview() {
    MifosMobileTheme {
        QrCodeImportScreenContent(
            onAction = { },
            modifier = Modifier,
        )
    }
}

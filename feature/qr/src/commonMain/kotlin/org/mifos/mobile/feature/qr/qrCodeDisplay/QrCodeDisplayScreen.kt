/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeDisplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.qr_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopBar
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun QrCodeDisplayScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeDisplayViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            QrCodeDisplayEvent.Navigate -> navigateBack.invoke()
            is QrCodeDisplayEvent.ShowToast -> { }
        }
    }

    QrCodeDisplayScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun QrCodeDialog(
    state: QrCodeDisplayState,
    onAction: (QrCodeDisplayAction) -> Unit,
) {
    when (state.dialogState) {
        QrCodeDisplayState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is QrCodeDisplayState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.dialogState.message,
                ),
                onDismissRequest = { onAction(QrCodeDisplayAction.DismissDialog) },
            )
        }
        null -> Unit
    }
}

@Composable
private fun QrCodeDisplayScreen(
    state: QrCodeDisplayState,
    modifier: Modifier = Modifier,
    onAction: (QrCodeDisplayAction) -> Unit,
) {
    var qrBitmap by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }

    MifosScaffold(
        modifier = modifier,
        topBar = {
            MifosTopBar(
                backPress = { onAction(QrCodeDisplayAction.OnNavigate) },
                topBarTitle = stringResource(Res.string.qr_code),
                actions = {
                    IconButton(
                        onClick = {
                            onAction(QrCodeDisplayAction.ShareQrCode)
                        },
                        content = {
                            Icon(
                                imageVector = MifosIcons.Share,
                                contentDescription = null,
                            )
                        },
                    )
                },
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize(),
            ) {
                if (state.qrBitmap != null) {
                    qrBitmap = state.qrBitmap
                    QrCodeDisplayContent(qrBitmap = state.qrBitmap)
                }
            }
        },
    )
    QrCodeDialog(
        state = state,
        onAction = onAction,
    )
}

@Composable
private fun QrCodeDisplayContent(
    qrBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            bitmap = qrBitmap,
            contentDescription = stringResource(Res.string.qr_code),
            modifier = Modifier
                .padding(20.dp)
                .aspectRatio(1f),
        )
    }
}

expect fun share(qrBitmap: ImageBitmap, string: String)

@Preview
@Composable
private fun QrCodeDisplayScreenPreview() {
    MifosMobileTheme {
        QrCodeDisplayScreen(
            state = QrCodeDisplayState(dialogState = null),
            onAction = { },
        )
    }
}

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.alexzhirkevich.qrose.ImageFormat
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import io.github.alexzhirkevich.qrose.toByteArray
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.choose_option
import mifos_mobile.feature.qr.generated.resources.qr_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
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
    val painter = rememberQrCodePainter(
        data = state.qrArgs ?: "",
        options = QrCodeDisplayState.QrViewState.Content(data = state.qrArgs ?: "").options,
    )

    val bytes: ByteArray = remember(painter) {
        painter.toByteArray(1024, 1024, ImageFormat.PNG)
    }
    val option = stringResource(Res.string.choose_option)
    MifosScaffold(
        modifier = modifier,
        topBar = {
            MifosTopAppBar(
                backPress = { onAction(QrCodeDisplayAction.OnNavigate) },
                topBarTitle = stringResource(Res.string.qr_code),
                actions = {
                    IconButton(
                        onClick = {
                            onAction(
                                QrCodeDisplayAction.ShareQrCode(
                                    bytes,
                                    option,
                                ),
                            )
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
                QrCodeDisplayContent(painter = painter)
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
    painter: Painter,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier
                .size(350.dp, 390.dp)
                .background(Color.White, shape = RoundedCornerShape(15.dp))
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "Mifos Mobile",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 45.dp)
                    .size(260.dp),
            )
        }
    }
}

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

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.generated_on
import mifos_mobile.feature.qr.generated.resources.qr_alignment_instruction
import mifos_mobile.feature.qr.generated.resources.qr_code
import mifos_mobile.feature.qr.generated.resources.qr_scan_instruction
import mifos_mobile.feature.qr.generated.resources.scan_your_qr
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
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

@Suppress("UnusedPrivateProperty")
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

    MifosElevatedScaffold(
        modifier = modifier,
        topBarTitle = stringResource(Res.string.qr_code),
        onNavigateBack = { onAction(QrCodeDisplayAction.OnNavigate) },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        content = {
            QrCodeDisplayContent(painter = painter)
        },
    )
}

@Composable
private fun QrCodeDisplayContent(
    painter: Painter,
    modifier: Modifier = Modifier,
) {
    val date = DateHelper.formattedShortDate
    Box(
        modifier = modifier
            .padding(
                horizontal = DesignToken.padding.large,
                vertical = DesignToken.padding.extraLargeIncreased,
            )
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.scan_your_qr),
                style = MifosTypography.titleLargeEmphasized,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(DesignToken.padding.largeIncreased))

            Text(
                text = stringResource(Res.string.qr_scan_instruction),
                style = MifosTypography.bodyMediumEmphasized,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(DesignToken.padding.extraExtraLarge))

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(212.dp),
            )

            Spacer(Modifier.height(DesignToken.padding.extraExtraLarge))

            Text(
                text = stringResource(Res.string.qr_alignment_instruction),
                style = MifosTypography.bodyMediumEmphasized,
                textAlign = TextAlign.Center,
            )
        }

        Text(
            text = stringResource(Res.string.generated_on) + date,
            style = MifosTypography.bodyMediumEmphasized,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Preview
@Composable
private fun QrCodeDisplayScreenPreview() {
    MifosMobileTheme {
        QrCodeDisplayScreen(
            state = QrCodeDisplayState(),
            onAction = { },
        )
    }
}

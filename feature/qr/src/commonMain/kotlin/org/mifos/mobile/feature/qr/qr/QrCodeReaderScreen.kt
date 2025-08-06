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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.feature_qr_instruction
import mifos_mobile.feature.qr.generated.resources.feature_qr_upload
import mifos_mobile.feature.qr.generated.resources.feature_qr_warning_message
import mifos_mobile.feature.qr.generated.resources.feature_qr_warning_title
import mifos_mobile.feature.qr.generated.resources.qr_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.qr.CodeType
import org.mifos.mobile.core.qr.QrScannerWithPermissions
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun QrCodeReaderScreen(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
    navigateToQrImportScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeReaderViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            QrCodeReaderEvent.Navigate -> navigateBack.invoke()

            QrCodeReaderEvent.NavigateToUploadQr -> navigateToQrImportScreen.invoke()

            is QrCodeReaderEvent.NavigateToBeneficiary -> {
                Logger.e("Revanth") {
                    event.beneficiary.toString()
                }
                openBeneficiaryApplication(event.beneficiary, event.beneficiaryState)
            }
        }
    }

    QrCodeReaderContent(
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    QrCodeReaderDialog(
        state = state,
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
                    message = stringResource(state.dialogState.message),
                ),
                onDismissRequest = { onAction(QrCodeReaderAction.OnDismiss) },
            )
        }

        null -> Unit
    }
}

@Composable
private fun QrCodeReaderContent(
    modifier: Modifier = Modifier,
    onAction: (QrCodeReaderAction) -> Unit,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(QrCodeReaderAction.OnNavigate) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.large),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = DesignToken.sizes.buttonHeight + 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.feature_qr_instruction),
                    style = MifosTypography.titleSmallEmphasized,
                    textAlign = TextAlign.Center,
                    color = AppColors.customBlack,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(DesignToken.shapes.medium)
                        .drawQrCorners()
                        .border(1.dp, Color.Transparent, DesignToken.shapes.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    QrScannerWithPermissions(
                        types = listOf(CodeType.QR),
                        modifier = Modifier.matchParentSize(),
                        onScanned = {
                            onAction(QrCodeReaderAction.ScanQrCode(it))
                            true
                        },
                    )

                    MifosCustomCard(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(DesignToken.padding.small),
                        shape = DesignToken.shapes.medium,
                        variant = CardVariant.OUTLINED,
                    ) {
                        Column(
                            modifier = Modifier.padding(DesignToken.padding.large),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = MifosIcons.Warning,
                                    contentDescription = "Warning",
                                    tint = Color.Unspecified,
                                )
                                Text(
                                    text = stringResource(Res.string.feature_qr_warning_title),
                                    style = MifosTypography.bodyMediumEmphasized,
                                    color = AppColors.customBlack,
                                )
                            }
                            Text(
                                text = stringResource(Res.string.feature_qr_warning_message),
                                style = MifosTypography.bodySmall,
                                color = AppColors.customBlack,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }

            MifosButton(
                onClick = { onAction(QrCodeReaderAction.OnNavigateToUpload) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight),
                shape = DesignToken.shapes.medium,
            ) {
                Text(
                    text = stringResource(Res.string.feature_qr_upload),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

private fun Modifier.drawQrCorners(): Modifier = drawWithContent {
    drawContent()

    val strokeWidth = 5.dp.toPx()
    val lineLength = 40.dp.toPx()

    val horizontalPadding = 50.dp.toPx() // for left & right
    val verticalPaddingTop = 50.dp.toPx() // for top corners
    val verticalPaddingBottom = 200.dp.toPx() // more padding for bottom corners

    val color = AppColors.customWhite

    // Top-left
    drawLine(
        color,
        Offset(horizontalPadding, verticalPaddingTop),
        Offset(horizontalPadding + lineLength, verticalPaddingTop),
        strokeWidth,
    )
    drawLine(
        color,
        Offset(horizontalPadding, verticalPaddingTop),
        Offset(horizontalPadding, verticalPaddingTop + lineLength),
        strokeWidth,
    )

    // Top-right
    drawLine(
        color,
        Offset(size.width - horizontalPadding, verticalPaddingTop),
        Offset(size.width - horizontalPadding - lineLength, verticalPaddingTop),
        strokeWidth,
    )
    drawLine(
        color,
        Offset(size.width - horizontalPadding, verticalPaddingTop),
        Offset(size.width - horizontalPadding, verticalPaddingTop + lineLength),
        strokeWidth,
    )

    // Bottom-left
    drawLine(
        color,
        Offset(horizontalPadding, size.height - verticalPaddingBottom),
        Offset(horizontalPadding + lineLength, size.height - verticalPaddingBottom),
        strokeWidth,
    )
    drawLine(
        color,
        Offset(horizontalPadding, size.height - verticalPaddingBottom),
        Offset(horizontalPadding, size.height - verticalPaddingBottom - lineLength),
        strokeWidth,
    )

    // Bottom-right
    drawLine(
        color,
        Offset(size.width - horizontalPadding, size.height - verticalPaddingBottom),
        Offset(size.width - horizontalPadding - lineLength, size.height - verticalPaddingBottom),
        strokeWidth,
    )
    drawLine(
        color,
        Offset(size.width - horizontalPadding, size.height - verticalPaddingBottom),
        Offset(size.width - horizontalPadding, size.height - verticalPaddingBottom - lineLength),
        strokeWidth,
    )
}

@Preview
@Composable
private fun QrCodeReaderScreenPreview() {
    MifosMobileTheme {
        QrCodeReaderContent(
            onAction = {},
        )
    }
}

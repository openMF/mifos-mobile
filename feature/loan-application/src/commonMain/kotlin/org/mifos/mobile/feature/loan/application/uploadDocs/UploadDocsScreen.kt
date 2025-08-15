/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.uploadDocs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_section_fill_details
import mifos_mobile.feature.loan_application.generated.resources.feature_button_next
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.loan.application.component.UploadDocumentsSection
import org.mifos.mobile.feature.loan.application.uploadDocs.component.BottomSheetContent

@Composable
internal fun UploadDocsScreen(
    navigateBack: () -> Unit,
    navigateToNext: () -> Unit,
    navigateToPreviewDoc: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadDocsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            UploadDocsEvent.NavigateBack -> navigateBack.invoke()
            UploadDocsEvent.Next -> navigateToNext.invoke()
            is UploadDocsEvent.PreviewDoc -> navigateToPreviewDoc.invoke()
        }
    }

    UploadDocsDialogs(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    UploadDocsScreenContent(
        modifier = modifier,
        state = state,
        onAction = {
            viewModel.trySendAction(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UploadDocsDialogs(
    dialogState: UploadDocumentDialog?,
    onAction: (UploadDocsAction) -> Unit,
) {
    when (dialogState) {
        is UploadDocumentDialog.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.error),
                ),
                onDismissRequest = {
                    onAction(UploadDocsAction.DismissDialog)
                },
            )
        }

        is UploadDocumentDialog.ShowSignaturePicker -> {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = dialogState.isSignatureMode,
            )

            // Animate the sheet expansion when transitioning to signature mode
            LaunchedEffect(dialogState.isSignatureMode) {
                if (dialogState.isSignatureMode) {
                    sheetState.expand()
                }
            }

            ModalBottomSheet(
                onDismissRequest = {
                    onAction(UploadDocsAction.DismissDialog)
                },
                sheetState = sheetState,
                containerColor = Color.White,
                contentWindowInsets = {
                    if (dialogState.isSignatureMode) {
                        WindowInsets(DesignToken.spacing.none)
                    } else {
                        BottomSheetDefaults.windowInsets
                    }
                },
                modifier = if (dialogState.isSignatureMode) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier.wrapContentHeight()
                },
                dragHandle = {
                    if (dialogState.isSignatureMode) {
                        null
                    } else {
                        BottomSheetDefaults.DragHandle()
                    }
                },
            ) {
                BottomSheetContent(
                    onAction = onAction,
                    isSignatureMode = dialogState.isSignatureMode,
                )
            }
        }

        null -> Unit
    }
}

@Composable
internal fun UploadDocsScreenContent(
    state: UploadDocsState,
    modifier: Modifier = Modifier,
    onAction: (UploadDocsAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        topBarTitle = stringResource(Res.string.feature_apply_loan_section_fill_details),
        onNavigateBack = { onAction(UploadDocsAction.OnNavigateBack) },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = DesignToken.padding.large)
                .padding(DesignToken.padding.large)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
        ) {
            UploadDocumentsSection(
                state = state,
                onAction = onAction,
            )

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight),
                shape = DesignToken.shapes.medium,
                onClick = {
                    onAction(UploadDocsAction.NavigateToNextScreen)
                },
                enabled = state.isSubmitEnabled,
            ) {
                Text(
                    text = stringResource(Res.string.feature_button_next),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Upload_Id_Preview() {
    MifosMobileTheme {
        UploadDocsScreenContent(
            state = UploadDocsState(dialogState = null),
            onAction = {},
        )
    }
}

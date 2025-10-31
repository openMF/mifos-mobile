/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.shareApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.share_application.generated.resources.Res
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_server
import mifos_mobile.feature.share_application.generated.resources.feature_share_button_next
import mifos_mobile.feature.share_application.generated.resources.feature_share_label_applicant_name
import mifos_mobile.feature.share_application.generated.resources.feature_share_label_product_name
import mifos_mobile.feature.share_application.generated.resources.feature_share_label_submission_date
import mifos_mobile.feature.share_application.generated.resources.feature_share_no_products_available
import mifos_mobile.feature.share_application.generated.resources.feature_share_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

/**
 * A Composable function that represents the Share Apply screen.
 *
 * @param navigateBack A function to navigate back to the previous screen.
 * @param navigateToFillDetailsScreen A function to navigate to the fill details screen.
 * @param viewModel An instance of [ShareApplyViewModel].
 */
@Composable
internal fun ShareApplyScreen(
    navigateBack: () -> Unit,
    navigateToFillDetailsScreen: (Long) -> Unit,
    viewModel: ShareApplyViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ShareApplicationEvent.NavigateBack -> navigateBack.invoke()

            is ShareApplicationEvent.NavigateToConfirmDetailsScreen -> {
                navigateToFillDetailsScreen(
                    state.selectedShareProductId,
                )
            }
        }
    }

    ShareApplicationContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ShareApplicationDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * A Composable function that displays a dialog based on the [ShareApplicationDialogState].
 *
 * @param dialogState The state of the dialog to be displayed.
 * @param onAction A function to handle actions from the dialog.
 */
@Composable
internal fun ShareApplicationDialog(
    dialogState: ShareApplicationDialogState?,
    onAction: (ShareApplicationAction) -> Unit,
) {
    when (dialogState) {
        is ShareApplicationDialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(ShareApplicationAction.DismissDialog) },
                onConfirm = { onAction(ShareApplicationAction.ConfirmNavigation) },
            )
        }

        null -> {}
    }
}

/**
 * A Composable function that displays the content of the Share Application screen.
 *
 * @param state The current state of the screen.
 * @param onAction A function to handle actions from the screen.
 * @param modifier A [Modifier] for the Composable.
 */
@Composable
internal fun ShareApplicationContent(
    state: ShareApplicationState,
    onAction: (ShareApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(ShareApplicationAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_share_title),
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
        when (state.uiState) {
            is ShareApplicationUiState.Loading -> {
                MifosProgressIndicator()
            }

            is ShareApplicationUiState.Empty -> {
                MifosErrorComponent(
                    isEmptyData = true,
                    message = stringResource(Res.string.feature_share_no_products_available),
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareApplicationAction.Retry) },
                )
            }

            is ShareApplicationUiState.Error -> {
                MifosErrorComponent(
                    message = stringResource(Res.string.feature_apply_share_error_server),
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareApplicationAction.Retry) },
                )
            }

            is ShareApplicationUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareApplicationAction.Retry) },
                )
            }

            is ShareApplicationUiState.Success -> {
                ShareApplicationForm(state, onAction, modifier)
            }

            else -> {}
        }
    }
}

/**
 * A Composable function that displays the form for the Share Application screen.
 *
 * @param state The current state of the screen.
 * @param onAction A function to handle actions from the screen.
 * @param modifier A [Modifier] for the Composable.
 */
@Composable
internal fun ShareApplicationForm(
    state: ShareApplicationState,
    onAction: (ShareApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(DesignToken.padding.large)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        MifosOutlinedTextField(
            value = state.applicantName,
            onValueChange = { },
            label = stringResource(Res.string.feature_share_label_applicant_name),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                enabled = false,
            ),
        )

        MifosOutlinedTextField(
            value = state.submittedOnDate,
            onValueChange = { },
            label = stringResource(Res.string.feature_share_label_submission_date),
            config = MifosTextFieldConfig(
                showClearIcon = false,
                enabled = false,
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = MifosIcons.Calendar,
                        contentDescription = "Open Date Picker",
                    )
                },
            ),
            shape = DesignToken.shapes.medium,
        )

        MifosOutlineDropdown(
            selectedText = state.selectedShareProduct,
            items = state.productOptionsMap,
            enabled = true,
            onItemSelected = { id, product ->
                onAction(ShareApplicationAction.ShareProductChange(id, product))
            },
            label = stringResource(Res.string.feature_share_label_product_name),
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
            enabled = state.isFormValid,
            onClick = {
                onAction(ShareApplicationAction.NavigateToConfirmDetails)
            },
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_share_button_next),
                style = MifosTypography.titleMedium,
            )
        }
    }
}

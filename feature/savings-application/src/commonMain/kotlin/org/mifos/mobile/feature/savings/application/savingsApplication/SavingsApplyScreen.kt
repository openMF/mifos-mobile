/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.savingsApplication

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
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_button_continue
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_applicant_name
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_savings_product
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_submission_date
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_title
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_unsaved_changes_message
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_unsaved_changes_title
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
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme

/**
 * A stateful composable that serves as the entry point for the "Apply for Savings" screen.
 *
 * This function connects to the [SavingsApplyViewModel] to observe UI state and handle
 * one-time events. It is responsible for orchestrating navigation to the next step
 * in the application process.
 *
 * @param navigateBack A lambda function to handle back navigation events.
 * @param navigateToFillDetailsScreen A lambda to navigate to the detailed application form,
 *   passing product and officer information.
 * @param viewModel The ViewModel responsible for the screen's logic and state.
 */
@Composable
internal fun SavingsApplyScreen(
    navigateBack: () -> Unit,
    navigateToFillDetailsScreen: (Long, String) -> Unit,
    viewModel: SavingsApplyViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SavingsApplicationEvent.NavigateBack -> navigateBack.invoke()

            is SavingsApplicationEvent.NavigateToFillDetailsScreen ->
                navigateToFillDetailsScreen.invoke(
                    state.selectedSavingsProductId,
                    state.selectedSavingsProduct,
                )
        }
    }

    SavingsAccountContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    SavingsAccountDialog(
        dialogState = state.savingsApplicationDialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * A composable responsible for displaying dialogs based on the [SavingsApplicationDialogState].
 *
 * This function handles the presentation of error dialogs and confirmation dialogs
 * for unsaved changes.
 *
 * @param dialogState The current state of the dialog to be displayed.
 * @param onAction A callback to send actions (like dismiss or confirm) to the ViewModel.
 */
@Composable
internal fun SavingsAccountDialog(
    dialogState: SavingsApplicationDialogState?,
    onAction: (SavingsApplicationAction) -> Unit,
) {
    when (dialogState) {
        is SavingsApplicationDialogState.Error -> {
            MifosErrorComponent(
                isRetryEnabled = false,
                message = stringResource(dialogState.message),
            )
        }

        is SavingsApplicationDialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    title = stringResource(Res.string.feature_apply_savings_unsaved_changes_title),
                    message = stringResource(Res.string.feature_apply_savings_unsaved_changes_message),
                ),
                onDismissRequest = { onAction(SavingsApplicationAction.DismissDialog) },
                onConfirm = { onAction(SavingsApplicationAction.ConfirmNavigation) },
            )
        }

        null -> {}
    }
}

/**
 * A stateless composable that renders the main UI for the "Apply for Savings" screen.
 *
 * It conditionally displays UI based on the [ScreenUiState] (e.g., loading, error, success).
 * The success state includes dropdowns for selecting a savings product and a field officer.
 *
 * @param state The current [SavingsApplicationState] to render.
 * @param onAction A callback to send user actions to the ViewModel.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
internal fun SavingsAccountContent(
    state: SavingsApplicationState,
    onAction: (SavingsApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(SavingsApplicationAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_apply_savings_title),
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
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    message = stringResource(state.uiState.message),
                    isRetryEnabled = true,
                    onRetry = { onAction(SavingsApplicationAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(SavingsApplicationAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(KptTheme.spacing.md)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
                ) {
                    MifosOutlinedTextField(
                        value = state.applicantName,
                        onValueChange = { },
                        label = stringResource(Res.string.feature_apply_savings_label_applicant_name),
                        shape = KptTheme.shapes.medium,
                        textStyle = MifosTypography.bodyLarge,
                        config = MifosTextFieldConfig(
                            enabled = false,
                        ),
                    )

                    MifosOutlinedTextField(
                        value = state.submittedOnDate,
                        onValueChange = { },
                        label = stringResource(Res.string.feature_apply_savings_label_submission_date),
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
                        shape = KptTheme.shapes.medium,
                    )

                    MifosOutlineDropdown(
                        selectedText = state.selectedSavingsProduct,
                        items = state.productOptionsMap,
                        enabled = true,
                        onItemSelected = { id, product ->
                            onAction(SavingsApplicationAction.SavingsProductChange(id, product))
                        },
                        label = stringResource(Res.string.feature_apply_savings_label_savings_product),
                    )

                    MifosButton(
                        modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                        enabled = state.selectedSavingsProductId != 0L,
                        onClick = {
                            onAction(SavingsApplicationAction.NavigateToConfirmDetails)
                        },
                        shape = KptTheme.shapes.medium,
                    ) {
                        Text(
                            text = stringResource(Res.string.feature_apply_savings_button_continue),
                            style = MifosTypography.titleMedium,
                        )
                    }
                }

                if (state.showOverlay) {
                    MifosProgressIndicatorOverlay()
                }
            }

            else -> { }
        }
    }
}

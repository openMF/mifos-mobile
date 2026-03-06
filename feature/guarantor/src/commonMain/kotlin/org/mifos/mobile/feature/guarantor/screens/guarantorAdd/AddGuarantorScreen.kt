/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorAdd

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.add_guarantor
import mifos_mobile.feature.guarantor.generated.resources.city
import mifos_mobile.feature.guarantor.generated.resources.first_name
import mifos_mobile.feature.guarantor.generated.resources.guarantor_type
import mifos_mobile.feature.guarantor.generated.resources.last_name
import mifos_mobile.feature.guarantor.generated.resources.submit
import mifos_mobile.feature.guarantor.generated.resources.update_guarantor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextField
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorType
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.utils.EventsEffect
import template.core.base.designsystem.theme.KptTheme

/**
 * Main composable function for the "Add Guarantor" screen.
 *
 * This screen allows users to add new guarantors or edit existing ones for a loan.
 * It handles form input for guarantor details including first name, last name, city,
 * and guarantor type. The screen manages different UI states including loading,
 * error, and success.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param modifier Modifier for styling and positioning the screen.
 * @param viewModel The ViewModel that manages the screen's state and business logic.
 *   Provided by Koin dependency injection.
 */
@Composable
internal fun AddGuarantorScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddGuarantorViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            AddGuarantorEvent.NavigateBack -> navigateBack()

            is AddGuarantorEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is AddGuarantorEvent.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                    navigateBack()
                }
            }
        }
    }

    AddGuarantorScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        snackbarHostState = snackbarHostState,
    )
}

/**
 * Private composable function for the "Add Guarantor" screen content.
 *
 * This composable renders the main UI structure including the scaffold,
 * content based on UI state, and dialog management. It handles both
 * adding new guarantors and editing existing ones.
 *
 * @param state The current UI state containing all necessary data for rendering.
 * @param onAction Callback function to handle user actions and UI events.
 * @param snackbarHostState The SnackbarHostState for showing toast messages.
 * @param modifier Modifier for styling and positioning the screen.
 */
@Composable
private fun AddGuarantorScreen(
    state: AddGuarantorState,
    onAction: (AddGuarantorAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = if (state.guarantorItem == null) {
            stringResource(Res.string.add_guarantor)
        } else {
            stringResource(
                Res.string.update_guarantor,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        onNavigationIconClick = { onAction(AddGuarantorAction.NavigateBack) },
        modifier = modifier,
        content = {
            Box(modifier = Modifier) {
                AddGuarantorContent(
                    state = state,
                    guarantorItem = state.guarantorItem,
                    onAction = onAction,
                    guarantorTypeOptions = state.templatePayload?.guarantorTypeOptions?.toList()
                        ?: listOf(),
                )
            }
        },
    )

    AddGuarantorDialog(
        dialogState = state.dialogState,
        onDismissRequest = { onAction(AddGuarantorAction.DismissDialog) },
    )
}

/**
 * Content composable for the "Add Guarantor" screen.
 *
 * This composable displays the form for adding/editing guarantor information,
 * including input fields for first name, last name, city, and guarantor type.
 * It also handles form submission and validation.
 *
 * @param state The current state containing form data and UI information.
 * @param guarantorItem The existing guarantor data for editing, null for new guarantors.
 * @param guarantorTypeOptions List of available guarantor type options.
 * @param onAction Callback function to handle user actions (form submission, navigation).
 * @param modifier Modifier for styling and positioning the content.
 */
@Composable
private fun AddGuarantorContent(
    state: AddGuarantorState,
    guarantorItem: GuarantorPayload?,
    guarantorTypeOptions: List<GuarantorType>,
    onAction: (AddGuarantorAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = guarantorItem) {
        state.firstName = state.guarantorItem?.firstname ?: ""
        state.lastName = state.guarantorItem?.lastname ?: ""
        state.city = state.guarantorItem?.city ?: ""
        state.guarantorType = state.guarantorItem?.guarantorType ?: GuarantorType()
    }

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .padding(horizontal = KptTheme.spacing.md)
            .padding(bottom = KptTheme.spacing.md),
    ) {
        MifosDropDownTextField(
            optionsList = guarantorTypeOptions.filter { it.id == 3L }.mapNotNull { it.value },
            selectedOption = state.guarantorType.value,
            labelResId = Res.string.guarantor_type,
            onClick = { _, item ->
                onAction.invoke(
                    AddGuarantorAction.SetGuarantortype(
                        guarantorTypeOptions.find { it.value == item }
                            ?: GuarantorType(),
                    ),
                )
            },
        )

        MifosTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.firstName,
            onValueChange = { onAction.invoke(AddGuarantorAction.OnFirstNameChange(it)) },
            label = stringResource(Res.string.first_name),
        )

        MifosTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.lastName,
            onValueChange = { onAction.invoke(AddGuarantorAction.OnLastnameChange(it)) },
            label = stringResource(Res.string.last_name),
        )

        MifosTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.city,
            onValueChange = { onAction.invoke(AddGuarantorAction.OnCityChange(it)) },
            label = stringResource(Res.string.city),
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.dp10))

        MifosButton(
            content = { Text(stringResource(Res.string.submit)) },
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(
                    AddGuarantorAction.ValidateFields(
                        GuarantorApplicationPayload(
                            firstName = state.firstName,
                            lastName = state.lastName,
                            guarantorTypeId = state.guarantorType.id,
                            city = state.city,
                        ),
                    ),
                )
            },
        )
    }
}

/**
 * Dialog composable for showing dialogs on the "Add Guarantor" screen.
 *
 * This composable handles the display of loading and error dialogs based on the current
 * dialog state in the [AddGuarantorState]. It shows loading overlays and error messages.
 *
 * @param dialogState The current state containing dialog information.
 * @param onDismissRequest Callback function to handle dialog dismissal.
 */
@Composable
private fun AddGuarantorDialog(
    dialogState: AddGuarantorState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is AddGuarantorState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        AddGuarantorState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )

        null -> Unit
    }
}

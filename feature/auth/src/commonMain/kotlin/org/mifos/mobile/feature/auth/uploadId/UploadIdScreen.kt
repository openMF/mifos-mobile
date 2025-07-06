/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.uploadId

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_common_submit
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_cancel
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_dob_label
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_edit_prompt
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_mobile_label
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_national_id_label
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_ok
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_subtitle
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_take_me_back
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun UploadIdScreen(
    navigateToRegisterScreen: () -> Unit,
    navigateToOtpAuthenticationScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadIdViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            UploadIdEvent.BackClick -> navigateToRegisterScreen.invoke()

            UploadIdEvent.NavigateToOtp -> navigateToOtpAuthenticationScreen.invoke()
        }
    }

    UploadIdDialogs(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(UploadIdAction.DismissDialog) }
        },
    )

    UploadIdScreenContent(
        modifier = modifier,
        state = state,
        onAction = {
            viewModel.trySendAction(it)
        },
    )
}

@Composable
private fun UploadIdDialogs(
    dialogState: UploadIdUiState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is UploadIdUiState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = stringResource(dialogState.message),
            ),
            onDismissRequest = onDismissRequest,
        )

        is UploadIdUiState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )

        null -> Unit
    }
}

@Composable
internal fun UploadIdScreenContent(
    state: UploadIdUiState,
    modifier: Modifier = Modifier,
    onAction: (UploadIdAction) -> Unit,
) {
    MifosScaffold(
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        },
    ) { paddingValues ->

        Column(
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(DesignToken.padding.large),
        ) {
            Text(
                text = stringResource(Res.string.feature_upload_id_title),
                style = MifosTypography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

            Text(
                text = stringResource(Res.string.feature_upload_id_subtitle),
                style = MifosTypography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))

            InputForm(
                state = state,
                onAction = onAction,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun InputForm(
    state: UploadIdUiState,
    modifier: Modifier = Modifier,
    onAction: (UploadIdAction) -> Unit,
) {
    var activateDate by rememberSaveable { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = activateDate,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Clock.System.now().toEpochMilliseconds()
            }
        },
    )

    val selectedDate = datePickerState.selectedDateMillis?.let { DateHelper.getDateAsStringFromLong(it) } ?: ""
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
    ) {
        UploadDocumentsSection(
            state = state,
            onAction = onAction,
        )

        MifosTextFieldWithError(
            value = state.cellPhone,
            label = stringResource(Res.string.feature_upload_id_mobile_label),
            isError = state.cellPhoneError != null,
            errorText = state.cellPhoneError?.let { stringResource(it) },
            onValueChange = { onAction(UploadIdAction.OnMobileChange(it)) },
        )

        MifosTextFieldWithError(
            value = state.nationalId,
            label = stringResource(Res.string.feature_upload_id_national_id_label),
            isError = state.nationalIdError != null,
            errorText = state.nationalIdError?.let { stringResource(it) },
            onValueChange = { onAction(UploadIdAction.OnNationalIdChange(it)) },
        )

        MifosTextFieldWithError(
            value = selectedDate,
            label = stringResource(Res.string.feature_upload_id_dob_label),
            isError = state.dobError != null,
            errorText = state.dobError?.let { stringResource(it) },
            showClearIcon = false,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        onAction(UploadIdAction.ToggleShowDatePicker)
                    },
                    imageVector = MifosIcons.Calendar,
                    contentDescription = "Open Date Picker",
                )
            },
            onValueChange = { onAction(UploadIdAction.OnDOBChange(it)) },
        )

        MifosButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonHeight),
            shape = DesignToken.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            text = {
                Text(
                    text = stringResource(Res.string.feature_common_submit),
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            onClick = {
                onAction(UploadIdAction.OnSubmit)
            },
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = DesignToken.padding.medium),
            horizontalArrangement = Arrangement.spacedBy(DesignToken.padding.small),
        ) {
            Text(
                text = stringResource(Res.string.feature_upload_id_edit_prompt),
                style = MifosTypography.labelMedium,
            )
            Text(
                text = stringResource(Res.string.feature_upload_id_take_me_back),
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = {
                        onAction(UploadIdAction.OnBackClick)
                    },
                ),
            )
        }

        if (state.showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    onAction(UploadIdAction.ToggleShowDatePicker)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAction(UploadIdAction.ToggleShowDatePicker)
                            datePickerState.selectedDateMillis?.let {
                                activateDate = it
                                onAction(UploadIdAction.OnDOBChange(DateHelper.getDateAsStringFromLong(it)))
                            }
                        },
                    ) { Text(stringResource(Res.string.feature_upload_id_ok)) }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onAction(UploadIdAction.ToggleShowDatePicker)
                        },
                    ) { Text(stringResource(Res.string.feature_upload_id_cancel)) }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
private fun MifosTextFieldWithError(
    value: String,
    label: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    errorText: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    showClearIcon: Boolean = true,
) {
    MifosOutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        shape = DesignToken.shapes.medium,
        textStyle = MifosTypography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            errorBorderColor = MaterialTheme.colorScheme.error,
        ),
        config = MifosTextFieldConfig(
            isError = isError,
            errorText = errorText,
            trailingIcon = trailingIcon,
            showClearIcon = showClearIcon,
        ),
    )
}

@Preview
@Composable
private fun Upload_Id_Preview() {
    MifosMobileTheme {
        UploadIdScreenContent(
            state = UploadIdUiState(dialogState = null),
            onAction = {},
        )
    }
}

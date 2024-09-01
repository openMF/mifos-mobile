/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorAdd

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorType
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.guarantor.R

@Composable
internal fun AddGuarantorScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddGuarantorViewModel = hiltViewModel(),
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()
    val guarantorItem = viewModel.guarantorItem.collectAsStateWithLifecycle()

    AddGuarantorScreen(
        uiState = uiState.value,
        guarantorItem = guarantorItem.value,
        navigateBack = navigateBack,
        modifier = modifier,
        onSubmitted = {
            when (guarantorItem.value) {
                null -> viewModel.createGuarantor(it)
                else -> viewModel.updateGuarantor(it)
            }
        },
    )
}

@Composable
private fun AddGuarantorScreen(
    uiState: GuarantorAddUiState,
    guarantorItem: GuarantorPayload?,
    navigateBack: () -> Unit,
    onSubmitted: (GuarantorApplicationPayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val guarantorTypeOptions = rememberSaveable { mutableStateOf(listOf<GuarantorType>()) }

    MifosScaffold(
        topBarTitleResId = if (guarantorItem == null) R.string.add_guarantor else R.string.update_guarantor,
        navigateBack = navigateBack,
        modifier = modifier,
        content = {
            Box(modifier = Modifier.padding(it)) {
                AddGuarantorContent(
                    guarantorItem = guarantorItem,
                    onSubmitted = onSubmitted,
                    guarantorTypeOptions = guarantorTypeOptions.value,
                )
                when (uiState) {
                    is GuarantorAddUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is GuarantorAddUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isEmptyData = false,
                            isRetryEnabled = false,
                        )
                    }

                    is GuarantorAddUiState.Template -> {
                        guarantorTypeOptions.value =
                            uiState.guarantorTemplatePayload?.guarantorTypeOptions?.toList()
                                ?: listOf()
                    }

                    is GuarantorAddUiState.Success -> {
                        Toast.makeText(
                            context,
                            stringResource(id = uiState.messageResId),
                            Toast.LENGTH_SHORT,
                        ).show()
                        navigateBack()
                    }
                }
            }
        },
    )
}

@Composable
private fun AddGuarantorContent(
    guarantorItem: GuarantorPayload?,
    guarantorTypeOptions: List<GuarantorType>,
    onSubmitted: (GuarantorApplicationPayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val firstName =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val lastName =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val city =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val guarantorType = rememberSaveable { mutableStateOf(GuarantorType()) }

    val firstNameError = rememberSaveable { mutableStateOf(false) }
    val lastNameError = rememberSaveable { mutableStateOf(false) }
    val guarantorTypeError = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = guarantorItem) {
        firstName.value = TextFieldValue(guarantorItem?.firstname ?: "")
        lastName.value = TextFieldValue(guarantorItem?.lastname ?: "")
        city.value = TextFieldValue(guarantorItem?.city ?: "")
        guarantorType.value = guarantorItem?.guarantorType ?: GuarantorType()
    }

    LaunchedEffect(key1 = firstName.value) { firstNameError.value = false }
    LaunchedEffect(key1 = lastName.value) { lastNameError.value = false }
    LaunchedEffect(key1 = guarantorType.value) { guarantorTypeError.value = false }

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        MifosDropDownTextField(
            optionsList = guarantorTypeOptions.filter { it.id == 3L }.mapNotNull { it.value },
            selectedOption = guarantorType.value.value,
            labelResId = R.string.guarantor_type,
            error = guarantorTypeError.value,
            onClick = { _, item ->
                guarantorType.value =
                    guarantorTypeOptions.find { it.value == item } ?: GuarantorType()
            },
            supportingText = stringResource(
                R.string.error_validation_blank,
                stringResource(R.string.guarantor_type),
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = firstName.value,
            onValueChange = { firstName.value = it },
            label = R.string.first_name,
            supportingText = stringResource(
                R.string.error_validation_blank,
                stringResource(R.string.first_name),
            ),
            error = firstNameError.value,
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lastName.value,
            onValueChange = { lastName.value = it },
            label = R.string.last_name,
            supportingText = stringResource(
                R.string.error_validation_blank,
                stringResource(R.string.last_name),
            ),
            error = lastNameError.value,
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = city.value,
            onValueChange = { city.value = it },
            label = R.string.city,
            supportingText = stringResource(
                R.string.error_validation_blank,
                stringResource(R.string.office_name),
            ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        MifosButton(
            textResId = R.string.submit,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                validateFields(
                    firstName = firstName.value.text,
                    lastName = lastName.value.text,
                    guarantorType = guarantorType.value,
                    city = city.value.text,
                    guarantorTypeError = guarantorTypeError,
                    firstNameError = firstNameError,
                    lastNameError = lastNameError,
                ) { onSubmitted(it) }
            },
        )
    }
}

private fun validateFields(
    firstName: String,
    lastName: String,
    city: String,
    guarantorType: GuarantorType,
    guarantorTypeError: MutableState<Boolean> = mutableStateOf(false),
    firstNameError: MutableState<Boolean> = mutableStateOf(false),
    lastNameError: MutableState<Boolean> = mutableStateOf(false),
    onSubmitted: (GuarantorApplicationPayload) -> Unit,
) {
    when {
        firstName.isEmpty() -> {
            firstNameError.value = true
        }

        lastName.isEmpty() -> {
            lastNameError.value = true
        }

        guarantorType.value.isNullOrEmpty() -> {
            guarantorTypeError.value = true
        }

        else -> {
            onSubmitted(
                GuarantorApplicationPayload(
                    firstName = firstName,
                    lastName = lastName,
                    city = city,
                    guarantorTypeId = guarantorType.id,
                ),
            )
        }
    }
}

@DevicePreviews
@Composable
private fun AddGuarantorScreenPreview() {
    MifosMobileTheme {
        AddGuarantorScreen(
            uiState = GuarantorAddUiState.Template(GuarantorTemplatePayload()),
            guarantorItem = GuarantorPayload(),
            navigateBack = {},
            onSubmitted = {},
        )
    }
}

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ktor.util.pipeline.StackWalkingFailedFrame.context
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.add_guarantor
import mifos_mobile.feature.guarantor.generated.resources.error_validation_blank
import mifos_mobile.feature.guarantor.generated.resources.first_name
import mifos_mobile.feature.guarantor.generated.resources.guarantor_type
import mifos_mobile.feature.guarantor.generated.resources.update_guarantor
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorType
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay


@Composable
internal fun AddGuarantorScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddGuarantorViewModel = koinViewModel(),
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
    val guarantorTypeOptions = rememberSaveable { mutableStateOf(listOf<GuarantorType>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    MifosScaffold(
        topBarTitle = if (guarantorItem == null) stringResource(Res.string.add_guarantor) else stringResource(
            Res.string.update_guarantor,
        ),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        backPress = navigateBack,
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
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = getString(uiState.messageStringRes)
                            )
                        }
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
    var firstName by rememberSaveable{mutableStateOf("")}
    var lastName by rememberSaveable{mutableStateOf("")}
    var city by rememberSaveable{mutableStateOf("")}


    val guarantorType = rememberSaveable { mutableStateOf(GuarantorType()) }

    var firstNameError by rememberSaveable { mutableStateOf(false) }
    var lastNameError by rememberSaveable { mutableStateOf(false) }
    var guarantorTypeError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = guarantorItem) {
        firstName = guarantorItem?.firstname ?: ""
        lastName = guarantorItem?.lastname ?: ""
        city = guarantorItem?.city ?: ""
        guarantorType.value = guarantorItem?.guarantorType ?: GuarantorType()
    }

    LaunchedEffect(key1 = firstName) { firstNameError = false }
    LaunchedEffect(key1 = lastName) { lastNameError= false }
    LaunchedEffect(key1 = guarantorType.value) { guarantorTypeError = false }

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        MifosDropDownTextField(
            optionsList = guarantorTypeOptions.filter { it.id == 3L }.mapNotNull { it.value },
            selectedOption = guarantorType.value.value,
            labelResId = Res.string.guarantor_type,
            error = guarantorTypeError,
            onClick = { _, item ->
                guarantorType.value =
                    guarantorTypeOptions.find { it.value == item } ?: GuarantorType()
            },
            supportingText = stringResource(
                Res.string.error_validation_blank,
                stringResource(Res.string.guarantor_type),
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = firstName,
            onValueChange = { firstName = it },
            label = stringResource(Res.string.first_name),
            supportingText = stringResource(
                Res.string.error_validation_blank,
                stringResource(Res.string.first_name),
            ),
            error = firstNameError,
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

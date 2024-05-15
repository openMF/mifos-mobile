package org.mifos.mobile.ui.guarantor.guarantor_add

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.models.guarantor.GuarantorType
import org.mifos.mobile.utils.Network


@Composable
fun AddGuarantorScreen(
    viewModel: AddGuarantorViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()
    val guarantorItem = viewModel.guarantorItem.collectAsStateWithLifecycle()

    AddGuarantorScreen(
        uiState = uiState.value,
        guarantorItem = guarantorItem.value,
        navigateBack = navigateBack,
        onSubmitted = {
            when (guarantorItem.value) {
                null -> viewModel.createGuarantor(it)
                else -> viewModel.updateGuarantor(it)
            }
        }
    )
}

@Composable
fun AddGuarantorScreen(
    uiState: GuarantorAddUiState,
    guarantorItem: GuarantorPayload?,
    navigateBack: () -> Unit,
    onSubmitted: (GuarantorApplicationPayload) -> Unit
) {
    val context = LocalContext.current
    val guarantorTypeOptions = rememberSaveable { mutableStateOf(listOf<GuarantorType>()) }

    MFScaffold(
        topBarTitleResId = if(guarantorItem == null) R.string.add_guarantor else R.string.update_guarantor,
        navigateBack = navigateBack,
        scaffoldContent = {
            Box(modifier = Modifier.padding(it)) {
                AddGuarantorContent(
                    guarantorItem = guarantorItem,
                    onSubmitted = onSubmitted,
                    guarantorTypeOptions = guarantorTypeOptions.value
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
                        guarantorTypeOptions.value = uiState.guarantorTemplatePayload?.guarantorTypeOptions?.toList() ?: listOf()
                    }

                    is GuarantorAddUiState.Success -> {
                        Toast.makeText(context, stringResource(id = uiState.messageResId), Toast.LENGTH_SHORT).show()
                        navigateBack()
                    }
                }
            }
        }
    )
}


@Composable
fun AddGuarantorContent(
    modifier: Modifier = Modifier,
    guarantorItem: GuarantorPayload?,
    guarantorTypeOptions: List<GuarantorType>,
    onSubmitted: (GuarantorApplicationPayload) -> Unit
) {
    val scrollState = rememberScrollState()
    var firstName = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue( "")) }
    var lastName = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue( "")) }
    var city = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue( "")) }
    var guarantorType = rememberSaveable { mutableStateOf(GuarantorType()) }

    var firstNameError = rememberSaveable { mutableStateOf(false) }
    var lastNameError = rememberSaveable { mutableStateOf(false) }
    var guarantorTypeError = rememberSaveable { mutableStateOf(false) }

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
            .padding(bottom = 16.dp)
    ) {

        MifosDropDownTextField(
            optionsList = guarantorTypeOptions.filter { it.id == 3L }.mapNotNull { it.value },
            selectedOption = guarantorType.value.value,
            labelResId = R.string.guarantor_type,
            error = guarantorTypeError.value,
            onClick = { index, item ->
                guarantorType.value = guarantorTypeOptions.find { it.value == item } ?: GuarantorType()
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
            error = firstNameError.value
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
            error = lastNameError.value
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

        Button(
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
                ){ onSubmitted(it) }
            },
            content = {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = stringResource(id = R.string.submit),
                    style = MaterialTheme.typography.titleMedium
                )
            }
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
    onSubmitted: (GuarantorApplicationPayload) -> Unit
) {
    when {
        firstName.isEmpty() -> { firstNameError.value = true }
        lastName.isEmpty() -> { lastNameError.value = true }
        guarantorType.value.isNullOrEmpty() -> { guarantorTypeError.value = true }
        else -> {
            onSubmitted(
                GuarantorApplicationPayload(
                    firstName = firstName,
                    lastName = lastName,
                    city = city,
                    guarantorTypeId = guarantorType.id
                )
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AddGuarantorScreenPreview() {
    MifosMobileTheme {
        AddGuarantorScreen(
            uiState = GuarantorAddUiState.Template(GuarantorTemplatePayload()),
            guarantorItem = GuarantorPayload(),
            navigateBack ={},
            onSubmitted = {}
        )
    }
}
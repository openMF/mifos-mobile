package org.mifos.mobile.ui.guarantor_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.utils.Network

@Composable
fun GuarantorDetailScreen(
    viewModel: GuarantorDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    updateGuarantor: () -> Unit
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()
    val uiData = viewModel.guarantorUiData.collectAsStateWithLifecycle()

    GuarantorDetailScreen(
        uiState = uiState.value,
        data = uiData.value,
        navigateBack = navigateBack,
        deleteGuarantor = { viewModel.deleteGuarantor() },
        updateGuarantor = updateGuarantor
    )
}

@Composable
fun GuarantorDetailScreen(
    uiState: GuarantorDetailUiState,
    data: GuarantorPayload,
    navigateBack: () -> Unit,
    deleteGuarantor: () -> Unit,
    updateGuarantor: () -> Unit
) {
    val context = LocalContext.current
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }

    Column {
        GuarantorDetailTopBar(
            navigateBack = navigateBack,
            deleteGuarantor = { openAlertDialog = true },
            updateGuarantor = updateGuarantor
        )

        Box(modifier = Modifier.weight(1f)) {
            GuarantorDetailContent(data = data)

            when (uiState) {
                is GuarantorDetailUiState.Loading -> {
                    MifosProgressIndicator(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(0.8f))
                    )
                }

                is GuarantorDetailUiState.GuarantorDeletedSuccessfully -> {
                    Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                    navigateBack()
                }

                is GuarantorDetailUiState.ShowError -> {
                    ErrorComponent(
                        errorMessage = uiState.message
                            ?: stringResource(id = R.string.something_went_wrong)
                    )
                }

                is GuarantorDetailUiState.Initial -> Unit
            }
        }
    }

    if (openAlertDialog) {
        MifosAlertDialog(
            onDismissRequest = { openAlertDialog = false },
            dismissText = stringResource(id = R.string.dismiss_button_content_description),
            confirmationText = stringResource(id = R.string.yes),
            dialogTitle = stringResource(id = R.string.delete_guarantor),
            onConfirmation = {
                openAlertDialog = false
                deleteGuarantor()
            },
            dialogText = stringResource(
                R.string.dialog_are_you_sure_that_you_want_to_string,
                stringResource(R.string.delete_guarantor),
            ),
        )
    }
}

@Composable
fun ErrorComponent(errorMessage: String) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            icon = R.drawable.ic_portable_wifi_off_black_24dp,
            error = R.string.no_internet_connection,
            isRetryEnabled = false,
        )
    } else {
        LaunchedEffect(errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

class UiStatesParameterProvider : PreviewParameterProvider<GuarantorDetailUiState> {
    override val values: Sequence<GuarantorDetailUiState>
        get() = sequenceOf(
            GuarantorDetailUiState.Initial,
            GuarantorDetailUiState.Loading,
            GuarantorDetailUiState.ShowError(message = null),
            GuarantorDetailUiState.GuarantorDeletedSuccessfully(message = null)
        )
}

@Preview(showSystemUi = true)
@Composable
fun GuarantorDetailScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class) guarantorDetailUiState: GuarantorDetailUiState
) {
    MifosMobileTheme {
        GuarantorDetailScreen(
            uiState = guarantorDetailUiState,
            data = GuarantorPayload(),
            navigateBack = {},
            deleteGuarantor = {},
            updateGuarantor = {}
        )
    }
}
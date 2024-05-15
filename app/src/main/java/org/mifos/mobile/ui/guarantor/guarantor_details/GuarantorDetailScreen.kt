package org.mifos.mobile.ui.guarantor.guarantor_details

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.ui.guarantor.guarantor_list.GuarantorListContent
import org.mifos.mobile.utils.Network

@Composable
fun GuarantorDetailScreen(
    viewModel: GuarantorDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    updateGuarantor: (index: Int, loanId: Long) -> Unit
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()

    GuarantorDetailScreen(
        uiState = uiState.value,
        navigateBack = navigateBack,
        deleteGuarantor = { viewModel.deleteGuarantor(it) },
        updateGuarantor = { updateGuarantor(viewModel.index.value, viewModel.loanId.value) }
    )
}

@Composable
fun GuarantorDetailScreen(
    uiState: GuarantorDetailUiState,
    navigateBack: () -> Unit,
    deleteGuarantor: (Long) -> Unit,
    updateGuarantor: () -> Unit
) {
    val context = LocalContext.current
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }
    val guarantorItem = rememberSaveable { mutableStateOf(GuarantorPayload()) }

    MFScaffold(
        topBar = {
            GuarantorDetailTopBar(
                navigateBack = navigateBack,
                deleteGuarantor = { openAlertDialog = true },
                updateGuarantor = updateGuarantor
            )
        },
        scaffoldContent = {
            Box(modifier = Modifier.padding(it)) {
                GuarantorDetailContent(data = guarantorItem.value)
                when (uiState) {
                    is GuarantorDetailUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is GuarantorDetailUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isEmptyData = false,
                            isRetryEnabled = false,
                        )
                    }

                    is GuarantorDetailUiState.ShowDetail -> {
                        if(uiState.guarantorItem != null) {
                            guarantorItem.value = uiState.guarantorItem
                        } else {
                            MifosErrorComponent(isEmptyData = true)
                        }
                    }

                    is GuarantorDetailUiState.GuarantorDeletedSuccessfully -> {
                        Toast.makeText(context, stringResource(id = uiState.messageResId), Toast.LENGTH_SHORT).show()
                        navigateBack()
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
                        deleteGuarantor.invoke(guarantorItem.value.id ?: -1)
                        openAlertDialog = false
                    },
                    dialogText = stringResource(
                        R.string.dialog_are_you_sure_that_you_want_to_string,
                        stringResource(R.string.delete_guarantor),
                    ),
                )
            }
        }
    )
}

class UiStatesParameterProvider : PreviewParameterProvider<GuarantorDetailUiState> {
    override val values: Sequence<GuarantorDetailUiState>
        get() = sequenceOf(
            GuarantorDetailUiState.ShowDetail(GuarantorPayload()),
            GuarantorDetailUiState.Loading,
            GuarantorDetailUiState.Error(message = null),
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
            navigateBack = {},
            deleteGuarantor = {},
            updateGuarantor = {}
        )
    }
}
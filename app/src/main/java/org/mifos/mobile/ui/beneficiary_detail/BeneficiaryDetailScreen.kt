package org.mifos.mobile.ui.beneficiary_detail

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary

@Composable
fun BeneficiaryDetailScreen(
    viewModel: BeneficiaryDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    updateBeneficiary: () -> Unit,
) {
    val uiState by viewModel.beneficiaryDetailsUiStates.collectAsStateWithLifecycle()
    val beneficiary by viewModel.beneficiary.collectAsStateWithLifecycle()

    BeneficiaryDetailScreen(
        beneficiary = beneficiary,
        uiState = uiState,
        navigateBack = navigateBack,
        updateBeneficiary = updateBeneficiary,
        deleteBeneficiary = {
            viewModel.deleteBeneficiary(it)
        }
    )
}

@Composable
fun BeneficiaryDetailScreen(
    beneficiary: Beneficiary?,
    uiState: BeneficiaryDetailsUiState,
    navigateBack: () -> Unit,
    updateBeneficiary: () -> Unit,
    deleteBeneficiary: (id: Long?) -> Unit,
) {
    var openDropdown by rememberSaveable { mutableStateOf(false) }
    var showAlert by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var deleteButtonClicked by remember { mutableStateOf(false) }
    var showBeneficiaryNullError by rememberSaveable {
        mutableStateOf(true)
    }

    if (showAlert) {
        MifosAlertDialog(
            onDismissRequest = {
                showAlert = !showAlert
            },
            dismissText = context.getString(R.string.cancel),
            onConfirmation = {
                showAlert = !showAlert
                deleteButtonClicked = true
                deleteBeneficiary.invoke(beneficiary?.id?.toLong())
            },
            confirmationText = context.getString(R.string.delete),
            dialogTitle = context.getString(R.string.delete_beneficiary),
            dialogText = context.getString(R.string.delete_beneficiary_confirmation) + "?"
        )
    }

    Scaffold(
        topBar = {
            BeneficiaryDetailTopAppBar(
                navigateBack = navigateBack,
                updateBeneficiaryClicked = updateBeneficiary,
                updateDropdownValue = { value ->
                    openDropdown = value
                    openDropdown
                },
                showAlert = {
                    showAlert = true
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            BeneficiaryDetailContent(
                beneficiary = beneficiary
            )

            when (uiState) {
                BeneficiaryDetailsUiState.Initial -> Unit

                BeneficiaryDetailsUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                BeneficiaryDetailsUiState.DeletedSuccessfully -> {
                    Toast.makeText(
                        context,
                        stringResource(id = R.string.beneficiary_deleted_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateBack.invoke()
                }

                is BeneficiaryDetailsUiState.ShowError -> {
                    if (deleteButtonClicked) {
                        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                        deleteButtonClicked = false
                    }
                    if (showBeneficiaryNullError) {
                        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                        showBeneficiaryNullError = false
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BeneficiaryDetailTopAppBar(
    navigateBack: () -> Unit,
    updateBeneficiaryClicked: () -> Unit,
    updateDropdownValue: (value: Boolean) -> Boolean,
    showAlert: () -> Unit
) {
    var openDropdown by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.beneficiary_detail)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        actions = {
            IconButton(
                onClick = { openDropdown = updateDropdownValue.invoke(!openDropdown) }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }

            if (openDropdown) {
                DropdownMenu(
                    expanded = openDropdown,
                    onDismissRequest = {
                        openDropdown = updateDropdownValue.invoke(!openDropdown)
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.update_beneficiary)) },
                        onClick = {
                            openDropdown = updateDropdownValue.invoke(!openDropdown)
                            updateBeneficiaryClicked.invoke()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.delete_beneficiary)) },
                        onClick = {
                            openDropdown = updateDropdownValue.invoke(!openDropdown)
                            showAlert.invoke()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF1B1B1F)
            else Color(0xFFFEFBFF)
        )
    )
}

class BeneficiaryDetailScreenUiStatesParameterProvider :
    PreviewParameterProvider<BeneficiaryDetailsUiState> {
    override val values: Sequence<BeneficiaryDetailsUiState>
        get() = sequenceOf(
            BeneficiaryDetailsUiState.Initial,
            BeneficiaryDetailsUiState.Loading,
            BeneficiaryDetailsUiState.DeletedSuccessfully,
            BeneficiaryDetailsUiState.ShowError(R.string.error_creating_beneficiary)
        )
}

@Composable
@Preview(showSystemUi = true)
fun PreviewBeneficiaryDetailScreen(
    @PreviewParameter(BeneficiaryDetailScreenUiStatesParameterProvider::class) beneficiaryDetailsUiState : BeneficiaryDetailsUiState
) {
    val beneficiary = Beneficiary(
        id = 1234,
        name = "Robert Brown",
        officeName = "Head office",
        clientName = "Tom carl",
        accountType = null,
        transferLimit = 2003030.00,
        accountNumber = "2345678"
    )
    MifosMobileTheme {
        BeneficiaryDetailScreen(
            beneficiary = beneficiary,
            uiState = beneficiaryDetailsUiState,
            navigateBack = {},
            updateBeneficiary = {},
            deleteBeneficiary = {},
        )
    }
}




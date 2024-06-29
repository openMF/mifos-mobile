package org.mifos.mobile.feature.beneficiary.beneficiary_application


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.guarantor.R


@Composable
fun BeneficiaryApplicationScreen(
    viewModel: BeneficiaryApplicationViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val beneficiaryState by viewModel.beneficiaryState.collectAsStateWithLifecycle()
    val beneficiary by viewModel.beneficiary.collectAsStateWithLifecycle()
    val uiState by viewModel.beneficiaryUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadBeneficiaryTemplate()
    }

    BeneficiaryApplicationScreen (
        uiState = uiState,
        navigateBack = navigateBack,
        beneficiaryState = beneficiaryState,
        beneficiary = beneficiary,
        onRetry = { viewModel.loadBeneficiaryTemplate() },
        onSubmit = { viewModel.submitBeneficiary(it) }
    )
}

@Composable
fun BeneficiaryApplicationScreen(
    uiState: BeneficiaryApplicationUiState,
    navigateBack: () -> Unit,
    beneficiaryState: BeneficiaryState,
    beneficiary: Beneficiary?,
    onRetry: () -> Unit,
    onSubmit: (BeneficiaryPayload) -> Unit
) {
    val context = LocalContext.current
    MFScaffold(
        topBarTitleResId = when(beneficiaryState) {
            BeneficiaryState.UPDATE -> R.string.update_beneficiary
            else -> R.string.add_beneficiary
        },
        navigateBack = navigateBack,
        scaffoldContent = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                when(uiState) {
                    is BeneficiaryApplicationUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = onRetry,
                            message = stringResource(id = uiState.errorResId)
                        )
                    }
                    is BeneficiaryApplicationUiState.Loading -> {
                        MifosProgressIndicator()
                    }
                    is BeneficiaryApplicationUiState.Success -> {
                        navigateBack()
                    }
                    is BeneficiaryApplicationUiState.Template -> {
                        BeneficiaryApplicationContent(
                            prefilledBeneficiary = beneficiary,
                            beneficiaryTemplate = uiState.beneficiaryTemplate,
                            beneficiaryState = beneficiaryState,
                            onSubmit = onSubmit
                        )
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BeneficiaryApplicationScreenPreview(
    @PreviewParameter(BeneficiaryApplicationUiPreview::class) uiState: BeneficiaryApplicationUiState
) {
    MifosMobileTheme {
        BeneficiaryApplicationScreen(
            uiState = uiState,
            navigateBack = { },
            onRetry = { },
            beneficiary = Beneficiary(),
            beneficiaryState = BeneficiaryState.CREATE_QR,
            onSubmit = { }
        )
    }
}

class BeneficiaryApplicationUiPreview : PreviewParameterProvider<BeneficiaryApplicationUiState> {
    override val values: Sequence<BeneficiaryApplicationUiState>
        get() = sequenceOf(
            BeneficiaryApplicationUiState.Template(BeneficiaryTemplate()),
            BeneficiaryApplicationUiState.Error(1),
            BeneficiaryApplicationUiState.Loading,
        )
}



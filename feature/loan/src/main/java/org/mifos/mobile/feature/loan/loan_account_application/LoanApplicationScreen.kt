package org.mifos.mobile.feature.loan.loan_account_application

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.R


@Composable
fun LoanApplicationScreen(
    viewModel: LoanApplicationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    reviewNewLoanApplication: () -> Unit,
    submitUpdateLoanApplication: () -> Unit
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()
    val uiData by viewModel.loanApplicationScreenData.collectAsStateWithLifecycle()
    val loanState by viewModel.loanState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = loanState) {
        viewModel.loadLoanApplicationTemplate(loanState)
    }

    LoanApplicationScreen(
        uiState = uiState,
        uiData = uiData,
        navigateBack = navigateBack,
        loanState = loanState,
        onRetry = { viewModel.loadLoanApplicationTemplate(loanState) },
        selectProduct = { viewModel.productSelected(it) },
        selectPurpose = { viewModel.purposeSelected(it) },
        setDisbursementDate = { viewModel.setDisburseDate(it) },
        reviewClicked = {
            viewModel.setPrincipalAmount(it)
            if (loanState == LoanState.CREATE) reviewNewLoanApplication()
            else submitUpdateLoanApplication()
        }
    )
}

@Composable
fun LoanApplicationScreen(
    uiState: LoanApplicationUiState,
    loanState: LoanState,
    uiData: LoanApplicationScreenData,
    navigateBack: () -> Unit,
    selectProduct: (Int) -> Unit,
    selectPurpose: (Int) -> Unit,
    setDisbursementDate: (String) -> Unit,
    reviewClicked: (String) -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            MifosTopBar(
                modifier = Modifier.fillMaxWidth(),
                navigateBack = { navigateBack() },
                title = {
                    Text(text = stringResource(
                        id = if (loanState == LoanState.CREATE) R.string.apply_for_loan
                        else R.string.update_loan
                    ))
                }
            )
        },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    LoanApplicationContent(
                        uiData = uiData,
                        selectProduct = selectProduct,
                        selectPurpose = selectPurpose,
                        reviewClicked = reviewClicked,
                        setDisbursementDate = setDisbursementDate
                    )
                    when (uiState) {
                        is LoanApplicationUiState.Success -> Unit

                        is LoanApplicationUiState.Loading -> { MifosProgressIndicatorOverlay() }

                        is LoanApplicationUiState.Error -> {
                            MifosErrorComponent(
                                isNetworkConnected = Network.isConnected(context),
                                isEmptyData = false,
                                isRetryEnabled = true,
                                onRetry = onRetry
                            )
                        }
                    }
                }
            }

        }
    )
}

class UiStatesParameterProvider : PreviewParameterProvider<LoanApplicationUiState> {
    override val values: Sequence<LoanApplicationUiState>
        get() = sequenceOf(
            LoanApplicationUiState.Error(R.string.something_went_wrong),
            LoanApplicationUiState.Loading,
            LoanApplicationUiState.Success
        )
}


@Preview(showSystemUi = true)
@Composable
fun ReviewLoanApplicationScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class) loanApplicationUiState: LoanApplicationUiState
) {
    MifosMobileTheme {
        LoanApplicationScreen(
            uiState = loanApplicationUiState,
            uiData = LoanApplicationScreenData(),
            loanState = LoanState.CREATE,
            navigateBack = {},
            selectPurpose = {},
            selectProduct = {},
            reviewClicked = {},
            setDisbursementDate = {},
            onRetry = {},
        )
    }
}


package org.mifos.mobile.feature.loan.loan_account_application

import android.content.Context
import androidx.compose.animation.core.updateTransition
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
import com.google.gson.Gson
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.R
import org.mifos.mobile.feature.loan.navigation.LoanRoute


@Composable
fun LoanApplicationScreen(
    viewModel: LoanApplicationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    reviewNewLoanApplication: (loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) -> Unit,
    submitUpdateLoanApplication: (loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) -> Unit,
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()
    val uiData by viewModel.loanApplicationScreenData.collectAsStateWithLifecycle()
    val loanState by viewModel.loanState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
            getLoanPayload(
                context = context,
                loanState = loanState,
                reviewNewLoanApplication = reviewNewLoanApplication,
                submitUpdateLoanApplication = submitUpdateLoanApplication,
                viewModel = viewModel
            )
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

private fun getLoanPayload(
    context: Context,
    viewModel: LoanApplicationViewModel,
    loanState: LoanState,
    reviewNewLoanApplication: (loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) -> Unit,
    submitUpdateLoanApplication: (loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) -> Unit,
) {
    val payload = LoansPayload().apply {
        clientId = viewModel.loanTemplate.clientId.takeIf { loanState == LoanState.CREATE }
        loanPurpose = viewModel.loanApplicationScreenData.value.selectedLoanPurpose ?: "Not provided"
        productName = viewModel.loanApplicationScreenData.value.selectedLoanProduct
        currency = viewModel.loanApplicationScreenData.value.currencyLabel
        if (viewModel.purposeId > 0) loanPurposeId = viewModel.purposeId
        productId = viewModel.productId
        principal = viewModel.loanApplicationScreenData.value.principalAmount?.toDoubleOrNull() ?: 0.0
        loanTermFrequency = viewModel.loanTemplate.termFrequency
        loanTermFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id
        loanType = "individual".takeIf { loanState == LoanState.CREATE }
        numberOfRepayments = viewModel.loanTemplate.numberOfRepayments
        repaymentEvery = viewModel.loanTemplate.repaymentEvery
        repaymentFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id
        interestRatePerPeriod = viewModel.loanTemplate.interestRatePerPeriod
        expectedDisbursementDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, viewModel.loanApplicationScreenData.value.disbursementDate)
        submittedOnDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, viewModel.loanApplicationScreenData.value.submittedDate).takeIf { loanState == LoanState.CREATE }
        transactionProcessingStrategyId = viewModel.loanTemplate.transactionProcessingStrategyId
        amortizationType = viewModel.loanTemplate.amortizationType?.id
        interestCalculationPeriodType = viewModel.loanTemplate.interestCalculationPeriodType?.id
        interestType = viewModel.loanTemplate.interestType?.id
    }
    
    val loansPayloadString = Gson().toJson(payload)
    when(loanState) {
        LoanState.CREATE -> reviewNewLoanApplication(
            loanState,
            loansPayloadString,
            viewModel.loanId.value,
            context.getString(R.string.string_and_string, context.getString(R.string.new_loan_application) + " ", viewModel.loanApplicationScreenData.value.clientName ?: ""),
            context.getString(R.string.string_and_string, context.getString(R.string.account_number) + " ", viewModel.loanApplicationScreenData.value.accountNumber ?: "")
        )
        LoanState.UPDATE -> submitUpdateLoanApplication(
            loanState,
            loansPayloadString,
            null,
            context.getString(R.string.string_and_string, context.getString(R.string.update_loan_application) + " ", viewModel.loanApplicationScreenData.value.clientName ?: ""),
            context.getString(R.string.string_and_string, context.getString(R.string.account_number) + " ", viewModel.loanApplicationScreenData.value.accountNumber ?: "")
        )
    }
}

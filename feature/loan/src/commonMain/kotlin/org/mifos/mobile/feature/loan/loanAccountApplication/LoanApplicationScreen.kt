/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountApplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.apply_for_loan
import mifos_mobile.feature.loan.generated.resources.update_loan
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay

// import mifos_mobile.feature.loan.generated.resources.update_loan_application
// import mifos_mobile.feature.loan.generated.resources.new_loan_application
// import mifos_mobile.feature.loan.generated.resources.account_number

@Composable
internal fun LoanApplicationScreen(
    navigateBack: () -> Unit,
    reviewNewLoanApplication: (
        loanState: LoanState,
        loansPayloadString: String,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
    submitUpdateLoanApplication: (
        loanState: LoanState,
        loansPayloadString: String,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanApplicationViewModel = koinViewModel(),
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()
    val uiData by viewModel.loanApplicationScreenData.collectAsStateWithLifecycle()
    val loanState by viewModel.loanState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = loanState) {
        viewModel.loadLoanApplicationTemplate(loanState)
    }

    LoanApplicationScreen(
        isOnline = isOnline,
        uiState = uiState,
        uiData = uiData,
        navigateBack = navigateBack,
        loanState = loanState,
        onRetry = { viewModel.loadLoanApplicationTemplate(loanState) },
        modifier = modifier,
        selectProduct = viewModel::productSelected,
        selectPurpose = viewModel::purposeSelected,
        setDisbursementDate = viewModel::setDisburseDate,
        reviewClicked = {
            viewModel.setPrincipalAmount(it)
            getLoanPayload(
                loanState = loanState,
                reviewNewLoanApplication = reviewNewLoanApplication,
                submitUpdateLoanApplication = submitUpdateLoanApplication,
                viewModel = viewModel,
            )
        },
    )
}

@Composable
private fun LoanApplicationScreen(
    isOnline: Boolean,
    uiState: LoanApplicationUiState,
    loanState: LoanState,
    uiData: LoanApplicationScreenData,
    navigateBack: () -> Unit,
    selectProduct: (Int) -> Unit,
    selectPurpose: (Int) -> Unit,
    setDisbursementDate: (String) -> Unit,
    reviewClicked: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MifosTopBar(
                modifier = Modifier.fillMaxWidth(),
                backPress = { navigateBack() },
                topBarTitle =
                stringResource(
                    if (loanState == LoanState.CREATE) {
                        Res.string.apply_for_loan
                    } else {
                        Res.string.update_loan
                    },
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LoanApplicationContent(
                        uiData = uiData,
                        selectProduct = selectProduct,
                        selectPurpose = selectPurpose,
                        reviewClicked = reviewClicked,
                        setDisbursementDate = setDisbursementDate,
                    )
                    when (uiState) {
                        is LoanApplicationUiState.Success -> Unit

                        is LoanApplicationUiState.Loading -> {
                            MifosProgressIndicatorOverlay()
                        }

                        is LoanApplicationUiState.Error -> {
                            MifosErrorComponent(
                                isNetworkConnected = isOnline,
                                isEmptyData = false,
                                isRetryEnabled = true,
                                onRetry = onRetry,
                            )
                        }
                    }
                }
            }
        },
    )
}

internal class UiStatesParameterProvider : PreviewParameterProvider<LoanApplicationUiState> {
    override val values: Sequence<LoanApplicationUiState>
        get() = sequenceOf(
            LoanApplicationUiState.Error("Something wen Wrong"),
            LoanApplicationUiState.Loading,
            LoanApplicationUiState.Success,
        )
}

@Preview
@Composable
private fun ReviewLoanApplicationScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    loanApplicationUiState: LoanApplicationUiState,
) {
    MifosMobileTheme {
        LoanApplicationScreen(
            isOnline = true,
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
    viewModel: LoanApplicationViewModel,
    loanState: LoanState,
    reviewNewLoanApplication: (
        loanState: LoanState,
        loansPayloadString: String,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
    submitUpdateLoanApplication: (
        loanState: LoanState,
        loansPayloadString: String,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
) {
    val payload = LoansPayload(
        clientId = viewModel.loanTemplate.clientId.takeIf { loanState == LoanState.CREATE },
        loanPurpose = viewModel.loanApplicationScreenData.value.selectedLoanPurpose ?: "Not provided",
        productName = viewModel.loanApplicationScreenData.value.selectedLoanProduct,
        currency = viewModel.loanApplicationScreenData.value.currencyLabel,
        loanPurposeId = if (viewModel.purposeId > 0) viewModel.purposeId else null,
        productId = viewModel.productId,
        principal = viewModel.loanApplicationScreenData.value.principalAmount?.toDoubleOrNull() ?: 0.0,
        loanTermFrequency = viewModel.loanTemplate.termFrequency,
        loanTermFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id,
        loanType = "individual".takeIf { loanState == LoanState.CREATE },
        numberOfRepayments = viewModel.loanTemplate.numberOfRepayments,
        repaymentEvery = viewModel.loanTemplate.repaymentEvery,
        repaymentFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id,
        interestRatePerPeriod = viewModel.loanTemplate.interestRatePerPeriod,
        expectedDisbursementDate = viewModel
            .loanApplicationScreenData.value.disbursementDate?.let {
                DateHelper.getSpecificFormat(
                    DateHelper.MONTH_FORMAT,
                    it,
                )
            },
        submittedOnDate = viewModel.loanApplicationScreenData.value.submittedDate?.let {
            DateHelper.getSpecificFormat(DateHelper.MONTH_FORMAT, it)
                .takeIf { loanState == LoanState.CREATE }
        },
        transactionProcessingStrategyId = viewModel.loanTemplate.transactionProcessingStrategyId,
        amortizationType = viewModel.loanTemplate.amortizationType?.id,
        interestCalculationPeriodType = viewModel.loanTemplate.interestCalculationPeriodType?.id,
        interestType = viewModel.loanTemplate.interestType?.id,
    )

//    val loansPayloadString = Gson().toJson(payload)
    val loansPayloadString = Json.encodeToString(payload)

    when (loanState) {
        LoanState.CREATE -> reviewNewLoanApplication(
            loanState,
            loansPayloadString,
            viewModel.loanId.value,
            "New Loan Application for ${viewModel.loanApplicationScreenData.value.clientName}",
            "Account Number ${viewModel.loanApplicationScreenData.value.accountNumber}",
//            context.getString(
//                Res.string.string_and_string,
//                context.getString(R.string.new_loan_application) + " ",
//                viewModel.loanApplicationScreenData.value.clientName ?: "",
//            ),
//            context.getString(
//                R.string.string_and_string,
//                context.getString(R.string.account_number) + " ",
//                viewModel.loanApplicationScreenData.value.accountNumber ?: "",
//            ),
//            (Res.string.new_loan_application + " " + viewModel.loanApplicationScreenData.value
//                .clientName)
//                ?: "",
//            (Res.string.account_number + " " + viewModel.loanApplicationScreenData.value
//                .accountNumber)
//                ?: "",
        )

        LoanState.UPDATE -> submitUpdateLoanApplication(
            loanState,
            loansPayloadString,
            null,
            "Update Loan Application for ${viewModel.loanApplicationScreenData.value.clientName}",
            "Account Number ${viewModel.loanApplicationScreenData.value.accountNumber}",
//            context.getString(
//                R.string.string_and_string,
//                context.getString(R.string.update_loan_application) + " ",
//                viewModel.loanApplicationScreenData.value.clientName ?: "",
//            ),
//            context.getString(
//                R.string.string_and_string,
//                context.getString(R.string.account_number) + " ",
//                viewModel.loanApplicationScreenData.value.accountNumber ?: "",
//            ),
        )
    }
}

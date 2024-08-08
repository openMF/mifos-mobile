package org.mifos.mobile.feature.loan.loan_account_summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDrawableSingleLine
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.loan.R
import org.mifos.mobile.feature.loan.loan_repayment_schedule.LoanUiState


@Composable
fun LoanAccountSummaryScreen(
    viewModel: LoanAccountSummaryViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val loanId by viewModel.loanId.collectAsStateWithLifecycle()
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = loanId) {
        viewModel.loadLoanAccountDetails()
    }

    LoanAccountSummaryScreen(
        navigateBack = navigateBack,
        uiState = uiState
    )
}

@Composable
fun LoanAccountSummaryScreen(
    navigateBack: () -> Unit,
    uiState: LoanAccountSummaryUiState
) {
    val context = LocalContext.current
    MFScaffold(
        topBarTitleResId = R.string.loan_summary,
        navigateBack = navigateBack,
        scaffoldContent = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is LoanAccountSummaryUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is LoanAccountSummaryUiState.Error -> {
                        MifosErrorComponent(isNetworkConnected = Network.isConnected(context))
                    }

                    is LoanAccountSummaryUiState.Success -> {
                        LoanAccountSummaryContent(
                            loanWithAssociations = uiState.loanWithAssociations
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun LoanAccountSummaryContent(
    loanWithAssociations: LoanWithAssociations?
) {
    var currencySymbol = loanWithAssociations?.currency?.displaySymbol
    if (currencySymbol == null) {
        currencySymbol = loanWithAssociations?.currency?.code ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MifosTextTitleDescSingleLine(
            modifier = Modifier.padding(horizontal = 14.dp),
            title = stringResource(id = R.string.account_short),
            description = loanWithAssociations?.accountNo ?: ""
        )

        MifosTextTitleDescSingleLine(
            modifier = Modifier.padding(horizontal = 14.dp),
            title = stringResource(id = R.string.loan_product),
            description = loanWithAssociations?.loanProductName ?: ""
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.principal),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.principal ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.interest),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.interestCharged ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.fees),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.feeChargesCharged ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.penalties),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.penaltyChargesCharged ?: 0.0
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.total_repayment),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.totalExpectedRepayment ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.total_paid),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.totalRepayment ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.interest_waived),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.interestWaived ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.penalties_waived),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.penaltyChargesWaived ?: 0.0
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.fees_waived),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.feeChargesWaived ?: 0.0
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.outstanding_balance),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.totalOutstanding ?: 0.0
                    )
                )
                MifosTextTitleDescDrawableSingleLine(
                    title = stringResource(id = R.string.account_status),
                    description = if (loanWithAssociations?.status?.active == true) stringResource(
                        id = R.string.active_uc
                    )
                    else stringResource(id = R.string.inactive_uc),
                    imageResId = if (loanWithAssociations?.status?.active == true) R.drawable.ic_check_circle_green_24px
                    else R.drawable.ic_report_problem_red_24px
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun LoanAccountSummaryPreview() {
    MifosMobileTheme {
        LoanAccountSummaryScreen(
            navigateBack = {},
            uiState = LoanAccountSummaryUiState.Success(loanWithAssociations = null)
        )
    }
}
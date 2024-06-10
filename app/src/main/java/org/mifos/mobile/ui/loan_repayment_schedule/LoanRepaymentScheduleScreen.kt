package org.mifos.mobile.ui.loan_repayment_schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.Periods
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.LoanUiState
import org.mifos.mobile.utils.Network

@Composable
fun LoanRepaymentScheduleScreen(
    viewmodel: LoanRepaymentScheduleViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    loanId: Long
) {
    val loanRepaymentScheduleUiState by viewmodel.loanUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewmodel.loanLoanWithAssociations(loanId)
    }

    LoanRepaymentScheduleScreen(
        loanRepaymentScheduleUiState = loanRepaymentScheduleUiState,
        navigateBack = navigateBack,
        onRetry = { viewmodel.loanLoanWithAssociations(loanId) }
    )
}

@Composable
fun LoanRepaymentScheduleScreen(
    loanRepaymentScheduleUiState: LoanUiState,
    navigateBack: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    MFScaffold(
        topBarTitleResId = R.string.loan_repayment_schedule,
        navigateBack = { navigateBack.invoke() }) { contentPadding ->
        when (loanRepaymentScheduleUiState) {
            LoanUiState.Loading -> {
                MifosProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                )
            }

            is LoanUiState.ShowError -> {
                MifosErrorComponent(
                    isNetworkConnected = Network.isConnected(context),
                    isRetryEnabled = true,
                    onRetry = onRetry
                )
            }

            is LoanUiState.ShowLoan -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(contentPadding)
                ) {
                    LoanRepaymentScheduleCard(loanRepaymentScheduleUiState.loanWithAssociations)
                    RepaymentScheduleTable(
                        periods = loanRepaymentScheduleUiState.loanWithAssociations.repaymentSchedule?.periods!!,
                        currency = loanRepaymentScheduleUiState.loanWithAssociations.currency?.displaySymbol
                            ?: "$"
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
fun LoanRepaymentScheduleCard(loanWithAssociations: LoanWithAssociations) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            LoanRepaymentScheduleCardItem(
                label = stringResource(id = R.string.account_number),
                value = loanWithAssociations.accountNo ?: "--"
            )
            LoanRepaymentScheduleCardItem(
                label = stringResource(id = R.string.disbursement_date),
                value = DateHelper.getDateAsString(loanWithAssociations.timeline?.expectedDisbursementDate)
            )
            LoanRepaymentScheduleCardItem(
                label = stringResource(id = R.string.no_of_payments),
                value = loanWithAssociations.numberOfRepayments.toString()
            )
        }
    }
}

@Composable
fun RepaymentScheduleTable(currency: String, periods: List<Periods>) {
    if (periods.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            item {
                Row {
                    TableCell(text = stringResource(id = R.string.s_no), weight = 0.5f)
                    TableCell(text = stringResource(id = R.string.date), weight = 1f)
                    TableCell(text = stringResource(id = R.string.loan_balance), weight = 1f)
                    TableCell(text = stringResource(id = R.string.repayment), weight = 1f)
                }
            }
            items(periods) { period ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell(text = "${periods.indexOf(period) + 1}", weight = 0.5f)
                    TableCell(text = DateHelper.getDateAsString(period.dueDate), weight = 1f)
                    TableCell(
                        text = if (period.principalOriginalDue == null) "$currency 0.00" else "$currency ${period.principalOriginalDue.toString()}",
                        weight = 1f
                    )
                    TableCell(
                        text = "$currency ${period.principalLoanBalanceOutstanding.toString()}",
                        weight = 1f
                    )
                }
            }
        }
    } else {
        EmptyDataView(icon = R.drawable.ic_charges, error = R.string.repayment_schedule)
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    val borderColor = if (isSystemInDarkTheme()) Color.Gray else Color.Black

    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, borderColor)
            .weight(weight)
            .padding(4.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoanRepaymentScheduleCardItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

class LoanRepaymentSchedulePreviewProvider : PreviewParameterProvider<LoanUiState> {
    override val values: Sequence<LoanUiState>
        get() = sequenceOf(
            LoanUiState.Loading,
            LoanUiState.ShowError(R.string.repayment_schedule),
            LoanUiState.ShowLoan(LoanWithAssociations())
        )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LoanRepaymentScheduleScreenPreview(
    @PreviewParameter(LoanRepaymentSchedulePreviewProvider::class) loanUiState: LoanUiState
) {
    MifosMobileTheme {
        LoanRepaymentScheduleScreen(
            loanRepaymentScheduleUiState = loanUiState,
            navigateBack = {}) {}
    }
}
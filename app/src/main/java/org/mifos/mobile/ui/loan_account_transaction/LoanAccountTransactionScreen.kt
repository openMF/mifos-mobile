package org.mifos.mobile.ui.loan_account_transaction

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.MifosSelfServiceApp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.ui.loan_account_transaction.LoanAccountTransactionUiState.Success
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper.getDateAsString
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Utils.formatTransactionType

@Composable
fun LoanAccountTransactionScreen(
    viewModel: LoanAccountTransactionViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {

    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()

    LoanAccountTransactionScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        retryConnection = { viewModel.loadLoanAccountDetails() }
    )
}

@Composable
fun LoanAccountTransactionScreen(
    uiState: LoanAccountTransactionUiState,
    navigateBack: () -> Unit,
    retryConnection: () -> Unit
) {
    val context = LocalContext.current
    var loanWithAssociations by rememberSaveable { mutableStateOf(LoanWithAssociations()) }

    Column(modifier = Modifier.fillMaxSize()) {
        MifosTopBar(
            navigateBack = navigateBack,
            title = { Text(text = stringResource(id = R.string.transactions)) }
        )

        Box(modifier = Modifier.weight(1f)) {
            LoanAccountTransactionContent(loanWithAssociations = loanWithAssociations)

            when (uiState) {
                is Success -> {
                    if(uiState.loanWithAssociations != null && uiState.loanWithAssociations.transactions?.isNotEmpty() == true) {
                        loanWithAssociations = uiState.loanWithAssociations
                    } else {
                        MifosErrorComponent(isEmptyData = true)
                    }
                }

                is LoanAccountTransactionUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is LoanAccountTransactionUiState.Error -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isEmptyData = false,
                        isRetryEnabled = true,
                        onRetry = retryConnection
                    )
                }
            }
        }

    }
}

@Composable
fun LoanAccountTransactionContent(
    loanWithAssociations: LoanWithAssociations,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = loanWithAssociations.loanProductName ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(items = loanWithAssociations.transactions?.toList().orEmpty()) {
                LoanAccountTransactionListItem(it)
            }
        }
    }
}

@Composable
fun LoanAccountTransactionListItem(transaction: Transaction?) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_local_atm_black_24dp),
            contentDescription = stringResource(id = R.string.atm_icon),
            modifier = Modifier.size(39.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatTransactionType(transaction?.type?.value),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row {
                Text(
                    text = stringResource(
                        id = R.string.string_and_string,
                        transaction?.currency?.displaySymbol ?: transaction?.currency?.code ?: "",
                        CurrencyUtil.formatCurrency(
                            MifosSelfServiceApp.context,
                            transaction?.amount ?: 0.0,
                        )
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = getDateAsString(transaction?.submittedOnDate),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }

    }
}

class LoanAccountTransactionUiStatesParameterProvider :
    PreviewParameterProvider<LoanAccountTransactionUiState> {
    override val values: Sequence<LoanAccountTransactionUiState>
        get() = sequenceOf(
            Success(LoanWithAssociations()),
            LoanAccountTransactionUiState.Error,
            LoanAccountTransactionUiState.Error,
            LoanAccountTransactionUiState.Loading,
        )
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoanAccountTransactionScreenPreview(
    @PreviewParameter(LoanAccountTransactionUiStatesParameterProvider::class) loanAccountTransactionUiState: LoanAccountTransactionUiState
) {
    MifosMobileTheme {
        LoanAccountTransactionScreen(
            uiState = loanAccountTransactionUiState,
            navigateBack = {},
            retryConnection = {}
        )
    }
}
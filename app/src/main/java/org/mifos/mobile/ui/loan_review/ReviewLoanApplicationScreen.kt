package org.mifos.mobile.ui.loan_review

import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.MFErrorParser
import org.mifos.mobile.utils.Network


@Composable
fun ReviewLoanApplicationScreen(
    uiState: ReviewLoanApplicationUiState,
    navigateBack: () -> Unit,
    navigateBackWithSuccess: () -> Unit,
    submit: () -> Unit,
) {
    val context = LocalContext.current
    Column(modifier= Modifier.fillMaxSize()) {
        MifosTopBar(
            modifier= Modifier.fillMaxWidth(),
            navigateBack = { navigateBack() },
            title = { Text(text = stringResource(id = R.string.update_loan)) }
        )
        when (uiState) {
            is ReviewLoanApplicationUiState.ShowContent -> {
                ReviewLoanApplicationContent(
                    modifier = Modifier.padding(16.dp),
                    data = uiState.reviewLoanApplicationUiData,
                    submit = submit
                )
            }

            is ReviewLoanApplicationUiState.Loading -> {
                MifosProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is ReviewLoanApplicationUiState.Error -> {
                ErrorComponent(errorThrowable = uiState.throwable)
            }

            is ReviewLoanApplicationUiState.Success -> {
                when (uiState.loanState) {
                    LoanState.CREATE ->
                        Toast.makeText(context, stringResource(id = R.string.loan_application_submitted_successfully), Toast.LENGTH_SHORT).show()

                    LoanState.UPDATE ->
                        Toast.makeText(context, stringResource(id = R.string.loan_application_updated_successfully), Toast.LENGTH_SHORT).show()
                }
                navigateBackWithSuccess()
            }
        }
    }

}

@Composable
fun ErrorComponent(
    errorThrowable: Throwable,
) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            icon = R.drawable.ic_portable_wifi_off_black_24dp,
            error = R.string.no_internet_connection,
            isRetryEnabled = false,
        )
    } else {
        EmptyDataView(
            modifier = Modifier.fillMaxSize(),
            icon = R.drawable.ic_error_black_24dp,
            error = R.string.something_went_wrong,
            errorString = MFErrorParser.errorMessage(errorThrowable)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ReviewLoanApplicationScreenPreview() {
    MifosMobileTheme {
        ReviewLoanApplicationScreen(uiState = ReviewLoanApplicationUiState
            .ShowContent(ReviewLoanApplicationUiData()), {}, {}, {}
        )
    }
}



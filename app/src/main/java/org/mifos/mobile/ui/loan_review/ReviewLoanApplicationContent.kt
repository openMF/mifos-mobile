package org.mifos.mobile.ui.loan_review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import java.util.Locale

@Composable
fun ReviewLoanApplicationContent(
    data: ReviewLoanApplicationUiData,
    modifier: Modifier = Modifier,
    submit: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = data.loanName ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = data.accountNo ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.product),
            description = data.loanProduct ?: "",
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.loan_purpose),
            description = data.loanPurpose ?: "",
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.principal),
            description = String.format(
                Locale.getDefault(),
                "%.2f", data.principal ?: 0.0,
            ),
            descriptionStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.currency),
            description = data.currency ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.submission_date),
            description = data.submissionDate ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.expected_disbursement_date),
            description = data.disbursementDate ?: ""
        )

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = { submit.invoke()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.submit_loan))
        }
    }
}

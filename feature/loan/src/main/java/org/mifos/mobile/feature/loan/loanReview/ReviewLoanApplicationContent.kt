/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanReview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.loan.R
import java.util.Locale

@Composable
internal fun ReviewLoanApplicationContent(
    data: ReviewLoanApplicationUiData,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
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
            descriptionStyle = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.loan_purpose),
            description = data.loanPurpose ?: "",
            descriptionStyle = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.principal),
            description = String.format(
                Locale.getDefault(),
                "%.2f",
                data.principal ?: 0.0,
            ),
            descriptionStyle = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.currency),
            description = data.currency ?: "",
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.submission_date),
            description = data.submissionDate ?: "",
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.expected_disbursement_date),
            description = data.disbursementDate ?: "",
        )

        Spacer(modifier = Modifier.height(26.dp))

        MifosButton(
            textResId = R.string.submit_loan,
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@DevicePreviews
@Composable
private fun ReviewLoanApplicationContentPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        ReviewLoanApplicationContent(
            data = ReviewLoanApplicationUiData(
                loanName = "Loan Name",
                accountNo = "Account No",
                loanProduct = "Loan Product",
                loanPurpose = "Loan Purpose",
                principal = 1000.0,
                currency = "USD",
                submissionDate = "2021-12-31",
                disbursementDate = "2022-01-01",
            ),
            onSubmit = {},
            modifier = modifier,
        )
    }
}

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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.currency
import mifos_mobile.feature.loan.generated.resources.expected_disbursement_date
import mifos_mobile.feature.loan.generated.resources.loan_purpose
import mifos_mobile.feature.loan.generated.resources.principal
import mifos_mobile.feature.loan.generated.resources.product
import mifos_mobile.feature.loan.generated.resources.submission_date
import mifos_mobile.feature.loan.generated.resources.submit_loan
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.common.formatAmount
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine

@Composable
internal fun ReviewLoanApplicationContent(
    data: ReviewLoanApplicationUiData,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        Text(
            text = data.loanName ?: "",
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = data.accountNo ?: "",
            style = MaterialTheme.typography.bodyMedium,
        )

        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.product),
            description = data.loanProduct ?: "",
            descriptionStyle = MaterialTheme.typography.bodyMedium,
        )

        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.loan_purpose),
            description = data.loanPurpose ?: "",
            descriptionStyle = MaterialTheme.typography.bodyMedium,
        )

        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.principal),
            description = formatAmount(data.principal ?: 0.0),
            descriptionStyle = MaterialTheme.typography.bodyMedium,
        )

        MifosTextTitleDescSingleLine(
            title = stringResource(Res.string.currency),
            description = data.currency ?: "",
        )

        MifosTextTitleDescSingleLine(
            title = stringResource(Res.string.submission_date),
            description = data.submissionDate ?: "",
        )

        MifosTextTitleDescSingleLine(
            title = stringResource(Res.string.expected_disbursement_date),
            description = data.disbursementDate ?: "",
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmit,
            content = {
                Text(
                    text = stringResource(Res.string.submit_loan),
                    style = MaterialTheme.typography.titleSmall,
                )
            },
        )
    }
}

@Preview
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

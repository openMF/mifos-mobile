/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_number
import mifos_mobile.feature.loan.generated.resources.currency
import mifos_mobile.feature.loan.generated.resources.due_date
import mifos_mobile.feature.loan.generated.resources.ic_charges
import mifos_mobile.feature.loan.generated.resources.ic_compare_arrows_black_24dp
import mifos_mobile.feature.loan.generated.resources.ic_qrcode_scan
import mifos_mobile.feature.loan.generated.resources.ic_surveys_48px
import mifos_mobile.feature.loan.generated.resources.loan_charges
import mifos_mobile.feature.loan.generated.resources.loan_summary
import mifos_mobile.feature.loan.generated.resources.loan_type
import mifos_mobile.feature.loan.generated.resources.make_payment
import mifos_mobile.feature.loan.generated.resources.monitor
import mifos_mobile.feature.loan.generated.resources.next_installment
import mifos_mobile.feature.loan.generated.resources.not_available
import mifos_mobile.feature.loan.generated.resources.outstanding_balance
import mifos_mobile.feature.loan.generated.resources.qr_code
import mifos_mobile.feature.loan.generated.resources.repayment_schedule
import mifos_mobile.feature.loan.generated.resources.string_and_string
import mifos_mobile.feature.loan.generated.resources.transactions
import mifos_mobile.feature.loan.generated.resources.view_charges
import mifos_mobile.feature.loan.generated.resources.view_loan_summary
import mifos_mobile.feature.loan.generated.resources.view_qr_code
import mifos_mobile.feature.loan.generated.resources.view_repayment
import mifos_mobile.feature.loan.generated.resources.view_transactions
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.core.ui.component.MonitorListItemWithIcon

@Composable
internal fun LoanAccountDetailContent(
    loanWithAssociations: LoanWithAssociations,
    viewLoanSummary: () -> Unit,
    viewCharges: () -> Unit,
    viewRepaymentSchedule: () -> Unit,
    viewTransactions: () -> Unit,
    viewQr: () -> Unit,
    makePayment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LoanAccountDetailsCard(
            loanWithAssociations = loanWithAssociations,
            makePayment = makePayment,
        )

        LoanMonitorComponent(
            viewLoanSummary = viewLoanSummary,
            viewCharges = viewCharges,
            viewRepaymentSchedule = viewRepaymentSchedule,
            viewTransactions = viewTransactions,
            viewQr = viewQr,
        )
    }
}

@Composable
private fun LoanAccountDetailsCard(
    loanWithAssociations: LoanWithAssociations,
    makePayment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isActive = loanWithAssociations.status?.active == true
    val currencySymbol = loanWithAssociations.summary?.currency?.code ?: "$"

    val dueDate = if (isActive) {
        val overdueSinceDate = loanWithAssociations.summary?.getOverdueSinceDate()
        overdueSinceDate?.let { DateHelper.getDateAsString(it) }
            ?: stringResource(Res.string.not_available)
    } else {
        stringResource(Res.string.not_available)
    }

    val nextInstallment = getNextInstallment(loanWithAssociations, currencySymbol)

    MifosCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.outstanding_balance),
                description = stringResource(
                    Res.string.string_and_string,
                    currencySymbol,
                    CurrencyFormatter.format(
                        loanWithAssociations.summary?.totalOutstanding,
                        currencySymbol,
                        5,
                    ),
                ),
                descriptionStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.next_installment),
                description = nextInstallment,
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.due_date),
                description = dueDate,
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.account_number),
                description = loanWithAssociations.accountNo ?: "",
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.loan_type),
                description = loanWithAssociations.loanType?.value ?: "",
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.currency),
                description = loanWithAssociations.summary?.currency?.code ?: "",
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            MifosButton(
                text = { Text(text = stringResource(Res.string.make_payment)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isActive,
                onClick = makePayment,
            )
        }
    }
}

@Composable
private fun LoanMonitorComponent(
    viewLoanSummary: () -> Unit,
    viewCharges: () -> Unit,
    viewRepaymentSchedule: () -> Unit,
    viewTransactions: () -> Unit,
    viewQr: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(Res.string.monitor),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        MonitorListItemWithIcon(
            titleId = Res.string.loan_summary,
            subTitleId = Res.string.view_loan_summary,
            iconId = Res.drawable.ic_surveys_48px,
            onClick = { viewLoanSummary.invoke() },
        )

        MonitorListItemWithIcon(
            titleId = Res.string.loan_charges,
            subTitleId = Res.string.view_charges,
            iconId = Res.drawable.ic_charges,
            onClick = { viewCharges.invoke() },
        )

        MonitorListItemWithIcon(
            titleId = Res.string.repayment_schedule,
            subTitleId = Res.string.view_repayment,
            iconId = Res.drawable.ic_charges,
            onClick = { viewRepaymentSchedule.invoke() },
        )

        MonitorListItemWithIcon(
            titleId = Res.string.transactions,
            subTitleId = Res.string.view_transactions,
            iconId = Res.drawable.ic_compare_arrows_black_24dp,
            onClick = { viewTransactions.invoke() },
        )

        MonitorListItemWithIcon(
            titleId = Res.string.qr_code,
            subTitleId = Res.string.view_qr_code,
            iconId = Res.drawable.ic_qrcode_scan,
            onClick = { viewQr.invoke() },
        )
    }
}

@Composable
private fun getNextInstallment(
    loanWithAssociations: LoanWithAssociations,
    currencySymbol: String,
): String {
    loanWithAssociations.repaymentSchedule?.periods?.forEach { period ->
        val dueDate = period.dueDate
        if (dueDate == loanWithAssociations.summary?.getOverdueSinceDate()) {
            return stringResource(
                Res.string.string_and_string,
                currencySymbol,
                CurrencyFormatter.format(
                    period.totalDueForPeriod ?: 0.0,
                    currencySymbol,
                    5,
                ),
            )
        }
    }
    return stringResource(Res.string.not_available)
}

@Preview
@Composable
private fun LoanAccountDetailContentPreview() {
    MifosMobileTheme {
        LoanAccountDetailContent(
            loanWithAssociations = LoanWithAssociations(),
            viewLoanSummary = {},
            viewCharges = {},
            viewRepaymentSchedule = {},
            viewTransactions = {},
            viewQr = {},
            makePayment = {},
        )
    }
}

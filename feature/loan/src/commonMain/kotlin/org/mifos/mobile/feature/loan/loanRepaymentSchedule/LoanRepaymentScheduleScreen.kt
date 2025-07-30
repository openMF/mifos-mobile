/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanRepaymentSchedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.todayIn
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_number
import mifos_mobile.feature.loan.generated.resources.disbursement_date
import mifos_mobile.feature.loan.generated.resources.due
import mifos_mobile.feature.loan.generated.resources.installments_left
import mifos_mobile.feature.loan.generated.resources.installments_paid
import mifos_mobile.feature.loan.generated.resources.loan_repayment_schedule
import mifos_mobile.feature.loan.generated.resources.not_active
import mifos_mobile.feature.loan.generated.resources.paid
import mifos_mobile.feature.loan.generated.resources.principal_paid_off
import mifos_mobile.feature.loan.generated.resources.repayment_schedule
import mifos_mobile.feature.loan.generated.resources.total_installments
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.loanaccount.component.LoanAccountCard

@Composable
internal fun LoanRepaymentScheduleScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanRepaymentScheduleViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LoanRepaymentScheduleEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    LoanRepaymentScheduleScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun LoanRepaymentScheduleDialog(
    dialogState: LoanRepaymentScheduleState.DialogState?,
    state: LoanRepaymentScheduleState,
    onAction: (LoanRepaymentScheduleAction) -> Unit,
) {
    when (dialogState) {
        is LoanRepaymentScheduleState.DialogState.Loading -> {
            MifosProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        is LoanRepaymentScheduleState.DialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.isOnline,
                isRetryEnabled = true,
                onRetry = { onAction(LoanRepaymentScheduleAction.RetryClicked) },
            )
        }
        null -> Unit
    }
}

@Composable
private fun LoanRepaymentScheduleScreen(
    state: LoanRepaymentScheduleState,
    onAction: (LoanRepaymentScheduleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.loan_repayment_schedule),
        onNavigationIconClick = { (onAction(LoanRepaymentScheduleAction.BackPress)) },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            state.loanWithAssociations?.let {
                LoanRepaymentScheduleCard(it)
                it.repaymentSchedule?.periods?.let { periodsList ->
                    RepaymentScheduleTable(
                        periods = periodsList,
                        currency = state.loanWithAssociations.currency?.displaySymbol ?: "",
                    )
                }
            }
        }
    }
    LoanRepaymentScheduleDialog(
        dialogState = state.dialogState,
        state = state,
        onAction = onAction,
    )
}

fun formatDate(date: List<Int>): String {
    return if (date.size == 3) {
        val day = date[2].toString().padStart(2, '0')
        val month = date[1].toString().padStart(2, '0')
        val year = date[0].toString().padStart(4, '0')
        "$day-$month-$year"
    } else {
        "--"
    }
}

@Composable
private fun LoanRepaymentScheduleCard(
    loanWithAssociations: LoanWithAssociations,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(vertical = DesignToken.padding.medium, horizontal = DesignToken.padding.large)
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(DesignToken.padding.medium),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
        ) {
            LoanRepaymentScheduleCardItem(
                label = stringResource(Res.string.account_number),
                value = loanWithAssociations.accountNo ?: "--",
            )
            LoanRepaymentScheduleCardItem(
                label = stringResource(Res.string.disbursement_date),
                value = formatDate(loanWithAssociations.timeline?.expectedDisbursementDate ?: emptyList()),
            )

            loanWithAssociations.summary?.let { summary ->
                LoanRepaymentScheduleCardItem(
                    label = stringResource(Res.string.principal_paid_off),
                    value = "${loanWithAssociations.currency?.displaySymbol ?: ""} ${summary.principalPaid ?: 0.0}",
                )

                val paidInstallments = loanWithAssociations.repaymentSchedule?.periods?.count { period ->
                    period.totalOutstandingForPeriod != null && period.totalOutstandingForPeriod!! <= 0 &&
                        period.principalOriginalDue != null && period.principalOriginalDue!! > 0
                } ?: 0

                LoanRepaymentScheduleCardItem(
                    label = stringResource(Res.string.installments_paid),
                    value = paidInstallments.toString(),
                )

                val totalInstallments = loanWithAssociations.numberOfRepayments ?: 0
                val installmentsLeft = totalInstallments - paidInstallments

                LoanRepaymentScheduleCardItem(
                    label = stringResource(Res.string.installments_left),
                    value = installmentsLeft.toString(),
                )
            }

            LoanRepaymentScheduleCardItem(
                label = stringResource(Res.string.total_installments),
                value = loanWithAssociations.numberOfRepayments.toString(),
            )
        }
    }
}

@Composable
private fun RepaymentScheduleTable(
    currency: String,
    periods: List<Periods>,
    modifier: Modifier = Modifier,
) {
    if (periods.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
        ) {
            items(periods) { period ->
                val periodIndex = periods.indexOf(period) + 1

                val status = when {
                    period.principalOriginalDue == null ||
                            period.principalOriginalDue!! <= 0 -> stringResource(Res.string.not_active)

                    period.totalOutstandingForPeriod != null &&
                            period.totalOutstandingForPeriod!! <= 0 -> stringResource(Res.string.paid)

                    period.totalOutstandingForPeriod != null &&
                            period.totalOutstandingForPeriod!! > 0 &&
                            !isDatePastDue(period.dueDate) -> stringResource(Res.string.due)

                    else -> stringResource(Res.string.not_active)
                }

                LoanAccountCard(
                    loanId = periodIndex.toLong(),
                    date = DateHelper.getDateAsString(period.dueDate),
                    amount = "$currency ${period.principalOriginalDue?.toInt() ?: 0}",
                    status = status,
                    onLoanClick = {},
                    onPaymentClick = {},
                    modifier = Modifier,
                )
            }
        }
    } else {
        EmptyDataView(icon = MifosIcons.Error, error = Res.string.repayment_schedule)
    }
}

private fun isDatePastDue(dueDate: List<Int>): Boolean {
    if (dueDate.size < 3) return false

    return try {
        val currentDate = kotlinx.datetime.Clock.System.todayIn(
            kotlinx.datetime.TimeZone.currentSystemDefault()
        )
        val dueDateLocal = LocalDate(dueDate[0], dueDate[1], dueDate[2])
        currentDate > dueDateLocal
    } catch (e: Exception) {
        false
    }
}

@Composable
private fun LoanRepaymentScheduleCardItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MifosTypography.labelMediumEmphasized,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = value,
            style = MifosTypography.labelMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun LoanRepaymentScheduleScreenPreview() {
    MifosMobileTheme {
        LoanRepaymentScheduleScreen(
            state = LoanRepaymentScheduleState(dialogState = null),
            onAction = {},
        )
    }
}

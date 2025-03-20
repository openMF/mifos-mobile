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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_number
import mifos_mobile.feature.loan.generated.resources.date
import mifos_mobile.feature.loan.generated.resources.disbursement_date
import mifos_mobile.feature.loan.generated.resources.loan_repayment_schedule
import mifos_mobile.feature.loan.generated.resources.no_of_payments
import mifos_mobile.feature.loan.generated.resources.principal
import mifos_mobile.feature.loan.generated.resources.repayment
import mifos_mobile.feature.loan.generated.resources.repayment_schedule
import mifos_mobile.feature.loan.generated.resources.s_no
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

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
        backPress = { (onAction(LoanRepaymentScheduleAction.BackPress)) },
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
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

@Composable
private fun LoanRepaymentScheduleCard(
    loanWithAssociations: LoanWithAssociations,
    modifier: Modifier = Modifier,
) {
    MifosCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LoanRepaymentScheduleCardItem(
                label = stringResource(Res.string.account_number),
                value = loanWithAssociations.accountNo ?: "--",
            )
            LoanRepaymentScheduleCardItem(
                label = stringResource(Res.string.disbursement_date),
                value = DateHelper.getDateAsString(
                    loanWithAssociations.timeline?.expectedDisbursementDate ?: emptyList(),
                ),
            )
            LoanRepaymentScheduleCardItem(
                label = stringResource(Res.string.no_of_payments),
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
                .fillMaxSize()
                .padding(12.dp),
        ) {
            item {
                Row {
                    TableCell(text = stringResource(Res.string.s_no), weight = 0.5f)
                    TableCell(text = stringResource(Res.string.date), weight = 1f)
                    TableCell(text = stringResource(Res.string.repayment), weight = 1f)
                    TableCell(text = stringResource(Res.string.principal), weight = 1f)
                }
            }
            items(periods) { period ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell(text = "${periods.indexOf(period) + 1}", weight = 0.5f)
                    TableCell(text = DateHelper.getDateAsString(period.dueDate), weight = 1f)
                    TableCell(
                        text = "$currency ${period.principalLoanBalanceOutstanding}",
                        weight = 1f,
                    )
                    TableCell(
                        text = if (period.principalOriginalDue == null) {
                            "$currency 0.00"
                        } else {
                            "$currency ${period.principalOriginalDue}"
                        },
                        weight = 1f,
                    )
                }
            }
        }
    } else {
        EmptyDataView(icon = MifosIcons.Error, error = Res.string.repayment_schedule)
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSystemInDarkTheme()) Color.Gray else Color.Black

    Text(
        text = text,
        modifier = modifier
            .border(1.dp, borderColor)
            .weight(weight)
            .padding(4.dp),
        textAlign = TextAlign.Center,
    )
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
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
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

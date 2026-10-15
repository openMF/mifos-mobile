/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_number_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_amount_and_balance
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_client_name_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_disbursement_date_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_export_pdf_error
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_export_pdf_error_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_export_to_pdf
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installment_totals
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_paid_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_period_details
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_principal_paid_off_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_product_type_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_repayment_schedule_pdf_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_balance
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_date
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_days
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_due
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_fees
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_in_advance
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_installment
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_interest
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_late
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_outstanding
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_paid
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_paid_date
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_penalties
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_table_header_principal
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_total_cost_of_loan
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_total_installments_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_total_label
import mifos_mobile.feature.loan_account.generated.resources.repayment_schedule
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosTableRow
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.pdf.Orientation
import org.mifos.mobile.core.ui.utils.pdf.PageConfig
import org.mifos.mobile.core.ui.utils.pdf.PageSize
import org.mifos.mobile.core.ui.utils.pdf.rememberPdfGenerator
import org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.pdf.RepaymentScheduleHtmlGenerator
import org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.pdf.RepaymentSchedulePdfStrings
import template.core.base.designsystem.theme.KptTheme

/**
 * The main composable for the repayment schedule screen.
 * It displays the repayment schedule for a loan account and allows the user to pay installments.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 * @param navigateToMakePaymentScreen A callback to navigate to the make payment screen.
 * @param viewModel The [RepaymentScheduleViewModel] for this screen.
 */
@Composable
internal fun RepaymentScheduleScreen(
    navigateBack: () -> Unit,
    navigateToMakePaymentScreen: (AccountDetails) -> Unit,
    viewModel: RepaymentScheduleViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val pdfGenerator = rememberPdfGenerator()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            RepaymentScheduleEvent.NavigateBack -> navigateBack.invoke()

            RepaymentScheduleEvent.ExportPdf -> {
                state.repaymentScheduleTableData?.let { tableData ->
                    coroutineScope.launch {
                        try {
                            val pdfStrings = createPdfStrings()
                            val htmlGenerator =
                                RepaymentScheduleHtmlGenerator(tableData, pdfStrings)
                            val htmlContent = htmlGenerator.generateHtml()
                            val fileName = "repayment_schedule_${tableData.accountNo}"
                            val pageConfig = PageConfig(
                                size = PageSize.A4,
                                orientation = Orientation.LANDSCAPE,
                                marginMm = 8,
                            )
                            pdfGenerator.generateAndSharePdf(htmlContent, fileName, pageConfig)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ensureActive()
                            viewModel.trySendAction(
                                RepaymentScheduleAction.PdfExportError(
                                    title = getString(Res.string.feature_loan_export_pdf_error_title),
                                    message = getString(Res.string.feature_loan_export_pdf_error),
                                ),
                            )
                        }
                    }
                }
            }

            is RepaymentScheduleEvent.PayInstallment -> {
                navigateToMakePaymentScreen(
                    AccountDetails(
                        accountId = event.accountId,
                        outstandingBalance = event.outStandingBalance,
                        transferType = event.transferTyp,
                        transferTarget = event.transferTarget,
                        transferSuccessDestination = event.transferSuccessDestination,
                    ),
                )
            }
        }
    }

    RepaymentScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    RepaymentDialogs(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * The content of the repayment schedule screen.
 * It displays the basic details of the loan and a table of repayment periods.
 *
 * @param state The [RepaymentScheduleState] for this screen.
 * @param onAction A callback to handle actions from the screen.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
internal fun RepaymentScreenContent(
    state: RepaymentScheduleState,
    onAction: (RepaymentScheduleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.repayment_schedule),
        onNavigateBack = {
            onAction(RepaymentScheduleAction.OnNavigateBack)
        },
        modifier = modifier,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        actions = {
            IconButton(onClick = { onAction(RepaymentScheduleAction.ExportToPdf) }) {
                Icon(
                    imageVector = MifosIcons.Export,
                    contentDescription = stringResource(Res.string.feature_loan_export_to_pdf),
                )
            }
        },
    ) {
        when (state.uiState) {
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(RepaymentScheduleAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(RepaymentScheduleAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                val scrollState = rememberScrollState()

                val smallWidth = DesignToken.sizes.tableCellWidthSmall
                val mediumWidth = DesignToken.sizes.tableCellWidthMedium
                val largeWidth = DesignToken.sizes.tableCellWidthLarge

                val columnWidths = remember(smallWidth, mediumWidth, largeWidth) {
                    listOf(
                        // #
                        smallWidth,
                        // Days
                        smallWidth,
                        // Date
                        mediumWidth,
                        // Paid Date
                        largeWidth,
                        // Balance
                        mediumWidth,
                        // Principal
                        mediumWidth,
                        // Interest
                        mediumWidth,
                        // Fees
                        mediumWidth,
                        // Penalties
                        mediumWidth,
                        // Due
                        mediumWidth,
                        // Paid
                        mediumWidth,
                        // In Advance
                        mediumWidth,
                        // Late
                        mediumWidth,
                        // Outstanding
                        mediumWidth,
                    )
                }

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            vertical = DesignToken.padding.large,
                        ),
                    horizontalAlignment = Alignment.Start,
                ) {
                    item {
                        MifosDetailsCard(
                            keyValuePairs = state.basicDetails,
                            modifier = Modifier.padding(
                                horizontal = DesignToken.padding.medium,
                            ),
                        )
                        Spacer(Modifier.height(KptTheme.spacing.md))
                    }

                    state.repaymentScheduleTableData?.let { tableData ->
                        stickyHeader {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(scrollState),
                            ) {
                                RepaymentTableHeader(columnWidths)
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(scrollState),
                            ) {
                                DisbursementRow(tableData, columnWidths)

                                tableData.periods.forEach { period ->
                                    RepaymentPeriodRow(period, columnWidths)
                                }
                                RepaymentTableFooter(tableData.totals, columnWidths)
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

/**
 * A composable that displays a dialog for the repayment schedule screen.
 * It can show an error dialog.
 *
 * @param dialogState The state of the dialog to display.
 * @param onAction A callback to handle actions from the dialog.
 */
@Composable
internal fun RepaymentDialogs(
    dialogState: RepaymentScheduleState.DialogState?,
    onAction: (RepaymentScheduleAction) -> Unit,
) {
    when (dialogState) {
        is RepaymentScheduleState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = dialogState.message,
                    title = dialogState.title,
                ),
                onDismissRequest = { onAction(RepaymentScheduleAction.DismissErrorDialog) },
            )
        }

        null -> Unit
    }
}

private suspend fun createPdfStrings(): RepaymentSchedulePdfStrings {
    return RepaymentSchedulePdfStrings(
        title = getString(Res.string.feature_loan_repayment_schedule_pdf_title),
        clientNameLabel = getString(Res.string.feature_loan_client_name_label),
        accountNumberLabel = getString(Res.string.feature_loan_account_number_label),
        productNameLabel = getString(Res.string.feature_loan_product_type_label),
        disbursementDateLabel = getString(Res.string.feature_loan_disbursement_date_label),
        installmentsLabel = getString(Res.string.feature_loan_total_installments_label),
        paidLabel = getString(Res.string.feature_loan_paid_label),
        totalLabel = getString(Res.string.feature_loan_total_label),
        principalPaidLabel = getString(Res.string.feature_loan_principal_paid_off_label),
        periodDetailsHeader = getString(Res.string.feature_loan_period_details),
        loanAmountBalanceHeader = getString(Res.string.feature_loan_amount_and_balance),
        totalCostLoanHeader = getString(Res.string.feature_loan_total_cost_of_loan),
        installmentTotalsHeader = getString(Res.string.feature_loan_installment_totals),
        hNo = getString(Res.string.feature_loan_table_header_installment),
        hDays = getString(Res.string.feature_loan_table_header_days),
        hDate = getString(Res.string.feature_loan_table_header_date),
        hPaidDate = getString(Res.string.feature_loan_table_header_paid_date),
        hBalance = getString(Res.string.feature_loan_table_header_balance),
        hPrincipal = getString(Res.string.feature_loan_table_header_principal),
        hInterest = getString(Res.string.feature_loan_table_header_interest),
        hFees = getString(Res.string.feature_loan_table_header_fees),
        hPenalties = getString(Res.string.feature_loan_table_header_penalties),
        hDue = getString(Res.string.feature_loan_table_header_due),
        hPaid = getString(Res.string.feature_loan_table_header_paid),
        hInAdvance = getString(Res.string.feature_loan_table_header_in_advance),
        hLate = getString(Res.string.feature_loan_table_header_late),
        hOutstanding = getString(Res.string.feature_loan_table_header_outstanding),
    )
}

@Composable
private fun RepaymentTableHeader(widths: List<Dp>) {
    val headers = listOf(
        Res.string.feature_loan_table_header_installment,
        Res.string.feature_loan_table_header_days,
        Res.string.feature_loan_table_header_date,
        Res.string.feature_loan_table_header_paid_date,
        Res.string.feature_loan_table_header_balance,
        Res.string.feature_loan_table_header_principal,
        Res.string.feature_loan_table_header_interest,
        Res.string.feature_loan_table_header_fees,
        Res.string.feature_loan_table_header_penalties,
        Res.string.feature_loan_table_header_due,
        Res.string.feature_loan_table_header_paid,
        Res.string.feature_loan_table_header_in_advance,
        Res.string.feature_loan_table_header_late,
        Res.string.feature_loan_table_header_outstanding,
    )

    MifosTableRow(
        cells = headers.map { headerRes ->
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = DesignToken.padding.small,
                            horizontal = DesignToken.padding.extraSmall,
                        ),
                ) {
                    Text(
                        text = stringResource(headerRes),
                        style = MifosTypography.labelMediumEmphasized,
                    )
                }
            }
        },
        widths = widths,
        edgeOffset = DesignToken.spacing.medium,
        cornerShape = DesignToken.shapes.topCornerDp8,
    )
}

@Composable
private fun DisbursementRow(
    data: RepaymentScheduleState.RepaymentScheduleTableData,
    widths: List<Dp>,
) {
    val cells: List<@Composable () -> Unit> = List(14) { index ->
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = DesignToken.padding.small,
                        horizontal = DesignToken.padding.extraSmall,
                    ),
            ) {
                val text = when (index) {
                    2 -> data.disbursementDate
                    4 -> data.loanAmount
                    else -> ""
                }
                Text(text = text, style = MifosTypography.labelMedium)
            }
        }
    }
    MifosTableRow(
        cells = cells,
        widths = widths,
        backgroundColor = Color.Transparent,
        edgeOffset = DesignToken.spacing.medium,
    )
}

@Composable
private fun RepaymentPeriodRow(
    period: RepaymentScheduleState.RepaymentScheduleTableData.PeriodData,
    widths: List<Dp>,
) {
    val periodValues = listOf(
        period.number, period.days, period.dueDate, period.paidDate,
        period.balanceOfLoan, period.principalDue, period.interest,
        period.fees, period.penalties, period.due, period.paid,
        period.inAdvance, period.late, period.outstanding,
    )
    MifosTableRow(
        cells = periodValues.map { value ->
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = DesignToken.padding.small,
                            horizontal = DesignToken.padding.extraSmall,
                        ),
                ) {
                    Text(text = value, style = MifosTypography.labelMedium)
                }
            }
        },
        widths = widths,
        backgroundColor = Color.Transparent,
        edgeOffset = DesignToken.spacing.medium,
    )
}

@Composable
private fun RepaymentTableFooter(
    totals: RepaymentScheduleState.RepaymentScheduleTableData.TotalsData,
    widths: List<Dp>,
) {
    val footerValues = listOf(
        stringResource(Res.string.feature_loan_total_label), "", "", "", "",
        totals.principalDue, totals.interest, totals.fees, totals.penalties,
        totals.due, totals.paid, totals.inAdvance, totals.late, totals.outstanding,
    )

    MifosTableRow(
        cells = footerValues.map { value ->
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = DesignToken.padding.small,
                            horizontal = DesignToken.padding.extraSmall,
                        ),
                ) {
                    Text(text = value, style = MifosTypography.labelMediumEmphasized)
                }
            }
        },
        widths = widths,
        edgeOffset = DesignToken.spacing.medium,
        cornerShape = DesignToken.shapes.bottomCornerDp8,
    )
}

@Preview
@Composable
private fun Repayment_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.padding(KptTheme.spacing.md),
        ) {
            RepaymentScreenContent(
                state = RepaymentScheduleState(dialogState = null),
                onAction = {},
            )
        }
    }
}

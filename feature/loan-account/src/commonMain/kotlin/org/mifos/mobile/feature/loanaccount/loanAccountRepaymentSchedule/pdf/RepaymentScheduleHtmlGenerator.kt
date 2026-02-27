/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.pdf

import kotlinx.html.BODY
import kotlinx.html.TBODY
import kotlinx.html.b
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.tfoot
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import org.mifos.mobile.core.ui.utils.pdf.HtmlTemplateGenerator
import org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.RepaymentScheduleState
import kotlin.time.ExperimentalTime

/**
 * Data model for localized strings used in the PDF.
 */
internal data class RepaymentSchedulePdfStrings(
    val title: String,
    val clientNameLabel: String,
    val accountNumberLabel: String,
    val productNameLabel: String,
    val disbursementDateLabel: String,
    val installmentsLabel: String,
    val paidLabel: String,
    val totalLabel: String,
    val principalPaidLabel: String,
    val periodDetailsHeader: String,
    val loanAmountBalanceHeader: String,
    val totalCostLoanHeader: String,
    val installmentTotalsHeader: String,
    val hNo: String,
    val hDays: String,
    val hDate: String,
    val hPaidDate: String,
    val hBalance: String,
    val hPrincipal: String,
    val hInterest: String,
    val hFees: String,
    val hPenalties: String,
    val hDue: String,
    val hPaid: String,
    val hInAdvance: String,
    val hLate: String,
    val hOutstanding: String,
)

/**
 * Generates HTML content for the Repayment Schedule PDF.
 */
internal class RepaymentScheduleHtmlGenerator(
    private val tableData: RepaymentScheduleState.RepaymentScheduleTableData,
    private val strings: RepaymentSchedulePdfStrings,
) : HtmlTemplateGenerator() {

    override fun getTitle(): String = strings.title

    @OptIn(ExperimentalTime::class)
    override fun BODY.generateBody() {
        div(classes = "container") {
            div(classes = "header-content") {
                h1 { +"${strings.title} #${tableData.accountNo}" }

                div(classes = "loan-info-grid") {
                    div {
                        p {
                            b { +"${strings.clientNameLabel}: " }
                            +tableData.clientName
                        }
                        p {
                            b { +"${strings.accountNumberLabel}: " }
                            +tableData.accountNo
                        }
                    }
                    div {
                        p {
                            b { +"${strings.productNameLabel}: " }
                            +tableData.productName
                        }
                        p {
                            b { +"${strings.disbursementDateLabel}: " }
                            +tableData.disbursementDate
                        }
                    }
                    div {
                        p {
                            b { +"${strings.installmentsLabel}: " }
                            +"${tableData.installmentsPaid} ${strings.paidLabel}"
                            +" / "
                            +"${tableData.totalInstallments} ${strings.totalLabel}"
                        }
                        p {
                            b { +"${strings.principalPaidLabel}: " }
                            +tableData.principalPaid
                        }
                    }
                }
            }

            table {
                id = "repaymentSchedule"
                thead {
                    tr {
                        th {
                            attributes["colspan"] = "4"
                            +strings.periodDetailsHeader
                        }
                        th {
                            attributes["colspan"] = "2"
                            +strings.loanAmountBalanceHeader
                        }
                        th {
                            attributes["colspan"] = "4"
                            +strings.totalCostLoanHeader
                        }
                        th {
                            attributes["colspan"] = "4"
                            +strings.installmentTotalsHeader
                        }
                    }
                    tr {
                        th { +strings.hNo }
                        th { +strings.hDays }
                        th { +strings.hDate }
                        th { +strings.hPaidDate }
                        th { +strings.hBalance }
                        th { +strings.hPrincipal }
                        th { +strings.hInterest }
                        th { +strings.hFees }
                        th { +strings.hPenalties }
                        th { +strings.hDue }
                        th { +strings.hPaid }
                        th { +strings.hInAdvance }
                        th { +strings.hLate }
                        th { +strings.hOutstanding }
                    }
                }
                tbody {
                    tr(classes = "disbursement-row") {
                        td {
                            attributes["colspan"] = "3"
                            +tableData.disbursementDate
                        }
                        td {
                            attributes["colspan"] = "2"
                            +tableData.loanAmount
                        }
                        td {
                            attributes["colspan"] = "9"
                            +""
                        }
                    }
                    generateTableRows()
                }
                tfoot {
                    tr {
                        td {
                            attributes["colspan"] = "1"
                            b { +strings.totalLabel }
                        }
                        td {
                            attributes["colspan"] = "4"
                            +""
                        }
                        td { b { +tableData.totals.principalDue } }
                        td { b { +tableData.totals.interest } }
                        td { b { +tableData.totals.fees } }
                        td { b { +tableData.totals.penalties } }
                        td { b { +tableData.totals.due } }
                        td { b { +tableData.totals.paid } }
                        td { b { +tableData.totals.inAdvance } }
                        td { b { +tableData.totals.late } }
                        td { b { +tableData.totals.outstanding } }
                    }
                }
            }
        }
    }

    private fun TBODY.generateTableRows() {
        tableData.periods.forEach { period ->
            tr {
                if (period.isPaid) classes = setOf("paid")
                td { +period.number }
                td { +period.days }
                td { +period.dueDate }
                td { +period.paidDate }
                td { +period.balanceOfLoan }
                td { +period.principalDue }
                td { +period.interest }
                td { +period.fees }
                td { +period.penalties }
                td { +period.due }
                td { +period.paid }
                td { +period.inAdvance }
                td { +period.late }
                td { +period.outstanding }
            }
        }
    }

    override fun getAdditionalStyles(): String {
        return """
        .container { padding: 0; font-family: sans-serif; }
        .header-content { margin-bottom: 20px; }
        h1 { color: #1976d2; font-size: 16pt; solid #1976d2; padding-bottom: 5px; margin-bottom: 15px; }
        .loan-info-grid { display: table; width: 100%; font-size: 8pt; }
        .loan-info-grid > div { display: table-cell; width: 33.33%; vertical-align: top; }
        table { width: 100%; border-collapse: collapse; font-size: 7pt; }
        thead th { background-color: #1976d2 !important; color: #ffffff !important; font-weight: bold; padding: 6px 4px; border: 1px solid #0d47a1; text-align: center !important; }
        td { padding: 3px 5px; border: 1px solid #bdc3c7; text-align: right !important; }
        tbody tr.disbursement-row { background-color: #f2f2f2; font-style: italic; color: #555; }
        tbody tr.disbursement-row td:nth-child(5) { text-align: right !important; font-weight: bold; color: #000; }
        tbody tr.paid { background-color: #e8f8f5; }
        tfoot tr td { background-color: #1976d2 !important; color: #ffffff !important; border: 1px solid #0d47a1; font-weight: bold; padding: 6px 4px; }
        """.trimIndent()
    }
}

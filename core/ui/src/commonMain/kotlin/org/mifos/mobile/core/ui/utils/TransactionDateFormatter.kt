/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.runtime.Composable
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.tx_date_today
import mifos_mobile.core.ui.generated.resources.tx_date_yesterday
import mifos_mobile.core.ui.generated.resources.tx_month_apr
import mifos_mobile.core.ui.generated.resources.tx_month_aug
import mifos_mobile.core.ui.generated.resources.tx_month_dec
import mifos_mobile.core.ui.generated.resources.tx_month_feb
import mifos_mobile.core.ui.generated.resources.tx_month_jan
import mifos_mobile.core.ui.generated.resources.tx_month_jul
import mifos_mobile.core.ui.generated.resources.tx_month_jun
import mifos_mobile.core.ui.generated.resources.tx_month_mar
import mifos_mobile.core.ui.generated.resources.tx_month_may
import mifos_mobile.core.ui.generated.resources.tx_month_nov
import mifos_mobile.core.ui.generated.resources.tx_month_oct
import mifos_mobile.core.ui.generated.resources.tx_month_sep
import mifos_mobile.core.ui.generated.resources.tx_type_accrual
import mifos_mobile.core.ui.generated.resources.tx_type_approval
import mifos_mobile.core.ui.generated.resources.tx_type_charge_off
import mifos_mobile.core.ui.generated.resources.tx_type_charge_payment
import mifos_mobile.core.ui.generated.resources.tx_type_credit_balance_refund
import mifos_mobile.core.ui.generated.resources.tx_type_deposit
import mifos_mobile.core.ui.generated.resources.tx_type_dividend_payout
import mifos_mobile.core.ui.generated.resources.tx_type_fee_deduction
import mifos_mobile.core.ui.generated.resources.tx_type_initiate_transfer
import mifos_mobile.core.ui.generated.resources.tx_type_interest_posting
import mifos_mobile.core.ui.generated.resources.tx_type_interest_waiver
import mifos_mobile.core.ui.generated.resources.tx_type_loan_charge_added
import mifos_mobile.core.ui.generated.resources.tx_type_overpayment
import mifos_mobile.core.ui.generated.resources.tx_type_recalculate_interest
import mifos_mobile.core.ui.generated.resources.tx_type_recover_repayment
import mifos_mobile.core.ui.generated.resources.tx_type_refund
import mifos_mobile.core.ui.generated.resources.tx_type_repayment
import mifos_mobile.core.ui.generated.resources.tx_type_repayment_at_disbursement
import mifos_mobile.core.ui.generated.resources.tx_type_reschedule
import mifos_mobile.core.ui.generated.resources.tx_type_tax_withholding
import mifos_mobile.core.ui.generated.resources.tx_type_undo_approval
import mifos_mobile.core.ui.generated.resources.tx_type_undo_disbursal
import mifos_mobile.core.ui.generated.resources.tx_type_waive_charges
import mifos_mobile.core.ui.generated.resources.tx_type_waive_interest
import mifos_mobile.core.ui.generated.resources.tx_type_withdrawal
import mifos_mobile.core.ui.generated.resources.tx_type_write_off
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.model.entity.client.Type
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Parses a `List<Int>` date (as returned by the Mifos API: [year, month, day])
 * into a [LocalDate], or returns null if the list is malformed.
 */
fun parseIsoDate(dateList: List<Int>): LocalDate? {
    if (dateList.size < 3) return null
    return try {
        @Suppress("DEPRECATION")
        LocalDate(year = dateList[0], monthNumber = dateList[1], dayOfMonth = dateList[2])
    } catch (_: Exception) {
        null
    }
}

/**
 * Parses an ISO date string ("yyyy-MM-dd") back to a [LocalDate], or null on failure.
 */
fun parseIsoDate(isoString: String): LocalDate? {
    return try {
        LocalDate.parse(isoString)
    } catch (_: Exception) {
        null
    }
}

/**
 * Converts a `List<Int>` date to an ISO date string "yyyy-MM-dd".
 * Falls back to an empty string if the list is malformed.
 * This is the stable, locale-neutral grouping key for ViewModels.
 */
fun List<Int>.toIsoDateString(): String {
    return parseIsoDate(this)?.toString() ?: ""
}

/**
 * Returns a localized, human-readable date string for a given [LocalDate].
 * Today / Yesterday are resolved from string resources; other dates use
 * a localizable "Day MonthAbbrev Year" pattern.
 */
@Suppress("DEPRECATION")
@OptIn(ExperimentalTime::class)
@Composable
fun formatTransactionDate(date: LocalDate?): String {
    if (date == null) return ""
    val tz = TimeZone.currentSystemDefault()
    val today = Clock.System.now().toLocalDateTime(tz).date
    val yesterday = today.minus(DatePeriod(days = 1))
    return when {
        date == today -> stringResource(Res.string.tx_date_today)
        date == yesterday -> stringResource(Res.string.tx_date_yesterday)
        else -> "${date.day} ${localizedMonth(date.monthNumber)} ${date.year}"
    }
}

/**
 * Overload that accepts a [List<Int>] date (Mifos API format).
 */
@Composable
fun formatTransactionDate(dateList: List<Int>): String =
    formatTransactionDate(parseIsoDate(dateList))

/**
 * Returns the same as [formatTransactionDate] — date header for grouped lists.
 * "Today", "Yesterday", or "15 Mar 2025".
 */
@Composable
fun formatTransactionDateWithPrefix(date: LocalDate?): String =
    formatTransactionDate(date)

/**
 * Returns a localized month abbreviation for a month number (1-12).
 */
@Composable
fun localizedMonth(monthNumber: Int): String = stringResource(
    when (monthNumber) {
        1 -> Res.string.tx_month_jan
        2 -> Res.string.tx_month_feb
        3 -> Res.string.tx_month_mar
        4 -> Res.string.tx_month_apr
        5 -> Res.string.tx_month_may
        6 -> Res.string.tx_month_jun
        7 -> Res.string.tx_month_jul
        8 -> Res.string.tx_month_aug
        9 -> Res.string.tx_month_sep
        10 -> Res.string.tx_month_oct
        11 -> Res.string.tx_month_nov
        else -> Res.string.tx_month_dec
    },
)

/**
 * Maps a [Type] (from the Mifos API) to a localized transaction type label.
 * Falls back to the API-provided [Type.value] string if no translation is found.
 */
@Suppress("CyclomaticComplexMethod")
@Composable
fun localizeTransactionType(type: Type?): String {
    if (type == null) return ""
    return when (type.code) {
        "savingsAccountInterestPosting",
        "loanInterestPosting",
        "interestPosting",
        -> stringResource(Res.string.tx_type_interest_posting)

        "deposit",
        "savingsDeposit",
        -> stringResource(Res.string.tx_type_deposit)

        "withdrawal",
        "savingsWithdrawal",
        -> stringResource(Res.string.tx_type_withdrawal)

        "repayment",
        "loanRepayment",
        -> stringResource(Res.string.tx_type_repayment)

        "repaymentAtDisbursement",
        "loanRepaymentAtDisbursement",
        -> stringResource(Res.string.tx_type_repayment_at_disbursement)

        "waiveInterest",
        "waiveLoanInterest",
        -> stringResource(Res.string.tx_type_waive_interest)

        "waiveCharges",
        "waiveLoanCharges",
        -> stringResource(Res.string.tx_type_waive_charges)

        "chargePayment",
        "loanChargePayment",
        -> stringResource(Res.string.tx_type_charge_payment)

        "feeDeduction",
        "savingsFeeDeduction",
        -> stringResource(Res.string.tx_type_fee_deduction)

        "withdrawTransfer",
        "initiateTransfer",
        "loanInitiateTransfer",
        -> stringResource(Res.string.tx_type_initiate_transfer)

        "dividendPayout" -> stringResource(Res.string.tx_type_dividend_payout)
        "accrual" -> stringResource(Res.string.tx_type_accrual)
        "writeOff", "loanWriteOff" -> stringResource(Res.string.tx_type_write_off)
        "recoverRepayment" -> stringResource(Res.string.tx_type_recover_repayment)
        "refund", "savingsRefund" -> stringResource(Res.string.tx_type_refund)
        "creditBalanceRefund" -> stringResource(Res.string.tx_type_credit_balance_refund)
        "overpaymentRefund", "overdraftInterest" -> stringResource(Res.string.tx_type_overpayment)
        "loanChargeAdded" -> stringResource(Res.string.tx_type_loan_charge_added)
        "approval" -> stringResource(Res.string.tx_type_approval)
        "undoApproval" -> stringResource(Res.string.tx_type_undo_approval)
        "undoDisbursal" -> stringResource(Res.string.tx_type_undo_disbursal)
        "reschedule" -> stringResource(Res.string.tx_type_reschedule)
        "chargeOff" -> stringResource(Res.string.tx_type_charge_off)
        "interestWaiver" -> stringResource(Res.string.tx_type_interest_waiver)
        "taxWithholding", "withholdTax" -> stringResource(Res.string.tx_type_tax_withholding)
        "recalculateInterest" -> stringResource(Res.string.tx_type_recalculate_interest)
        else -> type.value ?: type.code ?: ""
    }
}

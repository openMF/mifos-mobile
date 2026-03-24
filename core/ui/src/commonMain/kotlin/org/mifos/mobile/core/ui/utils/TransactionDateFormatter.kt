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
import mifos_mobile.core.ui.generated.resources.tx_digits
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
import mifos_mobile.core.ui.generated.resources.tx_type_reject_transfer
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
        else -> "${localizeNumber(date.day)} ${localizedMonth(date.monthNumber)} ${localizeNumber(date.year)}"
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
 * Converts an integer to a string using the locale's native digit script.
 * Uses [Res.string.tx_digits] — a 10-char string where each char is the
 * locale digit for 0–9 (e.g. "०१२३४५६७८९" for Hindi).
 */
@Composable
fun localizeNumber(n: Int): String {
    val digits = stringResource(Res.string.tx_digits)
    if (digits.length != 10) return n.toString()
    return n.toString().map { c ->
        if (c.isDigit()) digits[c - '0'] else c
    }.joinToString("")
}

/**
 * Maps a [Type] (from the Mifos API) to a localized transaction type label.
 * Handles both short codes ("deposit") and full Mifos API codes
 * ("savingsAccountTransactionType.deposit", "loanTransactionType.repayment", etc.).
 * Falls back to the API-provided [Type.value] string if no translation is found.
 */
@Suppress("CyclomaticComplexMethod")
@Composable
fun localizeTransactionType(type: Type?): String {
    if (type == null) return ""
    // Normalize: strip common Mifos prefixes so both short and full codes match
    val rawCode = type.code?.lowercase() ?: ""
    val code = rawCode
        .removePrefix("savingsaccounttransactiontype.")
        .removePrefix("loantransactiontype.")
        .removePrefix("sharetransactiontype.")
        .removePrefix("loan")
        .trimStart('.')

    return when {
        code in setOf("savingsaccountinterestposting", "loaninterestposting", "interestposting", "interestposting") ||
            rawCode.contains("interestposting") ->
            stringResource(Res.string.tx_type_interest_posting)

        code in setOf("deposit", "savingsdeposit") || rawCode.contains("deposit") && !rawCode.contains("repayment") ->
            stringResource(Res.string.tx_type_deposit)

        code in setOf("withdrawal", "savingswithdrawal") || rawCode.contains("withdrawal") ->
            stringResource(Res.string.tx_type_withdrawal)

        code in setOf("repayment") || rawCode == "loantransactiontype.repayment" || rawCode.endsWith(".repayment") ->
            stringResource(Res.string.tx_type_repayment)

        code in setOf("repaymentAtDisbursement".lowercase(), "repaymentatdisbursement") ->
            stringResource(Res.string.tx_type_repayment_at_disbursement)

        code in setOf("waiveinterest", "waiveloaninterest") || rawCode.contains("waiveinterest") ->
            stringResource(Res.string.tx_type_waive_interest)

        code in setOf("waivecharges", "waiveloancharges") || rawCode.contains("waivecharges") ->
            stringResource(Res.string.tx_type_waive_charges)

        code in setOf("chargepayment", "loanchargepayment") || rawCode.contains("chargepayment") ->
            stringResource(Res.string.tx_type_charge_payment)

        code in setOf("feededuction", "savingsfeededuction") || rawCode.contains("feededuction") ->
            stringResource(Res.string.tx_type_fee_deduction)

        code in setOf("withdrawtransfer", "initiatetransfer", "loaninitiatetransfer") ||
            rawCode.contains("initiatetransfer") || rawCode.contains("withdrawtransfer") ->
            stringResource(Res.string.tx_type_initiate_transfer)

        code == "approvetransfer" || rawCode.contains("approvetransfer") ->
            stringResource(Res.string.tx_type_initiate_transfer)

        code == "rejecttransfer" || rawCode.contains("rejecttransfer") ->
            stringResource(Res.string.tx_type_reject_transfer)

        code == "dividendpayout" -> stringResource(Res.string.tx_type_dividend_payout)
        code == "accrual" -> stringResource(Res.string.tx_type_accrual)
        code in setOf("writeoff", "loanwriteoff") || rawCode.contains("writeoff") ->
            stringResource(Res.string.tx_type_write_off)
        code == "recoverrepayment" -> stringResource(Res.string.tx_type_recover_repayment)
        code in setOf("refund", "savingsrefund") -> stringResource(Res.string.tx_type_refund)
        code == "creditbalancerefund" -> stringResource(Res.string.tx_type_credit_balance_refund)
        code in setOf("overpaymentrefund", "overdraftinterest") -> stringResource(Res.string.tx_type_overpayment)
        code == "loanchargeadded" -> stringResource(Res.string.tx_type_loan_charge_added)
        code == "approval" -> stringResource(Res.string.tx_type_approval)
        code == "undoapproval" -> stringResource(Res.string.tx_type_undo_approval)
        code == "undodisbursal" || code == "disbursalundone" -> stringResource(Res.string.tx_type_undo_disbursal)
        code == "reschedule" -> stringResource(Res.string.tx_type_reschedule)
        code == "chargeoff" -> stringResource(Res.string.tx_type_charge_off)
        code == "interestwaiver" -> stringResource(Res.string.tx_type_interest_waiver)
        code in setOf("taxwithholding", "withholdtax") -> stringResource(Res.string.tx_type_tax_withholding)
        code == "recalculateinterest" -> stringResource(Res.string.tx_type_recalculate_interest)
        code in setOf("disbursement") -> stringResource(Res.string.tx_type_repayment)
        else -> type.value ?: type.code ?: ""
    }
}

/**
 * Localizes the digit characters in a formatted currency/amount string
 * using the same [tx_digits] resource as [localizeNumber].
 * Non-digit characters (currency symbols, separators) are preserved unchanged.
 */
@Composable
fun localizeAmount(formattedAmount: String): String {
    val digits = stringResource(Res.string.tx_digits)
    if (digits.length != 10 || digits == "0123456789") return formattedAmount
    return formattedAmount.map { c ->
        if (c.isDigit()) digits[c - '0'] else c
    }.joinToString("")
}

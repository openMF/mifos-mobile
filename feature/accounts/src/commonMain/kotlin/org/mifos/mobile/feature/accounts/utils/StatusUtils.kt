/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.utils

import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_active
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_approval_pending
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_bronze
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_closed
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_disburse
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_in_arrears
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_matured
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_overpaid
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_personal
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_rejected
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_filter_withdrawn
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_active_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_approved_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_bank_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_closed_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_group_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_matured_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_nb_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_pending_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_wallet_account
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_credit
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_debit
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_1_year
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_2_years
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_3_months
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_6_months
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_past_month
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.model.FilterType
import org.mifos.mobile.feature.accounts.model.TransactionCheckboxStatus
import org.mifos.mobile.feature.accounts.model.TransactionFilterType

/**
 * Utility object that provides predefined checkbox options for different account types.
 *
 * These checkboxes are used in filtering loan, savings, and share accounts based on their statuses.
 */
object StatusUtils {

    /**
     * Retrieves the list of available checkbox statuses for savings accounts.
     *
     * @return A list of [CheckboxStatus] representing different savings account statuses.
     */
    internal fun getSavingsAccountCheckboxes(): List<CheckboxStatus> {
        val accountTypes = listOf(
            CheckboxStatus(Res.string.feature_savings_filter_wallet_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_bank_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_group_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_nb_account, type = FilterType.ACCOUNT_TYPE),
        )

        val accountStatuses = listOf(
            CheckboxStatus(Res.string.feature_savings_filter_active_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_pending_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_closed_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_matured_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_approved_account, type = FilterType.ACCOUNT_STATUS),
        )

        return accountTypes + accountStatuses
    }

    /**
     * Retrieves the list of available checkbox statuses for loan accounts.
     *
     * @return A list of [CheckboxStatus] representing different loan account statuses.
     */
    internal fun getLoanAccountCheckboxes(): List<CheckboxStatus> {
        val accountTypes = listOf(
            CheckboxStatus(Res.string.feature_loan_account_filter_personal, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_loan_account_filter_bronze, type = FilterType.ACCOUNT_TYPE),
        )

        val accountStatuses = listOf(
            CheckboxStatus(Res.string.feature_loan_account_filter_active, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_approval_pending, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_closed, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_disburse, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_overpaid, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_in_arrears, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_withdrawn, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_matured, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_rejected, type = FilterType.ACCOUNT_STATUS),
        )

        return accountTypes + accountStatuses
    }

    /**
     * Retrieves the list of available checkbox statuses for share accounts.
     *
     * @return A list of [CheckboxStatus] representing different share account statuses.
     */
    internal fun getShareAccountCheckboxes(): List<CheckboxStatus> {
        // TODO Replace with actual account types for share account
        val accountTypes = listOf(
            CheckboxStatus(Res.string.feature_savings_filter_wallet_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_bank_account, type = FilterType.ACCOUNT_TYPE),
        )

        val accountStatuses = listOf(
            CheckboxStatus(Res.string.feature_loan_account_filter_active, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_approval_pending, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_closed, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_disburse, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_overpaid, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_in_arrears, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_withdrawn, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_matured, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_loan_account_filter_rejected, type = FilterType.ACCOUNT_STATUS),
        )

        return accountTypes + accountStatuses
    }

    /**
     * Retrieves the list of available checkbox statuses for transactions.
     *
     * @return A list of [TransactionCheckboxStatus] representing different transaction statuses.
     */
    internal fun getTransactionCheckboxes(): List<TransactionCheckboxStatus> {
        val type = listOf(
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_credit,
                type = TransactionFilterType.TRANSACTION_TYPE,
            ),
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_debit,
                type = TransactionFilterType.TRANSACTION_TYPE,
            ),
        )

        val duration = listOf(
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_past_month,
                type = TransactionFilterType.DURATION,
            ),
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_past_3_months,
                type = TransactionFilterType.DURATION,
            ),
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_past_6_months,
                type = TransactionFilterType.DURATION,
            ),
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_past_1_year,
                type = TransactionFilterType.DURATION,
            ),
            TransactionCheckboxStatus(
                Res.string.feature_transaction_filter_past_2_years,
                type = TransactionFilterType.DURATION,
            ),
        )

        return type + duration
    }
}

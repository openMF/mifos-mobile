/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.utils

import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_active
import mifos_mobile.feature.loan_account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.loan_account.generated.resources.feature_account_closed
import mifos_mobile.feature.loan_account.generated.resources.feature_account_disburse
import mifos_mobile.feature.loan_account.generated.resources.feature_account_in_arrears
import mifos_mobile.feature.loan_account.generated.resources.feature_account_overpaid
import mifos_mobile.feature.loan_account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount

/**
 * Enum class representing different filters that can be applied to loan accounts.
 *
 * Each filter has a corresponding label (for UI representation) and a match condition
 * that determines whether a given [LoanAccount] meets the criteria for the filter.
 *
 * @property label The string resource representing the filter name.
 * @property matchCondition A lambda function that checks if a [LoanAccount] meets the filter condition.
 */
enum class FilterUtil(
    val label: StringResource,
    val matchCondition: (LoanAccount) -> Boolean,
) {
    /**
     * Filter for active loan accounts.
     * Matches if the loan account's status is active.
     */
    ACTIVE(
        label = Res.string.feature_account_active,
        matchCondition = { it.status?.active == true },
    ),

    /**
     * Filter for loan accounts that are pending approval.
     * Matches if the loan account's status indicates pending approval.
     */
    APPROVAL_PENDING(
        label = Res.string.feature_account_approval_pending,
        matchCondition = { it.status?.pendingApproval == true },
    ),

    /**
     * Filter for closed loan accounts.
     * Matches if the loan account's status indicates it has been closed.
     */
    CLOSED(
        label = Res.string.feature_account_closed,
        matchCondition = { it.status?.closed == true },
    ),

    /**
     * Filter for loan accounts waiting for disbursal.
     * Matches if the loan account's status indicates it is awaiting disbursement.
     */
    WAITING_FOR_DISBURSE(
        label = Res.string.feature_account_disburse,
        matchCondition = { it.status?.waitingForDisbursal == true },
    ),

    /**
     * Filter for overpaid loan accounts.
     * Matches if the loan account's status indicates it has been overpaid.
     */
    OVERPAID(
        label = Res.string.feature_account_overpaid,
        matchCondition = { it.status?.overpaid == true },
    ),

    /**
     * Filter for loan accounts that are in arrears.
     * Matches if the loan account's status indicates it is overdue or has unpaid installments.
     */
    IN_ARREARS(
        label = Res.string.feature_account_in_arrears,
        matchCondition = { it.status?.overpaid == true },
    ),

    /**
     * Filter for withdrawn loan accounts.
     * Matches if the loan account's status indicates it has been withdrawn before disbursement.
     */
    WITHDRAWN(
        label = Res.string.feature_account_withdrawn,
        matchCondition = { it.status?.overpaid == true },
    ),
    ;

    companion object {

        /**
         * Retrieves a [FilterUtil] instance based on the provided label.
         *
         * @param label The label to match against.
         * @return The corresponding [FilterUtil] instance if found, otherwise null.
         */
        fun fromLabel(label: StringResource?) = entries.find { it.label == label }
    }
}

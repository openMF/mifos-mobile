/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.utils

import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_account_active
import mifos_mobile.feature.savings_account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.savings_account.generated.resources.feature_account_approved
import mifos_mobile.feature.savings_account.generated.resources.feature_account_closed
import mifos_mobile.feature.savings_account.generated.resources.feature_account_matured
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount

/**
 * Enum class representing different filters that can be applied to savings accounts.
 *
 * Each filter has a corresponding label (for UI representation) and a match condition
 * that determines whether a given [SavingAccount] meets the criteria for the filter.
 *
 * @property label The string resource representing the filter name.
 * @property matchCondition A lambda function that checks if a [SavingAccount] meets the filter condition.
 */
enum class FilterUtil(
    val label: StringResource,
    val matchCondition: (SavingAccount) -> Boolean,
) {

    /**
     * Filter for active savings accounts.
     * Matches if the savings account's status is active.
     */
    ACTIVE(
        label = Res.string.feature_account_active,
        matchCondition = { it.status?.active == true },
    ),

    /**
     * Filter for approved savings accounts.
     * Matches if the savings account's status indicates it is approved.
     */
    APPROVED(
        label = Res.string.feature_account_approved,
        matchCondition = { it.status?.approved == true },
    ),

    /**
     * Filter for savings accounts that are pending approval.
     * Matches if the savings account's status indicates it is submitted and pending approval.
     */
    APPROVAL_PENDING(
        label = Res.string.feature_account_approval_pending,
        matchCondition = { it.status?.submittedAndPendingApproval == true },
    ),

    /**
     * Filter for matured savings accounts.
     * Matches if the savings account's status indicates it has matured.
     */
    MATURED(
        label = Res.string.feature_account_matured,
        matchCondition = { it.status?.matured == true },
    ),

    /**
     * Filter for closed savings accounts.
     * Matches if the savings account's status indicates it has been closed.
     */
    CLOSED(
        label = Res.string.feature_account_closed,
        matchCondition = { it.status?.closed == true },
    ),
    ;

    companion object {

        /**
         * Retrieves a [FilterUtil] instance based on the provided label.
         *
         * @param label The label to match against.
         * @return The corresponding [FilterUtil] instance if found, otherwise null.
         */
        fun fromLabel(label: StringResource?): FilterUtil? = entries.find { it.label == label }
    }
}

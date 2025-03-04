/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.utils

import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_account_active
import mifos_mobile.feature.share_account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.share_account.generated.resources.feature_account_approved
import mifos_mobile.feature.share_account.generated.resources.feature_account_closed
import mifos_mobile.feature.share_account.generated.resources.feature_account_rejected
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount

/**
 * Enum class representing different filters that can be applied to share accounts.
 *
 * Each filter has a corresponding label (for UI representation) and a match condition
 * that determines whether a given [ShareAccount] meets the criteria for the filter.
 *
 * @property label The string resource representing the filter name.
 * @property matchCondition A lambda function that checks if a [ShareAccount] meets the filter condition.
 */
enum class FilterUtil(
    val label: StringResource,
    val matchCondition: (ShareAccount) -> Boolean,
) {

    /**
     * Filter for active share accounts.
     * Matches if the share account's status is active.
     */
    ACTIVE(
        Res.string.feature_account_active,
        { it.status?.active == true },
    ),

    /**
     * Filter for approved share accounts.
     * Matches if the share account's status indicates it is approved.
     */
    APPROVED(
        Res.string.feature_account_approved,
        { it.status?.approved == true },
    ),

    /**
     * Filter for share accounts that are pending approval.
     * Matches if the share account's status indicates it is submitted and pending approval.
     */
    APPROVAL_PENDING(
        Res.string.feature_account_approval_pending,
        { it.status?.submittedAndPendingApproval == true },
    ),

    /**
     * Filter for rejected share accounts.
     * Matches if the share account's status indicates it has been rejected.
     */
    REJECTED(
        Res.string.feature_account_rejected,
        { it.status?.rejected == true },
    ),

    /**
     * Filter for closed share accounts.
     * Matches if the share account's status indicates it has been closed.
     */
    CLOSED(
        Res.string.feature_account_closed,
        { it.status?.closed == true },
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

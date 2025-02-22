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
import mifos_mobile.feature.share_account.generated.resources.feature_account_disburse
import mifos_mobile.feature.share_account.generated.resources.feature_account_in_arrears
import mifos_mobile.feature.share_account.generated.resources.feature_account_matured
import mifos_mobile.feature.share_account.generated.resources.feature_account_overpaid
import mifos_mobile.feature.share_account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.StringResource

data class FilterUtil(
    val activeString: StringResource,
    val approvedString: StringResource,
    val approvalPendingString: StringResource,
    val maturedString: StringResource,
    val closedString: StringResource,
    val waitingForDisburseString: StringResource,
    val overpaidString: StringResource,
    val withdrawnString: StringResource,
    val inArrearsString: StringResource,
)

fun getAccountsFilterLabels() =
    FilterUtil(
        activeString = Res.string.feature_account_active,
        approvedString = Res.string.feature_account_approved,
        approvalPendingString = Res.string.feature_account_approval_pending,
        maturedString = Res.string.feature_account_matured,
        closedString = Res.string.feature_account_closed,
        waitingForDisburseString = Res.string.feature_account_disburse,
        overpaidString = Res.string.feature_account_overpaid,
        withdrawnString = Res.string.feature_account_withdrawn,
        inArrearsString = Res.string.feature_account_in_arrears,
    )

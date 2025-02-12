/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.account.utils

import mifos_mobile.feature.account.generated.resources.Res
import mifos_mobile.feature.account.generated.resources.feature_account_active
import mifos_mobile.feature.account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.account.generated.resources.feature_account_approved
import mifos_mobile.feature.account.generated.resources.feature_account_closed
import mifos_mobile.feature.account.generated.resources.feature_account_disburse
import mifos_mobile.feature.account.generated.resources.feature_account_in_arrears
import mifos_mobile.feature.account.generated.resources.feature_account_matured
import mifos_mobile.feature.account.generated.resources.feature_account_overpaid
import mifos_mobile.feature.account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.StringResource

data class AccountsFilterUtil(
    var activeString: StringResource? = null,
    var approvedString: StringResource? = null,
    var approvalPendingString: StringResource? = null,
    var maturedString: StringResource? = null,
    var waitingForDisburseString: StringResource? = null,
    var overpaidString: StringResource? = null,
    var closedString: StringResource? = null,
    var withdrawnString: StringResource? = null,
    var inArrearsString: StringResource? = null,
) {
    companion object {
        fun getFilterStrings(): AccountsFilterUtil {
            return AccountsFilterUtil(
                activeString = Res.string.feature_account_active,
                approvedString = Res.string.feature_account_approved,
                approvalPendingString = Res.string.feature_account_approval_pending,
                maturedString = Res.string.feature_account_matured,
                waitingForDisburseString = Res.string.feature_account_disburse,
                overpaidString = Res.string.feature_account_overpaid,
                closedString = Res.string.feature_account_closed,
                withdrawnString = Res.string.feature_account_withdrawn,
                inArrearsString = Res.string.feature_account_in_arrears,
            )
        }
    }
}

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

import android.content.Context
import org.mifos.mobile.feature.account.R

data class AccountsFilterUtil(
    var activeString: String? = null,
    var approvedString: String? = null,
    var approvalPendingString: String? = null,
    var maturedString: String? = null,
    var waitingForDisburseString: String? = null,
    var overpaidString: String? = null,
    var closedString: String? = null,
    var withdrawnString: String? = null,
    var inArrearsString: String? = null,
) {
    companion object {
        fun getFilterStrings(context: Context): AccountsFilterUtil {
            return AccountsFilterUtil(
                activeString = context.getString(R.string.feature_account_active),
                approvedString = context.getString(R.string.feature_account_approved),
                approvalPendingString = context.getString(R.string.feature_account_approval_pending),
                maturedString = context.getString(R.string.feature_account_matured),
                waitingForDisburseString = context.getString(R.string.feature_account_disburse),
                overpaidString = context.getString(R.string.feature_account_overpaid),
                closedString = context.getString(R.string.feature_account_closed),
                withdrawnString = context.getString(R.string.feature_account_withdrawn),
                inArrearsString = context.getString(R.string.feature_account_in_arrears),
            )
        }
    }
}

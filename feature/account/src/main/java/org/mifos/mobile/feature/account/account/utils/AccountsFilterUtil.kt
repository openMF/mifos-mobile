package org.mifos.mobile.feature.account.account.utils

import android.content.Context
import org.mifos.mobile.feature.account.R

data class AccountsFilterUtil(
    var activeString: String? = null,
    var approvedString: String? = null,
    var approvalPendingString : String? = null,
    var maturedString : String? = null,
    var waitingForDisburseString : String? = null,
    var overpaidString: String? = null,
    var closedString : String? = null,
    var withdrawnString : String? = null,
    var inArrearsString : String? = null
) {
    companion object {
        fun getFilterStrings(context: Context): AccountsFilterUtil {
            return AccountsFilterUtil(
                activeString = context.getString(R.string.active),
                approvedString = context.getString(R.string.approved),
                approvalPendingString = context.getString(R.string.approval_pending),
                maturedString = context.getString(R.string.matured),
                waitingForDisburseString = context.getString(R.string.waiting_for_disburse),
                overpaidString = context.getString(R.string.overpaid),
                closedString = context.getString(R.string.closed),
                withdrawnString = context.getString(R.string.withdrawn),
                inArrearsString = context.getString(R.string.in_arrears),
            )
        }
    }
}
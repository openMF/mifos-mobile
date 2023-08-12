package org.mifos.mobile.utils

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
)
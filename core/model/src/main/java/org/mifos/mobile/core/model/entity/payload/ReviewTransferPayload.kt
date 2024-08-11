package org.mifos.mobile.core.model.entity.payload

import org.mifos.mobile.core.model.entity.templates.account.AccountOption

data class ReviewTransferPayload(
    var payToAccount: AccountOption? = null,
    var payFromAccount: AccountOption? = null,
    var amount: String = "",
    var review: String = ""
)
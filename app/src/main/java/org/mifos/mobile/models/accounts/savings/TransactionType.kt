package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class TransactionType(
        var id: Int? = null,
        var code: String,

        var value: String,

        var deposit: Boolean? = null,

        var dividendPayout: Boolean? = null,

        var withdrawal: Boolean? = null,

        var interestPosting: Boolean? = null,

        var feeDeduction: Boolean? = null,

        var initiateTransfer: Boolean? = null,

        var approveTransfer: Boolean? = null,

        var withdrawTransfer: Boolean? = null,

        var rejectTransfer: Boolean? = null,

        var overdraftInterest: Boolean? = null,

        var writtenoff: Boolean? = null,

        var overdraftFee: Boolean? = null,

        var withholdTax: Boolean? = null,

        var escheat: Boolean? = null

) : Parcelable
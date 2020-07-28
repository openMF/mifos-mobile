package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class Transactions(
        var id: Int? = null,

        var transactionType: TransactionType? = null,

        var accountId: Int? = null,

        var accountNo: String? = null,

        var date: List<Int> = ArrayList(),

        var currency: Currency? = null,

        var paymentDetailData: PaymentDetailData? = null,

        var amount: Double? = null,

        var runningBalance: Double? = null,

        var reversed: Boolean? = null,

        var submittedOnDate: List<Int>? = null,

        var interestedPostedAsOn: Boolean? = null

) : Parcelable
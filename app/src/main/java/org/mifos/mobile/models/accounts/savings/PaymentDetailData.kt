package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class PaymentDetailData(
        var id: Int? = null,

        var paymentType: PaymentType,

        var accountNumber: String,

        var checkNumber: String,

        var routingCode: String,

        var receiptNumber: String,

        var bankNumber: String
) : Parcelable
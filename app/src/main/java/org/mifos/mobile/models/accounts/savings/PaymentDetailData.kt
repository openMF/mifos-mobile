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

    var accountNumber: String? = null,

    var checkNumber: String? = null,

    var routingCode: String? = null,

    var receiptNumber: String? = null,

    var bankNumber: String? = null,
) : Parcelable

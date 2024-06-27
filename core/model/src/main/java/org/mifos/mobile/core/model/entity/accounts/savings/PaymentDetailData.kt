package org.mifos.mobile.core.model.entity.accounts.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

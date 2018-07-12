package org.mifos.mobilebanking.models.templates.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargePaymentMode (
        var id: Float = 0.toFloat(),
        var code: String? = null,
        var value: String? = null
) : Parcelable


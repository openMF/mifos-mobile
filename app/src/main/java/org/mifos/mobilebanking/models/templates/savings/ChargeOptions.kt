package org.mifos.mobilebanking.models.templates.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mifos.mobilebanking.models.accounts.savings.Currency

@Parcelize
data class ChargeOptions(
        var id: Int? = null,
        var name: String? = null,
        var active: Boolean? = null,
        var penalty: Boolean? = null,
        var currency: Currency,
        var amount: Float = 0.toFloat(),
        var chargeTimeType: ChargeTimeType,
        var chargeAppliesTo: ChargeAppliesTo,
        var chargeCalculationType: ChargeCalculationType,
        var chargePaymentMode: ChargePaymentMode
) : Parcelable
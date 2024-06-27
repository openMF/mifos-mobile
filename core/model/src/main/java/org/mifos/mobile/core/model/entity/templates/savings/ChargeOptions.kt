package org.mifos.mobile.core.model.entity.templates.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.Currency

@Parcelize
data class ChargeOptions(
    var id: Int? = null,
    var name: String? = null,
    var active: Boolean? = null,
    var penalty: Boolean? = null,
    var currency: Currency? = null,
    var amount: Float = 0.toFloat(),
    var chargeTimeType: ChargeTimeType,
    var chargeAppliesTo: ChargeAppliesTo,
    var chargeCalculationType: ChargeCalculationType,
    var chargePaymentMode: ChargePaymentMode,
) : Parcelable

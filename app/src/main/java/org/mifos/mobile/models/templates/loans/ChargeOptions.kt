package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.models.ChargeCalculationType
import org.mifos.mobile.models.ChargeTimeType

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class ChargeOptions(

    var id: Int? = null,

    var name: String? = null,

    var active: Boolean? = null,

    var penalty: Boolean? = null,

    var currency: Currency? = null,

    var amount: Double? = null,

    var chargeTimeType: ChargeTimeType? = null,

    var chargeAppliesTo: ChargeAppliesTo? = null,

    var chargeCalculationType: ChargeCalculationType? = null,

    var chargePaymentMode: ChargePaymentMode? = null,

    var taxGroup: TaxGroup? = null,

) : Parcelable

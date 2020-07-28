package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.models.ChargeCalculationType
import org.mifos.mobile.models.ChargeTimeType

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class ChargeOptions(

        var id: Int? = null,

        var name: String,

        var active: Boolean? = null,

        var penalty: Boolean? = null,

        var currency: Currency,

        var amount: Double? = null,

        var chargeTimeType: ChargeTimeType,

        var chargeAppliesTo: ChargeAppliesTo,

        var chargeCalculationType: ChargeCalculationType,

        var chargePaymentMode: ChargePaymentMode,

        var taxGroup: TaxGroup

) : Parcelable
package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobilebanking.models.ChargeCalculationType
import org.mifos.mobilebanking.models.ChargeTimeType

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class ChargeOptions(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String,

        @SerializedName("active")
        var active: Boolean? = null,

        @SerializedName("penalty")
        var penalty: Boolean? = null,

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("amount")
        var amount: Double? = null,

        @SerializedName("chargeTimeType")
        var chargeTimeType: ChargeTimeType,

        @SerializedName("chargeAppliesTo")
        var chargeAppliesTo: ChargeAppliesTo,

        @SerializedName("chargeCalculationType")
        var chargeCalculationType: ChargeCalculationType,

        @SerializedName("chargePaymentMode")
        var chargePaymentMode: ChargePaymentMode,

        @SerializedName("taxGroup")
        var taxGroup: TaxGroup

) : Parcelable
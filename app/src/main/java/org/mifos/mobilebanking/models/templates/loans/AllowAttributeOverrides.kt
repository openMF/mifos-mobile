package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class AllowAttributeOverrides(

        @SerializedName("amortizationType")
        var amortizationType: Boolean? = null,

        @SerializedName("interestType")
        var interestType: Boolean? = null,

        @SerializedName("transactionProcessingStrategyId")
        var transactionProcessingStrategyId: Boolean? = null,

        @SerializedName("interestCalculationPeriodType")
        var interestCalculationPeriodType: Boolean? = null,

        @SerializedName("inArrearsTolerance")
        var inArrearsTolerance: Boolean? = null,

        @SerializedName("repaymentEvery")
        var repaymentEvery: Boolean? = null,

        @SerializedName("graceOnPrincipalAndInterestPayment")
        var graceOnPrincipalAndInterestPayment: Boolean? = null,

        @SerializedName("graceOnArrearsAgeing")
        var graceOnArrearsAgeing: Boolean? = null

) : Parcelable
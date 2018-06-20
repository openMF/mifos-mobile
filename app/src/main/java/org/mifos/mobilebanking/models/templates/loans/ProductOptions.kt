package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class ProductOptions(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String,

        @SerializedName("includeInBorrowerCycle")
        var includeInBorrowerCycle: Boolean? = null,

        @SerializedName("useBorrowerCycle")
        var useBorrowerCycle: Boolean? = null,

        @SerializedName("isLinkedToFloatingInterestRates")
        var linkedToFloatingInterestRates: Boolean? = null,

        @SerializedName("isFloatingInterestRateCalculationAllowed")
        var floatingInterestRateCalculationAllowed: Boolean? = null,

        @SerializedName("allowVariableInstallments")
        var allowVariableInstallments: Boolean? = null,

        @SerializedName("isInterestRecalculationEnabled")
        var interestRecalculationEnabled: Boolean? = null,

        @SerializedName("canDefineInstallmentAmount")
        var canDefineInstallmentAmount: Boolean? = null,

        @SerializedName("holdGuaranteeFunds")
        var holdGuaranteeFunds: Boolean? = null,

        @SerializedName("accountMovesOutOfNPAOnlyOnArrearsCompletion")
        var accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null
) : Parcelable
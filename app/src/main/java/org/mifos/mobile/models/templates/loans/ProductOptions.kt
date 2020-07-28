package org.mifos.mobile.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class ProductOptions(

        var id: Int? = null,

        var name: String,

        var includeInBorrowerCycle: Boolean? = null,

        var useBorrowerCycle: Boolean? = null,

        @SerializedName("isLinkedToFloatingInterestRates")
        var linkedToFloatingInterestRates: Boolean? = null,

        @SerializedName("isFloatingInterestRateCalculationAllowed")
        var floatingInterestRateCalculationAllowed: Boolean? = null,

        var allowVariableInstallments: Boolean? = null,

        @SerializedName("isInterestRecalculationEnabled")
        var interestRecalculationEnabled: Boolean? = null,

        var canDefineInstallmentAmount: Boolean? = null,

        var holdGuaranteeFunds: Boolean? = null,

        var accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null
) : Parcelable
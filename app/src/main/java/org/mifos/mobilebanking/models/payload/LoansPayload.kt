/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobilebanking.models.payload

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nellyk on 2/20/2016.
 */

@Parcelize
data class LoansPayload(
        @SerializedName("clientId")
        var clientId: Int? = null,

        @SerializedName("productId")
        var productId: Int? = null,

        @SerializedName("principal")
        var principal: Double? = null,

        @SerializedName("loanTermFrequency")
        var loanTermFrequency: Int? = null,

        @SerializedName("loanTermFrequencyType")
        var loanTermFrequencyType: Int? = null,

        @SerializedName("loanType")
        var loanType: String? = null,

        @SerializedName("numberOfRepayments")
        var numberOfRepayments: Int? = null,

        @SerializedName("repaymentEvery")
        var repaymentEvery: Int? = null,

        @SerializedName("repaymentFrequencyType")
        var repaymentFrequencyType: Int? = null,

        @SerializedName("interestRatePerPeriod")
        var interestRatePerPeriod: Double? = null,

        @SerializedName("amortizationType")
        var amortizationType: Int? = null,

        @SerializedName("interestType")
        var interestType: Int? = null,

        @SerializedName("interestCalculationPeriodType")
        var interestCalculationPeriodType: Int? = null,

        @SerializedName("transactionProcessingStrategyId")
        var transactionProcessingStrategyId: Int? = null,

        @SerializedName("expectedDisbursementDate")
        var expectedDisbursementDate: String? = null,

        @SerializedName("submittedOnDate")
        var submittedOnDate: String? = null,

        @SerializedName("linkAccountId")
        var linkAccountId: Int? = null,

        @SerializedName("loanPurposeId")
        var loanPurposeId: Int? = null,

        @SerializedName("maxOutstandingLoanBalance")
        var maxOutstandingLoanBalance: Double? = null,

        var dateFormat : String = "dd MMMM yyyy",
        var locale : String = "en"
) : Parcelable
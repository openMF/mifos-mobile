package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.mifos.mobilebanking.models.accounts.Account

/**
 * @author Vishwajeet
 * @since 22/06/16.
 */
@Parcelize
data class LoanAccount(
        @SerializedName("loanProductId")
        var loanProductId: Long = 0,

        @SerializedName("externalId")
        var externalId: String? = null,

        @SerializedName("numberOfRepayments")
        var numberOfRepayments: Long = 0,

        @SerializedName("accountNo")
        var accountNo: String? = null,

        @SerializedName("productName")
        var productName: String? = null,

        @SerializedName("productId")
        var productId: Int? = null,

        @SerializedName("loanProductName")
        var loanProductName: String? = null,

        @SerializedName("clientName")
        var clientName: String? = null,

        @SerializedName("loanProductDescription")
        var loanProductDescription: String? = null,

        @SerializedName("principal")
        var principal: Double = 0.toDouble(),

        @SerializedName("annualInterestRate")
        var annualInterestRate: Double = 0.toDouble(),

        @SerializedName("status")
        var status: Status? = null,

        @SerializedName("loanType")
        var loanType: LoanType? = null,

        @SerializedName("loanCycle")
        var loanCycle: Int? = null,

        @SerializedName("loanBalance")
        var loanBalance: Double = 0.toDouble(),

        @SerializedName("amountPaid")
        var amountPaid: Double = 0.toDouble(),

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("inArrears")
        var inArrears: Boolean? = null,

        @SerializedName("summary")
        var summary: Summary? = null,

        @SerializedName("loanPurposeName")
        var loanPurposeName: String? = null,

        @SerializedName("timeline")
        var timeline: Timeline

) : Account(), Parcelable

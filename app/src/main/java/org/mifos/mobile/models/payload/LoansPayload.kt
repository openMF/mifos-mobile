/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobile.models.payload

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

/**
 * Created by nellyk on 2/20/2016.
 */

@Parcelize
data class LoansPayload(

        var clientId: Int? = null,

        var productId: Int? = null,

        var productName: String? = null,

        var principal: Double? = null,

        var loanTermFrequency: Int? = null,

        var loanTermFrequencyType: Int? = null,

        var loanType: String? = null,

        var numberOfRepayments: Int? = null,

        var repaymentEvery: Int? = null,

        var repaymentFrequencyType: Int? = null,

        var interestRatePerPeriod: Double? = null,

        var amortizationType: Int? = null,

        var interestType: Int? = null,

        var interestCalculationPeriodType: Int? = null,

        var transactionProcessingStrategyId: Int? = null,

        var expectedDisbursementDate: String? = null,

        var submittedOnDate: String? = null,

        var linkAccountId: Int? = null,

        var loanPurposeId: Int? = null,

        var loanPurpose: String? = null,

        var maxOutstandingLoanBalance: Double? = null,

        var currency: String? = null,

        var dateFormat: String = "dd MMMM yyyy",

        var locale: String = "en"
) : Parcelable
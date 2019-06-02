package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobile.models.Transaction

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class LoanWithAssociations(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("accountNo")
        var accountNo: String? = null,

        @SerializedName("externalId")
        var externalId: String? = null,

        @SerializedName("status")
        var status: Status? =null,

        @SerializedName("clientId")
        var clientId: Int? = null,

        @SerializedName("clientAccountNo")
        var clientAccountNo: String? = null,

        @SerializedName("clientName")
        var clientName: String? = null,

        @SerializedName("clientOfficeId")
        var clientOfficeId: Int? = null,

        @SerializedName("loanProductId")
        var loanProductId: Int? = null,

        @SerializedName("loanProductName")
        var loanProductName: String? = null,

        @SerializedName("isLoanProductLinkedToFloatingRate")
        var loanProductLinkedToFloatingRate: Boolean? = null,

        @SerializedName("loanType")
        var loanType: LoanType? = null,

        @SerializedName("currency")
        var currency: Currency? = null,

        @SerializedName("principal")
        var principal: Double? = null,

        @SerializedName("approvedPrincipal")
        var approvedPrincipal: Double? = null,

        @SerializedName("proposedPrincipal")
        var proposedPrincipal: Double? = null,

        @SerializedName("termFrequency")
        var termFrequency: Int? = null,

        @SerializedName("termPeriodFrequencyType")
        var termPeriodFrequencyType: TermPeriodFrequencyType? = null,

        @SerializedName("numberOfRepayments")
        var numberOfRepayments: Int? = null,

        @SerializedName("repaymentEvery")
        var repaymentEvery: Int? = null,

        @SerializedName("repaymentFrequencyType")
        var repaymentFrequencyType: RepaymentFrequencyType? = null,

        @SerializedName("interestRatePerPeriod")
        var interestRatePerPeriod: Int? = null,

        @SerializedName("interestRateFrequencyType")
        var interestRateFrequencyType: InterestRateFrequencyType? = null,

        @SerializedName("isFloatingInterestRate")
        var floatingInterestRate: Boolean? = null,

        @SerializedName("amortizationType")
        var amortizationType: AmortizationType? = null,

        @SerializedName("interestType")
        var interestType: InterestType? = null,

        @SerializedName("interestCalculationPeriodType")
        var interestCalculationPeriodType: InterestCalculationPeriodType? = null,

        @SerializedName("allowPartialPeriodInterestCalcualtion")
        var allowPartialPeriodInterestCalcualtion: Boolean? = null,

        @SerializedName("transactionProcessingStrategyId")
        var transactionProcessingStrategyId: Int? = null,

        @SerializedName("transactionProcessingStrategyName")
        var transactionProcessingStrategyName: String? = null,

        @SerializedName("syncDisbursementWithMeeting")
        var syncDisbursementWithMeeting: Boolean? = null,

        @SerializedName("timeline")
        var timeline: Timeline? = null,

        @SerializedName("summary")
        var summary: Summary? = null,

        @SerializedName("repaymentSchedule")
        var repaymentSchedule: RepaymentSchedule? = null,

        @SerializedName("feeChargesAtDisbursementCharged")
        var feeChargesAtDisbursementCharged: Double? = null,

        @SerializedName("loanProductCounter")
        var loanProductCounter: Int? = null,

        @SerializedName("multiDisburseLoan")
        var multiDisburseLoan: Boolean? = null,

        @SerializedName("canDefineInstallmentAmount")
        var canDefineInstallmentAmount: Boolean? = null,

        @SerializedName("canDisburse")
        var canDisburse: Boolean? = null,

        @SerializedName("canUseForTopup")
        var canUseForTopup: Boolean? = null,

        @SerializedName("isTopup")
        var topup: Boolean? = null,

        @SerializedName("closureLoanId")
        var closureLoanId: Int? = null,

        @SerializedName("inArrears")
        var inArrears: Boolean? = null,

        @SerializedName("isNPA")
        var npa: Boolean? = null,

        @SerializedName("daysInMonthType")
        var daysInMonthType: DaysInMonthType? = null,

        @SerializedName("daysInYearType")
        var daysInYearType: DaysInYearType? = null,

        @SerializedName("isInterestRecalculationEnabled")
        var interestRecalculationEnabled: Boolean? = null,

        @SerializedName("interestRecalculationData")
        var interestRecalculationData: InterestRecalculationData? = null,

        @SerializedName("createStandingInstructionAtDisbursement")
        var createStandingInstructionAtDisbursement: Boolean? = null,

        @SerializedName("isVariableInstallmentsAllowed")
        var variableInstallmentsAllowed: Boolean? = null,

        @SerializedName("minimumGap")
        var minimumGap: Int? = null,

        @SerializedName("maximumGap")
        var maximumGap: Int? = null,

        @SerializedName("transactions")
        var transactions: List<Transaction> = ArrayList(),

        @SerializedName("loanPurposeName")
        var loanPurposeName: String? = null
) : Parcelable
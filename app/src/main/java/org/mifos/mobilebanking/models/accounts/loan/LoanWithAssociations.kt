package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobilebanking.models.Transaction

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class LoanWithAssociations(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("accountNo")
        var accountNo: String,

        @SerializedName("externalId")
        var externalId: String,

        @SerializedName("status")
        var status: Status,

        @SerializedName("clientId")
        var clientId: Int? = null,

        @SerializedName("clientAccountNo")
        var clientAccountNo: String,

        @SerializedName("clientName")
        var clientName: String,

        @SerializedName("clientOfficeId")
        var clientOfficeId: Int? = null,

        @SerializedName("loanProductId")
        var loanProductId: Int? = null,

        @SerializedName("loanProductName")
        var loanProductName: String,

        @SerializedName("isLoanProductLinkedToFloatingRate")
        var loanProductLinkedToFloatingRate: Boolean? = null,

        @SerializedName("loanType")
        var loanType: LoanType,

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("principal")
        var principal: Double? = null,

        @SerializedName("approvedPrincipal")
        var approvedPrincipal: Double? = null,

        @SerializedName("proposedPrincipal")
        var proposedPrincipal: Double? = null,

        @SerializedName("termFrequency")
        var termFrequency: Int? = null,

        @SerializedName("termPeriodFrequencyType")
        var termPeriodFrequencyType: TermPeriodFrequencyType,

        @SerializedName("numberOfRepayments")
        var numberOfRepayments: Int? = null,

        @SerializedName("repaymentEvery")
        var repaymentEvery: Int? = null,

        @SerializedName("repaymentFrequencyType")
        var repaymentFrequencyType: RepaymentFrequencyType,

        @SerializedName("interestRatePerPeriod")
        var interestRatePerPeriod: Int? = null,

        @SerializedName("interestRateFrequencyType")
        var interestRateFrequencyType: InterestRateFrequencyType,

        @SerializedName("isFloatingInterestRate")
        var floatingInterestRate: Boolean? = null,

        @SerializedName("amortizationType")
        var amortizationType: AmortizationType,

        @SerializedName("interestType")
        var interestType: InterestType,

        @SerializedName("interestCalculationPeriodType")
        var interestCalculationPeriodType: InterestCalculationPeriodType,

        @SerializedName("allowPartialPeriodInterestCalcualtion")
        var allowPartialPeriodInterestCalcualtion: Boolean? = null,

        @SerializedName("transactionProcessingStrategyId")
        var transactionProcessingStrategyId: Int? = null,

        @SerializedName("transactionProcessingStrategyName")
        var transactionProcessingStrategyName: String,

        @SerializedName("syncDisbursementWithMeeting")
        var syncDisbursementWithMeeting: Boolean? = null,

        @SerializedName("timeline")
        var timeline: Timeline,

        @SerializedName("summary")
        var summary: Summary,

        @SerializedName("repaymentSchedule")
        var repaymentSchedule: RepaymentSchedule,

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
        var daysInMonthType: DaysInMonthType,

        @SerializedName("daysInYearType")
        var daysInYearType: DaysInYearType,

        @SerializedName("isInterestRecalculationEnabled")
        var interestRecalculationEnabled: Boolean? = null,

        @SerializedName("interestRecalculationData")
        var interestRecalculationData: InterestRecalculationData,

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
package org.mifos.mobile.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobile.models.accounts.loan.AmortizationType
import org.mifos.mobile.models.accounts.loan.DaysInMonthType
import org.mifos.mobile.models.accounts.loan.DaysInYearType
import org.mifos.mobile.models.accounts.loan.InterestCalculationPeriodType
import org.mifos.mobile.models.accounts.loan.InterestRateFrequencyType
import org.mifos.mobile.models.accounts.loan.InterestRecalculationData
import org.mifos.mobile.models.accounts.loan.InterestType
import org.mifos.mobile.models.accounts.loan.RepaymentFrequencyType

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Product(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String,

        @SerializedName("shortName")
        var shortName: String,

        @SerializedName("fundId")
        var fundId: Int? = null,

        @SerializedName("fundName")
        var fundName: String,

        @SerializedName("includeInBorrowerCycle")
        var includeInBorrowerCycle: Boolean? = null,

        @SerializedName("useBorrowerCycle")
        var useBorrowerCycle: Boolean? = null,

        @SerializedName("startDate")
        var startDate: List<Int>,

        @SerializedName("status")
        var status: String,

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("principal")
        var principal: Double? = null,

        @SerializedName("minPrincipal")
        var minPrincipal: Double? = null,

        @SerializedName("maxPrincipal")
        var maxPrincipal: Double? = null,

        @SerializedName("numberOfRepayments")
        var numberOfRepayments: Int? = null,

        @SerializedName("minNumberOfRepayments")
        var minNumberOfRepayments: Int? = null,

        @SerializedName("maxNumberOfRepayments")
        var maxNumberOfRepayments: Int? = null,

        @SerializedName("repaymentEvery")
        var repaymentEvery: Int? = null,

        @SerializedName("repaymentFrequencyType")
        var repaymentFrequencyType: RepaymentFrequencyType,

        @SerializedName("interestRatePerPeriod")
        var interestRatePerPeriod: Double? = null,

        @SerializedName("minInterestRatePerPeriod")
        var minInterestRatePerPeriod: Double? = null,

        @SerializedName("maxInterestRatePerPeriod")
        var maxInterestRatePerPeriod: Double? = null,

        @SerializedName("interestRateFrequencyType")
        var interestRateFrequencyType: InterestRateFrequencyType,

        @SerializedName("annualInterestRate")
        var annualInterestRate: Double? = null,

        @SerializedName("isLinkedToFloatingInterestRates")
        var linkedToFloatingInterestRates: Boolean? = null,

        @SerializedName("isFloatingInterestRateCalculationAllowed")
        var floatingInterestRateCalculationAllowed: Boolean? = null,

        @SerializedName("allowVariableInstallments")
        var allowVariableInstallments: Boolean? = null,

        @SerializedName("minimumGap")
        var minimumGap: Double? = null,

        @SerializedName("maximumGap")
        var maximumGap: Double? = null,

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

        @SerializedName("graceOnArrearsAgeing")
        var graceOnArrearsAgeing: Int? = null,

        @SerializedName("overdueDaysForNPA")
        var overdueDaysForNPA: Int? = null,

        @SerializedName("daysInMonthType")
        var daysInMonthType: DaysInMonthType,

        @SerializedName("daysInYearType")
        var daysInYearType: DaysInYearType,

        @SerializedName("isInterestRecalculationEnabled")
        var interestRecalculationEnabled: Boolean? = null,

        @SerializedName("interestRecalculationData")
        var interestRecalculationData: InterestRecalculationData,

        @SerializedName("canDefineInstallmentAmount")
        var canDefineInstallmentAmount: Boolean? = null,

        @SerializedName("accountingRule")
        var accountingRule: AccountingRule,

        @SerializedName("multiDisburseLoan")
        var multiDisburseLoan: Boolean? = null,

        @SerializedName("maxTrancheCount")
        var maxTrancheCount: Int? = null,

        @SerializedName("principalThresholdForLastInstallment")
        var principalThresholdForLastInstallment: Int? = null,

        @SerializedName("holdGuaranteeFunds")
        var holdGuaranteeFunds: Boolean? = null,

        @SerializedName("accountMovesOutOfNPAOnlyOnArrearsCompletion")
        var accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null,

        @SerializedName("allowAttributeOverrides")
        var allowAttributeOverrides: AllowAttributeOverrides

) : Parcelable
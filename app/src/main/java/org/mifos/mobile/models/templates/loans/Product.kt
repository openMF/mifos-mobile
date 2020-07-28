package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.models.accounts.loan.*

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Product(

        var id: Int? = null,

        var name: String,

        var shortName: String,

        var fundId: Int? = null,

        var fundName: String,

        var includeInBorrowerCycle: Boolean? = null,

        var useBorrowerCycle: Boolean? = null,

        var startDate: List<Int>,

        var status: String,

        var currency: Currency,

        var principal: Double? = null,

        var minPrincipal: Double? = null,

        var maxPrincipal: Double? = null,

        var numberOfRepayments: Int? = null,

        var minNumberOfRepayments: Int? = null,

        var maxNumberOfRepayments: Int? = null,

        var repaymentEvery: Int? = null,

        var repaymentFrequencyType: RepaymentFrequencyType,

        var interestRatePerPeriod: Double? = null,

        var minInterestRatePerPeriod: Double? = null,

        var maxInterestRatePerPeriod: Double? = null,

        var interestRateFrequencyType: InterestRateFrequencyType,

        var annualInterestRate: Double? = null,

        @SerializedName("isLinkedToFloatingInterestRates")
        var linkedToFloatingInterestRates: Boolean? = null,

        @SerializedName("isFloatingInterestRateCalculationAllowed")
        var floatingInterestRateCalculationAllowed: Boolean? = null,

        var allowVariableInstallments: Boolean? = null,

        var minimumGap: Double? = null,

        var maximumGap: Double? = null,

        var amortizationType: AmortizationType,

        var interestType: InterestType,

        var interestCalculationPeriodType: InterestCalculationPeriodType,

        var allowPartialPeriodInterestCalcualtion: Boolean? = null,

        var transactionProcessingStrategyId: Int? = null,

        var transactionProcessingStrategyName: String,

        var graceOnArrearsAgeing: Int? = null,

        var overdueDaysForNPA: Int? = null,

        var daysInMonthType: DaysInMonthType,

        var daysInYearType: DaysInYearType,

        @SerializedName("isInterestRecalculationEnabled")
        var interestRecalculationEnabled: Boolean? = null,

        var interestRecalculationData: InterestRecalculationData,

        var canDefineInstallmentAmount: Boolean? = null,

        var accountingRule: AccountingRule,

        var multiDisburseLoan: Boolean? = null,

        var maxTrancheCount: Int? = null,

        var principalThresholdForLastInstallment: Int? = null,

        var holdGuaranteeFunds: Boolean? = null,

        var accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null,

        var allowAttributeOverrides: AllowAttributeOverrides

) : Parcelable
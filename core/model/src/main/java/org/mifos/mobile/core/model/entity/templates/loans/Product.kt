package org.mifos.mobile.core.model.entity.templates.loans

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.AmortizationType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInMonthType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInYearType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestCalculationPeriodType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRateFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRecalculationData
import org.mifos.mobile.core.model.entity.accounts.loan.InterestType
import org.mifos.mobile.core.model.entity.accounts.loan.RepaymentFrequencyType

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Product(

    var id: Int? = null,

    var name: String? = null,

    var shortName: String? = null,

    var fundId: Int? = null,

    var fundName: String? = null,

    var includeInBorrowerCycle: Boolean? = null,

    var useBorrowerCycle: Boolean? = null,

    var startDate: List<Int>? = null,

    var status: String? = null,

    var currency: Currency? = null,

    var principal: Double? = null,

    var minPrincipal: Double? = null,

    var maxPrincipal: Double? = null,

    var numberOfRepayments: Int? = null,

    var minNumberOfRepayments: Int? = null,

    var maxNumberOfRepayments: Int? = null,

    var repaymentEvery: Int? = null,

    var repaymentFrequencyType: RepaymentFrequencyType? = null,

    var interestRatePerPeriod: Double? = null,

    var minInterestRatePerPeriod: Double? = null,

    var maxInterestRatePerPeriod: Double? = null,

    var interestRateFrequencyType: InterestRateFrequencyType? = null,

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

    var interestCalculationPeriodType: InterestCalculationPeriodType? = null,

    var allowPartialPeriodInterestCalcualtion: Boolean? = null,

    var transactionProcessingStrategyId: Int? = null,

    var transactionProcessingStrategyName: String? = null,

    var graceOnArrearsAgeing: Int? = null,

    var overdueDaysForNPA: Int? = null,

    var daysInMonthType: DaysInMonthType? = null,

    var daysInYearType: DaysInYearType,

    @SerializedName("isInterestRecalculationEnabled")
    var interestRecalculationEnabled: Boolean? = null,

    var interestRecalculationData: InterestRecalculationData? = null,

    var canDefineInstallmentAmount: Boolean? = null,

    var accountingRule: AccountingRule? = null,

    var multiDisburseLoan: Boolean? = null,

    var maxTrancheCount: Int? = null,

    var principalThresholdForLastInstallment: Int? = null,

    var holdGuaranteeFunds: Boolean? = null,

    var accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null,

    var allowAttributeOverrides: AllowAttributeOverrides? = null,

    ) : Parcelable

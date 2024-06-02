package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.models.accounts.loan.*
import java.util.*

/**
 * Created by Rajan Maurya on 15/07/16.
 */

@Parcelize
data class LoanTemplate(

    var clientId: Int? = null,

    var clientAccountNo: String? = null,

    var clientName: String? = null,

    var clientOfficeId: Int? = null,

    var loanProductName: String? = null,

    @SerializedName("isLoanProductLinkedToFloatingRate")
    var loanProductLinkedToFloatingRate: Boolean? = null,

    var fundId: Int? = null,

    var fundName: String? = null,

    var currency: Currency? = null,

    var principal: Double? = null,

    var approvedPrincipal: Double? = null,

    var proposedPrincipal: Double? = null,

    var termFrequency: Int? = null,

    var termPeriodFrequencyType: TermPeriodFrequencyType? = null,

    var numberOfRepayments: Int? = null,

    var repaymentEvery: Int? = null,

    var repaymentFrequencyType: RepaymentFrequencyType? = null,

    var interestRatePerPeriod: Double? = null,

    var interestRateFrequencyType: InterestRateFrequencyType? = null,

    var annualInterestRate: Double? = null,

    @SerializedName("isFloatingInterestRate")
    var floatingInterestRate: Boolean? = null,

    var amortizationType: AmortizationType? = null,

    var interestType: InterestType? = null,

    var interestCalculationPeriodType: InterestCalculationPeriodType? = null,

    var allowPartialPeriodInterestCalcualtion: Boolean? = null,

    var transactionProcessingStrategyId: Int? = null,

    var graceOnArrearsAgeing: Int? = null,

    var timeline: Timeline? = null,

    var productOptions: List<ProductOptions> = ArrayList(),

    var loanOfficerOptions: List<LoanOfficerOptions> = ArrayList(),

    var loanPurposeOptions: List<LoanPurposeOptions> = ArrayList(),

    var fundOptions: List<FundOptions> = ArrayList(),

    var termFrequencyTypeOptions: List<TermFrequencyTypeOptions> = ArrayList(),

    var repaymentFrequencyTypeOptions: List<RepaymentFrequencyTypeOptions> = ArrayList(),

    var repaymentFrequencyNthDayTypeOptions: List<RepaymentFrequencyNthDayTypeOptions> = ArrayList(),

    var repaymentFrequencyDaysOfWeekTypeOptions: List<RepaymentFrequencyDaysOfWeekTypeOptions> = ArrayList(),

    var interestRateFrequencyTypeOptions: List<InterestRateFrequencyTypeOptions> = ArrayList(),

    var amortizationTypeOptions: List<AmortizationTypeOptions> = ArrayList(),

    var interestTypeOptions: List<InterestTypeOptions> = ArrayList(),

    var interestCalculationPeriodTypeOptions: List<InterestCalculationPeriodType> = ArrayList(),

    var transactionProcessingStrategyOptions: List<TransactionProcessingStrategyOptions> = ArrayList(),

    var chargeOptions: List<ChargeOptions> = ArrayList(),

    var loanCollateralOptions: List<LoanCollateralOptions> = ArrayList(),

    var multiDisburseLoan: Boolean? = null,

    var canDefineInstallmentAmount: Boolean? = null,

    var canDisburse: Boolean? = null,

    var product: Product? = null,

    var daysInMonthType: DaysInMonthType? = null,

    var daysInYearType: DaysInYearType? = null,

    @SerializedName("isInterestRecalculationEnabled")
    var interestRecalculationEnabled: Boolean? = null,

    @SerializedName("isVariableInstallmentsAllowed")
    var variableInstallmentsAllowed: Boolean? = null,

    var minimumGap: Int? = null,

    var maximumGap: Int? = null,

    var accountLinkingOptions: List<AccountLinkingOptions> = ArrayList(),

) : Parcelable {
    /**
     * Required to set default value to the Fund spinner
     *
     * @param fundId The value received from the Template for that particular loanProduct
     * @return Returns the index of the fundOption list where the specified fundId is located
     */
    fun getFundNameFromId(fundId: Int): Int {
        for (i in fundOptions.indices) {
            if (fundOptions[i].id == fundId) {
                return i
            }
        }
        return 0
    }
}

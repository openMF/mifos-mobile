/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.loans

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.AmortizationType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInMonthType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInYearType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestCalculationPeriodType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRateFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestType
import org.mifos.mobile.core.model.entity.accounts.loan.RepaymentFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.TermPeriodFrequencyType

@Serializable
@Parcelize
data class LoanTemplate(

    val clientId: Int? = null,

    val clientAccountNo: String? = null,

    val clientName: String? = null,

    val clientOfficeId: Int? = null,

    val loanProductName: String? = null,

    @SerialName("isLoanProductLinkedToFloatingRate")
    val loanProductLinkedToFloatingRate: Boolean? = null,

    val fundId: Int? = null,

    val fundName: String? = null,

    val currency: Currency? = null,

    val principal: Double? = null,

    val approvedPrincipal: Double? = null,

    val proposedPrincipal: Double? = null,

    val termFrequency: Int? = null,

    val termPeriodFrequencyType: TermPeriodFrequencyType? = null,

    val numberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: RepaymentFrequencyType? = null,

    val interestRatePerPeriod: Double? = null,

    val interestRateFrequencyType: InterestRateFrequencyType? = null,

    val annualInterestRate: Double? = null,

    @SerialName("isFloatingInterestRate")
    val floatingInterestRate: Boolean? = null,

    val amortizationType: AmortizationType? = null,

    val interestType: InterestType? = null,

    val interestCalculationPeriodType: InterestCalculationPeriodType? = null,

    val allowPartialPeriodInterestCalculation: Boolean? = null,

    val transactionProcessingStrategyId: Int? = null,

    val graceOnArrearsAgeing: Int? = null,

    val timeline: Timeline? = null,

    val productOptions: List<ProductOptions> = emptyList(),

    val loanOfficerOptions: List<LoanOfficerOptions> = emptyList(),

    val loanPurposeOptions: List<LoanPurposeOptions> = emptyList(),

    val fundOptions: List<FundOptions> = emptyList(),

    val termFrequencyTypeOptions: List<TermFrequencyTypeOptions> = emptyList(),

    val repaymentFrequencyTypeOptions: List<RepaymentFrequencyTypeOptions> = emptyList(),

    val repaymentFrequencyNthDayTypeOptions: List<RepaymentFrequencyNthDayTypeOptions> = emptyList(),

    val repaymentFrequencyDaysOfWeekTypeOptions: List<RepaymentFrequencyDaysOfWeekTypeOptions> = emptyList(),

    val interestRateFrequencyTypeOptions: List<InterestRateFrequencyTypeOptions> = emptyList(),

    val amortizationTypeOptions: List<AmortizationTypeOptions> = emptyList(),

    val interestTypeOptions: List<InterestTypeOptions> = emptyList(),

    val interestCalculationPeriodTypeOptions: List<InterestCalculationPeriodType> = emptyList(),

    val transactionProcessingStrategyOptions: List<TransactionProcessingStrategyOptions> = emptyList(),

    val chargeOptions: List<ChargeOptions> = emptyList(),

    val loanCollateralOptions: List<LoanCollateralOptions> = emptyList(),

    val multiDisburseLoan: Boolean? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val canDisburse: Boolean? = null,

    val product: Product? = null,

    val daysInMonthType: DaysInMonthType? = null,

    val daysInYearType: DaysInYearType? = null,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    @SerialName("isvaliableInstallmentsAllowed")
    val valiableInstallmentsAllowed: Boolean? = null,

    val minimumGap: Int? = null,

    val maximumGap: Int? = null,

    val accountLinkingOptions: List<AccountLinkingOptions> = emptyList(),

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

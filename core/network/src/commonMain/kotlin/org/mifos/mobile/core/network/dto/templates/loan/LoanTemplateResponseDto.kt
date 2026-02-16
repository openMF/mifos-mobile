/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.templates.loan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.products.loan.LoanProductOptionsResponseDto
import org.mifos.mobile.core.network.dto.products.loan.LoanProductResponseDto

@Serializable
data class LoanTemplateResponseDto(

    val clientId: Int? = null,

    val clientAccountNo: String? = null,

    val clientName: String? = null,

    val clientOfficeId: Int? = null,

    val loanProductName: String? = null,

    @SerialName("isLoanProductLinkedToFloatingRate")
    val loanProductLinkedToFloatingRate: Boolean? = null,

    val fundId: Int? = null,

    val fundName: String? = null,

    val currency: CurrencyResponseDto? = null,

    val principal: Double? = null,

    val approvedPrincipal: Double? = null,

    val proposedPrincipal: Double? = null,

    val termFrequency: Int? = null,

    val termPeriodFrequencyType: TypeResponseDto? = null,

    val numberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: TypeResponseDto? = null,

    val interestRatePerPeriod: Double? = null,

    val interestRateFrequencyType: TypeResponseDto? = null,

    val annualInterestRate: Double? = null,

    @SerialName("isFloatingInterestRate")
    val floatingInterestRate: Boolean? = null,

    val amortizationType: TypeResponseDto? = null,

    val interestType: TypeResponseDto? = null,

    val interestCalculationPeriodType: TypeResponseDto? = null,

    val allowPartialPeriodInterestCalculation: Boolean? = null,

    val transactionProcessingStrategyId: Int? = null,

    val transactionProcessingStrategyCode: String? = null,

    val graceOnArrearsAgeing: Int? = null,

    val timeline: LoanTimelineResponseDto? = null,

    val productOptions: List<LoanProductOptionsResponseDto> = emptyList(),

    val loanOfficerOptions: List<LoanOfficerOptionsResponseDto> = emptyList(),

    val loanPurposeOptions: List<LoanPurposeOptionsResponseDto> = emptyList(),

    val fundOptions: List<FundOptionsResponseDto> = emptyList(),

    val termFrequencyTypeOptions: List<TypeResponseDto> = emptyList(),

    val repaymentFrequencyTypeOptions: List<TypeResponseDto> = emptyList(),

    val repaymentFrequencyNthDayTypeOptions: List<TypeResponseDto> = emptyList(),

    val repaymentFrequencyDaysOfWeekTypeOptions: List<TypeResponseDto> = emptyList(),

    val interestRateFrequencyTypeOptions: List<TypeResponseDto> = emptyList(),

    val amortizationTypeOptions: List<TypeResponseDto> = emptyList(),

    val interestTypeOptions: List<TypeResponseDto> = emptyList(),

    val interestCalculationPeriodTypeOptions: List<TypeResponseDto> = emptyList(),

    val transactionProcessingStrategyOptions: List<TypeResponseDto> = emptyList(),

    val chargeOptions: List<ChargeOptionsResponseDto> = emptyList(),

    val loanCollateralOptions: List<LoanCollateralOptionsResponseDto> = emptyList(),

    val multiDisburseLoan: Boolean? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val canDisburse: Boolean? = null,

    val product: LoanProductResponseDto? = null,

    val daysInMonthType: TypeResponseDto? = null,

    val daysInYearType: TypeResponseDto? = null,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    @SerialName("isvaliableInstallmentsAllowed")
    val valiableInstallmentsAllowed: Boolean? = null,

    val minimumGap: Int? = null,

    val maximumGap: Int? = null,

    val accountLinkingOptions: List<AccountLinkingOptionsResponseDto> = emptyList(),

)

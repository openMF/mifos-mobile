/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.products.loan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.intrest.InterestRecalculationDataResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.AllowAttributesOverridesResponseDto

@Serializable
data class LoanProductResponseDto(

    val id: Int? = null,

    val name: String? = null,

    val shortName: String? = null,

    val fundId: Int? = null,

    val fundName: String? = null,

    val includeInBorrowerCycle: Boolean? = null,

    val useBorrowerCycle: Boolean? = null,

    val startDate: List<Int>? = null,

    val status: String? = null,

    val currency: CurrencyResponseDto? = null,

    val principal: Double? = null,

    val minPrincipal: Double? = null,

    val maxPrincipal: Double? = null,

    val numberOfRepayments: Int? = null,

    val minNumberOfRepayments: Int? = null,

    val maxNumberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: TypeResponseDto? = null,

    val interestRatePerPeriod: Double? = null,

    val minInterestRatePerPeriod: Double? = null,

    val maxInterestRatePerPeriod: Double? = null,

    val interestRateFrequencyType: TypeResponseDto? = null,

    val annualInterestRate: Double? = null,

    @SerialName("isLinkedToFloatingInterestRates")
    val linkedToFloatingInterestRates: Boolean? = null,

    @SerialName("isFloatingInterestRateCalculationAllowed")
    val floatingInterestRateCalculationAllowed: Boolean? = null,

    val allowvaliableInstallments: Boolean? = null,

    val minimumGap: Double? = null,

    val maximumGap: Double? = null,

    val amortizationType: TypeResponseDto,

    val interestType: TypeResponseDto,

    val interestCalculationPeriodType: TypeResponseDto? = null,

    val allowPartialPeriodInterestCalculation: Boolean? = null,

    val transactionProcessingStrategyId: Int? = null,

    val transactionProcessingStrategyName: String? = null,

    val graceOnArrearsAgeing: Int? = null,

    val overdueDaysForNPA: Int? = null,

    val daysInMonthType: TypeResponseDto? = null,

    val daysInYearType: TypeResponseDto,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    val interestRecalculationData: InterestRecalculationDataResponseDto? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val accountingRule: TypeResponseDto? = null,

    val multiDisburseLoan: Boolean? = null,

    val maxTrancheCount: Int? = null,

    val principalThresholdForLastInstallment: Double? = null,

    val holdGuaranteeFunds: Boolean? = null,

    val accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null,

    val allowAttributeOverrides: AllowAttributesOverridesResponseDto? = null,

)

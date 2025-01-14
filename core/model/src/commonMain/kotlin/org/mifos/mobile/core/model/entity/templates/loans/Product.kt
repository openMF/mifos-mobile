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
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRecalculationData
import org.mifos.mobile.core.model.entity.accounts.loan.InterestType
import org.mifos.mobile.core.model.entity.accounts.loan.RepaymentFrequencyType

@Serializable
@Parcelize
data class Product(

    val id: Int? = null,

    val name: String? = null,

    val shortName: String? = null,

    val fundId: Int? = null,

    val fundName: String? = null,

    val includeInBorrowerCycle: Boolean? = null,

    val useBorrowerCycle: Boolean? = null,

    val startDate: List<Int>? = null,

    val status: String? = null,

    val currency: Currency? = null,

    val principal: Double? = null,

    val minPrincipal: Double? = null,

    val maxPrincipal: Double? = null,

    val numberOfRepayments: Int? = null,

    val minNumberOfRepayments: Int? = null,

    val maxNumberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: RepaymentFrequencyType? = null,

    val interestRatePerPeriod: Double? = null,

    val minInterestRatePerPeriod: Double? = null,

    val maxInterestRatePerPeriod: Double? = null,

    val interestRateFrequencyType: InterestRateFrequencyType? = null,

    val annualInterestRate: Double? = null,

    @SerialName("isLinkedToFloatingInterestRates")
    val linkedToFloatingInterestRates: Boolean? = null,

    @SerialName("isFloatingInterestRateCalculationAllowed")
    val floatingInterestRateCalculationAllowed: Boolean? = null,

    val allowvaliableInstallments: Boolean? = null,

    val minimumGap: Double? = null,

    val maximumGap: Double? = null,

    val amortizationType: AmortizationType,

    val interestType: InterestType,

    val interestCalculationPeriodType: InterestCalculationPeriodType? = null,

    val allowPartialPeriodInterestCalcualtion: Boolean? = null,

    val transactionProcessingStrategyId: Int? = null,

    val transactionProcessingStrategyName: String? = null,

    val graceOnArrearsAgeing: Int? = null,

    val overdueDaysForNPA: Int? = null,

    val daysInMonthType: DaysInMonthType? = null,

    val daysInYearType: DaysInYearType,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    val interestRecalculationData: InterestRecalculationData? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val accountingRule: AccountingRule? = null,

    val multiDisburseLoan: Boolean? = null,

    val maxTrancheCount: Int? = null,

    val principalThresholdForLastInstallment: Int? = null,

    val holdGuaranteeFunds: Boolean? = null,

    val accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null,

    val allowAttributeOverrides: AllowAttributeOverrides? = null,

) : Parcelable

/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable

@Serializable
data class SavingsAccountApplicationPayloadDto(

    // Mandatory
    val clientId: Int? = null,
    val productId: Int? = null,
    val submittedOnDate: String? = null,
    val locale: String,
    val dateFormat: String,
    val monthDayFormat: String,

    // Optional
    val accountNo: String? = null,
    val externalId: String? = null,
    val fieldOfficerId: Int? = null,

    // Override fields (inherited from Product if not provided)
    val nominalAnnualInterestRate: Double? = null,
    val interestCompoundingPeriodType: Int? = null,
    val interestPostingPeriodType: Int? = null,
    val interestCalculationType: Int? = null,
    val interestCalculationDaysInYearType: Int? = null,
    val minRequiredOpeningBalance: Double? = null,
    val lockinPeriodFrequency: Int? = null,
    val lockinPeriodFrequencyType: Int? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val overdraftLimit: Double? = null,
    val nominalAnnualInterestRateOverdraft: Double? = null,
    val minOverdraftForInterestCalculation: Double? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,

    // Charges
    val charges: List<SavingsChargePayloadDto>? = null,

)

@Serializable
data class SavingsChargePayloadDto(
    val chargeId: Int,
    val amount: Double,
)

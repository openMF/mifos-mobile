/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.templates.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.products.savings.SavingsProductOptionsResponseDto
import org.mifos.mobile.core.network.dto.products.savings.SavingsProductResponseDto

@Serializable
data class SavingsAccountTemplateResponseDto(
    val clientId: Int = 0,
    val clientName: String? = null,
    val savingsProductId: Int? = null,
    val savingsProductName: String? = null,
    val timeline: SavingsTimelineResponseDto? = null,
    val currency: CurrencyResponseDto? = null,
    val nominalAnnualInterestRate: Double? = null,
    val interestCompoundingPeriodType: SavingsProductOptionsResponseDto? = null,
    val interestPostingPeriodType: SavingsProductOptionsResponseDto? = null,
    val interestCalculationType: SavingsProductOptionsResponseDto? = null,
    val interestCalculationDaysInYearType: SavingsProductOptionsResponseDto? = null,
    val minRequiredOpeningBalance: Double? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,
    val isDormancyTrackingActive: Boolean? = null,
    val charges: List<SavingsChargeOptionsResponseDto>? = null,
    val productOptions: ArrayList<SavingsProductResponseDto> = arrayListOf(),
    val fieldOfficerOptions: List<FieldOfficerOptionsResponseDto> = emptyList(),
    val interestCompoundingPeriodTypeOptions: List<SavingsOptionsResponseDto> = emptyList(),
    val interestPostingPeriodTypeOptions: List<SavingsOptionsResponseDto> = emptyList(),
    val interestCalculationTypeOptions: List<SavingsOptionsResponseDto> = emptyList(),
    val interestCalculationDaysInYearTypeOptions: List<SavingsOptionsResponseDto> = emptyList(),
    val lockinPeriodFrequencyTypeOptions: List<SavingsOptionsResponseDto> = emptyList(),
    val withdrawalFeeTypeOptions: List<SavingsOptionsResponseDto> = emptyList(),
    val chargeOptions: ArrayList<SavingsChargeOptionsResponseDto> = arrayListOf(),
)

@Serializable
data class SavingsOptionsResponseDto(
    val id: Int,
    val code: String,
    val value: String,
)

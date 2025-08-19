/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.savings.Currency

@Serializable
@Parcelize
data class SavingsAccountTemplate(
    val clientId: Int = 0,
    val clientName: String? = null,
    val savingsProductId: Int? = null,
    val savingsProductName: String? = null,
    val timeline: Timeline? = null,
    val currency: Currency? = null,
    val nominalAnnualInterestRate: Double? = null,
    val interestCompoundingPeriodType: ProductOptions? = null,
    val interestPostingPeriodType: ProductOptions? = null,
    val interestCalculationType: ProductOptions? = null,
    val interestCalculationDaysInYearType: ProductOptions? = null,
    val minRequiredOpeningBalance: Double? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,
    val isDormancyTrackingActive: Boolean? = null,
    val charges: List<ChargeOptions>? = null,
    val productOptions: ArrayList<SavingsProduct> = arrayListOf(),
    val fieldOfficerOptions: List<FieldOfficerOptions> = emptyList(),
    val interestCompoundingPeriodTypeOptions: List<SavingsOptions> = emptyList(),
    val interestPostingPeriodTypeOptions: List<SavingsOptions> = emptyList(),
    val interestCalculationTypeOptions: List<SavingsOptions> = emptyList(),
    val interestCalculationDaysInYearTypeOptions: List<SavingsOptions> = emptyList(),
    val lockinPeriodFrequencyTypeOptions: List<SavingsOptions> = emptyList(),
    val withdrawalFeeTypeOptions: List<SavingsOptions> = emptyList(),
    val chargeOptions: ArrayList<ChargeOptions> = arrayListOf(),
) : Parcelable

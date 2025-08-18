/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class SavingsAccountApplicationPayload(

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
    val charges: List<SavingsChargePayload>? = null,

) : Parcelable

@Serializable
@Parcelize
data class SavingsChargePayload(
    val chargeId: Int,
    val amount: Double,
) : Parcelable

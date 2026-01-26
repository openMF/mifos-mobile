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
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.transaction.SavingsTransactionResponseDto

@Serializable
data class SavingsWithAssociationsResponseDto(

    val id: Long? = null,

    val accountNo: String? = null,

    val depositType: TypeResponseDto? = null,

    val externalId: String? = null,

    val clientId: Int? = null,

    val clientName: String? = null,

    val savingsProductId: Int? = null,

    val savingsProductName: String? = null,

    val fieldOfficerId: Int? = null,

    val status: SavingsStatusResponseDto? = null,

    val timeline: SavingsTimeLineResponseDto? = null,

    val currency: CurrencyResponseDto? = null,

    val nominalAnnualInterestRate: Double? = null,

    val minRequiredOpeningBalance: Double? = null,

    val lockinPeriodFrequency: Double? = null,

    val withdrawalFeeForTransfers: Boolean? = null,

    val allowOverdraft: Boolean? = null,

    val enforceMinRequiredBalance: Boolean? = null,

    val withHoldTax: Boolean? = null,

    val lastActiveTransactionDate: List<Int>? = null,

    val dormancyTrackingActive: Boolean? = null,

    val summary: SavingsSummaryResponseDto? = null,

    val transactions: List<SavingsTransactionResponseDto> = emptyList(),

)

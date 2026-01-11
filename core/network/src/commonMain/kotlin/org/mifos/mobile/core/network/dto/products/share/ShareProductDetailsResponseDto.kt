/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.products.share

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class ShareProductDetailsResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
    val description: String? = null,
    val currency: CurrencyResponseDto? = null,
    val totalShares: Int? = null,
    val totalSharesIssued: Int? = null,
    val unitPrice: Double? = null,
    val shareCapital: Double? = null,
    val nominalShares: Int? = null,
    val marketPrice: List<MarketPriceResponseDto>? = emptyList(),
    val charges: List<ShareProductChargeResponseDto>? = emptyList(),
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: TypeResponseDto? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodForDividendsTypeEnum: TypeResponseDto? = null,
    val accountingRule: TypeResponseDto? = null,
    val accountingMappings: AccountingMappingsResponseDto? = null,
    val clientSavingsAccounts: List<SavingsAccountSummaryResponseDto>? = emptyList(),
)

@Serializable
data class SavingsAccountSummaryResponseDto(
    val id: Int? = null,
    val savingsProductName: String? = null,
)

@Serializable
data class MarketPriceResponseDto(
    val id: Int? = null,
    val fromDate: String? = null,
    val shareValue: Double? = null,
)

@Serializable
data class AccountingMappingsResponseDto(
    val shareReferenceId: AccountingItemResponseDto? = null,
    val incomeFromFeeAccountId: AccountingItemResponseDto? = null,
    val shareEquityId: AccountingItemResponseDto? = null,
    val shareSuspenseId: AccountingItemResponseDto? = null,
)

@Serializable
data class AccountingItemResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val glCode: String? = null,
)

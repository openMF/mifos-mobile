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
data class ShareProductResponseDto(
    val accountingRule: TypeResponseDto? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val currency: CurrencyResponseDto? = null,
    val description: String? = null,
    val id: Int? = null,
    val lockPeriodTypeEnum: TypeResponseDto? = null,
    val lockinPeriod: Int? = null,
    val maximumShares: Int? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodForDividendsTypeEnum: TypeResponseDto? = null,
    val minimumShares: Int? = null,
    val name: String? = null,
    val nominalShares: Int? = null,
    val shareCapital: Double? = null,
    val shortName: String? = null,
    val totalShares: Int? = null,
    val totalSharesIssued: Int? = null,
    val unitPrice: Double? = null,
)

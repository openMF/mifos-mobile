/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.share

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.Charge

@Serializable
data class ShareAccountWithAssociations(
    val id: Long? = null,
    val accountNo: String? = null,
    val clientId: Long? = null,
    val clientName: String? = null,

    val productId: Long? = null,
    val productName: String? = null,

    val status: Status? = null,
    val currency: Currency? = null,
    val timeline: Timeline? = null,
    val summary: Summary? = null,

    val currentMarketPrice: Double? = null,
    val savingsAccountId: Long? = null,
    val savingsAccountNumber: String? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,

    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: EnumOptionData? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodTypeEnum: EnumOptionData? = null,

    val charges: List<Charge> = emptyList(),
    val purchasedShares: List<Transactions> = emptyList(),
    val dividends: List<String> = emptyList(),
)

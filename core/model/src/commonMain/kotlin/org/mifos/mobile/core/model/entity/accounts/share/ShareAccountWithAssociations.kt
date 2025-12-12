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
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.Charge

@Serializable
@Parcelize
data class ShareAccountWithAssociations(
    val id: Long,
    val accountNo: String,
    val clientId: Long,
    val clientName: String,

    val productId: Long,
    val productName: String,

    val status: Status,
    val currency: Currency,
    val timeline: Timeline,
    val summary: Summary,

    // Financials
    val currentMarketPrice: Double? = null,
    val savingsAccountId: Long? = null,
    val savingsAccountNumber: Long? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,

    // Configuration / Periods
    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: EnumOptionData? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodTypeEnum: EnumOptionData? = null,

    // Collections
    val charges: List<Charge> = emptyList(),
    val purchasedShares: List<Transactions> = emptyList(),
    val dividends: List<String> = emptyList(),
) : Parcelable

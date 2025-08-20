/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.shareProductDetails

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class ShareProductDetails(
    val id: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
    val description: String? = null,
    val currency: Currency? = null,
    val totalShares: Int? = null,
    val totalSharesIssued: Int? = null,
    val unitPrice: Double? = null,
    val shareCapital: Double? = null,
    val nominalShares: Int? = null,
    val marketPrice: List<MarketPrice>? = emptyList(),
    val charges: List<Charge>? = emptyList(),
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: EnumOption? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodForDividendsTypeEnum: EnumOption? = null,
    val accountingRule: EnumOption? = null,
    val accountingMappings: AccountingMappings? = null,
) : Parcelable

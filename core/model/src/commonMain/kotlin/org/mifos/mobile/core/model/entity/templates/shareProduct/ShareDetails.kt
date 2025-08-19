/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.shareProduct

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class ShareDetails(
    val id: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
    val description: String? = null,
    val currency: Currency? = null,
    val totalShares: Int? = null,
    val totalSharesIssued: Int? = null,
    val unitPrice: Int? = null,
    val shareCapital: Int? = null,
    val nominalShares: Int? = null,
    val marketPrice: List<MarketPrice>? = null,
    val charges: List<Charge>? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: LockPeriodTypeEnum? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodForDividendsTypeEnum: MinimumActivePeriodForDividendsTypeEnum? = null,
    val accountingRule: AccountingRule? = null,
    val accountingMappings: AccountingMappings? = null,
) : Parcelable

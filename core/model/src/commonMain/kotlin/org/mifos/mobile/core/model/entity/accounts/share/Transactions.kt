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

@Serializable
data class Transactions(
    val accountId: Long? = null,
    val amount: Double? = null,
    val amountPaid: Double? = null,
    val chargeAmount: Double? = null,
    val id: Long? = null,
    val numberOfShares: Int? = null,
    val purchasedDate: List<Int> = emptyList(),
    val purchasedPrice: Double? = null,
    val status: EnumOptionData? = null,
    val type: EnumOptionData? = null,
)

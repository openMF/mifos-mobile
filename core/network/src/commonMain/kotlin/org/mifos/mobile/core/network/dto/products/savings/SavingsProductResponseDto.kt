/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.products.savings

import kotlinx.serialization.Serializable

@Serializable
data class SavingsProductResponseDto(
    val id: Int,
    val name: String,
    val withdrawalFeeForTransfers: Boolean = false,
    val allowOverdraft: Boolean = false,
    val enforceMinRequiredBalance: Boolean = false,
    val lienAllowed: Boolean = false,
    val withHoldTax: Boolean = false,
)

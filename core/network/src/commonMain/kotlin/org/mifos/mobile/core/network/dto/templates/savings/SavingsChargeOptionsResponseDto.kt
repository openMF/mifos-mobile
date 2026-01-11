/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.templates.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class SavingsChargeOptionsResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val active: Boolean? = null,
    val penalty: Boolean? = null,
    val currency: CurrencyResponseDto? = null,
    val amount: Float = 0f,
    val chargeTimeType: TypeResponseDto,
    val chargeAppliesTo: TypeResponseDto,
    val chargeCalculationType: TypeResponseDto,
    val chargePaymentMode: TypeResponseDto,
)

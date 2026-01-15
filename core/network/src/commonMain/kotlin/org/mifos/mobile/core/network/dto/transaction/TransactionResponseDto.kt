/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class TransactionResponseDto(

    val id: Long? = null,

    val officeId: Long? = null,

    val officeName: String? = null,

    val type: TypeResponseDto,

    val date: List<Int> = emptyList(),

    val currency: CurrencyResponseDto? = null,

    val amount: Double? = null,

    val submittedOnDate: List<Int> = emptyList(),

    val reversed: Boolean? = null,

)

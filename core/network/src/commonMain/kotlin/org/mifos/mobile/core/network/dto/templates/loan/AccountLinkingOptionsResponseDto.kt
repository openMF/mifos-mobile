/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.templates.loan

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class AccountLinkingOptionsResponseDto(

    val accountNo: String? = null,

    val clientId: Int? = null,

    val clientName: String? = null,

    val currency: CurrencyResponseDto? = null,

    val fieldOfficerId: Int? = null,

    val id: Int? = null,

    val productId: Int? = null,

    val productName: String? = null,
)

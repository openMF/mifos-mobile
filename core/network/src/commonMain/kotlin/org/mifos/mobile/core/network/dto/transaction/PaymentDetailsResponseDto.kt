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

@Serializable
data class PaymentDetailsResponseDto(
    val id: Int? = null,

    val paymentType: PaymentTypeResponseDto,

    val accountNumber: String? = null,

    val checkNumber: String? = null,

    val routingCode: String? = null,

    val receiptNumber: String? = null,

    val bankNumber: String? = null,
)

@Serializable
data class PaymentTypeResponseDto(
    val id: Int? = null,

    val name: String? = null,
)

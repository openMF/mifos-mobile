/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class TransferPayloadDto(
    val fromOfficeId: Int? = null,

    val fromClientId: Long? = null,

    val fromAccountType: Int? = null,

    val fromAccountId: String? = null,

    val toOfficeId: Int? = null,

    val toClientId: Long? = null,

    val toAccountType: Int? = null,

    val toAccountId: String? = null,

    val transferDate: String? = null,

    val transferAmount: Double? = null,

    val transferDescription: String? = null,

    val dateFormat: String? = null,

    val locale: String? = null,

)

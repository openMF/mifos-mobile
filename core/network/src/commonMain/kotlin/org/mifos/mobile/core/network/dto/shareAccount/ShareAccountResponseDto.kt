/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.shareAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class ShareAccountResponseDto(

    val id: Long = 0,

    val accountNo: String? = null,

    val totalApprovedShares: Int? = null,

    val totalPendingForApprovalShares: Int? = null,

    val productId: Int? = null,

    val productName: String? = null,

    val shortProductName: String? = null,

    val status: ShareStatusResponseDto? = null,

    val currency: CurrencyResponseDto? = null,

    val timeline: ShareTimelineResponseDto? = null,

)

/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.pocket

import kotlinx.serialization.Serializable

@Serializable
data class PocketResponseDto(
    val loanAccounts: List<PocketAccountDto> = emptyList(),
    val savingsAccounts: List<PocketAccountDto> = emptyList(),
    val shareAccounts: List<PocketAccountDto> = emptyList(),
)

@Serializable
data class PocketAccountDto(
    val pocketId: Long,
    val accountId: Long,
    val accountType: Int,
    val accountNumber: String,
    val id: Long,
)

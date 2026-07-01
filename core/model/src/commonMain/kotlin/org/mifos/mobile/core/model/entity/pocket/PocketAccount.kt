/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.pocket

import org.mifos.mobile.core.model.enums.AccountType

data class PocketAccount(
    val pocketId: Long,
    val id: Long,
    val accountId: Long,
    val accountType: AccountType,
    val accountNumber: String,
)

data class DetailedPocketAccount(
    val pocket: PocketAccount,
    val productName: String?,
    val balance: Double?,
    val currencyCode: String?,
    val decimalPlaces: Int?,
    val status: AccountStatus?,
)
data class LinkableAccount(
    val accountId: Long,
    val productName: String?,
    val accountNumber: String?,
    val accountType: AccountType,
    val balance: Double?,
    val currencyCode: String?,
    val decimalPlaces: Int?,
    val status: AccountStatus?,
)

enum class AccountStatus {
    PENDING,
    APPROVED,
    ACTIVE,
    CLOSED,
    REJECTED,
    WITHDRAWN,
    OVERPAID,
    MATURED,
    UNKNOWN,
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.model.enums.TransferType

@Serializable
data class AccountDetails(
    val accountId: Long,
    val accountNo: String = "",
    val outstandingBalance: Double? = null,
    val transferType: String,
    val transferTarget: TransferType,
    val transferSuccessDestination: String,
)

@Serializable
data class TransferArgs(
    val transferPayloadJson: String?,
) {
    val transferPayload: AccountDetails?
        get() = transferPayloadJson?.let { Json.decodeFromString<AccountDetails>(it) }

    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): TransferArgs = Json.decodeFromString(json)
    }
}

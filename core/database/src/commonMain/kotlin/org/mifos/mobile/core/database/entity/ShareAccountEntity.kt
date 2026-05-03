/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "share_accounts")
data class ShareAccountEntity(
    @PrimaryKey(autoGenerate = false)
    val shareId: Long,
    val clientId: Long,
    val accountNo: String,
    val productName: String,
    val totalApprovedShares: Int,
    val totalPendingShares: Int,
    val status: String,
    val currencyCode: String,
    val lastFetchedAt: Long = 0L,
)

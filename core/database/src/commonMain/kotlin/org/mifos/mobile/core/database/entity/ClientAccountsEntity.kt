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

@Entity(tableName = "client_accounts_summary")
data class ClientAccountsEntity(
    @PrimaryKey(autoGenerate = false)
    val clientId: Long,
    val loanAccountsJson: String,
    val savingsAccountsJson: String,
    val shareAccountsJson: String,
    val lastFetchedAt: Long = 0L,
)

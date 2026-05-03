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

@Entity(tableName = "guarantors")
data class GuarantorEntity(
    @PrimaryKey(autoGenerate = false)
    val guarantorId: Long,
    val loanId: Long,
    val firstname: String,
    val lastname: String,
    val guarantorType: String,
    val relationship: String? = null,
    val amount: Double? = null,
    val lastFetchedAt: Long = 0L,
)

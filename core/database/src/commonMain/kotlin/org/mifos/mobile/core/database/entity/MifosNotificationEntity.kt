/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.entity

import org.mifos.mobile.core.database.Entity
import org.mifos.mobile.core.database.PrimaryKey

@Entity(
    tableName = "mifos_notification",
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    foreignKeys = [],
    ignoredColumns = [],
)
data class MifosNotificationEntity(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: Long,
    val msg: String?,
    val read: Boolean?,
)

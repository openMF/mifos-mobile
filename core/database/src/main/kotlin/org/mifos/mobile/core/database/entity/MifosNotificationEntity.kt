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

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mifos_notifications")
data class MifosNotificationEntity(
    @PrimaryKey val timeStamp: Long,
    val msg: String? = null,
    val read: Boolean? = null,
)

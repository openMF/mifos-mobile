/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import org.mifos.mobile.core.database.entity.ChargeEntity
import org.mifos.mobile.core.database.entity.MifosNotificationEntity
import org.mifos.mobile.core.database.utils.ChargeTypeConverters

@Database(
    entities = [
        ChargeEntity::class,
        MifosNotificationEntity::class,
    ],
    version = SelfServiceDatabase.VERSION,
    exportSchema = true,
    autoMigrations = [],
)
@TypeConverters(ChargeTypeConverters::class)
abstract class SelfServiceDatabase : RoomDatabase() {

    abstract fun mifosNotificationDao(): MifosNotificationDao

    abstract fun chargeDao(): ChargeDao

    companion object {
        // Update the version number if you change the schema
        const val VERSION = 1
    }
}

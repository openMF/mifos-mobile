/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import org.mifos.mobile.core.database.dao.PocketAccountDao
import org.mifos.mobile.core.database.entity.ChargeEntity
import org.mifos.mobile.core.database.entity.MifosNotificationEntity
import org.mifos.mobile.core.database.entity.PendingPocketDelinkEntity
import org.mifos.mobile.core.database.entity.PocketAccountEntity
import org.mifos.mobile.core.database.utils.ChargeTypeConverters

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

@Database(
    entities = [
        ChargeEntity::class,
        MifosNotificationEntity::class,
        PocketAccountEntity::class,
        PendingPocketDelinkEntity::class,
    ],
    version = AppDatabase.VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@TypeConverters(ChargeTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
actual abstract class AppDatabase : RoomDatabase() {
    actual abstract val mifosNotificationDao: MifosNotificationDao
    actual abstract val chargeDao: ChargeDao
    actual abstract val pocketAccountDao: PocketAccountDao

    companion object {
        const val VERSION = 2
        const val DATABASE_NAME = "mifos_database.db"
    }
}

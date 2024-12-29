/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mifos.mobile.core.datastore.dao.ChargeDao
import org.mifos.mobile.core.datastore.dao.MifosNotificationDao
import org.mifos.mobile.core.datastore.entity.Charge
import org.mifos.mobile.core.datastore.entity.MifosNotification
import org.mifos.mobile.core.datastore.utils.ChargeTypeConverters

@Database(
    entities = [Charge::class, MifosNotification::class],
    version = SelfServiceDatabase.VERSION,
    exportSchema = false,
)
@TypeConverters(ChargeTypeConverters::class)
abstract class SelfServiceDatabase : RoomDatabase() {

    abstract fun mifosNotificationDao(): MifosNotificationDao
    abstract fun chargeDao(): ChargeDao

    companion object {
        private const val NAME: String = "SelfService"
        const val VERSION: Int = 1

        @Volatile
        @Suppress("ktlint:standard:property-naming")
        private var INSTANCE: SelfServiceDatabase? = null

        fun getDatabase(context: Context): SelfServiceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SelfServiceDatabase::class.java,
                    NAME,
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                Log.e("SelfServiceDatabase", "Database initialized")
                instance
            }
        }
    }
}

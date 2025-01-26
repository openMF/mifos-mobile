/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

class AppDatabaseFactory {
    fun createDatabase(): RoomDatabase.Builder<AppDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Bookpedia")
            os.contains("mac") -> File(userHome, "Library/Application Support/Bookpedia")
            else -> File(userHome, ".local/share/Bookpedia")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, AppDatabase.DATABASE_NAME)
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}

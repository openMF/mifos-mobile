/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

/**
 * Desktop (JVM) factory for creating Room 3 database instances.
 *
 * Resolves the database path to the OS-appropriate application data directory:
 * - **Windows**: `%APPDATA%/MifosDatabase/`
 * - **macOS**: `~/Library/Application Support/MifosDatabase/`
 * - **Linux**: `~/.local/share/MifosDatabase/`
 */
class AppDatabaseFactory {

    /**
     * Creates a [RoomDatabase.Builder] for the given database type.
     *
     * Automatically creates parent directories if they don't exist.
     *
     * @param T The concrete [RoomDatabase] subclass.
     * @param databaseName On-disk file name for the database.
     * @return A pre-configured builder.
     */
    inline fun <reified T : RoomDatabase> createDatabase(
        databaseName: String,
    ): RoomDatabase.Builder<T> {
        val dbPath = getDatabasePath(databaseName)
        dbPath.parentFile?.mkdirs()
        return Room.databaseBuilder<T>(
            name = dbPath.absolutePath,
        )
    }

    /**
     * Resolves the full filesystem path for the database file.
     *
     * Visibility is `@PublishedApi internal` because it is called from the
     * `inline` [createDatabase] function but should not be part of the public API.
     */
    @PublishedApi
    internal fun getDatabasePath(databaseName: String): File {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "MifosDatabase")
            os.contains("mac") -> File(userHome, "Library/Application Support/MifosDatabase")
            else -> File(userHome, ".local/share/MifosDatabase")
        }
        return File(appDataDir, databaseName)
    }
}

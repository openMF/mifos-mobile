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
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Native (iOS/macOS) factory for creating Room 3 database instances.
 *
 * Stores the database in the app's `NSDocumentDirectory`, which is the standard
 * location for user-generated data on Apple platforms.
 */
class AppDatabaseFactory {

    /**
     * Creates a [RoomDatabase.Builder] for the given database type.
     *
     * @param T The concrete [RoomDatabase] subclass.
     * @param databaseName On-disk file name for the database.
     * @return A pre-configured builder.
     */
    inline fun <reified T : RoomDatabase> createDatabase(
        databaseName: String,
    ): RoomDatabase.Builder<T> {
        val dbFilePath = documentDirectory() + "/$databaseName"
        return Room.databaseBuilder<T>(
            name = dbFilePath,
        )
    }

    /**
     * Returns the absolute path to the app's document directory on Apple platforms.
     *
     * @throws IllegalArgumentException if the document directory cannot be resolved.
     */
    @OptIn(ExperimentalForeignApi::class)
    fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}

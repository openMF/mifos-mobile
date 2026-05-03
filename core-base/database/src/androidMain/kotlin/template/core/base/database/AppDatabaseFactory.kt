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

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

/**
 * Android-specific factory for creating Room 3 database instances.
 *
 * Uses the application [Context] to resolve the platform database directory.
 *
 * @param context Android application context used for database path resolution.
 */
class AppDatabaseFactory(
    @PublishedApi internal val context: Context,
) {

    /**
     * Creates a [RoomDatabase.Builder] for the given database type.
     *
     * @param T The concrete [RoomDatabase] subclass (e.g., `AppDatabase`).
     * @param databaseName On-disk file name for the database.
     * @return A pre-configured builder — callers should chain `.setDriver()`,
     *   `.setQueryCoroutineContext()`, and `.build()`.
     */
    inline fun <reified T : RoomDatabase> createDatabase(
        databaseName: String,
    ): RoomDatabase.Builder<T> {
        return Room.databaseBuilder<T>(
            context = context.applicationContext,
            name = context.getDatabasePath(databaseName).absolutePath,
        )
    }
}

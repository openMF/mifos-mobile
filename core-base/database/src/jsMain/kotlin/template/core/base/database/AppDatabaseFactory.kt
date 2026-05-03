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

/**
 * JS (Kotlin/JS) factory for creating Room 3 database instances.
 *
 * Uses the browser's Origin Private File System (OPFS) for persistence via the
 * `sqlite-web` driver. The [databaseName] is used as the OPFS file name.
 */
class AppDatabaseFactory {

    /**
     * Creates a [RoomDatabase.Builder] for the given database type.
     *
     * @param T The concrete [RoomDatabase] subclass.
     * @param databaseName OPFS file name for the database.
     * @return A pre-configured builder.
     */
    inline fun <reified T : RoomDatabase> createDatabase(
        databaseName: String,
    ): RoomDatabase.Builder<T> {
        return Room.databaseBuilder<T>(
            name = databaseName,
        )
    }
}

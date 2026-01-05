/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Android-specific implementation of the database factory for creating Room database instances.
 *
 * This factory class provides a standardized approach to database creation on Android platforms,
 * ensuring proper context handling and consistent database configuration across the application.
 * The factory leverages Android's application context to prevent memory leaks and maintain
 * database accessibility throughout the application lifecycle.
 *
 * Key features:
 * - Automatic application context usage to prevent memory leaks
 * - Type-safe database creation with compile-time verification
 * - Consistent database naming and configuration
 * - Integration with Android's storage systems
 *
 * @param context The Android context used for database creation, typically an Application or Activity context
 *
 * @see androidx.room.Room
 * @see androidx.room.RoomDatabase
 */
class AppDatabaseFactory(
    private val context: Context,
) {

    /**
     * Creates a Room database builder configured for Android environments.
     *
     * This method constructs a RoomDatabase.Builder instance that can be further customized
     * with additional configuration options such as migrations, type converters, or callback
     * handlers before building the final database instance.
     *
     * The method automatically uses the application context to ensure the database remains
     * accessible throughout the application lifecycle while preventing potential memory leaks
     * that could occur when using activity or service contexts.
     *
     * @param T The type of RoomDatabase to create, must extend RoomDatabase
     * @param databaseClass The Class object representing the database type to instantiate
     * @param databaseName The name of the database file to create or access
     * @return A RoomDatabase.Builder instance ready for additional configuration and building
     *
     * @throws IllegalArgumentException if the database class is invalid or cannot be instantiated
     * @throws SQLiteException if there are issues with database creation or access
     *
     * Example usage:
     * ```kotlin
     * class MyApplication : Application() {
     *     private val databaseFactory = AppDatabaseFactory(this)
     *
     *     val userDatabase: UserDatabase by lazy {
     *         databaseFactory
     *             .createDatabase(UserDatabase::class.java, "user_database.db")
     *             .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
     *             .addTypeConverter(DateConverters())
     *             .build()
     *     }
     * }
     * ```
     *
     * Configuration recommendations:
     * - Use descriptive database names that reflect their purpose
     * - Consider implementing proper migration strategies for schema changes
     * - Add appropriate type converters for complex data types
     * - Configure database callbacks for initialization or validation logic
     */
    fun <T : RoomDatabase> createDatabase(databaseClass: Class<T>, databaseName: String): RoomDatabase.Builder<T> {
        return Room.databaseBuilder(
            context.applicationContext,
            databaseClass,
            databaseName,
        )
    }
}

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

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.util.findAndInstantiateDatabaseImpl
import java.io.File

/**
 * Desktop-specific implementation of the database factory for creating Room database instances.
 *
 * This factory class provides cross-platform desktop database creation capabilities,
 * automatically selecting appropriate storage locations based on the operating system.
 * The implementation ensures databases are stored in platform-conventional directories
 * that provide appropriate persistence and user access patterns.
 *
 * Platform-specific storage locations:
 * - Windows: %APPDATA%/MifosDatabase
 * - macOS: ~/Library/Application Support/MifosDatabase
 * - Linux: ~/.local/share/MifosDatabase
 *
 * Key features:
 * - Automatic platform detection and directory selection
 * - Cross-platform file system compatibility
 * - Type-safe database creation using inline reified generics
 * - Automatic directory creation when required
 * - Integration with JVM-based Room implementations
 *
 * @see androidx.room.Room
 * @see androidx.room.RoomDatabase
 */
class AppDatabaseFactory {

    /**
     * Creates a Room database builder configured for desktop environments.
     *
     * This method constructs a RoomDatabase.Builder instance specifically configured
     * for desktop applications, with automatic platform-appropriate storage location
     * selection. The method leverages inline reified generics to provide type safety
     * while maintaining flexibility in database instantiation.
     *
     * The implementation automatically detects the current operating system and
     * selects the conventional application data directory for that platform,
     * ensuring databases are stored in locations that align with user expectations
     * and system conventions.
     *
     * @param T The type of RoomDatabase to create, must extend RoomDatabase
     * @param databaseName The name of the database file to create or access
     * @param factory Optional factory function for database instantiation, defaults to Room's automatic discovery
     * @return A RoomDatabase.Builder instance ready for additional configuration and building
     *
     * @throws SecurityException if the application lacks permission to create directories or files
     * @throws IOException if there are file system issues during directory or database creation
     * @throws ClassNotFoundException if the database implementation class cannot be located
     *
     * Directory creation behavior:
     * - Automatically creates the MifosDatabase directory if it does not exist
     * - Respects existing directory permissions and structure
     * - Uses platform-appropriate path separators and naming conventions
     *
     * Example usage:
     * ```kotlin
     * class DesktopApplication {
     *     private val databaseFactory = AppDatabaseFactory()
     *
     *     val transactionDatabase: TransactionDatabase by lazy {
     *         databaseFactory
     *             .createDatabase<TransactionDatabase>("transactions.db")
     *             .addMigrations(MIGRATION_1_2)
     *             .enableMultiInstanceInvalidation()
     *             .build()
     *     }
     *
     *     val userDatabase: UserDatabase by lazy {
     *         databaseFactory
     *             .createDatabase<UserDatabase>("users.db") {
     *                 // Custom factory implementation if needed
     *                 UserDatabase_Impl()
     *             }
     *             .addTypeConverter(CustomConverters())
     *             .build()
     *     }
     * }
     * ```
     *
     * Platform considerations:
     * - Windows installations should ensure %APPDATA% is accessible
     * - macOS applications may require appropriate entitlements for file system access
     * - Linux environments should verify user home directory permissions
     * - Consider backup and synchronization implications of chosen storage locations
     */
    inline fun <reified T : RoomDatabase> createDatabase(
        databaseName: String,
        noinline factory: () -> T = { findAndInstantiateDatabaseImpl(T::class.java) },
    ): RoomDatabase.Builder<T> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "MifosDatabase")
            os.contains("mac") -> File(userHome, "Library/Application Support/MifosDatabase")
            else -> File(userHome, ".local/share/MifosDatabase")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, databaseName)

        return Room.databaseBuilder(
            name = dbFile.absolutePath,
            factory = factory,
        )
    }
}

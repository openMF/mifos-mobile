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
import androidx.room.util.findDatabaseConstructorAndInitDatabaseImpl
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Native platform implementation of the database factory for iOS and macOS applications.
 *
 * This factory class provides database creation capabilities specifically designed for
 * iOS and macOS platforms using Kotlin/Native interoperability with Foundation framework APIs.
 * The implementation ensures databases are stored in the standard document directory,
 * providing appropriate persistence characteristics and compliance with platform guidelines.
 *
 * Platform integration features:
 * - Direct integration with iOS/macOS Foundation framework
 * - Standard document directory storage following Apple guidelines
 * - Type-safe database creation using inline reified generics
 * - Kotlin/Native C interop for optimal platform performance
 * - Compliance with iOS app sandbox requirements
 *
 * Storage characteristics:
 * - Databases stored in application's document directory
 * - Automatic backup eligibility through iTunes/iCloud (configurable)
 * - Persistent across application updates and device restores
 * - Accessible through Files app on iOS (when appropriate)
 *
 * @see androidx.room.Room
 * @see androidx.room.RoomDatabase
 * @see platform.Foundation.NSFileManager
 */
class AppDatabaseFactory {

    /**
     * Creates a Room database builder configured for iOS/macOS native environments.
     *
     * This method constructs a RoomDatabase.Builder instance specifically optimized
     * for native iOS and macOS applications. The implementation leverages Foundation
     * framework APIs through Kotlin/Native interop to ensure proper platform integration
     * and adherence to Apple's storage guidelines.
     *
     * The database file is automatically placed in the application's document directory,
     * which provides appropriate persistence characteristics and follows Apple's
     * recommended storage patterns for user-generated content and application data.
     *
     * @param T The type of RoomDatabase to create, must extend RoomDatabase
     * @param databaseName The name of the database file to create within the document directory
     * @param factory Optional factory function for database instantiation, defaults to Room's constructor discovery
     * @return A RoomDatabase.Builder instance ready for additional configuration and building
     *
     * @throws NSException if document directory access fails or is unavailable
     * @throws RuntimeException if the database constructor cannot be located or instantiated
     * @throws SecurityException if the application lacks required file system permissions
     *
     * Platform considerations:
     * - Database files are eligible for iCloud backup by default
     * - Files may be accessible through the Files app depending on configuration
     * - Storage location complies with App Store Review Guidelines
     * - Automatic cleanup may occur during low storage conditions
     *
     * Example usage:
     * ```kotlin
     * class IOSApp {
     *     private val databaseFactory = AppDatabaseFactory()
     *
     *     val coreDatabase: CoreDatabase by lazy {
     *         databaseFactory
     *             .createDatabase<CoreDatabase>("core_data.db")
     *             .addMigrations(MIGRATION_VERSIONS)
     *             .setJournalMode(RoomDatabase.JournalMode.WAL)
     *             .build()
     *     }
     *
     *     val cacheDatabase: CacheDatabase by lazy {
     *         databaseFactory
     *             .createDatabase<CacheDatabase>("cache.db") {
     *                 CacheDatabase_Impl()
     *             }
     *             .addCallback(object : RoomDatabase.Callback() {
     *                 override fun onCreate(db: SupportSQLiteDatabase) {
     *                     // Initialize cache tables
     *                 }
     *             })
     *             .build()
     *     }
     * }
     * ```
     *
     * Configuration recommendations:
     * - Consider WAL mode for improved concurrent access performance
     * - Implement proper migration strategies for iOS app updates
     * - Configure backup exclusion for cache or temporary databases
     * - Monitor storage usage in compliance with platform guidelines
     */
    inline fun <reified T : RoomDatabase> createDatabase(
        databaseName: String,
        noinline factory: () -> T = { findDatabaseConstructorAndInitDatabaseImpl(T::class) },
    ): RoomDatabase.Builder<T> {
        val dbFilePath = documentDirectory() + "/$databaseName"
        return Room.databaseBuilder(
            name = dbFilePath,
            factory = factory,
        )
    }

    /**
     * Retrieves the path to the application's document directory using Foundation framework APIs.
     *
     * This method provides access to the standard iOS/macOS document directory through
     * Kotlin/Native interop with the Foundation framework. The document directory serves
     * as the primary location for storing user-generated content and application data
     * that should persist across application launches and system updates.
     *
     * The implementation uses NSFileManager to locate the document directory within
     * the user domain, ensuring proper sandboxing compliance and platform integration.
     * This approach guarantees that database files are stored in locations that align
     * with Apple's storage guidelines and user expectations.
     *
     * @return The absolute file system path to the application's document directory
     *
     * @throws RuntimeException if the document directory cannot be located or accessed
     * @throws NSException if Foundation framework calls fail due to system restrictions
     *
     * Directory characteristics:
     * - Persistent across application updates and device restores
     * - Included in iTunes and iCloud backups by default
     * - Accessible through document provider extensions when configured
     * - Subject to iOS storage management and optimization
     *
     * Implementation notes:
     * - Uses NSUserDomainMask to ensure user-specific directory access
     * - Leverages NSDocumentDirectory constant for standard directory location
     * - Employs Kotlin/Native C interop for optimal performance and integration
     * - Handles potential nil responses from Foundation framework appropriately
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

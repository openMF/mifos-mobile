/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("KotlinNoActualForExpect")

package template.core.base.database

import kotlin.reflect.KClass

/**
 * Cross-platform annotation for marking interfaces as Data Access Objects (DAOs).
 *
 * This annotation is used to mark interfaces that contain database access methods.
 * The Room persistence library will generate implementations of these interfaces
 * at compile time.
 *
 * Example:
 * ```kotlin
 * @Dao
 * interface UserDao {
 *     @Query("SELECT * FROM users")
 *     suspend fun getAllUsers(): List<User>
 * }
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect annotation class Dao()

/**
 * Cross-platform annotation for defining SQL queries on DAO methods.
 *
 * This annotation is used to define raw SQL queries that will be executed
 * when the annotated method is called. The query can contain parameters
 * that correspond to method parameters.
 *
 * @param value The SQL query string to execute
 *
 * Example:
 * ```kotlin
 * @Query("SELECT * FROM users WHERE age > :minAge")
 * suspend fun getUsersOlderThan(minAge: Int): List<User>
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.BINARY)
expect annotation class Query(
    val value: String,
)

/**
 * Cross-platform annotation for marking DAO methods that insert entities into the database.
 *
 * This annotation defines how the method should behave when inserting entities.
 * It can handle single entities, lists of entities, or arrays of entities.
 *
 * @param entity The entity class that this method inserts (used for type checking)
 * @param onConflict Strategy to use when there's a conflict during insertion
 *
 * Example:
 * ```kotlin
 * @Insert(onConflict = OnConflictStrategy.REPLACE)
 * suspend fun insertUser(user: User): Long
 *
 * @Insert(onConflict = OnConflictStrategy.IGNORE)
 * suspend fun insertUsers(users: List<User>): List<Long>
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Insert(
    val entity: KClass<*>,
    val onConflict: Int,
)

/**
 * Cross-platform annotation for marking entity fields as primary keys.
 *
 * This annotation identifies which field(s) serve as the primary key for the entity.
 * Primary keys uniquely identify each row in the database table.
 *
 * @param autoGenerate Whether the database should automatically generate values for this primary key
 *
 * Example:
 * ```kotlin
 * @Entity
 * data class User(
 *     @PrimaryKey(autoGenerate = true)
 *     val id: Long = 0,
 *     val name: String
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class PrimaryKey(
    val autoGenerate: Boolean,
)

/**
 * Cross-platform annotation for defining foreign key constraints.
 *
 * This annotation is used within the @Entity annotation to define relationships
 * between entities through foreign key constraints. It ensures referential integrity
 * between related tables.
 *
 * Example:
 * ```kotlin
 * @Entity(
 *     foreignKeys = [ForeignKey(
 *         entity = User::class,
 *         parentColumns = ["id"],
 *         childColumns = ["userId"],
 *         onDelete = ForeignKey.CASCADE
 *     )]
 * )
 * data class Post(
 *     @PrimaryKey val id: Long,
 *     val userId: Long,
 *     val content: String
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(allowedTargets = [])
@Retention(AnnotationRetention.BINARY)
expect annotation class ForeignKey

/**
 * Cross-platform annotation for defining database indexes.
 *
 * Indexes improve query performance by creating optimized data structures
 * for faster data retrieval. This annotation is used within the @Entity
 * annotation to define indexes on one or more columns.
 *
 * Example:
 * ```kotlin
 * @Entity(
 *     indices = [
 *         Index(value = ["email"], unique = true),
 *         Index(value = ["firstName", "lastName"])
 *     ]
 * )
 * data class User(
 *     @PrimaryKey val id: Long,
 *     val email: String,
 *     val firstName: String,
 *     val lastName: String
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(allowedTargets = [])
@Retention(AnnotationRetention.BINARY)
expect annotation class Index

/**
 * Cross-platform annotation for marking classes as database entities.
 *
 * This annotation transforms a Kotlin class into a database table.
 * Each instance of the class represents a row in the table, and each
 * property represents a column.
 *
 * @param tableName Custom name for the database table (defaults to class name)
 * @param indices Array of indexes to create on this table
 * @param inheritSuperIndices Whether to inherit indexes from parent classes
 * @param primaryKeys Array of column names that form the composite primary key
 * @param foreignKeys Array of foreign key constraints for this table
 * @param ignoredColumns Array of property names to exclude from the table
 *
 * Example:
 * ```kotlin
 * @Entity(
 *     tableName = "user_profiles",
 *     indices = [Index(value = ["email"], unique = true)]
 * )
 * data class UserProfile(
 *     @PrimaryKey(autoGenerate = true)
 *     val id: Long = 0,
 *     val email: String,
 *     val displayName: String
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Entity(
    val tableName: String,
    val indices: Array<Index>,
    val inheritSuperIndices: Boolean,
    val primaryKeys: Array<String>,
    val foreignKeys: Array<ForeignKey>,
    val ignoredColumns: Array<String>,
)

/**
 * Cross-platform annotation for marking DAO methods that update entities in the database.
 *
 * This annotation is used to update existing entities in the database.
 * The method can accept single entities, lists of entities, or arrays of entities.
 * By default, it uses the primary key to identify which rows to update.
 *
 * @param entity The entity class that this method updates (used for type checking)
 * @param onConflict Strategy to use when there's a conflict during update
 *
 * Example:
 * ```kotlin
 * @Update(onConflict = OnConflictStrategy.REPLACE)
 * suspend fun updateUser(user: User): Int
 *
 * @Update
 * suspend fun updateUsers(users: List<User>): Int
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Update(
    val entity: KClass<*>,
    val onConflict: Int,
)

/**
 * Cross-platform annotation for marking DAO methods that delete entities from the database.
 *
 * This annotation is used to delete entities from the database.
 * The method can accept single entities, lists of entities, or arrays of entities.
 * The entities are matched by their primary key.
 *
 * @param entity The entity class that this method deletes (used for type checking)
 *
 * Example:
 * ```kotlin
 * @Delete
 * suspend fun deleteUser(user: User): Int
 *
 * @Delete
 * suspend fun deleteUsers(users: List<User>): Int
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Delete(
    val entity: KClass<*>,
)

/**
 * Cross-platform annotation for marking DAO methods that insert or update entities.
 *
 * This annotation combines insert and update operations. If the entity doesn't exist
 * (based on primary key), it will be inserted. If it exists, it will be updated.
 * This is useful for synchronization scenarios.
 *
 * @param entity The entity class that this method upserts (used for type checking)
 *
 * Example:
 * ```kotlin
 * @Upsert
 * suspend fun upsertUser(user: User): Long
 *
 * @Upsert
 * suspend fun upsertUsers(users: List<User>): List<Long>
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Upsert(
    val entity: KClass<*>,
)

/**
 * Cross-platform annotation for marking DAO methods that should run in a transaction.
 *
 * This annotation ensures that all database operations within the method are executed
 * as a single atomic transaction. If any operation fails, all changes are rolled back.
 * This is crucial for maintaining data consistency.
 *
 * Example:
 * ```kotlin
 * @Transaction
 * suspend fun transferFunds(fromAccount: Account, toAccount: Account) {
 *     updateAccount(fromAccount)
 *     updateAccount(toAccount)
 *     insertTransaction(Transaction(...))
 * }
 *
 * @Transaction
 * @Query("SELECT * FROM users WHERE id = :userId")
 * suspend fun getUserWithPosts(userId: Long): UserWithPosts
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Transaction()

/**
 * Cross-platform annotation for customizing column properties in database tables.
 *
 * This annotation allows you to specify custom column names, types, indexes,
 * and collation sequences for entity fields. Use it when the default column
 * mapping doesn't meet your requirements.
 *
 * @param name Custom name for the database column
 * @param typeAffinity The type affinity for the column (INTEGER, TEXT, REAL, BLOB)
 * @param index Whether to create an index on this column
 * @param collate The collation sequence for text columns (BINARY, NOCASE, RTRIM, LOCALIZED, UNICODE)
 * @param defaultValue The SQL default value expression for the column
 *
 * Example:
 * ```kotlin
 * @Entity
 * data class User(
 *     @PrimaryKey(autoGenerate = true)
 *     val id: Long = 0,
 *     @ColumnInfo(name = "user_name", collate = CollationSequence.NOCASE)
 *     val name: String,
 *     @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
 *     val createdAt: String
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class ColumnInfo(
    val name: String,
    val typeAffinity: Int,
    val index: Boolean,
    val collate: Int,
    val defaultValue: String,
)

/**
 * Cross-platform annotation for embedding objects within entities.
 *
 * This annotation allows you to include all fields from another object
 * directly into the parent entity's table. This is useful for normalizing
 * data models without creating separate tables.
 *
 * @param prefix Prefix to add to embedded field names to avoid naming conflicts
 *
 * Example:
 * ```kotlin
 * data class Address(
 *     val street: String,
 *     val city: String,
 *     val zipCode: String
 * )
 *
 * @Entity
 * data class User(
 *     @PrimaryKey val id: Long,
 *     val name: String,
 *     @Embedded(prefix = "home_")
 *     val homeAddress: Address,
 *     @Embedded(prefix = "work_")
 *     val workAddress: Address
 * )
 * // This creates columns: id, name, home_street, home_city, home_zipCode, work_street, work_city, work_zipCode
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Embedded(
    val prefix: String,
)

/**
 * Cross-platform annotation for defining relationships between entities.
 *
 * This annotation is used to establish one-to-many or many-to-many relationships
 * between entities. It's typically used in data classes that represent joined
 * queries and must be used within methods annotated with @Transaction.
 *
 * @param entity The entity class that this relation points to
 * @param parentColumn The column name in the parent entity
 * @param entityColumn The column name in the related entity
 * @param associateBy Junction table information for many-to-many relationships
 * @param projection List of columns to fetch from the related entity
 *
 * Example:
 * ```kotlin
 * data class UserWithPosts(
 *     @Embedded val user: User,
 *     @Relation(
 *         parentColumn = "id",
 *         entityColumn = "userId"
 *     )
 *     val posts: List<Post>
 * )
 *
 * @Dao
 * interface UserDao {
 *     @Transaction
 *     @Query("SELECT * FROM users WHERE id = :userId")
 *     suspend fun getUserWithPosts(userId: Long): UserWithPosts
 * }
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class Relation(
    val entity: KClass<*>,
    val parentColumn: String,
    val entityColumn: String,
    val associateBy: Junction,
    val projection: Array<String>,
)

/**
 * Cross-platform annotation for defining junction tables in many-to-many relationships.
 *
 * This annotation is used within @Relation to specify the junction table
 * that connects two entities in a many-to-many relationship.
 *
 * @param value The junction entity class
 * @param parentColumn The column in the junction table that references the parent entity
 * @param entityColumn The column in the junction table that references the child entity
 *
 * Example:
 * ```kotlin
 * @Entity(primaryKeys = ["userId", "playlistId"])
 * data class UserPlaylistCrossRef(
 *     val userId: Long,
 *     val playlistId: Long
 * )
 *
 * data class UserWithPlaylists(
 *     @Embedded val user: User,
 *     @Relation(
 *         parentColumn = "id",
 *         entityColumn = "id",
 *         associateBy = Junction(
 *             value = UserPlaylistCrossRef::class,
 *             parentColumn = "userId",
 *             entityColumn = "playlistId"
 *         )
 *     )
 *     val playlists: List<Playlist>
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(allowedTargets = [])
@Retention(AnnotationRetention.BINARY)
expect annotation class Junction(
    val value: KClass<*>,
    val parentColumn: String,
    val entityColumn: String,
)

/**
 * Cross-platform annotation for marking methods as database type converters.
 *
 * Type converters enable Room to store and retrieve complex data types that are not
 * natively supported by SQLite. This annotation marks methods that convert between
 * custom types and primitive types that SQLite can understand.
 *
 * Example:
 * ```kotlin
 * object DateConverters {
 *     @TypeConverter
 *     fun fromTimestamp(value: Long?): Date? {
 *         return value?.let { Date(it) }
 *     }
 *
 *     @TypeConverter
 *     fun dateToTimestamp(date: Date?): Long? {
 *         return date?.time
 *     }
 * }
 *
 * // Register in database
 * @Database(...)
 * @TypeConverters(DateConverters::class)
 * abstract class MyDatabase : RoomDatabase()
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class TypeConverter()

/**
 * Cross-platform annotation for specifying which type converters to use.
 *
 * This annotation tells Room which type converter classes to use for an entity,
 * DAO, or database. It can be applied at different scopes to control where
 * converters are available.
 *
 * @param value Array of type converter classes
 * @param builtInTypeConverters Configuration for built-in type converters
 *
 * Example:
 * ```kotlin
 * @Database(
 *     entities = [User::class, Post::class],
 *     version = 1
 * )
 * @TypeConverters(Converters::class)
 * abstract class AppDatabase : RoomDatabase() {
 *     abstract fun userDao(): UserDao
 * }
 *
 * @Entity
 * @TypeConverters(DateConverters::class)
 * data class Event(
 *     @PrimaryKey val id: Long,
 *     val date: Date
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.BINARY)
expect annotation class TypeConverters(
    /**
     * The list of type converter classes. If converter methods are not static, Room will create an
     * instance of these classes.
     *
     * @return The list of classes that contains the converter methods.
     */
    vararg val value: KClass<*>,

    /**
     * Configure whether Room can use various built in converters for common types. See
     * [BuiltInTypeConverters] for details.
     */
    val builtInTypeConverters: BuiltInTypeConverters,
)

/**
 * Cross-platform annotation for configuring built-in type converters.
 *
 * This annotation allows you to enable or disable Room's built-in type converters
 * for specific types like enums and UUID. Use it within @TypeConverters annotation.
 *
 * Note: For advanced configuration, reference androidx.room.BuiltInTypeConverters.State directly.
 * The default constructor uses INHERITED for all converters (enabled by default).
 *
 * Example:
 * ```kotlin
 * @TypeConverters(
 *     value = [CustomConverters::class],
 *     builtInTypeConverters = BuiltInTypeConverters()
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(allowedTargets = [])
@Retention(AnnotationRetention.BINARY)
expect annotation class BuiltInTypeConverters()

/**
 * Cross-platform annotation for marking a class as a Room database.
 *
 * This annotation defines the database configuration, including the list of entities,
 * database version, and whether to export the schema. The annotated class must be
 * abstract and extend RoomDatabase.
 *
 * @param entities Array of entity classes that belong to this database
 * @param version Database version number (increment when schema changes)
 * @param exportSchema Whether to export the database schema to a folder
 * @param views Array of database view classes
 * @param autoMigrations Array of automatic migration specifications
 *
 * Example:
 * ```kotlin
 * @Database(
 *     entities = [User::class, Post::class],
 *     version = 2,
 *     exportSchema = true
 * )
 * @TypeConverters(Converters::class)
 * abstract class AppDatabase : RoomDatabase() {
 *     abstract fun userDao(): UserDao
 *     abstract fun postDao(): PostDao
 * }
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Database(
    val entities: Array<KClass<*>>,
    val views: Array<KClass<*>>,
    val version: Int,
    val exportSchema: Boolean,
    val autoMigrations: Array<AutoMigration>,
)

/**
 * Cross-platform annotation for defining automatic migrations between database versions.
 *
 * This annotation specifies how Room should automatically migrate the database
 * from one version to another. It's used within the @Database annotation.
 *
 * @param from The starting version of the migration
 * @param to The target version of the migration
 * @param spec Optional migration specification class for complex migrations
 *
 * Example:
 * ```kotlin
 * @Database(
 *     entities = [User::class],
 *     version = 3,
 *     autoMigrations = [
 *         AutoMigration(from = 1, to = 2),
 *         AutoMigration(from = 2, to = 3, spec = Migration2to3::class)
 *     ]
 * )
 * abstract class AppDatabase : RoomDatabase()
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(allowedTargets = [])
@Retention(AnnotationRetention.BINARY)
expect annotation class AutoMigration(
    val from: Int,
    val to: Int,
    val spec: KClass<*>,
)

/**
 * Cross-platform annotation for ignoring fields in entities.
 *
 * This annotation marks fields that should not be persisted to the database.
 * Use it for computed properties, transient state, or any data that shouldn't
 * be stored.
 *
 * Example:
 * ```kotlin
 * @Entity
 * data class User(
 *     @PrimaryKey val id: Long,
 *     val firstName: String,
 *     val lastName: String,
 *     @Ignore
 *     val fullName: String = "$firstName $lastName"
 * )
 *
 * @Entity
 * data class Product(
 *     @PrimaryKey val id: Long,
 *     val name: String,
 *     val price: Double,
 *     @Ignore
 *     var isSelected: Boolean = false
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
)
@Retention(AnnotationRetention.BINARY)
expect annotation class Ignore()

/**
 * Cross-platform annotation for defining database views.
 *
 * This annotation creates a read-only database view based on a SQL query.
 * Views are useful for creating reusable query results that can be treated
 * like entities but are computed from other tables.
 *
 * @param value The SQL SELECT query that defines the view
 * @param viewName Custom name for the database view
 *
 * Example:
 * ```kotlin
 * @DatabaseView(
 *     "SELECT user.id, user.name, COUNT(post.id) as postCount " +
 *     "FROM users as user LEFT JOIN posts as post ON user.id = post.userId " +
 *     "GROUP BY user.id",
 *     viewName = "user_summary"
 * )
 * data class UserSummary(
 *     val id: Long,
 *     val name: String,
 *     val postCount: Int
 * )
 * ```
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class DatabaseView(
    val value: String,
    val viewName: String,
)

/**
 * Cross-platform constants for handling database conflicts during insert operations.
 *
 * These constants define the behavior when inserting data that conflicts with
 * existing constraints (such as primary key or unique constraints).
 *
 * Example usage:
 * ```kotlin
 * @Insert(onConflict = OnConflictStrategy.REPLACE)
 * suspend fun insertUser(user: User)
 *
 * @Insert(onConflict = OnConflictStrategy.IGNORE)
 * suspend fun insertUserIfNotExists(user: User)
 * ```
 */
object OnConflictStrategy {
    /** No conflict resolution strategy specified (may cause exceptions) */
    const val NONE = 0

    /** Replace the existing data with the new data when conflicts occur */
    const val REPLACE = 1

    /** Rollback the transaction when conflicts occur */
    const val ROLLBACK = 2

    /** Abort the current operation when conflicts occur */
    const val ABORT = 3

    /** Fail the operation and throw an exception when conflicts occur */
    const val FAIL = 4

    /** Ignore the new data when conflicts occur (keep existing data) */
    const val IGNORE = 5
}

/**
 * Cross-platform constants for ColumnInfo type affinity.
 *
 * These constants define the storage class hint for a column in SQLite.
 * Type affinity determines how data is stored and compared in the column.
 */
object ColumnInfoTypeAffinity {
    /** Default type affinity (Room will infer from Kotlin type) */
    const val UNDEFINED = 1

    /** Store as TEXT */
    const val TEXT = 2

    /** Store as INTEGER */
    const val INTEGER = 3

    /** Store as REAL (floating point) */
    const val REAL = 4

    /** Store as BLOB (binary data) */
    const val BLOB = 5

    /** Indicates that the column name should be inherited from the field name */
    const val INHERIT_FIELD_NAME: String = "[field-name]"

    /** Indicates that no default value is specified for the column */
    const val VALUE_UNSPECIFIED: String = "[value-unspecified]"
}

/**
 * Cross-platform constants for ColumnInfo collation sequences.
 *
 * These constants define how text columns are compared and sorted.
 */
object CollationSequence {
    /** Default collation (depends on database configuration) */
    const val UNSPECIFIED = 1

    /** Binary comparison (case-sensitive) */
    const val BINARY = 2

    /** Case-insensitive comparison */
    const val NOCASE = 3

    /** Ignore trailing spaces */
    const val RTRIM = 4

    /** Locale-sensitive comparison */
    const val LOCALIZED = 5

    /** Unicode-aware comparison */
    const val UNICODE = 6
}

/**
 * Cross-platform constants for foreign key actions.
 *
 * These constants define the action to take when a referenced key is updated or deleted.
 */
object ForeignKeyAction {
    /** Take no action when a referenced key changes */
    const val NO_ACTION = 1

    /** Prevent the operation if it would violate the foreign key constraint */
    const val RESTRICT = 2

    /** Set the foreign key column to NULL when the referenced key is deleted/updated */
    const val SET_NULL = 3

    /** Set the foreign key column to its default value when the referenced key is deleted/updated */
    const val SET_DEFAULT = 4

    /** Cascade the delete/update operation to the referencing rows */
    const val CASCADE = 5
}

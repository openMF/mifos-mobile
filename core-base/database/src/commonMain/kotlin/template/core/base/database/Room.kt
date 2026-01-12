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

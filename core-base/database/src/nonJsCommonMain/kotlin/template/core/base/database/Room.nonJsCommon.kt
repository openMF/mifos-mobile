/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.database

import androidx.room.AutoMigration
import androidx.room.BuiltInTypeConverters
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.DatabaseView
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import androidx.room.Upsert

/**
 * Multiplatform typealiases for Room database annotations and interfaces.
 *
 * This file provides `actual` typealiases for common Room annotations and interfaces, allowing
 * shared code to use Room database features in a platform-agnostic way. These typealiases map
 * to the corresponding androidx.room components, enabling code sharing across all non-JS platforms
 * (Android, Desktop, iOS/Native) in a Kotlin Multiplatform project.
 *
 * @see <a href="https://developer.android.com/training/data-storage/room">Room Persistence Library</a>
 */

/**
 * Typealias for the Room `@Dao` annotation/interface.
 * Used to mark Data Access Objects in shared code.
 */
actual typealias Dao = Dao

/**
 * Typealias for the Room `@Query` annotation.
 * Used to annotate methods in DAOs for SQL queries.
 */
actual typealias Query = Query

/**
 * Typealias for the Room `@Insert` annotation.
 * Used to annotate methods in DAOs for insert operations.
 */
actual typealias Insert = Insert

/**
 * Typealias for the Room `@Update` annotation.
 * Used to annotate methods in DAOs for update operations.
 */
actual typealias Update = Update

/**
 * Typealias for the Room `@Delete` annotation.
 * Used to annotate methods in DAOs for delete operations.
 */
actual typealias Delete = Delete

/**
 * Typealias for the Room `@Upsert` annotation.
 * Used to annotate methods in DAOs for insert or update operations.
 */
actual typealias Upsert = Upsert

/**
 * Typealias for the Room `@Transaction` annotation.
 * Used to annotate methods that should run in a database transaction.
 */
actual typealias Transaction = Transaction

/**
 * Typealias for the Room `@PrimaryKey` annotation.
 * Used to mark primary key fields in entities.
 */
actual typealias PrimaryKey = PrimaryKey

/**
 * Typealias for the Room `@ForeignKey` annotation.
 * Used to define foreign key relationships in entities.
 */
actual typealias ForeignKey = ForeignKey

/**
 * Typealias for the Room `@Index` annotation.
 * Used to define indices on entity fields.
 */
actual typealias Index = Index

/**
 * Typealias for the Room `@Entity` annotation.
 * Used to mark classes as database entities.
 */
actual typealias Entity = Entity

/**
 * Typealias for the Room `@ColumnInfo` annotation.
 * Used to customize column properties in database tables.
 */
actual typealias ColumnInfo = ColumnInfo

/**
 * Typealias for the Room `@Embedded` annotation.
 * Used to embed objects within entities.
 */
actual typealias Embedded = Embedded

/**
 * Typealias for the Room `@Relation` annotation.
 * Used to define relationships between entities.
 */
actual typealias Relation = Relation

/**
 * Typealias for the Room `@Junction` annotation.
 * Used to define junction tables in many-to-many relationships.
 */
actual typealias Junction = Junction

/**
 * Typealias for the Room `@TypeConverter` annotation.
 * Used to mark methods as type converters.
 */
actual typealias TypeConverter = TypeConverter

/**
 * Typealias for the Room `@TypeConverters` annotation.
 * Used to specify which type converters to use.
 */
actual typealias TypeConverters = TypeConverters

/**
 * Typealias for the Room `BuiltInTypeConverters` annotation.
 * Used to configure built-in type converters.
 */
actual typealias BuiltInTypeConverters = BuiltInTypeConverters

/**
 * Typealias for the Room `@Database` annotation.
 * Used to mark a class as a Room database.
 */
actual typealias Database = Database

/**
 * Typealias for the Room `@AutoMigration` annotation.
 * Used to define automatic migrations between database versions.
 */
actual typealias AutoMigration = AutoMigration

/**
 * Typealias for the Room `@Ignore` annotation.
 * Used to ignore fields in entities.
 */
actual typealias Ignore = Ignore

/**
 * Typealias for the Room `@DatabaseView` annotation.
 * Used to define database views.
 */
actual typealias DatabaseView = DatabaseView

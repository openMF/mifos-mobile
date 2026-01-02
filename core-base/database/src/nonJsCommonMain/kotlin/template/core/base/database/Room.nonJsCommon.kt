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

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

/**
 * Multiplatform typealiases for Room database annotations and interfaces.
 *
 * This file provides `actual` typealiases for common Room annotations and interfaces, allowing
 * shared code to use Room database features in a platform-agnostic way. These typealiases map
 * to the corresponding Android Room components, enabling code sharing across platforms in a
 * Kotlin Multiplatform project.
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

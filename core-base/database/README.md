# Core Base Database Module

A comprehensive Kotlin Multiplatform library providing cross-platform database abstractions using the Room persistence library for Android, Desktop, and Native platforms.

## Overview

This module serves as the foundational database layer for Mifos Initiative applications, enabling consistent database operations across Android, Desktop (JVM), and Native (iOS/macOS) platforms. By leveraging the Room persistence library and Kotlin Multiplatform's expect/actual pattern, the module delivers a unified database API that works seamlessly across all supported platforms while respecting platform-specific implementation details.

## Architecture

The module employs Kotlin Multiplatform's expect/actual pattern to provide platform-specific implementations while maintaining a common interface, allowing shared business logic to interact with databases without platform-specific dependencies.

### Common Module (`commonMain`)

The common module defines expect declarations for all Room annotations and helper objects:

- **Room.kt**: Contains expect declarations for 17 Room annotations including DAO, entity, query, transaction, and type conversion annotations
- **Helper Objects**: Provides cross-platform constants for conflict resolution strategies, column type affinities, and collation sequences

### Platform-Specific Modules

#### Non-JS Common Module (`nonJsCommonMain`)

Provides actual implementations for Android, Desktop, and Native platforms through typealiases to androidx.room:

- **Room.nonJsCommon.kt**: Contains actual typealias implementations mapping expect declarations to androidx.room annotations
- Covers all supported platforms except JavaScript/Web targets
- Uses direct typealiasing to ensure feature parity with androidx.room

#### Android Implementation (`androidMain`)

- **AppDatabaseFactory**: Android-specific factory requiring application Context
- Uses `Room.databaseBuilder()` with Android's application context
- Stores databases in standard Android internal storage directory
- Supports all Room features including migrations, type converters, and callbacks

#### Desktop Implementation (`desktopMain`)

- **AppDatabaseFactory**: Desktop-specific factory with platform-aware directory selection
- Creates databases in platform-appropriate directories:
  - **Windows**: `%APPDATA%/MifosDatabase`
  - **macOS**: `~/Library/Application Support/MifosDatabase`
  - **Linux**: `~/.local/share/MifosDatabase`
- Uses inline reified generics for type-safe database instantiation
- Automatically creates storage directories when required

#### Native Implementation (`nativeMain`)

- **AppDatabaseFactory**: iOS/macOS-specific factory using Foundation framework
- Stores databases in iOS/macOS document directory through Kotlin/Native interop
- Integrates with platform file system APIs using NSFileManager
- Supports iCloud backup eligibility and Files app integration

## Room Annotations

The module provides cross-platform support for 17 Room annotations through the expect/actual pattern:

### Data Access Annotations

#### `@Dao`
Marks interfaces as Data Access Objects containing database operation methods.

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
```

#### `@Query`
Defines SQL queries to execute when the annotated method is called.

```kotlin
@Query("SELECT * FROM users WHERE age > :minAge")
suspend fun getUsersOlderThan(minAge: Int): List<User>
```

#### `@Insert`
Marks methods that insert entities into the database with configurable conflict resolution.

```kotlin
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertUser(user: User): Long
```

#### `@Update`
Marks methods that update existing entities based on primary keys with conflict handling.

```kotlin
@Update(onConflict = OnConflictStrategy.REPLACE)
suspend fun updateUser(user: User): Int
```

#### `@Delete`
Marks methods that delete entities from the database matched by primary key.

```kotlin
@Delete
suspend fun deleteUser(user: User): Int
```

#### `@Upsert`
Combines insert and update operations, inserting new entities or updating existing ones.

```kotlin
@Upsert
suspend fun upsertUser(user: User): Long
```

#### `@Transaction`
Ensures all database operations within the method execute as a single atomic transaction.

```kotlin
@Transaction
suspend fun transferFunds(fromAccount: Account, toAccount: Account) {
    updateAccount(fromAccount)
    updateAccount(toAccount)
    insertTransaction(Transaction(...))
}
```

### Entity Annotations

#### `@Entity`
Marks classes as database entities, transforming them into database tables.

```kotlin
@Entity(
    tableName = "user_profiles",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val displayName: String
)
```

#### `@PrimaryKey`
Identifies fields serving as primary keys with optional auto-generation.

```kotlin
@PrimaryKey(autoGenerate = true)
val id: Long = 0
```

#### `@ColumnInfo`
Customizes column properties including names, types, indexes, and collation sequences.

```kotlin
@ColumnInfo(name = "user_name", collate = CollationSequence.NOCASE)
val name: String
```

#### `@Embedded`
Includes all fields from another object directly into the parent entity's table.

```kotlin
@Embedded(prefix = "home_")
val homeAddress: Address
```

#### `@Ignore`
Marks fields that should not be persisted to the database.

```kotlin
@Ignore
val fullName: String = "$firstName $lastName"
```

### Relationship Annotations

#### `@Relation`
Establishes one-to-many or many-to-many relationships between entities.

```kotlin
@Relation(
    parentColumn = "id",
    entityColumn = "userId"
)
val posts: List<Post>
```

#### `@Junction`
Defines junction tables for many-to-many relationships, used within `@Relation`.

```kotlin
@Relation(
    parentColumn = "id",
    entityColumn = "id",
    associateBy = Junction(
        value = UserPlaylistCrossRef::class,
        parentColumn = "userId",
        entityColumn = "playlistId"
    )
)
val playlists: List<Playlist>
```

#### `@ForeignKey`
Defines foreign key constraints ensuring referential integrity between tables.

```kotlin
@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Post(...)
```

#### `@Index`
Creates database indexes to improve query performance on specified columns.

```kotlin
@Entity(indices = [Index(value = ["email"], unique = true)])
data class User(...)
```

### Type Conversion Annotations

#### `@TypeConverter`
Marks methods as type converters enabling Room to store and retrieve complex data types.

```kotlin
object DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
```

#### `@TypeConverters`
Specifies which type converter classes to use at database, entity, or DAO level.

```kotlin
@Database(entities = [User::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase()
```

#### `@BuiltInTypeConverters`
Configures Room's built-in type converters for common types like enums and UUID.

```kotlin
@TypeConverters(
    value = [CustomConverters::class],
    builtInTypeConverters = BuiltInTypeConverters()
)
```

### Database Configuration Annotations

#### `@Database`
Marks a class as a Room database, defining entities, version, and configuration.

```kotlin
@Database(
    entities = [User::class, Post::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

#### `@AutoMigration`
Defines automatic migrations between database versions, used within `@Database`.

```kotlin
@Database(
    entities = [User::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = Migration2to3::class)
    ]
)
abstract class AppDatabase : RoomDatabase()
```

#### `@DatabaseView`
Creates read-only database views based on SQL queries.

```kotlin
@DatabaseView(
    "SELECT user.id, user.name, COUNT(post.id) as postCount " +
    "FROM users as user LEFT JOIN posts as post ON user.id = post.userId " +
    "GROUP BY user.id",
    viewName = "user_summary"
)
data class UserSummary(val id: Long, val name: String, val postCount: Int)
```

## Helper Objects

### OnConflictStrategy

Defines conflict resolution behavior for insert and update operations:

- `NONE` (0): No conflict resolution strategy specified
- `REPLACE` (1): Replace existing data with new data
- `ROLLBACK` (2): Rollback the transaction on conflict
- `ABORT` (3): Abort the current operation on conflict
- `FAIL` (4): Fail the operation and throw exception
- `IGNORE` (5): Ignore new data, keeping existing data

### ColumnInfoTypeAffinity

Defines storage class hints for SQLite columns:

- `UNDEFINED` (1): Default type affinity (Room infers from Kotlin type)
- `TEXT` (2): Store as TEXT
- `INTEGER` (3): Store as INTEGER
- `REAL` (4): Store as REAL (floating point)
- `BLOB` (5): Store as BLOB (binary data)

### CollationSequence

Defines text comparison and sorting behavior:

- `UNSPECIFIED` (1): Default collation (depends on database configuration)
- `BINARY` (2): Binary comparison (case-sensitive)
- `NOCASE` (3): Case-insensitive comparison
- `RTRIM` (4): Ignore trailing spaces
- `LOCALIZED` (5): Locale-sensitive comparison
- `UNICODE` (6): Unicode-aware comparison

## Usage Examples

### Basic Database Setup

#### Android

```kotlin
class MyApplication : Application() {
    private val databaseFactory = AppDatabaseFactory(this)

    val database: AppDatabase by lazy {
        databaseFactory
            .createDatabase(AppDatabase::class.java, "app_database.db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .addTypeConverter(DateConverters())
            .build()
    }
}
```

#### Desktop

```kotlin
class DesktopApplication {
    private val databaseFactory = AppDatabaseFactory()

    val database: AppDatabase by lazy {
        databaseFactory
            .createDatabase<AppDatabase>("app_database.db")
            .enableMultiInstanceInvalidation()
            .build()
    }
}
```

#### Native (iOS/macOS)

```kotlin
class IOSApplication {
    private val databaseFactory = AppDatabaseFactory()

    val database: AppDatabase by lazy {
        databaseFactory
            .createDatabase<AppDatabase>("app_database.db")
            .setJournalMode(RoomDatabase.JournalMode.WAL)
            .build()
    }
}
```

### Defining Entities with Relationships

```kotlin
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_name", collate = CollationSequence.NOCASE)
    val name: String,
    val email: String,
    @Embedded(prefix = "address_")
    val address: Address,
    @Ignore
    val fullName: String = name
)

@Entity(
    tableName = "posts",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"])]
)
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val content: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String
)
```

### Creating Comprehensive DAOs

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY user_name")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User): Int

    @Delete
    suspend fun deleteUser(user: User): Int

    @Upsert
    suspend fun upsertUsers(users: List<User>): List<Long>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithPosts(userId: Long): UserWithPosts
}
```

### Implementing Type Converters

```kotlin
object DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

object ListConverters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.filter { it.isNotEmpty() }
    }
}
```

### Database Definition with Migrations

```kotlin
@Database(
    entities = [User::class, Post::class],
    views = [UserSummary::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = Migration2to3::class)
    ]
)
@TypeConverters(DateConverters::class, ListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}
```

## Dependencies

The module relies on the following dependencies:

- **androidx.room.runtime** (2.8.4+): Core Room database functionality
- **Kotlin Multiplatform**: Cross-platform code sharing infrastructure
- **Platform APIs**:
  - Android: Context for database creation
  - Desktop: File system APIs for storage management
  - Native: Foundation framework (NSFileManager, NSDocumentDirectory)

## Gradle Configuration

```kotlin
kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }
        desktopMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }
        nativeMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }
    }
}
```

## Platform Considerations

### Android

- Requires minimum API level compatible with Room (API 16+)
- Database files stored in internal app storage, inaccessible to other apps
- Supports full Room functionality including migrations, type converters, and callbacks
- Automatic application context usage prevents memory leaks
- Integration with Android Architecture Components (LiveData, Flow)

### Desktop

- Cross-platform directory selection ensures appropriate database placement
- Supports full Room functionality on JVM platforms
- Automatic directory creation with proper permissions
- Platform-specific storage conventions:
  - Windows follows APPDATA guidelines
  - macOS uses Application Support directory
  - Linux adheres to XDG Base Directory specification

### Native (iOS/macOS)

- Uses iOS/macOS document directory for database storage
- Leverages Kotlin/Native C interop for platform APIs
- Database files eligible for iCloud backup by default
- Complies with App Store Review Guidelines for data storage
- Accessible through Files app when configured appropriately
- Requires proper entitlements for file system access

## Technical Notes

### Expect/Actual Pattern

The module uses Kotlin Multiplatform's expect/actual pattern to provide platform-agnostic database APIs. All annotations are defined as expect declarations in commonMain and implemented as actual typealiases in nonJsCommonMain, mapping directly to androidx.room annotations.

### Default Parameter Values

The expect annotation declarations do not include default parameter values due to Kotlin/Native compiler limitations with expect/actual + typealias combinations. However, default values from androidx.room remain effective through the actual typealias implementations, ensuring consistent behavior across platforms without requiring explicit parameter specification.

### Type Safety

All annotations maintain full type safety through proper use of KClass, Array, and primitive types. The typealias approach ensures compile-time verification and IDE support across all platforms.

## Best Practices

### Database Design

1. **Version Management**: Always increment version numbers when modifying schema
2. **Migration Strategy**: Implement proper Room migrations for schema changes to prevent data loss
3. **Type Converters**: Use `@TypeConverter` for complex data types not natively supported by SQLite
4. **Conflict Resolution**: Choose appropriate `OnConflictStrategy` based on business requirements
5. **Indexing**: Create indexes on frequently queried columns to improve performance
6. **Foreign Keys**: Define foreign key constraints to maintain referential integrity

### Performance Optimization

1. **Transaction Management**: Wrap bulk operations in `@Transaction` methods for atomicity and performance
2. **Query Optimization**: Use appropriate indexes and avoid SELECT * when possible
3. **Batch Operations**: Prefer batch insert/update methods over individual operations
4. **Lazy Loading**: Consider using Paging library for large result sets
5. **Journal Mode**: Use WAL (Write-Ahead Logging) mode for improved concurrent access

### Testing

1. **Platform Coverage**: Test database operations on all target platforms
2. **Migration Testing**: Verify migrations work correctly between all version combinations
3. **Concurrent Access**: Test multi-threaded database access scenarios
4. **Type Converter Testing**: Validate type converters handle edge cases correctly
5. **Integration Testing**: Test complete DAO operations with real database instances

## Contributing

When contributing to this module:

- Maintain consistency with the expect/actual pattern
- Test changes across all supported platforms (Android, Desktop, iOS/macOS)
- Update documentation for any API changes or new features
- Follow Kotlin coding conventions and project style guidelines
- Ensure proper license headers on all source files
- Add comprehensive KDoc comments for new annotations or classes

## License

This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

See https://github.com/openMF/kmp-project-template/blob/main/LICENSE for complete license details.

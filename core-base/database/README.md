# Core Base Database Module

A Kotlin Multiplatform library that provides cross-platform database abstractions using Room
database for Android, Desktop, and Native platforms.

## Overview

This module serves as a foundational database layer for the Mifos Initiative applications, enabling
consistent database operations across Android, Desktop (JVM), and Native (iOS/macOS) platforms using
the Room persistence library.

## Architecture

The module follows the Kotlin Multiplatform expect/actual pattern to provide platform-specific
implementations while maintaining a common interface:

### Common Module (`commonMain`)

- **Room.kt**: Defines expect declarations for Room annotations (`@Dao`, `@Entity`, `@Query`, etc.)
- **TypeConverter.kt**: Defines expect declaration for `@TypeConverter` annotation
- **OnConflictStrategy**: Platform-agnostic constants for database conflict resolution

### Platform-Specific Modules

- **Android (`androidMain`)**: Uses androidx.room directly with Android Context
- **Desktop (`desktopMain`)**: Uses androidx.room with file-based database storage
- **Native (`nativeMain`)**: Uses androidx.room with iOS/macOS document directory storage

## Key Components

### AppDatabaseFactory

Platform-specific factory classes that handle database creation and configuration:

#### Android Implementation

- Requires Android `Context` for database creation
- Uses `Room.databaseBuilder()` with application context
- Stores databases in standard Android app data directory

#### Desktop Implementation

- Creates databases in platform-appropriate directories:
    - **Windows**: `%APPDATA%/MifosDatabase`
    - **macOS**: `~/Library/Application Support/MifosDatabase`
    - **Linux**: `~/.local/share/MifosDatabase`
- Uses inline reified generics for type-safe database instantiation

#### Native Implementation

- Stores databases in iOS/macOS document directory
- Uses platform-specific file system APIs
- Leverages Kotlin/Native interop for Foundation framework access

### Room Annotations

Cross-platform type aliases for Room annotations that ensure consistent API across all platforms:

- `@Dao` - Data Access Object annotation
- `@Entity` - Database entity annotation
- `@Query` - SQL query annotation
- `@Insert` - Insert operation annotation
- `@PrimaryKey` - Primary key annotation
- `@ForeignKey` - Foreign key constraint annotation
- `@Index` - Database index annotation
- `@TypeConverter` - Type conversion annotation

## Usage Examples

### Basic Setup

#### Android

```kotlin
class MyApplication : Application() {
    val databaseFactory = AppDatabaseFactory(this)

    val database = databaseFactory
        .createDatabase(MyDatabase::class.java, "my_database.db")
        .build()
}
```

#### Desktop

```kotlin
class DesktopApp {
    val databaseFactory = AppDatabaseFactory()

    val database = databaseFactory
        .createDatabase<MyDatabase>("my_database.db")
        .build()
}
```

#### Native (iOS/macOS)

```kotlin
class IOSApp {
    val databaseFactory = AppDatabaseFactory()

    val database = databaseFactory
        .createDatabase<MyDatabase>("my_database.db")
        .build()
}
```

### Defining Entities

```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String
)
```

### Creating DAOs

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Long)
}
```

### Database Definition

```kotlin
@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

## Dependencies

The module relies on the following key dependencies:

- **androidx.room.runtime**: Core Room database functionality
- **Kotlin Multiplatform**: Cross-platform code sharing
- **Platform-specific APIs**: Context (Android), File system (Desktop), Foundation (Native)

## Configuration

### Gradle Setup

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

- Requires minimum API level compatible with Room
- Database files stored in internal app storage
- Supports all Room features including migrations and type converters

### Desktop

- Cross-platform directory selection ensures proper database placement
- Supports full Room functionality on JVM
- Automatic directory creation for database storage

### Native (iOS/macOS)

- Uses iOS/macOS document directory for database storage
- Leverages Kotlin/Native C interop for platform APIs
- Requires iOS/macOS specific Room dependencies

## Best Practices

1. **Database Versioning**: Always increment version numbers when changing schema
2. **Migration Strategy**: Implement proper Room migrations for schema changes
3. **Type Converters**: Use `@TypeConverter` for complex data types
4. **Conflict Resolution**: Choose appropriate `OnConflictStrategy` for your use case
5. **Testing**: Test database operations on all target platforms

## Contributing

When contributing to this module:

- Maintain expect/actual pattern consistency
- Test changes across all supported platforms
- Update documentation for any API changes
- Follow Kotlin coding conventions
- Ensure proper license headers on all files

## License

This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

See https://github.com/openMF/kmp-project-template/blob/main/LICENSE for more details.
# Client Layer Guide

> **Location**: `core/` (Network + Data)
> **Command**: `/client [Feature]`

---

## Overview

The client layer contains two sub-layers that handle all client-side logic:

```
┌─────────────────────────────────────────────────────────────────────┐
│  CLIENT LAYER (core/)                                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  core/network/        → API Services (Ktorfit), DTOs, DataManager   │
│       │                                                              │
│       ▼                                                              │
│  core/data/           → Repositories, Data transformations          │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Directory Structure

```
core/
├── network/                    # API Layer
│   └── src/commonMain/kotlin/.../network/
│       ├── services/
│       │   ├── AuthenticationService.kt
│       │   ├── BeneficiaryService.kt
│       │   ├── ClientService.kt
│       │   ├── LoanAccountsListService.kt
│       │   ├── SavingAccountsListService.kt
│       │   └── ...
│       ├── model/
│       │   └── [Domain]Entity.kt          # DTOs
│       ├── DataManager.kt                 # Service accessor
│       ├── KtorfitClient.kt               # Ktorfit setup
│       └── di/
│           └── NetworkModule.kt           # Koin registration
│
├── data/                       # Data Layer
│   └── src/commonMain/kotlin/.../data/
│       ├── repository/
│       │   └── [Feature]Repository.kt     # Interface
│       ├── repositoryImpl/
│       │   └── [Feature]RepositoryImp.kt  # Implementation
│       └── di/
│           └── RepositoryModule.kt        # Koin registration
│
├── model/                      # Domain Models (shared)
├── database/                   # Local database (Room)
├── datastore/                  # Preferences (UserPreferencesRepository)
├── designsystem/               # Design tokens, theme
├── ui/                         # Shared UI components
└── common/                     # Utilities, dispatchers
```

---

## Implementation Flow

```
/client [Feature]
    │
    ├── 1. Network Layer
    │   ├── Create [Feature]Service.kt (Ktorfit interface)
    │   ├── Add to KtorfitClient.kt
    │   ├── Add to DataManager.kt
    │   └── DTOs in model/ package
    │
    └── 2. Data Layer
        ├── Create [Feature]Repository interface
        ├── Create [Feature]RepositoryImp implementation
        └── Register in RepositoryModule.kt
```

---

## Code Patterns

### Service (Ktorfit Interface)

```kotlin
// core/network/services/[Feature]Service.kt
interface [Feature]Service {

    @GET("self/[endpoint]")
    suspend fun getData(): [Feature]Entity

    @POST("self/[endpoint]")
    suspend fun createData(@Body payload: [Request]Entity): ResponseBody
}
```

### DataManager

```kotlin
// core/network/DataManager.kt
class DataManager(
    private val ktorfitClient: KtorfitClient,
) {
    val [feature]Api by lazy { ktorfitClient.[feature]Api }
}
```

### Repository Interface

```kotlin
// core/data/repository/[Feature]Repository.kt
interface [Feature]Repository {
    suspend fun getData(): [DomainModel]
    suspend fun createData(param: Param): Result
}
```

### Repository Implementation

```kotlin
// core/data/repositoryImpl/[Feature]RepositoryImp.kt
class [Feature]RepositoryImp(
    private val dataManager: DataManager,
    @Named(MifosDispatchers.IO.name) private val ioDispatcher: CoroutineDispatcher,
) : [Feature]Repository {

    override suspend fun getData(): [DomainModel] = withContext(ioDispatcher) {
        dataManager.[feature]Api.getData().toDomainModel()
    }
}
```

### Koin Registration

```kotlin
// core/data/di/RepositoryModule.kt
val RepositoryModule = module {
    single<[Feature]Repository> {
        [Feature]RepositoryImp(get(), get(ioDispatcher))
    }
}
```

---

## Critical Rules

```
⚠️ Repository ALWAYS uses DataManager, never raw Ktorfit services!

✅ CORRECT: Repository → DataManager → Service
❌ WRONG:   Repository → Service directly
```

---

## Build Commands

```bash
./gradlew :core:network:build
./gradlew :core:data:build
./gradlew :core:model:build
```

---

## Related Files

- Patterns: `claude-product-cycle/design-spec-layer/_shared/PATTERNS.md`
- API Specs: `claude-product-cycle/design-spec-layer/features/[feature]/API.md`

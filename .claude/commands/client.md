# /client - Client Layer Implementation

## Purpose
Implement the client layer (Network + Data) for a feature. This includes DTOs, Services, and Repositories.

---

## Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                    /client [Feature] WORKFLOW                      │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  STEP 1: READ SPEC                                                │
│  ├─→ Read features/[feature]/SPEC.md                              │
│  ├─→ Read features/[feature]/API.md                               │
│  └─→ Read server-layer/FINERACT_API.md                            │
│                                                                    │
│  STEP 2: CHECK EXISTING CODE                                      │
│  ├─→ Check core/network/services/ for existing service            │
│  ├─→ Check core/data/repository/ for existing repository          │
│  └─→ Identify what needs to be created/updated                    │
│                                                                    │
│  STEP 3: NETWORK LAYER                                            │
│  ├─→ Create/update DTOs in core/network/model/ (if needed)        │
│  ├─→ Create/update Service interface in core/network/services/    │
│  └─→ Register in NetworkModule                                    │
│                                                                    │
│  STEP 4: DATA LAYER                                               │
│  ├─→ Create/update Repository interface                           │
│  ├─→ Create/update RepositoryImpl                                  │
│  └─→ Register in DataModule                                       │
│                                                                    │
│  STEP 5: BUILD & VERIFY                                           │
│  ├─→ ./gradlew :core:network:build                                │
│  ├─→ ./gradlew :core:data:build                                   │
│  └─→ ./gradlew spotlessApply                                      │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## File Locations

| Component | Location |
|-----------|----------|
| DTOs | `core/network/model/` |
| Service Interface | `core/network/services/` |
| Repository Interface | `core/data/repository/` |
| Repository Impl | `core/data/repositoryImpl/` |
| Network DI | `core/network/di/NetworkModule.kt` |
| Data DI | `core/data/di/DataModule.kt` |

---

## Service Pattern

```kotlin
// core/network/services/[Feature]Service.kt
interface [Feature]Service {

    @GET(ApiEndPoints.[ENDPOINT])
    fun getData(): Flow<DataType>

    @GET(ApiEndPoints.[ENDPOINT] + "/{id}")
    fun getById(@Path("id") id: Long): Flow<DataType>

    @POST(ApiEndPoints.[ENDPOINT])
    suspend fun create(@Body payload: PayloadType): HttpResponse

    @PUT(ApiEndPoints.[ENDPOINT] + "/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body payload: PayloadType,
    ): HttpResponse

    @DELETE(ApiEndPoints.[ENDPOINT] + "/{id}")
    suspend fun delete(@Path("id") id: Long): HttpResponse
}
```

---

## Repository Pattern

```kotlin
// core/data/repository/[Feature]Repository.kt
interface [Feature]Repository {
    fun getData(): Flow<DataState<List<Data>>>
    fun getById(id: Long): Flow<DataState<Data>>
    suspend fun create(data: Data): DataState<Unit>
    suspend fun update(id: Long, data: Data): DataState<Unit>
    suspend fun delete(id: Long): DataState<Unit>
}

// core/data/repositoryImpl/[Feature]RepositoryImpl.kt
class [Feature]RepositoryImpl(
    private val service: [Feature]Service,
) : [Feature]Repository {

    override fun getData(): Flow<DataState<List<Data>>> = flow {
        emit(DataState.Loading)
        try {
            val result = service.getData().first()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }
}
```

---

## DI Registration

```kotlin
// core/network/di/NetworkModule.kt
val networkModule = module {
    single<[Feature]Service> { get<Ktorfit>().create<[Feature]Service>() }
}

// core/data/di/DataModule.kt
val dataModule = module {
    single<[Feature]Repository> { [Feature]RepositoryImpl(get()) }
}
```

---

## Output Template

```
┌──────────────────────────────────────────────────────────────────────┐
│  ✅ CLIENT LAYER COMPLETE                                            │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  Created/Updated:                                                     │
│  ├─ core/network/services/[Feature]Service.kt                        │
│  ├─ core/data/repository/[Feature]Repository.kt                      │
│  └─ core/data/repositoryImpl/[Feature]RepositoryImpl.kt              │
│                                                                       │
│  Registered in DI:                                                    │
│  ├─ NetworkModule: [Feature]Service ✅                                │
│  └─ DataModule: [Feature]Repository ✅                                │
│                                                                       │
│  🔨 BUILD: :core:network ✅ :core:data ✅                             │
│  🧹 LINT: spotlessApply ✅                                            │
│                                                                       │
├──────────────────────────────────────────────────────────────────────┤
│  NEXT STEP:                                                           │
│  Run:  /feature [Feature]                                            │
└──────────────────────────────────────────────────────────────────────┘
```

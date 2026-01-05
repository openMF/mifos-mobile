# Client Layer Implementation Patterns

> **Purpose**: Service and Repository implementation patterns for client layer
> **Derived from**: Design layer API.md files
> **Location**: `core/network/` (services), `core/data/` (repositories)

---

## Table of Contents
1. [Overview](#overview)
2. [Service Interface Pattern](#service-interface-pattern)
3. [Repository Interface Pattern](#repository-interface-pattern)
4. [Repository Implementation Pattern](#repository-implementation-pattern)
5. [DTO Patterns](#dto-patterns)
6. [DataState Pattern](#datastate-pattern)
7. [Flow vs Suspend Guidelines](#flow-vs-suspend-guidelines)
8. [Complete Examples](#complete-examples)

---

## Overview

```
┌─────────────────────────────────────────────────────────────────┐
│  SERVICE (core/network/services/)                               │
│  └─→ Ktorfit interface with HTTP annotations                   │
│  └─→ Returns Flow<T> or HttpResponse                           │
├─────────────────────────────────────────────────────────────────┤
│  REPOSITORY INTERFACE (core/data/repository/)                   │
│  └─→ Defines contract for data operations                      │
│  └─→ Returns Flow<DataState<T>> or DataState<T>                │
├─────────────────────────────────────────────────────────────────┤
│  REPOSITORY IMPLEMENTATION (core/data/repositoryImpl/)          │
│  └─→ Implements repository interface                           │
│  └─→ Handles error mapping and dispatcher context              │
└─────────────────────────────────────────────────────────────────┘
```

---

## Service Interface Pattern

### Location
`core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/services/`

### Template

```kotlin
package org.mifos.mobile.core.network.services

import de.jensklingenberg.ktorfit.http.*
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface [Feature]Service {

    // GET - List (returns typed Flow)
    @GET(ApiEndPoints.[FEATURE])
    fun get[Feature]List(): Flow<List<[Entity]>>

    // GET - Single item (returns typed Flow)
    @GET(ApiEndPoints.[FEATURE] + "/{id}")
    fun get[Feature]Details(
        @Path("id") id: Long,
    ): Flow<[Entity]>

    // GET - With query params
    @GET(ApiEndPoints.[FEATURE] + "/{id}")
    fun get[Feature]WithAssociations(
        @Path("id") id: Long,
        @Query("associations") associations: String?,
    ): Flow<[EntityWithAssociations]>

    // GET - Template
    @GET(ApiEndPoints.[FEATURE] + "/template")
    fun get[Feature]Template(): Flow<[Feature]Template>

    // POST - Create (returns HttpResponse for error handling)
    @POST(ApiEndPoints.[FEATURE])
    suspend fun create[Feature](
        @Body payload: [Feature]Payload?,
    ): HttpResponse

    // PUT - Update
    @PUT(ApiEndPoints.[FEATURE] + "/{id}")
    suspend fun update[Feature](
        @Path("id") id: Long,
        @Body payload: [Feature]UpdatePayload?,
    ): HttpResponse

    // DELETE - Remove
    @DELETE(ApiEndPoints.[FEATURE] + "/{id}")
    suspend fun delete[Feature](
        @Path("id") id: Long,
    ): HttpResponse

    // POST - With command
    @POST(ApiEndPoints.[FEATURE] + "/{id}?command=action")
    suspend fun [feature]Action(
        @Path("id") id: Long,
        @Body payload: [Action]Payload?,
    ): HttpResponse
}
```

### Key Rules

| Annotation | Usage | Return Type |
|------------|-------|-------------|
| `@GET` | Read operations | `Flow<T>` |
| `@POST` | Create operations | `suspend HttpResponse` |
| `@PUT` | Update operations | `suspend HttpResponse` |
| `@DELETE` | Delete operations | `suspend HttpResponse` |
| `@Path` | URL path variable | N/A |
| `@Query` | Query parameter | N/A |
| `@Body` | Request body | N/A |

### When to Use Flow vs HttpResponse

| Scenario | Return Type | Reason |
|----------|-------------|--------|
| GET typed response | `Flow<T>` | Direct deserialization |
| POST/PUT/DELETE | `suspend HttpResponse` | Need to handle errors manually |
| GET with error handling | `suspend HttpResponse` | Custom error extraction |

---

## Repository Interface Pattern

### Location
`core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/`

### Template

```kotlin
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.common.DataState

interface [Feature]Repository {

    // GET - List
    fun get[Feature]List(): Flow<DataState<List<[Entity]>>>

    // GET - Single
    fun get[Feature]Details(id: Long): Flow<DataState<[Entity]>>

    // GET - Template
    fun get[Feature]Template(): Flow<DataState<[Feature]Template>>

    // POST - Create
    suspend fun create[Feature](payload: [Feature]Payload?): DataState<String>

    // PUT - Update
    suspend fun update[Feature](
        id: Long?,
        payload: [Feature]UpdatePayload?,
    ): DataState<String>

    // DELETE - Remove
    suspend fun delete[Feature](id: Long?): DataState<String>
}
```

### Key Rules

| Operation | Return Type | Pattern |
|-----------|-------------|---------|
| Read (list/single) | `Flow<DataState<T>>` | Streaming updates |
| Create/Update/Delete | `DataState<T>` | One-shot operation |
| Template fetching | `Flow<DataState<T>>` | May update |

---

## Repository Implementation Pattern

### Location
`core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repositoryImpl/`

### Template

```kotlin
package org.mifos.mobile.core.data.repositoryImpl

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.network.DataManager
import java.io.IOException

class [Feature]RepositoryImpl(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : [Feature]Repository {

    // Pattern 1: Flow-based GET (for lists and details)
    override fun get[Feature]List(): Flow<DataState<List<[Entity]>>> = flow {
        try {
            dataManager.[feature]Api.get[Feature]List()
                .collect { response ->
                    emit(DataState.Success(response))
                }
        } catch (e: Exception) {
            emit(DataState.Error(e, null))
        }
    }.flowOn(ioDispatcher)

    // Pattern 2: Alternative Flow with map/catch
    override fun get[Feature]Details(id: Long): Flow<DataState<[Entity]>> {
        return dataManager.[feature]Api.get[Feature]Details(id)
            .map { DataState.Success(it) }
            .catch { emit(DataState.Error(it, null)) }
            .flowOn(ioDispatcher)
    }

    // Pattern 3: Suspend-based POST/PUT/DELETE with error handling
    override suspend fun create[Feature](payload: [Feature]Payload?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.[feature]Api.create[Feature](payload)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(
                    Exception("Network error: ${e.message ?: "Please check your connection"}"),
                    null
                )
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    // Pattern 4: Update with path parameter
    override suspend fun update[Feature](
        id: Long?,
        payload: [Feature]UpdatePayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.[feature]Api.update[Feature](id!!, payload)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(
                    Exception("Network error: ${e.message ?: "Please check your connection"}"),
                    null
                )
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    // Pattern 5: Delete operation
    override suspend fun delete[Feature](id: Long?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.[feature]Api.delete[Feature](id!!)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(
                    Exception("Network error: ${e.message ?: "Please check your connection"}"),
                    null
                )
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }
}
```

---

## DTO Patterns

### Location
`core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/entity/`

### Response DTO (Entity)

```kotlin
@Serializable
@Parcelize
data class [Entity](
    val id: Long? = null,
    val name: String? = null,
    val status: Status? = null,
    val createdDate: List<Int>? = null,
    // All fields nullable with defaults
) : Parcelable
```

### Request DTO (Payload)

```kotlin
@Serializable
@Parcelize
data class [Feature]Payload(
    val locale: String? = null,
    val dateFormat: String? = null,
    val name: String? = null,
    val amount: Double? = null,
    // Fields matching API request body
) : Parcelable
```

### Update DTO

```kotlin
@Serializable
data class [Feature]UpdatePayload(
    val name: String? = null,
    val amount: Int = 0,
    // Only updatable fields
)
```

### Template DTO

```kotlin
@Serializable
@Parcelize
data class [Feature]Template(
    val options: List<Option>? = null,
    val defaultValue: String? = null,
) : Parcelable
```

### DTO Rules

| Rule | Example |
|------|---------|
| All fields nullable | `val name: String? = null` |
| Use default values | `val amount: Double = 0.0` |
| Add `@Serializable` | For JSON parsing |
| Add `@Parcelize` | For Android state saving |
| Implement `Parcelable` | For navigation args |

---

## DataState Pattern

### Definition

```kotlin
sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable, val message: String?) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
}
```

### Usage in ViewModel

```kotlin
class [Feature]ViewModel(
    private val repository: [Feature]Repository,
) : ViewModel() {

    private val _state = MutableStateFlow([Feature]State())
    val state: StateFlow<[Feature]State> = _state.asStateFlow()

    fun load[Feature]() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            repository.get[Feature]List().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                items = result.data,
                            )
                        }
                    }
                    is DataState.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message,
                            )
                        }
                    }
                    is DataState.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
}
```

---

## Flow vs Suspend Guidelines

### Use Flow When

| Scenario | Example |
|----------|---------|
| Streaming data | `fun observeItems(): Flow<DataState<List<T>>>` |
| Real-time updates | WebSocket connections |
| List operations | `fun getList(): Flow<DataState<List<T>>>` |
| May emit multiple values | Pagination |

### Use Suspend When

| Scenario | Example |
|----------|---------|
| One-shot operations | `suspend fun create(): DataState<T>` |
| POST/PUT/DELETE | Write operations |
| Single response | `suspend fun getOnce(): DataState<T>` |
| Error handling needed | HTTP response inspection |

---

## Complete Examples

### Beneficiary Service

```kotlin
interface BeneficiaryService {
    @GET(ApiEndPoints.BENEFICIARIES + "/tpt")
    fun beneficiaryList(): Flow<List<Beneficiary>>

    @GET(ApiEndPoints.BENEFICIARIES + "/tpt/template")
    fun beneficiaryTemplate(): Flow<BeneficiaryTemplate>

    @POST(ApiEndPoints.BENEFICIARIES + "/tpt")
    suspend fun createBeneficiary(@Body payload: BeneficiaryPayload?): HttpResponse

    @PUT(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun updateBeneficiary(
        @Path("beneficiaryId") beneficiaryId: Long,
        @Body payload: BeneficiaryUpdatePayload?,
    ): HttpResponse

    @DELETE(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun deleteBeneficiary(@Path("beneficiaryId") beneficiaryId: Long): HttpResponse
}
```

### Beneficiary Repository Interface

```kotlin
interface BeneficiaryRepository {
    fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>>
    suspend fun createBeneficiary(payload: BeneficiaryPayload?): DataState<String>
    suspend fun updateBeneficiary(id: Long?, payload: BeneficiaryUpdatePayload?): DataState<String>
    suspend fun deleteBeneficiary(id: Long?): DataState<String>
    fun beneficiaryList(): Flow<DataState<List<Beneficiary>>>
}
```

---

## Koin Module Registration

### Location
`feature/[name]/di/[Feature]Module.kt`

```kotlin
val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

### DataManager Access

```kotlin
// DataManager provides access to all services
class DataManager(
    val authApi: AuthenticationService,
    val beneficiaryApi: BeneficiaryService,
    val clientApi: ClientService,
    val savingsApi: SavingAccountsListService,
    // ... other services
)
```

---

## Related Files

- Error Handling: [ERROR_HANDLING.md](ERROR_HANDLING.md)
- API Reference: [API_REFERENCE.md](API_REFERENCE.md)
- API Index: [API_INDEX.md](API_INDEX.md)
- Design Layer API.md: `design-spec-layer/features/*/API.md`

---

## Changelog

| Date | Change |
|------|--------|
| 2025-01-05 | Created with patterns from codebase analysis |

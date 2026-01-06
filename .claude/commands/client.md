# /client - Client Layer Implementation

## Purpose

Implement the client layer (Network + Data) using O(1) lookup and pattern detection. Creates Services, Repositories, and DI registration with code matching existing codebase conventions.

---

## Command Variants

```
/client                        # Show client layer status
/client [Feature]              # Implement client layer for feature
/client [Feature] --network    # Network layer only (Service)
/client [Feature] --data       # Data layer only (Repository)
```

---

## Workflow with O(1) Optimization

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  /client [Feature] - O(1) OPTIMIZED WORKFLOW                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  PHASE 0: O(1) CONTEXT LOADING                                              │
│  ├─→ Read FEATURE_MAP.md              → Check if service/repo exist         │
│  ├─→ Read API_INDEX.md                → Get endpoint definitions            │
│  ├─→ Read features/[name]/API.md      → Get feature-specific endpoints      │
│  └─→ Read features/[name]/SPEC.md     → Get data requirements               │
│                                                                              │
│  PHASE 1: PATTERN DETECTION                                                 │
│  ├─→ Read existing Service            → Extract interface pattern           │
│  ├─→ Read existing Repository         → Extract implementation pattern      │
│  └─→ Read NetworkModule/DataModule    → Extract DI pattern                  │
│                                                                              │
│  PHASE 2: NETWORK LAYER (if needed)                                         │
│  ├─→ Check FEATURE_MAP for existing   → Skip if exists                      │
│  ├─→ Create Service interface         → Pattern-matched code                │
│  └─→ Register in NetworkModule        → DI registration                     │
│                                                                              │
│  PHASE 3: DATA LAYER (if needed)                                            │
│  ├─→ Check FEATURE_MAP for existing   → Skip if exists                      │
│  ├─→ Create Repository interface      → Pattern-matched code                │
│  ├─→ Create RepositoryImpl            → Pattern-matched code                │
│  └─→ Register in RepositoryModule     → DI registration                     │
│                                                                              │
│  PHASE 4: BUILD & VERIFY                                                    │
│  ├─→ ./gradlew :core:network:build                                          │
│  ├─→ ./gradlew :core:data:build                                             │
│  ├─→ ./gradlew spotlessApply                                                │
│  └─→ Update FEATURE_MAP.md            → Maintain O(1) index                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## PHASE 0: O(1) Context Loading

### Files to Read

| File | Purpose | Data Extracted |
|------|---------|----------------|
| `client-layer/FEATURE_MAP.md` | Service/Repo inventory | existingServices[], existingRepos[] |
| `server-layer/API_INDEX.md` | All API endpoints | endpoints[], dtos[] |
| `design-spec-layer/features/[name]/API.md` | Feature endpoints | featureEndpoints[] |
| `design-spec-layer/features/[name]/SPEC.md` | Data requirements | models[], fields[] |

### Decision Matrix (from FEATURE_MAP.md lookup)

```markdown
| Component | Exists | Action |
|-----------|:------:|--------|
| ${Feature}Service | ✅/❌ | SKIP/CREATE |
| ${Feature}Repository | ✅/❌ | SKIP/CREATE |
```

---

## PHASE 1: Pattern Detection

### Reference Files

```
1. Service Reference:
   core/network/src/commonMain/.../services/BeneficiaryService.kt

2. Repository Reference:
   core/data/src/commonMain/.../repository/BeneficiaryRepository.kt
   core/data/src/commonMain/.../repository/BeneficiaryRepositoryImp.kt

3. DI Reference:
   core/network/src/commonMain/.../di/NetworkModule.kt
   core/data/src/commonMain/.../di/RepositoryModule.kt
```

### Extracted Patterns

```kotlin
// Service Pattern
val servicePattern = ServicePattern(
    returnFlow = "Flow<Type>",           // GET returns Flow
    returnSuspend = "HttpResponse",       // POST/PUT/DELETE returns HttpResponse
    pathAnnotation = "@Path(\"id\")",
    bodyAnnotation = "@Body",
    endpointConstant = "ApiEndPoints.CONSTANT"
)

// Repository Pattern
val repoPattern = RepositoryPattern(
    interfaceReturn = "Flow<DataState<T>>",
    implUsesFlow = "= flow { emit(...) }",
    loadingEmit = "emit(DataState.Loading)",
    successEmit = "emit(DataState.Success(data))",
    errorEmit = "emit(DataState.Error(e.message ?: \"Unknown error\"))"
)

// DI Pattern
val diPattern = DiPattern(
    serviceDeclaration = "single<Service> { get<Ktorfit>().create<Service>() }",
    repoDeclaration = "single<Repository> { RepositoryImp(get()) }"
)
```

---

## PHASE 2: Network Layer

### File Locations

| Component | Location |
|-----------|----------|
| Service Interface | `core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/services/` |
| API Endpoints | `core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/ApiEndPoints.kt` |
| Network DI | `core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/di/NetworkModule.kt` |

### Service Template (Pattern-Matched)

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.core.network.services

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.network.ApiEndPoints
import org.mifos.mobile.core.network.model.${Dto}

interface ${Feature}Service {

    @GET(ApiEndPoints.${ENDPOINT_CONSTANT})
    fun get${Feature}List(): Flow<List<${Dto}>>

    @GET(ApiEndPoints.${ENDPOINT_CONSTANT} + "/{id}")
    fun get${Feature}ById(@Path("id") id: Long): Flow<${Dto}>

    @POST(ApiEndPoints.${ENDPOINT_CONSTANT})
    suspend fun create${Feature}(@Body payload: ${Payload}): HttpResponse

    @PUT(ApiEndPoints.${ENDPOINT_CONSTANT} + "/{id}")
    suspend fun update${Feature}(
        @Path("id") id: Long,
        @Body payload: ${Payload},
    ): HttpResponse

    @DELETE(ApiEndPoints.${ENDPOINT_CONSTANT} + "/{id}")
    suspend fun delete${Feature}(@Path("id") id: Long): HttpResponse
}
```

### Add Endpoint Constant (if needed)

```kotlin
// ApiEndPoints.kt
object ApiEndPoints {
    // ... existing constants
    const val ${ENDPOINT_CONSTANT} = "${endpoint_path}"
}
```

### Register in NetworkModule

```kotlin
// NetworkModule.kt
val networkModule = module {
    // ... existing registrations
    single<${Feature}Service> { get<Ktorfit>().create<${Feature}Service>() }
}
```

---

## PHASE 3: Data Layer

### File Locations

| Component | Location |
|-----------|----------|
| Repository Interface | `core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/` |
| Repository Impl | `core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/` |
| Data DI | `core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/di/RepositoryModule.kt` |

### Repository Interface Template

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.${Model}

interface ${Feature}Repository {
    fun get${Feature}List(): Flow<DataState<List<${Model}>>>
    fun get${Feature}ById(id: Long): Flow<DataState<${Model}>>
    suspend fun create${Feature}(data: ${Model}): DataState<Unit>
    suspend fun update${Feature}(id: Long, data: ${Model}): DataState<Unit>
    suspend fun delete${Feature}(id: Long): DataState<Unit>
}
```

### Repository Implementation Template

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.${Model}
import org.mifos.mobile.core.network.services.${Feature}Service

class ${Feature}RepositoryImp(
    private val ${feature}Service: ${Feature}Service,
) : ${Feature}Repository {

    override fun get${Feature}List(): Flow<DataState<List<${Model}>>> = flow {
        emit(DataState.Loading)
        try {
            val result = ${feature}Service.get${Feature}List().first()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }

    override fun get${Feature}ById(id: Long): Flow<DataState<${Model}>> = flow {
        emit(DataState.Loading)
        try {
            val result = ${feature}Service.get${Feature}ById(id).first()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun create${Feature}(data: ${Model}): DataState<Unit> {
        return try {
            ${feature}Service.create${Feature}(data.toPayload())
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun update${Feature}(id: Long, data: ${Model}): DataState<Unit> {
        return try {
            ${feature}Service.update${Feature}(id, data.toPayload())
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun delete${Feature}(id: Long): DataState<Unit> {
        return try {
            ${feature}Service.delete${Feature}(id)
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e.message ?: "Unknown error")
        }
    }
}
```

### Register in RepositoryModule

```kotlin
// RepositoryModule.kt
val repositoryModule = module {
    // ... existing registrations
    single<${Feature}Repository> { ${Feature}RepositoryImp(get()) }
}
```

---

## PHASE 4: Build & Verify

### Build Commands

```bash
# Build network module
./gradlew :core:network:build

# Build data module
./gradlew :core:data:build

# Format code
./gradlew spotlessApply --no-configuration-cache

# Run detekt
./gradlew detekt
```

### Update FEATURE_MAP.md

Add new entry to maintain O(1) lookup:

```markdown
| ${feature} | ${Feature}Service | ${Feature}Repository | ${Notes} |
```

---

## Output Template

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ✅ CLIENT LAYER COMPLETE                                                    │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  📚 O(1) Context Used:                                                        │
│  ├─ FEATURE_MAP.md → Checked existing: [existing services/repos]             │
│  ├─ API_INDEX.md → Mapped [n] endpoints                                      │
│  └─ API.md → Feature endpoints: [list]                                       │
│                                                                               │
│  📊 Pattern Matching:                                                         │
│  ├─ Service pattern from: BeneficiaryService.kt                              │
│  └─ Repository pattern from: BeneficiaryRepositoryImp.kt                     │
│                                                                               │
│  🔧 Network Layer:                                                            │
│  ├─ ${Feature}Service.kt                [CREATED|SKIPPED]                    │
│  ├─ ApiEndPoints.${CONSTANT}            [ADDED|EXISTS]                       │
│  └─ NetworkModule registration          [ADDED|EXISTS]                       │
│                                                                               │
│  🔧 Data Layer:                                                               │
│  ├─ ${Feature}Repository.kt             [CREATED|SKIPPED]                    │
│  ├─ ${Feature}RepositoryImp.kt          [CREATED|SKIPPED]                    │
│  └─ RepositoryModule registration       [ADDED|EXISTS]                       │
│                                                                               │
│  📋 Index Updated:                                                            │
│  └─ FEATURE_MAP.md                      [UPDATED]                            │
│                                                                               │
│  🔨 BUILD:                                                                    │
│  ├─ :core:network ✅                                                          │
│  └─ :core:data ✅                                                             │
│                                                                               │
│  🧹 LINT: spotlessApply ✅                                                    │
│                                                                               │
├──────────────────────────────────────────────────────────────────────────────┤
│  NEXT STEP:                                                                   │
│  Run:  /feature ${Feature}                                                   │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Client Status (No Argument)

When `/client` called without arguments, read FEATURE_MAP.md:

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  📋 CLIENT LAYER STATUS (from FEATURE_MAP.md)                                │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Summary: 13 services | 17 repositories | 2 DI modules                       │
│                                                                               │
│  | Feature         | Service           | Repository         | Status     │   │
│  |-----------------|-------------------|--------------------|-----------│   │
│  | auth            | AuthenticationSvc | UserAuthRepository | ✅ Complete│   │
│  | home            | ClientService     | HomeRepository     | ✅ Complete│   │
│  | accounts        | ClientService     | AccountsRepository | ✅ Complete│   │
│  | beneficiary     | BeneficiaryService| BeneficiaryRepo    | ✅ Complete│   │
│  | ...                                                                       │
│                                                                               │
│  Commands:                                                                    │
│  • /client [feature] → Implement client layer                                │
│  • /gap-analysis client → Check for gaps                                     │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Error Handling

### Missing API Endpoint

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ⚠️ MISSING API ENDPOINT                                                     │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Feature: ${feature}                                                         │
│  Expected: API.md with endpoint definitions                                  │
│  Found: File missing or empty                                                │
│                                                                               │
│  Options:                                                                     │
│  • d / design   → Run /design ${feature} api first                           │
│  • m / manual   → Enter endpoints manually                                   │
│  • a / abort    → Cancel implementation                                      │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Build Failure

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ❌ BUILD FAILED: :core:network                                              │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Error: Unresolved reference: ${Dto}                                         │
│                                                                               │
│  📍 Auto-Fix Suggestion:                                                      │
│  Create DTO in core/network/model/:                                          │
│                                                                               │
│  ```kotlin                                                                   │
│  @Serializable                                                               │
│  data class ${Dto}(                                                          │
│      val id: Long,                                                           │
│      // ... fields from API.md                                               │
│  )                                                                           │
│  ```                                                                         │
│                                                                               │
│  Options:                                                                     │
│  • f / fix    → Create DTO and rebuild                                       │
│  • m / manual → Show full DTO template                                       │
│  • a / abort  → Stop implementation                                          │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Related Files

### O(1) Index Files

| File | Purpose |
|------|---------|
| `client-layer/FEATURE_MAP.md` | Service/Repository inventory |
| `server-layer/API_INDEX.md` | All API endpoints |
| `client-layer/LAYER_STATUS.md` | Implementation status |

### Reference Code

| Component | Reference File |
|-----------|----------------|
| Service | `BeneficiaryService.kt` |
| Repository | `BeneficiaryRepositoryImp.kt` |
| DI | `NetworkModule.kt`, `RepositoryModule.kt` |

---

## Related Commands

| Command | Purpose |
|---------|---------|
| `/feature [Feature]` | Feature layer (ViewModel + Screen) |
| `/implement [Feature]` | Full E2E (Client + Feature) |
| `/gap-analysis client` | Check client layer gaps |
| `/verify [Feature]` | Verify implementation |

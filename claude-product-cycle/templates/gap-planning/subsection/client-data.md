# Gap Planning: Client → Data Sub-Section

## Implementation Plan: Repositories

**Location**: `core/data/repository/`
**Progress**: {{DATA_PCT}}% Complete
**Current Focus**: {{CURRENT_FOCUS}}

---

### Task Queue

| # | Repository | Status | Services | Action |
|:-:|------------|:------:|----------|--------|
{{TASK_QUEUE_ROWS}}

---

### Current Task: {{CURRENT_REPO}}

**Goal**: {{TASK_GOAL}}

**File**: `core/data/repository/{{REPO_FILE}}`

**Code Sketch**:
```kotlin
class {{REPO_NAME}}(
    private val service: {{SERVICE_NAME}},
    private val dispatchers: DispatcherProvider
) {
    suspend fun get{{ENTITY}}(): Flow<Result<{{ENTITY}}>> = flow {
        emit(Result.Loading)
        try {
            val result = service.get{{ENTITY}}()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatchers.io)
}
```

**Steps**:
1. Create repository class
2. Inject service dependency
3. Implement CRUD operations
4. Add to Koin module
5. Test repository

---

### Repository Template

```kotlin
// core/data/repository/{{REPO_NAME}}.kt
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.DispatcherProvider
import org.mifos.mobile.core.common.Result
import org.mifos.mobile.core.network.services.{{SERVICE_NAME}}

class {{REPO_NAME}}(
    private val service: {{SERVICE_NAME}},
    private val dispatchers: DispatcherProvider
) {
    fun getAll(): Flow<Result<List<{{ENTITY}}>>> = flow {
        emit(Result.Loading)
        try {
            val result = service.getAll()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatchers.io)

    fun getById(id: Long): Flow<Result<{{ENTITY}}>> = flow {
        emit(Result.Loading)
        try {
            val result = service.getById(id)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatchers.io)

    suspend fun create(request: {{REQUEST_DTO}}): Result<{{RESPONSE_DTO}}> {
        return try {
            val result = service.create(request)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

---

### Koin Module Update

```kotlin
// core/data/di/DataModule.kt
val dataModule = module {
    single { {{REPO_NAME}}(get(), get()) }
}
```

---

### After Completion

1. Run `/gap-analysis client data` to see updated status
2. Continue with feature layer: `/gap-planning feature`
3. Update `client-layer/LAYER_STATUS.md`

---

### Verification

- [ ] Repository compiles without errors
- [ ] All CRUD operations implemented
- [ ] Error handling with Result wrapper
- [ ] Added to Koin module
- [ ] Unit tests pass

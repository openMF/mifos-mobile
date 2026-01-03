# Gap Planning: Client → Network Sub-Section

## Implementation Plan: Network Services

**Location**: `core/network/services/`
**Progress**: {{NETWORK_PCT}}% Complete
**Current Focus**: {{CURRENT_FOCUS}}

---

### Task Queue

| # | Service | Status | Endpoints | Action |
|:-:|---------|:------:|:---------:|--------|
{{TASK_QUEUE_ROWS}}

---

### Current Task: {{CURRENT_SERVICE}}

**Goal**: {{TASK_GOAL}}

**File**: `core/network/services/{{SERVICE_FILE}}`

**Code Sketch**:
```kotlin
interface {{SERVICE_NAME}} {
    @GET("self/{{ENDPOINT}}")
    suspend fun get{{ENTITY}}(): {{RESPONSE_TYPE}}

    @POST("self/{{ENDPOINT}}")
    suspend fun create{{ENTITY}}(
        @Body request: {{REQUEST_TYPE}}
    ): {{RESPONSE_TYPE}}
}
```

**Steps**:
1. Create service interface
2. Add to Koin module
3. Update repository to use service
4. Test endpoints

---

### Service Template

```kotlin
// core/network/services/{{SERVICE_NAME}}.kt
package org.mifos.mobile.core.network.services

import de.jensklingenberg.ktorfit.http.*
import org.mifos.mobile.core.model.entity.*

interface {{SERVICE_NAME}} {
    @GET("self/{{BASE_PATH}}")
    suspend fun getAll(): List<{{ENTITY}}>

    @GET("self/{{BASE_PATH}}/{id}")
    suspend fun getById(@Path("id") id: Long): {{ENTITY}}

    @POST("self/{{BASE_PATH}}")
    suspend fun create(@Body request: {{REQUEST_DTO}}): {{RESPONSE_DTO}}

    @PUT("self/{{BASE_PATH}}/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: {{REQUEST_DTO}}
    ): {{RESPONSE_DTO}}

    @DELETE("self/{{BASE_PATH}}/{id}")
    suspend fun delete(@Path("id") id: Long)
}
```

---

### Koin Module Update

```kotlin
// core/network/di/NetworkModule.kt
val networkModule = module {
    single<{{SERVICE_NAME}}> { get<Ktorfit>().create() }
}
```

---

### After Completion

1. Run `/gap-analysis client network` to see updated status
2. Continue with repository: `/gap-planning client data`
3. Update `client-layer/LAYER_STATUS.md`

---

### Verification

- [ ] Service compiles without errors
- [ ] Endpoints match Fineract API
- [ ] Added to Koin module
- [ ] Repository uses service

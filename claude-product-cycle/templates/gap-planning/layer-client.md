# Client Layer Planning Template

## Implementation Plan: Client Layer

**Location**: `core/network/` + `core/data/` + `core/model/`
**Reference**: `claude-product-cycle/client-layer/LAYER_STATUS.md`
**Last Updated**: {{DATE}}

---

### Gaps Identified

| # | Feature | Missing Component | Priority | Effort |
|---|---------|-------------------|:--------:|:------:|
{{CLIENT_GAPS_TABLE}}

---

### Tasks Overview

| # | Task | Files | Priority | Effort |
|---|------|-------|:--------:|:------:|
{{TASKS_TABLE}}

---

### Task Details

{{TASK_DETAILS}}

---

### Service Template

For missing `*Service.kt`:

**Create** `core/network/src/commonMain/.../services/[Feature]Service.kt`:
```kotlin
package org.mifos.mobile.core.network.services

import de.jensklingenberg.ktorfit.http.*
import org.mifos.mobile.core.model.[feature].*

interface [Feature]Service {

    @GET("self/[endpoint]")
    suspend fun get[Feature](): [ResponseType]

    @POST("self/[endpoint]")
    suspend fun create[Feature](
        @Body request: [RequestType]
    ): [ResponseType]
}
```

**Register in** `core/network/src/commonMain/.../di/NetworkModule.kt`:
```kotlin
single<[Feature]Service> { get<Ktorfit>().create() }
```

---

### Repository Template

For missing `*Repository.kt`:

**Create** `core/data/src/commonMain/.../repository/[Feature]Repository.kt`:
```kotlin
package org.mifos.mobile.core.data.repository

import org.mifos.mobile.core.network.services.[Feature]Service
import org.mifos.mobile.core.model.[feature].*

interface [Feature]Repository {
    suspend fun get[Feature](): Result<[Type]>
}

class [Feature]RepositoryImpl(
    private val service: [Feature]Service
) : [Feature]Repository {

    override suspend fun get[Feature](): Result<[Type]> {
        return runCatching {
            service.get[Feature]()
        }
    }
}
```

**Register in** `core/data/src/commonMain/.../di/DataModule.kt`:
```kotlin
single<[Feature]Repository> { [Feature]RepositoryImpl(get()) }
```

---

### Model Template

For missing models:

**Create** `core/model/src/commonMain/.../[feature]/[Model].kt`:
```kotlin
package org.mifos.mobile.core.model.[feature]

import kotlinx.serialization.Serializable

@Serializable
data class [Model](
    val id: Long,
    val name: String,
    // ... fields from API.md
)
```

---

### Verification

```bash
./gradlew :core:network:build
./gradlew :core:data:build
./gradlew :core:model:build
```

After completing:
1. Update `client-layer/LAYER_STATUS.md`
2. Update `PRODUCT_MAP.md`

---

**Ready?** Run `/client [feature]` to implement

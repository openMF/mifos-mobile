# Fake Repository Pattern

> Detailed instructions for creating test doubles in Mifos Mobile

---

## Overview

Fake repositories:
- Implement the real repository interface
- Provide configurable responses for testing
- Track method calls for verification
- Enable test isolation

---

## File Location

```
core/testing/src/commonMain/kotlin/org/mifos/mobile/core/testing/fake/Fake${Feature}Repository.kt
```

---

## Standard Template

```kotlin
package org.mifos.mobile.core.testing.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.data.repository.${Feature}Repository
import org.mifos.mobile.core.model.${Model}
import org.mifos.mobile.core.common.DataState

class Fake${Feature}Repository : ${Feature}Repository {

    // ═══════════════════════════════════════════════════════════════
    // CALL TRACKING
    // ═══════════════════════════════════════════════════════════════

    /**
     * Number of times load method was called.
     * Use to verify refresh/retry behavior.
     */
    var loadCallCount = 0
        private set

    /**
     * Number of times create method was called.
     */
    var createCallCount = 0
        private set

    /**
     * Number of times update method was called.
     */
    var updateCallCount = 0
        private set

    /**
     * Number of times delete method was called.
     */
    var deleteCallCount = 0
        private set

    /**
     * Last payload passed to create method.
     * Use to verify correct data was sent.
     */
    var lastCreatePayload: ${Model}Payload? = null
        private set

    /**
     * Last ID passed to delete method.
     */
    var lastDeleteId: Long? = null
        private set

    // ═══════════════════════════════════════════════════════════════
    // CONFIGURABLE RESPONSES
    // ═══════════════════════════════════════════════════════════════

    private var loadResponse: DataState<List<${Model}>> = DataState.Loading
    private var singleResponse: DataState<${Model}> = DataState.Loading
    private var createResponse: DataState<${Model}> = DataState.Loading
    private var updateResponse: DataState<${Model}> = DataState.Loading
    private var deleteResponse: DataState<Unit> = DataState.Loading

    // ═══════════════════════════════════════════════════════════════
    // SETUP METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Configure load to return success with data.
     *
     * @param data The list of items to return
     */
    fun setLoadSuccess(data: List<${Model}>) {
        loadResponse = DataState.Success(data)
    }

    /**
     * Configure load to return error.
     *
     * @param message The error message
     */
    fun setLoadError(message: String = "Failed to load") {
        loadResponse = DataState.Error(message)
    }

    /**
     * Configure load to return empty list.
     */
    fun setLoadEmpty() {
        loadResponse = DataState.Success(emptyList())
    }

    /**
     * Configure load to return loading state (useful for testing loading UI).
     */
    fun setLoadLoading() {
        loadResponse = DataState.Loading
    }

    /**
     * Configure get single item to return success.
     */
    fun setSingleSuccess(item: ${Model}) {
        singleResponse = DataState.Success(item)
    }

    /**
     * Configure get single item to return error.
     */
    fun setSingleError(message: String = "Item not found") {
        singleResponse = DataState.Error(message)
    }

    /**
     * Configure create to return success.
     */
    fun setCreateSuccess(item: ${Model}) {
        createResponse = DataState.Success(item)
    }

    /**
     * Configure create to return error.
     */
    fun setCreateError(message: String = "Failed to create") {
        createResponse = DataState.Error(message)
    }

    /**
     * Configure update to return success.
     */
    fun setUpdateSuccess(item: ${Model}) {
        updateResponse = DataState.Success(item)
    }

    /**
     * Configure update to return error.
     */
    fun setUpdateError(message: String = "Failed to update") {
        updateResponse = DataState.Error(message)
    }

    /**
     * Configure delete to return success.
     */
    fun setDeleteSuccess() {
        deleteResponse = DataState.Success(Unit)
    }

    /**
     * Configure delete to return error.
     */
    fun setDeleteError(message: String = "Failed to delete") {
        deleteResponse = DataState.Error(message)
    }

    // ═══════════════════════════════════════════════════════════════
    // REPOSITORY IMPLEMENTATION
    // ═══════════════════════════════════════════════════════════════

    override fun get${Feature}s(): Flow<DataState<List<${Model}>>> = flow {
        loadCallCount++
        emit(loadResponse)
    }

    override fun get${Feature}(id: Long): Flow<DataState<${Model}>> = flow {
        emit(singleResponse)
    }

    override fun create${Feature}(payload: ${Model}Payload): Flow<DataState<${Model}>> = flow {
        createCallCount++
        lastCreatePayload = payload
        emit(createResponse)
    }

    override fun update${Feature}(id: Long, payload: ${Model}Payload): Flow<DataState<${Model}>> = flow {
        updateCallCount++
        emit(updateResponse)
    }

    override fun delete${Feature}(id: Long): Flow<DataState<Unit>> = flow {
        deleteCallCount++
        lastDeleteId = id
        emit(deleteResponse)
    }

    // ═══════════════════════════════════════════════════════════════
    // RESET
    // ═══════════════════════════════════════════════════════════════

    /**
     * Reset all counters and responses.
     * Call in @AfterTest to ensure test isolation.
     */
    fun reset() {
        // Reset counters
        loadCallCount = 0
        createCallCount = 0
        updateCallCount = 0
        deleteCallCount = 0

        // Reset captured data
        lastCreatePayload = null
        lastDeleteId = null

        // Reset responses to loading
        loadResponse = DataState.Loading
        singleResponse = DataState.Loading
        createResponse = DataState.Loading
        updateResponse = DataState.Loading
        deleteResponse = DataState.Loading
    }
}
```

---

## Usage Examples

### Basic Test Setup

```kotlin
class ${Feature}ViewModelTest {
    private lateinit var fakeRepository: Fake${Feature}Repository
    private lateinit var viewModel: ${Feature}ViewModel

    @BeforeTest
    fun setup() {
        fakeRepository = Fake${Feature}Repository()
        viewModel = ${Feature}ViewModel(repository = fakeRepository)
    }

    @AfterTest
    fun teardown() {
        fakeRepository.reset()
    }
}
```

### Testing Success State

```kotlin
@Test
fun `load success updates state`() = runTest {
    val testData = ${Feature}Fixtures.createList(5)
    fakeRepository.setLoadSuccess(testData)

    viewModel.loadData()

    viewModel.stateFlow.test {
        val state = expectMostRecentItem()
        assertEquals(testData, (state.uiState as Success).data)
    }
}
```

### Testing Error State

```kotlin
@Test
fun `load error shows error`() = runTest {
    fakeRepository.setLoadError("Network unavailable")

    viewModel.loadData()

    viewModel.stateFlow.test {
        val state = expectMostRecentItem()
        assertTrue(state.uiState is Error)
        assertEquals("Network unavailable", (state.uiState as Error).message)
    }
}
```

### Verifying Method Calls

```kotlin
@Test
fun `refresh calls repository twice`() = runTest {
    fakeRepository.setLoadSuccess(emptyList())

    viewModel.loadData()
    viewModel.trySendAction(Action.Refresh)

    assertEquals(2, fakeRepository.loadCallCount)
}
```

### Verifying Payload

```kotlin
@Test
fun `create sends correct payload`() = runTest {
    val payload = ${Feature}Payload(name = "Test")
    fakeRepository.setCreateSuccess(${Feature}Fixtures.create())

    viewModel.create(payload)

    assertEquals(payload, fakeRepository.lastCreatePayload)
}
```

### Testing Delete Flow

```kotlin
@Test
fun `delete calls repository with correct id`() = runTest {
    fakeRepository.setDeleteSuccess()

    viewModel.delete(itemId = 42L)

    assertEquals(1, fakeRepository.deleteCallCount)
    assertEquals(42L, fakeRepository.lastDeleteId)
}
```

---

## Advanced Patterns

### Sequential Responses

For testing pagination or retry:

```kotlin
class FakePaginatedRepository : Repository {
    private val responses = mutableListOf<DataState<List<Item>>>()
    private var responseIndex = 0

    fun addResponse(response: DataState<List<Item>>) {
        responses.add(response)
    }

    override fun getItems(): Flow<DataState<List<Item>>> = flow {
        if (responseIndex < responses.size) {
            emit(responses[responseIndex++])
        }
    }
}

// Usage in test:
@Test
fun `pagination loads next page`() = runTest {
    fakeRepository.addResponse(DataState.Success(page1))
    fakeRepository.addResponse(DataState.Success(page2))

    viewModel.loadData()      // Gets page1
    viewModel.loadMore()      // Gets page2

    // Verify combined data
}
```

### Delayed Responses

For testing loading states:

```kotlin
class FakeDelayedRepository : Repository {
    var delay: Long = 0L

    override fun getItems(): Flow<DataState<List<Item>>> = flow {
        emit(DataState.Loading)
        delay(delay)
        emit(DataState.Success(data))
    }
}

// Usage:
@Test
fun `shows loading while fetching`() = runTest {
    fakeRepository.delay = 1000L

    viewModel.loadData()

    viewModel.stateFlow.test {
        assertTrue(awaitItem().uiState is Loading)
        // ...
    }
}
```

### Error Then Success

For testing retry:

```kotlin
@Test
fun `retry after error succeeds`() = runTest {
    fakeRepository.setLoadError("Network error")
    viewModel.loadData()

    // Verify error state
    viewModel.stateFlow.test {
        assertTrue(expectMostRecentItem().uiState is Error)
    }

    // Configure success and retry
    fakeRepository.setLoadSuccess(testData)
    viewModel.trySendAction(Action.Retry)

    // Verify success state
    viewModel.stateFlow.test {
        assertTrue(expectMostRecentItem().uiState is Success)
    }
}
```

---

## Naming Convention

| Real Repository | Fake Repository |
|-----------------|-----------------|
| `UserAuthRepository` | `FakeUserAuthRepository` |
| `BeneficiaryRepository` | `FakeBeneficiaryRepository` |
| `HomeRepository` | `FakeHomeRepository` |
| `LoanRepository` | `FakeLoanRepository` |

---

## Checklist

When creating a fake repository:

- [ ] Implements real repository interface
- [ ] Has call counters for all methods
- [ ] Has configurable responses (success, error, loading)
- [ ] Captures payloads for verification
- [ ] Has `reset()` method
- [ ] Uses fixtures for default data
- [ ] Documents public methods

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Not resetting between tests | Call `reset()` in `@AfterTest` |
| Returning same response | Use response queues for sequences |
| Missing interface methods | Implement all methods |
| Not tracking calls | Add counter for each method |

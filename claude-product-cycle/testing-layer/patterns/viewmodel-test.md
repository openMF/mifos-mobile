# ViewModel Test Pattern

> Detailed instructions for testing ViewModels in Mifos Mobile

---

## Overview

ViewModel tests verify:
- State transitions (Loading → Success/Error)
- Action handling (user interactions)
- Event emission (navigation, dialogs)
- Business logic correctness

---

## File Location

```
feature/${feature}/src/commonTest/kotlin/org/mifos/mobile/feature/${feature}/${Feature}ViewModelTest.kt
```

---

## Dependencies

```kotlin
// build.gradle.kts
kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)  // Flow testing
        }
    }
}
```

---

## Test Structure

```kotlin
class ${Feature}ViewModelTest {
    // ═══════════════════════════════════════════════════════════════
    // SETUP
    // ═══════════════════════════════════════════════════════════════

    private val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ${Feature}ViewModel
    private lateinit var fakeRepository: Fake${Feature}Repository

    @BeforeTest
    fun setup() {
        fakeRepository = Fake${Feature}Repository()
        viewModel = ${Feature}ViewModel(
            repository = fakeRepository
        )
    }

    @AfterTest
    fun teardown() {
        fakeRepository.reset()
    }

    // ═══════════════════════════════════════════════════════════════
    // INITIAL STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `initial state is loading`() = runTest {
        viewModel.stateFlow.test {
            val state = awaitItem()
            assertTrue(state.uiState is ${Feature}UiState.Loading)
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // SUCCESS STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `load success updates state with data`() = runTest {
        val testData = ${Feature}Fixtures.createList(5)
        fakeRepository.setLoadSuccess(testData)

        viewModel.loadData()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}UiState.Success)
            assertEquals(testData, (state.uiState as ${Feature}UiState.Success).data)
        }
    }

    @Test
    fun `empty data shows empty state`() = runTest {
        fakeRepository.setLoadEmpty()

        viewModel.loadData()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}UiState.Success)
            assertTrue((state.uiState as ${Feature}UiState.Success).data.isEmpty())
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // ERROR STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `load error shows error state`() = runTest {
        fakeRepository.setLoadError("Network error")

        viewModel.loadData()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}UiState.Error)
            assertEquals("Network error", (state.uiState as ${Feature}UiState.Error).message)
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // ACTION TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `refresh action reloads data`() = runTest {
        fakeRepository.setLoadSuccess()

        viewModel.loadData()
        viewModel.trySendAction(${Feature}Action.Refresh)

        assertEquals(2, fakeRepository.loadCallCount)
    }

    @Test
    fun `item click action triggers navigation event`() = runTest {
        viewModel.trySendAction(${Feature}Action.ItemClicked(itemId = 1L))

        viewModel.eventFlow.test {
            val event = awaitItem()
            assertEquals(${Feature}Event.NavigateToDetail(1L), event)
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // DIALOG TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `delete action shows confirmation dialog`() = runTest {
        viewModel.trySendAction(${Feature}Action.DeleteClicked(itemId = 1L))

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.dialogState is DialogState.Confirmation)
        }
    }

    @Test
    fun `confirm delete calls repository`() = runTest {
        fakeRepository.setDeleteSuccess()

        viewModel.trySendAction(${Feature}Action.ConfirmDelete(itemId = 1L))

        assertEquals(1, fakeRepository.deleteCallCount)
    }

    @Test
    fun `dialog dismiss clears dialog state`() = runTest {
        viewModel.trySendAction(${Feature}Action.DeleteClicked(itemId = 1L))
        viewModel.trySendAction(${Feature}Action.DismissDialog)

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertNull(state.dialogState)
        }
    }
}
```

---

## Test Categories

### 1. Initial State Tests

Verify the ViewModel starts with correct default state.

```kotlin
@Test
fun `initial state has loading ui state`() = runTest {
    viewModel.stateFlow.test {
        assertTrue(awaitItem().uiState is Loading)
    }
}

@Test
fun `initial state has null dialog state`() = runTest {
    viewModel.stateFlow.test {
        assertNull(awaitItem().dialogState)
    }
}
```

### 2. Data Loading Tests

Test success, error, and empty scenarios.

```kotlin
@Test
fun `load with pagination appends data`() = runTest {
    fakeRepository.setLoadSuccess(page1Data)
    viewModel.loadData()

    fakeRepository.setLoadSuccess(page2Data)
    viewModel.trySendAction(Action.LoadMore)

    viewModel.stateFlow.test {
        val data = (expectMostRecentItem().uiState as Success).data
        assertEquals(page1Data + page2Data, data)
    }
}
```

### 3. User Action Tests

Test all actions defined in the Action sealed class.

```kotlin
// For each action in ${Feature}Action:
@Test
fun `action X updates state correctly`() = runTest {
    viewModel.trySendAction(${Feature}Action.X)

    viewModel.stateFlow.test {
        // Verify state change
    }
}
```

### 4. Event Tests

Test navigation and one-time events.

```kotlin
@Test
fun `submit success emits navigation event`() = runTest {
    fakeRepository.setCreateSuccess()

    viewModel.trySendAction(Action.Submit)

    viewModel.eventFlow.test {
        assertEquals(Event.NavigateBack, awaitItem())
    }
}
```

### 5. Validation Tests

Test form validation logic.

```kotlin
@Test
fun `empty input shows validation error`() = runTest {
    viewModel.trySendAction(Action.NameChanged(""))
    viewModel.trySendAction(Action.Submit)

    viewModel.stateFlow.test {
        assertNotNull(expectMostRecentItem().validationError)
    }
}

@Test
fun `valid input clears validation error`() = runTest {
    viewModel.trySendAction(Action.NameChanged(""))
    viewModel.trySendAction(Action.NameChanged("Valid Name"))

    viewModel.stateFlow.test {
        assertNull(expectMostRecentItem().validationError)
    }
}
```

---

## MainDispatcherRule

Required for testing coroutines in ViewModels:

```kotlin
// core/testing/src/commonMain/.../rule/MainDispatcherRule.kt

class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) {
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }
}
```

---

## Turbine Usage

### Basic Flow Testing

```kotlin
viewModel.stateFlow.test {
    val first = awaitItem()   // Get first emission
    val latest = expectMostRecentItem()  // Skip to latest
    cancelAndIgnoreRemainingEvents()
}
```

### Common Turbine Methods

| Method | Purpose |
|--------|---------|
| `awaitItem()` | Wait for next emission |
| `expectMostRecentItem()` | Get latest, skip intermediates |
| `awaitComplete()` | Wait for flow completion |
| `cancelAndIgnoreRemainingEvents()` | Clean up |
| `expectNoEvents()` | Verify no emissions |

---

## Test Coverage Checklist

For each ViewModel, test:

- [ ] Initial state
- [ ] Load success with data
- [ ] Load success with empty data
- [ ] Load error
- [ ] Each action in Action sealed class
- [ ] Each event in Event sealed class
- [ ] Validation (if applicable)
- [ ] Dialog states (if applicable)
- [ ] Refresh/Retry
- [ ] Pagination (if applicable)

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Not using `runTest` | Wrap all tests in `runTest { }` |
| Missing dispatcher rule | Add `MainDispatcherRule` |
| Not resetting fakes | Call `reset()` in `@AfterTest` |
| Using `awaitItem()` for latest | Use `expectMostRecentItem()` |
| Not testing all states | Check coverage checklist |

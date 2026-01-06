# Test Stubs Guide

> Auto-generated test scaffolding for TDD support in `/implement` command

---

## Overview

The `/implement` command automatically generates test stubs (Phase 5) to support Test-Driven Development. This guide explains the generated files, how to use them, and best practices.

---

## Generated Test Files

When running `/implement [feature]`, the following test files are created:

```
feature/[feature]/
├── src/
│   ├── commonTest/kotlin/org/mifos/mobile/feature/[feature]/
│   │   ├── [Feature]ViewModelTest.kt      # ViewModel unit tests
│   │   └── fake/
│   │       └── Fake[Feature]Repository.kt # Test double
│   │
│   └── androidInstrumentedTest/kotlin/org/mifos/mobile/feature/[feature]/
│       └── [Feature]ScreenTest.kt         # Compose UI tests
```

---

## Test Patterns

### 1. ViewModel Test Pattern

```kotlin
class ${Feature}ViewModelTest {
    // Rule for coroutine testing
    private val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ${Feature}ViewModel
    private lateinit var fakeRepository: Fake${Feature}Repository

    @BeforeTest
    fun setup() {
        fakeRepository = Fake${Feature}Repository()
        viewModel = ${Feature}ViewModel(repository = fakeRepository)
    }

    // Test Categories:
    // 1. Initial State Tests
    @Test
    fun `initial state is loading`() = runTest {
        viewModel.stateFlow.test {
            val state = awaitItem()
            assertTrue(state.uiState is ${Feature}ScreenState.Loading)
        }
    }

    // 2. Success State Tests
    @Test
    fun `data loaded successfully shows success state`() = runTest {
        fakeRepository.setSuccessResponse(testData)
        viewModel.loadData()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}ScreenState.Success)
        }
    }

    // 3. Error State Tests
    @Test
    fun `data load failure shows error state`() = runTest {
        fakeRepository.setErrorResponse("Network error")
        viewModel.loadData()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}ScreenState.Error)
        }
    }

    // 4. Action Tests
    @Test
    fun `action updates state correctly`() = runTest {
        viewModel.trySendAction(${Feature}Action.SomeAction)

        viewModel.stateFlow.test {
            // Verify state change
        }
    }

    // 5. Event Tests
    @Test
    fun `action triggers navigation event`() = runTest {
        viewModel.trySendAction(${Feature}Action.ItemClicked(id))

        viewModel.eventFlow.test {
            assertEquals(${Feature}Event.NavigateToDetail(id), awaitItem())
        }
    }
}
```

### 2. Screen Test Pattern

```kotlin
class ${Feature}ScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // Test Categories:
    // 1. Loading State
    @Test
    fun loadingState_displaysLoadingIndicator() {
        val state = ${Feature}State(uiState = ${Feature}ScreenState.Loading)

        composeTestRule.setContent {
            ${Feature}Content(state = state, onAction = {})
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LOADING)
            .assertIsDisplayed()
    }

    // 2. Success State
    @Test
    fun successState_displaysContent() {
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Success(testData)
        )

        composeTestRule.setContent {
            ${Feature}Content(state = state, onAction = {})
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.SCREEN)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LIST)
            .assertIsDisplayed()
    }

    // 3. Error State
    @Test
    fun errorState_displaysErrorMessage() {
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Error("Network error")
        )

        composeTestRule.setContent {
            ${Feature}Content(state = state, onAction = {})
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.ERROR)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Network error")
            .assertIsDisplayed()
    }

    // 4. Empty State
    @Test
    fun emptyState_displaysEmptyMessage() {
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Success(emptyList())
        )

        composeTestRule.setContent {
            ${Feature}Content(state = state, onAction = {})
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.EMPTY)
            .assertIsDisplayed()
    }

    // 5. User Interaction
    @Test
    fun itemClick_triggersAction() {
        var actionReceived: ${Feature}Action? = null
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Success(testData)
        )

        composeTestRule.setContent {
            ${Feature}Content(
                state = state,
                onAction = { actionReceived = it }
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:item:1")
            .performClick()

        assertEquals(${Feature}Action.ItemClicked(1), actionReceived)
    }
}
```

### 3. Fake Repository Pattern

```kotlin
class Fake${Feature}Repository : ${Feature}Repository {
    // Call tracking
    var loadCallCount = 0
        private set

    // Configurable response
    private var response: DataState<List<${Model}>> = DataState.Loading

    // Setup methods
    fun setSuccessResponse(data: List<${Model}>) {
        response = DataState.Success(data)
    }

    fun setErrorResponse(message: String) {
        response = DataState.Error(message)
    }

    fun setEmptyResponse() {
        response = DataState.Success(emptyList())
    }

    fun setLoadingState() {
        response = DataState.Loading
    }

    // Repository implementation
    override fun get${Feature}(): Flow<DataState<List<${Model}>>> = flow {
        loadCallCount++
        emit(response)
    }

    // Reset for test isolation
    fun reset() {
        loadCallCount = 0
        response = DataState.Loading
    }
}
```

---

## TestTag Convention

### Naming Pattern

```
{feature}:{component}:{identifier}
```

### Standard Tags

| Component | Pattern | Example |
|-----------|---------|---------|
| Screen container | `{feature}:screen` | `beneficiary:screen` |
| Loading indicator | `{feature}:loading` | `beneficiary:loading` |
| Error container | `{feature}:error` | `beneficiary:error` |
| Empty state | `{feature}:empty` | `beneficiary:empty` |
| List container | `{feature}:list` | `beneficiary:list` |
| List item | `{feature}:item:{id}` | `beneficiary:item:123` |
| Action button | `{feature}:{action}` | `beneficiary:add` |
| Retry button | `{feature}:retry` | `beneficiary:retry` |
| Input field | `{feature}:input:{name}` | `auth:input:username` |
| Form submit | `{feature}:submit` | `auth:submit` |

### TestTags Object

Each feature should have a TestTags object:

```kotlin
object ${Feature}TestTags {
    const val SCREEN = "${feature}:screen"
    const val LOADING = "${feature}:loading"
    const val ERROR = "${feature}:error"
    const val EMPTY = "${feature}:empty"
    const val LIST = "${feature}:list"
    const val RETRY = "${feature}:retry"

    fun item(id: Long) = "${feature}:item:$id"
}
```

### Applying TestTags

```kotlin
@Composable
fun ${Feature}Screen(...) {
    Scaffold(
        modifier = Modifier.testTag(${Feature}TestTags.SCREEN)
    ) {
        when (state.uiState) {
            is Loading -> LoadingIndicator(
                modifier = Modifier.testTag(${Feature}TestTags.LOADING)
            )
            is Error -> ErrorView(
                modifier = Modifier.testTag(${Feature}TestTags.ERROR)
            )
            is Success -> ContentList(
                modifier = Modifier.testTag(${Feature}TestTags.LIST)
            )
        }
    }
}
```

---

## Test Dependencies

### build.gradle.kts

```kotlin
kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }
    }
}
```

### Key Libraries

| Library | Purpose | Usage |
|---------|---------|-------|
| `kotlin-test` | Assertions | `assertEquals`, `assertTrue` |
| `kotlinx-coroutines-test` | Coroutine testing | `runTest`, `TestDispatcher` |
| `turbine` | Flow testing | `stateFlow.test { }` |
| `compose-ui-test` | Compose UI testing | `onNodeWithTag`, `performClick` |

---

## Test Execution

### Run Commands

```bash
# Run all tests for a feature
./gradlew :feature:${feature}:test

# Run ViewModel tests only (commonTest)
./gradlew :feature:${feature}:jvmTest

# Run Screen tests (Android instrumented)
./gradlew :feature:${feature}:connectedAndroidTest

# Run with coverage
./gradlew :feature:${feature}:test jacocoTestReport
```

### CI Integration

```yaml
# .github/workflows/test.yml
- name: Run Unit Tests
  run: ./gradlew testDebug

- name: Run UI Tests
  run: ./gradlew connectedAndroidTest
```

---

## TDD Workflow

### Red-Green-Refactor

```
1. WRITE FAILING TEST (Red)
   └─ Generated stub has TODO assertions
   └─ Test fails because implementation is incomplete

2. IMPLEMENT (Green)
   └─ Write minimum code to pass
   └─ Fill in ViewModel/Screen logic

3. REFACTOR (Clean)
   └─ Improve code quality
   └─ Keep tests passing

4. VERIFY
   └─ Run /verify [feature]
   └─ Check TestTag validation
```

### Stub Completion Checklist

After `/implement` generates stubs:

- [ ] Fill in test data fixtures
- [ ] Complete assertion logic (replace TODOs)
- [ ] Add edge case tests
- [ ] Verify all TestTags are applied
- [ ] Run tests to confirm passing
- [ ] Update TESTING_STATUS.md

---

## Common Test Scenarios

### 1. Pagination Test

```kotlin
@Test
fun `load more appends to list`() = runTest {
    fakeRepository.setSuccessResponse(page1Data)
    viewModel.loadData()

    fakeRepository.setSuccessResponse(page2Data)
    viewModel.trySendAction(${Feature}Action.LoadMore)

    viewModel.stateFlow.test {
        val state = expectMostRecentItem()
        val data = (state.uiState as Success).data
        assertEquals(page1Data + page2Data, data)
    }
}
```

### 2. Pull-to-Refresh Test

```kotlin
@Test
fun `refresh replaces data`() = runTest {
    fakeRepository.setSuccessResponse(oldData)
    viewModel.loadData()

    fakeRepository.setSuccessResponse(newData)
    viewModel.trySendAction(${Feature}Action.Refresh)

    viewModel.stateFlow.test {
        val state = expectMostRecentItem()
        val data = (state.uiState as Success).data
        assertEquals(newData, data)
    }
}
```

### 3. Form Validation Test

```kotlin
@Test
fun `invalid input shows validation error`() = runTest {
    viewModel.trySendAction(${Feature}Action.Submit(invalidInput))

    viewModel.stateFlow.test {
        val state = expectMostRecentItem()
        assertNotNull(state.validationError)
    }
}
```

### 4. Dialog Confirmation Test

```kotlin
@Test
fun `delete shows confirmation dialog`() = runTest {
    viewModel.trySendAction(${Feature}Action.DeleteClicked(id))

    viewModel.stateFlow.test {
        val state = expectMostRecentItem()
        assertTrue(state.dialogState is DialogState.Confirmation)
    }
}

@Test
fun `confirm delete triggers delete action`() = runTest {
    viewModel.trySendAction(${Feature}Action.ConfirmDelete(id))

    assertEquals(1, fakeRepository.deleteCallCount)
}
```

---

## Troubleshooting

### Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Test timeout | Missing `runTest` | Wrap in `runTest { }` |
| Flow not emitting | Wrong dispatcher | Use `MainDispatcherRule` |
| Node not found | Missing testTag | Add `Modifier.testTag()` |
| Assertion failure | Stale state | Use `expectMostRecentItem()` |

### Debug Tips

```kotlin
// Print state for debugging
viewModel.stateFlow.test {
    val state = expectMostRecentItem()
    println("Current state: $state")
    // assertions...
}

// Print compose tree
composeTestRule.onRoot().printToLog("COMPOSE_TREE")
```

---

## Related Files

- [TESTING_STATUS.md](./feature-layer/TESTING_STATUS.md) - Feature test coverage
- [/verify command](../.claude/commands/verify.md) - TestTag validation
- [/implement command](../.claude/commands/implement.md) - Test stub generation

---

## Commands

```bash
# Generate test stubs for feature
/implement [feature]          # Phase 5 generates tests

# Verify TestTag compliance
/verify [feature]             # Includes TestTag validation

# Check testing gaps
/gap-analysis testing         # Shows test coverage gaps
```

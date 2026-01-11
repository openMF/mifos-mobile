# /implement - E2E Feature Implementation

## Purpose

Full end-to-end implementation using O(1) lookup and pattern detection. Implements client layer (Network + Data) and feature layer (UI) with automatic code generation matching existing codebase conventions.

---

## Command Variants

```
/implement                       # Show feature status list
/implement [Feature]             # Full E2E implementation
/implement [Feature] --quick     # Skip checkpoints
/implement [Feature] --no-git    # Skip git integration
/implement improve [Feature]     # Improve existing feature
```

---

## E2E Pipeline with O(1) Optimization

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  /implement [Feature] - O(1) OPTIMIZED PIPELINE                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  PHASE 0: CONTEXT LOADING (O(1))        ~50-200 lines instead of scanning   │
│  ├─→ Read FEATURE_MAP.md                → Get services + repositories       │
│  ├─→ Read MODULES_INDEX.md              → Get module structure              │
│  ├─→ Read SCREENS_INDEX.md              → Get existing screens/VMs          │
│  └─→ Read feature/*/SPEC.md + API.md    → Get requirements                  │
│                                                                              │
│  PHASE 1: PATTERN DETECTION             Match existing conventions          │
│  ├─→ Read existing ViewModel            → Extract State/Event/Action pattern│
│  ├─→ Read existing Screen               → Extract Composable pattern        │
│  ├─→ Read existing Repository           → Extract DataState pattern         │
│  └─→ Store conventions in memory        → Apply to generated code           │
│                                                                              │
│  PHASE 2: CLIENT LAYER                  Services + Repositories             │
│  ├─→ Check if exists in FEATURE_MAP     → Skip or create                    │
│  ├─→ Generate with pattern matching     → Matches existing code style       │
│  ├─→ Register in DI                     → NetworkModule + RepositoryModule  │
│  ├─→ Build: ./gradlew :core:network:build :core:data:build                  │
│  └─→ ⏸️ CHECKPOINT                                                           │
│                                                                              │
│  PHASE 3: FEATURE LAYER                 ViewModel + Screen + Navigation     │
│  ├─→ Generate ViewModel (MVI)           → With testTags built-in            │
│  ├─→ Generate Screen                    → With design tokens if available   │
│  ├─→ Generate Navigation                → Type-safe routes                  │
│  ├─→ Register in DI                     → Feature Koin module               │
│  ├─→ Build: ./gradlew :feature:[name]:build                                 │
│  └─→ ⏸️ CHECKPOINT                                                           │
│                                                                              │
│  PHASE 4: FINALIZE                      Update indexes + status             │
│  ├─→ Update FEATURE_MAP.md              → Add new mappings                  │
│  ├─→ Update MODULES_INDEX.md            → Add module entry                  │
│  ├─→ Update SCREENS_INDEX.md            → Add screen entries                │
│  ├─→ Update STATUS.md files             → Mark as implemented               │
│  └─→ Final build: ./gradlew build                                           │
│                                                                              │
│  PHASE 5: TEST STUBS (TDD Support)      Generate test scaffolding           │
│  ├─→ Generate ViewModel test            → commonTest with Turbine           │
│  ├─→ Generate Screen test               → androidInstrumentedTest           │
│  ├─→ Generate Fake repository           → For testing isolation             │
│  ├─→ Update TESTING_STATUS.md           → Mark stubs created                │
│  └─→ ⏸️ CHECKPOINT                       → Review generated tests            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## PHASE 0: O(1) Context Loading

### Step 0.1: Read Index Files

```markdown
## Files to Read (O(1) - ~200 lines total instead of scanning 1000s)

| File | Purpose | Data Extracted |
|------|---------|----------------|
| `claude-product-cycle/client-layer/FEATURE_MAP.md` | Service/Repo mapping | services[], repositories[] |
| `claude-product-cycle/feature-layer/MODULES_INDEX.md` | Module structure | moduleExists, vmCount, screenCount |
| `claude-product-cycle/feature-layer/SCREENS_INDEX.md` | Screen details | existingScreens[], existingViewModels[] |
| `claude-product-cycle/design-spec-layer/features/[name]/SPEC.md` | Requirements | screens[], actions[], states[] |
| `claude-product-cycle/design-spec-layer/features/[name]/API.md` | Endpoints | endpoints[], dtos[] |
| `claude-product-cycle/testing-layer/TEST_PATTERNS.md` | Test patterns | testPatterns[], conventions |
| `claude-product-cycle/testing-layer/TEST_TAGS_INDEX.md` | TestTag specs | testTags[], namingConvention |
```

### Step 0.2: Build Context Object

```kotlin
// Conceptual context built from O(1) reads
val context = ImplementContext(
    feature = "beneficiary",

    // From FEATURE_MAP.md
    services = ["BeneficiaryService"],
    repositories = ["BeneficiaryRepository"],

    // From MODULES_INDEX.md
    moduleExists = true,
    moduleHasVMs = 4,
    moduleHasScreens = 4,

    // From SPEC.md
    requiredScreens = ["List", "Add", "Edit", "Detail"],
    requiredStates = ["Loading", "Success", "Error", "Empty"],
    requiredActions = ["Retry", "Add", "Edit", "Delete", "Select"],

    // From API.md
    endpoints = [
        GET("/beneficiaries"),
        POST("/beneficiaries"),
        PUT("/beneficiaries/{id}"),
        DELETE("/beneficiaries/{id}")
    ]
)
```

---

## PHASE 1: Pattern Detection

### Step 1.1: Read Reference Files

**Select reference files from existing code:**

```
1. ViewModel Reference:
   feature/home/src/commonMain/.../viewmodel/HomeViewModel.kt

2. Screen Reference:
   feature/home/src/commonMain/.../ui/HomeScreen.kt

3. Repository Reference:
   core/data/src/commonMain/.../repository/HomeRepository.kt
   core/data/src/commonMain/.../repository/HomeRepositoryImp.kt
```

### Step 1.2: Extract Patterns

```kotlin
// Extracted ViewModel Pattern
val vmPattern = ViewModelPattern(
    baseClass = "BaseViewModel<State, Event, Action>",
    stateAnnotation = "@Immutable",
    screenStatePattern = "sealed interface ${Feature}ScreenState",
    eventPattern = "sealed interface ${Feature}Event",
    actionPattern = "sealed interface ${Feature}Action",
    handleActionPattern = "override fun handleAction(action: ${Feature}Action)",
    dataLoadingPattern = "viewModelScope.launch { repository.method().collect { ... } }"
)

// Extracted Screen Pattern
val screenPattern = ScreenPattern(
    koinViewModel = "koinViewModel()",
    stateCollection = "collectAsStateWithLifecycle()",
    eventCollection = "LaunchedEffect(Unit) { viewModel.eventFlow.collect { ... } }",
    contentSeparation = "${Feature}Content(state, onAction)",
    previewAnnotation = "@Preview\n@Composable\nfun ${Feature}Preview()"
)

// Extracted Repository Pattern
val repoPattern = RepositoryPattern(
    returnType = "Flow<DataState<T>>",
    loadingEmit = "emit(DataState.Loading)",
    successEmit = "emit(DataState.Success(data))",
    errorEmit = "emit(DataState.Error(e.message ?: \"Unknown error\"))"
)
```

---

## PHASE 2: Client Layer

### Step 2.1: Check Existing (O(1))

From FEATURE_MAP.md, check if services/repositories exist:

```markdown
## Decision Matrix

| Component | Exists | Action |
|-----------|:------:|--------|
| BeneficiaryService | ✅ | Skip creation |
| BeneficiaryRepository | ✅ | Skip creation |
| NewFeatureService | ❌ | CREATE |
| NewFeatureRepository | ❌ | CREATE |
```

### Step 2.2: Generate Service (if needed)

**Pattern-matched code generation:**

```kotlin
// Generated from SPEC.md + API.md + pattern detection
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

### Step 2.3: Generate Repository (if needed)

**Interface:**
```kotlin
interface ${Feature}Repository {
    fun get${Feature}List(): Flow<DataState<List<${Model}>>>
    fun get${Feature}ById(id: Long): Flow<DataState<${Model}>>
    suspend fun create${Feature}(data: ${Model}): DataState<Unit>
    suspend fun update${Feature}(id: Long, data: ${Model}): DataState<Unit>
    suspend fun delete${Feature}(id: Long): DataState<Unit>
}
```

**Implementation (pattern-matched):**
```kotlin
class ${Feature}RepositoryImp(
    private val service: ${Feature}Service,
) : ${Feature}Repository {

    override fun get${Feature}List(): Flow<DataState<List<${Model}>>> = flow {
        emit(DataState.Loading)
        try {
            val result = service.get${Feature}List().first()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }
    // ... other methods following same pattern
}
```

### Step 2.4: Register DI

**NetworkModule.kt:**
```kotlin
single<${Feature}Service> { get<Ktorfit>().create<${Feature}Service>() }
```

**RepositoryModule.kt:**
```kotlin
single<${Feature}Repository> { ${Feature}RepositoryImp(get()) }
```

### Step 2.5: Build & Verify

```bash
./gradlew :core:network:build :core:data:build
./gradlew spotlessApply --no-configuration-cache
```

---

## PHASE 3: Feature Layer

### Step 3.1: Generate ViewModel (MVI Pattern)

```kotlin
internal class ${Feature}ViewModel(
    private val repository: ${Feature}Repository,
) : BaseViewModel<${Feature}State, ${Feature}Event, ${Feature}Action>(
    initialState = ${Feature}State()
) {

    init {
        load${Feature}()
    }

    override fun handleAction(action: ${Feature}Action) {
        when (action) {
            is ${Feature}Action.Retry -> load${Feature}()
            is ${Feature}Action.OnItemClick -> handleItemClick(action.id)
            // ... from SPEC.md actions
        }
    }

    private fun load${Feature}() {
        viewModelScope.launch {
            repository.get${Feature}List()
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> updateState {
                            it.copy(uiState = ${Feature}ScreenState.Loading)
                        }
                        is DataState.Success -> updateState {
                            it.copy(
                                uiState = ${Feature}ScreenState.Success,
                                data = dataState.data
                            )
                        }
                        is DataState.Error -> updateState {
                            it.copy(uiState = ${Feature}ScreenState.Error(dataState.message))
                        }
                    }
                }
        }
    }
}

// State - from SPEC.md
@Immutable
data class ${Feature}State(
    val data: List<${Item}> = emptyList(),
    val uiState: ${Feature}ScreenState = ${Feature}ScreenState.Loading,
    // ... from SPEC.md state fields
)

sealed interface ${Feature}ScreenState {
    data object Loading : ${Feature}ScreenState
    data object Success : ${Feature}ScreenState
    data class Error(val message: String) : ${Feature}ScreenState
}

// Events - from SPEC.md navigation
sealed interface ${Feature}Event {
    data class NavigateToDetail(val id: Long) : ${Feature}Event
    data object NavigateBack : ${Feature}Event
}

// Actions - from SPEC.md user interactions
sealed interface ${Feature}Action {
    data object Retry : ${Feature}Action
    data class OnItemClick(val id: Long) : ${Feature}Action
    // ... from SPEC.md
}
```

### Step 3.2: Generate Screen with TestTags

```kotlin
// TestTags (generated for testing)
internal object ${Feature}TestTags {
    const val SCREEN = "${feature}:screen"
    const val LOADING = "${feature}:loading"
    const val ERROR = "${feature}:error"
    const val LIST = "${feature}:list"
    const val ITEM_PREFIX = "${feature}:item:"  // + id
    const val RETRY_BUTTON = "${feature}:retry"
    const val ADD_BUTTON = "${feature}:add"
}

@Composable
fun ${Feature}Screen(
    viewModel: ${Feature}ViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ${Feature}Event.NavigateBack -> onNavigateBack()
                is ${Feature}Event.NavigateToDetail -> onNavigateToDetail(event.id)
            }
        }
    }

    ${Feature}Content(
        state = state,
        onAction = viewModel::sendAction,
        modifier = Modifier.testTag(${Feature}TestTags.SCREEN)
    )
}

@Composable
private fun ${Feature}Content(
    state: ${Feature}State,
    onAction: (${Feature}Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.uiState) {
        is ${Feature}ScreenState.Loading -> {
            MifosLoadingWheel(
                modifier = Modifier.testTag(${Feature}TestTags.LOADING)
            )
        }
        is ${Feature}ScreenState.Success -> {
            ${Feature}SuccessContent(
                data = state.data,
                onItemClick = { onAction(${Feature}Action.OnItemClick(it)) },
                modifier = Modifier.testTag(${Feature}TestTags.LIST)
            )
        }
        is ${Feature}ScreenState.Error -> {
            MifosErrorContent(
                message = state.uiState.message,
                onRetry = { onAction(${Feature}Action.Retry) },
                modifier = Modifier.testTag(${Feature}TestTags.ERROR)
            )
        }
    }
}
```

### Step 3.3: Generate Navigation

```kotlin
fun NavGraphBuilder.${feature}Screen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    composable<${Feature}Route> {
        ${Feature}Screen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

@Serializable
data object ${Feature}Route
```

### Step 3.4: Generate DI Module

```kotlin
val ${feature}Module = module {
    viewModelOf(::${Feature}ViewModel)
}
```

### Step 3.5: Build & Verify

```bash
./gradlew :feature:${name}:build
./gradlew spotlessApply detekt --no-configuration-cache
```

---

## PHASE 4: Finalize

### Step 4.1: Update O(1) Index Files

**FEATURE_MAP.md:**
```markdown
| ${feature} | ${Service} | ${Repository} | Notes |
```

**MODULES_INDEX.md:**
```markdown
| ${n} | ${module} | feature/${module} | ✅ | ${vmCount} | ${screenCount} |
```

**SCREENS_INDEX.md:**
```markdown
### ${module} (${screenCount} screens)

| Screen | ViewModel | File |
|--------|-----------|------|
| ${Screen}Screen | ${Screen}ViewModel | ui/${Screen}Screen.kt |
```

### Step 4.2: Update STATUS.md Files

**Feature STATUS.md:**
```markdown
| Component | Status | Notes |
|-----------|:------:|-------|
| Client Layer | ✅ | Service + Repository |
| Feature Layer | ✅ | ViewModel + Screen |
| Navigation | ✅ | Registered |
| DI | ✅ | Module registered |
```

### Step 4.3: Final Build

```bash
./gradlew build
git add .
git commit -m "feat(${feature}): complete E2E implementation"
```

---

## PHASE 5: Test Stub Generation (TDD Support)

### Step 5.1: Generate ViewModel Test Stub

**Location**: `feature/${name}/src/commonTest/kotlin/.../viewmodel/${Feature}ViewModelTest.kt`

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.testing.fake.Fake${Feature}Repository
import org.mifos.mobile.core.testing.rule.MainDispatcherRule
import org.mifos.mobile.feature.${package}.${Feature}ScreenState
import org.mifos.mobile.feature.${package}.${Feature}Action
import org.mifos.mobile.feature.${package}.${Feature}Event
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * ViewModel tests for ${Feature}
 *
 * Generated by /implement command
 * Run: ./gradlew :feature:${name}:test
 */
class ${Feature}ViewModelTest {

    private val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ${Feature}ViewModel
    private lateinit var fakeRepository: Fake${Feature}Repository

    @BeforeTest
    fun setup() {
        fakeRepository = Fake${Feature}Repository()
        viewModel = ${Feature}ViewModel(
            repository = fakeRepository,
        )
    }

    // ===========================================
    // INITIAL STATE TESTS
    // ===========================================

    @Test
    fun `initial state is loading`() = runTest {
        viewModel.stateFlow.test {
            val state = awaitItem()
            assertTrue(state.uiState is ${Feature}ScreenState.Loading)
        }
    }

    // ===========================================
    // DATA LOADING TESTS
    // ===========================================

    @Test
    fun `when data loads successfully, state is success`() = runTest {
        // Given
        fakeRepository.setSuccessResponse(/* test data */)

        // When
        viewModel.handleAction(${Feature}Action.Retry)

        // Then
        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}ScreenState.Success)
        }
    }

    @Test
    fun `when data load fails, state is error`() = runTest {
        // Given
        fakeRepository.setErrorResponse("Network error")

        // When
        viewModel.handleAction(${Feature}Action.Retry)

        // Then
        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is ${Feature}ScreenState.Error)
        }
    }

    @Test
    fun `when data is empty, state is empty`() = runTest {
        // Given
        fakeRepository.setEmptyResponse()

        // When
        viewModel.handleAction(${Feature}Action.Retry)

        // Then
        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            // TODO: Check for Empty state if defined
        }
    }

    // ===========================================
    // ACTION TESTS
    // ===========================================

    @Test
    fun `retry action triggers reload`() = runTest {
        // Given
        fakeRepository.setSuccessResponse(/* test data */)

        // When
        viewModel.handleAction(${Feature}Action.Retry)

        // Then
        assertEquals(1, fakeRepository.loadCallCount)
    }

    // TODO: Add more action tests based on SPEC.md
    // @Test fun `onItemClick action triggers navigation event`()
    // @Test fun `onAddClick action triggers navigate to add`()

    // ===========================================
    // EVENT TESTS
    // ===========================================

    @Test
    fun `item click emits navigation event`() = runTest {
        viewModel.eventFlow.test {
            // When
            viewModel.handleAction(${Feature}Action.OnItemClick(itemId = 1L))

            // Then
            val event = awaitItem()
            assertTrue(event is ${Feature}Event.NavigateToDetail)
        }
    }
}
```

### Step 5.2: Generate Screen Test Stub

**Location**: `feature/${name}/src/androidInstrumentedTest/kotlin/.../ui/${Feature}ScreenTest.kt`

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.mifos.mobile.feature.${package}.${Feature}State
import org.mifos.mobile.feature.${package}.${Feature}ScreenState
import org.mifos.mobile.feature.${package}.${Feature}Action

/**
 * UI tests for ${Feature}Screen
 *
 * Generated by /implement command
 * Run: ./gradlew :feature:${name}:connectedDebugAndroidTest
 */
class ${Feature}ScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ===========================================
    // LOADING STATE TESTS
    // ===========================================

    @Test
    fun loadingState_displaysLoadingIndicator() {
        // Given
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Loading
        )

        // When
        composeTestRule.setContent {
            ${Feature}Content(
                state = state,
                onAction = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LOADING)
            .assertIsDisplayed()
    }

    // ===========================================
    // SUCCESS STATE TESTS
    // ===========================================

    @Test
    fun successState_displaysList() {
        // Given
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Success,
            data = listOf(/* test data */)
        )

        // When
        composeTestRule.setContent {
            ${Feature}Content(
                state = state,
                onAction = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LIST)
            .assertIsDisplayed()
    }

    @Test
    fun successState_itemClickTriggersAction() {
        // Given
        var actionReceived: ${Feature}Action? = null
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Success,
            data = listOf(/* test data with id=1 */)
        )

        // When
        composeTestRule.setContent {
            ${Feature}Content(
                state = state,
                onAction = { actionReceived = it }
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:item:1")
            .performClick()

        // Then
        assertTrue(actionReceived is ${Feature}Action.OnItemClick)
    }

    // ===========================================
    // ERROR STATE TESTS
    // ===========================================

    @Test
    fun errorState_displaysErrorMessage() {
        // Given
        val errorMessage = "Network error"
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Error(errorMessage)
        )

        // When
        composeTestRule.setContent {
            ${Feature}Content(
                state = state,
                onAction = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithTag(${Feature}TestTags.ERROR)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_retryButtonTriggersAction() {
        // Given
        var actionReceived: ${Feature}Action? = null
        val state = ${Feature}State(
            uiState = ${Feature}ScreenState.Error("Error")
        )

        // When
        composeTestRule.setContent {
            ${Feature}Content(
                state = state,
                onAction = { actionReceived = it }
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.RETRY_BUTTON)
            .performClick()

        // Then
        assertEquals(${Feature}Action.Retry, actionReceived)
    }

    // ===========================================
    // EMPTY STATE TESTS (if applicable)
    // ===========================================

    // TODO: Add empty state test if defined in SPEC.md
    // @Test fun emptyState_displaysEmptyIllustration()
}
```

### Step 5.3: Generate Fake Repository

**Location**: `core/testing/src/commonMain/kotlin/.../fake/Fake${Feature}Repository.kt`

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.core.testing.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.${Feature}Repository
import org.mifos.mobile.core.model.entity.${Model}

/**
 * Fake repository for testing ${Feature}
 *
 * Generated by /implement command
 */
class Fake${Feature}Repository : ${Feature}Repository {

    // Track call counts for verification
    var loadCallCount = 0
        private set

    // Configurable responses
    private var response: DataState<List<${Model}>> = DataState.Loading

    fun setSuccessResponse(data: List<${Model}>) {
        response = DataState.Success(data)
    }

    fun setErrorResponse(message: String) {
        response = DataState.Error(message)
    }

    fun setEmptyResponse() {
        response = DataState.Success(emptyList())
    }

    fun reset() {
        loadCallCount = 0
        response = DataState.Loading
    }

    // Repository implementation
    override fun get${Feature}List(): Flow<DataState<List<${Model}>>> = flow {
        loadCallCount++
        emit(DataState.Loading)
        emit(response)
    }

    override fun get${Feature}ById(id: Long): Flow<DataState<${Model}>> = flow {
        emit(DataState.Loading)
        when (val currentResponse = response) {
            is DataState.Success -> {
                val item = currentResponse.data.find { it.id == id }
                if (item != null) {
                    emit(DataState.Success(item))
                } else {
                    emit(DataState.Error("Item not found"))
                }
            }
            is DataState.Error -> emit(DataState.Error(currentResponse.message))
            is DataState.Loading -> emit(DataState.Loading)
        }
    }

    override suspend fun create${Feature}(data: ${Model}): DataState<Unit> {
        return DataState.Success(Unit)
    }

    override suspend fun update${Feature}(id: Long, data: ${Model}): DataState<Unit> {
        return DataState.Success(Unit)
    }

    override suspend fun delete${Feature}(id: Long): DataState<Unit> {
        return DataState.Success(Unit)
    }
}
```

### Step 5.4: Update TESTING_STATUS.md

Add entry to `feature-layer/TESTING_STATUS.md`:

```markdown
| ${feature} | ${vmCount} | ${vmCount} | ${screenCount} | 0 | Stubs |
```

### TEST Checkpoint

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  🧪 TEST STUBS GENERATED (Phase 5)                                           │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  📁 Files Created:                                                           │
│  ├─ feature/${name}/src/commonTest/.../viewmodel/${Feature}ViewModelTest.kt  │
│  ├─ feature/${name}/src/androidInstrumentedTest/.../ui/${Feature}ScreenTest.kt│
│  └─ core/testing/src/commonMain/.../fake/Fake${Feature}Repository.kt         │
│                                                                               │
│  🧪 Test Stubs Generated:                                                    │
│  ├─ ViewModel tests: [n] stubs (initial, loading, success, error, actions)  │
│  ├─ Screen tests: [n] stubs (loading, success, error, interactions)         │
│  └─ Fake repository: Full implementation for testing                        │
│                                                                               │
│  📊 TestTags Used:                                                           │
│  ├─ ${feature}:screen                                                        │
│  ├─ ${feature}:loading                                                       │
│  ├─ ${feature}:error                                                         │
│  ├─ ${feature}:list                                                          │
│  ├─ ${feature}:item:{id}                                                     │
│  └─ ${feature}:retry                                                         │
│                                                                               │
│  📋 Next Steps:                                                              │
│  1. Run tests: ./gradlew :feature:${name}:test                              │
│  2. Fill in TODO comments with actual test data                             │
│  3. Add more tests based on SPEC.md requirements                            │
│                                                                               │
├──────────────────────────────────────────────────────────────────────────────┤
│  Options:                                                                     │
│  • c / continue  → Finalize implementation                                   │
│  • r / run       → Run generated tests                                       │
│  • v / view [file] → View specific test file                                 │
│  • s / skip      → Skip test generation                                      │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Checkpoint Templates

### CLIENT Checkpoint

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ✅ CLIENT LAYER COMPLETE (Phase 2)                                          │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  📚 O(1) Context Used:                                                        │
│  ├─ FEATURE_MAP.md → Identified existing: [services], [repos]                │
│  └─ API.md → Mapped [n] endpoints                                            │
│                                                                               │
│  🔧 Created/Updated:                                                          │
│  ├─ core/network/services/${Feature}Service.kt     [CREATED|UPDATED|SKIPPED] │
│  ├─ core/data/repository/${Feature}Repository.kt   [CREATED|UPDATED|SKIPPED] │
│  ├─ core/data/repository/${Feature}RepositoryImp.kt                          │
│  └─ DI Modules                                     [REGISTERED]              │
│                                                                               │
│  📊 Pattern Matching:                                                         │
│  └─ Applied patterns from: HomeRepositoryImp.kt                              │
│                                                                               │
│  🔨 BUILD: :core:network ✅ :core:data ✅                                     │
│  🧹 LINT: spotlessApply ✅                                                    │
│                                                                               │
├──────────────────────────────────────────────────────────────────────────────┤
│  Options:                                                                     │
│  • c / continue  → Proceed to FEATURE layer                                  │
│  • i / improve   → Describe improvements                                     │
│  • v / view      → Show generated file                                       │
└──────────────────────────────────────────────────────────────────────────────┘
```

### FEATURE Checkpoint

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ✅ FEATURE LAYER COMPLETE (Phase 3)                                         │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  📚 O(1) Context Used:                                                        │
│  ├─ MODULES_INDEX.md → Verified module structure                             │
│  ├─ SCREENS_INDEX.md → Identified [n] existing screens                       │
│  └─ SPEC.md → Mapped [n] screens, [n] states, [n] actions                    │
│                                                                               │
│  🔧 Created/Updated:                                                          │
│  ├─ feature/${name}/viewmodel/${Feature}ViewModel.kt                         │
│  ├─ feature/${name}/ui/${Feature}Screen.kt                                   │
│  ├─ feature/${name}/navigation/${Feature}Navigation.kt                       │
│  └─ feature/${name}/di/${Feature}Module.kt                                   │
│                                                                               │
│  🏷️ TestTags Generated:                                                       │
│  ├─ ${feature}:screen                                                         │
│  ├─ ${feature}:loading                                                        │
│  ├─ ${feature}:error                                                          │
│  └─ ${feature}:list                                                           │
│                                                                               │
│  📊 Pattern Matching:                                                         │
│  └─ Applied patterns from: HomeViewModel.kt, HomeScreen.kt                   │
│                                                                               │
│  🔨 BUILD: :feature:${name} ✅                                                │
│  🧹 LINT: spotlessApply ✅ detekt ✅                                          │
│                                                                               │
├──────────────────────────────────────────────────────────────────────────────┤
│  Options:                                                                     │
│  • c / continue  → Finalize and update indexes                               │
│  • i / improve   → Describe improvements                                     │
│  • v / view [file] → Show specific file                                      │
│  • t / test      → Show generated TestTags                                   │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Final Report

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║  /implement ${Feature} - COMPLETE                                             ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  📊 O(1) OPTIMIZATION METRICS                                                  ║
║  ├─ Index files read: 4 files (~200 lines)                                    ║
║  ├─ Directory scans avoided: ~50                                               ║
║  └─ Pattern files read: 3 references                                           ║
║                                                                                ║
║  ✅ PHASE 0: CONTEXT LOADING                                                   ║
║     └─ Loaded from: FEATURE_MAP, MODULES_INDEX, SCREENS_INDEX, SPEC, API      ║
║                                                                                ║
║  ✅ PHASE 1: PATTERN DETECTION                                                 ║
║     └─ Patterns from: HomeViewModel, HomeScreen, HomeRepository               ║
║                                                                                ║
║  ✅ PHASE 2: CLIENT LAYER                                                      ║
║     ├─ Files: [n] created, [n] updated                                        ║
║     └─ Build: :core:network ✅ :core:data ✅                                   ║
║                                                                                ║
║  ✅ PHASE 3: FEATURE LAYER                                                     ║
║     ├─ Files: [n] created                                                     ║
║     ├─ TestTags: [n] generated                                                ║
║     └─ Build: :feature:${name} ✅                                             ║
║                                                                                ║
║  ✅ PHASE 4: FINALIZE                                                          ║
║     ├─ Updated: FEATURE_MAP.md, MODULES_INDEX.md, SCREENS_INDEX.md           ║
║     └─ Final Build: ./gradlew build ✅                                        ║
║                                                                                ║
║  ✅ PHASE 5: TEST STUBS                                                        ║
║     ├─ ViewModel test: ${Feature}ViewModelTest.kt ✅                          ║
║     ├─ Screen test: ${Feature}ScreenTest.kt ✅                                ║
║     ├─ Fake repository: Fake${Feature}Repository.kt ✅                        ║
║     └─ Updated: TESTING_STATUS.md ✅                                          ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  📊 SUMMARY                                                                    ║
║  ├─ Files Created: [n]                                                        ║
║  ├─ Files Updated: [n]                                                        ║
║  ├─ TestTags Generated: [n]                                                   ║
║  ├─ Test Stubs Generated: [n]                                                 ║
║  └─ Index Files Updated: 4 (incl. TESTING_STATUS)                            ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  🎉 IMPLEMENTATION COMPLETE                                                    ║
║                                                                                ║
║  Next steps:                                                                   ║
║  • Verify: /verify ${Feature}                                                 ║
║  • Test: /verify-tests ${Feature}                                             ║
║  • Push: git push -u origin feature/${feature}                                ║
║                                                                                ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## Feature List (No Argument)

When `/implement` called without arguments, read MODULES_INDEX.md:

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  📋 FEATURES - Implementation Status (from MODULES_INDEX.md)                  │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  | # | Feature         | Client | Feature | Gaps | Command               │   │
│  |:-:|-----------------|:------:|:-------:|:----:|----------------------|   │
│  | 1 | auth            | ✅     | ✅      | 0    | /implement auth      │   │
│  | 2 | home            | ✅     | ✅      | 0    | /implement home      │   │
│  | 3 | accounts        | ✅     | ✅      | 0    | /implement accounts  │   │
│  | 4 | beneficiary     | ✅     | ✅      | 0    | /implement beneficiary│  │
│  | 5 | transfer        | ✅     | ✅      | 0    | /implement transfer  │   │
│  | ...                                                                       │
│                                                                               │
│  Which feature? (Or type feature name directly)                              │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Error Handling

### Build Failure

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ❌ BUILD FAILED                                                              │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Module: :core:network                                                        │
│  Error: Unresolved reference: ApiEndPoints.BENEFICIARIES                     │
│                                                                               │
│  📍 Auto-Fix Suggestion:                                                      │
│  Add to core/network/ApiEndPoints.kt:                                        │
│  const val BENEFICIARIES = "beneficiaries"                                   │
│                                                                               │
│  Options:                                                                     │
│  • f / fix    → Apply auto-fix and rebuild                                   │
│  • m / manual → Show what to fix manually                                    │
│  • a / abort  → Stop implementation                                          │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Related Commands

| Command | Purpose |
|---------|---------|
| `/client [Feature]` | Client layer only |
| `/feature [Feature]` | Feature layer only |
| `/verify [Feature]` | Verify implementation vs spec |
| `/verify-tests [Feature]` | Run tests |
| `/gap-analysis` | Check what needs implementation |

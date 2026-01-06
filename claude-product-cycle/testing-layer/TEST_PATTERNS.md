# Test Patterns - O(1) Reference

> Quick lookup for test patterns used in Mifos Mobile

---

## Pattern Quick Reference

| # | Pattern | Use Case | Location | Details |
|:-:|---------|----------|----------|---------|
| 1 | ViewModel Test | Test state, actions, events | `commonTest/` | [viewmodel-test.md](./patterns/viewmodel-test.md) |
| 2 | Screen Test | Test UI composition | `androidInstrumentedTest/` | [screen-test.md](./patterns/screen-test.md) |
| 3 | Fake Repository | Test isolation | `commonTest/fake/` | [fake-repository.md](./patterns/fake-repository.md) |
| 4 | Integration Test | Test user flows | `cmp-android/androidTest/` | [integration-test.md](./patterns/integration-test.md) |
| 5 | Screenshot Test | Visual regression | `test/` (Roborazzi) | [screenshot-test.md](./patterns/screenshot-test.md) |

---

## 1. ViewModel Test Pattern

### When to Use
- Testing state transitions (Loading → Success → Error)
- Testing action handling
- Testing event emission (navigation, dialogs)

### Quick Template

```kotlin
class ${Feature}ViewModelTest {
    private val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: ${Feature}ViewModel
    private lateinit var fakeRepository: Fake${Feature}Repository

    @BeforeTest
    fun setup() {
        fakeRepository = Fake${Feature}Repository()
        viewModel = ${Feature}ViewModel(repository = fakeRepository)
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel.stateFlow.test {
            assertTrue(awaitItem().uiState is Loading)
        }
    }

    @Test
    fun `load success updates state`() = runTest {
        fakeRepository.setSuccessResponse(testData)
        viewModel.loadData()

        viewModel.stateFlow.test {
            assertTrue(expectMostRecentItem().uiState is Success)
        }
    }
}
```

### Key Libraries
| Library | Import | Purpose |
|---------|--------|---------|
| Turbine | `app.cash.turbine.test` | Flow testing |
| Coroutines Test | `kotlinx.coroutines.test.runTest` | Coroutine testing |
| kotlin-test | `kotlin.test.*` | Assertions |

---

## 2. Screen Test Pattern

### When to Use
- Testing UI renders correctly for each state
- Testing user interactions trigger correct actions
- Testing accessibility (content descriptions)

### Quick Template

```kotlin
class ${Feature}ScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_displaysLoader() {
        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(uiState = Loading),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:loading")
            .assertIsDisplayed()
    }

    @Test
    fun itemClick_triggersAction() {
        var receivedAction: ${Feature}Action? = null

        composeTestRule.setContent {
            ${Feature}Content(
                state = successState,
                onAction = { receivedAction = it }
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:item:1")
            .performClick()

        assertEquals(${Feature}Action.ItemClicked(1), receivedAction)
    }
}
```

### Key Methods
| Method | Purpose |
|--------|---------|
| `onNodeWithTag(tag)` | Find by testTag |
| `onNodeWithText(text)` | Find by text |
| `assertIsDisplayed()` | Verify visible |
| `assertIsEnabled()` | Verify clickable |
| `performClick()` | Simulate tap |
| `performTextInput(text)` | Type text |

---

## 3. Fake Repository Pattern

### When to Use
- Isolating ViewModel from real data source
- Testing different response scenarios
- Verifying repository method calls

### Quick Template

```kotlin
class Fake${Feature}Repository : ${Feature}Repository {
    var loadCallCount = 0
        private set

    private var response: DataState<List<${Model}>> = DataState.Loading

    fun setSuccessResponse(data: List<${Model}>) {
        response = DataState.Success(data)
    }

    fun setErrorResponse(message: String) {
        response = DataState.Error(message)
    }

    override fun getData(): Flow<DataState<List<${Model}>>> = flow {
        loadCallCount++
        emit(response)
    }

    fun reset() {
        loadCallCount = 0
        response = DataState.Loading
    }
}
```

### Naming Convention
| Real | Fake |
|------|------|
| `BeneficiaryRepository` | `FakeBeneficiaryRepository` |
| `HomeRepository` | `FakeHomeRepository` |
| `LoanRepository` | `FakeLoanRepository` |

---

## 4. Integration Test Pattern

### When to Use
- Testing complete user flows
- Testing navigation between screens
- Testing data persistence across screens

### Quick Template

```kotlin
@HiltAndroidTest
class ${Feature}FlowTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginFlow_navigatesToHome() {
        // Enter credentials
        composeTestRule
            .onNodeWithTag("auth:input:username")
            .performTextInput("testuser")

        composeTestRule
            .onNodeWithTag("auth:input:password")
            .performTextInput("password123")

        // Click login
        composeTestRule
            .onNodeWithTag("auth:submit")
            .performClick()

        // Verify navigation to home
        composeTestRule
            .onNodeWithTag("home:screen")
            .assertIsDisplayed()
    }
}
```

### Critical Flows
| Flow | Screens | Priority |
|------|---------|:--------:|
| Login → Passcode → Home | 3 | P0 |
| Home → Transfer → Confirm | 3 | P0 |
| Home → Loan Details → Schedule | 3 | P1 |
| Settings → Change Password | 2 | P2 |

---

## 5. Screenshot Test Pattern

### When to Use
- Visual regression testing
- Documenting UI states
- Catching unintended UI changes

### Quick Template

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ${Feature}ScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.LastImage
        )
    )

    @Test
    fun ${feature}Screen_loading() {
        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(uiState = Loading),
                    onAction = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
```

### Golden Image Location
```
feature/${feature}/src/test/resources/screenshots/
├── ${feature}Screen_loading.png
├── ${feature}Screen_success.png
├── ${feature}Screen_error.png
└── ${feature}Screen_empty.png
```

---

## Test State Categories

Every feature should test these states:

| State | Description | TestTag |
|-------|-------------|---------|
| Loading | Initial data fetch | `{feature}:loading` |
| Success | Data loaded | `{feature}:screen` |
| Error | Load failed | `{feature}:error` |
| Empty | No data | `{feature}:empty` |
| Refreshing | Pull-to-refresh | `{feature}:refreshing` |

---

## Common Test Scenarios

### Pagination
```kotlin
@Test
fun `load more appends data`() = runTest {
    fakeRepository.setSuccessResponse(page1)
    viewModel.loadData()

    fakeRepository.setSuccessResponse(page2)
    viewModel.trySendAction(Action.LoadMore)

    viewModel.stateFlow.test {
        val data = (expectMostRecentItem().uiState as Success).data
        assertEquals(page1 + page2, data)
    }
}
```

### Pull-to-Refresh
```kotlin
@Test
fun `refresh replaces data`() = runTest {
    fakeRepository.setSuccessResponse(oldData)
    viewModel.loadData()

    fakeRepository.setSuccessResponse(newData)
    viewModel.trySendAction(Action.Refresh)

    viewModel.stateFlow.test {
        val data = (expectMostRecentItem().uiState as Success).data
        assertEquals(newData, data)
    }
}
```

### Form Validation
```kotlin
@Test
fun `invalid input shows error`() = runTest {
    viewModel.trySendAction(Action.Submit(invalidInput))

    viewModel.stateFlow.test {
        assertNotNull(expectMostRecentItem().validationError)
    }
}
```

### Dialog Confirmation
```kotlin
@Test
fun `delete shows confirmation`() = runTest {
    viewModel.trySendAction(Action.DeleteClicked(id))

    viewModel.stateFlow.test {
        assertTrue(expectMostRecentItem().dialogState is DialogState.Confirmation)
    }
}
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
            implementation(libs.turbine)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.roborazzi)
                implementation(libs.robolectric)
            }
        }
    }
}
```

---

## Commands

```bash
# Generate tests using pattern
/implement [feature]           # Uses patterns from this file

# Verify pattern compliance
/verify [feature]              # Checks TestTag patterns

# See detailed pattern
Read testing-layer/patterns/[pattern].md
```

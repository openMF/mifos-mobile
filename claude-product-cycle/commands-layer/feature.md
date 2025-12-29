# /feature - Feature/UI Layer Implementation

## Purpose
Implement the feature/UI layer including ViewModel, Screen, Components, and Navigation.

---

## Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                    /feature [Feature] WORKFLOW                     │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  STEP 1: READ SPEC                                                │
│  ├─→ Read features/[feature]/SPEC.md                              │
│  ├─→ Extract UI sections, state model, user actions               │
│  └─→ Read _shared/PATTERNS.md for MVI pattern                     │
│                                                                    │
│  STEP 2: CHECK PREREQUISITES                                      │
│  ├─→ Verify client layer exists (Repository)                      │
│  └─→ If missing, suggest: /client [Feature] first                 │
│                                                                    │
│  STEP 3: CREATE VIEWMODEL                                         │
│  ├─→ Define State data class                                      │
│  ├─→ Define Event sealed interface                                │
│  ├─→ Define Action sealed interface                               │
│  ├─→ Implement handleAction()                                      │
│  └─→ Implement data loading logic                                 │
│                                                                    │
│  STEP 4: CREATE SCREEN                                            │
│  ├─→ Create main Screen composable                                │
│  ├─→ Handle state rendering                                       │
│  ├─→ Handle event collection                                      │
│  └─→ Connect to ViewModel actions                                 │
│                                                                    │
│  STEP 5: CREATE COMPONENTS                                        │
│  ├─→ Extract reusable components                                  │
│  └─→ Add @Preview annotations                                     │
│                                                                    │
│  STEP 6: CREATE NAVIGATION                                        │
│  ├─→ Define navigation route                                      │
│  └─→ Register in navigation graph                                 │
│                                                                    │
│  STEP 7: REGISTER DI                                              │
│  ├─→ Create [Feature]Module.kt                                    │
│  └─→ Register ViewModel                                           │
│                                                                    │
│  STEP 8: BUILD & VERIFY                                           │
│  ├─→ ./gradlew :feature:[name]:build                              │
│  └─→ ./gradlew spotlessApply detekt                               │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## File Locations

```
feature/[name]/src/commonMain/kotlin/org/mifos/mobile/feature/[name]/
├── [Feature]ViewModel.kt           # MVI ViewModel
├── [Feature]Screen.kt              # Main screen composable
├── components/                      # UI components
│   └── [Component].kt
├── navigation/
│   └── [Feature]Navigation.kt      # Navigation definition
└── di/
    └── [Feature]Module.kt          # Koin module
```

---

## ViewModel Pattern (MVI)

```kotlin
internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
) : BaseViewModel<[Feature]State, [Feature]Event, [Feature]Action>(
    initialState = [Feature]State()
) {

    init {
        loadData()
    }

    override fun handleAction(action: [Feature]Action) {
        when (action) {
            is [Feature]Action.Retry -> loadData()
            is [Feature]Action.OnItemClick -> handleItemClick(action.id)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getData()
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> updateState {
                            it.copy(uiState = [Feature]ScreenState.Loading)
                        }
                        is DataState.Success -> updateState {
                            it.copy(
                                uiState = [Feature]ScreenState.Success,
                                data = dataState.data
                            )
                        }
                        is DataState.Error -> updateState {
                            it.copy(uiState = [Feature]ScreenState.Error(dataState.message))
                        }
                    }
                }
        }
    }

    private fun handleItemClick(id: Long) {
        sendEvent([Feature]Event.NavigateToDetail(id))
    }
}

// State
@Immutable
data class [Feature]State(
    val data: List<Item> = emptyList(),
    val uiState: [Feature]ScreenState = [Feature]ScreenState.Loading,
)

sealed interface [Feature]ScreenState {
    data object Loading : [Feature]ScreenState
    data object Success : [Feature]ScreenState
    data class Error(val message: String) : [Feature]ScreenState
}

// Events (one-time navigation/toasts)
sealed interface [Feature]Event {
    data class NavigateToDetail(val id: Long) : [Feature]Event
    data object NavigateBack : [Feature]Event
}

// Actions (user interactions)
sealed interface [Feature]Action {
    data object Retry : [Feature]Action
    data class OnItemClick(val id: Long) : [Feature]Action
}
```

---

## Screen Pattern

```kotlin
@Composable
fun [Feature]Screen(
    viewModel: [Feature]ViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is [Feature]Event.NavigateBack -> onNavigateBack()
                is [Feature]Event.NavigateToDetail -> onNavigateToDetail(event.id)
            }
        }
    }

    [Feature]Content(
        state = state,
        onAction = viewModel::sendAction,
    )
}

@Composable
private fun [Feature]Content(
    state: [Feature]State,
    onAction: ([Feature]Action) -> Unit,
) {
    when (state.uiState) {
        is [Feature]ScreenState.Loading -> LoadingContent()
        is [Feature]ScreenState.Success -> SuccessContent(
            data = state.data,
            onItemClick = { onAction([Feature]Action.OnItemClick(it)) }
        )
        is [Feature]ScreenState.Error -> ErrorContent(
            message = state.uiState.message,
            onRetry = { onAction([Feature]Action.Retry) }
        )
    }
}
```

---

## Navigation Pattern

```kotlin
// Navigation definition
fun NavGraphBuilder.[feature]Screen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    composable<[Feature]Route> {
        [Feature]Screen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

// Route
@Serializable
data object [Feature]Route
```

---

## DI Module Pattern

```kotlin
val [feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

---

## Output Template

```
┌──────────────────────────────────────────────────────────────────────┐
│  ✅ FEATURE LAYER COMPLETE                                           │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  Created/Updated:                                                     │
│  ├─ feature/[name]/[Feature]ViewModel.kt                             │
│  ├─ feature/[name]/[Feature]Screen.kt                                │
│  ├─ feature/[name]/components/*.kt                                   │
│  ├─ feature/[name]/navigation/[Feature]Navigation.kt                 │
│  └─ feature/[name]/di/[Feature]Module.kt                             │
│                                                                       │
│  Navigation:                                                          │
│  └─ Route registered ✅                                               │
│                                                                       │
│  🔨 BUILD: :feature:[name] ✅                                         │
│  🧹 LINT: spotlessApply ✅ detekt ✅                                  │
│                                                                       │
├──────────────────────────────────────────────────────────────────────┤
│  NEXT STEP:                                                           │
│  Run:  /verify [Feature]                                             │
└──────────────────────────────────────────────────────────────────────┘
```

# Feature Layer Guide

> **Location**: `feature/`
> **Command**: `/feature [Feature]`

---

## Overview

The feature layer contains UI modules with ViewModel + Screen (MVI pattern):

```
┌─────────────────────────────────────────────────────────────────────┐
│  FEATURE LAYER (feature/)                                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  feature/[name]/                                                     │
│  ├── [Name]ViewModel.kt     → MVI (State, Event, Action)            │
│  ├── [Name]Screen.kt        → Compose UI                            │
│  ├── navigation/            → Navigation routes                     │
│  ├── components/            → Feature-specific components           │
│  └── di/[Name]Module.kt     → Koin registration                     │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Directory Structure

```
feature/
├── home/
│   └── src/commonMain/kotlin/.../feature/home/
│       ├── HomeScreen.kt
│       ├── HomeViewModel.kt
│       ├── navigation/
│       │   └── HomeNavigation.kt
│       ├── components/
│       │   └── BottomSheetContent.kt
│       └── di/
│           └── HomeModule.kt
│
├── accounts/
├── auth/
├── beneficiary/
├── client-charge/
├── guarantor/
├── loan-account/
├── loan-application/
├── location/
├── notification/
├── onboarding-language/
├── passcode/
├── qr/
├── recent-transaction/
├── savings-account/
├── savings-application/
├── settings/
├── share-account/
├── share-application/
├── status/
├── third-party-transfer/
└── transfer-process/
```

---

## MVI Architecture

```kotlin
// State - UI state (immutable)
@Immutable
data class [Feature]State(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null,
)

// ScreenState - Loading/Success/Error states
sealed interface [Feature]ScreenState {
    data object Loading : [Feature]ScreenState
    data object Success : [Feature]ScreenState
    data class Error(val message: StringResource) : [Feature]ScreenState
}

// Event - One-shot navigation/effects
sealed interface [Feature]Event {
    data class NavigateToDetail(val id: Long) : [Feature]Event
    data object NavigateBack : [Feature]Event
}

// Action - User interactions
sealed interface [Feature]Action {
    data class OnItemClick(val id: Long) : [Feature]Action
    data object OnRefresh : [Feature]Action

    // Internal actions for handling async results
    sealed interface Internal : [Feature]Action {
        data class ReceiveData(val dataState: DataState<Data>) : Internal
    }
}

// ViewModel
internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
) : BaseViewModel<[Feature]State, [Feature]Event, [Feature]Action>(
    initialState = [Feature]State(),
) {
    override fun handleAction(action: [Feature]Action) {
        when (action) {
            is [Feature]Action.OnItemClick ->
                sendEvent([Feature]Event.NavigateToDetail(action.id))
            is [Feature]Action.Internal.ReceiveData ->
                handleDataResult(action.dataState)
        }
    }
}
```

---

## Screen Pattern

```kotlin
@Composable
internal fun [Feature]Screen(
    navigateToDetail: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: [Feature]ViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel) { event ->
        when (event) {
            is [Feature]Event.NavigateToDetail -> navigateToDetail(event.id)
            [Feature]Event.NavigateBack -> navigateBack()
        }
    }

    [Feature]ScreenContent(
        state = state,
        onAction = viewModel::trySendAction,
        modifier = modifier,
    )
}

@Composable
private fun [Feature]ScreenContent(
    state: [Feature]State,
    onAction: ([Feature]Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    // UI implementation
}
```

---

## Navigation Pattern

```kotlin
// feature/[name]/navigation/[Feature]Navigation.kt

const val [FEATURE]_NAVIGATION_ROUTE = "[feature]_route"
const val [FEATURE]_SCREEN_ROUTE = "[feature]_screen"

fun NavController.navigateTo[Feature]Screen() {
    navigate([FEATURE]_SCREEN_ROUTE)
}

fun NavGraphBuilder.[feature]NavGraph(
    navigateBack: () -> Unit,
    navigateToDetail: (Long) -> Unit,
) {
    navigation(
        startDestination = [FEATURE]_SCREEN_ROUTE,
        route = [FEATURE]_NAVIGATION_ROUTE,
    ) {
        composable(route = [FEATURE]_SCREEN_ROUTE) {
            [Feature]Screen(
                navigateBack = navigateBack,
                navigateToDetail = navigateToDetail,
            )
        }
    }
}
```

---

## Implementation Flow

```
/feature [Feature]
    │
    ├── 1. Create State, ScreenState, Event, Action
    │
    ├── 2. Create ViewModel
    │   └── Inject Repositories from data layer
    │
    ├── 3. Create Screen
    │   ├── Use EventsEffect for navigation events
    │   └── Use collectAsStateWithLifecycle for state
    │
    ├── 4. Create Components (if needed)
    │
    ├── 5. Create [Feature]Module.kt
    │   └── viewModelOf(::[Feature]ViewModel)
    │
    ├── 6. Create Navigation
    │   └── NavGraphBuilder extension functions
    │
    └── 7. Register in cmp-navigation
        ├── Add to KoinModules.kt
        └── Add to navigation graph
```

---

## Koin Module Pattern

```kotlin
// feature/[name]/di/[Feature]Module.kt
val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

---

## Build Command

```bash
./gradlew :feature:[name]:build
```

---

## Cross-Update Rules (MANDATORY)

### String Resources - No Hardcoded Strings

**NEVER use hardcoded strings or `String.format()` in feature code.**

All user-facing strings MUST be defined in `composeResources/values/strings.xml` and accessed via `stringResource()`.

```kotlin
// WRONG - Hardcoded string
Text(text = "Welcome to Mifos Mobile")

// WRONG - String.format()
Text(text = String.format("Hello, %s!", userName))

// CORRECT - Use stringResource
Text(text = stringResource(Res.string.welcome_message))

// CORRECT - Use stringResource with arguments
Text(text = stringResource(Res.string.hello_user, userName))
```

**strings.xml example:**
```xml
<!-- feature/[name]/src/commonMain/composeResources/values/strings.xml -->
<resources>
    <string name="welcome_message">Welcome to Mifos Mobile</string>
    <string name="hello_user">Hello, %1$s!</string>
</resources>
```

---

## Related Files

- UI Spec: `claude-product-cycle/design-spec-layer/features/[feature]/SPEC.md`
- Patterns: `claude-product-cycle/design-spec-layer/_shared/PATTERNS.md`
- Navigation: `cmp-navigation/src/commonMain/kotlin/cmp/navigation/`

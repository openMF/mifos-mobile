# New Feature Planning Template

## Implementation Plan: {{FEATURE_NAME}}

**Gap Type**: Feature layer not created
**Current**: Design ✅ | Server ✅ | Client ✅ | Feature ❌
**Target**: Design ✅ | Server ✅ | Client ✅ | Feature ✅

---

### Tasks Overview

| # | Task | Files | Priority | Effort |
|---|------|-------|:--------:|:------:|
| 1 | Create feature module structure | `feature/{{FEATURE}}/build.gradle.kts` | P0 | S |
| 2 | Create ViewModel | `feature/{{FEATURE}}/.../{{FEATURE_PASCAL}}ViewModel.kt` | P0 | M |
| 3 | Create Screen | `feature/{{FEATURE}}/.../{{FEATURE_PASCAL}}Screen.kt` | P0 | M |
| 4 | Add navigation | `feature/{{FEATURE}}/.../navigation/` | P0 | S |
| 5 | Create DI module | `feature/{{FEATURE}}/di/{{FEATURE_PASCAL}}Module.kt` | P0 | S |
| 6 | Register in app | `cmp-shared/.../di/AppModule.kt` | P0 | S |

---

### Task 1: Create Feature Module

**Create** `feature/{{FEATURE}}/build.gradle.kts`:
```kotlin
plugins {
    id("org.convention.cmp.feature")
}

android {
    namespace = "org.mifos.mobile.feature.{{FEATURE}}"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
    implementation(projects.core.common)
}
```

---

### Task 2: Create ViewModel

**Create** `feature/{{FEATURE}}/src/commonMain/.../{{FEATURE_PASCAL}}ViewModel.kt`:
```kotlin
class {{FEATURE_PASCAL}}ViewModel(
    private val repository: {{FEATURE_PASCAL}}Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<{{FEATURE_PASCAL}}UiState>({{FEATURE_PASCAL}}UiState.Loading)
    val uiState: StateFlow<{{FEATURE_PASCAL}}UiState> = _uiState.asStateFlow()

    init {
        load{{FEATURE_PASCAL}}()
    }

    private fun load{{FEATURE_PASCAL}}() {
        viewModelScope.launch {
            // Implementation
        }
    }
}

sealed interface {{FEATURE_PASCAL}}UiState {
    data object Loading : {{FEATURE_PASCAL}}UiState
    data class Success(val data: {{DATA_TYPE}}) : {{FEATURE_PASCAL}}UiState
    data class Error(val message: String) : {{FEATURE_PASCAL}}UiState
}
```

---

### Task 3: Create Screen

**Create** `feature/{{FEATURE}}/src/commonMain/.../{{FEATURE_PASCAL}}Screen.kt`:
```kotlin
@Composable
fun {{FEATURE_PASCAL}}Screen(
    viewModel: {{FEATURE_PASCAL}}ViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    {{FEATURE_PASCAL}}ScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun {{FEATURE_PASCAL}}ScreenContent(
    uiState: {{FEATURE_PASCAL}}UiState,
    onNavigateBack: () -> Unit
) {
    // UI Implementation based on MOCKUP.md
}
```

---

### Task 4: Add Navigation

**Create** `feature/{{FEATURE}}/src/commonMain/.../navigation/{{FEATURE_PASCAL}}Navigation.kt`:
```kotlin
fun NavGraphBuilder.{{FEATURE_CAMEL}}Screen(
    onNavigateBack: () -> Unit
) {
    composable<{{FEATURE_PASCAL}}Route> {
        {{FEATURE_PASCAL}}Screen(
            onNavigateBack = onNavigateBack
        )
    }
}

@Serializable
data object {{FEATURE_PASCAL}}Route
```

---

### Task 5: Create DI Module

**Create** `feature/{{FEATURE}}/src/commonMain/.../di/{{FEATURE_PASCAL}}Module.kt`:
```kotlin
val {{FEATURE_CAMEL}}Module = module {
    viewModelOf(::{{FEATURE_PASCAL}}ViewModel)
}
```

---

### Task 6: Register in App

**Update** `cmp-shared/src/commonMain/.../di/AppModule.kt`:
```kotlin
val appModule = module {
    includes(
        // ... existing modules
        {{FEATURE_CAMEL}}Module
    )
}
```

---

### Verification

```bash
./gradlew :feature:{{FEATURE}}:build
./gradlew :cmp-android:assembleDemoDebug
# Run app and navigate to {{FEATURE}}
```

---

### After Completion

1. Update `feature-layer/LAYER_STATUS.md`
2. Update `design-spec-layer/features/{{FEATURE}}/STATUS.md`
3. Update `PRODUCT_MAP.md`

---

**Ready?** Run `/implement {{FEATURE}}`

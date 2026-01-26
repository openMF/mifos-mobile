# /feature - Feature/UI Layer Implementation

## Purpose

Implement the feature/UI layer using O(1) lookup and pattern detection. Creates ViewModel (MVI), Screen (Compose), Navigation, and DI with TestTags built-in and code matching existing codebase conventions.

---

## Command Variants

```
/feature                         # Show feature layer status
/feature [Feature]               # Implement feature layer
/feature [Feature] --vm          # ViewModel only
/feature [Feature] --ui          # Screen only
/feature [Feature] --nav         # Navigation only
```

---

## Workflow with O(1) Optimization

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  /feature [Feature] - O(1) OPTIMIZED WORKFLOW                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  PHASE 0: O(1) CONTEXT LOADING                                              │
│  ├─→ Read MODULES_INDEX.md            → Check if module exists              │
│  ├─→ Read SCREENS_INDEX.md            → Get existing screens/VMs            │
│  ├─→ Read FEATURE_MAP.md              → Get repository dependencies         │
│  ├─→ Read features/[name]/SPEC.md     → Get UI requirements                 │
│  └─→ Read features/[name]/mockups/    → Get design tokens (if available)    │
│                                                                              │
│  PHASE 1: PATTERN DETECTION                                                 │
│  ├─→ Read existing ViewModel          → Extract MVI pattern                 │
│  ├─→ Read existing Screen             → Extract Composable pattern          │
│  ├─→ Read existing Navigation         → Extract route pattern               │
│  └─→ Read existing TestTags           → Extract tag naming convention       │
│                                                                              │
│  PHASE 2: VIEWMODEL                                                         │
│  ├─→ Generate State class             → From SPEC.md state fields           │
│  ├─→ Generate Event sealed interface  → From SPEC.md navigation             │
│  ├─→ Generate Action sealed interface → From SPEC.md user actions           │
│  ├─→ Implement handleAction()         → Pattern-matched                     │
│  └─→ Implement data loading           → Using repository                    │
│                                                                              │
│  PHASE 3: SCREEN + TESTTAGS + DESIGN TOKENS                                 │
│  ├─→ Generate TestTags object         → feature:component pattern           │
│  ├─→ Generate main Screen composable  → With testTag modifiers              │
│  ├─→ Generate Content composable      → State-driven rendering              │
│  ├─→ Generate state composables       → Loading, Success, Error, Empty      │
│  └─→ Apply design tokens (Phase 3.5)  → If mockups/design-tokens.json exists│
│                                                                              │
│  PHASE 3.5: DESIGN TOKEN INTEGRATION (if tokens exist)                      │
│  ├─→ Read DESIGN_TOKENS_INDEX.md      → Check if feature has tokens         │
│  ├─→ Read design-tokens.json          → Parse colors, typography, components│
│  ├─→ Generate ${Feature}Theme.kt      → Feature-specific colors/gradients   │
│  ├─→ Apply component specs            → Button heights, radii, shadows      │
│  └─→ Add animation modifiers          → If animations defined               │
│                                                                              │
│  PHASE 4: NAVIGATION + DI                                                   │
│  ├─→ Generate NavGraphBuilder ext     → Type-safe navigation                │
│  ├─→ Generate Route data class        → @Serializable                       │
│  ├─→ Generate Koin module             → viewModelOf()                       │
│  └─→ Register in app navigation       → Update navigation graph             │
│                                                                              │
│  PHASE 5: BUILD & UPDATE INDEXES                                            │
│  ├─→ ./gradlew :feature:[name]:build                                        │
│  ├─→ ./gradlew spotlessApply detekt                                         │
│  ├─→ Update MODULES_INDEX.md          → Add/update module entry             │
│  └─→ Update SCREENS_INDEX.md          → Add screen entries                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## PHASE 0: O(1) Context Loading

### Files to Read

| File | Purpose | Data Extracted |
|------|---------|----------------|
| `feature-layer/MODULES_INDEX.md` | Module existence | moduleExists, vmCount, screenCount |
| `feature-layer/SCREENS_INDEX.md` | Screen details | existingScreens[], existingVMs[] |
| `client-layer/FEATURE_MAP.md` | Repository deps | repositories[] |
| `design-spec-layer/features/[name]/SPEC.md` | UI requirements | screens[], states[], actions[], events[] |
| `design-spec-layer/features/[name]/mockups/design-tokens.json` | Design tokens | colors, spacing, typography |
| `design-spec-layer/DESIGN_TOKENS_INDEX.md` | Token availability | hasTokens, format, components |

### Context Object Built

```kotlin
val context = FeatureContext(
    feature = "beneficiary",

    // From MODULES_INDEX.md
    moduleExists = true,
    existingVMs = 4,
    existingScreens = 4,

    // From FEATURE_MAP.md
    repository = "BeneficiaryRepository",

    // From SPEC.md
    screens = ["List", "Add", "Edit", "Detail"],
    states = ["Loading", "Success", "Error", "Empty"],
    actions = ["Retry", "Add", "Edit", "Delete", "Select", "Confirm"],
    events = ["NavigateToDetail", "NavigateToAdd", "NavigateBack", "ShowSnackbar"],

    // From design-tokens.json (optional)
    designTokens = DesignTokens(
        primaryColor = "0xFF1A73E8",
        spacing = mapOf("small" to 8, "medium" to 16, "large" to 24)
    )
)
```

---

## PHASE 1: Pattern Detection

### Reference Files

```
1. ViewModel Reference:
   feature/home/src/commonMain/.../viewmodel/HomeViewModel.kt

2. Screen Reference:
   feature/home/src/commonMain/.../ui/HomeScreen.kt

3. Navigation Reference:
   feature/home/src/commonMain/.../navigation/HomeNavigation.kt

4. DI Reference:
   feature/home/src/commonMain/.../di/HomeModule.kt
```

### Extracted Patterns

```kotlin
// ViewModel Pattern
val vmPattern = ViewModelPattern(
    baseClass = "BaseViewModel<${Feature}State, ${Feature}Event, ${Feature}Action>",
    stateAnnotation = "@Immutable",
    initBlock = "init { load${Feature}() }",
    handleAction = "override fun handleAction(action: ${Feature}Action)",
    updateState = "updateState { it.copy(...) }",
    sendEvent = "sendEvent(${Feature}Event.NavigateTo...)"
)

// Screen Pattern
val screenPattern = ScreenPattern(
    viewModelParam = "viewModel: ${Feature}ViewModel = koinViewModel()",
    stateCollection = "val state by viewModel.stateFlow.collectAsStateWithLifecycle()",
    eventCollection = "LaunchedEffect(Unit) { viewModel.eventFlow.collect { ... } }",
    contentCall = "${Feature}Content(state = state, onAction = viewModel::sendAction)",
    testTagModifier = "Modifier.testTag(${Feature}TestTags.SCREEN)"
)

// TestTag Pattern
val testTagPattern = TestTagPattern(
    objectName = "${Feature}TestTags",
    screenTag = "${feature}:screen",
    componentTag = "${feature}:{component}",
    itemTag = "${feature}:item:{id}"
)
```

---

## PHASE 2: ViewModel

### File Location

```
feature/[name]/src/commonMain/kotlin/org/mifos/mobile/feature/[package]/viewmodel/${Feature}ViewModel.kt
```

### ViewModel Template (MVI Pattern)

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.${Feature}Repository
import org.mifos.mobile.core.ui.base.BaseViewModel

internal class ${Feature}ViewModel(
    private val ${feature}Repository: ${Feature}Repository,
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
            ${feature}Repository.get${Feature}List()
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> updateState {
                            it.copy(uiState = ${Feature}UiState.Loading)
                        }
                        is DataState.Success -> updateState {
                            it.copy(
                                uiState = if (dataState.data.isEmpty()) {
                                    ${Feature}UiState.Empty
                                } else {
                                    ${Feature}UiState.Success
                                },
                                data = dataState.data
                            )
                        }
                        is DataState.Error -> updateState {
                            it.copy(uiState = ${Feature}UiState.Error(dataState.message))
                        }
                    }
                }
        }
    }

    private fun handleItemClick(id: Long) {
        sendEvent(${Feature}Event.NavigateToDetail(id))
    }
}

// State - fields from SPEC.md
@Immutable
data class ${Feature}State(
    val data: List<${Item}> = emptyList(),
    val uiState: ${Feature}UiState = ${Feature}UiState.Loading,
    val isRefreshing: Boolean = false,
    // ... additional fields from SPEC.md
)

// UI States
sealed interface ${Feature}UiState {
    data object Loading : ${Feature}UiState
    data object Success : ${Feature}UiState
    data object Empty : ${Feature}UiState
    data class Error(val message: String) : ${Feature}UiState
}

// Events - one-time navigation/effects from SPEC.md
sealed interface ${Feature}Event {
    data class NavigateToDetail(val id: Long) : ${Feature}Event
    data object NavigateToAdd : ${Feature}Event
    data object NavigateBack : ${Feature}Event
    data class ShowSnackbar(val message: String) : ${Feature}Event
}

// Actions - user interactions from SPEC.md
sealed interface ${Feature}Action {
    data object Retry : ${Feature}Action
    data object Refresh : ${Feature}Action
    data class OnItemClick(val id: Long) : ${Feature}Action
    data object OnAddClick : ${Feature}Action
    // ... from SPEC.md
}
```

---

## PHASE 3: Screen + TestTags

### File Locations

```
feature/[name]/src/commonMain/kotlin/org/mifos/mobile/feature/[package]/
├── ui/
│   ├── ${Feature}Screen.kt
│   └── ${Feature}TestTags.kt
└── components/
    └── ${Feature}Item.kt
```

### TestTags Template

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.ui

/**
 * Test tags for ${Feature} screen components.
 * Pattern: feature:component[:identifier]
 *
 * Usage in tests:
 * ```
 * composeTestRule.onNodeWithTag(${Feature}TestTags.SCREEN).assertIsDisplayed()
 * composeTestRule.onNodeWithTag(${Feature}TestTags.itemTag(123)).performClick()
 * ```
 */
internal object ${Feature}TestTags {
    // Screen
    const val SCREEN = "${feature}:screen"
    const val TOP_BAR = "${feature}:topBar"
    const val CONTENT = "${feature}:content"

    // States
    const val LOADING = "${feature}:loading"
    const val ERROR = "${feature}:error"
    const val EMPTY = "${feature}:empty"
    const val LIST = "${feature}:list"

    // Actions
    const val RETRY_BUTTON = "${feature}:retryButton"
    const val ADD_BUTTON = "${feature}:addButton"
    const val REFRESH = "${feature}:refresh"

    // Items (dynamic)
    private const val ITEM_PREFIX = "${feature}:item:"
    fun itemTag(id: Long) = "$ITEM_PREFIX$id"
    fun itemTag(id: String) = "$ITEM_PREFIX$id"

    // Item components
    const val ITEM_TITLE = "${feature}:item:title"
    const val ITEM_SUBTITLE = "${feature}:item:subtitle"
    const val ITEM_ICON = "${feature}:item:icon"
}
```

---

## PHASE 3.5: Design Token Integration

### When to Apply

Design tokens are applied when `design-spec-layer/DESIGN_TOKENS_INDEX.md` shows the feature has tokens.

### Check Token Availability

```kotlin
// From DESIGN_TOKENS_INDEX.md
val hasTokens = feature in ["auth", "dashboard", "settings", "guarantor", "qr", "passcode", "location", "client-charge"]
val tokenFormat = when (feature) {
    "auth" -> "google-stitch"
    else -> "md3"
}
```

### Token Integration Steps

```
IF hasTokens THEN
  1. Read design-tokens.json from features/[feature]/mockups/
  2. Parse token format (google-stitch vs md3)
  3. Extract relevant tokens:
     - colors → Custom colors or gradients
     - typography → Font sizes/weights (usually use MD3 defaults)
     - spacing → Map to DesignToken.spacing
     - radius → Map to DesignToken.shapes
     - components → Apply specs to generated components
     - animations → Add animation modifiers
  4. Generate ${Feature}Theme.kt if custom colors/gradients needed
  5. Apply tokens in Screen generation
END
```

### File: ${Feature}Theme.kt (Generated if Custom Colors)

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Design tokens from features/${feature}/mockups/design-tokens.json
 * Generated: [DATE]
 * Format: [google-stitch | md3]
 */
object ${Feature}Theme {
    // Gradient (from google-stitch format)
    val primaryGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF667EEA), // tokens.colors.primary.gradient.start
            Color(0xFF764BA2)  // tokens.colors.primary.gradient.end
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    // Semantic colors
    object Colors {
        val success = Color(0xFF00D09C)
        val error = Color(0xFFFF4757)
        val warning = Color(0xFFFFB800)
        val info = Color(0xFF667EEA)
    }

    // Component specs (if defined)
    object Components {
        val buttonHeight = 56.dp
        val buttonRadius = 16.dp
        val inputHeight = 56.dp
        val inputRadius = 12.dp
        val cardRadius = 16.dp
    }
}
```

### Apply Gradient to Button

```kotlin
// Without tokens (default):
Button(
    onClick = onClick,
    modifier = modifier,
) {
    Text(text)
}

// With tokens (gradient):
Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    modifier = modifier
        .height(${Feature}Theme.Components.buttonHeight)
        .background(
            brush = ${Feature}Theme.primaryGradient,
            shape = RoundedCornerShape(${Feature}Theme.Components.buttonRadius)
        ),
) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
    )
}
```

### Apply Component Specs

When design-tokens.json has component specs:

```json
{
  "components": [
    {
      "id": "primary-button",
      "specs": {
        "height": "56dp",
        "radius": "16dp",
        "background": "gradient",
        "textSize": "16sp",
        "textWeight": "600"
      }
    }
  ]
}
```

Generated component:

```kotlin
@Composable
fun ${Feature}PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier
            .height(56.dp)  // from specs.height
            .background(
                brush = ${Feature}Theme.primaryGradient,  // from specs.background = "gradient"
                shape = RoundedCornerShape(16.dp)  // from specs.radius
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,  // from specs.textSize
            fontWeight = FontWeight.SemiBold,  // from specs.textWeight = "600"
            color = Color.White,
        )
    }
}
```

### Apply Animations (If Defined)

When design-tokens.json has animations:

```json
{
  "animations": {
    "buttonPress": { "scale": "0.98", "duration": "100ms" },
    "errorShake": { "translateX": "[-10, 10, -5, 5, 0]", "duration": "300ms" }
  }
}
```

Add animation utilities:

```kotlin
// ${Feature}Animations.kt
object ${Feature}Animations {
    // Button press animation
    fun Modifier.buttonPressAnimation(
        interactionSource: InteractionSource,
    ): Modifier = composed {
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.98f else 1f,
            animationSpec = tween(durationMillis = 100)
        )
        this.scale(scale)
    }

    // Error shake animation
    @Composable
    fun Modifier.errorShake(
        trigger: Boolean,
    ): Modifier {
        val offsetX by animateFloatAsState(
            targetValue = if (trigger) 0f else 0f,
            animationSpec = keyframes {
                durationMillis = 300
                -10f at 0
                10f at 75
                -5f at 150
                5f at 225
                0f at 300
            }
        )
        return this.offset(x = offsetX.dp)
    }
}
```

### Token Format Detection

```kotlin
// In Phase 0
val tokenFormat = when {
    designTokens?.tool == "google-stitch" -> TokenFormat.GOOGLE_STITCH
    designTokens?.tokens?.colors?.containsKey("primary") == true -> TokenFormat.MD3
    else -> null
}

// Google Stitch format has:
// - tokens.colors.primary.gradient (with start, end, angle)
// - tokens.colors.surface.light / .dark
// - animations block

// MD3 format has:
// - tokens.colors.primary (direct color string)
// - tokens.typography with MD3 scale names
```

### Output Template (With Tokens)

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  🎨 DESIGN TOKENS APPLIED                                                    │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Source: features/${feature}/mockups/design-tokens.json                      │
│  Format: [google-stitch | md3]                                               │
│                                                                               │
│  📊 Tokens Applied:                                                          │
│  ├─ Colors: [count] custom colors                                           │
│  ├─ Gradients: [count] gradient definitions                                  │
│  ├─ Components: [count] with specs                                          │
│  └─ Animations: [count] animation definitions                               │
│                                                                               │
│  📁 Files Generated:                                                         │
│  ├─ theme/${Feature}Theme.kt        [CREATED]                               │
│  ├─ theme/${Feature}Animations.kt   [CREATED if animations exist]           │
│  └─ components/${Feature}Button.kt  [CREATED if component specs exist]      │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

### Screen Template

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.*
import org.mifos.mobile.feature.${package}.viewmodel.*

@Composable
fun ${Feature}Screen(
    viewModel: ${Feature}ViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ${Feature}Event.NavigateBack -> onNavigateBack()
                is ${Feature}Event.NavigateToDetail -> onNavigateToDetail(event.id)
                is ${Feature}Event.NavigateToAdd -> onNavigateToAdd()
                is ${Feature}Event.ShowSnackbar -> {
                    // Handle snackbar
                }
            }
        }
    }

    ${Feature}ScreenContent(
        state = state,
        onAction = viewModel::sendAction,
        modifier = modifier.testTag(${Feature}TestTags.SCREEN),
    )
}

@Composable
internal fun ${Feature}ScreenContent(
    state: ${Feature}State,
    onAction: (${Feature}Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            MifosTopAppBar(
                title = "${Feature}",
                onNavigationClick = { onAction(${Feature}Action.NavigateBack) },
                modifier = Modifier.testTag(${Feature}TestTags.TOP_BAR),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(${Feature}Action.OnAddClick) },
                modifier = Modifier.testTag(${Feature}TestTags.ADD_BUTTON),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag(${Feature}TestTags.CONTENT),
        ) {
            when (state.uiState) {
                is ${Feature}UiState.Loading -> {
                    ${Feature}Loading(
                        modifier = Modifier.testTag(${Feature}TestTags.LOADING),
                    )
                }
                is ${Feature}UiState.Success -> {
                    ${Feature}Success(
                        data = state.data,
                        onItemClick = { onAction(${Feature}Action.OnItemClick(it)) },
                        modifier = Modifier.testTag(${Feature}TestTags.LIST),
                    )
                }
                is ${Feature}UiState.Empty -> {
                    ${Feature}Empty(
                        onAddClick = { onAction(${Feature}Action.OnAddClick) },
                        modifier = Modifier.testTag(${Feature}TestTags.EMPTY),
                    )
                }
                is ${Feature}UiState.Error -> {
                    ${Feature}Error(
                        message = state.uiState.message,
                        onRetry = { onAction(${Feature}Action.Retry) },
                        modifier = Modifier.testTag(${Feature}TestTags.ERROR),
                    )
                }
            }
        }
    }
}

@Composable
private fun ${Feature}Loading(modifier: Modifier = Modifier) {
    MifosLoadingWheel(modifier = modifier)
}

@Composable
private fun ${Feature}Success(
    data: List<${Item}>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = data,
            key = { it.id }
        ) { item ->
            ${Feature}Item(
                item = item,
                onClick = { onItemClick(item.id) },
                modifier = Modifier.testTag(${Feature}TestTags.itemTag(item.id)),
            )
        }
    }
}

@Composable
private fun ${Feature}Empty(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosEmptyContent(
        title = "No ${Feature} Found",
        message = "Add your first ${feature}",
        buttonText = "Add ${Feature}",
        onButtonClick = onAddClick,
        modifier = modifier,
    )
}

@Composable
private fun ${Feature}Error(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosErrorContent(
        message = message,
        onRetry = onRetry,
        modifier = modifier,
        retryButtonModifier = Modifier.testTag(${Feature}TestTags.RETRY_BUTTON),
    )
}
```

---

## PHASE 4: Navigation + DI

### Navigation Template

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.${package}.ui.${Feature}Screen

@Serializable
data object ${Feature}Route

@Serializable
data class ${Feature}DetailRoute(val id: Long)

fun NavGraphBuilder.${feature}Screen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
) {
    composable<${Feature}Route> {
        ${Feature}Screen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToAdd = onNavigateToAdd,
        )
    }
}

fun NavController.navigateTo${Feature}() {
    navigate(${Feature}Route)
}

fun NavController.navigateTo${Feature}Detail(id: Long) {
    navigate(${Feature}DetailRoute(id))
}
```

### DI Module Template

```kotlin
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.mifos.mobile.feature.${package}.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.${package}.viewmodel.${Feature}ViewModel

val ${feature}Module = module {
    viewModelOf(::${Feature}ViewModel)
}
```

---

## PHASE 5: Build & Update Indexes

### Build Commands

```bash
# Build feature module
./gradlew :feature:${name}:build

# Format and lint
./gradlew spotlessApply detekt --no-configuration-cache
```

### Update MODULES_INDEX.md

```markdown
| ${n} | ${module} | feature/${module} | ✅ | ${vmCount} | ${screenCount} |
```

### Update SCREENS_INDEX.md

```markdown
### ${module} (${screenCount} screens)

| Screen | ViewModel | File |
|--------|-----------|------|
| ${Feature}Screen | ${Feature}ViewModel | ui/${Feature}Screen.kt |
```

---

## Output Template

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ✅ FEATURE LAYER COMPLETE                                                   │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  📚 O(1) Context Used:                                                        │
│  ├─ MODULES_INDEX.md → Verified module: [exists/new]                         │
│  ├─ SCREENS_INDEX.md → Existing screens: [count]                             │
│  ├─ FEATURE_MAP.md → Repository: ${Feature}Repository                        │
│  └─ SPEC.md → Mapped [n] screens, [n] states, [n] actions                    │
│                                                                               │
│  📊 Pattern Matching:                                                         │
│  ├─ ViewModel pattern from: HomeViewModel.kt                                 │
│  └─ Screen pattern from: HomeScreen.kt                                       │
│                                                                               │
│  🔧 ViewModel:                                                                │
│  └─ ${Feature}ViewModel.kt       [CREATED|UPDATED]                           │
│     ├─ State: ${Feature}State                                                │
│     ├─ Events: [count] defined                                               │
│     └─ Actions: [count] defined                                              │
│                                                                               │
│  🎨 Screen:                                                                   │
│  ├─ ${Feature}Screen.kt          [CREATED|UPDATED]                           │
│  ├─ ${Feature}TestTags.kt        [CREATED]                                   │
│  └─ components/                  [count] files                               │
│                                                                               │
│  🏷️ TestTags Generated:                                                       │
│  ├─ ${feature}:screen                                                         │
│  ├─ ${feature}:loading                                                        │
│  ├─ ${feature}:error                                                          │
│  ├─ ${feature}:empty                                                          │
│  ├─ ${feature}:list                                                           │
│  ├─ ${feature}:item:{id}                                                      │
│  └─ ... [n] total tags                                                        │
│                                                                               │
│  🧭 Navigation:                                                               │
│  ├─ ${Feature}Navigation.kt      [CREATED|UPDATED]                           │
│  └─ Routes: ${Feature}Route, ${Feature}DetailRoute                           │
│                                                                               │
│  📦 DI:                                                                       │
│  └─ ${Feature}Module.kt          [CREATED|UPDATED]                           │
│                                                                               │
│  📋 Indexes Updated:                                                          │
│  ├─ MODULES_INDEX.md             [UPDATED]                                   │
│  └─ SCREENS_INDEX.md             [UPDATED]                                   │
│                                                                               │
│  🔨 BUILD: :feature:${name} ✅                                                │
│  🧹 LINT: spotlessApply ✅ detekt ✅                                          │
│                                                                               │
├──────────────────────────────────────────────────────────────────────────────┤
│  NEXT STEPS:                                                                  │
│  • Verify: /verify ${Feature}                                                │
│  • Test: /verify-tests ${Feature}                                            │
│  • Full E2E: /implement ${Feature} (if client layer also needed)             │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Feature Status (No Argument)

When `/feature` called without arguments, read MODULES_INDEX.md and SCREENS_INDEX.md:

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  📋 FEATURE LAYER STATUS (from MODULES_INDEX.md)                             │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Summary: 23 modules | 63 screens | 49 ViewModels | 21 DI modules            │
│                                                                               │
│  | Module          | VMs | Screens | DI | Status     | Command           │   │
│  |-----------------|:---:|:-------:|:--:|------------|-------------------|   │
│  | auth            | 5   | 6       | ✅ | ✅ Complete | /feature auth     │   │
│  | home            | 1   | 1       | ✅ | ✅ Complete | /feature home     │   │
│  | accounts        | 3   | 3       | ✅ | ✅ Complete | /feature accounts │   │
│  | beneficiary     | 4   | 4       | ✅ | ✅ Complete | /feature beneficiary ││
│  | ...                                                                       │
│                                                                               │
│  Commands:                                                                    │
│  • /feature [name] → Implement feature layer                                 │
│  • /gap-analysis feature → Check for gaps                                    │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Error Handling

### Missing Repository

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ⚠️ MISSING PREREQUISITE: Repository                                         │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Feature: ${feature}                                                         │
│  Expected: ${Feature}Repository in FEATURE_MAP.md                            │
│  Found: Not registered                                                       │
│                                                                               │
│  The ViewModel requires a repository for data operations.                    │
│                                                                               │
│  Options:                                                                     │
│  • c / client   → Run /client ${feature} first (recommended)                 │
│  • s / skip     → Continue without repository (limited functionality)        │
│  • a / abort    → Cancel implementation                                      │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Missing SPEC.md

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ⚠️ MISSING SPECIFICATION                                                    │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Feature: ${feature}                                                         │
│  Expected: design-spec-layer/features/${feature}/SPEC.md                     │
│  Found: File missing                                                         │
│                                                                               │
│  SPEC.md defines screens, states, and actions for code generation.           │
│                                                                               │
│  Options:                                                                     │
│  • d / design   → Run /design ${feature} first (recommended)                 │
│  • m / manual   → Use default template (basic CRUD)                          │
│  • a / abort    → Cancel implementation                                      │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Build Failure

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ❌ BUILD FAILED: :feature:${name}                                           │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Error: Unresolved reference: ${Feature}Repository                           │
│                                                                               │
│  📍 Root Cause:                                                               │
│  Repository not registered in RepositoryModule                               │
│                                                                               │
│  📍 Auto-Fix:                                                                 │
│  Add to core/data/di/RepositoryModule.kt:                                    │
│  single<${Feature}Repository> { ${Feature}RepositoryImp(get()) }             │
│                                                                               │
│  Options:                                                                     │
│  • f / fix    → Apply fix and rebuild                                        │
│  • c / client → Run /client ${feature} to create full client layer           │
│  • a / abort  → Stop implementation                                          │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## TestTag Reference

### Naming Convention

```
Pattern: feature:component[:identifier]

Examples:
- beneficiary:screen              # Main screen
- beneficiary:loading             # Loading state
- beneficiary:error               # Error state
- beneficiary:list                # List content
- beneficiary:item:123            # Specific item by ID
- beneficiary:addButton           # Action button
- beneficiary:retryButton         # Retry action
```

### Test Usage Example

```kotlin
@Test
fun beneficiaryScreen_showsLoadingState() {
    composeTestRule.setContent {
        BeneficiaryScreenContent(
            state = BeneficiaryState(uiState = BeneficiaryUiState.Loading),
            onAction = {}
        )
    }

    composeTestRule
        .onNodeWithTag(BeneficiaryTestTags.LOADING)
        .assertIsDisplayed()
}

@Test
fun beneficiaryScreen_itemClick_navigatesToDetail() {
    val actions = mutableListOf<BeneficiaryAction>()

    composeTestRule.setContent {
        BeneficiaryScreenContent(
            state = BeneficiaryState(
                uiState = BeneficiaryUiState.Success,
                data = listOf(Beneficiary(id = 123, name = "Test"))
            ),
            onAction = { actions.add(it) }
        )
    }

    composeTestRule
        .onNodeWithTag(BeneficiaryTestTags.itemTag(123))
        .performClick()

    assertEquals(BeneficiaryAction.OnItemClick(123), actions.first())
}
```

---

## Related Commands

| Command | Purpose |
|---------|---------|
| `/client [Feature]` | Client layer (Repository) |
| `/implement [Feature]` | Full E2E (Client + Feature) |
| `/verify [Feature]` | Verify implementation vs spec |
| `/verify-tests [Feature]` | Run tests |
| `/gap-analysis feature` | Check feature layer gaps |

# Feature Layer Guide

> **Location**: `feature/`
> **Command**: `/feature [Feature]`

## Table of Contents
1. [Overview](#overview)
2. [Creating New Feature](#creating-new-feature)
3. [Directory Structure](#directory-structure)
4. [Component Organization](#component-organization)
5. [Build Commands](#build-commands)
6. [Cross-Update Rules](#cross-update-rules)
7. [Instructions Reference](#instructions-reference)

---

## Overview

The feature layer contains UI modules following MVI (Model-View-Intent) architecture:

```
┌─────────────────────────────────────────────────────────────────────┐
│  FEATURE LAYER (feature/)                                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  feature/[name]/                                                     │
│  ├── [screen]/                 → Screen package                      │
│  │   ├── [Screen]Screen.kt    → Compose UI                          │
│  │   ├── [Screen]ViewModel.kt → MVI (State, Event, Action)          │
│  │   ├── [Screen].kt          → State/Event/Action definitions      │
│  │   └── components/          → Screen-specific components          │
│  ├── components/              → Feature-shared components            │
│  ├── navigation/              → Navigation routes                    │
│  └── di/[Name]Module.kt       → Koin registration                   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Creating New Feature

### Step 1: Create Module Directory

```bash
mkdir -p feature/[name]/src/commonMain/kotlin/org/mifos/mobile/feature/[name]
mkdir -p feature/[name]/src/commonMain/composeResources/values
```

### Step 2: Create build.gradle.kts

```kotlin
// feature/[name]/build.gradle.kts

plugins {
    alias(libs.plugins.mifos.cmp.feature)
}

android {
    namespace = "org.mifos.mobile.feature.[name]"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
}
```

### Step 3: Register in settings.gradle.kts

```kotlin
// settings.gradle.kts
include(":feature:[name]")
```

### Step 4: Create Directory Structure

```
feature/[name]/src/commonMain/kotlin/org/mifos/mobile/feature/[name]/
├── [screen]/
│   ├── [Screen]Screen.kt
│   ├── [Screen]ViewModel.kt
│   ├── [Screen].kt
│   └── components/
├── components/
├── navigation/
│   └── [Feature]Navigation.kt
└── di/
    └── [Feature]Module.kt
```

### Step 5: Create strings.xml

```xml
<!-- feature/[name]/src/commonMain/composeResources/values/strings.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="feature_[name]_title">Feature Title</string>
</resources>
```

### New Feature Checklist

- [ ] Module directory created
- [ ] build.gradle.kts configured
- [ ] Registered in settings.gradle.kts
- [ ] Screen package created with Screen, ViewModel, State/Event/Action
- [ ] Navigation setup in navigation/
- [ ] DI module created in di/
- [ ] DI module registered in KoinModules.kt
- [ ] strings.xml created for string resources
- [ ] Build passes: `./gradlew :feature:[name]:build`

---

## Directory Structure

### Single-Screen Feature

For features with one main screen:

```
feature/[name]/
├── [Feature]Screen.kt
├── [Feature]ViewModel.kt
├── [Feature].kt                    # State/Event/Action
├── components/
│   └── [Feature]Component.kt
├── navigation/
│   └── [Feature]Navigation.kt
└── di/
    └── [Feature]Module.kt
```

**Example - Notification:**
```
feature/notification/
├── NotificationScreen.kt
├── NotificationViewModel.kt
├── Notification.kt
├── components/
│   └── NotificationItem.kt
├── navigation/
│   └── NotificationNavigation.kt
└── di/
    └── NotificationModule.kt
```

### Multi-Screen Feature

For features with multiple screens:

```
feature/[name]/
├── [screen1]/
│   ├── [Screen1]Screen.kt
│   ├── [Screen1]ViewModel.kt
│   ├── [Screen1].kt
│   └── components/
│       └── [Screen1]Header.kt
├── [screen2]/
│   ├── [Screen2]Screen.kt
│   ├── [Screen2]ViewModel.kt
│   ├── [Screen2].kt
│   └── components/
│       └── [Screen2]Form.kt
├── components/                     # Shared across screens
│   └── [Feature]SharedComponent.kt
├── navigation/
│   └── [Feature]Navigation.kt
└── di/
    └── [Feature]Module.kt
```

**Example - Auth:**
```
feature/auth/
├── login/
│   ├── LoginScreen.kt
│   ├── LoginViewModel.kt
│   ├── Login.kt
│   └── components/
│       ├── LoginHeader.kt
│       └── LoginForm.kt
├── registration/
│   ├── RegistrationScreen.kt
│   ├── RegistrationViewModel.kt
│   ├── Registration.kt
│   └── components/
│       └── RegistrationSteps.kt
├── otpAuthentication/
│   ├── OtpAuthenticationScreen.kt
│   ├── OtpAuthenticationViewModel.kt
│   └── components/
│       └── OtpInput.kt
├── components/                     # Shared auth components
│   ├── AuthHeader.kt
│   └── AuthFooter.kt
├── navigation/
│   └── AuthNavigation.kt
└── di/
    └── AuthModule.kt
```

---

## Component Organization

### Component Hierarchy

```
┌─────────────────────────────────────────────────────────────┐
│  Screen-Specific: feature/[name]/[screen]/components/      │
│  → Used only in that screen                                │
├─────────────────────────────────────────────────────────────┤
│  Feature-Shared: feature/[name]/components/                │
│  → Used across screens in same feature                     │
├─────────────────────────────────────────────────────────────┤
│  App-Wide: core/ui/component/                              │
│  → Used in 2+ features                                     │
├─────────────────────────────────────────────────────────────┤
│  Design System: core/designsystem/component/               │
│  → UI primitives (Button, TextField, Card)                 │
├─────────────────────────────────────────────────────────────┤
│  Foundation: core-base/designsystem/                       │
│  → Theme, layouts (KptTheme, KptGrid)                      │
└─────────────────────────────────────────────────────────────┘
```

### Component Placement Decision

```
Creating a new component?
│
├── Used only in this screen?
│   └── feature/[name]/[screen]/components/
│
├── Used across screens in same feature?
│   └── feature/[name]/components/
│
├── Used in 2+ features?
│   └── core/ui/component/
│
├── UI primitive (Button, TextField variant)?
│   └── core/designsystem/component/
│
└── Theme/Layout component?
    └── core-base/designsystem/
```

### Examples

| Component | Location | Reason |
|-----------|----------|--------|
| `LoginForm` | `feature/auth/login/components/` | Only in login screen |
| `AuthHeader` | `feature/auth/components/` | Shared across auth screens |
| `MifosAccountCard` | `core/ui/component/` | Used in accounts, home |
| `MifosButton` | `core/designsystem/component/` | UI primitive |
| `KptTheme` | `core-base/designsystem/` | Foundation |

---

## Build Commands

```bash
# Build specific feature
./gradlew :feature:[name]:build

# Build all features
./gradlew build

# Lint check
./gradlew :feature:[name]:detekt

# Format code
./gradlew spotlessApply

# Run tests
./gradlew :feature:[name]:test
```

---

## Cross-Update Rules

### String Resources

**NEVER use hardcoded strings.** All user-facing strings MUST be in `strings.xml`:

```kotlin
// WRONG - Hardcoded string
Text(text = "Welcome to Mifos Mobile")

// WRONG - String.format()
Text(text = String.format("Hello, %s!", userName))

// CORRECT - Use stringResource
Text(text = stringResource(Res.string.welcome_message))

// CORRECT - With arguments
Text(text = stringResource(Res.string.hello_user, userName))
```

**strings.xml:**
```xml
<!-- feature/[name]/src/commonMain/composeResources/values/strings.xml -->
<resources>
    <string name="welcome_message">Welcome to Mifos Mobile</string>
    <string name="hello_user">Hello, %1$s!</string>
</resources>
```

### Status Updates

After implementing a feature, update:
1. `feature-layer/LAYER_STATUS.md` - Feature layer status
2. `design-spec-layer/features/[feature]/STATUS.md` - Feature design status

### Component Creation

**ALWAYS check existing components before creating new ones.**

See [core-layer/COMPONENTS.md](../core-layer/COMPONENTS.md) for complete registry.

**Lookup Strategy:**
```
Step 1: Check Static Registry (Fast)
        → Read core-layer/COMPONENTS.md tables

Step 2: If Not Found → Dynamic Search
        → grep -r "@Composable" core/ | grep -i "[type]"

Step 3: If Found Dynamically → Update Registry
        → Add to core-layer/COMPONENTS.md static tables
```

**Naming Convention:**
| Location | Prefix | Example |
|----------|--------|---------|
| core-base/designsystem | `Kpt*` | `KptGrid`, `KptShimmerLoadingBox` |
| core/designsystem | `Mifos*` | `MifosButton`, `MifosTextField` |
| core/ui | `Mifos*` | `MifosAccountCard`, `MifosErrorComponent` |
| feature/[name]/components | `[Feature]*` | `AuthHeader` |
| feature/[name]/[screen]/components | `[Screen]*` | `LoginForm` |

**Update Rules:**
| Scenario | Action |
|----------|--------|
| Found in static registry | No update needed |
| Found via dynamic search | ADD to static registry |
| Created new component in core/ | ADD to static registry |
| Created feature component | No update needed |

---

## Instructions Reference

For detailed implementation patterns, see:

| Pattern | File | When to Use |
|---------|------|-------------|
| **ViewModel** | [instructions/VIEWMODEL.md](instructions/VIEWMODEL.md) | Creating/updating ViewModel, State, Event, Action |
| **Compose Screen** | [instructions/COMPOSE.md](instructions/COMPOSE.md) | Creating screens, components, UI patterns |
| **Navigation** | [instructions/NAVIGATION.md](instructions/NAVIGATION.md) | Setting up routes, NavGraph |
| **Dependency Injection** | [instructions/DI.md](instructions/DI.md) | Koin module registration |
| **Updating Feature** | [instructions/UPDATING_FEATURE.md](instructions/UPDATING_FEATURE.md) | v2.0 UI redesign, improving existing features |

### Quick Pattern Reference

**ViewModel Pattern:**
```kotlin
internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
) : BaseViewModel<[Feature]State, [Feature]Event, [Feature]Action>(
    initialState = [Feature]State()
) {
    override fun handleAction(action: [Feature]Action) { ... }
}
```

**Screen Pattern:**
```kotlin
@Composable
internal fun [Feature]Screen(
    onNavigateBack: () -> Unit,
    viewModel: [Feature]ViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel.eventFlow) { event -> ... }
    [Feature]Content(state = state, onAction = viewModel::trySendAction)
}
```

**Navigation Pattern:**
```kotlin
@Serializable
data object [Feature]Route

fun NavGraphBuilder.[feature]Screen(onNavigateBack: () -> Unit) {
    composableWithStayTransitions<[Feature]Route> {
        [Feature]Screen(onNavigateBack = onNavigateBack)
    }
}
```

**DI Pattern:**
```kotlin
val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

---

## Related Files

- Design Specs: `claude-product-cycle/design-spec-layer/features/[feature]/SPEC.md`
- Mockups: `claude-product-cycle/design-spec-layer/features/[feature]/MOCKUP.md`
- Patterns: `claude-product-cycle/design-spec-layer/_shared/PATTERNS.md`
- Navigation: `cmp-navigation/src/commonMain/kotlin/cmp/navigation/`
- Feature Status: `claude-product-cycle/feature-layer/LAYER_STATUS.md`

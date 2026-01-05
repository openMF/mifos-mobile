# Updating Existing Feature

## Table of Contents
1. [Overview](#overview)
2. [When to Use](#when-to-use)
3. [Pre-Update Checklist](#pre-update-checklist)
4. [Update Workflow](#update-workflow)
5. [Component Updates](#component-updates)
6. [State Migration](#state-migration)
7. [Testing Updates](#testing-updates)
8. [Post-Update Checklist](#post-update-checklist)

---

## Overview

This guide covers updating existing features for v2.0 UI redesign or improvements. Unlike creating new features, updates require careful consideration of:
- Existing state management
- Current navigation patterns
- Component reusability
- Backward compatibility

---

## When to Use

Use this guide when:

| Scenario | Example |
|----------|---------|
| **v2.0 UI Redesign** | Modernizing login screen with new design |
| **Adding Screens** | Adding detail screen to existing feature |
| **Refactoring** | Splitting monolithic screen into sections |
| **Component Migration** | Moving from custom to design system components |
| **Pattern Alignment** | Updating to Container + Content pattern |

---

## Pre-Update Checklist

Before starting updates:

- [ ] Read current design spec: `design-spec-layer/features/[feature]/SPEC.md`
- [ ] Review mockups: `design-spec-layer/features/[feature]/MOCKUP.md`
- [ ] Check API changes: `design-spec-layer/features/[feature]/API.md`
- [ ] Understand current implementation structure
- [ ] Identify reusable components
- [ ] Plan state changes (if any)
- [ ] Identify screens that can be updated independently

---

## Update Workflow

### Step 1: Analyze Current Implementation

```bash
# List current structure
ls -la feature/[name]/src/commonMain/kotlin/org/mifos/mobile/feature/[name]/

# Find all screens
find feature/[name]/ -name "*Screen.kt"

# Find all ViewModels
find feature/[name]/ -name "*ViewModel.kt"
```

### Step 2: Compare with Design Spec

| Current | Design Spec | Action |
|---------|-------------|--------|
| LoginScreen.kt | Login Screen v2.0 | Update UI |
| - | Biometric Login | Add new screen |
| OldComponent.kt | Removed | Delete |

### Step 3: Update Order

**Recommended order:**
1. **State/Action** - Add new fields without breaking existing
2. **ViewModel** - Add new handlers
3. **Components** - Create/update section components
4. **Screen** - Update layout
5. **Navigation** - Add new routes if needed
6. **DI** - Update if new dependencies

---

## Component Updates

### Replacing Hardcoded Values

```kotlin
// BEFORE - Hardcoded
Column(
    modifier = Modifier.padding(16.dp)
) {
    Text(
        text = "Title",
        fontSize = 24.sp,
        color = Color.Black,
    )
}

// AFTER - Theme tokens
Column(
    modifier = Modifier.padding(KptTheme.spacing.md)
) {
    Text(
        text = stringResource(Res.string.title),
        style = MifosTypography.titleLarge,
        color = KptTheme.colorScheme.onSurface,
    )
}
```

### Replacing Custom Components

```kotlin
// BEFORE - Custom implementation
Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.Gray)
        .padding(16.dp)
) {
    // Custom loading
}

// AFTER - Design system component
KptShimmerLoadingBox(
    modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .clip(KptTheme.shapes.medium),
)
```

### Section Extraction

```kotlin
// BEFORE - Monolithic
@Composable
private fun AccountContent(state: State) {
    Column {
        // 200+ lines of mixed code
        Text(state.title)
        Text(state.balance)
        // ... buttons, lists, etc.
    }
}

// AFTER - Section-based
@Composable
private fun AccountSuccessContent(
    state: State,
    onAction: (Action) -> Unit,
) {
    LazyColumn {
        item { AccountHeaderSection(state.title) }
        item { AccountBalanceSection(state.balance) }
        item { AccountActionsSection(onAction) }
        items(state.transactions) { TransactionItem(it) }
    }
}
```

---

## State Migration

### Adding New Fields

```kotlin
// BEFORE
data class [Feature]State(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
)

// AFTER - Add with defaults (non-breaking)
data class [Feature]State(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    // New fields with defaults
    val selectedFilter: FilterType = FilterType.ALL,
    val searchQuery: String = "",
    val dialogState: DialogState? = null,
)
```

### Adding New Actions

```kotlin
// BEFORE
sealed interface [Feature]Action {
    data class OnItemClick(val id: Long) : [Feature]Action
}

// AFTER - Add new actions
sealed interface [Feature]Action {
    data class OnItemClick(val id: Long) : [Feature]Action
    // New actions
    data class OnFilterChange(val filter: FilterType) : [Feature]Action
    data class OnSearchQuery(val query: String) : [Feature]Action
    data object OnClearSearch : [Feature]Action
}
```

### Adding New Events

```kotlin
// BEFORE
sealed interface [Feature]Event {
    data object NavigateBack : [Feature]Event
}

// AFTER - Add new events
sealed interface [Feature]Event {
    data object NavigateBack : [Feature]Event
    // New events
    data class ShowFilterSheet(val options: List<FilterType>) : [Feature]Event
    data class ShowToast(val message: String) : [Feature]Event
}
```

---

## Testing Updates

### Verify No Regression

```bash
# Run feature tests
./gradlew :feature:[name]:test

# Run lint
./gradlew :feature:[name]:detekt

# Build feature
./gradlew :feature:[name]:build
```

### Manual Testing

- [ ] All existing flows still work
- [ ] New UI matches mockups
- [ ] Loading states display correctly
- [ ] Error states handle gracefully
- [ ] Empty states show appropriate message
- [ ] Navigation works as expected
- [ ] Dialogs open/close properly

---

## Post-Update Checklist

After completing updates:

- [ ] All screens follow Container + Content pattern
- [ ] All components use KptTheme tokens (no hardcoded values)
- [ ] All strings in strings.xml (no hardcoded text)
- [ ] Section components in `[screen]/components/`
- [ ] Shared components in `feature/[name]/components/`
- [ ] Previews added for all states
- [ ] Build passes: `./gradlew :feature:[name]:build`
- [ ] Update STATUS.md: `design-spec-layer/features/[feature]/STATUS.md`
- [ ] Update LAYER_STATUS.md if structure changed

---

## Common Update Patterns

### Pattern 1: Add Search to List Screen

1. Add state fields: `searchQuery`, `filteredItems`
2. Add actions: `OnSearchQuery`, `OnClearSearch`
3. Add `MifosSearchTextField` above list
4. Filter in ViewModel based on query

### Pattern 2: Add Detail Screen to Feature

1. Create `[feature]Detail/` package
2. Add `[Feature]DetailRoute` with argument
3. Add navigation extension function
4. Register in NavGraph
5. Update list screen to navigate on click

### Pattern 3: Convert to Tab Layout

1. Add `selectedTab` to state
2. Add `OnTabSelect` action
3. Use `MifosTabPager` or `TabRow`
4. Create content composable per tab

### Pattern 4: Add Pull-to-Refresh

1. Add `isRefreshing` to state
2. Add `OnRefresh` action
3. Wrap content with `PullToRefreshBox`
4. Call refresh in ViewModel

---

## Related Files

- New Feature: [LAYER_GUIDE.md](../LAYER_GUIDE.md#creating-new-feature)
- Compose Patterns: [COMPOSE.md](COMPOSE.md)
- ViewModel Patterns: [VIEWMODEL.md](VIEWMODEL.md)
- Navigation: [NAVIGATION.md](NAVIGATION.md)

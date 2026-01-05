# Compose Screen Patterns

## Table of Contents
1. [Component Hierarchy](#component-hierarchy)
2. [Available Components](#available-components)
3. [Screen Structure](#screen-structure)
4. [Container + Content Pattern](#container--content-pattern)
5. [Section-Based Design](#section-based-design)
6. [Component Placement Rules](#component-placement-rules)
7. [Reusability Rules](#reusability-rules)
8. [Loading States](#loading-states)
9. [Error States](#error-states)
10. [Empty States](#empty-states)
11. [Dialog Management](#dialog-management)
12. [Theming Guidelines](#theming-guidelines)
13. [Preview Patterns](#preview-patterns)

---

## Component Hierarchy

```
┌─────────────────────────────────────────────────────────────┐
│  LEVEL 5: Foundation (core-base/designsystem)              │
│  KptTheme, KptTopAppBar, KptShimmerLoadingBox, KptGrid     │
├─────────────────────────────────────────────────────────────┤
│  LEVEL 4: Design System (core/designsystem)                │
│  MifosButton, MifosTextField, MifosCard, MifosScaffold     │
├─────────────────────────────────────────────────────────────┤
│  LEVEL 3: App-Wide (core/ui)                               │
│  MifosAccountCard, TransactionScreenItem, EmptyDataView    │
├─────────────────────────────────────────────────────────────┤
│  LEVEL 2: Feature-Shared (feature/[name]/components/)      │
│  AuthHeader, TransferAmountInput                           │
├─────────────────────────────────────────────────────────────┤
│  LEVEL 1: Screen-Specific (feature/[name]/[screen]/comp/)  │
│  LoginForm, AccountDetailsHeader                           │
└─────────────────────────────────────────────────────────────┘
```

**Rule**: Always check higher levels before creating new components.

---

## Available Components

> **Lookup Strategy**: Static registry first → Dynamic search if not found → Auto-update registry
> See [core-layer/COMPONENTS.md](../../core-layer/COMPONENTS.md) for complete component registry.

### Component Lookup (ALWAYS DO FIRST)

**Step 1: Check Static Registry (Fast)**
```
Read: core-layer/COMPONENTS.md → Static Component Registry section
```

**Step 2: If Not Found → Dynamic Search**
```bash
# Search by component type
grep -r "@Composable" core/ core-base/ | grep -i "[type]"

# Or list directories
ls core-base/designsystem/**/component/
ls core/designsystem/**/component/
ls core/ui/**/component/
```

**Step 3: If Found Dynamically → Update Registry**
```
Add to: core-layer/COMPONENTS.md → appropriate static table
```

### Source Directories

| Layer | Path | Prefix | Purpose |
|-------|------|--------|---------|
| Foundation | `core-base/designsystem/.../component/` | `Kpt*` | Theme, loading, animations |
| Foundation | `core-base/designsystem/.../layout/` | `Kpt*` | Grid, flow, responsive layouts |
| Design System | `core/designsystem/.../component/` | `Mifos*` | UI primitives (button, textfield) |
| Business | `core/ui/.../component/` | `Mifos*` | App-wide (cards, lists, states) |

### Naming Convention Rules

| Prefix | Location | When to Use |
|--------|----------|-------------|
| `Kpt*` | core-base/designsystem | Theme, layout, animation components |
| `Mifos*` | core/designsystem | UI primitives (Button, TextField, Dialog) |
| `Mifos*` | core/ui | Business components (Card, Item, State) |
| `[Feature]*` | feature/[name]/components | Feature-shared components |
| `[Screen]*` | feature/[name]/[screen]/components | Screen-specific components |

### Component Type by Name Pattern

| Pattern | Type | Look In |
|---------|------|---------|
| `*Button` | Action | `core/designsystem` |
| `*TextField`, `*Field` | Input | `core/designsystem` |
| `*Dialog`, `*Sheet` | Modal | `core/designsystem` |
| `*Card` | Container | `core/ui` |
| `*Item` | List item | `core/ui` |
| `*Component`, `*View` | Composite | `core/ui` |
| `*Grid`, `*Row`, `*Column` | Layout | `core-base/designsystem` |
| `*Scaffold`, `*Layout` | Structure | `core-base/designsystem` |

### Theme Tokens (Always Available)

```kotlin
// Spacing
KptTheme.spacing.xs   // 4.dp
KptTheme.spacing.sm   // 8.dp
KptTheme.spacing.md   // 16.dp
KptTheme.spacing.lg   // 24.dp
KptTheme.spacing.xl   // 32.dp

// Shapes
KptTheme.shapes.small   // 4.dp rounded
KptTheme.shapes.medium  // 8.dp rounded
KptTheme.shapes.large   // 16.dp rounded

// Colors
KptTheme.colorScheme.primary
KptTheme.colorScheme.onPrimary
KptTheme.colorScheme.surface
KptTheme.colorScheme.onSurface
KptTheme.colorScheme.error
```

---

## Screen Structure

### Directory Layout

```
feature/[name]/
├── [screen]/
│   ├── [Screen]Screen.kt           # Container composable
│   ├── [Screen]ViewModel.kt        # ViewModel
│   ├── [Screen].kt                 # State/Event/Action
│   └── components/                 # Screen-specific components
│       ├── [Screen]Header.kt
│       ├── [Screen]Content.kt
│       └── [Screen]Card.kt
├── components/                     # Feature-shared components
│   └── Shared[Feature]Component.kt
├── navigation/
└── di/
```

### File Naming

| File | Naming |
|------|--------|
| Screen | `[Screen]Screen.kt` |
| ViewModel | `[Screen]ViewModel.kt` |
| State/Event/Action | `[Screen].kt` |
| Components | `[Screen][Purpose].kt` |

---

## Container + Content Pattern

### Container Composable

Handles ViewModel integration, events, and dialogs:

```kotlin
@Composable
internal fun [Screen]Screen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: [Screen]ViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    // Handle one-time events
    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is [Screen]Event.NavigateBack -> onNavigateBack()
            is [Screen]Event.NavigateToDetail -> onNavigateToDetail(event.id)
            is [Screen]Event.ShowToast -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }

    // Render dialogs
    [Screen]Dialogs(
        dialogState = state.dialogState,
        onDismiss = remember(viewModel) {
            { viewModel.trySendAction([Screen]Action.OnDismissDialog) }
        },
    )

    // Render content
    [Screen]Content(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = remember(viewModel) { { viewModel.trySendAction(it) } },
        modifier = modifier,
    )
}
```

### Content Composable

Pure UI, testable:

```kotlin
@Composable
private fun [Screen]Content(
    state: [Screen]State,
    snackbarHostState: SnackbarHostState,
    onAction: ([Screen]Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBar = {
            MifosTopAppBar(
                title = stringResource(Res.string.screen_title),
                onNavigationClick = { onAction([Screen]Action.OnNavigateBack) },
            )
        },
        snackbarHost = { KptSnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        when (state.uiState) {
            is [Screen]ScreenState.Loading -> {
                [Screen]LoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is [Screen]ScreenState.Success -> {
                [Screen]SuccessContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is [Screen]ScreenState.Error -> {
                MifosErrorComponent(
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction([Screen]Action.OnRetry) },
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is [Screen]ScreenState.Empty -> {
                EmptyDataView(
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is [Screen]ScreenState.Network -> {
                NoInternet(
                    onRetry = { onAction([Screen]Action.OnRetry) },
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}
```

### Key Patterns

| Pattern | Purpose |
|---------|---------|
| `remember(viewModel)` | Memoize callbacks to prevent recomposition |
| `EventsEffect` | Lifecycle-aware event handling |
| `collectAsStateWithLifecycle` | Lifecycle-aware state collection |
| Separate dialogs | Keep dialog logic isolated |

---

## Section-Based Design

### Rule: Break Screens into Sections

**Bad** - Monolithic screen:
```kotlin
@Composable
private fun AccountContent(state: AccountState) {
    Column {
        // 200+ lines of mixed UI code
    }
}
```

**Good** - Section-based:
```kotlin
@Composable
private fun [Screen]SuccessContent(
    state: [Screen]State,
    onAction: ([Screen]Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        // Header Section
        item {
            [Screen]HeaderSection(
                title = state.title,
                subtitle = state.subtitle,
            )
        }

        // Summary Section
        item {
            [Screen]SummarySection(
                balance = state.balance,
                currency = state.currency,
            )
        }

        // Actions Section
        item {
            [Screen]ActionsSection(
                onTransfer = { onAction([Screen]Action.OnTransfer) },
                onWithdraw = { onAction([Screen]Action.OnWithdraw) },
            )
        }

        // List Header
        item {
            SectionHeader(title = stringResource(Res.string.transactions))
        }

        // List Items
        items(
            items = state.transactions,
            key = { it.id }
        ) { transaction ->
            TransactionScreenItem(
                transaction = transaction,
                onClick = { onAction([Screen]Action.OnTransactionClick(it.id)) },
            )
        }
    }
}
```

### Section Component Template

```kotlin
// feature/[name]/[screen]/components/[Screen]HeaderSection.kt

@Composable
internal fun [Screen]HeaderSection(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(KptTheme.spacing.md),
    ) {
        Text(
            text = title,
            style = MifosTypography.titleLarge,
            color = KptTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))
        Text(
            text = subtitle,
            style = MifosTypography.bodyMedium,
            color = KptTheme.colorScheme.onSurfaceVariant,
        )
    }
}
```

---

## Component Placement Rules

### Decision Tree

```
Creating a new component?
│
├── Is it a UI primitive (Button variant, TextField variant)?
│   └── YES → core/designsystem/component/
│
├── Used in 2+ features?
│   └── YES → core/ui/component/
│
├── Used across screens in same feature?
│   └── YES → feature/[name]/components/
│
└── Used only in this screen?
    └── YES → feature/[name]/[screen]/components/
```

### Examples

| Component | Location | Reason |
|-----------|----------|--------|
| `MifosOutlinedButton` | `core/designsystem/` | UI primitive |
| `TransactionScreenItem` | `core/ui/` | Used in accounts, home, recent |
| `AuthHeader` | `feature/auth/components/` | Used in login, register, otp |
| `LoginForm` | `feature/auth/login/components/` | Only in login screen |

---

## Reusability Rules

### When to Move to core/ui

Move a component when:
1. **Used in 2+ features**
2. **Represents a business concept** (Account, Transaction, Beneficiary)
3. **Has consistent behavior** across uses

```kotlin
// Step 1: Identify repeated component
// feature/accounts/components/AccountCard.kt
// feature/home/components/AccountCard.kt  <- Duplication!

// Step 2: Move to core/ui
// core/ui/component/MifosAccountCard.kt

// Step 3: Update imports everywhere
import org.mifos.mobile.core.ui.component.MifosAccountCard
```

### When to Move to Feature components/

Move from screen-specific when:
1. Used in 2+ screens within same feature
2. Shared styling/behavior within feature

---

## Loading States

### Shimmer Loading

```kotlin
@Composable
private fun [Screen]LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(KptTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
    ) {
        // Header skeleton
        KptShimmerLoadingBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(KptTheme.shapes.medium),
        )

        // Card skeletons
        repeat(3) {
            KptShimmerLoadingBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(KptTheme.shapes.medium),
            )
        }
    }
}
```

### Inline Loading

```kotlin
if (state.isLoading) {
    MifosProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
    )
}
```

### Overlay Loading

```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    // Content
    [Screen]SuccessContent(...)

    // Loading overlay
    if (state.showOverlay) {
        MifosLoadingDialog()
    }
}
```

---

## Error States

### Full Screen Error

```kotlin
MifosErrorComponent(
    message = stringResource(state.uiState.message),
    onRetry = { onAction([Screen]Action.OnRetry) },
    modifier = Modifier.padding(paddingValues),
)
```

### Inline Error

```kotlin
if (state.error != null) {
    Text(
        text = state.error,
        color = KptTheme.colorScheme.error,
        style = MifosTypography.bodySmall,
    )
}
```

---

## Empty States

```kotlin
EmptyDataView(
    icon = Icons.Default.Inbox,
    title = stringResource(Res.string.no_data_title),
    message = stringResource(Res.string.no_data_message),
    modifier = Modifier.padding(paddingValues),
)
```

---

## Dialog Management

### Dialog State in ViewModel State

```kotlin
@Immutable
data class [Screen]State(
    // ...
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data class ConfirmDelete(val item: Item) : DialogState
        data object Success : DialogState
    }
}
```

### Dialog Composable

```kotlin
@Composable
private fun [Screen]Dialogs(
    dialogState: [Screen]State.DialogState?,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit = {},
) {
    when (dialogState) {
        is [Screen]State.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = dialogState.message,
                ),
                onDismissRequest = onDismiss,
            )
        }
        is [Screen]State.DialogState.ConfirmDelete -> {
            MifosAlertDialog(
                title = stringResource(Res.string.confirm_delete),
                message = stringResource(Res.string.delete_message),
                confirmText = stringResource(Res.string.delete),
                onConfirm = onConfirmDelete,
                onDismiss = onDismiss,
            )
        }
        is [Screen]State.DialogState.Success -> {
            MifosSuccessDialog(
                message = stringResource(Res.string.success_message),
                onDismiss = onDismiss,
            )
        }
        null -> Unit
    }
}
```

---

## Theming Guidelines

### NEVER Use Hardcoded Values

```kotlin
// WRONG - Hardcoded values
Column(
    modifier = Modifier.padding(16.dp)  // Hardcoded
) {
    Text(
        text = "Title",
        fontSize = 24.sp,              // Hardcoded
        color = Color(0xFF1A1A1A),     // Hardcoded
    )
}

// CORRECT - Theme tokens
Column(
    modifier = Modifier.padding(KptTheme.spacing.md)
) {
    Text(
        text = stringResource(Res.string.title),  // String resource
        style = MifosTypography.titleLarge,       // Typography token
        color = KptTheme.colorScheme.onSurface,   // Color token
    )
}
```

### Theme Token Quick Reference

> Full theme tokens defined in [Available Components](#theme-tokens-always-available) section.

| Token Type | Access | Examples |
|------------|--------|----------|
| Spacing | `KptTheme.spacing.*` | `xs`, `sm`, `md`, `lg`, `xl` |
| Shapes | `KptTheme.shapes.*` | `small`, `medium`, `large` |
| Colors | `KptTheme.colorScheme.*` | `primary`, `surface`, `error` |
| Typography | `MifosTypography.*` | `titleLarge`, `bodyMedium` |

### Theming Rules

1. **NEVER** use hardcoded `dp`, `sp`, or `Color()` values
2. **ALWAYS** use `KptTheme.spacing.*` for padding/margin
3. **ALWAYS** use `KptTheme.shapes.*` for corner radius
4. **ALWAYS** use `KptTheme.colorScheme.*` for colors
5. **ALWAYS** use `MifosTypography.*` for text styles
6. **ALWAYS** use `stringResource()` for user-facing text

---

## Preview Patterns

### Content Preview

```kotlin
@Preview
@Composable
private fun [Screen]ContentPreview() {
    MifosMobileTheme {
        [Screen]Content(
            state = [Screen]State(
                uiState = [Screen]ScreenState.Success,
                items = listOf(
                    Item(id = 1, name = "Item 1"),
                    Item(id = 2, name = "Item 2"),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
        )
    }
}
```

### State Previews

```kotlin
@Preview
@Composable
private fun [Screen]LoadingPreview() {
    MifosMobileTheme {
        [Screen]LoadingContent()
    }
}

@Preview
@Composable
private fun [Screen]ErrorPreview() {
    MifosMobileTheme {
        MifosErrorComponent(
            message = "Failed to load data",
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun [Screen]EmptyPreview() {
    MifosMobileTheme {
        EmptyDataView()
    }
}
```

### Component Preview

```kotlin
@Preview
@Composable
private fun [Screen]HeaderSectionPreview() {
    MifosMobileTheme {
        [Screen]HeaderSection(
            title = "Account Details",
            subtitle = "Savings Account",
        )
    }
}
```

---

## Checklist for New Screens

Before creating a new screen:

### Component Lookup (REQUIRED)
- [ ] **Step 1**: Check static registry in [core-layer/COMPONENTS.md](../../core-layer/COMPONENTS.md)
- [ ] **Step 2**: If not found, run dynamic search commands
- [ ] **Step 3**: If found dynamically, update the static registry
- [ ] Verify no duplicate exists before creating new component

### Screen Design
- [ ] Plan sections (Header, Content, Actions, List)
- [ ] Identify reusable components (don't create duplicates!)
- [ ] Follow naming conventions (`Kpt*`, `Mifos*`, `[Feature]*`)

### Implementation
- [ ] Follow Container + Content pattern
- [ ] Use theme tokens ONLY (no hardcoded values)
- [ ] Use `stringResource()` for all text
- [ ] Use `remember(viewModel)` for callbacks

### States
- [ ] Add loading state with `KptShimmerLoadingBox`
- [ ] Add error state with `MifosErrorComponent`
- [ ] Add empty state with `EmptyDataView`
- [ ] Add network error state with `NoInternet`
- [ ] Handle dialogs separately

### Verification
- [ ] Create previews for each state
- [ ] No hardcoded `dp`, `sp`, `Color()` values
- [ ] All strings in `strings.xml`

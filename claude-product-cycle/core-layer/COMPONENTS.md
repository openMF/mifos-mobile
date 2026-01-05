# Component Discovery Guide

> **Purpose**: Fast component lookup with automatic updates
> **Pattern**: Static first → Dynamic fallback → Auto-update

---

## Table of Contents
1. [Lookup Strategy](#lookup-strategy)
2. [Static Component Registry](#static-component-registry)
3. [Dynamic Discovery](#dynamic-discovery)
4. [Naming Conventions](#naming-conventions)
5. [Auto-Update Rules](#auto-update-rules)
6. [Component Placement](#component-placement)

---

## Lookup Strategy

```
┌─────────────────────────────────────────────────────────────────┐
│  STEP 1: Check Static Registry (Fast)                          │
│  → Look in tables below for existing component                  │
├─────────────────────────────────────────────────────────────────┤
│  STEP 2: If Not Found → Dynamic Search (Fallback)               │
│  → Run discovery commands to find in source                     │
├─────────────────────────────────────────────────────────────────┤
│  STEP 3: If Found Dynamically → Update Static Registry          │
│  → Add new component to appropriate table below                 │
└─────────────────────────────────────────────────────────────────┘
```

**Why This Pattern:**
- Static lookup is instant (read from file)
- Dynamic search catches new components
- Auto-update keeps registry current

---

## Static Component Registry

### Foundation Components (core-base/designsystem)

**Prefix: `Kpt*`**

#### Components (`component/`)
| Component | Purpose | Usage |
|-----------|---------|-------|
| `KptTopAppBar` | Configurable app bar | Standard/Large/Medium variants |
| `KptShimmerLoadingBox` | Skeleton loading | Loading placeholders |
| `KptSnackbarHost` | Snackbar container | Toast messages |
| `KptAnimationSpecs` | Animation specifications | Standard animations |
| `BounceAnimation` | Bounce effect | Button press feedback |
| `SlideTransition` | Slide animation | Screen transitions |

#### Layouts (`layout/`)
| Layout | Purpose | Usage |
|--------|---------|-------|
| `KptGrid` | Responsive grid | Card grids |
| `KptFlowRow` | Horizontal flow | Tag/chip layouts |
| `KptFlowColumn` | Vertical flow | Wrapping columns |
| `KptStack` | Z-axis stacking | Overlays |
| `KptMasonryGrid` | Masonry layout | Pinterest-style |
| `KptResponsiveLayout` | Adaptive layout | Screen size adaptation |
| `KptSidebarLayout` | Sidebar with content | Navigation drawer |
| `KptSplitPane` | Resizable split | Two-panel layout |
| `AdaptiveListDetailPaneScaffold` | List-detail adaptive | Master-detail |
| `AdaptiveNavigableListDetailScaffold` | Navigable list-detail | Navigable master-detail |
| `AdaptiveNavigableSupportingPaneScaffold` | Supporting pane | Three-pane layout |
| `AdaptiveNavigationSuiteScaffold` | Navigation suite | Adaptive navigation |

#### Theme Tokens
| Token | Access | Values |
|-------|--------|--------|
| Spacing | `KptTheme.spacing.*` | `xs`(4dp), `sm`(8dp), `md`(16dp), `lg`(24dp), `xl`(32dp) |
| Shapes | `KptTheme.shapes.*` | `small`, `medium`, `large` |
| Colors | `KptTheme.colorScheme.*` | Material3 color scheme |

---

### Design System Components (core/designsystem)

**Prefix: `Mifos*`**

| Component | Purpose | Usage |
|-----------|---------|-------|
| `MifosScaffold` | Screen scaffold | Top bar, bottom bar, content |
| `MifosTopAppBar` | App bar | Navigation icon, title, actions |
| `MifosTopBar` | Simple title bar | Title only |
| `MifosButton` | Primary button | Main actions |
| `MifosTextField` | Text input | Form fields |
| `MifosPasswordField` | Password input | Visibility toggle |
| `MifosOtpTextField` | OTP input | Verification codes |
| `MifosSearchTextField` | Search input | Search bars |
| `MifosCard` | Card container | Content cards |
| `MifosBottomSheet` | Bottom sheet | Modal content |
| `MifosAlertDialog` | Alert dialog | Confirmations |
| `MifosBasicDialog` | Basic dialog | Simple messages |
| `MifosLoadingDialog` | Loading dialog | Blocking loader |
| `MifosTab` | Tab item | Tab navigation |
| `MifosTabPager` | Tab pager | Swipeable tabs |
| `MifosDropDownMenu` | Dropdown menu | Selection menu |
| `MifosRadioButton` | Radio button | Single selection |
| `MifosNavigation` | Navigation | Nav components |

---

### Business Components (core/ui)

**Prefix: `Mifos*` or descriptive name**

#### Cards
| Component | Purpose | Usage |
|-----------|---------|-------|
| `MifosAccountCard` | Account display | Account list items |
| `MifosDetailsCard` | Detail display | Information cards |
| `MifosDashboardCard` | Dashboard item | Home dashboard |
| `MifosActionCard` | Action card | Clickable actions |
| `MifosItemCard` | Generic item | List items |
| `MifosLabelValueCard` | Key-value | Detail rows |
| `MifosPoweredCard` | Footer card | "Powered by" |
| `MifosTitleSearchCard` | Title + search | Searchable headers |

#### Lists & Items
| Component | Purpose | Usage |
|-----------|---------|-------|
| `BeneficiaryCard` | Beneficiary item | Beneficiary list |
| `BeneficiariesListing` | Beneficiary list | Full list view |
| `TransactionScreenItem` | Transaction item | Transaction list |
| `FaqItemHolder` | FAQ item | Expandable FAQ |
| `AboutUsItemCard` | About item | About section |
| `MonitorListItemWithIcon` | Icon list item | Settings list |

#### States
| Component | Purpose | Usage |
|-----------|---------|-------|
| `MifosErrorComponent` | Error state | Error display |
| `EmptyDataView` | Empty state | No data |
| `NoInternet` | Network error | Offline state |
| `MifosProgressIndicator` | Loading spinner | Inline loading |
| `MifosStatusComponent` | Status badge | Status display |
| `MifosSuccessDialog` | Success dialog | Confirmation |

#### Forms
| Component | Purpose | Usage |
|-----------|---------|-------|
| `MifosDropDownTextField` | Dropdown field | Form dropdowns |
| `MifosOutlineDropDown` | Outlined dropdown | Outlined variant |
| `MifosDropDownPayFromComponent` | Pay from selector | Transfer forms |
| `MifosCheckBox` | Checkbox | Multi-select |
| `MFStepProcess` | Step indicator | Multi-step forms |
| `FilterTopSection` | Filter header | List filters |

#### User/Profile
| Component | Purpose | Usage |
|-----------|---------|-------|
| `MifosUserImage` | User avatar | Profile images |
| `MifosTextUserImage` | Text avatar | Initials avatar |
| `UserProfileField` | Profile field | Profile display |
| `MifosHiddenTextRow` | Hidden text | Sensitive data |

#### Other
| Component | Purpose | Usage |
|-----------|---------|-------|
| `MifosRoundIcon` | Round icon button | FAB-like |
| `MifosLinkText` | Link text | Clickable links |
| `MifosTextButtonWithTopDrawable` | Text button + icon | Icon buttons |
| `MifosMobileIcon` | App icon | Branding |
| `MifosRadioButtonAlertDialog` | Radio dialog | Selection dialog |
| `MifosAlertDialog` | Alert dialog | Confirmations |
| `MifosTexts` | Text styles | Styled text |

---

## Dynamic Discovery

### When to Use

Use dynamic discovery when:
1. Component not found in static registry above
2. Searching for recently added components
3. Unsure if component exists

### Discovery Commands

```bash
# Foundation components (Kpt*)
ls core-base/designsystem/src/commonMain/kotlin/**/component/
ls core-base/designsystem/src/commonMain/kotlin/**/layout/

# Design system components (Mifos* in designsystem)
ls core/designsystem/src/commonMain/kotlin/**/component/

# Business components (core/ui)
ls core/ui/src/commonMain/kotlin/**/component/
```

### Search by Type

```bash
# Find all Button components
grep -r "@Composable" core/ core-base/ | grep -i "button"

# Find all Card components
grep -r "@Composable" core/ core-base/ | grep -i "card"

# Find all Dialog components
grep -r "@Composable" core/ core-base/ | grep -i "dialog"

# Find all TextField/Input components
grep -r "@Composable" core/ core-base/ | grep -iE "(textfield|input|field)"

# Find loading/progress components
grep -r "@Composable" core/ core-base/ | grep -iE "(loading|progress|shimmer)"

# Find error/empty state components
grep -r "@Composable" core/ core-base/ | grep -iE "(error|empty|nodata)"
```

### Claude Glob Patterns

```
core-base/designsystem/**/component/*.kt
core-base/designsystem/**/layout/*.kt
core/designsystem/**/component/*.kt
core/ui/**/component/*.kt
```

---

## Naming Conventions

### Prefix Rules

| Prefix | Location | Purpose |
|--------|----------|---------|
| `Kpt*` | core-base/designsystem | Foundation/Theme |
| `Mifos*` | core/designsystem | UI primitives |
| `Mifos*` | core/ui | Business components |
| `[Feature]*` | feature/[name]/components | Feature-shared |
| `[Screen]*` | feature/[name]/[screen]/components | Screen-specific |

### Component Type by Name Pattern

| Pattern | Type | Look In |
|---------|------|---------|
| `*Button` | Action | core/designsystem |
| `*TextField`, `*Field` | Input | core/designsystem |
| `*Dialog`, `*Sheet` | Modal | core/designsystem |
| `*Card` | Container | core/ui |
| `*Item` | List item | core/ui |
| `*Component`, `*View` | Composite | core/ui |
| `*Indicator` | Feedback | core/ui |
| `*Grid`, `*Row`, `*Column` | Layout | core-base |
| `*Scaffold`, `*Layout` | Structure | core-base |

---

## Auto-Update Rules

### When to Update This File

| Scenario | Action |
|----------|--------|
| Found in static registry | No update needed |
| Found via dynamic search | ADD to static registry |
| Created new component in core/ | ADD to static registry |
| Created feature component | No update (not in registry) |

### How to Update

When you find a component dynamically that's not in the static registry:

1. Identify the correct table (Foundation/Design System/Business)
2. Add a new row with: Component | Purpose | Usage
3. Keep tables alphabetically sorted within categories

**Example:**
```markdown
| `NewMifosComponent` | Brief purpose | When to use |
```

### What NOT to Update

- Feature-specific components (`feature/*/components/`)
- Screen-specific components (`feature/*/[screen]/components/`)
- Temporary or experimental components

---

## Component Placement

### Decision Tree

```
Creating a new component?
│
├── Is it a theme/layout primitive?
│   └── YES → core-base/designsystem (Kpt*)
│
├── Is it a UI primitive (Button, TextField)?
│   └── YES → core/designsystem (Mifos*)
│
├── Used in 2+ features?
│   └── YES → core/ui (Mifos*)
│
├── Used across screens in same feature?
│   └── YES → feature/[name]/components/
│
└── Used only in one screen?
    └── YES → feature/[name]/[screen]/components/
```

### After Creating in core/

If you create a new component in `core/` or `core-base/`:
1. **ADD it to the static registry above**
2. Follow the naming convention
3. Include Purpose and Usage columns

---

## Quick Reference

### I need a...

| Need | Check Static Table | If Not Found |
|------|-------------------|--------------|
| Button | Design System Components | `grep -i button core/` |
| Card | Business Components → Cards | `grep -i card core/` |
| List item | Business Components → Lists | `grep -i item core/` |
| Loading | Foundation Components | Already exists: `KptShimmerLoadingBox` |
| Error | Business Components → States | Already exists: `MifosErrorComponent` |
| Empty | Business Components → States | Already exists: `EmptyDataView` |
| Dialog | Design System Components | `grep -i dialog core/` |
| Layout | Foundation Layouts | `grep -i layout core-base/` |

---

## Related Files

- Feature Layer Guide: `feature-layer/LAYER_GUIDE.md`
- Compose Patterns: `feature-layer/instructions/COMPOSE.md`
- Design Spec Patterns: `design-spec-layer/_shared/PATTERNS.md`

---

## Changelog

| Date | Change |
|------|--------|
| 2025-01-05 | Created with hybrid static + dynamic approach |

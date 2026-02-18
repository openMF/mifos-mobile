# /verify - Implementation Verification

## Purpose

Validate implementation matches specification using O(1) lookup. Compares SPEC.md requirements against actual code and identifies gaps with actionable fixes.

---

## Command Variants

```
/verify                          # Show all features verification status
/verify [Feature]                # Full verification for feature
/verify [Feature] --quick        # Skip detailed code analysis
/verify [Feature] --spec         # Verify spec completeness only
/verify [Feature] --code         # Verify code completeness only
/verify all                      # Verify all features (summary)
```

---

## Verification Pipeline with O(1) Optimization

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  /verify [Feature] - O(1) OPTIMIZED PIPELINE                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  PHASE 0: O(1) CONTEXT LOADING                                              │
│  ├─→ Read FEATURES_INDEX.md           → Feature exists? Spec status?        │
│  ├─→ Read FEATURE_MAP.md              → Expected services/repos             │
│  ├─→ Read MODULES_INDEX.md            → Expected VMs/Screens                │
│  ├─→ Read SCREENS_INDEX.md            → Screen-ViewModel mapping            │
│  └─→ Read API_INDEX.md                → Expected endpoints                  │
│                                                                              │
│  PHASE 1: SPEC ANALYSIS                                                     │
│  ├─→ Read features/[name]/SPEC.md     → Extract requirements                │
│  ├─→ Read features/[name]/API.md      → Extract API requirements            │
│  ├─→ Read features/[name]/STATUS.md   → Current status claims               │
│  └─→ Build requirement checklist      → What SHOULD exist                   │
│                                                                              │
│  PHASE 2: CODE ANALYSIS (O(1) paths from indexes)                           │
│  ├─→ Check ViewModel exists           → From SCREENS_INDEX.md path          │
│  ├─→ Check Screen exists              → From SCREENS_INDEX.md path          │
│  ├─→ Check Service exists             → From FEATURE_MAP.md path            │
│  ├─→ Check Repository exists          → From FEATURE_MAP.md path            │
│  └─→ Build implementation checklist   → What DOES exist                     │
│                                                                              │
│  PHASE 3: DEEP VERIFICATION (if not --quick)                                │
│  ├─→ Read ViewModel code              → Check State/Event/Action            │
│  ├─→ Read Screen code                 → Check UI states, TestTags           │
│  ├─→ Compare SPEC actions vs code     → All actions handled?                │
│  ├─→ Compare SPEC states vs code      → All states rendered?                │
│  └─→ Check DI registration            → Koin modules complete?              │
│                                                                              │
│  PHASE 4: GAP DETECTION                                                     │
│  ├─→ Compare requirement vs impl      → Identify missing items              │
│  ├─→ Categorize gaps by severity      → P0 (critical) → P2 (polish)         │
│  ├─→ Generate fix suggestions         → Actionable steps                    │
│  └─→ Calculate verification score     → Percentage complete                 │
│                                                                              │
│  PHASE 5: REPORT & UPDATE                                                   │
│  ├─→ Generate verification report     → Structured output                   │
│  ├─→ Update STATUS.md (optional)      → If user approves                    │
│  └─→ Suggest next command             → /implement or /gap-planning         │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## PHASE 0: O(1) Context Loading

### Files to Read (~500 lines total instead of scanning)

| File | Purpose | Data Extracted |
|------|---------|----------------|
| `design-spec-layer/FEATURES_INDEX.md` | Feature inventory | featureExists, specStatus |
| `client-layer/FEATURE_MAP.md` | Service/Repo mapping | expectedServices[], expectedRepos[] |
| `feature-layer/MODULES_INDEX.md` | Module structure | expectedVMs, expectedScreens |
| `feature-layer/SCREENS_INDEX.md` | Screen details | screenPaths[], vmPaths[] |
| `server-layer/API_INDEX.md` | Endpoint inventory | expectedEndpoints[] |
| `testing-layer/TEST_TAGS_INDEX.md` | TestTag specs | expectedTags[], namingPattern |
| `testing-layer/LAYER_STATUS.md` | Test coverage | testCoverage, fakeRepos |

### Context Object Built

```kotlin
val context = VerifyContext(
    feature = "beneficiary",

    // From FEATURES_INDEX.md
    specExists = true,
    specStatus = "✅ Complete",

    // From FEATURE_MAP.md
    expectedServices = ["BeneficiaryService"],
    expectedRepositories = ["BeneficiaryRepository"],

    // From MODULES_INDEX.md
    expectedVMs = 4,
    expectedScreens = 4,

    // From SCREENS_INDEX.md
    screens = [
        Screen("BeneficiaryListScreen", "BeneficiaryListViewModel"),
        Screen("BeneficiaryDetailScreen", "BeneficiaryDetailViewModel"),
        Screen("BeneficiaryApplicationScreen", "BeneficiaryApplicationViewModel"),
        Screen("BeneficiaryApplicationConfirmationScreen", "BeneficiaryApplicationConfirmationViewModel")
    ],

    // From API_INDEX.md
    expectedEndpoints = [
        "GET /beneficiaries",
        "POST /beneficiaries",
        "PUT /beneficiaries/{id}",
        "DELETE /beneficiaries/{id}"
    ]
)
```

---

## PHASE 1: Spec Analysis

### Read Specification Files

```
design-spec-layer/features/[feature]/
├── SPEC.md     → UI sections, user actions, state model
├── API.md      → Required endpoints, DTOs
└── STATUS.md   → Claimed implementation status
```

### Extract Requirements from SPEC.md

```kotlin
val specRequirements = SpecRequirements(
    // From SPEC.md Section 2: Screen Layout
    uiSections = ["Header", "List", "EmptyState", "ErrorState", "LoadingState"],

    // From SPEC.md Section 3: User Interactions
    userActions = [
        Action("Retry", "Reload data on error"),
        Action("PullRefresh", "Refresh list"),
        Action("ItemClick", "Navigate to detail"),
        Action("AddClick", "Navigate to add form"),
        Action("DeleteClick", "Delete with confirmation")
    ],

    // From SPEC.md Section 4: State Model
    stateFields = ["data", "uiState", "isRefreshing", "selectedItem"],
    screenStates = ["Loading", "Success", "Error", "Empty"],

    // From SPEC.md Section 5: API Requirements
    apiEndpoints = ["GET /beneficiaries", "POST /beneficiaries", ...]
)
```

---

## PHASE 2: Code Analysis (O(1) Paths)

### File Paths from Index Files

| Component | Path Source | Example Path |
|-----------|-------------|--------------|
| ViewModel | SCREENS_INDEX.md | `feature/beneficiary/.../viewmodel/BeneficiaryListViewModel.kt` |
| Screen | SCREENS_INDEX.md | `feature/beneficiary/.../ui/BeneficiaryListScreen.kt` |
| Service | FEATURE_MAP.md | `core/network/.../services/BeneficiaryService.kt` |
| Repository | FEATURE_MAP.md | `core/data/.../repository/BeneficiaryRepository.kt` |
| DI Module | MODULES_INDEX.md | `feature/beneficiary/.../di/BeneficiaryModule.kt` |

### Check File Existence

```kotlin
val codeAnalysis = CodeAnalysis(
    // File existence checks
    viewModelsExist = [true, true, true, true],  // 4/4
    screensExist = [true, true, true, true],      // 4/4
    serviceExists = true,
    repositoryExists = true,
    diModuleExists = true,

    // Navigation check
    navigationRegistered = true,

    // TestTags check
    testTagsExist = false  // Gap detected!
)
```

---

## PHASE 3: Deep Verification

### ViewModel Verification

```kotlin
// Read ViewModel and check:
val vmVerification = ViewModelVerification(
    // State class
    hasStateClass = true,
    stateFieldsMatch = compareFields(spec.stateFields, vm.stateFields),
    missingStateFields = ["selectedItem"],  // Gap!

    // Screen states
    hasScreenStates = true,
    screenStatesMatch = compareStates(spec.screenStates, vm.screenStates),
    missingScreenStates = [],

    // Actions
    hasActionInterface = true,
    actionsMatch = compareActions(spec.userActions, vm.actions),
    missingActions = ["DeleteClick"],  // Gap!

    // Events
    hasEventInterface = true,
    eventsImplemented = true
)
```

### Screen Verification

```kotlin
// Read Screen and check:
val screenVerification = ScreenVerification(
    // UI states rendered
    hasLoadingState = true,
    hasSuccessState = true,
    hasErrorState = true,
    hasEmptyState = false,  // Gap!

    // TestTags
    hasTestTags = false,  // Gap!
    testTagsObject = null,

    // Event collection
    collectsEvents = true,

    // Content separation
    hasContentComposable = true
)
```

### DI Verification

```kotlin
val diVerification = DiVerification(
    viewModelRegistered = true,
    repositoryRegistered = true,
    serviceRegistered = true
)
```

---

## PHASE 4: Gap Detection

### Gap Categories

| Severity | Description | Examples |
|:--------:|-------------|----------|
| P0 | Critical - App won't work | Missing ViewModel, Service not registered |
| P1 | Major - Feature incomplete | Missing action handler, Empty state |
| P2 | Minor - Polish needed | Missing TestTags, Missing Preview |

### Gap Report Structure

```kotlin
val gaps = GapReport(
    feature = "beneficiary",
    score = 85,  // 85% complete

    p0Gaps = [],  // None - critical items present

    p1Gaps = [
        Gap(
            category = "ViewModel",
            item = "DeleteClick action",
            specReference = "SPEC.md Section 3.5",
            suggestedFix = "Add DeleteClick to BeneficiaryAction sealed interface"
        ),
        Gap(
            category = "Screen",
            item = "Empty state",
            specReference = "SPEC.md Section 2.4",
            suggestedFix = "Add BeneficiaryEmpty composable when data.isEmpty()"
        )
    ],

    p2Gaps = [
        Gap(
            category = "Testing",
            item = "TestTags object",
            specReference = "Testing standards",
            suggestedFix = "Add BeneficiaryTestTags object with feature:component pattern"
        )
    ]
)
```

---

## PHASE 5: Report Generation

### Full Verification Report

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║  /verify beneficiary - VERIFICATION REPORT                                    ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  📊 VERIFICATION SCORE: 85%  [████████░░]                                     ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  📚 O(1) CONTEXT LOADED                                                       ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  FEATURES_INDEX.md  → Feature exists: ✅  Spec status: ✅ Complete            ║
║  FEATURE_MAP.md     → Services: 1 expected  Repos: 1 expected                 ║
║  MODULES_INDEX.md   → VMs: 4 expected  Screens: 4 expected                    ║
║  SCREENS_INDEX.md   → 4 screen-VM mappings found                              ║
║  API_INDEX.md       → 4 endpoints expected                                    ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  ✅ PASSING CHECKS                                                            ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  CLIENT LAYER:                                                                 ║
║  ├─ BeneficiaryService.kt                     ✅ Exists                       ║
║  ├─ BeneficiaryRepository.kt                  ✅ Exists                       ║
║  ├─ BeneficiaryRepositoryImp.kt               ✅ Exists                       ║
║  ├─ NetworkModule registration                ✅ Registered                   ║
║  └─ RepositoryModule registration             ✅ Registered                   ║
║                                                                                ║
║  FEATURE LAYER:                                                                ║
║  ├─ BeneficiaryListViewModel.kt               ✅ Exists                       ║
║  ├─ BeneficiaryDetailViewModel.kt             ✅ Exists                       ║
║  ├─ BeneficiaryApplicationViewModel.kt        ✅ Exists                       ║
║  ├─ BeneficiaryApplicationConfirmationVM.kt   ✅ Exists                       ║
║  ├─ 4 Screen files                            ✅ All exist                    ║
║  ├─ BeneficiaryModule.kt                      ✅ DI registered                ║
║  └─ Navigation                                ✅ Configured                   ║
║                                                                                ║
║  STATE MODEL:                                                                  ║
║  ├─ State class                               ✅ Defined                      ║
║  ├─ ScreenState sealed interface              ✅ Loading/Success/Error        ║
║  ├─ Event sealed interface                    ✅ Navigation events            ║
║  └─ Action sealed interface                   ✅ User actions                 ║
║                                                                                ║
║  API INTEGRATION:                                                              ║
║  ├─ GET /beneficiaries                        ✅ Called                       ║
║  ├─ POST /beneficiaries                       ✅ Called                       ║
║  ├─ PUT /beneficiaries/{id}                   ✅ Called                       ║
║  └─ DELETE /beneficiaries/{id}                ✅ Called                       ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  ⚠️ GAPS FOUND (3)                                                            ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  P1 - MAJOR (2):                                                              ║
║  ┌────────────────────────────────────────────────────────────────────────┐   ║
║  │ Gap: Empty state not implemented                                       │   ║
║  │ Spec: SPEC.md Section 2.4 - "Show empty illustration when no data"     │   ║
║  │ File: feature/beneficiary/.../ui/BeneficiaryListScreen.kt              │   ║
║  │                                                                         │   ║
║  │ 📍 Fix:                                                                 │   ║
║  │ Add to BeneficiaryListScreen:                                          │   ║
║  │ ```kotlin                                                              │   ║
║  │ is BeneficiaryUiState.Empty -> {                                       │   ║
║  │     BeneficiaryEmpty(                                                  │   ║
║  │         onAddClick = { onAction(BeneficiaryAction.OnAddClick) }        │   ║
║  │     )                                                                  │   ║
║  │ }                                                                      │   ║
║  │ ```                                                                    │   ║
║  └────────────────────────────────────────────────────────────────────────┘   ║
║                                                                                ║
║  ┌────────────────────────────────────────────────────────────────────────┐   ║
║  │ Gap: selectedItem state field missing                                  │   ║
║  │ Spec: SPEC.md Section 4.1 - State includes selectedItem for delete     │   ║
║  │ File: feature/beneficiary/.../viewmodel/BeneficiaryListViewModel.kt    │   ║
║  │                                                                         │   ║
║  │ 📍 Fix:                                                                 │   ║
║  │ Add to BeneficiaryState:                                               │   ║
║  │ ```kotlin                                                              │   ║
║  │ val selectedItem: Beneficiary? = null,                                 │   ║
║  │ ```                                                                    │   ║
║  └────────────────────────────────────────────────────────────────────────┘   ║
║                                                                                ║
║  P2 - MINOR (1):                                                              ║
║  ┌────────────────────────────────────────────────────────────────────────┐   ║
║  │ Gap: TestTags object missing                                           │   ║
║  │ Spec: Testing standards - All screens should have TestTags             │   ║
║  │ File: feature/beneficiary/.../ui/BeneficiaryTestTags.kt (create)       │   ║
║  │                                                                         │   ║
║  │ 📍 Fix: Run /feature beneficiary --tags to generate                    │   ║
║  └────────────────────────────────────────────────────────────────────────┘   ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  📋 SUMMARY                                                                   ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  | Category      | Expected | Found | Score |                                 ║
║  |---------------|:--------:|:-----:|:-----:|                                 ║
║  | Client Layer  | 5        | 5     | 100%  |                                 ║
║  | Feature Layer | 10       | 9     | 90%   |                                 ║
║  | State Model   | 8        | 7     | 87%   |                                 ║
║  | API Calls     | 4        | 4     | 100%  |                                 ║
║  | Testing       | 2        | 1     | 50%   |                                 ║
║  |---------------|----------|-------|-------|                                 ║
║  | TOTAL         | 29       | 26    | 85%   |                                 ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  🎯 NEXT STEPS                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  Options:                                                                      ║
║  • f / fix       → Run /implement beneficiary to auto-fix gaps                ║
║  • m / manual    → Fix gaps manually using suggestions above                  ║
║  • u / update    → Update STATUS.md to reflect current state                  ║
║  • i / ignore    → Mark gaps as intentional (document reason)                 ║
║                                                                                ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## All Features Verification (No Argument)

When `/verify` called without arguments, show summary from index files:

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║  /verify - ALL FEATURES VERIFICATION STATUS                                   ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  Data from: FEATURES_INDEX.md, MODULES_INDEX.md, FEATURE_MAP.md               ║
║                                                                                ║
║  | # | Feature           | Spec | Client | Feature | Score | Gaps |           ║
║  |:-:|-------------------|:----:|:------:|:-------:|:-----:|:----:|           ║
║  | 1 | auth              | ✅   | ✅     | ✅      | 95%   | 1    |           ║
║  | 2 | home              | ✅   | ✅     | ✅      | 100%  | 0    |           ║
║  | 3 | accounts          | ✅   | ✅     | ✅      | 98%   | 1    |           ║
║  | 4 | beneficiary       | ✅   | ✅     | ✅      | 85%   | 3    |           ║
║  | 5 | loan-account      | ✅   | ✅     | ✅      | 92%   | 2    |           ║
║  | 6 | savings-account   | ✅   | ✅     | ✅      | 90%   | 2    |           ║
║  | 7 | share-account     | ✅   | ✅     | ✅      | 88%   | 2    |           ║
║  | 8 | transfer          | ✅   | ✅     | ✅      | 95%   | 1    |           ║
║  | 9 | recent-transaction| ✅   | ✅     | ✅      | 100%  | 0    |           ║
║  | 10| notification      | ✅   | ✅     | ✅      | 100%  | 0    |           ║
║  | 11| settings          | ✅   | ✅     | ✅      | 85%   | 3    |           ║
║  | 12| passcode          | ✅   | -      | ✅      | 100%  | 0    |           ║
║  | 13| guarantor         | ✅   | ✅     | ✅      | 90%   | 2    |           ║
║  | 14| qr                | ✅   | -      | ✅      | 95%   | 1    |           ║
║  | 15| location          | ✅   | -      | ✅      | 80%   | 2    |           ║
║  | 16| client-charge     | ✅   | ✅     | ✅      | 92%   | 1    |           ║
║  | 17| dashboard         | ⚠️   | ❌     | ❌      | 20%   | 8    |           ║
║                                                                                ║
║  OVERALL: 89% verified  |  Total Gaps: 29                                     ║
║                                                                                ║
║  Commands:                                                                     ║
║  • /verify [feature]     → Detailed verification                              ║
║  • /verify all --fix     → Show all gaps with fixes                           ║
║  • /gap-planning feature → Plan to fix gaps                                   ║
║                                                                                ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## Verification Checklist (Quick Reference)

### Client Layer Checks

| Check | Source | Verification |
|-------|--------|--------------|
| Service exists | FEATURE_MAP.md | File exists at path |
| Repository exists | FEATURE_MAP.md | File exists at path |
| RepositoryImpl exists | FEATURE_MAP.md | File exists at path |
| NetworkModule registration | NetworkModule.kt | Contains service binding |
| RepositoryModule registration | RepositoryModule.kt | Contains repo binding |

### Feature Layer Checks

| Check | Source | Verification |
|-------|--------|--------------|
| ViewModel exists | SCREENS_INDEX.md | File exists at path |
| Screen exists | SCREENS_INDEX.md | File exists at path |
| DI Module exists | MODULES_INDEX.md | File exists at path |
| Navigation registered | Navigation graph | Contains route |

### State Model Checks

| Check | Source | Verification |
|-------|--------|--------------|
| State class defined | ViewModel file | `data class ${Feature}State` |
| ScreenState sealed | ViewModel file | `sealed interface ${Feature}UiState` |
| Event sealed | ViewModel file | `sealed interface ${Feature}Event` |
| Action sealed | ViewModel file | `sealed interface ${Feature}Action` |
| handleAction implemented | ViewModel file | `override fun handleAction` |

### UI State Checks

| Check | Source | Verification |
|-------|--------|--------------|
| Loading state | Screen file | `${Feature}UiState.Loading` branch |
| Success state | Screen file | `${Feature}UiState.Success` branch |
| Error state | Screen file | `${Feature}UiState.Error` branch |
| Empty state | Screen file | `${Feature}UiState.Empty` branch (if in spec) |

### Testing Checks

| Check | Source | Verification |
|-------|--------|--------------|
| TestTags object | Screen directory | `${Feature}TestTags.kt` exists |
| testTag modifiers | Screen file | `Modifier.testTag()` used |
| TestTag naming | TestTags object | Follows `feature:component:id` pattern |
| All states tagged | Screen file | Loading, Success, Error have tags |
| Interactive elements | Screen file | Buttons, inputs have tags |

---

## TestTag Validation (Enhanced)

### TestTag Naming Convention

Pattern: `feature:component:element`

| Component | Pattern | Example |
|-----------|---------|---------|
| Screen | `{feature}:screen` | `beneficiary:screen` |
| Loading | `{feature}:loading` | `beneficiary:loading` |
| Error | `{feature}:error` | `beneficiary:error` |
| List | `{feature}:list` | `beneficiary:list` |
| Item | `{feature}:item:{id}` | `beneficiary:item:123` |
| Button | `{feature}:{action}` | `beneficiary:retry`, `beneficiary:add` |
| Input | `{feature}:input:{name}` | `auth:input:username` |

### TestTag Validation Rules

```kotlin
val testTagValidation = TestTagValidation(
    // Required TestTags (P2 if missing)
    required = [
        "${feature}:screen",
        "${feature}:loading",
        "${feature}:error",
    ],

    // Recommended TestTags (suggestions only)
    recommended = [
        "${feature}:list",          // For list screens
        "${feature}:item:{id}",     // For list items
        "${feature}:retry",         // For error retry
        "${feature}:empty",         // For empty state
    ],

    // Validate naming convention
    namingConvention = regex("^[a-z-]+:[a-z-]+(?::[a-z0-9-]+)?$")
)
```

### TestTag Validation Report

```
╠═══════════════════════════════════════════════════════════════════════════════╣
║  🏷️ TESTTAG VALIDATION                                                        ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                                ║
║  TestTags Object: ${Feature}TestTags.kt                                        ║
║  Location: feature/${name}/.../ui/${Feature}TestTags.kt                        ║
║  Status: [✅ EXISTS | ❌ MISSING]                                              ║
║                                                                                ║
║  Required Tags:                                                                ║
║  ├─ ${feature}:screen    [✅ Found | ❌ Missing]                              ║
║  ├─ ${feature}:loading   [✅ Found | ❌ Missing]                              ║
║  └─ ${feature}:error     [✅ Found | ❌ Missing]                              ║
║                                                                                ║
║  Screen Usage:                                                                 ║
║  ├─ ${Feature}Screen.kt      testTag() calls: [n]                             ║
║  ├─ ${Feature}Content.kt     testTag() calls: [n]                             ║
║  └─ Total coverage: [n] / [expected]                                          ║
║                                                                                ║
║  Naming Convention:                                                            ║
║  ├─ Valid tags: [n]                                                           ║
║  └─ Invalid tags: [list of non-conforming tags]                               ║
║                                                                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
```

### TestTag Gap Examples

```
┌────────────────────────────────────────────────────────────────────────┐
│ Gap: TestTags object missing                                           │
│ Severity: P2 (Testing)                                                 │
│ File: feature/${name}/.../ui/${Feature}TestTags.kt (create)            │
│                                                                         │
│ 📍 Fix: Generate TestTags                                              │
│ ```kotlin                                                              │
│ internal object ${Feature}TestTags {                                   │
│     const val SCREEN = "${feature}:screen"                             │
│     const val LOADING = "${feature}:loading"                           │
│     const val ERROR = "${feature}:error"                               │
│     const val LIST = "${feature}:list"                                 │
│     const val RETRY_BUTTON = "${feature}:retry"                        │
│     const val ITEM_PREFIX = "${feature}:item:"  // + id               │
│ }                                                                      │
│ ```                                                                    │
│                                                                         │
│ Command: /feature ${feature} --tags                                    │
└────────────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────────────┐
│ Gap: Missing testTag modifiers in Screen                               │
│ Severity: P2 (Testing)                                                 │
│ File: feature/${name}/.../ui/${Feature}Screen.kt                       │
│                                                                         │
│ 📍 Fix: Add testTag modifiers to composables                           │
│ ```kotlin                                                              │
│ // Loading state                                                       │
│ MifosLoadingWheel(                                                     │
│     modifier = Modifier.testTag(${Feature}TestTags.LOADING)            │
│ )                                                                      │
│                                                                         │
│ // Error state                                                         │
│ MifosErrorContent(                                                     │
│     modifier = Modifier.testTag(${Feature}TestTags.ERROR)              │
│ )                                                                      │
│                                                                         │
│ // List                                                                │
│ LazyColumn(                                                            │
│     modifier = Modifier.testTag(${Feature}TestTags.LIST)               │
│ )                                                                      │
│ ```                                                                    │
└────────────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────────────┐
│ Gap: TestTag naming doesn't follow convention                          │
│ Severity: P2 (Polish)                                                  │
│ File: feature/${name}/.../ui/${Feature}TestTags.kt                     │
│                                                                         │
│ Found: "BeneficiaryScreen", "LoadingIndicator"                         │
│ Expected: "beneficiary:screen", "beneficiary:loading"                  │
│                                                                         │
│ 📍 Fix: Update to feature:component:element pattern                    │
│ ```kotlin                                                              │
│ // Before (invalid)                                                    │
│ const val SCREEN = "BeneficiaryScreen"                                 │
│ const val LOADING = "LoadingIndicator"                                 │
│                                                                         │
│ // After (valid)                                                       │
│ const val SCREEN = "beneficiary:screen"                                │
│ const val LOADING = "beneficiary:loading"                              │
│ ```                                                                    │
└────────────────────────────────────────────────────────────────────────┘
```

### TestTag Scoring

| Criterion | Weight | Passed | Score |
|-----------|:------:|:------:|:-----:|
| TestTags object exists | 40% | ✅/❌ | x/40 |
| Required tags defined | 30% | n/3 | x/30 |
| testTag() modifiers used | 20% | n/m | x/20 |
| Naming convention | 10% | n/n | x/10 |
| **Total** | 100% | | x/100 |

---

## Error Handling

### Feature Not Found

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ❌ FEATURE NOT FOUND                                                        │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Feature: "xyz"                                                              │
│  Checked: FEATURES_INDEX.md                                                  │
│                                                                               │
│  Did you mean one of these?                                                  │
│  • beneficiary                                                               │
│  • beneficiary-detail                                                        │
│                                                                               │
│  Or run /verify to see all features.                                         │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Spec Missing

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  ⚠️ SPEC MISSING                                                             │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  Feature: dashboard                                                          │
│  Expected: design-spec-layer/features/dashboard/SPEC.md                      │
│  Found: File does not exist                                                  │
│                                                                               │
│  Cannot verify without specification.                                        │
│                                                                               │
│  Options:                                                                     │
│  • d / design   → Run /design dashboard to create spec                       │
│  • c / code     → Verify code only (--code flag)                             │
│  • a / abort    → Cancel verification                                        │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## O(1) File Reference

| Index File | Data Used For |
|------------|---------------|
| `design-spec-layer/FEATURES_INDEX.md` | Feature list, spec status |
| `client-layer/FEATURE_MAP.md` | Service/Repository paths |
| `feature-layer/MODULES_INDEX.md` | Module structure, VM/Screen counts |
| `feature-layer/SCREENS_INDEX.md` | Screen-ViewModel mappings, file paths |
| `server-layer/API_INDEX.md` | Expected API endpoints |

---

## Related Commands

| Command | Purpose |
|---------|---------|
| `/implement [Feature]` | Fix gaps automatically |
| `/gap-analysis [Feature]` | Broader gap analysis |
| `/gap-planning [Feature]` | Plan fixes for gaps |
| `/design [Feature]` | Update specification |
| `/verify-tests [Feature]` | Verify test coverage |

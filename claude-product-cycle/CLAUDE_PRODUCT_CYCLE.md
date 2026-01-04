# Claude Product Cycle - Mifos Mobile

> **Vision**: AI-powered development framework for structured feature implementation
> **Project**: Mifos Mobile - Self-Service Banking Application (KMP)
> **Backend**: Apache Fineract Self-Service APIs
> **Created**: 2025-12-26

---

## Project Overview

Mifos Mobile is a Kotlin Multiplatform (KMP) application for financial institutions. It provides end-users with the ability to manage their accounts (Loan, Savings, Shares), beneficiaries, transfers, and view transactions. The application consumes the Apache Fineract Self-Service API.

### Tech Stack
- **Frontend**: Kotlin Multiplatform (Android, iOS, Desktop, Web)
- **UI**: Jetpack Compose Multiplatform
- **Architecture**: MVI (Model-View-Intent) with Clean Architecture
- **DI**: Koin
- **Networking**: Ktor + Ktorfit
- **Backend**: Apache Fineract (External API - not managed by this project)

---

## The Big Picture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     CLAUDE PRODUCT CYCLE FRAMEWORK                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ┌──────────────────────────────────────────────────────────────────────┐  │
│   │                    DEVELOPMENT CYCLE                                  │  │
│   │                                                                       │  │
│   │   /projectstatus    → See where you are                              │  │
│   │        │                                                              │  │
│   │        ▼                                                              │  │
│   │   /design [Feature] → Architect the feature (Opus)                   │  │
│   │        │                                                              │  │
│   │        ▼                                                              │  │
│   │   /implement [Feature] → Full E2E implementation                     │  │
│   │        │                                                              │  │
│   │        │   OR use layer commands:                                    │  │
│   │        │   /client [Feature]  → Network + Data layers                │  │
│   │        │   /feature [Feature] → UI layer (ViewModel + Screen)        │  │
│   │        │                                                              │  │
│   │        ▼                                                              │  │
│   │   /verify [Feature]  → Validate implementation vs spec               │  │
│   │                                                                       │  │
│   └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Framework Components

### Directory Structure

```
claude-product-cycle/
├── CLAUDE_PRODUCT_CYCLE.md          # This file - master documentation
├── COMMANDS.md                       # Quick command reference
│
├── commands-layer/                   # Slash commands for Claude Code
│   ├── design.md                    # /design - Architecture phase
│   ├── implement.md                 # /implement - E2E implementation
│   ├── client.md                    # /client - Network + Data layers
│   ├── feature.md                   # /feature - UI layer
│   ├── verify.md                    # /verify - Validation
│   └── projectstatus.md             # /projectstatus - Overview
│
├── design-spec-layer/               # Feature specifications
│   ├── STATUS.md                    # Single source of truth for all features
│   ├── _shared/                     # Shared patterns and guides
│   │   ├── PATTERNS.md              # Implementation patterns
│   │   └── API_REFERENCE.md         # Fineract API quick reference
│   └── features/                    # Individual feature bundles
│       └── [feature]/
│           ├── SPEC.md              # What to build (UI, flows)
│           ├── API.md               # APIs needed
│           └── STATUS.md            # Feature implementation status
│
└── server-layer/                    # Backend documentation
    └── FINERACT_API.md              # Fineract Self-Service API docs
```

---

## Command Reference

| Command | Phase | Purpose | Model |
|---------|-------|---------|-------|
| `/projectstatus` | Any | See project status, all commands | Any |
| `/design [Feature]` | Design | Create SPEC.md + API.md (architecture) | **Opus** |
| `/implement [Feature]` | Build | Full feature implementation (all layers) | Sonnet |
| `/client [Feature]` | Build | Network + Data + Domain layers | Sonnet |
| `/feature [Feature]` | Build | UI layer (ViewModel + Screen) | Sonnet |
| `/verify [Feature]` | QA | Validate implementation vs spec | Sonnet |

---

## Architecture Layers

### 1. Network Layer (`core/network/`)

**Purpose**: API communication with Fineract Self-Service endpoints

```kotlin
// Service interface using Ktorfit
interface ClientService {
    @GET(ApiEndPoints.CLIENTS)
    fun clients(): Flow<Page<Client>>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getClientAccounts(@Path("clientId") clientId: Long): Flow<ClientAccounts>
}
```

**Key Components**:
- `services/` - Ktorfit service interfaces
- `utils/ApiEndPoints.kt` - API endpoint constants
- `di/NetworkModule.kt` - DI registration

### 2. Data Layer (`core/data/`)

**Purpose**: Repository implementations, data transformations

```kotlin
interface HomeRepository {
    fun clientAccounts(clientId: Long): Flow<DataState<ClientAccounts>>
    fun currentClient(clientId: Long): Flow<DataState<Client>>
}
```

**Key Components**:
- `repository/` - Repository interfaces and implementations
- `di/DataModule.kt` - DI registration

### 3. Feature Layer (`feature/[name]/`)

**Purpose**: UI presentation with MVI architecture

```kotlin
// ViewModel with State, Event, Action pattern
class HomeViewModel(
    private val homeRepository: HomeRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeAction>(initialState) {
    override fun handleAction(action: HomeAction) { ... }
}

// Immutable UI State
@Immutable
data class HomeState(
    val clientAccounts: ClientAccounts? = null,
    val uiState: HomeScreenState = HomeScreenState.Loading,
)

// One-time Events (navigation, toasts)
sealed interface HomeEvent {
    data class Navigate(val route: String) : HomeEvent
}

// User Actions
sealed interface HomeAction {
    data object Retry : HomeAction
    data class OnNavigate(val route: String) : HomeAction
}
```

**Key Components**:
- `[Feature]ViewModel.kt` - MVI ViewModel
- `[Feature]Screen.kt` - Compose UI
- `components/` - Reusable composables
- `navigation/` - Navigation definition
- `di/[Feature]Module.kt` - DI registration

---

## Feature Catalog

| Feature | Module | Status | APIs |
|---------|--------|--------|------|
| Auth (Login/Register) | `feature:auth` | Done | authentication, registration |
| Home Dashboard | `feature:home` | Done | clients, accounts |
| Accounts Overview | `feature:accounts` | Done | clients/accounts |
| Savings Account | `feature:savings-account` | Done | savingsaccounts |
| Loan Account | `feature:loan-account` | Done | loans |
| Share Account | `feature:share-account` | Done | shareaccounts |
| Beneficiary | `feature:beneficiary` | Done | beneficiaries/tpt |
| Transfer | `feature:transfer-process` | Done | accounttransfers |
| Third Party Transfer | `feature:third-party-transfer` | Done | beneficiaries, accounttransfers |
| Recent Transactions | `feature:recent-transaction` | Done | savingsaccounts/transactions |
| Notifications | `feature:notification` | Done | notifications |
| Client Charges | `feature:client-charge` | Done | clients/charges |
| Guarantor | `feature:guarantor` | Done | loans/guarantors |
| QR Code | `feature:qr` | Done | - (local) |
| Settings | `feature:settings` | Done | user |
| Passcode | `feature:passcode` | Done | - (local) |
| Location | `feature:location` | Done | - (external maps) |

---

## Fineract Self-Service API Overview

Base URL: `https://{server}/fineract-provider/api/v1/self/`

### Core Endpoints

| Category | Endpoint | Methods |
|----------|----------|---------|
| Authentication | `authentication` | POST |
| Registration | `registration` | POST, GET |
| Clients | `clients` | GET |
| Savings | `savingsaccounts` | GET, POST, PUT |
| Loans | `loans` | GET, POST, PUT |
| Shares | `shareaccounts` | GET, POST |
| Beneficiaries | `beneficiaries/tpt` | GET, POST, PUT, DELETE |
| Transfers | `accounttransfers` | GET, POST |
| User | `user` | GET, PUT |

---

## Development Workflow

### 1. Design Phase (`/design [Feature]`)

```
1. Read existing code and understand current implementation
2. Create/update SPEC.md with:
   - ASCII mockups
   - User interactions
   - State model
   - API requirements
3. Create/update API.md with endpoint details
4. Update STATUS.md
```

### 2. Implementation Phase (`/implement [Feature]`)

```
1. Git: Create feature branch
2. Validate: Check dependencies
3. Client Layer:
   - Create/update DTOs in core/network/model/
   - Create/update Service in core/network/services/
   - Create/update Repository in core/data/repository/
   - Register in DI modules
4. Feature Layer:
   - Create/update ViewModel
   - Create/update Screen
   - Create/update Components
   - Register navigation
   - Register in DI module
5. Build & Test: ./gradlew build
6. Lint: ./gradlew spotlessApply detekt
7. Commit with conventional commits
```

### 3. Verification Phase (`/verify [Feature]`)

```
1. Compare SPEC.md vs actual code
2. Check all user actions implemented
3. Check all API calls present
4. Verify DI registration
5. Check navigation
6. Report gaps
```

---

## Patterns

### MVI ViewModel Pattern

```kotlin
class FeatureViewModel(
    private val repository: FeatureRepository,
) : BaseViewModel<FeatureState, FeatureEvent, FeatureAction>(
    initialState = FeatureState()
) {
    override fun handleAction(action: FeatureAction) {
        when (action) {
            is FeatureAction.Load -> loadData()
            is FeatureAction.Retry -> retry()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getData()
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> updateState { it.copy(isLoading = true) }
                        is DataState.Success -> updateState {
                            it.copy(isLoading = false, data = dataState.data)
                        }
                        is DataState.Error -> updateState {
                            it.copy(isLoading = false, error = dataState.message)
                        }
                    }
                }
        }
    }
}
```

### Repository Pattern

```kotlin
class FeatureRepositoryImpl(
    private val service: FeatureService,
) : FeatureRepository {
    override fun getData(): Flow<DataState<Data>> = flow {
        emit(DataState.Loading)
        try {
            val result = service.fetchData().first()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }
}
```

### Screen Pattern

```kotlin
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is FeatureEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    FeatureContent(
        state = state,
        onAction = viewModel::sendAction,
    )
}
```

---

## Cross-Update Rules

After ANY implementation:
1. Update feature's `STATUS.md`
2. Update main `claude-product-cycle/design-spec-layer/STATUS.md`
3. Add changelog entries
4. Run `./gradlew build` to verify

---

## Getting Started

This section provides everything you need to effectively use the Claude Product Cycle framework.

### Prerequisites

Before starting, ensure you have:

| Requirement | Details |
|-------------|---------|
| Claude Code CLI | Anthropic's CLI tool with slash command support |
| Project Access | Clone of `mifos-mobile` repository |
| Branch | Work from `development` or feature branches |
| Fineract Server | Access to demo: `gsoc.mifos.community` (demo credentials in CLAUDE.md) |

### Session Workflow

Always use session commands to maintain context across conversations:

```
┌─────────────────────────────────────────────────────────────────┐
│                    SESSION WORKFLOW                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   /session-start              # Load context from previous       │
│        │                      # session (CURRENT_WORK.md)        │
│        ▼                                                         │
│   ┌─────────────────────┐                                       │
│   │   DO YOUR WORK      │     # Use gap-analysis, design,       │
│   │   • /gap-analysis   │     # implement, verify commands      │
│   │   • /design         │                                       │
│   │   • /implement      │                                       │
│   │   • /verify         │                                       │
│   └─────────────────────┘                                       │
│        │                                                         │
│        ▼                                                         │
│   /session-end                # Save context for next session    │
│                               # (updates CURRENT_WORK.md)        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 5-Layer Lifecycle

Every feature flows through 5 layers:

```
┌─────────────────────────────────────────────────────────────────┐
│                     5-LAYER LIFECYCLE                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Layer 1: DESIGN                                               │
│   ├── SPEC.md      → What to build (screens, flows)             │
│   ├── MOCKUP.md    → How it looks (ASCII/visual mockups)        │
│   ├── API.md       → What APIs needed (endpoints, payloads)     │
│   └── STATUS.md    → Implementation progress                    │
│                                                                  │
│   Layer 2: SERVER                                               │
│   └── Fineract API → External API availability check            │
│                                                                  │
│   Layer 3: CLIENT                                               │
│   ├── Network      → core/network/services/*Service.kt          │
│   ├── Data         → core/data/repository/*Repository.kt        │
│   └── Model        → core/network/model/*.kt                    │
│                                                                  │
│   Layer 4: FEATURE                                              │
│   ├── ViewModel    → feature/*/viewmodel/*ViewModel.kt          │
│   ├── Screen       → feature/*/screens/*Screen.kt               │
│   ├── Navigation   → feature/*/navigation/*Navigation.kt        │
│   └── DI           → feature/*/di/*Module.kt                    │
│                                                                  │
│   Layer 5: PLATFORM                                             │
│   ├── Android      → cmp-android/                               │
│   ├── iOS          → cmp-ios/                                   │
│   ├── Desktop      → cmp-desktop/                               │
│   └── Web          → cmp-web/                                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Gap Analysis & Planning

Use these commands to understand what's done vs what's needed:

```bash
# Quick overview - all layers summary
/gap-analysis

# Layer-specific analysis
/gap-analysis design           # Design specs, mockups, API docs status
/gap-analysis server           # Fineract API availability
/gap-analysis client           # Network services, repositories status
/gap-analysis feature          # ViewModels, Screens status
/gap-analysis platform         # Platform-specific builds status

# Sub-section analysis
/gap-analysis design mockup    # Mockup generation status only
/gap-analysis design spec      # Specification status only
/gap-analysis client network   # Network services only
/gap-analysis feature auth     # Single feature status

# Feature-specific (all 5 layers for one feature)
/gap-analysis auth             # Auth feature across all layers
/gap-analysis transfer         # Transfer feature across all layers

# Plan improvements
/gap-planning auth             # Generate plan to fill gaps for auth
/gap-planning design mockup    # Plan for mockup generation
```

### Design Layer Workflow

The design layer has 4 sub-sections per feature:

```
features/[name]/
├── SPEC.md       # WHAT to build
│   ├── Feature Overview
│   ├── User Stories
│   ├── Screen Descriptions
│   ├── User Flows
│   └── State Management
│
├── MOCKUP.md     # HOW it looks
│   ├── ASCII Mockups (all screens)
│   ├── Component Breakdown
│   ├── Interaction Patterns
│   └── Data Binding Reference → References API.md
│
├── API.md        # WHAT APIs (Single Source of Truth)
│   ├── Endpoints Required
│   ├── Request/Response Schemas
│   ├── Error Responses
│   └── Dependencies
│
└── STATUS.md     # Progress tracking
    ├── Design Phase (%)
    ├── Client Phase (%)
    ├── Feature Phase (%)
    └── Platform Phase (%)
```

**Generating Mockups**:
```bash
# Generate mockups for a feature
/design auth                   # Opens design workflow
# Then select "mockup" sub-section when prompted
```

### Implementation Workflow

```bash
# Full E2E implementation (all layers)
/implement auth

# OR layer-by-layer implementation
/client auth                   # Network + Data layers only
/feature auth                  # UI layer only (requires client layer)

# After implementation
/verify auth                   # Validate against spec
```

**Implementation Order**:
```
1. /design [feature]      → Create specifications
2. /implement [feature]   → Full implementation
   - OR -
2a. /client [feature]     → Network + Repository
2b. /feature [feature]    → ViewModel + Screen
3. /verify [feature]      → Validate completeness
```

### Common Scenarios

#### Scenario 1: Start New Feature

```bash
/session-start                 # Load previous context
/gap-analysis                  # See what's missing
/design new-feature            # Create specs (uses Opus model)
/implement new-feature         # Implement E2E
/verify new-feature            # Validate
/session-end                   # Save context
```

#### Scenario 2: Fix Gaps in Existing Feature

```bash
/session-start
/gap-analysis auth             # Find gaps in auth feature
/gap-planning auth             # Get action plan
/implement auth                # Fill the gaps
/verify auth
/session-end
```

#### Scenario 3: Update Mockups Only

```bash
/session-start
/gap-analysis design mockup    # See which mockups are missing
/design auth                   # Then choose mockup sub-section
/session-end
```

#### Scenario 4: Check Before PR

```bash
/session-start
/gap-analysis                  # Full project overview
/verify [feature]              # Verify your changes
./ci-prepush.sh                # Run pre-push checks
/session-end
```

### Key Context Files

| File | Purpose | When to Check |
|------|---------|---------------|
| `CURRENT_WORK.md` | Active work, next actions | Start of session |
| `PRODUCT_MAP.md` | Master status tracker | Project overview |
| `design-spec-layer/STATUS.md` | Design layer progress | Before design work |
| `features/*/STATUS.md` | Per-feature progress | Before feature work |

### Quick Reference

| Task | Command |
|------|---------|
| See project status | `/projectstatus` |
| Find gaps | `/gap-analysis` or `/gap-analysis [layer]` |
| Plan improvements | `/gap-planning [feature]` |
| Design a feature | `/design [feature]` |
| Implement E2E | `/implement [feature]` |
| Client layer only | `/client [feature]` |
| UI layer only | `/feature [feature]` |
| Validate work | `/verify [feature]` |
| Start session | `/session-start` |
| End session | `/session-end` |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-26 | Initial framework setup for Mifos Mobile |

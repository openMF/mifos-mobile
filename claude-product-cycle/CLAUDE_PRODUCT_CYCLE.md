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

1. **Check Status**: Run `/projectstatus` to see current state
2. **Pick Feature**: Choose a feature that needs work
3. **Design First**: Run `/design [Feature]` to create/review spec
4. **Implement**: Run `/implement [Feature]` for full implementation
5. **Verify**: Run `/verify [Feature]` to validate

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-26 | Initial framework setup for Mifos Mobile |

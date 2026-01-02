# Client Layer - Status & Memory

> **Layer**: Network + Data
> **Command**: `/client [Feature]`
> **Location**: `core/` (network, data)

---

## Current Status

| Sub-Layer | Components | Status |
|-----------|------------|--------|
| Network (Services) | 13 | Complete |
| Network (DTOs) | 40+ | Complete |
| Data (Repositories) | 17 | Complete |

---

## Layer Structure

```
core/
├── network/                    # API Layer
│   └── src/commonMain/kotlin/.../network/
│       ├── DataManager.kt           # Service accessor
│       ├── KtorfitClient.kt         # Ktorfit setup
│       ├── services/                # API interfaces
│       │   ├── AuthenticationService.kt
│       │   ├── BeneficiaryService.kt
│       │   ├── ClientChargeService.kt
│       │   ├── ClientService.kt
│       │   ├── GuarantorService.kt
│       │   ├── LoanAccountsListService.kt
│       │   ├── NotificationService.kt
│       │   ├── RecentTransactionsService.kt
│       │   ├── RegistrationService.kt
│       │   ├── SavingAccountsListService.kt
│       │   ├── ShareAccountService.kt
│       │   ├── ThirdPartyTransferService.kt
│       │   └── UserDetailsService.kt
│       ├── model/                   # DTOs
│       └── di/NetworkModule.kt
│
├── data/                       # Data Layer
│   └── src/commonMain/kotlin/.../data/
│       ├── repository/              # Interfaces
│       ├── repositoryImpl/          # Implementations
│       └── di/RepositoryModule.kt
│
├── model/                      # Domain Models (shared)
├── database/                   # Local DB (Room)
├── datastore/                  # Preferences
├── designsystem/               # UI components
├── ui/                         # Shared UI
└── common/                     # Utilities
```

---

## Services (Network Layer)

| Service | Purpose | Base Endpoint |
|---------|---------|---------------|
| AuthenticationService | Login, logout | /self/authentication |
| RegistrationService | User registration | /self/registration |
| ClientService | Client profile | /self/clients |
| UserDetailsService | User details | /self/userdetails |
| SavingAccountsListService | Savings accounts | /self/savingsaccounts |
| LoanAccountsListService | Loan accounts | /self/loanaccounts |
| ShareAccountService | Share accounts | /self/shareaccounts |
| BeneficiaryService | Beneficiaries CRUD | /self/beneficiaries |
| ThirdPartyTransferService | Transfers | /self/accounttransfers |
| RecentTransactionsService | Transaction history | /self/transactions |
| NotificationService | Notifications | /self/notifications |
| GuarantorService | Guarantor management | /self/loans/{id}/guarantors |
| ClientChargeService | Client charges | /self/clients/{id}/charges |

---

## Repositories (Data Layer)

| Repository | Service Used | Status |
|------------|--------------|--------|
| UserAuthRepository | AuthenticationService | Complete |
| UserDataRepository | - (DataStore) | Complete |
| UserDetailRepository | UserDetailsService | Complete |
| ClientRepository | ClientService | Complete |
| AccountsRepository | Multiple services | Complete |
| SavingsAccountRepository | SavingAccountsListService | Complete |
| LoanRepository | LoanAccountsListService | Complete |
| ShareAccountRepository | ShareAccountService | Complete |
| BeneficiaryRepository | BeneficiaryService | Complete |
| TransferRepository | Multiple services | Complete |
| ThirdPartyTransferRepository | ThirdPartyTransferService | Complete |
| RecentTransactionRepository | RecentTransactionsService | Complete |
| NotificationRepository | NotificationService | Complete |
| GuarantorRepository | GuarantorService | Complete |
| ClientChargeRepository | ClientChargeService | Complete |
| HomeRepository | Multiple services | Complete |
| ReviewLoanApplicationRepository | LoanAccountsListService | Complete |

---

## DataManager APIs

| API | Service | Lazy Loaded |
|-----|---------|-------------|
| authenticationApi | AuthenticationService | Yes |
| registrationApi | RegistrationService | Yes |
| clientsApi | ClientService | Yes |
| userDetailsApi | UserDetailsService | Yes |
| savingAccountsListApi | SavingAccountsListService | Yes |
| loanAccountsListApi | LoanAccountsListService | Yes |
| shareAccountApi | ShareAccountService | Yes |
| beneficiaryApi | BeneficiaryService | Yes |
| thirdPartyTransferApi | ThirdPartyTransferService | Yes |
| recentTransactionsApi | RecentTransactionsService | Yes |
| notificationApi | NotificationService | Yes |
| guarantorApi | GuarantorService | Yes |
| clientChargeApi | ClientChargeService | Yes |

---

## Critical Rules

```
REPOSITORY ALWAYS USES DataManager!

CORRECT: Repository → DataManager → Service
WRONG:   Repository → Service directly
```

---

## Build Commands

```bash
./gradlew :core:network:build
./gradlew :core:data:build
./gradlew :core:model:build
```

---

## DI Modules

| Module | Location | Registration |
|--------|----------|--------------|
| NetworkModule | core/network/di/ | DataManager, KtorfitClient |
| RepositoryModule | core/data/di/ | All Repositories |
| PreferenceModule | core/datastore/di/ | UserPreferencesRepository |
| DatabaseModule | core/database/di/ | Room Database |

All registered in: `cmp-navigation/di/KoinModules.kt`

---

## Related Docs

- Patterns: `claude-product-cycle/design-spec-layer/_shared/PATTERNS.md`
- API Specs: `claude-product-cycle/design-spec-layer/features/[feature]/API.md`
- Layer Guide: `claude-product-cycle/client-layer/LAYER_GUIDE.md`

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-26 | Created LAYER_STATUS.md |
| 2025-12-26 | All 17 repositories verified complete |

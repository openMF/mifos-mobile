# Feature → Client Components Map

> **13 services** | **17 repositories** | **2 DI modules**

---

## Feature to Components (O(1) Lookup)

| Feature | Services | Repositories | Notes |
|---------|----------|--------------|-------|
| auth | AuthenticationService, RegistrationService, UserDetailsService | UserAuthRepository, UserDataRepository | Login, Register, Password |
| home | ClientService, NotificationService | HomeRepository, NotificationRepository | Dashboard, Profile |
| accounts | ClientService | AccountsRepository | Account listing |
| loan-account | LoanAccountsListService, GuarantorService | LoanRepository, GuarantorRepository, ReviewLoanApplicationRepository | Loan details |
| savings-account | SavingAccountsListService | SavingsAccountRepository | Savings details |
| share-account | ShareAccountService | ShareAccountRepository | Share details |
| beneficiary | BeneficiaryService | BeneficiaryRepository | TPT beneficiaries |
| transfer | ThirdPartyTransferService, SavingAccountsListService | TransferRepository, ThirdPartyTransferRepository | Fund transfer |
| notification | NotificationService | NotificationRepository | Push notifications |
| recent-transaction | RecentTransactionsService | RecentTransactionRepository | Transaction history |
| client-charge | ClientChargeService | ClientChargeRepository | Client charges |
| settings | UserDetailsService | UserDetailRepository | Password change |
| guarantor | GuarantorService | GuarantorRepository | Loan guarantors |
| user-profile | ClientService | ClientRepository, UserDetailRepository | Profile display |

---

## Services Inventory (13)

| Service | File | Key Methods |
|---------|------|-------------|
| AuthenticationService | AuthenticationService.kt | `authenticate()` |
| RegistrationService | RegistrationService.kt | `registerUser()`, `verifyUser()` |
| ClientService | ClientService.kt | `clients()`, `getClientForId()`, `getClientAccounts()`, `getClientImage()` |
| LoanAccountsListService | LoanAccountsListService.kt | `getLoanWithAssociations()`, `createLoansAccount()`, `withdrawLoanAccount()` |
| SavingAccountsListService | SavingAccountsListService.kt | `getSavingsWithAssociations()`, `makeTransfer()`, `submitSavingAccountApplication()` |
| BeneficiaryService | BeneficiaryService.kt | `beneficiaryList()`, `createBeneficiary()`, `updateBeneficiary()`, `deleteBeneficiary()` |
| ThirdPartyTransferService | ThirdPartyTransferService.kt | `accountTransferTemplate()`, `makeTransfer()` |
| NotificationService | NotificationService.kt | `getUserNotificationId()`, `registerNotification()`, `updateRegisterNotification()` |
| RecentTransactionsService | RecentTransactionsService.kt | `getRecentTransactionsList()` |
| UserDetailsService | UserDetailsService.kt | `updateAccountPassword()` |
| ShareAccountService | ShareAccountService.kt | `getShareProducts()`, `submitShareApplication()`, `getShareAccountDetails()` |
| GuarantorService | GuarantorService.kt | `getGuarantorList()`, `getGuarantorTemplate()`, `createGuarantor()` |
| ClientChargeService | ClientChargeService.kt | `getClientChargeList()`, `getChargeList()` |

---

## Repositories Inventory (17)

| Repository | Implementation | Depends On |
|------------|----------------|------------|
| AccountsRepository | AccountsRepositoryImp | ClientService |
| BeneficiaryRepository | BeneficiaryRepositoryImp | BeneficiaryService |
| ClientChargeRepository | ClientChargeRepositoryImp | ClientChargeService |
| ClientRepository | ClientRepositoryImp | ClientService |
| GuarantorRepository | GuarantorRepositoryImp | GuarantorService |
| HomeRepository | HomeRepositoryImp | ClientService |
| LoanRepository | LoanRepositoryImp | LoanAccountsListService |
| NotificationRepository | NotificationRepositoryImp | NotificationService |
| RecentTransactionRepository | RecentTransactionRepositoryImp | RecentTransactionsService |
| ReviewLoanApplicationRepository | ReviewLoanApplicationRepositoryImpl | LoanAccountsListService |
| SavingsAccountRepository | SavingsAccountRepositoryImp | SavingAccountsListService |
| ShareAccountRepository | ShareAccountRepositoryImp | ShareAccountService |
| ThirdPartyTransferRepository | ThirdPartyTransferRepositoryImp | ThirdPartyTransferService |
| TransferRepository | TransferRepositoryImp | SavingAccountsListService |
| UserAuthRepository | UserAuthRepositoryImp | AuthenticationService |
| UserDataRepository | AuthenticationUserRepository | DataStore |
| UserDetailRepository | UserDetailRepositoryImp | UserDetailsService |

---

## DI Modules

| Module | Location | Provides |
|--------|----------|----------|
| NetworkModule | `core/network/di/NetworkModule.kt` | HttpClient, KtorfitClient, DataManager |
| RepositoryModule | `core/data/di/RepositoryModule.kt` | All 17 repositories as singletons |

---

## Reverse Lookup: Service → Features

| Service | Used By Features |
|---------|------------------|
| AuthenticationService | auth |
| RegistrationService | auth |
| ClientService | home, accounts, user-profile |
| LoanAccountsListService | loan-account |
| SavingAccountsListService | savings-account, transfer |
| BeneficiaryService | beneficiary |
| ThirdPartyTransferService | transfer |
| NotificationService | home, notification |
| RecentTransactionsService | recent-transaction |
| UserDetailsService | auth, settings |
| ShareAccountService | share-account |
| GuarantorService | loan-account, guarantor |
| ClientChargeService | client-charge |

---

## Reverse Lookup: Repository → Features

| Repository | Used By Features |
|------------|------------------|
| AccountsRepository | accounts |
| BeneficiaryRepository | beneficiary |
| ClientChargeRepository | client-charge |
| ClientRepository | user-profile |
| GuarantorRepository | loan-account, guarantor |
| HomeRepository | home |
| LoanRepository | loan-account |
| NotificationRepository | home, notification |
| RecentTransactionRepository | recent-transaction |
| ReviewLoanApplicationRepository | loan-account |
| SavingsAccountRepository | savings-account |
| ShareAccountRepository | share-account |
| ThirdPartyTransferRepository | transfer |
| TransferRepository | transfer |
| UserAuthRepository | auth |
| UserDataRepository | auth |
| UserDetailRepository | settings, user-profile |

---

## O(1) File Access

| Need | Path |
|------|------|
| Service | `core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/services/[Name]Service.kt` |
| Repository Interface | `core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/[Name]Repository.kt` |
| Repository Impl | `core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/[Name]RepositoryImp.kt` |
| Network DI | `core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/di/NetworkModule.kt` |
| Data DI | `core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/di/RepositoryModule.kt` |

---

## Architecture Flow

```
Feature (ViewModel)
    ↓
Repository (Interface)
    ↓
RepositoryImpl (Implementation)
    ↓
DataManager (Service Accessor)
    ↓
Service (Ktorfit API)
    ↓
HttpClient (Ktor)
```

---

## Related Files

- [LAYER_STATUS.md](LAYER_STATUS.md) - Implementation status
- [LAYER_GUIDE.md](LAYER_GUIDE.md) - Architecture patterns
- [server-layer/API_INDEX.md](../server-layer/API_INDEX.md) - API endpoints

---

## Auto-Update Rules

| Scenario | Action |
|----------|--------|
| New service added | Add to Services Inventory |
| New repository added | Add to Repositories Inventory + Reverse Lookup |
| Feature uses new service | Update Feature to Components table |

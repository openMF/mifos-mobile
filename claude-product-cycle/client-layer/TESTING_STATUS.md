# Client Layer - Testing Status

> Repository and service testing documentation

---

## Overview

The client layer handles data operations. Testing ensures repositories correctly transform API responses and handle errors.

---

## Current State

| Component | Total | Tested | Coverage |
|-----------|:-----:|:------:|:--------:|
| Services | 13 | 0 | 0% |
| Repositories | 17 | 14 | 82% |
| DataStore | 3 | 3 | 100% |

---

## Testing Scope

| Component | Test Type | Purpose |
|-----------|-----------|---------|
| Services | Unit Tests | Verify API call construction |
| Repositories | Unit Tests | Verify data transformation |
| DataStore | Unit Tests | Verify local persistence |

---

## Repository Testing Status

### Existing Tests (14)

| # | Repository | Tests | File |
|:-:|------------|:-----:|------|
| 1 | AccountsRepository | 2 | `AccountsRepositoryTest.kt` |
| 2 | BeneficiaryRepository | 1 | `BeneficiaryRepositoryTest.kt` |
| 3 | ClientChargeRepository | 1 | `ClientChargeRepositoryTest.kt` |
| 4 | ClientRepository | 1 | `ClientRepositoryTest.kt` |
| 5 | GuarantorRepository | 1 | `GuarantorRepositoryTest.kt` |
| 6 | HomeRepository | 1 | `HomeRepositoryTest.kt` |
| 7 | LoanRepository | 1 | `LoanRepositoryTest.kt` |
| 8 | NotificationRepository | 1 | `NotificationRepositoryTest.kt` |
| 9 | RecentTransactionRepository | 1 | `RecentTransactionRepositoryTest.kt` |
| 10 | SavingsAccountRepository | 1 | `SavingsAccountRepositoryTest.kt` |
| 11 | ShareAccountRepository | 1 | `ShareAccountRepositoryTest.kt` |
| 12 | ThirdPartyTransferRepository | 1 | `ThirdPartyTransferRepositoryTest.kt` |
| 13 | TransferRepository | 1 | `TransferRepositoryTest.kt` |
| 14 | UserAuthRepository | - | (needs tests) |

**Location**: `core/data/src/commonTest/kotlin/org/mifos/mobile/core/data/repository/`

---

## Repository Test Coverage Matrix

| # | Repository | Success | Error | Empty | Offline | Status |
|:-:|------------|:-------:|:-----:|:-----:|:-------:|:------:|
| 1 | AccountsRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 2 | BeneficiaryRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 3 | ClientChargeRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 4 | ClientRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 5 | GuarantorRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 6 | HomeRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 7 | LoanRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 8 | NotificationRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 9 | RecentTransactionRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 10 | SavingsAccountRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 11 | ShareAccountRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 12 | ThirdPartyTransferRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 13 | TransferRepository | ✅ | ⬜ | ⬜ | ⬜ | Partial |
| 14 | UserAuthRepository | ⬜ | ⬜ | ⬜ | ⬜ | Not Started |
| 15 | UserDataRepository | ⬜ | ⬜ | ⬜ | ⬜ | Not Started |
| 16 | UserPreferencesRepository | ⬜ | ⬜ | ⬜ | ⬜ | Not Started |
| 17 | RegistrationRepository | ⬜ | ⬜ | ⬜ | ⬜ | Not Started |

**Legend**: ✅ Tested | ⬜ Not Tested

---

## Missing Tests

### Priority 1 - Auth Flow
- [ ] UserAuthRepository (login, logout, token refresh)
- [ ] UserPreferencesRepository (user settings)
- [ ] RegistrationRepository (sign up flow)

### Priority 2 - Error Handling
- [ ] All repositories: error scenarios
- [ ] All repositories: empty response handling
- [ ] All repositories: offline caching

### Priority 3 - Edge Cases
- [ ] Pagination handling
- [ ] Concurrent request handling
- [ ] Cache invalidation

---

## Fake Repository Implementations

For ViewModel testing, create fake repositories:

**Location**: `core/testing/src/commonMain/kotlin/org/mifos/mobile/core/testing/fake/`

```kotlin
class FakeHomeRepository : HomeRepository {
    private var result: DataState<HomeData> = DataState.Loading

    fun setResult(result: DataState<HomeData>) {
        this.result = result
    }

    override suspend fun getHomeData(): Flow<DataState<HomeData>> = flow {
        emit(result)
    }
}
```

### Fake Repositories Needed

| # | Fake Repository | For Testing |
|:-:|-----------------|-------------|
| 1 | FakeHomeRepository | HomeViewModel |
| 2 | FakeUserAuthRepository | LoginViewModel |
| 3 | FakeAccountsRepository | AccountsViewModel |
| 4 | FakeBeneficiaryRepository | BeneficiaryViewModel |
| 5 | FakeLoanRepository | LoanViewModel |
| 6 | FakeSavingsAccountRepository | SavingsViewModel |
| 7 | FakeTransferRepository | TransferViewModel |
| 8 | FakeNotificationRepository | NotificationViewModel |
| 9 | FakeSettingsRepository | SettingsViewModel |

---

## Test Fixtures

**Location**: `core/testing/src/commonMain/kotlin/org/mifos/mobile/core/testing/fixture/`

```kotlin
object ClientAccountsFixture {
    fun create(
        savingsAccounts: List<SavingAccount> = emptyList(),
        loanAccounts: List<LoanAccount> = emptyList(),
        shareAccounts: List<ShareAccount> = emptyList()
    ) = ClientAccounts(
        savingsAccounts = savingsAccounts,
        loanAccounts = loanAccounts,
        shareAccounts = shareAccounts
    )

    fun withSavings() = create(
        savingsAccounts = listOf(SavingAccountFixture.create())
    )
}
```

---

## Implementation Priority

| Phase | Scope | Tests |
|:-----:|-------|:-----:|
| 1 | Auth repositories | 15 |
| 2 | Error handling (all repos) | 34 |
| 3 | Fake implementations | 9 |
| 4 | Test fixtures | 12 |

---

## Commands

```bash
# Run client layer tests
./gradlew :core:data:test

# Check test coverage
/gap-analysis client testing

# Plan missing tests
/gap-planning client testing
```

---

## Related Files

- [FEATURE_MAP.md](./FEATURE_MAP.md) - Feature to service mapping
- [LAYER_STATUS.md](./LAYER_STATUS.md) - Implementation status

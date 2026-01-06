# Testing Layer - Status Dashboard

> **17 features** | **49 ViewModels** | **63 Screens** | **Last Updated**: 2026-01-05

---

## Quick Overview

| Metric | Current | Target | Progress |
|--------|:-------:|:------:|:--------:|
| ViewModel Tests | 0/49 | 49 | [░░░░░░░░░░] 0% |
| Screen Tests | 0/63 | 63 | [░░░░░░░░░░] 0% |
| Fake Repositories | 6/17 | 17 | [███░░░░░░░] 35% |
| TestTags Objects | 17/17 | 17 | [██████████] 100% |
| Test Fixtures | 3/10 | 10 | [███░░░░░░░] 30% |
| Integration Tests | 0/5 | 5 | [░░░░░░░░░░] 0% |
| Screenshot Tests | 0/20 | 20 | [░░░░░░░░░░] 0% |

---

## core:testing Module (KMP)

**Location**: `core/testing/`
**Status**: ✅ Active
**Platforms**: Android, iOS, Desktop, Native

### Module Structure

```
core/testing/src/
├── commonMain/kotlin/org/mifos/mobile/core/testing/
│   ├── di/TestModule.kt                    # Koin test module
│   ├── fake/                               # Fake repositories (6)
│   │   ├── FakeUserAuthRepository.kt
│   │   ├── FakeHomeRepository.kt
│   │   ├── FakeAccountsRepository.kt
│   │   ├── FakeBeneficiaryRepository.kt
│   │   ├── FakeTransferRepository.kt
│   │   └── FakeNotificationRepository.kt
│   ├── fixture/                            # Test fixtures (3)
│   │   ├── UserFixture.kt
│   │   ├── ClientAccountsFixture.kt
│   │   └── BeneficiaryFixture.kt
│   ├── rule/MainDispatcherRule.kt          # Coroutine testing (KMP)
│   └── util/
│       ├── TestTags.kt                     # TestTags for all 17 features
│       ├── FlowTestExtensions.kt           # Flow testing helpers
│       └── TestCoroutineExtensions.kt      # Coroutine helpers
├── androidMain/kotlin/org/mifos/mobile/core/testing/
│   ├── rule/MainDispatcherTestRule.kt      # JUnit4 TestRule
│   └── ComposeTestHelpers.kt               # Compose UI test extensions
├── iosMain/kotlin/org/mifos/mobile/core/testing/
│   └── IosTestUtils.kt                     # iOS accessibility helpers
├── desktopMain/kotlin/org/mifos/mobile/core/testing/
│   └── DesktopTestUtils.kt                 # Desktop/Swing helpers
└── nativeMain/kotlin/org/mifos/mobile/core/testing/
    └── NativeTestUtils.kt                  # Native platform helpers
```

### Platform-Specific Features

| Platform | Source Set | Features |
|----------|------------|----------|
| Common | `commonMain` | TestTags, Fakes, Fixtures, MainDispatcherRule |
| Android | `androidMain` | JUnit4 TestRule, Compose UI test helpers |
| iOS | `iosMain` | Accessibility ID conversion, XCTest helpers |
| Desktop | `desktopMain` | Swing dispatcher, Desktop test setup |
| Native | `nativeMain` | Native dispatcher setup, timing utils |

---

## Feature Test Coverage

| # | Feature | VMs | VM Tests | Screens | Screen Tests | Fakes | TestTags | Status |
|:-:|---------|:---:|:--------:|:-------:|:------------:|:-----:|:--------:|:------:|
| 1 | auth | 5 | 0 | 6 | 0 | ✅ | ✅ | Infrastructure Ready |
| 2 | home | 1 | 0 | 1 | 0 | ✅ | ✅ | Infrastructure Ready |
| 3 | accounts | 3 | 0 | 3 | 0 | ✅ | ✅ | Infrastructure Ready |
| 4 | savings-account | 3 | 0 | 4 | 0 | ❌ | ✅ | TestTags Ready |
| 5 | loan-account | 4 | 0 | 4 | 0 | ❌ | ✅ | TestTags Ready |
| 6 | share-account | 2 | 0 | 2 | 0 | ❌ | ✅ | TestTags Ready |
| 7 | beneficiary | 4 | 0 | 4 | 0 | ✅ | ✅ | Infrastructure Ready |
| 8 | transfer | 2 | 0 | 2 | 0 | ✅ | ✅ | Infrastructure Ready |
| 9 | recent-transaction | 1 | 0 | 1 | 0 | ❌ | ✅ | TestTags Ready |
| 10 | notification | 1 | 0 | 1 | 0 | ✅ | ✅ | Infrastructure Ready |
| 11 | settings | 5 | 0 | 9 | 0 | ❌ | ✅ | TestTags Ready |
| 12 | passcode | 2 | 0 | 2 | 0 | ❌ | ✅ | TestTags Ready |
| 13 | guarantor | 3 | 0 | 3 | 0 | ❌ | ✅ | TestTags Ready |
| 14 | qr | 3 | 0 | 3 | 0 | ❌ | ✅ | TestTags Ready |
| 15 | location | 0 | 0 | 1 | 0 | ❌ | ✅ | TestTags Ready |
| 16 | client-charge | 2 | 0 | 2 | 0 | ❌ | ✅ | TestTags Ready |
| 17 | dashboard | 0 | 0 | 0 | 0 | ❌ | ✅ | TestTags Ready |
| | **TOTAL** | **41** | **0** | **48** | **0** | **6/17** | **17/17** | |

**Legend**: ✅ Complete | ⚠️ Partial | ❌ Missing

### Fake Repositories Available (6)

| Fake | Interface | Key Methods |
|------|-----------|-------------|
| `FakeUserAuthRepository` | `UserAuthRepository` | login, register, logout |
| `FakeHomeRepository` | `HomeRepository` | getClientAccounts, getUserData |
| `FakeAccountsRepository` | `AccountsRepository` | getLoanAccounts, getSavingsAccounts |
| `FakeBeneficiaryRepository` | `BeneficiaryRepository` | getBeneficiaries, createBeneficiary |
| `FakeTransferRepository` | `TransferRepository` | makeTransfer, getTransferTemplate |
| `FakeNotificationRepository` | `NotificationRepository` | getNotifications, markAsRead |

### Test Fixtures Available (3)

| Fixture | Entity | Factory Methods |
|---------|--------|-----------------|
| `UserFixture` | `User` | createDefault, createAdmin, createUnauthenticated |
| `ClientAccountsFixture` | `ClientAccounts` | createEmpty, createWithSampleData, createWithLoansOnly |
| `BeneficiaryFixture` | `Beneficiary` | createDefault, createList, createPayload |

---

## Test Types

### Unit Tests (commonTest)

| Category | Location | Framework | Status |
|----------|----------|-----------|:------:|
| ViewModel Tests | `feature/*/src/commonTest/` | kotlin-test, Turbine | ❌ |
| Repository Tests | `core/data/src/commonTest/` | kotlin-test | ✅ 14 |
| DataStore Tests | `core/datastore/src/commonTest/` | kotlin-test | ✅ |

### UI Tests (androidInstrumentedTest)

| Category | Location | Framework | Status |
|----------|----------|-----------|:------:|
| Screen Tests | `feature/*/src/androidInstrumentedTest/` | Compose UI Test | ❌ |
| Integration Tests | `cmp-android/src/androidTest/` | Compose UI Test | ❌ |

### Screenshot Tests

| Category | Location | Framework | Status |
|----------|----------|-----------|:------:|
| Component Screenshots | `core/designsystem/src/test/` | Roborazzi | ❌ |
| Screen Screenshots | `feature/*/src/test/` | Roborazzi | ❌ |

---

## Priority Queue

| Priority | Feature | Reason | Effort |
|:--------:|---------|--------|:------:|
| P0 | auth | Core flow, most complex | L |
| P0 | home | Entry point, high visibility | M |
| P0 | accounts | Core business logic | M |
| P1 | transfer | Financial operations | L |
| P1 | beneficiary | CRUD operations | M |
| P1 | loan-account | Complex states | M |
| P1 | savings-account | Multiple views | M |
| P2 | settings | Many screens, lower risk | L |
| P2 | notification | Simple list | S |
| P2 | recent-transaction | Simple list | S |

---

## O(1) Index Files

| File | Purpose | Entries |
|------|---------|:-------:|
| [TEST_PATTERNS.md](./TEST_PATTERNS.md) | Test pattern reference | 5 |
| [TEST_TAGS_INDEX.md](./TEST_TAGS_INDEX.md) | TestTag lookup | 17 |
| [TEST_FIXTURES_INDEX.md](./TEST_FIXTURES_INDEX.md) | Fixture lookup | 0 |
| [FAKE_REPOS_INDEX.md](./FAKE_REPOS_INDEX.md) | Fake repo lookup | 0 |

---

## Commands

```bash
# Check testing status
/gap-analysis testing           # Overall test coverage

# Generate tests for feature
/implement [feature]            # Phase 5 generates test stubs

# Verify TestTag compliance
/verify [feature]               # Includes TestTag validation

# Run tests
/verify-tests [feature]         # Run and report test results
```

---

## Related Files

- [TEST_STUBS_GUIDE.md](../TEST_STUBS_GUIDE.md) - TDD reference guide
- [patterns/](./patterns/) - Detailed test patterns
- [templates/](./templates/) - Code templates

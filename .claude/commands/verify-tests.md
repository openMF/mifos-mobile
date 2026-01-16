# /verify-tests - Test Verification (O(1) Enhanced)

Run and verify tests for features across the project with O(1) status lookups.

## Usage

```
/verify-tests                        # Show test status dashboard (O(1))
/verify-tests [feature]              # Run all tests for feature
/verify-tests [feature] unit         # Run ViewModel tests only
/verify-tests [feature] ui           # Run UI tests only
/verify-tests [feature] integration  # Run integration tests
/verify-tests [feature] screenshot   # Run screenshot tests
/verify-tests client                 # Run all client layer tests
/verify-tests feature                # Run all feature layer tests
/verify-tests platform               # Run all platform tests
```

---

## O(1) Workflow

```
+-------------------------------------------------------------------------+
|                /verify-tests WORKFLOW (O(1) ENHANCED)                    |
+-------------------------------------------------------------------------+
|                                                                          |
|  PHASE 0: O(1) CONTEXT LOADING                                          |
|  +--> Read feature-layer/TESTING_STATUS.md     --> VM/Screen test status|
|  +--> Read client-layer/TESTING_STATUS.md      --> Repository test status|
|  +--> Read platform-layer/TESTING_STATUS.md    --> E2E/Screenshot status|
|  +--> Read feature-layer/MODULES_INDEX.md      --> Feature paths        |
|                                                                          |
|  PHASE 1: DETERMINE TEST SCOPE                                          |
|  +--> If no args: Show test dashboard from indexes                      |
|  +--> If [feature]: Get paths from MODULES_INDEX                        |
|  +--> If [layer]: Get layer test config                                 |
|                                                                          |
|  PHASE 2: EXECUTE TESTS                                                 |
|  +--> Build appropriate Gradle command                                  |
|  +--> Run tests via Bash                                                |
|  +--> Capture output                                                    |
|                                                                          |
|  PHASE 3: PARSE RESULTS                                                 |
|  +--> Extract: passed, failed, skipped                                  |
|  +--> Extract: failure details                                          |
|  +--> Calculate coverage (if available)                                 |
|                                                                          |
|  PHASE 4: UPDATE STATUS                                                 |
|  +--> Update TESTING_STATUS.md with new results                         |
|  +--> Log test run timestamp                                            |
|                                                                          |
|  PHASE 5: REPORT                                                        |
|  +--> Show results with next steps                                      |
|                                                                          |
+-------------------------------------------------------------------------+
```

---

## Phase 0: O(1) Context Loading

### Index Files to Read

| File | Purpose | Lines |
|------|---------|:-----:|
| `testing-layer/LAYER_STATUS.md` | **Primary** test dashboard | ~200 |
| `testing-layer/TEST_PATTERNS.md` | Test patterns & conventions | ~300 |
| `testing-layer/TEST_TAGS_INDEX.md` | TestTag specifications | ~350 |
| `testing-layer/TEST_FIXTURES_INDEX.md` | Test fixtures inventory | ~250 |
| `testing-layer/FAKE_REPOS_INDEX.md` | Fake repositories status | ~200 |
| `feature-layer/MODULES_INDEX.md` | Feature → Path mapping | ~115 |

### O(1) Path Pattern

```
feature/[module]/src/commonTest/             # Unit tests (ViewModel)
feature/[module]/src/androidInstrumentedTest/ # UI tests (Screen)
core/data/src/commonTest/                    # Repository tests
cmp-android/src/androidTest/                 # E2E integration tests
core/designsystem/src/test/                  # Screenshot tests
```

---

## If No Arguments: Test Dashboard

Read from TESTING_STATUS.md files and show:

```
+=========================================================================+
|                      TEST STATUS DASHBOARD (O(1))                        |
+=========================================================================+

## Layer Summary

| Layer | Tests | Passed | Failed | Coverage | Status |
|-------|:-----:|:------:|:------:|:--------:|:------:|
| Client | 14 | 14 | 0 | 82% | [=======   ] |
| Feature | 0 | 0 | 0 | 0% | [          ] |
| Platform | 0 | 0 | 0 | 0% | [          ] |

## Feature Testing Matrix (from TESTING_STATUS.md)

| Feature | VMs | VM Tests | Screens | UI Tests | Status |
|---------|:---:|:--------:|:-------:|:--------:|:------:|
| auth | 5 | 0 | 6 | 0 | [ ] Not Started |
| home | 1 | 0 | 1 | 0 | [ ] Not Started |
| accounts | 3 | 0 | 3 | 0 | [ ] Not Started |
| ... (from feature-layer/TESTING_STATUS.md)

## Repository Testing (from client-layer/TESTING_STATUS.md)

| Repository | Tests | Success | Error | Empty | Status |
|------------|:-----:|:-------:|:-----:|:-----:|:------:|
| AccountsRepository | 2 | [x] | [ ] | [ ] | Partial |
| UserAuthRepository | 0 | [ ] | [ ] | [ ] | Not Started |
| ... (from client-layer/TESTING_STATUS.md)

## Quick Commands

| Action | Command |
|--------|---------|
| Run all tests | `./gradlew test` |
| Run feature tests | `/verify-tests [feature]` |
| Run client tests | `/verify-tests client` |
| Check gaps | `/gap-analysis testing` |

+=========================================================================+
```

---

## Feature Test Mapping (from MODULES_INDEX.md)

| # | Feature | Module Path | Unit Test Path | UI Test Path |
|:-:|---------|-------------|----------------|--------------|
| 1 | auth | feature/auth | feature/auth/src/commonTest/ | feature/auth/src/androidInstrumentedTest/ |
| 2 | home | feature/home | feature/home/src/commonTest/ | feature/home/src/androidInstrumentedTest/ |
| 3 | accounts | feature/accounts | feature/accounts/src/commonTest/ | feature/accounts/src/androidInstrumentedTest/ |
| 4 | beneficiary | feature/beneficiary | feature/beneficiary/src/commonTest/ | feature/beneficiary/src/androidInstrumentedTest/ |
| 5 | loan-account | feature/loan-account | feature/loan-account/src/commonTest/ | feature/loan-account/src/androidInstrumentedTest/ |
| 6 | savings-account | feature/savings-account | feature/savings-account/src/commonTest/ | feature/savings-account/src/androidInstrumentedTest/ |
| 7 | share-account | feature/share-account | feature/share-account/src/commonTest/ | feature/share-account/src/androidInstrumentedTest/ |
| 8 | transfer | feature/transfer-process | feature/transfer-process/src/commonTest/ | feature/transfer-process/src/androidInstrumentedTest/ |
| 9 | recent-transaction | feature/recent-transaction | feature/recent-transaction/src/commonTest/ | feature/recent-transaction/src/androidInstrumentedTest/ |
| 10 | notification | feature/notification | feature/notification/src/commonTest/ | feature/notification/src/androidInstrumentedTest/ |
| 11 | settings | feature/settings | feature/settings/src/commonTest/ | feature/settings/src/androidInstrumentedTest/ |
| 12 | passcode | libs/mifos-passcode | libs/mifos-passcode/src/commonTest/ | libs/mifos-passcode/src/androidInstrumentedTest/ |
| 13 | guarantor | feature/guarantor | feature/guarantor/src/commonTest/ | feature/guarantor/src/androidInstrumentedTest/ |
| 14 | qr | feature/qr-code | feature/qr-code/src/commonTest/ | feature/qr-code/src/androidInstrumentedTest/ |
| 15 | location | feature/location | feature/location/src/commonTest/ | feature/location/src/androidInstrumentedTest/ |
| 16 | user-profile | feature/user-profile | feature/user-profile/src/commonTest/ | feature/user-profile/src/androidInstrumentedTest/ |

---

## Test Type Commands

### `/verify-tests [feature]` - All Tests

```bash
# Unit tests (ViewModel)
./gradlew :feature:[module]:test

# UI tests (Screen) - requires emulator
./gradlew :feature:[module]:connectedDebugAndroidTest
```

### `/verify-tests [feature] unit` - ViewModel Only

```bash
./gradlew :feature:[module]:test
```

### `/verify-tests [feature] ui` - Screen Only

```bash
./gradlew :feature:[module]:connectedDebugAndroidTest
```

### `/verify-tests [feature] integration` - E2E Flow

```bash
./gradlew :cmp-android:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=org.mifos.mobile.[Feature]FlowTest
```

### `/verify-tests [feature] screenshot` - Visual

```bash
# Compare against golden images
./gradlew :core:designsystem:compareRoborazziDebug

# Record new golden images
./gradlew :core:designsystem:recordRoborazziDebug
```

---

## Layer Test Commands

### `/verify-tests client` - Repository Tests

```bash
./gradlew :core:data:test
```

### `/verify-tests feature` - All Feature Tests

```bash
./gradlew feature:test
```

### `/verify-tests platform` - Platform Tests

```bash
# E2E tests
./gradlew :cmp-android:connectedDebugAndroidTest

# Screenshot tests
./gradlew :core:designsystem:compareRoborazziDebug
```

---

## Output Format

### After Running Tests

```
+=========================================================================+
|  VERIFY TESTS - [target]                                                 |
+=========================================================================+

## Test Execution

| Type | Command | Tests | Passed | Failed | Status |
|------|---------|:-----:|:------:|:------:|:------:|
| Unit | `./gradlew :feature:auth:test` | 45 | 45 | 0 | [x] |
| UI | `./gradlew :feature:auth:connectedDebugAndroidTest` | 25 | 23 | 2 | [!] |

## Failed Tests

| Test | Error | File |
|------|-------|------|
| LoginScreenTest.testErrorState | AssertionError | LoginScreenTest.kt:45 |
| LoginScreenTest.testLoading | TimeoutException | LoginScreenTest.kt:32 |

## Coverage Summary

| Component | Coverage | Target | Status |
|-----------|:--------:|:------:|:------:|
| ViewModel | 85% | 80% | [x] Pass |
| Screen | 72% | 60% | [x] Pass |
| Repository | 90% | 80% | [x] Pass |

## Index Updated

[x] feature-layer/TESTING_STATUS.md - Updated test counts
[x] Last run: [timestamp]

+---------+----------------------------------------------------------+
|  NEXT STEPS                                                        |
+---------+----------------------------------------------------------+
| 1 | Fix failing tests: LoginScreenTest.kt:45, :32              |
| 2 | Increase coverage: Add tests for uncovered paths           |
| 3 | Re-run: /verify-tests auth                                 |
+---------+----------------------------------------------------------+
```

---

## Error Handling

### Feature Not Found

```
+-------------------------------------------------------------------------+
|  ERROR: Feature '[name]' not found                                       |
+-------------------------------------------------------------------------+
|                                                                          |
|  The feature '[name]' does not exist in MODULES_INDEX.md                |
|                                                                          |
|  Available features:                                                    |
|  auth, home, accounts, beneficiary, loan-account, savings-account,      |
|  share-account, transfer, recent-transaction, notification, settings,   |
|  passcode, guarantor, qr, location, user-profile                        |
|                                                                          |
|  Did you mean: [closest match]?                                         |
|                                                                          |
+-------------------------------------------------------------------------+
```

### No Tests Found

```
+-------------------------------------------------------------------------+
|  WARNING: No tests found for '[feature]'                                 |
+-------------------------------------------------------------------------+
|                                                                          |
|  Test directory: feature/[module]/src/commonTest/                       |
|  Status: Empty                                                          |
|                                                                          |
|  To create tests:                                                       |
|  1. Run /gap-planning [feature] testing                                 |
|  2. Follow TDD pattern in TESTING_STATUS.md                             |
|                                                                          |
+-------------------------------------------------------------------------+
```

### Gradle Error

```
+-------------------------------------------------------------------------+
|  ERROR: Gradle build failed                                              |
+-------------------------------------------------------------------------+
|                                                                          |
|  Command: ./gradlew :feature:[module]:test                              |
|  Exit code: 1                                                           |
|                                                                          |
|  Error output:                                                          |
|  [Gradle error message]                                                 |
|                                                                          |
|  Suggestions:                                                           |
|  1. Check compilation errors: ./gradlew :feature:[module]:compileKotlin |
|  2. Clean build: ./gradlew clean                                        |
|  3. Check dependencies: ./gradlew :feature:[module]:dependencies        |
|                                                                          |
+-------------------------------------------------------------------------+
```

---

## Coverage Targets

| Component | Minimum | Target | Excellent |
|-----------|:-------:|:------:|:---------:|
| ViewModel | 60% | 80% | 90%+ |
| Repository | 70% | 80% | 90%+ |
| Screen | 40% | 60% | 80%+ |
| Integration | - | 8 flows | 15+ flows |
| Screenshot | - | 30 golden | 60+ golden |

---

## TestTag System (from TESTING_STATUS.md)

### Pattern: `feature:component:element`

```kotlin
object TestTags {
    object Auth {
        const val SCREEN = "auth:screen"
        const val USERNAME_FIELD = "auth:username"
        const val PASSWORD_FIELD = "auth:password"
        const val LOGIN_BUTTON = "auth:loginButton"
        const val ERROR_MESSAGE = "auth:error"
        const val LOADING_INDICATOR = "auth:loading"
    }
    // ... for all features
}
```

---

## Integration Test Flows (from platform-layer/TESTING_STATUS.md)

| # | Flow | Screens | Tests | Status |
|:-:|------|:-------:|:-----:|:------:|
| 1 | Login -> Passcode -> Home | 3 | 0 | [ ] |
| 2 | Registration -> OTP -> Login | 4 | 0 | [ ] |
| 3 | Home -> Account Details | 2 | 0 | [ ] |
| 4 | Home -> Transfer -> Confirm | 3 | 0 | [ ] |
| 5 | Home -> Beneficiary -> Add | 2 | 0 | [ ] |
| 6 | Settings -> Change Password | 2 | 0 | [ ] |
| 7 | Loan -> Schedule -> Summary | 3 | 0 | [ ] |
| 8 | QR -> Scan -> Transfer | 3 | 0 | [ ] |

---

## Related Commands

| Command | Purpose |
|---------|---------|
| `/gap-analysis testing` | View all testing gaps |
| `/gap-analysis [layer] testing` | Layer-specific test gaps |
| `/gap-planning [feature] testing` | Plan test implementation |
| `/verify [feature]` | Verify implementation vs spec |

---

## Key Files

```
claude-product-cycle/
+-- feature-layer/
|   +-- TESTING_STATUS.md        # O(1) ViewModel/Screen test status
|   +-- MODULES_INDEX.md         # Feature -> path mapping
+-- client-layer/
|   +-- TESTING_STATUS.md        # O(1) Repository test status
+-- platform-layer/
|   +-- TESTING_STATUS.md        # O(1) E2E/Screenshot status
```

---

## Gradle Commands Reference

### Unit Tests

```bash
# All unit tests
./gradlew test

# Specific module
./gradlew :feature:auth:test
./gradlew :core:data:test

# With coverage
./gradlew test jacocoTestReport
```

### UI Tests

```bash
# All UI tests (requires emulator/device)
./gradlew connectedDebugAndroidTest

# Specific feature
./gradlew :feature:auth:connectedDebugAndroidTest
```

### Screenshot Tests (Roborazzi)

```bash
# Record golden images
./gradlew :core:designsystem:recordRoborazziDebug

# Compare against golden images
./gradlew :core:designsystem:compareRoborazziDebug

# View differences
open build/reports/roborazzi/
```

ARGUMENTS: $ARGUMENTS

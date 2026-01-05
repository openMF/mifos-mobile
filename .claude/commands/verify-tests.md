# Verify Tests Command

Run and verify tests for features across the project.

## Usage

```
/verify-tests                        # Run all tests, show status
/verify-tests auth                   # Run auth feature tests
/verify-tests auth unit              # Run auth ViewModel tests only
/verify-tests auth ui                # Run auth UI tests only
/verify-tests auth integration       # Run auth integration tests
/verify-tests auth screenshot        # Run auth screenshot tests
/verify-tests client                 # Run all client layer tests
/verify-tests feature                # Run all feature layer tests
/verify-tests platform               # Run all platform tests
```

---

## Output Format

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  VERIFY TESTS - [target]                                                     ║
╠══════════════════════════════════════════════════════════════════════════════╣

## Test Execution

| Type | Command | Tests | Passed | Failed | Status |
|------|---------|:-----:|:------:|:------:|:------:|
| Unit | `./gradlew :feature:auth:test` | 45 | 45 | 0 | ✅ |
| UI | `./gradlew :feature:auth:connectedDebugAndroidTest` | 25 | 23 | 2 | ⚠️ |
| Integration | `./gradlew :cmp-android:connectedDebugAndroidTest` | 8 | 8 | 0 | ✅ |
| Screenshot | `./gradlew :core:designsystem:compareRoborazziDebug` | 12 | 12 | 0 | ✅ |

## Failed Tests

| Test | Error | File |
|------|-------|------|
| LoginScreenTest.testErrorState | AssertionError | LoginScreenTest.kt:45 |
| LoginScreenTest.testLoading | TimeoutException | LoginScreenTest.kt:32 |

## Coverage Summary

| Component | Coverage | Target | Status |
|-----------|:--------:|:------:|:------:|
| ViewModel | 85% | 80% | ✅ |
| Screen | 72% | 60% | ✅ |
| Repository | 90% | 80% | ✅ |

---

## Next Steps

1. Fix failing tests: `/gap-planning auth testing`
2. Increase coverage: Add tests for uncovered paths
3. Re-run: `/verify-tests auth`

╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## Test Commands Reference

### Unit Tests (ViewModel + Repository)

```bash
# All unit tests
./gradlew test

# Specific module
./gradlew :feature:auth:test
./gradlew :core:data:test

# With coverage
./gradlew test jacocoTestReport
```

### UI Tests (Compose)

```bash
# All UI tests (requires emulator/device)
./gradlew connectedDebugAndroidTest

# Specific feature
./gradlew :feature:auth:connectedDebugAndroidTest
```

### Integration Tests (E2E)

```bash
# Full E2E tests
./gradlew :cmp-android:connectedDebugAndroidTest

# Specific test class
./gradlew :cmp-android:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=org.mifos.mobile.AuthFlowTest
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

---

## Instructions for Claude

### Step 1: Determine Test Scope

| Parameter | Test Type | Gradle Command |
|-----------|-----------|----------------|
| (none) | All tests | `./gradlew test connectedDebugAndroidTest` |
| `[feature]` | Feature tests | `./gradlew :feature:[name]:test` |
| `[feature] unit` | ViewModel only | `./gradlew :feature:[name]:test` |
| `[feature] ui` | Screen tests | `./gradlew :feature:[name]:connectedDebugAndroidTest` |
| `[feature] integration` | E2E flow | `./gradlew :cmp-android:connectedDebugAndroidTest` |
| `[feature] screenshot` | Visual | `./gradlew :core:designsystem:compareRoborazziDebug` |
| `client` | Repositories | `./gradlew :core:data:test` |
| `feature` | All ViewModels | `./gradlew feature:test` |
| `platform` | All E2E | `./gradlew :cmp-android:connectedDebugAndroidTest` |

### Step 2: Execute Tests

Run the appropriate Gradle command and capture output.

### Step 3: Parse Results

From Gradle output, extract:
- Total tests run
- Tests passed
- Tests failed
- Failed test names and errors
- Coverage percentages (if available)

### Step 4: Generate Report

Display results in the formatted output above.

### Step 5: Suggest Next Steps

Based on results:
- If all pass: "All tests passing. Coverage: X%"
- If failures: List fixes needed with file paths
- If low coverage: Suggest adding tests

---

## Feature Test Mapping

| Feature | Unit Test Path | UI Test Path |
|---------|----------------|--------------|
| auth | `feature/auth/src/commonTest/` | `feature/auth/src/androidInstrumentedTest/` |
| home | `feature/home/src/commonTest/` | `feature/home/src/androidInstrumentedTest/` |
| accounts | `feature/account/src/commonTest/` | `feature/account/src/androidInstrumentedTest/` |
| beneficiary | `feature/beneficiary/src/commonTest/` | `feature/beneficiary/src/androidInstrumentedTest/` |
| loan-account | `feature/loan-account/src/commonTest/` | `feature/loan-account/src/androidInstrumentedTest/` |
| savings-account | `feature/savings-account/src/commonTest/` | `feature/savings-account/src/androidInstrumentedTest/` |
| transfer | `feature/transfer-process/src/commonTest/` | `feature/transfer-process/src/androidInstrumentedTest/` |
| notification | `feature/notification/src/commonTest/` | `feature/notification/src/androidInstrumentedTest/` |
| settings | `feature/settings/src/commonTest/` | `feature/settings/src/androidInstrumentedTest/` |
| qr | `feature/qr-code/src/commonTest/` | `feature/qr-code/src/androidInstrumentedTest/` |
| guarantor | `feature/guarantor/src/commonTest/` | `feature/guarantor/src/androidInstrumentedTest/` |
| passcode | `libs/mifos-passcode/src/commonTest/` | `libs/mifos-passcode/src/androidInstrumentedTest/` |
| location | `feature/location/src/commonTest/` | `feature/location/src/androidInstrumentedTest/` |
| user-profile | `feature/user-profile/src/commonTest/` | `feature/user-profile/src/androidInstrumentedTest/` |

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

## Related Commands

- `/gap-analysis testing` - View testing status
- `/gap-planning testing [layer]` - Plan test implementation
- `/verify [feature]` - Verify implementation vs spec

ARGUMENTS: $ARGUMENTS

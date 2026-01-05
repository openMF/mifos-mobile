# Platform Layer - Testing Status

> E2E, Screenshot, and Platform-specific testing documentation

---

## Overview

The platform layer handles platform-specific testing:
- E2E integration tests (full user journeys)
- Screenshot/visual regression tests (Roborazzi)
- Platform-specific functionality tests

---

## Current State

| Platform | E2E Tests | Screenshot Tests | Status |
|----------|:---------:|:----------------:|:------:|
| Android | 0 | 0 | ⬜ Not Started |
| iOS | - | - | ⚠️ Manual |
| Desktop | 0 | 0 | ⬜ Not Started |
| Web | 0 | 0 | ⬜ Not Started |

---

## Testing by Platform

### Android (Primary)

| Test Type | Framework | Location | Status |
|-----------|-----------|----------|:------:|
| E2E Tests | Compose UI Test | `cmp-android/src/androidTest/` | ⬜ |
| Screenshot | Roborazzi | `core/designsystem/src/test/` | ⬜ |
| Unit Tests | JUnit | `cmp-android/src/test/` | ⬜ |

### iOS (CocoaPods)

| Test Type | Framework | Location | Status |
|-----------|-----------|----------|:------:|
| UI Tests | XCUITest | `cmp-ios/iosApp/` | ⬜ |
| Unit Tests | XCTest | `cmp-ios/iosApp/` | ⬜ |

### Desktop (JVM)

| Test Type | Framework | Location | Status |
|-----------|-----------|----------|:------:|
| UI Tests | Compose Desktop Test | `cmp-desktop/src/test/` | ⬜ |
| Unit Tests | JUnit | `cmp-desktop/src/test/` | ⬜ |

### Web (Kotlin/JS)

| Test Type | Framework | Location | Status |
|-----------|-----------|----------|:------:|
| E2E Tests | Playwright/Cypress | TBD | ⬜ |
| Unit Tests | kotlin.test | `cmp-web/src/jsTest/` | ⬜ |

---

## E2E Test Scenarios

### Critical User Journeys

| # | Journey | Platforms | Tests | Status |
|:-:|---------|-----------|:-----:|:------:|
| 1 | Onboarding → Login → Home | Android | 0 | ⬜ |
| 2 | Registration → OTP → Login | Android | 0 | ⬜ |
| 3 | Home → Account Details | Android | 0 | ⬜ |
| 4 | Home → Transfer → Confirm | Android | 0 | ⬜ |
| 5 | Home → Loan Details → Schedule | Android | 0 | ⬜ |
| 6 | Settings → Change Password | Android | 0 | ⬜ |
| 7 | QR Scan → Transfer | Android | 0 | ⬜ |
| 8 | Offline → Online Sync | Android | 0 | ⬜ |

---

## Screenshot Testing (Roborazzi)

### Configuration

**Location**: `core/designsystem/src/test/`

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ComponentScreenshotTest {
    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.LastImage
        )
    )

    @Test
    fun mifosButton_light() {
        composeTestRule.setContent {
            MifosTheme(darkTheme = false) {
                MifosButton(text = "Login", onClick = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun mifosButton_dark() {
        composeTestRule.setContent {
            MifosTheme(darkTheme = true) {
                MifosButton(text = "Login", onClick = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
```

### Screenshot Test Coverage

| Component | Light | Dark | Status |
|-----------|:-----:|:----:|:------:|
| MifosButton | ⬜ | ⬜ | Not Started |
| MifosTextField | ⬜ | ⬜ | Not Started |
| MifosCard | ⬜ | ⬜ | Not Started |
| MifosTopBar | ⬜ | ⬜ | Not Started |
| MifosBottomNav | ⬜ | ⬜ | Not Started |
| MifosDialog | ⬜ | ⬜ | Not Started |
| MifosLoadingWheel | ⬜ | ⬜ | Not Started |
| AccountCard | ⬜ | ⬜ | Not Started |
| TransactionItem | ⬜ | ⬜ | Not Started |
| BeneficiaryItem | ⬜ | ⬜ | Not Started |

---

## Screen Screenshot Tests

| # | Feature | Screens | Golden Images | Status |
|:-:|---------|:-------:|:-------------:|:------:|
| 1 | auth | 6 | 0 | ⬜ |
| 2 | home | 1 | 0 | ⬜ |
| 3 | accounts | 3 | 0 | ⬜ |
| 4 | savings-account | 4 | 0 | ⬜ |
| 5 | loan-account | 4 | 0 | ⬜ |
| 6 | share-account | 2 | 0 | ⬜ |
| 7 | beneficiary | 4 | 0 | ⬜ |
| 8 | transfer | 2 | 0 | ⬜ |
| 9 | recent-transaction | 1 | 0 | ⬜ |
| 10 | notification | 1 | 0 | ⬜ |
| 11 | settings | 9 | 0 | ⬜ |
| 12 | passcode | 2 | 0 | ⬜ |
| 13 | guarantor | 3 | 0 | ⬜ |
| 14 | qr | 3 | 0 | ⬜ |
| 15 | location | 1 | 0 | ⬜ |
| 16 | user-profile | 2 | 0 | ⬜ |

---

## Platform-Specific Tests

### Android-Specific

| Test | Description | Status |
|------|-------------|:------:|
| Deep Links | Verify deep link handling | ⬜ |
| Notifications | Push notification handling | ⬜ |
| Biometrics | Fingerprint/Face ID login | ⬜ |
| Camera | QR code scanning | ⬜ |
| Location | Branch finder | ⬜ |
| Back Navigation | System back button | ⬜ |

### iOS-Specific

| Test | Description | Status |
|------|-------------|:------:|
| Deep Links | Universal links | ⬜ |
| Notifications | APNs handling | ⬜ |
| Biometrics | Touch ID/Face ID | ⬜ |
| Camera | QR code scanning | ⬜ |
| Location | Core Location | ⬜ |

### Desktop-Specific

| Test | Description | Status |
|------|-------------|:------:|
| Window Management | Resize, minimize | ⬜ |
| Keyboard Shortcuts | Standard shortcuts | ⬜ |
| System Tray | Optional integration | ⬜ |

### Web-Specific

| Test | Description | Status |
|------|-------------|:------:|
| Browser Compat | Chrome, Firefox, Safari | ⬜ |
| CORS Handling | API requests | ⬜ |
| Responsive | Mobile/Tablet/Desktop | ⬜ |
| PWA | Service worker, offline | ⬜ |

---

## CI/CD Integration

### GitHub Actions Workflow

```yaml
# .github/workflows/test.yml
name: Tests

on: [push, pull_request]

jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Run unit tests
        run: ./gradlew test

  android-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run Android tests
        run: ./gradlew :cmp-android:connectedDebugAndroidTest

  screenshot-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run Roborazzi tests
        run: ./gradlew :core:designsystem:testDebugUnitTest
      - name: Compare screenshots
        run: ./gradlew :core:designsystem:compareRoborazziDebug
```

---

## Implementation Priority

| Phase | Platform | Tests | Effort |
|:-----:|----------|:-----:|:------:|
| 1 | Android E2E | 20 | L |
| 2 | Screenshot (Roborazzi) | 30 | M |
| 3 | iOS UI | 10 | M |
| 4 | Desktop | 5 | S |
| 5 | Web | 10 | M |

---

## Commands

```bash
# Run Android E2E tests
./gradlew :cmp-android:connectedDebugAndroidTest

# Run screenshot tests
./gradlew :core:designsystem:testDebugUnitTest

# Record new screenshots
./gradlew :core:designsystem:recordRoborazziDebug

# Compare screenshots
./gradlew :core:designsystem:compareRoborazziDebug

# Check platform test status
/gap-analysis platform testing
```

---

## Related Files

- [LAYER_STATUS.md](./LAYER_STATUS.md) - Platform status
- [platforms/ANDROID.md](./platforms/ANDROID.md) - Android details
- [platforms/IOS.md](./platforms/IOS.md) - iOS details
- [platforms/DESKTOP.md](./platforms/DESKTOP.md) - Desktop details
- [platforms/WEB.md](./platforms/WEB.md) - Web details

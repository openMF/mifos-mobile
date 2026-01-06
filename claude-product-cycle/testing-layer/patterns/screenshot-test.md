# Screenshot Test Pattern

> Visual regression testing with Roborazzi in Mifos Mobile

---

## Overview

Screenshot tests:
- Capture golden images of UI states
- Detect visual regressions
- Document UI appearance
- Ensure design consistency

---

## File Location

```
feature/${feature}/src/test/kotlin/org/mifos/mobile/feature/${feature}/${Feature}ScreenshotTest.kt
```

---

## Dependencies

```kotlin
// build.gradle.kts
plugins {
    id("io.github.takahirom.roborazzi")
}

dependencies {
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.robolectric)
    testImplementation(libs.compose.ui.test.junit4)
}
```

---

## Test Structure

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w360dp-h640dp-xhdpi")
class ${Feature}ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.LastImage(),
            outputDirectoryPath = "src/test/resources/screenshots/${feature}"
        )
    )

    // ═══════════════════════════════════════════════════════════════
    // LOADING STATE
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun ${feature}Screen_loading() {
        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Loading
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    // ═══════════════════════════════════════════════════════════════
    // SUCCESS STATE
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun ${feature}Screen_success() {
        val testData = ${Feature}Fixtures.createList(3)

        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Success(testData)
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun ${feature}Screen_successWithManyItems() {
        val testData = ${Feature}Fixtures.createList(10)

        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Success(testData)
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    // ═══════════════════════════════════════════════════════════════
    // EMPTY STATE
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun ${feature}Screen_empty() {
        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Success(emptyList())
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    // ═══════════════════════════════════════════════════════════════
    // ERROR STATE
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun ${feature}Screen_error() {
        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Error("Network error")
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    // ═══════════════════════════════════════════════════════════════
    // DIALOG STATES
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun ${feature}Screen_confirmationDialog() {
        composeTestRule.setContent {
            MifosTheme {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Success(emptyList()),
                        dialogState = DialogState.Confirmation(
                            title = "Delete Item?",
                            message = "This action cannot be undone."
                        )
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    // ═══════════════════════════════════════════════════════════════
    // DARK THEME
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun ${feature}Screen_darkTheme() {
        val testData = ${Feature}Fixtures.createList(3)

        composeTestRule.setContent {
            MifosTheme(darkTheme = true) {
                ${Feature}Content(
                    state = ${Feature}State(
                        uiState = ${Feature}UiState.Success(testData)
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage()
    }
}
```

---

## Golden Image Directory

```
feature/${feature}/src/test/resources/screenshots/${feature}/
├── ${feature}Screen_loading.png
├── ${feature}Screen_success.png
├── ${feature}Screen_successWithManyItems.png
├── ${feature}Screen_empty.png
├── ${feature}Screen_error.png
├── ${feature}Screen_confirmationDialog.png
└── ${feature}Screen_darkTheme.png
```

---

## Roborazzi Commands

### Record New Golden Images

```bash
# Record all screenshots
./gradlew recordRoborazziDebug

# Record for specific module
./gradlew :feature:${feature}:recordRoborazziDebug
```

### Compare Against Golden Images

```bash
# Verify screenshots match
./gradlew verifyRoborazziDebug

# Verify specific module
./gradlew :feature:${feature}:verifyRoborazziDebug
```

### Compare and Generate Report

```bash
# Generate comparison report
./gradlew compareRoborazziDebug
```

---

## Device Configurations

### Standard Phone

```kotlin
@Config(
    sdk = [33],
    qualifiers = "w360dp-h640dp-xhdpi"
)
```

### Large Phone

```kotlin
@Config(
    sdk = [33],
    qualifiers = "w412dp-h915dp-xxhdpi"
)
```

### Tablet

```kotlin
@Config(
    sdk = [33],
    qualifiers = "w800dp-h1280dp-mdpi"
)
```

### Landscape

```kotlin
@Config(
    sdk = [33],
    qualifiers = "land-w640dp-h360dp-xhdpi"
)
```

---

## Component Screenshots

For design system components:

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class MifosButtonScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mifosButton_primary() {
        composeTestRule.setContent {
            MifosTheme {
                MifosButton(
                    text = "Login",
                    onClick = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun mifosButton_disabled() {
        composeTestRule.setContent {
            MifosTheme {
                MifosButton(
                    text = "Login",
                    onClick = {},
                    enabled = false
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun mifosButton_loading() {
        composeTestRule.setContent {
            MifosTheme {
                MifosButton(
                    text = "Login",
                    onClick = {},
                    isLoading = true
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
```

---

## Multi-State Preview

Capture multiple states in one image:

```kotlin
@Test
fun ${feature}Screen_allStates() {
    composeTestRule.setContent {
        MifosTheme {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Loading
                Box(modifier = Modifier.weight(1f)) {
                    ${Feature}Content(
                        state = ${Feature}State(uiState = Loading),
                        onAction = {}
                    )
                }

                // Success
                Box(modifier = Modifier.weight(1f)) {
                    ${Feature}Content(
                        state = ${Feature}State(uiState = Success(data)),
                        onAction = {}
                    )
                }

                // Error
                Box(modifier = Modifier.weight(1f)) {
                    ${Feature}Content(
                        state = ${Feature}State(uiState = Error("Error")),
                        onAction = {}
                    )
                }
            }
        }
    }

    composeTestRule.onRoot().captureRoboImage()
}
```

---

## CI Integration

### GitHub Actions

```yaml
# .github/workflows/screenshots.yml
name: Screenshot Tests

on: [pull_request]

jobs:
  screenshot-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Verify Screenshots
        run: ./gradlew verifyRoborazziDebug

      - name: Upload Comparison Report
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: screenshot-comparison
          path: '**/build/outputs/roborazzi/**'
```

---

## Best Practices

### 1. Consistent Test Data

```kotlin
// Use fixtures for consistent data
val testData = ${Feature}Fixtures.createList(3)

// Not random data that changes
val testData = listOf(
    Item(name = UUID.randomUUID().toString())  // Bad!
)
```

### 2. Fixed Dimensions

```kotlin
// Wrap content in fixed size for consistency
Box(modifier = Modifier.size(360.dp, 640.dp)) {
    ${Feature}Content(...)
}
```

### 3. Disable Animations

```kotlin
@Before
fun setup() {
    // Disable animations for consistent screenshots
    composeTestRule.mainClock.autoAdvance = false
}
```

### 4. Test All States

- Loading
- Success (with data)
- Success (empty)
- Error
- Dialogs
- Dark theme

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Images differ slightly | Ensure fixed device config |
| Animations cause diff | Disable animations |
| Font rendering differs | Use Robolectric native graphics |
| Random data in image | Use fixtures with fixed data |

---

## Checklist

For each screen:

- [ ] Loading state screenshot
- [ ] Success state (with data)
- [ ] Success state (empty)
- [ ] Error state
- [ ] Dialog states
- [ ] Dark theme variant
- [ ] Different device sizes (optional)

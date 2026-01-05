# Screen Test Pattern

> Detailed instructions for testing Compose screens in Mifos Mobile

---

## Overview

Screen tests verify:
- UI renders correctly for each state
- User interactions trigger correct actions
- Accessibility (content descriptions, testTags)
- Visual appearance (with screenshots)

---

## File Location

```
feature/${feature}/src/androidInstrumentedTest/kotlin/org/mifos/mobile/feature/${feature}/${Feature}ScreenTest.kt
```

---

## Dependencies

```kotlin
// build.gradle.kts
kotlin {
    sourceSets {
        androidInstrumentedTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }
    }
}
```

---

## Test Structure

```kotlin
class ${Feature}ScreenTest {
    // ═══════════════════════════════════════════════════════════════
    // SETUP
    // ═══════════════════════════════════════════════════════════════

    @get:Rule
    val composeTestRule = createComposeRule()

    // ═══════════════════════════════════════════════════════════════
    // LOADING STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun loadingState_displaysLoadingIndicator() {
        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(uiState = ${Feature}UiState.Loading),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LOADING)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LIST)
            .assertDoesNotExist()
    }

    // ═══════════════════════════════════════════════════════════════
    // SUCCESS STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun successState_displaysContent() {
        val testData = ${Feature}Fixtures.createList(3)

        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Success(testData)
                ),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.SCREEN)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.LIST)
            .assertIsDisplayed()
    }

    @Test
    fun successState_displaysAllItems() {
        val testData = ${Feature}Fixtures.createList(5)

        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Success(testData)
                ),
                onAction = {}
            )
        }

        testData.forEach { item ->
            composeTestRule
                .onNodeWithTag(${Feature}TestTags.item(item.id))
                .assertIsDisplayed()
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // ERROR STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun errorState_displaysErrorMessage() {
        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Error("Network error")
                ),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.ERROR)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Network error")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysRetryButton() {
        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Error("Error")
                ),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.RETRY)
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    // ═══════════════════════════════════════════════════════════════
    // EMPTY STATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun emptyState_displaysEmptyMessage() {
        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Success(emptyList())
                ),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.EMPTY)
            .assertIsDisplayed()
    }

    // ═══════════════════════════════════════════════════════════════
    // USER INTERACTION TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun itemClick_triggersAction() {
        var receivedAction: ${Feature}Action? = null
        val testData = ${Feature}Fixtures.createList(3)

        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Success(testData)
                ),
                onAction = { receivedAction = it }
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.item(testData[0].id))
            .performClick()

        assertEquals(
            ${Feature}Action.ItemClicked(testData[0].id),
            receivedAction
        )
    }

    @Test
    fun retryClick_triggersRetryAction() {
        var receivedAction: ${Feature}Action? = null

        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Error("Error")
                ),
                onAction = { receivedAction = it }
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.RETRY)
            .performClick()

        assertEquals(${Feature}Action.Retry, receivedAction)
    }

    @Test
    fun fabClick_triggersAddAction() {
        var receivedAction: ${Feature}Action? = null

        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Success(emptyList())
                ),
                onAction = { receivedAction = it }
            )
        }

        composeTestRule
            .onNodeWithTag(${Feature}TestTags.FAB)
            .performClick()

        assertEquals(${Feature}Action.AddClicked, receivedAction)
    }

    // ═══════════════════════════════════════════════════════════════
    // FORM INPUT TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun textInput_updatesState() {
        var receivedAction: ${Feature}Action? = null

        composeTestRule.setContent {
            ${Feature}FormContent(
                state = ${Feature}FormState(),
                onAction = { receivedAction = it }
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:input:name")
            .performTextInput("Test Name")

        assertEquals(
            ${Feature}Action.NameChanged("Test Name"),
            receivedAction
        )
    }

    @Test
    fun submitButton_disabledWhenInvalid() {
        composeTestRule.setContent {
            ${Feature}FormContent(
                state = ${Feature}FormState(name = ""),  // Invalid
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:submit")
            .assertIsNotEnabled()
    }

    @Test
    fun submitButton_enabledWhenValid() {
        composeTestRule.setContent {
            ${Feature}FormContent(
                state = ${Feature}FormState(name = "Valid Name"),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithTag("${feature}:submit")
            .assertIsEnabled()
    }

    // ═══════════════════════════════════════════════════════════════
    // DIALOG TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun confirmationDialog_displayedWhenDialogStateSet() {
        composeTestRule.setContent {
            ${Feature}Content(
                state = ${Feature}State(
                    uiState = ${Feature}UiState.Success(emptyList()),
                    dialogState = DialogState.Confirmation(
                        title = "Delete?",
                        message = "Are you sure?"
                    )
                ),
                onAction = {}
            )
        }

        composeTestRule
            .onNodeWithText("Delete?")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Are you sure?")
            .assertIsDisplayed()
    }
}
```

---

## Compose Test API Reference

### Finding Nodes

| Method | Purpose | Example |
|--------|---------|---------|
| `onNodeWithTag(tag)` | Find by testTag | `onNodeWithTag("auth:screen")` |
| `onNodeWithText(text)` | Find by text | `onNodeWithText("Login")` |
| `onNodeWithContentDescription(desc)` | Find by a11y label | `onNodeWithContentDescription("Close")` |
| `onAllNodesWithTag(tag)` | Find all matching | `onAllNodesWithTag("item")` |
| `onRoot()` | Get root node | `onRoot()` |

### Assertions

| Method | Purpose |
|--------|---------|
| `assertIsDisplayed()` | Verify visible |
| `assertDoesNotExist()` | Verify not in tree |
| `assertIsEnabled()` | Verify clickable |
| `assertIsNotEnabled()` | Verify disabled |
| `assertTextEquals(text)` | Verify text content |
| `assertHasClickAction()` | Verify clickable |

### Actions

| Method | Purpose |
|--------|---------|
| `performClick()` | Tap element |
| `performTextInput(text)` | Type text |
| `performTextClearance()` | Clear text field |
| `performScrollTo()` | Scroll to element |
| `performSwipeLeft()` | Swipe gesture |
| `performTouchInput { swipeUp() }` | Custom touch |

### Waiting

```kotlin
// Wait for condition
composeTestRule.waitUntil(timeoutMillis = 5000) {
    composeTestRule
        .onAllNodesWithTag("item")
        .fetchSemanticsNodes()
        .isNotEmpty()
}
```

---

## Test Categories

### 1. State Rendering Tests

Test each UI state renders correctly.

```kotlin
@Test
fun loading_showsProgressIndicator() { ... }

@Test
fun success_showsContent() { ... }

@Test
fun error_showsErrorView() { ... }

@Test
fun empty_showsEmptyView() { ... }
```

### 2. User Interaction Tests

Test all clickable elements trigger correct actions.

```kotlin
@Test
fun button_click_triggersAction() {
    var action: Action? = null

    composeTestRule.setContent {
        Screen(onAction = { action = it })
    }

    composeTestRule.onNodeWithTag("button").performClick()

    assertEquals(Action.ButtonClicked, action)
}
```

### 3. Form Tests

Test input fields and validation.

```kotlin
@Test
fun input_updatesOnTyping() { ... }

@Test
fun validation_showsErrorOnInvalid() { ... }

@Test
fun submit_disabledWhenInvalid() { ... }
```

### 4. Navigation Tests

Test navigation callbacks.

```kotlin
@Test
fun backButton_triggersNavigateBack() { ... }

@Test
fun item_click_triggersNavigateToDetail() { ... }
```

---

## TestTag Best Practices

### Applying Tags

```kotlin
@Composable
fun ${Feature}Screen() {
    Scaffold(
        modifier = Modifier.testTag(${Feature}TestTags.SCREEN)
    ) {
        // Content
    }
}
```

### Tag Naming

```kotlin
object ${Feature}TestTags {
    const val SCREEN = "${feature}:screen"
    const val LOADING = "${feature}:loading"
    const val ERROR = "${feature}:error"
    const val EMPTY = "${feature}:empty"
    const val LIST = "${feature}:list"
    const val FAB = "${feature}:fab"

    fun item(id: Long) = "${feature}:item:$id"
}
```

---

## Test Coverage Checklist

For each screen, test:

- [ ] Loading state displays correctly
- [ ] Success state displays content
- [ ] Error state displays message and retry
- [ ] Empty state displays empty message
- [ ] All clickable elements trigger actions
- [ ] Form inputs update state
- [ ] Form validation works
- [ ] Dialogs display correctly
- [ ] Accessibility labels present

---

## Debug Helpers

### Print Compose Tree

```kotlin
composeTestRule.onRoot().printToLog("COMPOSE_TREE")
```

### Print All Nodes

```kotlin
composeTestRule
    .onAllNodes(hasTestTag("item"))
    .printToLog("ITEMS")
```

### Screenshot Debugging

```kotlin
composeTestRule.onRoot().captureToImage()
```

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Not finding node | Check testTag is applied |
| Flaky tests | Use `waitUntil` for async |
| Testing implementation | Test behavior, not structure |
| Missing states | Test all UI states |

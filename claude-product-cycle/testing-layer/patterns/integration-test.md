# Integration Test Pattern

> Detailed instructions for testing user flows in Mifos Mobile

---

## Overview

Integration tests verify:
- Complete user flows across screens
- Navigation between features
- Data persistence across screens
- Real user scenarios

---

## File Location

```
cmp-android/src/androidTest/kotlin/org/mifos/mobile/flow/${Feature}FlowTest.kt
```

---

## Dependencies

```kotlin
// cmp-android/build.gradle.kts
dependencies {
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}
```

---

## Test Structure

```kotlin
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ${Feature}FlowTest {

    // ═══════════════════════════════════════════════════════════════
    // SETUP
    // ═══════════════════════════════════════════════════════════════

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var testRepository: Fake${Feature}Repository

    @Before
    fun setup() {
        hiltRule.inject()
        // Configure initial state
    }

    @After
    fun teardown() {
        testRepository.reset()
    }

    // ═══════════════════════════════════════════════════════════════
    // FLOW TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun loginFlow_navigatesToHome() {
        // Given: User is on login screen
        composeTestRule
            .onNodeWithTag("auth:login:screen")
            .assertIsDisplayed()

        // When: User enters valid credentials
        composeTestRule
            .onNodeWithTag("auth:input:username")
            .performTextInput("testuser")

        composeTestRule
            .onNodeWithTag("auth:input:password")
            .performTextInput("password123")

        composeTestRule
            .onNodeWithTag("auth:submit:login")
            .performClick()

        // Then: User is navigated to passcode screen
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("passcode:screen")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // When: User enters passcode
        enterPasscode("1234")

        // Then: User is navigated to home screen
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("home:screen")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun transferFlow_completesSuccessfully() {
        // Given: User is logged in and on home
        navigateToHome()

        // When: User initiates transfer
        composeTestRule
            .onNodeWithTag("home:action:transfer")
            .performClick()

        // Then: Transfer screen is displayed
        composeTestRule
            .onNodeWithTag("transfer:screen")
            .assertIsDisplayed()

        // When: User fills transfer form
        selectFromAccount(accountIndex = 0)
        selectBeneficiary(beneficiaryIndex = 0)
        enterAmount("1000")

        // When: User reviews transfer
        composeTestRule
            .onNodeWithTag("transfer:review")
            .performClick()

        // Then: Review screen shows correct info
        composeTestRule
            .onNodeWithText("1000")
            .assertIsDisplayed()

        // When: User confirms transfer
        composeTestRule
            .onNodeWithTag("transfer:confirm")
            .performClick()

        // Then: Success screen is displayed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("transfer:success:screen")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════

    private fun navigateToHome() {
        // Skip login/passcode if already logged in
        // Or perform login flow
    }

    private fun enterPasscode(passcode: String) {
        passcode.forEach { digit ->
            composeTestRule
                .onNodeWithText(digit.toString())
                .performClick()
        }
    }

    private fun selectFromAccount(accountIndex: Int) {
        composeTestRule
            .onNodeWithTag("transfer:input:fromAccount")
            .performClick()

        composeTestRule
            .onAllNodesWithTag("dropdown:item")
            .get(accountIndex)
            .performClick()
    }

    private fun selectBeneficiary(beneficiaryIndex: Int) {
        composeTestRule
            .onNodeWithTag("transfer:input:toBeneficiary")
            .performClick()

        composeTestRule
            .onAllNodesWithTag("dropdown:item")
            .get(beneficiaryIndex)
            .performClick()
    }

    private fun enterAmount(amount: String) {
        composeTestRule
            .onNodeWithTag("transfer:input:amount")
            .performTextInput(amount)
    }
}
```

---

## Critical User Flows

### 1. Authentication Flow

```
Login Screen → Passcode Setup → Home
```

```kotlin
@Test
fun newUserRegistration_flow() {
    // 1. Navigate to registration
    composeTestRule.onNodeWithText("Register").performClick()

    // 2. Fill registration form
    composeTestRule.onNodeWithTag("auth:input:firstName").performTextInput("John")
    composeTestRule.onNodeWithTag("auth:input:lastName").performTextInput("Doe")
    composeTestRule.onNodeWithTag("auth:input:email").performTextInput("john@test.com")
    composeTestRule.onNodeWithTag("auth:input:mobile").performTextInput("1234567890")
    composeTestRule.onNodeWithTag("auth:input:username").performTextInput("johndoe")
    composeTestRule.onNodeWithTag("auth:input:password").performTextInput("password123")
    composeTestRule.onNodeWithTag("auth:submit:register").performClick()

    // 3. Verify OTP screen
    waitForScreen("auth:otp:screen")

    // 4. Enter OTP
    composeTestRule.onNodeWithTag("auth:input:otp").performTextInput("123456")
    composeTestRule.onNodeWithTag("auth:submit:verify").performClick()

    // 5. Verify navigation to login
    waitForScreen("auth:login:screen")
}
```

### 2. Transfer Flow

```
Home → Transfer → Review → Confirm → Success
```

```kotlin
@Test
fun thirdPartyTransfer_flow() {
    navigateToHome()

    // 1. Open transfer
    composeTestRule.onNodeWithTag("home:action:transfer").performClick()
    waitForScreen("transfer:screen")

    // 2. Select accounts
    selectFromAccount(0)
    selectBeneficiary(0)

    // 3. Enter details
    enterAmount("500")
    composeTestRule.onNodeWithTag("transfer:input:remark").performTextInput("Test transfer")

    // 4. Review
    composeTestRule.onNodeWithTag("transfer:review").performClick()
    composeTestRule.onNodeWithText("500").assertIsDisplayed()

    // 5. Confirm
    composeTestRule.onNodeWithTag("transfer:confirm").performClick()

    // 6. Verify success
    waitForScreen("transfer:success:screen")
}
```

### 3. Beneficiary CRUD Flow

```
List → Add → Success → List (with new item)
```

```kotlin
@Test
fun addBeneficiary_flow() {
    navigateToHome()
    navigateToBeneficiaries()

    // 1. Open add form
    composeTestRule.onNodeWithTag("beneficiary:fab").performClick()
    waitForScreen("beneficiary:form:screen")

    // 2. Fill form
    composeTestRule.onNodeWithTag("beneficiary:input:name").performTextInput("Test Beneficiary")
    composeTestRule.onNodeWithTag("beneficiary:input:account").performTextInput("123456789")

    // 3. Submit
    composeTestRule.onNodeWithTag("beneficiary:submit").performClick()

    // 4. Verify navigation back to list
    waitForScreen("beneficiary:screen")

    // 5. Verify new beneficiary in list
    composeTestRule.onNodeWithText("Test Beneficiary").assertIsDisplayed()
}
```

### 4. Account Detail Flow

```
Home → Accounts → Savings Detail → Transactions
```

```kotlin
@Test
fun viewSavingsAccount_flow() {
    navigateToHome()

    // 1. Open accounts
    composeTestRule.onNodeWithTag("home:accounts").performClick()
    waitForScreen("accounts:screen")

    // 2. Select savings tab
    composeTestRule.onNodeWithTag("accounts:tab:savings").performClick()

    // 3. Open first account
    composeTestRule.onAllNodesWithTag("accounts:item").get(0).performClick()
    waitForScreen("savings:screen")

    // 4. Verify account details
    composeTestRule.onNodeWithTag("savings:balance").assertIsDisplayed()

    // 5. View transactions
    composeTestRule.onNodeWithTag("savings:tab:transactions").performClick()
    composeTestRule.onNodeWithTag("savings:transaction:list").assertIsDisplayed()
}
```

### 5. Settings Flow

```
Home → Settings → Change Password → Success
```

```kotlin
@Test
fun changePassword_flow() {
    navigateToHome()
    navigateToSettings()

    // 1. Open change password
    composeTestRule.onNodeWithTag("settings:item:changePassword").performClick()

    // 2. Enter passwords
    composeTestRule.onNodeWithTag("settings:input:currentPassword").performTextInput("oldpass")
    composeTestRule.onNodeWithTag("settings:input:newPassword").performTextInput("newpass123")
    composeTestRule.onNodeWithTag("settings:input:confirmPassword").performTextInput("newpass123")

    // 3. Submit
    composeTestRule.onNodeWithTag("settings:submit:password").performClick()

    // 4. Verify success message
    composeTestRule.onNodeWithText("Password changed successfully").assertIsDisplayed()
}
```

---

## Helper Functions

### Wait for Screen

```kotlin
private fun waitForScreen(screenTag: String, timeoutMillis: Long = 5000) {
    composeTestRule.waitUntil(timeoutMillis) {
        composeTestRule
            .onAllNodesWithTag(screenTag)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}
```

### Navigation Helpers

```kotlin
private fun navigateToHome() {
    // If not logged in, perform login
    // Otherwise just verify home screen
}

private fun navigateToBeneficiaries() {
    composeTestRule.onNodeWithTag("home:menu").performClick()
    composeTestRule.onNodeWithText("Beneficiaries").performClick()
    waitForScreen("beneficiary:screen")
}

private fun navigateToSettings() {
    composeTestRule.onNodeWithTag("home:menu").performClick()
    composeTestRule.onNodeWithText("Settings").performClick()
    waitForScreen("settings:screen")
}
```

### Form Helpers

```kotlin
private fun fillTextField(tag: String, text: String) {
    composeTestRule
        .onNodeWithTag(tag)
        .performTextClearance()
        .performTextInput(text)
}

private fun selectDropdownItem(dropdownTag: String, itemIndex: Int) {
    composeTestRule.onNodeWithTag(dropdownTag).performClick()
    composeTestRule.onAllNodesWithTag("dropdown:item").get(itemIndex).performClick()
}
```

---

## Test Data Setup

### Using Fake Repositories

```kotlin
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {
    @Provides
    @Singleton
    fun provideFakeRepository(): BeneficiaryRepository = FakeBeneficiaryRepository()
}
```

### Injecting Test Data

```kotlin
@Before
fun setup() {
    hiltRule.inject()

    // Configure test data
    testRepository.setLoadSuccess(
        listOf(
            Beneficiary(id = 1, name = "Test Beneficiary 1"),
            Beneficiary(id = 2, name = "Test Beneficiary 2")
        )
    )
}
```

---

## Best Practices

### 1. Test User Behavior

```kotlin
// Good: Test what user sees and does
@Test
fun user_canViewAccountBalance() {
    navigateToAccount()
    composeTestRule.onNodeWithText("$5,000").assertIsDisplayed()
}

// Bad: Test implementation details
@Test
fun viewModel_stateIsCorrect() { ... }
```

### 2. Use Meaningful Names

```kotlin
// Good
@Test
fun transferWithInsufficientFunds_showsError() { ... }

// Bad
@Test
fun test1() { ... }
```

### 3. Single Assertion Focus

```kotlin
// Good: One logical assertion per test
@Test
fun loginSuccess_navigatesToHome() { ... }

@Test
fun loginFailure_showsErrorMessage() { ... }

// Bad: Multiple unrelated assertions
@Test
fun loginTest() {
    // Tests success AND failure AND navigation...
}
```

---

## Checklist

For each critical flow:

- [ ] Happy path test
- [ ] Error handling test
- [ ] Edge case tests
- [ ] Back navigation test
- [ ] State persistence test

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Hardcoded waits | Use `waitUntil` |
| Missing test data | Inject fake repositories |
| Flaky tests | Add proper synchronization |
| Testing too much | Split into smaller tests |

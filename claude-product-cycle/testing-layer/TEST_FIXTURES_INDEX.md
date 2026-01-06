# Test Fixtures Index - O(1) Lookup

> **17 features** | Reusable test data | **Last Updated**: 2026-01-05

---

## Quick Lookup

| # | Feature | Fixture File | Models | Status |
|:-:|---------|--------------|:------:|:------:|
| 1 | auth | `AuthFixtures.kt` | User, AuthPayload | ❌ |
| 2 | home | `HomeFixtures.kt` | ClientAccounts | ❌ |
| 3 | accounts | `AccountFixtures.kt` | SavingAccount, LoanAccount, ShareAccount | ❌ |
| 4 | savings-account | `SavingsFixtures.kt` | SavingAccount, Transaction | ❌ |
| 5 | loan-account | `LoanFixtures.kt` | LoanAccount, RepaymentSchedule | ❌ |
| 6 | share-account | `ShareFixtures.kt` | ShareAccount | ❌ |
| 7 | beneficiary | `BeneficiaryFixtures.kt` | Beneficiary, BeneficiaryPayload | ❌ |
| 8 | transfer | `TransferFixtures.kt` | TransferPayload, TransferTemplate | ❌ |
| 9 | recent-transaction | `TransactionFixtures.kt` | Transaction | ❌ |
| 10 | notification | `NotificationFixtures.kt` | Notification | ❌ |
| 11 | settings | `SettingsFixtures.kt` | - | ❌ |
| 12 | passcode | `PasscodeFixtures.kt` | - | ❌ |
| 13 | guarantor | `GuarantorFixtures.kt` | Guarantor | ❌ |
| 14 | qr | `QrFixtures.kt` | QrPayload | ❌ |
| 15 | location | `LocationFixtures.kt` | Office | ❌ |
| 16 | client-charge | `ChargeFixtures.kt` | Charge | ❌ |
| 17 | dashboard | `DashboardFixtures.kt` | - | ❌ |

---

## O(1) Path Pattern

```
core/testing/src/commonMain/kotlin/org/mifos/mobile/core/testing/fixtures/[Feature]Fixtures.kt
```

---

## Fixture Pattern

### Standard Structure

```kotlin
object ${Feature}Fixtures {
    // Single item
    fun create${Model}(
        id: Long = 1L,
        name: String = "Test ${Model}",
        // ... other params with defaults
    ): ${Model} = ${Model}(
        id = id,
        name = name,
        // ...
    )

    // List
    fun create${Model}List(count: Int = 3): List<${Model}> =
        (1..count).map { create${Model}(id = it.toLong()) }

    // Specific scenarios
    val empty${Model} = create${Model}(name = "")
    val invalid${Model} = create${Model}(id = -1)
}
```

---

## Feature Fixtures

### 1. AuthFixtures

```kotlin
object AuthFixtures {
    fun createUser(
        userId: Long = 1L,
        userName: String = "testuser",
        clientId: Long = 100L,
        clientName: String = "Test Client",
        authenticated: Boolean = true
    ): User = User(
        userId = userId,
        userName = userName,
        clientId = clientId,
        clientName = clientName,
        authenticated = authenticated
    )

    fun createAuthPayload(
        username: String = "testuser",
        password: String = "password123"
    ): LoginPayload = LoginPayload(
        username = username,
        password = password
    )

    // Pre-built scenarios
    val validUser = createUser()
    val unauthenticatedUser = createUser(authenticated = false)
    val validCredentials = createAuthPayload()
    val invalidCredentials = createAuthPayload(password = "wrong")
}
```

### 2. BeneficiaryFixtures

```kotlin
object BeneficiaryFixtures {
    fun createBeneficiary(
        id: Long = 1L,
        name: String = "John Doe",
        officeName: String = "Main Office",
        accountNumber: String = "000000001",
        transferLimit: Double = 10000.0
    ): Beneficiary = Beneficiary(
        id = id,
        name = name,
        officeName = officeName,
        accountNumber = accountNumber,
        transferLimit = transferLimit
    )

    fun createBeneficiaryList(count: Int = 3): List<Beneficiary> =
        (1..count).map { i ->
            createBeneficiary(
                id = i.toLong(),
                name = "Beneficiary $i",
                accountNumber = "00000000$i"
            )
        }

    fun createBeneficiaryPayload(
        name: String = "New Beneficiary",
        accountNumber: String = "123456789",
        officeId: Int = 1,
        accountType: Int = 1
    ): BeneficiaryPayload = BeneficiaryPayload(
        name = name,
        accountNumber = accountNumber,
        officeId = officeId,
        accountType = accountType
    )

    // Pre-built scenarios
    val emptyList = emptyList<Beneficiary>()
    val singleBeneficiary = listOf(createBeneficiary())
    val multipleBeneficiaries = createBeneficiaryList(5)
}
```

### 3. AccountFixtures

```kotlin
object AccountFixtures {
    fun createSavingAccount(
        id: Long = 1L,
        accountNo: String = "SAV-001",
        productName: String = "Regular Savings",
        balance: Double = 5000.0,
        status: Status = Status(active = true)
    ): SavingAccount = SavingAccount(
        id = id,
        accountNo = accountNo,
        productName = productName,
        accountBalance = balance,
        status = status
    )

    fun createLoanAccount(
        id: Long = 1L,
        accountNo: String = "LOAN-001",
        productName: String = "Personal Loan",
        principal: Double = 50000.0,
        outstanding: Double = 25000.0,
        status: Status = Status(active = true)
    ): LoanAccount = LoanAccount(
        id = id,
        accountNo = accountNo,
        productName = productName,
        principal = principal,
        loanBalance = outstanding,
        status = status
    )

    fun createShareAccount(
        id: Long = 1L,
        accountNo: String = "SHR-001",
        productName: String = "Community Shares",
        totalShares: Int = 100,
        status: Status = Status(active = true)
    ): ShareAccount = ShareAccount(
        id = id,
        accountNo = accountNo,
        productName = productName,
        totalApprovedShares = totalShares,
        status = status
    )

    fun createClientAccounts(
        savingsCount: Int = 2,
        loansCount: Int = 1,
        sharesCount: Int = 1
    ): ClientAccounts = ClientAccounts(
        savingsAccounts = (1..savingsCount).map { createSavingAccount(id = it.toLong()) },
        loanAccounts = (1..loansCount).map { createLoanAccount(id = it.toLong()) },
        shareAccounts = (1..sharesCount).map { createShareAccount(id = it.toLong()) }
    )

    // Pre-built scenarios
    val emptyAccounts = ClientAccounts(emptyList(), emptyList(), emptyList())
    val savingsOnly = ClientAccounts(listOf(createSavingAccount()), emptyList(), emptyList())
    val fullPortfolio = createClientAccounts(3, 2, 1)
}
```

### 4. TransferFixtures

```kotlin
object TransferFixtures {
    fun createTransferPayload(
        fromAccountId: Long = 1L,
        fromClientId: Long = 100L,
        toAccountId: Long = 2L,
        toClientId: Long = 200L,
        amount: Double = 1000.0,
        transferDate: String = "2026-01-05",
        remark: String = "Test transfer"
    ): TransferPayload = TransferPayload(
        fromAccountId = fromAccountId,
        fromClientId = fromClientId,
        toAccountId = toAccountId,
        toClientId = toClientId,
        transferAmount = amount,
        transferDate = transferDate,
        transferDescription = remark
    )

    fun createTransferTemplate(
        fromAccounts: List<SavingAccount> = listOf(AccountFixtures.createSavingAccount()),
        beneficiaries: List<Beneficiary> = BeneficiaryFixtures.createBeneficiaryList()
    ): TransferTemplate = TransferTemplate(
        fromAccountOptions = fromAccounts,
        toBeneficiaryList = beneficiaries
    )

    // Pre-built scenarios
    val minTransfer = createTransferPayload(amount = 1.0)
    val maxTransfer = createTransferPayload(amount = 100000.0)
    val invalidTransfer = createTransferPayload(amount = -100.0)
}
```

### 5. LoanFixtures

```kotlin
object LoanFixtures {
    fun createLoanWithSchedule(
        account: LoanAccount = AccountFixtures.createLoanAccount(),
        scheduleCount: Int = 12
    ): LoanWithSchedule = LoanWithSchedule(
        account = account,
        schedule = (1..scheduleCount).map { i ->
            RepaymentSchedule(
                installment = i,
                dueDate = "2026-${String.format("%02d", i)}-01",
                principalDue = account.principal / scheduleCount,
                interestDue = 100.0,
                totalDue = (account.principal / scheduleCount) + 100.0,
                paid = i <= 6
            )
        }
    )

    fun createLoanTransaction(
        id: Long = 1L,
        type: String = "REPAYMENT",
        amount: Double = 1000.0,
        date: String = "2026-01-05"
    ): LoanTransaction = LoanTransaction(
        id = id,
        type = TransactionType(value = type),
        amount = amount,
        date = listOf(2026, 1, 5)
    )

    // Pre-built scenarios
    val newLoan = createLoanWithSchedule(scheduleCount = 12)
    val fullyPaidLoan = LoanWithSchedule(
        account = AccountFixtures.createLoanAccount(outstanding = 0.0),
        schedule = emptyList()
    )
}
```

### 6. NotificationFixtures

```kotlin
object NotificationFixtures {
    fun createNotification(
        id: Long = 1L,
        title: String = "Test Notification",
        content: String = "This is a test notification",
        isRead: Boolean = false,
        createdAt: String = "2026-01-05T10:00:00Z"
    ): Notification = Notification(
        id = id,
        objectType = "notification",
        objectId = id,
        action = title,
        content = content,
        isRead = isRead,
        createdAt = createdAt
    )

    fun createNotificationList(count: Int = 5): List<Notification> =
        (1..count).map { i ->
            createNotification(
                id = i.toLong(),
                title = "Notification $i",
                isRead = i % 2 == 0
            )
        }

    // Pre-built scenarios
    val unreadNotification = createNotification(isRead = false)
    val readNotification = createNotification(isRead = true)
    val emptyNotifications = emptyList<Notification>()
}
```

---

## Usage in Tests

### ViewModel Test

```kotlin
class BeneficiaryViewModelTest {
    @Test
    fun `load success shows list`() = runTest {
        val testData = BeneficiaryFixtures.createBeneficiaryList(5)
        fakeRepository.setSuccessResponse(testData)

        viewModel.loadBeneficiaries()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertEquals(testData, (state.uiState as Success).data)
        }
    }

    @Test
    fun `empty list shows empty state`() = runTest {
        fakeRepository.setSuccessResponse(BeneficiaryFixtures.emptyList)

        viewModel.loadBeneficiaries()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue((state.uiState as Success).data.isEmpty())
        }
    }
}
```

### Screen Test

```kotlin
class BeneficiaryScreenTest {
    @Test
    fun successState_displaysItems() {
        val testData = BeneficiaryFixtures.createBeneficiaryList(3)

        composeTestRule.setContent {
            BeneficiaryContent(
                state = BeneficiaryState(
                    uiState = BeneficiaryUiState.Success(testData)
                ),
                onAction = {}
            )
        }

        testData.forEach { beneficiary ->
            composeTestRule
                .onNodeWithTag("beneficiary:item:${beneficiary.id}")
                .assertIsDisplayed()
        }
    }
}
```

---

## Commands

```bash
# Generate fixtures for feature
/implement [feature]           # Creates fixture file in Phase 5

# Check fixture status
/gap-analysis testing fixtures
```

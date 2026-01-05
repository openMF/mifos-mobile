# Fake Repositories Index - O(1) Lookup

> **17 repositories** | Test doubles for isolation | **Last Updated**: 2026-01-05

---

## Quick Lookup

| # | Feature | Repository | Fake Repository | Status |
|:-:|---------|------------|-----------------|:------:|
| 1 | auth | `UserAuthRepository` | `FakeUserAuthRepository` | ❌ |
| 2 | home | `HomeRepository` | `FakeHomeRepository` | ❌ |
| 3 | accounts | `AccountsRepository` | `FakeAccountsRepository` | ❌ |
| 4 | savings-account | `SavingsAccountRepository` | `FakeSavingsAccountRepository` | ❌ |
| 5 | loan-account | `LoanRepository` | `FakeLoanRepository` | ❌ |
| 6 | share-account | `ShareAccountRepository` | `FakeShareAccountRepository` | ❌ |
| 7 | beneficiary | `BeneficiaryRepository` | `FakeBeneficiaryRepository` | ❌ |
| 8 | transfer | `TransferRepository` | `FakeTransferRepository` | ❌ |
| 9 | recent-transaction | `RecentTransactionRepository` | `FakeRecentTransactionRepository` | ❌ |
| 10 | notification | `NotificationRepository` | `FakeNotificationRepository` | ❌ |
| 11 | settings | `UserPreferencesRepository` | `FakeUserPreferencesRepository` | ❌ |
| 12 | guarantor | `GuarantorRepository` | `FakeGuarantorRepository` | ❌ |
| 13 | qr | - | - | N/A |
| 14 | location | - | - | N/A |
| 15 | client-charge | `ClientChargeRepository` | `FakeClientChargeRepository` | ❌ |
| 16 | passcode | - | - | N/A |
| 17 | dashboard | - | - | N/A |

---

## O(1) Path Pattern

```
core/testing/src/commonMain/kotlin/org/mifos/mobile/core/testing/fake/Fake${Feature}Repository.kt
```

---

## Fake Repository Pattern

### Standard Structure

```kotlin
class Fake${Feature}Repository : ${Feature}Repository {
    // ═══════════════════════════════════════════════════════════════
    // CALL TRACKING
    // ═══════════════════════════════════════════════════════════════
    var loadCallCount = 0
        private set
    var createCallCount = 0
        private set
    var updateCallCount = 0
        private set
    var deleteCallCount = 0
        private set

    // ═══════════════════════════════════════════════════════════════
    // CONFIGURABLE RESPONSES
    // ═══════════════════════════════════════════════════════════════
    private var loadResponse: DataState<List<${Model}>> = DataState.Loading
    private var createResponse: DataState<${Model}> = DataState.Loading
    private var deleteResponse: DataState<Unit> = DataState.Loading

    // ═══════════════════════════════════════════════════════════════
    // SETUP METHODS (for test configuration)
    // ═══════════════════════════════════════════════════════════════
    fun setLoadSuccessResponse(data: List<${Model}>) {
        loadResponse = DataState.Success(data)
    }

    fun setLoadErrorResponse(message: String) {
        loadResponse = DataState.Error(message)
    }

    fun setLoadEmptyResponse() {
        loadResponse = DataState.Success(emptyList())
    }

    fun setCreateSuccessResponse(data: ${Model}) {
        createResponse = DataState.Success(data)
    }

    fun setCreateErrorResponse(message: String) {
        createResponse = DataState.Error(message)
    }

    fun setDeleteSuccessResponse() {
        deleteResponse = DataState.Success(Unit)
    }

    fun setDeleteErrorResponse(message: String) {
        deleteResponse = DataState.Error(message)
    }

    // ═══════════════════════════════════════════════════════════════
    // REPOSITORY IMPLEMENTATION
    // ═══════════════════════════════════════════════════════════════
    override fun get${Feature}s(): Flow<DataState<List<${Model}>>> = flow {
        loadCallCount++
        emit(loadResponse)
    }

    override fun create${Model}(payload: ${Model}Payload): Flow<DataState<${Model}>> = flow {
        createCallCount++
        emit(createResponse)
    }

    override fun delete${Model}(id: Long): Flow<DataState<Unit>> = flow {
        deleteCallCount++
        emit(deleteResponse)
    }

    // ═══════════════════════════════════════════════════════════════
    // RESET (for test isolation)
    // ═══════════════════════════════════════════════════════════════
    fun reset() {
        loadCallCount = 0
        createCallCount = 0
        updateCallCount = 0
        deleteCallCount = 0
        loadResponse = DataState.Loading
        createResponse = DataState.Loading
        deleteResponse = DataState.Loading
    }
}
```

---

## Feature Fake Repositories

### 1. FakeUserAuthRepository

```kotlin
class FakeUserAuthRepository : UserAuthRepository {
    var loginCallCount = 0
    var registerCallCount = 0
    var verifyOtpCallCount = 0

    private var loginResponse: DataState<User> = DataState.Loading
    private var registerResponse: DataState<RegisterPayload> = DataState.Loading
    private var otpResponse: DataState<Unit> = DataState.Loading

    fun setLoginSuccess(user: User = AuthFixtures.validUser) {
        loginResponse = DataState.Success(user)
    }

    fun setLoginError(message: String = "Invalid credentials") {
        loginResponse = DataState.Error(message)
    }

    fun setRegisterSuccess() {
        registerResponse = DataState.Success(RegisterPayload())
    }

    fun setOtpSuccess() {
        otpResponse = DataState.Success(Unit)
    }

    override fun login(payload: LoginPayload): Flow<DataState<User>> = flow {
        loginCallCount++
        emit(loginResponse)
    }

    override fun register(payload: RegisterPayload): Flow<DataState<RegisterPayload>> = flow {
        registerCallCount++
        emit(registerResponse)
    }

    override fun verifyOtp(otp: String): Flow<DataState<Unit>> = flow {
        verifyOtpCallCount++
        emit(otpResponse)
    }

    fun reset() {
        loginCallCount = 0
        registerCallCount = 0
        verifyOtpCallCount = 0
        loginResponse = DataState.Loading
        registerResponse = DataState.Loading
        otpResponse = DataState.Loading
    }
}
```

### 2. FakeHomeRepository

```kotlin
class FakeHomeRepository : HomeRepository {
    var loadAccountsCallCount = 0
    var loadUserCallCount = 0

    private var accountsResponse: DataState<ClientAccounts> = DataState.Loading
    private var userResponse: DataState<Client> = DataState.Loading

    fun setAccountsSuccess(accounts: ClientAccounts = AccountFixtures.fullPortfolio) {
        accountsResponse = DataState.Success(accounts)
    }

    fun setAccountsError(message: String = "Failed to load accounts") {
        accountsResponse = DataState.Error(message)
    }

    fun setUserSuccess(client: Client) {
        userResponse = DataState.Success(client)
    }

    override fun getClientAccounts(): Flow<DataState<ClientAccounts>> = flow {
        loadAccountsCallCount++
        emit(accountsResponse)
    }

    override fun getCurrentUser(): Flow<DataState<Client>> = flow {
        loadUserCallCount++
        emit(userResponse)
    }

    fun reset() {
        loadAccountsCallCount = 0
        loadUserCallCount = 0
        accountsResponse = DataState.Loading
        userResponse = DataState.Loading
    }
}
```

### 3. FakeBeneficiaryRepository

```kotlin
class FakeBeneficiaryRepository : BeneficiaryRepository {
    var loadCallCount = 0
    var createCallCount = 0
    var updateCallCount = 0
    var deleteCallCount = 0

    private var loadResponse: DataState<List<Beneficiary>> = DataState.Loading
    private var createResponse: DataState<Beneficiary> = DataState.Loading
    private var updateResponse: DataState<Beneficiary> = DataState.Loading
    private var deleteResponse: DataState<Unit> = DataState.Loading

    fun setLoadSuccess(data: List<Beneficiary> = BeneficiaryFixtures.multipleBeneficiaries) {
        loadResponse = DataState.Success(data)
    }

    fun setLoadError(message: String = "Failed to load beneficiaries") {
        loadResponse = DataState.Error(message)
    }

    fun setLoadEmpty() {
        loadResponse = DataState.Success(emptyList())
    }

    fun setCreateSuccess(beneficiary: Beneficiary = BeneficiaryFixtures.createBeneficiary()) {
        createResponse = DataState.Success(beneficiary)
    }

    fun setCreateError(message: String = "Failed to create beneficiary") {
        createResponse = DataState.Error(message)
    }

    fun setDeleteSuccess() {
        deleteResponse = DataState.Success(Unit)
    }

    fun setDeleteError(message: String = "Failed to delete beneficiary") {
        deleteResponse = DataState.Error(message)
    }

    override fun getBeneficiaries(): Flow<DataState<List<Beneficiary>>> = flow {
        loadCallCount++
        emit(loadResponse)
    }

    override fun createBeneficiary(payload: BeneficiaryPayload): Flow<DataState<Beneficiary>> = flow {
        createCallCount++
        emit(createResponse)
    }

    override fun updateBeneficiary(id: Long, payload: BeneficiaryPayload): Flow<DataState<Beneficiary>> = flow {
        updateCallCount++
        emit(updateResponse)
    }

    override fun deleteBeneficiary(id: Long): Flow<DataState<Unit>> = flow {
        deleteCallCount++
        emit(deleteResponse)
    }

    fun reset() {
        loadCallCount = 0
        createCallCount = 0
        updateCallCount = 0
        deleteCallCount = 0
        loadResponse = DataState.Loading
        createResponse = DataState.Loading
        updateResponse = DataState.Loading
        deleteResponse = DataState.Loading
    }
}
```

### 4. FakeTransferRepository

```kotlin
class FakeTransferRepository : TransferRepository {
    var loadTemplateCallCount = 0
    var makeTransferCallCount = 0

    private var templateResponse: DataState<TransferTemplate> = DataState.Loading
    private var transferResponse: DataState<Unit> = DataState.Loading

    fun setTemplateSuccess(template: TransferTemplate = TransferFixtures.createTransferTemplate()) {
        templateResponse = DataState.Success(template)
    }

    fun setTemplateError(message: String = "Failed to load transfer template") {
        templateResponse = DataState.Error(message)
    }

    fun setTransferSuccess() {
        transferResponse = DataState.Success(Unit)
    }

    fun setTransferError(message: String = "Transfer failed") {
        transferResponse = DataState.Error(message)
    }

    override fun getTransferTemplate(): Flow<DataState<TransferTemplate>> = flow {
        loadTemplateCallCount++
        emit(templateResponse)
    }

    override fun makeTransfer(payload: TransferPayload): Flow<DataState<Unit>> = flow {
        makeTransferCallCount++
        emit(transferResponse)
    }

    fun reset() {
        loadTemplateCallCount = 0
        makeTransferCallCount = 0
        templateResponse = DataState.Loading
        transferResponse = DataState.Loading
    }
}
```

### 5. FakeNotificationRepository

```kotlin
class FakeNotificationRepository : NotificationRepository {
    var loadCallCount = 0
    var markReadCallCount = 0

    private var loadResponse: DataState<List<Notification>> = DataState.Loading
    private var markReadResponse: DataState<Unit> = DataState.Loading

    fun setLoadSuccess(data: List<Notification> = NotificationFixtures.createNotificationList()) {
        loadResponse = DataState.Success(data)
    }

    fun setLoadError(message: String = "Failed to load notifications") {
        loadResponse = DataState.Error(message)
    }

    fun setLoadEmpty() {
        loadResponse = DataState.Success(emptyList())
    }

    fun setMarkReadSuccess() {
        markReadResponse = DataState.Success(Unit)
    }

    override fun getNotifications(): Flow<DataState<List<Notification>>> = flow {
        loadCallCount++
        emit(loadResponse)
    }

    override fun markAsRead(id: Long): Flow<DataState<Unit>> = flow {
        markReadCallCount++
        emit(markReadResponse)
    }

    fun reset() {
        loadCallCount = 0
        markReadCallCount = 0
        loadResponse = DataState.Loading
        markReadResponse = DataState.Loading
    }
}
```

### 6. FakeUserPreferencesRepository

```kotlin
class FakeUserPreferencesRepository : UserPreferencesRepository {
    private var _theme: String = "system"
    private var _language: String = "en"
    private var _passcodeEnabled: Boolean = false

    val themeFlow = MutableStateFlow(_theme)
    val languageFlow = MutableStateFlow(_language)
    val passcodeEnabledFlow = MutableStateFlow(_passcodeEnabled)

    fun setTheme(theme: String) {
        _theme = theme
        themeFlow.value = theme
    }

    fun setLanguage(language: String) {
        _language = language
        languageFlow.value = language
    }

    fun setPasscodeEnabled(enabled: Boolean) {
        _passcodeEnabled = enabled
        passcodeEnabledFlow.value = enabled
    }

    override fun getTheme(): Flow<String> = themeFlow
    override fun getLanguage(): Flow<String> = languageFlow
    override fun isPasscodeEnabled(): Flow<Boolean> = passcodeEnabledFlow

    override suspend fun updateTheme(theme: String) {
        setTheme(theme)
    }

    override suspend fun updateLanguage(language: String) {
        setLanguage(language)
    }

    fun reset() {
        _theme = "system"
        _language = "en"
        _passcodeEnabled = false
        themeFlow.value = _theme
        languageFlow.value = _language
        passcodeEnabledFlow.value = _passcodeEnabled
    }
}
```

---

## Usage in Tests

### Basic Usage

```kotlin
class BeneficiaryViewModelTest {
    private lateinit var fakeRepository: FakeBeneficiaryRepository
    private lateinit var viewModel: BeneficiaryViewModel

    @BeforeTest
    fun setup() {
        fakeRepository = FakeBeneficiaryRepository()
        viewModel = BeneficiaryViewModel(repository = fakeRepository)
    }

    @AfterTest
    fun teardown() {
        fakeRepository.reset()  // Ensure test isolation
    }

    @Test
    fun `load success shows data`() = runTest {
        fakeRepository.setLoadSuccess()

        viewModel.loadBeneficiaries()

        viewModel.stateFlow.test {
            val state = expectMostRecentItem()
            assertTrue(state.uiState is Success)
        }
    }

    @Test
    fun `delete calls repository`() = runTest {
        fakeRepository.setDeleteSuccess()

        viewModel.deleteBeneficiary(1L)

        assertEquals(1, fakeRepository.deleteCallCount)
    }
}
```

### Verifying Calls

```kotlin
@Test
fun `refresh reloads data`() = runTest {
    fakeRepository.setLoadSuccess()

    viewModel.loadBeneficiaries()
    viewModel.trySendAction(Action.Refresh)

    assertEquals(2, fakeRepository.loadCallCount)
}
```

---

## Commands

```bash
# Generate fake repository for feature
/implement [feature]           # Creates fake in Phase 5

# Check fake repository status
/gap-analysis testing fakes
```

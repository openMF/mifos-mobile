# Savings Account - API Reference

---

## Endpoints Required

### 1. Get Savings Account with Associations

**Endpoint**: `GET /self/savingsaccounts/{accountId}`

**Purpose**: Fetch savings account details with optional associations (transactions, charges)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Query Parameters:
  associations: string (optional) - "transactions" or "charges"
```

**Response**:
```json
{
    "id": 12345,
    "accountNo": "000000012345",
    "depositType": {
        "id": 100,
        "code": "depositAccountType.savingsDeposit",
        "value": "Savings"
    },
    "clientId": 1,
    "clientName": "John Doe",
    "savingsProductId": 1,
    "savingsProductName": "Basic Savings",
    "status": {
        "id": 300,
        "code": "savingsAccountStatusType.active",
        "value": "Active",
        "submittedAndPendingApproval": false,
        "approved": true,
        "rejected": false,
        "withdrawnByApplicant": false,
        "active": true,
        "closed": false
    },
    "timeline": {
        "submittedOnDate": [2025, 12, 15],
        "approvedOnDate": [2025, 12, 16],
        "activatedOnDate": [2025, 12, 16]
    },
    "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$",
        "nameCode": "currency.USD",
        "displayLabel": "US Dollar ($)"
    },
    "nominalAnnualInterestRate": 5.5,
    "summary": {
        "currency": { "code": "USD", "displaySymbol": "$", "decimalPlaces": 2 },
        "totalDeposits": 5000.00,
        "totalWithdrawals": 3765.44,
        "accountBalance": 1234.56
    },
    "transactions": [
        {
            "id": 1,
            "transactionType": { "id": 1, "code": "savingsAccountTransactionType.deposit", "value": "Deposit" },
            "accountId": 12345,
            "accountNo": "000000012345",
            "date": [2025, 12, 28],
            "currency": { "code": "USD", "displaySymbol": "$", "decimalPlaces": 2 },
            "amount": 100.00,
            "runningBalance": 1234.56,
            "submittedOnDate": [2025, 12, 28]
        }
    ]
}
```

**Kotlin DTO**: `SavingsWithAssociations`

**Status**: Implemented in `SavingAccountsListService`

---

### 2. Get Savings Account Template

**Endpoint**: `GET /self/savingsaccounts/template`

**Purpose**: Fetch available savings products for new account or update

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Query Parameters:
  clientId: Long (required) - Client ID
  productId: Long (optional) - Specific product ID for details
```

**Response**:
```json
{
    "clientId": 1,
    "clientName": "John Doe",
    "productOptions": [
        {
            "id": 1,
            "name": "Basic Savings",
            "withdrawalFeeForTransfers": false,
            "allowOverdraft": false,
            "enforceMinRequiredBalance": false,
            "withHoldTax": false
        },
        {
            "id": 2,
            "name": "Premium Savings",
            "withdrawalFeeForTransfers": true,
            "allowOverdraft": true,
            "enforceMinRequiredBalance": true,
            "withHoldTax": false
        }
    ],
    "chargeOptions": [],
    "interestCompoundingPeriodTypeOptions": [],
    "interestPostingPeriodTypeOptions": [],
    "interestCalculationTypeOptions": [],
    "interestCalculationDaysInYearTypeOptions": [],
    "lockinPeriodFrequencyTypeOptions": [],
    "withdrawalFeeTypeOptions": []
}
```

**Kotlin DTO**: `SavingsAccountTemplate`

**Status**: Implemented in `SavingAccountsListService`

---

### 3. Update Savings Account

**Endpoint**: `PUT /self/savingsaccounts/{accountsId}`

**Purpose**: Update savings account product (for pending accounts)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json

Body:
{
    "clientId": 1,
    "productId": 2
}
```

**Response**:
```json
{
    "officeId": 1,
    "clientId": 1,
    "savingsId": 12345,
    "resourceId": 12345,
    "changes": {
        "productId": 2
    }
}
```

**Kotlin DTO (Request)**: `SavingsAccountUpdatePayload`

**Status**: Implemented in `SavingAccountsListService`

---

### 4. Withdraw Savings Account Application

**Endpoint**: `POST /self/savingsaccounts/{savingsId}?command=withdrawnByApplicant`

**Purpose**: Withdraw a pending savings account application

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json

Body:
{
    "locale": "en",
    "dateFormat": "dd MMMM yyyy",
    "withdrawnOnDate": "29 December 2025",
    "note": "User requested withdrawal"
}
```

**Response**:
```json
{
    "officeId": 1,
    "clientId": 1,
    "savingsId": 12345,
    "resourceId": 12345,
    "changes": {
        "status": {
            "id": 400,
            "code": "savingsAccountStatusType.withdrawn.by.applicant",
            "value": "Withdrawn by applicant"
        }
    }
}
```

**Kotlin DTO (Request)**: `SavingsAccountWithdrawPayload`

**Status**: Implemented in `SavingAccountsListService`

---

### 5. Get Transaction Details

**Endpoint**: `GET /self/savingsaccounts/{accountId}/transactions/{transactionId}`

**Purpose**: Fetch detailed information about a specific transaction

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
{
    "id": 1,
    "transactionType": {
        "id": 1,
        "code": "savingsAccountTransactionType.deposit",
        "value": "Deposit",
        "deposit": true,
        "withdrawal": false,
        "interestPosting": false,
        "feeDeduction": false
    },
    "accountId": 12345,
    "accountNo": "000000012345",
    "date": [2025, 12, 28],
    "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$"
    },
    "amount": 100.00,
    "runningBalance": 1234.56,
    "reversed": false,
    "submittedOnDate": [2025, 12, 28],
    "interestedPostedAsOn": false
}
```

**Kotlin DTO**: `TransactionDetails`

**Status**: Implemented in `SavingAccountsListService`

---

### 6. Get Account Transfer Template

**Endpoint**: `GET /self/accounttransfers/template`

**Purpose**: Fetch available accounts for transfer (source and destination)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Query Parameters:
  fromAccountId: Long (optional) - Source account ID
  fromAccountType: Long (optional) - Account type ID (2 = Savings)
```

**Response**:
```json
{
    "fromAccountTypeOptions": [
        { "id": 1, "code": "accountType.loan", "value": "Loan Account" },
        { "id": 2, "code": "accountType.savings", "value": "Savings Account" }
    ],
    "toAccountTypeOptions": [
        { "id": 1, "code": "accountType.loan", "value": "Loan Account" },
        { "id": 2, "code": "accountType.savings", "value": "Savings Account" }
    ],
    "fromAccountOptions": [
        {
            "accountId": 12345,
            "accountNo": "000000012345",
            "accountType": { "id": 2, "value": "Savings Account" },
            "clientId": 1,
            "clientName": "John Doe",
            "officeId": 1,
            "officeName": "Head Office"
        }
    ],
    "toAccountOptions": []
}
```

**Kotlin DTO**: `AccountOptionsTemplate`

**Status**: Implemented in `SavingAccountsListService`

---

### 7. Make Account Transfer

**Endpoint**: `POST /self/accounttransfers`

**Purpose**: Transfer funds between accounts

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json

Body:
{
    "fromOfficeId": 1,
    "fromClientId": 1,
    "fromAccountType": 2,
    "fromAccountId": 12345,
    "toOfficeId": 1,
    "toClientId": 2,
    "toAccountType": 2,
    "toAccountId": 12346,
    "transferDate": "29 December 2025",
    "transferAmount": 100.00,
    "transferDescription": "Transfer to beneficiary",
    "dateFormat": "dd MMMM yyyy",
    "locale": "en"
}
```

**Response**:
```json
{
    "savingsId": 12345,
    "resourceId": 1,
    "changes": {
        "transferAmount": 100.00
    }
}
```

**Kotlin DTO (Request)**: `TransferPayload`

**Status**: Implemented in `SavingAccountsListService`

---

## API Summary

| Endpoint | Method | Service | Repository | Status |
|----------|--------|---------|------------|--------|
| `/savingsaccounts/{id}` | GET | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/savingsaccounts/template` | GET | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/savingsaccounts/{id}` | PUT | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/savingsaccounts/{id}?command=withdrawnByApplicant` | POST | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/savingsaccounts/{id}/transactions/{txnId}` | GET | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/accounttransfers/template` | GET | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/accounttransfers` | POST | SavingAccountsListService | SavingsAccountRepository | Implemented |

---

## Kotlin Implementation

### Service Interface (SavingAccountsListService.kt)

```kotlin
interface SavingAccountsListService {
    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    fun getSavingsWithAssociations(
        @Path("accountId") accountId: Long,
        @Query("associations") associationType: String?,
    ): Flow<SavingsWithAssociations>

    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template")
    fun accountTransferTemplate(
        @Query("fromAccountId") accountId: Long?,
        @Query("fromAccountType") accountType: Long?,
    ): Flow<AccountOptionsTemplate>

    @POST(ApiEndPoints.ACCOUNT_TRANSFER)
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): HttpResponse

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/template")
    fun getSavingsAccountApplicationTemplate(
        @Query("clientId") clientId: Long?,
    ): Flow<SavingsAccountTemplate>

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/template")
    fun getSavingsAccountApplicationTemplateByProduct(
        @Query("clientId") clientId: Long?,
        @Query("productId") productId: Long?,
    ): Flow<SavingsAccountTemplate>

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS)
    suspend fun submitSavingAccountApplication(
        @Body payload: SavingsAccountApplicationPayload?,
    ): HttpResponse

    @PUT(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountsId}")
    suspend fun updateSavingsAccountUpdate(
        @Path("accountsId") accountsId: Long,
        @Body payload: SavingsAccountUpdatePayload?,
    ): HttpResponse

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}?command=withdrawnByApplicant")
    suspend fun submitWithdrawSavingsAccount(
        @Path("savingsId") savingsId: Long,
        @Body payload: SavingsAccountWithdrawPayload?,
    ): HttpResponse

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}/transactions/{transactionId}")
    fun getSavingsAccountTransactionDetails(
        @Path("accountId") savingsId: Long,
        @Path("transactionId") transactionId: Long,
    ): Flow<TransactionDetails>
}
```

### Repository Interface (SavingsAccountRepository.kt)

```kotlin
interface SavingsAccountRepository {

    fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Flow<DataState<SavingsWithAssociations>>

    fun getSavingsAccountTransactionDetails(
        accountId: Long,
        transactionId: Long,
    ): Flow<DataState<TransactionDetails>>

    fun getSavingAccountApplicationTemplate(
        clientId: Long?
    ): Flow<DataState<SavingsAccountTemplate>>

    fun getSavingAccountApplicationTemplateByProduct(
        clientId: Long?,
        productId: Long?,
    ): Flow<DataState<SavingsAccountTemplate>>

    suspend fun submitSavingAccountApplication(
        payload: SavingsAccountApplicationPayload?
    ): DataState<String>

    suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?,
    ): DataState<String>

    suspend fun submitWithdrawSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountWithdrawPayload?,
    ): DataState<String>

    fun accountTransferTemplate(
        accountId: Long?,
        accountType: Long?
    ): Flow<DataState<AccountOptionsTemplate>>
}
```

---

## Kotlin DTOs

### SavingAccount.kt

```kotlin
@Serializable
@Parcelize
data class SavingAccount(
    val id: Long = 0,
    val accountNo: String? = null,
    val productName: String? = null,
    val productId: Int? = null,
    val overdraftLimit: Long = 0,
    val minRequiredBalance: Long = 0,
    val accountBalance: Double = 0.0,
    val totalDeposits: Double = 0.0,
    val savingsProductName: String? = null,
    val clientName: String? = null,
    val savingsProductId: String? = null,
    val nominalAnnualInterestRate: Double = 0.0,
    val status: Status? = null,
    val currency: Currency? = null,
    val depositType: DepositType? = null,
    val lastActiveTransactionDate: List<Int>? = null,
    val timeLine: TimeLine? = null,
) : Parcelable
```

### SavingsWithAssociations.kt

```kotlin
@Serializable
@Parcelize
data class SavingsWithAssociations(
    val id: Long? = null,
    val accountNo: String? = null,
    val depositType: DepositType? = null,
    val externalId: String? = null,
    val clientId: Int? = null,
    val clientName: String? = null,
    val savingsProductId: Int? = null,
    val savingsProductName: String? = null,
    val fieldOfficerId: Int? = null,
    val status: Status? = null,
    val timeline: TimeLine? = null,
    val currency: Currency? = null,
    internal val nominalAnnualInterestRate: Double? = null,
    val minRequiredOpeningBalance: Double? = null,
    val lockinPeriodFrequency: Double? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,
    val lastActiveTransactionDate: List<Int>? = null,
    val dormancyTrackingActive: Boolean? = null,
    val summary: Summary? = null,
    val transactions: List<Transactions> = emptyList(),
) : Parcelable
```

### SavingsAccountUpdatePayload.kt

```kotlin
@Serializable
data class SavingsAccountUpdatePayload(
    val clientId: Long? = 0,
    val productId: Long? = 0,
)
```

### SavingsAccountWithdrawPayload.kt

```kotlin
@Serializable
@Parcelize
data class SavingsAccountWithdrawPayload(
    val locale: String? = null,
    val dateFormat: String? = null,
    val withdrawnOnDate: String? = null,
    val note: String? = null,
) : Parcelable
```

### SavingsAccountTemplate.kt

```kotlin
@Serializable
@Parcelize
data class SavingsAccountTemplate(
    val clientId: Int = 0,
    val clientName: String? = null,
    val savingsProductId: Int? = null,
    val savingsProductName: String? = null,
    val timeline: Timeline? = null,
    val currency: Currency? = null,
    val nominalAnnualInterestRate: Double? = null,
    val interestCompoundingPeriodType: ProductOptions? = null,
    val interestPostingPeriodType: ProductOptions? = null,
    val interestCalculationType: ProductOptions? = null,
    val interestCalculationDaysInYearType: ProductOptions? = null,
    val minRequiredOpeningBalance: Double? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,
    val isDormancyTrackingActive: Boolean? = null,
    val charges: List<ChargeOptions>? = null,
    val productOptions: ArrayList<SavingsProduct> = arrayListOf(),
    val fieldOfficerOptions: List<FieldOfficerOptions> = emptyList(),
    val interestCompoundingPeriodTypeOptions: List<SavingsOptions> = emptyList(),
    val interestPostingPeriodTypeOptions: List<SavingsOptions> = emptyList(),
    val interestCalculationTypeOptions: List<SavingsOptions> = emptyList(),
    val interestCalculationDaysInYearTypeOptions: List<SavingsOptions> = emptyList(),
    val lockinPeriodFrequencyTypeOptions: List<SavingsOptions> = emptyList(),
    val withdrawalFeeTypeOptions: List<SavingsOptions> = emptyList(),
    val chargeOptions: ArrayList<ChargeOptions> = arrayListOf(),
) : Parcelable
```

### SavingStatus.kt

```kotlin
@Serializable
enum class SavingStatus(val status: String) {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    CLOSED("Closed"),
    SUBMIT_AND_PENDING_APPROVAL("Submitted and pending approval"),
    ;

    companion object {
        fun fromStatus(status: String): SavingStatus {
            return entries.find { it.status.equals(status, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid status: $status")
        }
    }
}
```

---

## Error Handling

The repository implementation handles the following error scenarios:

```kotlin
class SavingsAccountRepositoryImp(...) : SavingsAccountRepository {

    override suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.savingAccountsListApi
                    .updateSavingsAccountUpdate(accountId!!, payload)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(
                    Exception("Network error: ${e.message ?: "Please check your connection"}"),
                    null
                )
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }
}
```

---

## Notes

- All endpoints are prefixed with `/self/` indicating self-service (client-facing) APIs
- The `associations` query parameter can be `transactions` or `charges` to include related data
- Update operations require authentication (passcode verification) before submission
- Withdraw operation marks the account as "Withdrawn by applicant" status
- Currency formatting is done client-side using `CurrencyFormatter` utility
- Date formatting uses `DateHelper` with format "dd MMMM yyyy"

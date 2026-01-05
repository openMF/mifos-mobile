# Transfer - API Reference

> **Base URL**: `https://tt.mifos.community/fineract-provider/api/v1/self/`
> **Authentication**: Basic Auth with `Fineract-Platform-TenantId` header
> **Last Updated**: 2025-12-29

---

## Authentication Headers

All endpoints require:
```
Headers:
  Authorization: Basic {base64EncodedAuthenticationKey}
  Fineract-Platform-TenantId: {tenantId}
  Content-Type: application/json
```

---

## 1. Self-Transfer Template

### GET /self/accounttransfers/template

**Purpose**: Fetch account options for self-transfers between user's own accounts

**Service**: `SavingAccountsListService`

**Request**:
```
GET /self/accounttransfers/template?fromAccountId=1001&fromAccountType=2
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| fromAccountId | Long | No | Pre-select source account ID |
| fromAccountType | Long | No | Account type: 1=Loan, 2=Savings |

**Response**:
```json
{
  "fromAccountOptions": [
    {
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "accountType": {
        "id": 2,
        "code": "accountType.savings",
        "value": "Savings Account"
      },
      "clientId": 123,
      "clientName": "John Doe",
      "officeId": 1,
      "officeName": "Head Office"
    },
    {
      "accountId": 1002,
      "accountNo": "SA-0009876543",
      "accountType": {
        "id": 2,
        "code": "accountType.savings",
        "value": "Savings Account"
      },
      "clientId": 123,
      "clientName": "John Doe",
      "officeId": 1,
      "officeName": "Head Office"
    }
  ],
  "toAccountOptions": [
    {
      "accountId": 2001,
      "accountNo": "LA-0005678901",
      "accountType": {
        "id": 1,
        "code": "accountType.loan",
        "value": "Loan Account"
      },
      "clientId": 123,
      "clientName": "John Doe",
      "officeId": 1,
      "officeName": "Head Office"
    },
    {
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "accountType": {
        "id": 2,
        "code": "accountType.savings",
        "value": "Savings Account"
      },
      "clientId": 123,
      "clientName": "John Doe",
      "officeId": 1,
      "officeName": "Head Office"
    }
  ]
}
```

**Kotlin Service**:
```kotlin
interface SavingAccountsListService {
    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template")
    fun accountTransferTemplate(
        @Query("fromAccountId") accountId: Long?,
        @Query("fromAccountType") accountType: Long?,
    ): Flow<AccountOptionsTemplate>
}
```

**Status**: Implemented in `SavingAccountsListService.kt`

---

## 2. Third-Party Transfer Template

### GET /self/accounttransfers/template?type=tpt

**Purpose**: Fetch account options for third-party transfers to beneficiaries

**Service**: `ThirdPartyTransferService`

**Request**:
```
GET /self/accounttransfers/template?type=tpt
```

**Response**:
```json
{
  "fromAccountOptions": [
    {
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "accountType": {
        "id": 2,
        "code": "accountType.savings",
        "value": "Savings Account"
      },
      "clientId": 123,
      "clientName": "John Doe",
      "officeId": 1,
      "officeName": "Head Office"
    }
  ],
  "toAccountOptions": [
    {
      "accountId": 5001,
      "accountNo": "SA-0007654321",
      "accountType": {
        "id": 2,
        "code": "accountType.savings",
        "value": "Savings Account"
      },
      "clientId": 456,
      "clientName": "Jane Doe",
      "officeId": 1,
      "officeName": "Head Office"
    }
  ]
}
```

**Kotlin Service**:
```kotlin
interface ThirdPartyTransferService {
    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    fun accountTransferTemplate(): Flow<AccountOptionsTemplate>
}
```

**Status**: Implemented in `ThirdPartyTransferService.kt`

---

## 3. Execute Self-Transfer

### POST /self/accounttransfers

**Purpose**: Execute a fund transfer between user's own accounts

**Service**: `SavingAccountsListService`

**Request**:
```json
POST /self/accounttransfers

{
  "fromOfficeId": 1,
  "fromClientId": 123,
  "fromAccountType": 2,
  "fromAccountId": "SA-0001234567",
  "toOfficeId": 1,
  "toClientId": 123,
  "toAccountType": 1,
  "toAccountId": "LA-0005678901",
  "dateFormat": "dd MMMM yyyy",
  "locale": "en",
  "transferDate": "29 December 2025",
  "transferAmount": 500.00,
  "transferDescription": "Loan repayment"
}
```

**Request Fields**:
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| fromOfficeId | Int | Yes | Source account office ID |
| fromClientId | Long | Yes | Source account client ID |
| fromAccountType | Int | Yes | 1=Loan, 2=Savings |
| fromAccountId | String | Yes | Source account number |
| toOfficeId | Int | Yes | Destination account office ID |
| toClientId | Long | Yes | Destination account client ID |
| toAccountType | Int | Yes | 1=Loan, 2=Savings |
| toAccountId | String | Yes | Destination account number |
| dateFormat | String | Yes | Date format pattern |
| locale | String | Yes | Locale code (e.g., "en") |
| transferDate | String | Yes | Transfer date in specified format |
| transferAmount | Double | Yes | Amount to transfer |
| transferDescription | String | No | Optional remarks/description |

**Response (Success)**:
```json
{
  "savingsId": 1001,
  "resourceId": 50001
}
```

**Response Fields**:
| Field | Type | Description |
|-------|------|-------------|
| savingsId | Int | Source savings account ID |
| resourceId | Int | Created transfer resource ID |

**Kotlin Service**:
```kotlin
interface SavingAccountsListService {
    @POST(ApiEndPoints.ACCOUNT_TRANSFER)
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): HttpResponse
}
```

**Status**: Implemented in `SavingAccountsListService.kt`

---

## 4. Execute Third-Party Transfer

### POST /self/accounttransfers?type=tpt

**Purpose**: Execute a fund transfer to a third-party beneficiary account

**Service**: `ThirdPartyTransferService`

**Request**:
```json
POST /self/accounttransfers?type=tpt

{
  "fromOfficeId": 1,
  "fromClientId": 123,
  "fromAccountType": 2,
  "fromAccountId": "SA-0001234567",
  "toOfficeId": 1,
  "toClientId": 456,
  "toAccountType": 2,
  "toAccountId": "SA-0007654321",
  "dateFormat": "dd MMMM yyyy",
  "locale": "en",
  "transferDate": "29 December 2025",
  "transferAmount": 1000.00,
  "transferDescription": "Monthly support"
}
```

**Response**: Same as self-transfer response

**Kotlin Service**:
```kotlin
interface ThirdPartyTransferService {
    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=tpt")
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): HttpResponse
}
```

**Status**: Implemented in `ThirdPartyTransferService.kt`

---

## 5. Kotlin DTOs

### 5.1 AccountOptionsTemplate
```kotlin
@Serializable
@Parcelize
data class AccountOptionsTemplate(
    val fromAccountOptions: List<AccountOption> = emptyList(),
    val toAccountOptions: List<AccountOption> = emptyList(),
) : Parcelable
```

### 5.2 AccountOption
```kotlin
@Serializable
@Parcelize
data class AccountOption(
    val accountId: Int? = null,
    val accountNo: String? = null,
    val accountType: AccountType? = null,
    val clientId: Long? = null,
    val clientName: String? = null,
    val officeId: Int? = null,
    val officeName: String? = null,
) : Parcelable
```

### 5.3 AccountType
```kotlin
@Serializable
@Parcelize
data class AccountType(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
) : Parcelable
```

### 5.4 TransferPayload
```kotlin
@Serializable
@Parcelize
data class TransferPayload(
    val fromOfficeId: Int? = null,
    val fromClientId: Long? = null,
    val fromAccountType: Int? = null,
    val fromAccountId: String? = null,
    val toOfficeId: Int? = null,
    val toClientId: Long? = null,
    val toAccountType: Int? = null,
    val toAccountId: String? = null,
    val transferDate: String? = null,
    val transferAmount: Double? = null,
    val transferDescription: String? = null,
    val dateFormat: String? = null,
    val locale: String? = null,
) : Parcelable
```

### 5.5 TransferResponse
```kotlin
@Serializable
data class TransferResponse(
    val savingsId: Int? = null,
    val resourceId: Int? = null,
)
```

### 5.6 ReviewTransferPayload
```kotlin
@Serializable
data class ReviewTransferPayload(
    val payToAccount: AccountOption? = null,
    val payFromAccount: AccountOption? = null,
    val amount: String = "",
    val review: String = "",
)
```

### 5.7 TransferType
```kotlin
enum class TransferType {
    TPT,
    SELF,
}
```

---

## 6. Repository Implementations

### 6.1 TransferRepository
```kotlin
interface TransferRepository {
    suspend fun makeTransfer(
        payload: TransferPayload,
        transferType: TransferType?,
    ): DataState<String>
}
```

### 6.2 TransferRepositoryImp
```kotlin
class TransferRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : TransferRepository {
    override suspend fun makeTransfer(
        payload: TransferPayload,
        transferType: TransferType?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = when (transferType) {
                    TransferType.SELF -> dataManager.savingAccountsListApi.makeTransfer(payload)
                    else -> dataManager.thirdPartyTransferApi.makeTransfer(payload)
                }
                val transferResponse = Json.decodeFromString<TransferResponse>(response.bodyAsText())
                DataState.Success(transferResponse.resourceId.toString())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }
}
```

### 6.3 ThirdPartyTransferRepository
```kotlin
interface ThirdPartyTransferRepository {
    fun thirdPartyTransferTemplate(): Flow<DataState<AccountOptionsTemplate>>
}
```

### 6.4 ThirdPartyTransferRepositoryImp
```kotlin
class ThirdPartyTransferRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : ThirdPartyTransferRepository {
    override fun thirdPartyTransferTemplate(): Flow<DataState<AccountOptionsTemplate>> {
        return dataManager.thirdPartyTransferApi.accountTransferTemplate()
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
```

---

## 7. API Endpoint Constants

```kotlin
object ApiEndPoints {
    const val ACCOUNT_TRANSFER = "accounttransfers"
    // Full paths:
    // Self-transfer template: accounttransfers/template
    // TPT template: accounttransfers/template?type=tpt
    // Self-transfer: accounttransfers
    // TPT transfer: accounttransfers?type=tpt
}
```

---

## 8. Error Responses

| Status Code | Error | Description | User Message |
|-------------|-------|-------------|--------------|
| 400 | Bad Request | Invalid payload | "Please check your input" |
| 401 | Unauthorized | Invalid token | "Please login again" |
| 403 | Forbidden | Insufficient permissions | "Access denied" |
| 404 | Not Found | Account not found | "Account not found" |
| 409 | Conflict | Insufficient balance | "Insufficient balance for transfer" |
| 500 | Server Error | Internal error | "Service unavailable" |

**Error Response Format**:
```json
{
  "developerMessage": "Detailed error for debugging",
  "httpStatusCode": "400",
  "defaultUserMessage": "User-friendly error message",
  "userMessageGlobalisationCode": "error.msg.code",
  "errors": [
    {
      "developerMessage": "Field-specific error",
      "defaultUserMessage": "Amount must be greater than 0",
      "userMessageGlobalisationCode": "validation.msg.amount.invalid",
      "parameterName": "transferAmount"
    }
  ]
}
```

---

## 9. API Summary Table

| Endpoint | Method | Service | Repository | Purpose |
|----------|--------|---------|------------|---------|
| /accounttransfers/template | GET | SavingAccountsListService | SavingsAccountRepository | Self-transfer options |
| /accounttransfers/template?type=tpt | GET | ThirdPartyTransferService | ThirdPartyTransferRepository | TPT options |
| /accounttransfers | POST | SavingAccountsListService | TransferRepository | Execute self-transfer |
| /accounttransfers?type=tpt | POST | ThirdPartyTransferService | TransferRepository | Execute TPT |

---

## 10. Account Type Values

| ID | Code | Value | Description |
|----|------|-------|-------------|
| 1 | accountType.loan | Loan Account | Loan account type |
| 2 | accountType.savings | Savings Account | Savings account type |

---

## 11. Date Formatting

**Required Format**: `dd MMMM yyyy`
**Example**: `29 December 2025`
**Locale**: `en`

```kotlin
val transferDate = listOf(
    currentDate.day,
    currentDate.month.number,
    currentDate.year,
)
val formattedDate = DateHelper.getDateMonthYearString(transferDate)
// Output: "29 December 2025"
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial API documentation based on implementation code |

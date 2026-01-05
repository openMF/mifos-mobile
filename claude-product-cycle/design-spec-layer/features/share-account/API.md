# Share Account - API Reference

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

## 1. Get Share Products

### GET /self/products/share

**Purpose**: Fetch available share products for a client

**Request**:
```
GET /self/products/share?clientId=123
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| clientId | Long | Optional | Client ID to filter available products |

**Response**:
```json
{
  "totalFilteredRecords": 2,
  "pageItems": [
    {
      "id": 1,
      "name": "Equity Shares",
      "shortName": "ES",
      "description": "Standard equity shares for cooperative members",
      "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$",
        "nameCode": "currency.USD",
        "displayLabel": "US Dollar ($)"
      },
      "totalShares": 10000,
      "totalSharesIssued": 5000,
      "unitPrice": 25.0,
      "shareCapital": 250000.0,
      "nominalShares": 100,
      "minimumShares": 10,
      "maximumShares": 500,
      "allowDividendCalculationForInactiveClients": false,
      "lockinPeriod": 12,
      "lockPeriodTypeEnum": {
        "id": 2,
        "code": "savings.lockin.period.months",
        "value": "Months"
      },
      "minimumActivePeriod": 6,
      "minimumActivePeriodForDividendsTypeEnum": {
        "id": 2,
        "code": "savings.lockin.period.months",
        "value": "Months"
      },
      "accountingRule": {
        "id": 2,
        "code": "accountingRuleType.cash",
        "value": "CASH BASED"
      }
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
@Parcelize
data class ShareProduct(
    val accountingRule: AccountingRule? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val currency: Currency? = null,
    val description: String? = null,
    val id: Int? = null,
    val lockPeriodTypeEnum: LockPeriodTypeEnum? = null,
    val lockinPeriod: Int? = null,
    val maximumShares: Int? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodForDividendsTypeEnum: MinimumActivePeriodForDividendsTypeEnum? = null,
    val minimumShares: Int? = null,
    val name: String? = null,
    val nominalShares: Int? = null,
    val shareCapital: Double? = null,
    val shortName: String? = null,
    val totalShares: Int? = null,
    val totalSharesIssued: Int? = null,
    val unitPrice: Double? = null,
) : Parcelable
```

**Service Method**:
```kotlin
@GET("${ApiEndPoints.PRODUCTS}/" + ApiEndPoints.SHARE)
fun getShareProducts(
    @Query("clientId") clientId: Long?,
): Flow<Page<ShareProduct>>
```

**Status**: Implemented in ShareAccountService

---

## 2. Get Share Product Details

### GET /self/products/share/{productId}

**Purpose**: Fetch detailed information about a specific share product

**Request**:
```
GET /self/products/share/1?clientId=123
```

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| productId | Long | Yes | Share product ID |

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| clientId | Long | Optional | Client ID for eligibility check |

**Response**:
```json
{
  "id": 1,
  "name": "Equity Shares",
  "shortName": "ES",
  "description": "Standard equity shares for cooperative members",
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "decimalPlaces": 2,
    "displaySymbol": "$",
    "nameCode": "currency.USD",
    "displayLabel": "US Dollar ($)"
  },
  "totalShares": 10000,
  "totalSharesIssued": 5000,
  "unitPrice": 25.0,
  "shareCapital": 250000.0,
  "nominalShares": 100,
  "marketPrice": [
    {
      "id": 1,
      "fromDate": [2024, 1, 1],
      "shareValue": 25.0
    }
  ],
  "charges": [
    {
      "id": 1,
      "name": "Share Application Fee",
      "amount": 10.0,
      "chargeTimeType": {
        "id": 1,
        "code": "chargeTimeType.shareAccountActivation",
        "value": "Share Account Activation"
      }
    }
  ],
  "allowDividendCalculationForInactiveClients": false,
  "lockinPeriod": 12,
  "lockPeriodTypeEnum": {
    "id": 2,
    "code": "savings.lockin.period.months",
    "value": "Months"
  },
  "minimumActivePeriod": 6,
  "minimumActivePeriodForDividendsTypeEnum": {
    "id": 2,
    "code": "savings.lockin.period.months",
    "value": "Months"
  },
  "accountingRule": {
    "id": 2,
    "code": "accountingRuleType.cash",
    "value": "CASH BASED"
  },
  "accountingMappings": {
    "shareReferenceId": 1,
    "shareSuspenseId": 2,
    "shareEquityId": 3
  },
  "clientSavingsAccounts": [
    {
      "id": 1001,
      "savingsProductName": "Primary Savings"
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
@Parcelize
data class ShareProductDetails(
    val id: Int? = null,
    val name: String? = null,
    val shortName: String? = null,
    val description: String? = null,
    val currency: Currency? = null,
    val totalShares: Int? = null,
    val totalSharesIssued: Int? = null,
    val unitPrice: Double? = null,
    val shareCapital: Double? = null,
    val nominalShares: Int? = null,
    val marketPrice: List<MarketPrice>? = emptyList(),
    val charges: List<Charge>? = emptyList(),
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: EnumOption? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodForDividendsTypeEnum: EnumOption? = null,
    val accountingRule: EnumOption? = null,
    val accountingMappings: AccountingMappings? = null,
    val clientSavingsAccounts: List<SavingsAccountSummary>? = emptyList(),
) : Parcelable

@Serializable
@Parcelize
data class SavingsAccountSummary(
    val id: Int? = null,
    val savingsProductName: String? = null,
) : Parcelable
```

**Service Method**:
```kotlin
@GET("${ApiEndPoints.PRODUCTS}/" + ApiEndPoints.SHARE + "/{productId}")
fun getShareProductById(
    @Path("productId") productId: Long,
    @Query("clientId") clientId: Long?,
): Flow<ShareProductDetails>
```

**Status**: Implemented in ShareAccountService

---

## 3. Submit Share Application

### POST /self/shareaccounts

**Purpose**: Submit a new share account application

**Request**:
```json
POST /self/shareaccounts

{
  "clientId": 123,
  "productId": 1,
  "requestedShares": 50,
  "unitPrice": 25.0,
  "savingsAccountId": 1001,
  "applicationDate": "15 January 2024",
  "submittedDate": "15 January 2024",
  "locale": "en",
  "dateFormat": "dd MMMM yyyy"
}
```

**Request Body Fields**:
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| clientId | Long | Yes | Client ID |
| productId | Int | Yes | Share product ID |
| requestedShares | Int | Yes | Number of shares to purchase |
| unitPrice | Double | Yes | Price per share |
| savingsAccountId | Int | Yes | Linked savings account for payment |
| applicationDate | String | Yes | Date of application |
| submittedDate | String | Yes | Submission date |
| locale | String | Yes | Locale for date formatting |
| dateFormat | String | Yes | Date format pattern |

**Response**:
```json
{
  "officeId": 1,
  "clientId": 123,
  "savingsId": 3001,
  "resourceId": 3001
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class ShareApplicationPayload(
    val productId: Int,
    val unitPrice: Double,
    val requestedShares: Int,
    val submittedDate: String,
    val savingsAccountId: Int,
    val applicationDate: String,
    val locale: String,
    val dateFormat: String,
    val clientId: Long,
)
```

**Service Method**:
```kotlin
@POST(ApiEndPoints.SHARE_ACCOUNTS)
suspend fun submitShareApplication(
    @Body payload: ShareApplicationPayload?,
): HttpResponse
```

**Status**: Implemented in ShareAccountService

---

## 4. Get Share Account Details

### GET /self/shareaccounts/{accountId}

**Purpose**: Fetch detailed information about a specific share account

**Request**:
```
GET /self/shareaccounts/3001?associations=all
```

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| accountId | Long | Yes | Share account ID |

**Query Parameters**:
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| associations | String | "all" | Include related data (all, transactions, charges) |

**Response**:
```json
{
  "id": 3001,
  "accountNo": "SH-0001234567",
  "clientId": 123,
  "clientName": "John Doe",
  "productId": 1,
  "productName": "Equity Shares",
  "status": {
    "id": 300,
    "code": "shareAccountStatusType.active",
    "value": "Active",
    "submittedAndPendingApproval": false,
    "approved": true,
    "rejected": false,
    "active": true,
    "closed": false
  },
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "decimalPlaces": 2,
    "displaySymbol": "$",
    "nameCode": "currency.USD",
    "displayLabel": "US Dollar ($)"
  },
  "timeline": {
    "submittedOnDate": [2024, 1, 15],
    "submittedByUsername": "john.doe",
    "submittedByFirstname": "John",
    "submittedByLastname": "Doe",
    "approvedDate": [2024, 1, 18],
    "approvedByUsername": "admin",
    "approvedByFirstname": "System",
    "approvedByLastname": "Admin",
    "activatedDate": [2024, 1, 20],
    "activatedByUsername": "admin",
    "activatedByFirstname": "System",
    "activatedByLastname": "Admin"
  },
  "summary": {
    "id": 3001,
    "accountNo": "SH-0001234567",
    "productId": 1,
    "productName": "Equity Shares",
    "status": {
      "id": 300,
      "code": "shareAccountStatusType.active",
      "value": "Active"
    },
    "currency": {
      "code": "USD",
      "displaySymbol": "$"
    },
    "totalApprovedShares": 50,
    "totalPendingForApprovalShares": 0
  },
  "currentMarketPrice": 25.0,
  "savingsAccountId": 1001,
  "savingsAccountNumber": "SA-0001234567",
  "allowDividendCalculationForInactiveClients": false,
  "lockinPeriod": 12,
  "lockPeriodTypeEnum": {
    "id": 2,
    "code": "savings.lockin.period.months",
    "value": "Months"
  },
  "minimumActivePeriod": 6,
  "minimumActivePeriodTypeEnum": {
    "id": 2,
    "code": "savings.lockin.period.months",
    "value": "Months"
  },
  "charges": [
    {
      "id": 1,
      "chargeId": 1,
      "accountId": 3001,
      "name": "Share Application Fee",
      "chargeTimeType": {
        "id": 1,
        "code": "chargeTimeType.shareAccountActivation",
        "value": "Share Account Activation"
      },
      "amount": 10.0,
      "amountPaid": 10.0,
      "amountOutstanding": 0.0,
      "amountWaived": 0.0,
      "isPaid": true,
      "isActive": true
    }
  ],
  "purchasedShares": [
    {
      "id": 1,
      "accountId": 3001,
      "purchasedDate": [2024, 1, 20],
      "numberOfShares": 50,
      "purchasedPrice": 25.0,
      "amount": 1250.0,
      "amountPaid": 1250.0,
      "chargeAmount": 10.0,
      "status": {
        "id": 300,
        "code": "purchasedSharesStatusType.approved",
        "value": "Approved"
      },
      "type": {
        "id": 500,
        "code": "purchaseType.purchase",
        "value": "Purchase"
      }
    }
  ],
  "dividends": []
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class ShareAccountWithAssociations(
    val id: Long? = null,
    val accountNo: String? = null,
    val clientId: Long? = null,
    val clientName: String? = null,
    val productId: Long? = null,
    val productName: String? = null,
    val status: Status? = null,
    val currency: Currency? = null,
    val timeline: Timeline? = null,
    val summary: Summary? = null,
    val currentMarketPrice: Double? = null,
    val savingsAccountId: Long? = null,
    val savingsAccountNumber: String? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,
    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: EnumOptionData? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodTypeEnum: EnumOptionData? = null,
    val charges: List<Charge> = emptyList(),
    val purchasedShares: List<Transactions> = emptyList(),
    val dividends: List<String> = emptyList(),
)
```

**Service Method**:
```kotlin
@GET(ApiEndPoints.SHARE_ACCOUNTS + "/{accountId}")
fun getShareAccountDetails(
    @Path("accountId") accountId: Long,
    @Query("associations") associations: String = "all",
): Flow<ShareAccountWithAssociations>
```

**Status**: Implemented in ShareAccountService

---

## 5. Supporting Data Models

### Status

```kotlin
@Serializable
data class Status(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
    val submittedAndPendingApproval: Boolean? = null,
    val approved: Boolean? = null,
    val rejected: Boolean? = null,
    val active: Boolean? = null,
    val closed: Boolean? = null,
)
```

### Currency

```kotlin
@Serializable
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int = 0,
    val inMultiplesOf: Double = 0.0,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null,
)
```

### Timeline

```kotlin
@Serializable
data class Timeline(
    val submittedOnDate: List<Int>? = null,
    val submittedByUsername: String? = null,
    val submittedByFirstname: String? = null,
    val submittedByLastname: String? = null,
    val approvedDate: List<Int>? = null,
    val approvedByUsername: String? = null,
    val approvedByFirstname: String? = null,
    val approvedByLastname: String? = null,
    val activatedDate: List<Int>? = null,
    val activatedByUsername: String? = null,
    val activatedByFirstname: String? = null,
    val activatedByLastname: String? = null,
)
```

### Summary

```kotlin
@Serializable
data class Summary(
    val id: Long? = null,
    val accountNo: String? = null,
    val productId: Long? = null,
    val productName: String? = null,
    val status: Status? = null,
    val currency: Currency? = null,
    val timeline: Timeline? = null,
    val totalApprovedShares: Int? = null,
    val totalPendingForApprovalShares: Int? = null,
)
```

### Transactions (Share Transactions)

```kotlin
@Serializable
data class Transactions(
    val accountId: Long? = null,
    val amount: Double? = null,
    val amountPaid: Double? = null,
    val chargeAmount: Double? = null,
    val id: Long? = null,
    val numberOfShares: Int? = null,
    val purchasedDate: List<Int> = emptyList(),
    val purchasedPrice: Double? = null,
    val status: EnumOptionData? = null,
    val type: EnumOptionData? = null,
)
```

### EnumOptionData

```kotlin
@Serializable
data class EnumOptionData(
    val id: Long? = null,
    val code: String? = null,
    val value: String? = null,
)
```

### ShareAccount (List Item)

```kotlin
@Serializable
data class ShareAccount(
    val id: Long = 0,
    val accountNo: String? = null,
    val totalApprovedShares: Int? = null,
    val totalPendingForApprovalShares: Int? = null,
    val productId: Int? = null,
    val productName: String? = null,
    val shortProductName: String? = null,
    val status: Status? = null,
    val currency: Currency? = null,
    val timeline: Timeline? = null,
)
```

---

## API Summary Table

| Endpoint | Method | Service | Repository | Priority |
|----------|--------|---------|------------|----------|
| /self/products/share | GET | ShareAccountService | ShareAccountRepository | P1 |
| /self/products/share/{id} | GET | ShareAccountService | ShareAccountRepository | P1 |
| /self/shareaccounts | POST | ShareAccountService | ShareAccountRepository | P1 |
| /self/shareaccounts/{id} | GET | ShareAccountService | ShareAccountRepository | P0 |
| /self/clients/{id}/accounts | GET | ClientService | AccountsRepository | P0 |

---

## API Endpoint Constants

```kotlin
object ApiEndPoints {
    const val SHARE_ACCOUNTS = "shareaccounts"
    const val SHARE = "share"
    const val PRODUCTS = "products"
}
```

---

## Error Responses

| Status Code | Error | Description | User Message |
|-------------|-------|-------------|--------------|
| 400 | Bad Request | Invalid request payload | "Please check your input" |
| 401 | Unauthorized | Invalid/expired token | "Please login again" |
| 403 | Forbidden | Insufficient permissions | "Access denied" |
| 404 | Not Found | Account doesn't exist | "Share account not found" |
| 409 | Conflict | Business rule violation | "Unable to process request" |
| 500 | Server Error | Internal server error | "Service unavailable" |

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
      "defaultUserMessage": "Number of shares must be greater than 0",
      "userMessageGlobalisationCode": "validation.msg.shares.invalid",
      "parameterName": "requestedShares"
    }
  ]
}
```

---

## Repository Implementation

```kotlin
class ShareAccountRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : ShareAccountRepository {

    override fun getShareProducts(clientId: Long?): Flow<DataState<Page<ShareProduct>>> {
        return dataManager.shareAccountApi.getShareProducts(clientId)
            .map { response -> DataState.Success(response) }
            .catch { exception -> DataState.Error(exception, exception.message) }
            .flowOn(ioDispatcher)
    }

    override fun getShareProductById(
        productId: Long,
        clientId: Long?,
    ): Flow<DataState<ShareProductDetails>> {
        return dataManager.shareAccountApi.getShareProductById(productId, clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun submitShareApplication(payload: ShareApplicationPayload?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.shareAccountApi.submitShareApplication(payload)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override fun getShareAccountDetails(accountId: Long): Flow<DataState<ShareAccountWithAssociations>> {
        return dataManager.shareAccountApi.getShareAccountDetails(accountId)
            .asDataStateFlow()
            .flowOn(ioDispatcher)
    }
}
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial API documentation based on implementation |

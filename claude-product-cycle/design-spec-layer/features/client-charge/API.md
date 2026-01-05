# Client Charges - API Reference

> **Last Updated**: 2025-12-29

---

## Base URL

```
https://tt.mifos.community/fineract-provider/api/v1/self/
```

---

## Endpoints Required

### 1. Get Client Charges

**Endpoint**: `GET /clients/{clientId}/charges`

**Purpose**: Fetches all charges associated with the client (paginated response)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
{
    "totalFilteredRecords": 3,
    "pageItems": [
        {
            "clientId": 1,
            "chargeId": 101,
            "name": "Processing Fee",
            "dueDate": [2025, 1, 15],
            "chargeTimeType": {
                "id": 2,
                "code": "chargeTimeType.specifiedDueDate",
                "value": "Specified due date"
            },
            "chargeCalculationType": {
                "id": 1,
                "code": "chargeCalculationType.flat",
                "value": "Flat"
            },
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$",
                "nameCode": "currency.USD",
                "displayLabel": "US Dollar ($)"
            },
            "amount": 50.00,
            "amountPaid": 0.00,
            "amountWaived": 0.00,
            "amountWrittenOff": 0.00,
            "amountOutstanding": 50.00,
            "penalty": false,
            "isActive": true,
            "isChargePaid": false,
            "isChargeWaived": false,
            "paid": false,
            "waived": false
        },
        {
            "clientId": 1,
            "chargeId": 102,
            "name": "Annual Maintenance Fee",
            "dueDate": [2024, 12, 1],
            "chargeTimeType": {
                "id": 1,
                "code": "chargeTimeType.annual",
                "value": "Annual"
            },
            "chargeCalculationType": {
                "id": 1,
                "code": "chargeCalculationType.flat",
                "value": "Flat"
            },
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$",
                "nameCode": "currency.USD",
                "displayLabel": "US Dollar ($)"
            },
            "amount": 25.00,
            "amountPaid": 25.00,
            "amountWaived": 0.00,
            "amountWrittenOff": 0.00,
            "amountOutstanding": 0.00,
            "penalty": false,
            "isActive": false,
            "isChargePaid": true,
            "isChargeWaived": false,
            "paid": true,
            "waived": false
        }
    ]
}
```

**Kotlin DTO**: Uses `Page<Charge>` from `core/model/entity/`

**Status**: Implemented in `ClientChargeService`

---

### 2. Get Loan Charges

**Endpoint**: `GET /loans/{loanId}/charges`

**Purpose**: Fetches charges for a specific loan account

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
[
    {
        "chargeId": 201,
        "name": "Disbursement Fee",
        "dueDate": [2024, 11, 1],
        "chargeTimeType": {
            "id": 1,
            "code": "chargeTimeType.disbursement",
            "value": "Disbursement"
        },
        "chargeCalculationType": {
            "id": 2,
            "code": "chargeCalculationType.percentageOfAmount",
            "value": "% Amount"
        },
        "currency": {
            "code": "USD",
            "name": "US Dollar",
            "decimalPlaces": 2,
            "displaySymbol": "$"
        },
        "amount": 100.00,
        "amountPaid": 100.00,
        "amountWaived": 0.00,
        "amountWrittenOff": 0.00,
        "amountOutstanding": 0.00,
        "penalty": false,
        "isActive": false,
        "isChargePaid": true,
        "paid": true,
        "waived": false
    },
    {
        "chargeId": 202,
        "name": "Late Payment Penalty",
        "dueDate": [2025, 1, 1],
        "chargeTimeType": {
            "id": 9,
            "code": "chargeTimeType.overdueInstallment",
            "value": "Overdue Installment"
        },
        "chargeCalculationType": {
            "id": 1,
            "code": "chargeCalculationType.flat",
            "value": "Flat"
        },
        "currency": {
            "code": "USD",
            "name": "US Dollar",
            "decimalPlaces": 2,
            "displaySymbol": "$"
        },
        "amount": 15.00,
        "amountPaid": 0.00,
        "amountWaived": 0.00,
        "amountWrittenOff": 0.00,
        "amountOutstanding": 15.00,
        "penalty": true,
        "isActive": true,
        "isChargePaid": false,
        "paid": false,
        "waived": false
    }
]
```

**Kotlin DTO**: Uses `List<Charge>` from `core/model/entity/`

**Status**: Implemented in `ClientChargeService`

---

### 3. Get Savings Account Charges

**Endpoint**: `GET /savingsaccounts/{accountId}/charges`

**Purpose**: Fetches charges for a specific savings account

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
[
    {
        "chargeId": 301,
        "name": "Account Opening Fee",
        "dueDate": [2024, 6, 1],
        "chargeTimeType": {
            "id": 1,
            "code": "chargeTimeType.savingsActivation",
            "value": "Savings Activation"
        },
        "chargeCalculationType": {
            "id": 1,
            "code": "chargeCalculationType.flat",
            "value": "Flat"
        },
        "currency": {
            "code": "USD",
            "name": "US Dollar",
            "decimalPlaces": 2,
            "displaySymbol": "$"
        },
        "amount": 10.00,
        "amountPaid": 10.00,
        "amountWaived": 0.00,
        "amountWrittenOff": 0.00,
        "amountOutstanding": 0.00,
        "penalty": false,
        "isActive": false,
        "isChargePaid": true,
        "paid": true,
        "waived": false
    },
    {
        "chargeId": 302,
        "name": "Monthly Service Fee",
        "dueDate": [2025, 1, 31],
        "chargeTimeType": {
            "id": 3,
            "code": "chargeTimeType.monthly",
            "value": "Monthly Fee"
        },
        "chargeCalculationType": {
            "id": 1,
            "code": "chargeCalculationType.flat",
            "value": "Flat"
        },
        "currency": {
            "code": "USD",
            "name": "US Dollar",
            "decimalPlaces": 2,
            "displaySymbol": "$"
        },
        "amount": 5.00,
        "amountPaid": 0.00,
        "amountWaived": 0.00,
        "amountWrittenOff": 0.00,
        "amountOutstanding": 5.00,
        "penalty": false,
        "isActive": true,
        "isChargePaid": false,
        "paid": false,
        "waived": false
    }
]
```

**Kotlin DTO**: Uses `List<Charge>` from `core/model/entity/`

**Status**: Implemented in `ClientChargeService`

---

### 4. Get Share Account Details (includes Charges)

**Endpoint**: `GET /shareaccounts/{accountId}`

**Purpose**: Fetches share account details which includes associated charges

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
    "accountNo": "000000001",
    "clientId": 1,
    "productId": 1,
    "productName": "Share Product",
    "status": {
        "id": 300,
        "code": "shareAccountStatusType.active",
        "value": "Active"
    },
    "charges": [
        {
            "chargeId": 401,
            "name": "Share Purchase Fee",
            "dueDate": [2024, 8, 1],
            "chargeTimeType": {
                "id": 13,
                "code": "chargeTimeType.sharesPurchase",
                "value": "Share Purchase"
            },
            "chargeCalculationType": {
                "id": 1,
                "code": "chargeCalculationType.flat",
                "value": "Flat"
            },
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$"
            },
            "amount": 20.00,
            "amountPaid": 20.00,
            "amountWaived": 0.00,
            "amountWrittenOff": 0.00,
            "amountOutstanding": 0.00,
            "penalty": false,
            "isActive": false,
            "isChargePaid": true,
            "paid": true,
            "waived": false
        }
    ]
}
```

**Kotlin DTO**: Uses `ShareAccountDetails` which contains `List<Charge>`

**Status**: Implemented in `ShareAccountService`

---

## Kotlin DTOs

### Charge

```kotlin
// Location: core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/entity/Charge.kt

@Serializable
@Parcelize
data class Charge(
    val clientId: Int? = null,
    val chargeId: Int? = null,
    val name: String? = null,
    val dueDate: ArrayList<Int?> = arrayListOf(),
    val chargeTimeType: ChargeTimeType? = null,
    val chargeCalculationType: ChargeCalculationType? = null,
    val currency: Currency? = null,
    val amount: Double = 0.0,
    val amountPaid: Double = 0.0,
    val amountWaived: Double = 0.0,
    val amountWrittenOff: Double = 0.0,
    val amountOutstanding: Double = 0.0,
    val penalty: Boolean = false,
    val isActive: Boolean = false,
    val isChargePaid: Boolean = false,
    val isChargeWaived: Boolean = false,
    val paid: Boolean = false,
    val waived: Boolean = false,
) : Parcelable
```

### ChargeTimeType

```kotlin
// Location: core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/entity/ChargeTimeType.kt

@Serializable
@Parcelize
data class ChargeTimeType(
    val id: Int = 0,
    val code: String? = null,
    val value: String? = null,
) : Parcelable
```

### ChargeCalculationType

```kotlin
// Location: core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/entity/ChargeCalculationType.kt

@Parcelize
@Serializable
data class ChargeCalculationType(
    val id: Int = 0,
    val code: String? = null,
    val value: String? = null,
) : Parcelable
```

### Currency

```kotlin
// Location: core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/entity/Currency.kt

@Serializable
@Parcelize
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int = 0,
    val inMultiplesOf: Double = 0.0,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null,
) : Parcelable
```

### Page

```kotlin
// Location: core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/entity/Page.kt

@Serializable
data class Page<T>(
    val totalFilteredRecords: Int = 0,
    val pageItems: List<T> = emptyList(),
)
```

### ChargeType (Enum)

```kotlin
// Location: core/model/src/commonMain/kotlin/org/mifos/mobile/core/model/enums/ChargeType.kt

enum class ChargeType(val type: String) {
    CLIENT("clients"),
    SAVINGS("savingsaccounts"),
    LOAN("loans"),
    SHARE("shareaccounts"),
}
```

---

## Kotlin Implementation

### Service (ClientChargeService.kt)

```kotlin
// Location: core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/services/ClientChargeService.kt

interface ClientChargeService {

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    fun getClientChargeList(@Path("clientId") clientId: Long): Flow<Page<Charge>>

    @GET("{chargeType}/{chargeTypeId}/charges")
    fun getChargeList(
        @Path("chargeType") chargeType: String,
        @Path("chargeTypeId") chargeTypeId: Long,
    ): Flow<List<Charge>>
}
```

### Repository Interface (ClientChargeRepository.kt)

```kotlin
// Location: core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/ClientChargeRepository.kt

interface ClientChargeRepository {

    fun getCharges(clientId: Long): Flow<DataState<Page<Charge>>>

    fun getLoanOrSavingsCharges(chargeType: ChargeType, chargeTypeId: Long): Flow<DataState<List<Charge>>>

    fun clientLocalCharges(): Flow<DataState<Page<Charge>>>

    suspend fun syncCharges(charges: Page<Charge>?): DataState<Page<Charge>?>

    fun getShareAccountCharges(shareAccountId: Long): Flow<DataState<List<Charge>>>
}
```

### Repository Implementation (ClientChargeRepositoryImp.kt)

```kotlin
// Location: core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repositoryImpl/ClientChargeRepositoryImp.kt

class ClientChargeRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : ClientChargeRepository {

    override fun getCharges(clientId: Long): Flow<DataState<Page<Charge>>> {
        return dataManager.clientChargeApi.getClientChargeList(clientId)
            .map { response -> DataState.Success(response) }
            .catch { exception -> DataState.Error(exception, exception.message) }
            .flowOn(ioDispatcher)
    }

    override fun getLoanOrSavingsCharges(
        chargeType: ChargeType,
        chargeTypeId: Long
    ): Flow<DataState<List<Charge>>> {
        return dataManager.clientChargeApi.getChargeList(chargeType.type, chargeTypeId)
            .map { response -> DataState.Success(response) }
            .catch { exception -> DataState.Error(exception, exception.message) }
            .flowOn(ioDispatcher)
    }

    override fun getShareAccountCharges(shareAccountId: Long): Flow<DataState<List<Charge>>> {
        return dataManager.shareAccountApi.getShareAccountDetails(shareAccountId)
            .map { response -> DataState.Success(response.charges) }
            .catch { exception -> DataState.Error(exception, exception.message) }
            .flowOn(ioDispatcher)
    }

    // ... other methods
}
```

---

## API Summary

| Endpoint | Method | Service | Repository | Response Type | Status |
|----------|--------|---------|------------|---------------|--------|
| `/clients/{id}/charges` | GET | ClientChargeService | ClientChargeRepository | `Page<Charge>` | Implemented |
| `/loans/{id}/charges` | GET | ClientChargeService | ClientChargeRepository | `List<Charge>` | Implemented |
| `/savingsaccounts/{id}/charges` | GET | ClientChargeService | ClientChargeRepository | `List<Charge>` | Implemented |
| `/shareaccounts/{id}` | GET | ShareAccountService | ClientChargeRepository | `ShareAccountDetails` (contains charges) | Implemented |

---

## Notes

- **Client Charges**: Return paginated response with `Page<Charge>` wrapper containing `totalFilteredRecords` and `pageItems`
- **Loan/Savings Charges**: Return direct `List<Charge>` without pagination wrapper
- **Share Charges**: Retrieved via Share Account Details endpoint, extracted from `response.charges`
- **Currency Formatting**: Use `CurrencyFormatter.format()` with currency code from charge
- **Date Handling**: Due dates are arrays `[year, month, day]`, use `DateHelper.getDateAsString()` to format
- **Charge Status**: Check `isChargePaid` or `paid` boolean to determine payment status
- **Empty Due Date**: Handle empty `dueDate` array by showing "-" as fallback

---

## Error Responses

| Status Code | Description | Handling |
|-------------|-------------|----------|
| 401 | Unauthorized | Redirect to login |
| 403 | Forbidden | Show permission error |
| 404 | Account not found | Show "Account not found" error |
| 500 | Server error | Show generic server error with retry |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Updated with complete DTOs, repository implementation, and detailed response examples from codebase analysis |
| 2025-12-29 | Initial API documentation |

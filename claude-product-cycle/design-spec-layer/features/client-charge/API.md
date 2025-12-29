# Client Charges - API Reference

## Base URL
`https://tt.mifos.community/fineract-provider/api/v1/self/`

---

## Endpoints Required

### 1. Get Client Charges

**Endpoint**: `GET /clients/{clientId}/charges`

**Description**: Fetches all charges associated with the client

**Response**:
```json
{
    "totalFilteredRecords": 2,
    "pageItems": [
        {
            "id": 1,
            "name": "Processing Fee",
            "amount": 50.00,
            "amountPaid": 0.00,
            "amountWaived": 0.00,
            "amountOutstanding": 50.00,
            "dueDate": [2025, 1, 15],
            "isPenalty": false,
            "isActive": true,
            "currency": {
                "code": "USD",
                "displaySymbol": "$"
            }
        }
    ]
}
```

**Status**: Implemented in ClientChargeService

---

### 2. Get Loan Charges

**Endpoint**: `GET /loans/{loanId}/charges`

**Description**: Fetches charges for a specific loan account

**Response**:
```json
[
    {
        "id": 1,
        "name": "Disbursement Fee",
        "amount": 100.00,
        "amountPaid": 100.00,
        "amountWaived": 0.00,
        "amountOutstanding": 0.00,
        "dueDate": [2024, 12, 1],
        "isPenalty": false,
        "isActive": false
    }
]
```

**Status**: Implemented in ClientChargeService

---

### 3. Get Savings Account Charges

**Endpoint**: `GET /savingsaccounts/{accountId}/charges`

**Description**: Fetches charges for a specific savings account

**Response**: Same structure as loan charges

**Status**: Implemented in ClientChargeService

---

### 4. Get Share Account Charges

**Endpoint**: `GET /shareaccounts/{accountId}/charges`

**Description**: Fetches charges for a specific share account

**Response**: Same structure as loan charges

**Status**: Implemented in ShareAccountRepository

---

## Kotlin DTO

```kotlin
@Serializable
data class Charge(
    val id: Long = 0,
    val name: String? = null,
    val amount: Double = 0.0,
    val amountPaid: Double = 0.0,
    val amountWaived: Double = 0.0,
    val amountOutstanding: Double = 0.0,
    val dueDate: List<Int>? = null,
    val isPenalty: Boolean = false,
    val isActive: Boolean = false,
    val currency: Currency? = null,
)
```

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| /clients/{id}/charges | ClientChargeService | ClientChargeRepository | Implemented |
| /loans/{id}/charges | ClientChargeService | ClientChargeRepository | Implemented |
| /savingsaccounts/{id}/charges | ClientChargeService | ClientChargeRepository | Implemented |
| /shareaccounts/{id}/charges | ShareAccountService | ShareAccountRepository | Implemented |

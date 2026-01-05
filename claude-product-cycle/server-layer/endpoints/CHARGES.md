# Charges Endpoints

> **Category**: Charges
> **Endpoints**: 3
> **Service**: `ClientChargeService`

---

## GET /clients/{clientId}/charges

**Purpose**: Get client charges (paginated)

**Service**: `ClientChargeService.getClientChargeList(clientId)`

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
            "chargeTimeType": { "id": 2, "value": "Specified due date" },
            "chargeCalculationType": { "id": 1, "value": "Flat" },
            "currency": {
                "code": "USD",
                "displaySymbol": "$",
                "decimalPlaces": 2
            },
            "amount": 50.00,
            "amountPaid": 0.00,
            "amountOutstanding": 50.00,
            "penalty": false,
            "isActive": true,
            "paid": false
        }
    ]
}
```

**DTO**: `Page<Charge>`
```kotlin
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
    val paid: Boolean = false,
    val waived: Boolean = false,
) : Parcelable
```

---

## GET /loans/{loanId}/charges

**Purpose**: Get loan charges

**Service**: `ClientChargeService.getChargeList("loans", loanId)`

**Response**:
```json
[
    {
        "chargeId": 201,
        "name": "Disbursement Fee",
        "dueDate": [2024, 11, 1],
        "chargeTimeType": { "id": 1, "value": "Disbursement" },
        "amount": 100.00,
        "amountPaid": 100.00,
        "amountOutstanding": 0.00,
        "penalty": false,
        "paid": true
    }
]
```

**DTO**: `List<Charge>`

---

## GET /savingsaccounts/{accountId}/charges

**Purpose**: Get savings account charges

**Service**: `ClientChargeService.getChargeList("savingsaccounts", accountId)`

**Response**:
```json
[
    {
        "chargeId": 301,
        "name": "Monthly Service Fee",
        "dueDate": [2025, 1, 31],
        "chargeTimeType": { "id": 3, "value": "Monthly Fee" },
        "amount": 5.00,
        "amountPaid": 0.00,
        "amountOutstanding": 5.00,
        "penalty": false,
        "paid": false
    }
]
```

**DTO**: `List<Charge>`

---

## Charge Type Enum

```kotlin
enum class ChargeType(val type: String) {
    CLIENT("clients"),
    SAVINGS("savingsaccounts"),
    LOAN("loans"),
    SHARE("shareaccounts"),
}
```

---

## Related

- Design Layer: [client-charge/API.md](../../design-spec-layer/features/client-charge/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

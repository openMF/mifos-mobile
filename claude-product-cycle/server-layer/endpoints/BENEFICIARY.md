# Beneficiary Endpoints

> **Category**: Beneficiary
> **Endpoints**: 5
> **Service**: `BeneficiaryService`

---

## GET /beneficiaries/tpt

**Purpose**: Get list of beneficiaries

**Service**: `BeneficiaryService.beneficiaryList()`

**Response**:
```json
[
    {
        "id": 5001,
        "name": "John Doe",
        "officeName": "Head Office",
        "clientName": "John Doe",
        "accountType": { "id": 2, "value": "Savings Account" },
        "accountNumber": "SA-0001234567",
        "transferLimit": 10000.00
    }
]
```

**DTO**: `List<Beneficiary>`
```kotlin
@Serializable
@Parcelize
data class Beneficiary(
    val id: Long? = null,
    val name: String? = null,
    val officeName: String? = null,
    val clientName: String? = null,
    val accountType: AccountType? = null,
    val accountNumber: String? = null,
    val transferLimit: Double? = null,
) : Parcelable
```

---

## GET /beneficiaries/tpt/template

**Purpose**: Get beneficiary template (account type options)

**Service**: `BeneficiaryService.beneficiaryTemplate()`

**Response**:
```json
{
    "accountTypeOptions": [
        { "id": 0, "value": "Share Account" },
        { "id": 1, "value": "Loan Account" },
        { "id": 2, "value": "Savings Account" }
    ]
}
```

**DTO**: `BeneficiaryTemplate`

---

## POST /beneficiaries/tpt

**Purpose**: Create new beneficiary

**Service**: `BeneficiaryService.createBeneficiary(payload)`

**Request**:
```json
{
    "locale": "en",
    "name": "John Doe",
    "accountNumber": "SA-0001234567",
    "accountType": 2,
    "transferLimit": 10000,
    "officeName": "Head Office"
}
```

**Response**:
```json
{
    "resourceId": 5003
}
```

**DTO**: `BeneficiaryPayload`
```kotlin
@Serializable
@Parcelize
data class BeneficiaryPayload(
    val locale: String? = null,
    val name: String? = null,
    val accountNumber: String? = null,
    val accountType: Int? = 0,
    val transferLimit: Int? = 0,
    val officeName: String? = null,
) : Parcelable
```

---

## PUT /beneficiaries/tpt/{beneficiaryId}

**Purpose**: Update beneficiary

**Service**: `BeneficiaryService.updateBeneficiary(beneficiaryId, payload)`

**Request**:
```json
{
    "name": "John Doe Updated",
    "transferLimit": 15000
}
```

**DTO**: `BeneficiaryUpdatePayload`
```kotlin
@Serializable
data class BeneficiaryUpdatePayload(
    val name: String? = null,
    val transferLimit: Int = 0,
)
```

**Note**: Only `name` and `transferLimit` can be updated.

---

## DELETE /beneficiaries/tpt/{beneficiaryId}

**Purpose**: Delete beneficiary

**Service**: `BeneficiaryService.deleteBeneficiary(beneficiaryId)`

**Response**:
```json
{
    "resourceId": 5001
}
```

---

## Account Type Mapping

| ID | Code | Value |
|----|------|-------|
| 0 | accountType.share | Share Account |
| 1 | accountType.loan | Loan Account |
| 2 | accountType.savings | Savings Account |

---

## Related

- Design Layer: [beneficiary/API.md](../../design-spec-layer/features/beneficiary/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

# Transfer Endpoints

> **Category**: Transfer
> **Endpoints**: 2
> **Service**: `SavingAccountsListService`, `ThirdPartyTransferService`

---

## GET /accounttransfers/template

**Purpose**: Get transfer template with account options

**Service**: `SavingAccountsListService.accountTransferTemplate(fromAccountId, fromAccountType)`

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| `fromAccountId` | Long | Source account ID |
| `fromAccountType` | Long | Account type (2 = Savings) |

**Response**:
```json
{
    "fromAccountTypeOptions": [
        { "id": 1, "value": "Loan Account" },
        { "id": 2, "value": "Savings Account" }
    ],
    "toAccountTypeOptions": [
        { "id": 1, "value": "Loan Account" },
        { "id": 2, "value": "Savings Account" }
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
    "toAccountOptions": [...]
}
```

**DTO**: `AccountOptionsTemplate`

---

## POST /accounttransfers

**Purpose**: Execute account transfer

**Service**: `SavingAccountsListService.makeTransfer(payload)`

**Request**:
```json
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

**DTO**: `TransferPayload`
```kotlin
@Serializable
data class TransferPayload(
    val fromOfficeId: Long? = null,
    val fromClientId: Long? = null,
    val fromAccountType: Long? = null,
    val fromAccountId: Long? = null,
    val toOfficeId: Long? = null,
    val toClientId: Long? = null,
    val toAccountType: Long? = null,
    val toAccountId: Long? = null,
    val transferDate: String? = null,
    val transferAmount: Double? = null,
    val transferDescription: String? = null,
    val dateFormat: String? = null,
    val locale: String? = null,
)
```

---

## Third Party Transfer

For transfers to beneficiaries, use:
- **Template**: `GET /accounttransfers/template?type=tpt`
- **Execute**: `POST /accounttransfers?type=tpt`

**Service**: `ThirdPartyTransferService`

---

## Related

- Design Layer: [transfer/API.md](../../design-spec-layer/features/transfer/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

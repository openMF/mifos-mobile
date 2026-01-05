# Share Account Endpoints

> **Category**: Share Account
> **Endpoints**: 3
> **Service**: `ShareAccountService`

---

## GET /products/share

**Purpose**: Get available share products

**Service**: `ShareAccountService.getShareProducts()`

**Response**:
```json
[
    {
        "id": 1,
        "name": "Common Shares",
        "shortName": "CS",
        "description": "Common equity shares",
        "unitPrice": 100.00,
        "currency": {
            "code": "USD",
            "displaySymbol": "$",
            "decimalPlaces": 2
        },
        "totalShares": 10000,
        "totalSharesIssued": 5000
    }
]
```

**DTO**: `List<ShareProduct>`

---

## POST /shareaccounts

**Purpose**: Submit share account application

**Service**: `ShareAccountService.submitShareApplication(payload)`

**Request**:
```json
{
    "clientId": 1,
    "productId": 1,
    "requestedShares": 10,
    "applicationDate": "29 December 2025",
    "locale": "en",
    "dateFormat": "dd MMMM yyyy"
}
```

**Response**:
```json
{
    "officeId": 1,
    "clientId": 1,
    "resourceId": 12345
}
```

**DTO**: `ShareAccountPayload`

---

## GET /shareaccounts/{accountId}

**Purpose**: Get share account details

**Service**: `ShareAccountService.getShareAccountDetails(accountId)`

**Response**:
```json
{
    "id": 12345,
    "accountNo": "000000001",
    "clientId": 1,
    "clientName": "John Doe",
    "productId": 1,
    "productName": "Common Shares",
    "status": {
        "id": 300,
        "code": "shareAccountStatusType.active",
        "value": "Active"
    },
    "timeline": {
        "submittedOnDate": [2025, 12, 15],
        "approvedDate": [2025, 12, 16],
        "activatedDate": [2025, 12, 16]
    },
    "currency": {
        "code": "USD",
        "displaySymbol": "$"
    },
    "summary": {
        "totalApprovedShares": 10,
        "totalPendingShares": 0,
        "totalShareValue": 1000.00
    },
    "charges": [...]
}
```

**DTO**: `ShareAccountDetails`

---

## Related

- Design Layer: [share-account/API.md](../../design-spec-layer/features/share-account/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

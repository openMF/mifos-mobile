# Savings Account Endpoints

> **Category**: Savings Account
> **Endpoints**: 7
> **Service**: `SavingAccountsListService`

---

## GET /savingsaccounts/{accountId}

**Purpose**: Get savings account details with optional associations

**Service**: `SavingAccountsListService.getSavingsWithAssociations(accountId, associations)`

**Query Parameters**:
| Parameter | Type | Values |
|-----------|------|--------|
| `associations` | String | `transactions`, `charges` |

**Response**:
```json
{
    "id": 12345,
    "accountNo": "000000012345",
    "depositType": { "id": 100, "value": "Savings" },
    "clientId": 1,
    "clientName": "John Doe",
    "savingsProductId": 1,
    "savingsProductName": "Basic Savings",
    "status": {
        "id": 300,
        "code": "savingsAccountStatusType.active",
        "value": "Active"
    },
    "currency": {
        "code": "USD",
        "displaySymbol": "$",
        "decimalPlaces": 2
    },
    "summary": {
        "totalDeposits": 5000.00,
        "totalWithdrawals": 3765.44,
        "accountBalance": 1234.56
    },
    "transactions": [...]
}
```

**DTO**: `SavingsWithAssociations`

---

## GET /savingsaccounts/template

**Purpose**: Get template for savings application

**Service**: `SavingAccountsListService.getSavingsAccountApplicationTemplate(clientId)`

**Query Parameters**:
| Parameter | Type | Required |
|-----------|------|----------|
| `clientId` | Long | Yes |
| `productId` | Long | No |

**Response**:
```json
{
    "clientId": 1,
    "clientName": "John Doe",
    "productOptions": [
        {
            "id": 1,
            "name": "Basic Savings",
            "allowOverdraft": false
        }
    ]
}
```

**DTO**: `SavingsAccountTemplate`

---

## GET /savingsproducts

**Purpose**: Get available savings products

**Service**: `SavingAccountsListService.getSavingsProducts()`

**Response**:
```json
[
    {
        "id": 1,
        "name": "Basic Savings",
        "shortName": "BS",
        "description": "Basic savings account"
    }
]
```

---

## POST /savingsaccounts

**Purpose**: Submit savings account application

**Service**: `SavingAccountsListService.submitSavingAccountApplication(payload)`

**Request**:
```json
{
    "clientId": 1,
    "productId": 1,
    "locale": "en",
    "dateFormat": "dd MMMM yyyy",
    "submittedOnDate": "29 December 2025"
}
```

**Response**:
```json
{
    "officeId": 1,
    "clientId": 1,
    "savingsId": 12345,
    "resourceId": 12345
}
```

**DTO**: `SavingsAccountApplicationPayload`

---

## PUT /savingsaccounts/{accountId}

**Purpose**: Update pending savings account

**Service**: `SavingAccountsListService.updateSavingsAccountUpdate(accountId, payload)`

**Request**:
```json
{
    "clientId": 1,
    "productId": 2
}
```

**DTO**: `SavingsAccountUpdatePayload`

---

## POST /savingsaccounts/{savingsId}?command=withdrawnByApplicant

**Purpose**: Withdraw savings application

**Service**: `SavingAccountsListService.submitWithdrawSavingsAccount(savingsId, payload)`

**Request**:
```json
{
    "locale": "en",
    "dateFormat": "dd MMMM yyyy",
    "withdrawnOnDate": "29 December 2025",
    "note": "User requested withdrawal"
}
```

**DTO**: `SavingsAccountWithdrawPayload`

---

## GET /savingsaccounts/{accountId}/transactions/{transactionId}

**Purpose**: Get savings transaction details

**Service**: `SavingAccountsListService.getSavingsAccountTransactionDetails(accountId, transactionId)`

**Response**:
```json
{
    "id": 1,
    "transactionType": { "id": 1, "value": "Deposit" },
    "accountId": 12345,
    "date": [2025, 12, 28],
    "amount": 100.00,
    "runningBalance": 1234.56
}
```

**DTO**: `TransactionDetails`

---

## Related

- Design Layer: [savings-account/API.md](../../design-spec-layer/features/savings-account/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

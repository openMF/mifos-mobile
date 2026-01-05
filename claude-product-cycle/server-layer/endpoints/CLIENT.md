# Client Endpoints

> **Category**: Client
> **Endpoints**: 4
> **Service**: `ClientService`

---

## GET /clients

**Purpose**: Get client list for authenticated user

**Service**: `ClientService.clients()`

**Response**:
```json
{
    "pageItems": [
        {
            "id": 1,
            "accountNo": "000000001",
            "displayName": "John Doe",
            "officeId": 1,
            "officeName": "Head Office"
        }
    ]
}
```

**DTO**: `Page<Client>`

---

## GET /clients/{clientId}

**Purpose**: Get client details

**Service**: `ClientService.getClientDetails(clientId)`

**Response**:
```json
{
    "id": 1,
    "accountNo": "000000001",
    "displayName": "John Doe",
    "firstname": "John",
    "lastname": "Doe",
    "mobileNo": "1234567890",
    "emailAddress": "john@example.com",
    "dateOfBirth": [1990, 1, 15],
    "officeId": 1,
    "officeName": "Head Office"
}
```

**DTO**: `Client`

---

## GET /clients/{clientId}/images

**Purpose**: Get client profile image

**Service**: `ClientService.getClientImage(clientId)`

**Response**: Base64 encoded image or image URL

---

## GET /clients/{clientId}/accounts

**Purpose**: Get all accounts for a client

**Service**: `ClientService.getClientAccounts(clientId)`

**Response**:
```json
{
    "savingsAccounts": [
        {
            "id": 12345,
            "accountNo": "000000012345",
            "productName": "Basic Savings",
            "accountBalance": 1234.56,
            "status": { "id": 300, "value": "Active" }
        }
    ],
    "loanAccounts": [
        {
            "id": 67890,
            "accountNo": "000000067890",
            "productName": "Personal Loan",
            "loanBalance": 5000.00,
            "status": { "id": 300, "value": "Active" }
        }
    ],
    "shareAccounts": []
}
```

**DTO**: `ClientAccounts`

---

## Related

- Design Layer: [home/API.md](../../design-spec-layer/features/home/API.md), [accounts/API.md](../../design-spec-layer/features/accounts/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

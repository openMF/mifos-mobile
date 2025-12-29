# Home Dashboard - API Reference

---

## Endpoints Required

### 1. Get Client Details

**Endpoint**: `GET /self/clients/{clientId}`

**Purpose**: Fetch client profile information for greeting

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
    "status": { "id": 300, "value": "Active" },
    "fullname": "John Doe",
    "firstname": "John",
    "lastname": "Doe",
    "displayName": "John Doe",
    "officeId": 1,
    "officeName": "Head Office"
}
```

**Kotlin DTO**: Uses `Client` from `core/model/entity/client/`

**Status**: ✅ Implemented in `ClientService`

---

### 2. Get Client Accounts

**Endpoint**: `GET /self/clients/{clientId}/accounts`

**Purpose**: Fetch all accounts (savings, loans, shares) for balance calculation

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
{
    "savingsAccounts": [
        {
            "id": 1,
            "accountNo": "000000001",
            "productName": "Savings",
            "status": { "id": 300, "value": "Active" },
            "currency": { "code": "USD", "displaySymbol": "$", "decimalPlaces": 2 },
            "accountBalance": 1000.00
        }
    ],
    "loanAccounts": [
        {
            "id": 1,
            "accountNo": "000000002",
            "productName": "Loan",
            "status": { "id": 300, "value": "Active" },
            "currency": { "code": "USD", "displaySymbol": "$", "decimalPlaces": 2 },
            "loanBalance": 5000.00
        }
    ],
    "shareAccounts": []
}
```

**Kotlin DTO**: Uses `ClientAccounts` from `core/model/entity/client/`

**Status**: ✅ Implemented in `ClientService`

---

### 3. Get Client Image

**Endpoint**: `GET /self/clients/{clientId}/images`

**Purpose**: Fetch client profile image

**Response**: Binary image data or 404

**Status**: ✅ Implemented in `ClientService`

---

### 4. Get Notifications Count

**Endpoint**: `GET /self/notifications`

**Purpose**: Fetch unread notification count

**Response**:
```json
{
    "totalFilteredRecords": 5,
    "pageItems": [...]
}
```

**Status**: ✅ Implemented in `NotificationService`

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| `/clients/{id}` | ClientService | HomeRepository | ✅ |
| `/clients/{id}/accounts` | ClientService | HomeRepository | ✅ |
| `/clients/{id}/images` | ClientService | HomeRepository | ✅ |
| `/notifications` | NotificationService | HomeRepository | ✅ |

---

## Kotlin Implementation

### Service (ClientService.kt)

```kotlin
interface ClientService {
    @GET(ApiEndPoints.CLIENTS)
    fun clients(): Flow<Page<Client>>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    fun getClientForId(@Path("clientId") clientId: Long): Flow<Client>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    fun getClientImage(@Path("clientId") clientId: Long): Flow<HttpResponse>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getClientAccounts(@Path("clientId") clientId: Long): Flow<ClientAccounts>
}
```

### Repository (HomeRepository.kt)

```kotlin
interface HomeRepository {
    fun clientAccounts(clientId: Long): Flow<DataState<ClientAccounts>>
    fun currentClient(clientId: Long): Flow<DataState<Client>>
    fun clientImage(clientId: Long): Flow<DataState<String>>
    fun unreadNotificationsCount(): Flow<DataState<Int>>
}
```

---

## Notes

- Balance calculation happens client-side by summing all account balances
- Currency symbol taken from first account with balance
- Image endpoint returns 404 if no image set - handle gracefully

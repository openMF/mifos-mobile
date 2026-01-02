# Fineract Self-Service API Quick Reference

> **Base URL**: `https://{server}/fineract-provider/api/v1/self/`
> **Authentication**: Basic Auth + Tenant Header
> **Last Updated**: 2025-12-26

---

## Authentication Headers

```
Authorization: Basic {base64(username:password)}
Fineract-Platform-TenantId: {tenant}
Content-Type: application/json
```

---

## API Endpoints

### Authentication

| Endpoint | Method | Description |
|----------|--------|-------------|
| `authentication` | POST | User login |
| `registration` | POST | User registration |
| `registration/user` | POST | Verify registration |
| `user` | GET | Get current user details |
| `user` | PUT | Update user details |

### Clients

| Endpoint | Method | Description |
|----------|--------|-------------|
| `clients` | GET | Get client list |
| `clients/{clientId}` | GET | Get client by ID |
| `clients/{clientId}/images` | GET | Get client image |
| `clients/{clientId}/accounts` | GET | Get client accounts |
| `clients/{clientId}/charges` | GET | Get client charges |

### Savings Accounts

| Endpoint | Method | Description |
|----------|--------|-------------|
| `savingsaccounts` | GET | Get savings accounts list |
| `savingsaccounts` | POST | Apply for savings account |
| `savingsaccounts/{accountId}` | GET | Get savings account details |
| `savingsaccounts/{accountId}` | PUT | Update savings account |
| `savingsaccounts/{accountId}?command=withdrawnByApplicant` | POST | Withdraw application |
| `savingsaccounts/template` | GET | Get savings account template |
| `savingsaccounts/{accountId}/transactions` | GET | Get transactions |

### Loan Accounts

| Endpoint | Method | Description |
|----------|--------|-------------|
| `loans` | GET | Get loan accounts list |
| `loans` | POST | Apply for loan |
| `loans/{loanId}` | GET | Get loan details |
| `loans/{loanId}` | PUT | Update loan |
| `loans/{loanId}?command=withdrawnByApplicant` | POST | Withdraw loan application |
| `loans/template` | GET | Get loan template |
| `loans/{loanId}/transactions` | GET | Get loan transactions |
| `loans/{loanId}/guarantors` | GET | Get guarantors |
| `loans/{loanId}/guarantors` | POST | Add guarantor |
| `loans/{loanId}/guarantors/{guarantorId}` | DELETE | Remove guarantor |

### Share Accounts

| Endpoint | Method | Description |
|----------|--------|-------------|
| `shareaccounts` | GET | Get share accounts list |
| `shareaccounts` | POST | Apply for shares |
| `shareaccounts/{accountId}` | GET | Get share account details |

### Beneficiaries

| Endpoint | Method | Description |
|----------|--------|-------------|
| `beneficiaries/tpt` | GET | Get beneficiary list |
| `beneficiaries/tpt` | POST | Create beneficiary |
| `beneficiaries/tpt/template` | GET | Get beneficiary template |
| `beneficiaries/tpt/{beneficiaryId}` | PUT | Update beneficiary |
| `beneficiaries/tpt/{beneficiaryId}` | DELETE | Delete beneficiary |

### Transfers

| Endpoint | Method | Description |
|----------|--------|-------------|
| `accounttransfers` | POST | Make transfer |
| `accounttransfers/template` | GET | Get transfer template |

### Notifications

| Endpoint | Method | Description |
|----------|--------|-------------|
| `notifications` | GET | Get notifications |
| `notifications` | PUT | Mark as read |

### Products

| Endpoint | Method | Description |
|----------|--------|-------------|
| `products/savings` | GET | Get savings products |
| `products/loans` | GET | Get loan products |
| `products/share` | GET | Get share products |

---

## Common Query Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| `associations` | Include related data | `all`, `transactions`, `charges` |
| `fields` | Limit response fields | `id,name,status` |
| `offset` | Pagination offset | `0` |
| `limit` | Pagination limit | `20` |

---

## Response Patterns

### Single Entity
```json
{
    "id": 1,
    "accountNo": "000000001",
    "status": {
        "id": 300,
        "code": "savingsAccountStatusType.active",
        "value": "Active"
    }
}
```

### List (Paginated)
```json
{
    "totalFilteredRecords": 100,
    "pageItems": [
        { "id": 1, "name": "Item 1" },
        { "id": 2, "name": "Item 2" }
    ]
}
```

### Error
```json
{
    "developerMessage": "Error details",
    "httpStatusCode": "400",
    "defaultUserMessage": "User friendly message",
    "userMessageGlobalisationCode": "error.msg.code",
    "errors": []
}
```

---

## Kotlin Service Constants

```kotlin
// core/network/utils/ApiEndPoints.kt
object ApiEndPoints {
    const val AUTHENTICATION = "authentication"
    const val CLIENTS = "clients"
    const val SAVINGS_ACCOUNTS = "savingsaccounts"
    const val SHARE_ACCOUNTS = "shareaccounts"
    const val LOANS = "loans"
    const val BENEFICIARIES = "beneficiaries"
    const val ACCOUNT_TRANSFER = "accounttransfers"
    const val REGISTRATION = "registration"
    const val USER = "user"
    const val PRODUCTS = "products"
    const val DEVICE = "device"
}
```

---

## Status Codes Reference

### Savings Account Status
| Code | Value |
|------|-------|
| 100 | Submitted and pending approval |
| 200 | Approved |
| 300 | Active |
| 400 | Withdrawn by applicant |
| 500 | Rejected |
| 600 | Closed |

### Loan Account Status
| Code | Value |
|------|-------|
| 100 | Submitted and pending approval |
| 200 | Approved |
| 300 | Active |
| 400 | Withdrawn by applicant |
| 500 | Rejected |
| 600 | Closed (obligations met) |
| 700 | Overpaid |

---

## Demo Credentials

For testing:
- **Instance**: `gsoc.mifos.community`
- **Username**: `maria`
- **Password**: `password`
- **Tenant**: `default`

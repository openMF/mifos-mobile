# Fineract Self-Service API Documentation

> **Server**: Apache Fineract (External - not managed by this project)
> **API Base**: `https://{server}/fineract-provider/api/v1/self/`
> **Documentation**: https://sandbox.mifos.community/fineract-provider/swagger-ui/index.html
> **Last Updated**: 2025-12-26

---

## Overview

Mifos Mobile consumes the Apache Fineract Self-Service API. Unlike projects with custom backends (e.g., Supabase), we don't create or modify server-side code. This document serves as a reference for available endpoints.

**Key Point**: All endpoints are prefixed with `/self/` indicating they are self-service APIs for end-users (not back-office operations).

---

## Authentication

### POST `/authentication`

Authenticate a user and get access token.

**Request**:
```json
{
    "username": "string",
    "password": "string"
}
```

**Response**:
```json
{
    "username": "string",
    "userId": 1,
    "base64EncodedAuthenticationKey": "string",
    "authenticated": true,
    "officeId": 1,
    "officeName": "string",
    "roles": [],
    "permissions": [],
    "clients": [1],
    "isSelfServiceUser": true
}
```

**Kotlin**:
```kotlin
interface AuthenticationService {
    @POST(ApiEndPoints.AUTHENTICATION)
    suspend fun authenticate(@Body loginPayload: LoginPayload): User
}
```

---

## Client APIs

### GET `/clients`

Get list of clients for the authenticated user.

**Response**:
```json
{
    "totalFilteredRecords": 1,
    "pageItems": [
        {
            "id": 1,
            "accountNo": "000000001",
            "status": { "id": 300, "code": "clientStatusType.active", "value": "Active" },
            "fullname": "John Doe",
            "displayName": "John Doe",
            "officeId": 1,
            "officeName": "Head Office"
        }
    ]
}
```

### GET `/clients/{clientId}`

Get specific client details.

### GET `/clients/{clientId}/accounts`

Get all accounts (savings, loans, shares) for a client.

**Query Parameters**:
- `fields`: Filter response fields (e.g., `savingsAccounts,loanAccounts`)

**Response**:
```json
{
    "savingsAccounts": [
        {
            "id": 1,
            "accountNo": "000000001",
            "productId": 1,
            "productName": "Savings Product",
            "status": { "id": 300, "value": "Active" },
            "currency": { "code": "USD", "displaySymbol": "$", "decimalPlaces": 2 },
            "accountBalance": 1000.00
        }
    ],
    "loanAccounts": [],
    "shareAccounts": []
}
```

### GET `/clients/{clientId}/images`

Get client profile image.

**Response**: Image data or HTTP 404 if not found.

### GET `/clients/{clientId}/charges`

Get charges associated with the client.

---

## Savings Account APIs

### GET `/savingsaccounts/{accountId}`

Get savings account details.

**Query Parameters**:
- `associations`: `all`, `transactions`, `charges`

**Response**:
```json
{
    "id": 1,
    "accountNo": "000000001",
    "clientId": 1,
    "clientName": "John Doe",
    "savingsProductId": 1,
    "savingsProductName": "Savings Product",
    "status": { "id": 300, "value": "Active" },
    "currency": { "code": "USD", "displaySymbol": "$" },
    "accountBalance": 1000.00,
    "summary": {
        "totalDeposits": 5000.00,
        "totalWithdrawals": 4000.00,
        "availableBalance": 1000.00
    },
    "transactions": []
}
```

### GET `/savingsaccounts/template`

Get template for applying for savings account.

**Query Parameters**:
- `clientId`: Required
- `productId`: Optional (for specific product template)

### POST `/savingsaccounts`

Apply for a new savings account.

**Request**:
```json
{
    "clientId": 1,
    "productId": 1,
    "locale": "en",
    "dateFormat": "dd MMMM yyyy",
    "submittedOnDate": "01 January 2025",
    "nominalAnnualInterestRate": 5.0,
    "interestCompoundingPeriodType": 1,
    "interestPostingPeriodType": 4,
    "interestCalculationType": 1,
    "interestCalculationDaysInYearType": 365
}
```

### PUT `/savingsaccounts/{accountId}`

Update a pending savings account application.

### POST `/savingsaccounts/{accountId}?command=withdrawnByApplicant`

Withdraw savings account application.

**Request**:
```json
{
    "locale": "en",
    "dateFormat": "dd MMMM yyyy",
    "withdrawnOnDate": "01 January 2025",
    "note": "Withdrawal reason"
}
```

---

## Loan Account APIs

### GET `/loans/{loanId}`

Get loan account details.

**Query Parameters**:
- `associations`: `all`, `repaymentSchedule`, `transactions`, `charges`, `guarantors`

### GET `/loans/template`

Get template for applying for loan.

**Query Parameters**:
- `clientId`: Required
- `productId`: Optional
- `templateType`: `individual`

### POST `/loans`

Apply for a new loan.

**Request**:
```json
{
    "clientId": 1,
    "productId": 1,
    "principal": 10000.00,
    "loanTermFrequency": 12,
    "loanTermFrequencyType": 2,
    "numberOfRepayments": 12,
    "repaymentEvery": 1,
    "repaymentFrequencyType": 2,
    "interestRatePerPeriod": 1.5,
    "amortizationType": 1,
    "interestType": 0,
    "interestCalculationPeriodType": 1,
    "transactionProcessingStrategyCode": "mifos-standard-strategy",
    "expectedDisbursementDate": "01 January 2025",
    "submittedOnDate": "01 January 2025",
    "locale": "en",
    "dateFormat": "dd MMMM yyyy"
}
```

### PUT `/loans/{loanId}`

Update a pending loan application.

### POST `/loans/{loanId}?command=withdrawnByApplicant`

Withdraw loan application.

### GET `/loans/{loanId}/guarantors`

Get loan guarantors.

### POST `/loans/{loanId}/guarantors`

Add a guarantor to loan.

### DELETE `/loans/{loanId}/guarantors/{guarantorId}`

Remove guarantor from loan.

---

## Beneficiary APIs

### GET `/beneficiaries/tpt`

Get list of third-party transfer beneficiaries.

**Response**:
```json
[
    {
        "id": 1,
        "name": "Jane Doe",
        "officeName": "Head Office",
        "clientName": "Jane Doe",
        "accountType": { "id": 2, "value": "Savings" },
        "accountNumber": "000000002",
        "transferLimit": 10000.00
    }
]
```

### GET `/beneficiaries/tpt/template`

Get template for creating beneficiary.

### POST `/beneficiaries/tpt`

Create a new beneficiary.

**Request**:
```json
{
    "locale": "en",
    "name": "Jane Doe",
    "accountNumber": "000000002",
    "accountType": 2,
    "transferLimit": 10000.00
}
```

### PUT `/beneficiaries/tpt/{beneficiaryId}`

Update beneficiary.

### DELETE `/beneficiaries/tpt/{beneficiaryId}`

Delete beneficiary.

---

## Transfer APIs

### GET `/accounttransfers/template`

Get template for account transfer.

**Query Parameters**:
- `fromAccountId`: Source account ID
- `fromAccountType`: 1 (Loan) or 2 (Savings)

### POST `/accounttransfers`

Make an account transfer.

**Request**:
```json
{
    "fromOfficeId": 1,
    "fromClientId": 1,
    "fromAccountType": 2,
    "fromAccountId": 1,
    "toOfficeId": 1,
    "toClientId": 2,
    "toAccountType": 2,
    "toAccountId": 2,
    "dateFormat": "dd MMMM yyyy",
    "locale": "en",
    "transferDate": "01 January 2025",
    "transferAmount": 100.00,
    "transferDescription": "Transfer to savings"
}
```

---

## Share Account APIs

### GET `/shareaccounts/{accountId}`

Get share account details.

### GET `/shareaccounts/template`

Get template for applying for shares.

### POST `/shareaccounts`

Apply for share account.

---

## User APIs

### GET `/user`

Get current user details.

### PUT `/user`

Update user profile.

**Request**:
```json
{
    "username": "string",
    "firstname": "string",
    "lastname": "string",
    "email": "string"
}
```

---

## Registration APIs

### POST `/registration`

Self-service user registration.

**Request**:
```json
{
    "username": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "mobileNumber": "string",
    "accountNumber": "string",
    "password": "string",
    "authenticationMode": "email"
}
```

### POST `/registration/user`

Verify registration.

**Request**:
```json
{
    "requestId": "string",
    "authenticationToken": "string"
}
```

---

## Error Responses

All error responses follow this format:

```json
{
    "developerMessage": "Detailed error for developers",
    "httpStatusCode": "400",
    "defaultUserMessage": "User-friendly error message",
    "userMessageGlobalisationCode": "error.msg.code",
    "errors": [
        {
            "developerMessage": "Field-specific error",
            "defaultUserMessage": "User message",
            "userMessageGlobalisationCode": "error.msg.field.code",
            "parameterName": "fieldName"
        }
    ]
}
```

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request - Validation error |
| 401 | Unauthorized - Invalid credentials |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## Notes for Implementation

1. **Date Format**: Always use `dd MMMM yyyy` (e.g., "01 January 2025")
2. **Locale**: Always include `locale: "en"` in requests
3. **Tenant Header**: Required for all requests
4. **Authentication**: Use Basic Auth with base64 encoded credentials
5. **Associations**: Use `associations=all` to get complete data in one call

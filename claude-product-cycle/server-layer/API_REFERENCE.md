# Fineract Self-Service API Reference

> **Purpose**: Complete endpoint documentation for client layer implementation
> **Derived from**: Design layer `features/*/API.md` files
> **Swagger UI**: [sandbox.mifos.community/swagger-ui](https://sandbox.mifos.community/fineract-provider/swagger-ui/index.html#/)

---

## Table of Contents
1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Client](#client)
4. [Accounts](#accounts)
5. [Savings Account](#savings-account)
6. [Loan Account](#loan-account)
7. [Share Account](#share-account)
8. [Beneficiary](#beneficiary)
9. [Transfer](#transfer)
10. [Guarantor](#guarantor)
11. [Notification](#notification)
12. [Charges](#charges)
13. [User Settings](#user-settings)

---

## Overview

### Base URL
```
https://{server}/fineract-provider/api/v1/self/
```

### Servers
| Environment | Server |
|-------------|--------|
| Sandbox | `sandbox.mifos.community` |
| Demo | `tt.mifos.community` or `gsoc.mifos.community` |

### Headers (All Requests)
```
Authorization: Basic {base64(username:password)}
Fineract-Platform-TenantId: {tenant}
Content-Type: application/json
```

### Common Query Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `locale` | String | Response locale (default: "en") |
| `dateFormat` | String | Date format (default: "dd MMMM yyyy") |
| `associations` | String | Include related data |

---

## Authentication

### POST /authentication
**Purpose**: Login with username and password

**Service**: `AuthenticationService.authenticate()`

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
    "userId": 123,
    "username": "john_doe",
    "clients": [456],
    "isAuthenticated": true,
    "base64EncodedAuthenticationKey": "encoded_key",
    "officeName": "Head Office"
}
```

**DTO**: `User`
```kotlin
@Serializable
data class User(
    val userId: Long,
    val username: String?,
    val clients: List<Long>,
    val isAuthenticated: Boolean,
    val base64EncodedAuthenticationKey: String?,
    val officeName: String?,
)
```

---

### POST /registration
**Purpose**: Register new client

**Service**: `RegistrationService.register()`

**Request**:
```json
{
    "accountNumber": "string",
    "authenticationMode": "email",
    "email": "user@example.com",
    "firstName": "John",
    "middleName": "M",
    "lastName": "Doe",
    "mobileNumber": "1234567890",
    "password": "securePassword123",
    "username": "john_doe"
}
```

**Response**:
```json
{
    "requestId": "12345"
}
```

**DTO**: `RegisterPayload`

---

### POST /registration/user
**Purpose**: Verify OTP for registration

**Service**: `RegistrationService.verifyOtp()`

**Request**:
```json
{
    "authenticationToken": "123456",
    "requestId": "12345"
}
```

**Response**:
```json
{
    "message": "User verified successfully"
}
```

---

## Client

### GET /clients
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

### GET /clients/{clientId}
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

### GET /clients/{clientId}/images
**Purpose**: Get client profile image

**Service**: `ClientService.getClientImage(clientId)`

**Response**: Base64 encoded image or image URL

---

### GET /clients/{clientId}/accounts
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

## Savings Account

### GET /savingsaccounts/{accountId}
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

### GET /savingsaccounts/template
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

### POST /savingsaccounts
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

### PUT /savingsaccounts/{accountId}
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

### POST /savingsaccounts/{savingsId}?command=withdrawnByApplicant
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

## Loan Account

### GET /loans/{loanId}
**Purpose**: Get loan account details with associations

**Service**: `LoanService.getLoanWithAssociations(loanId, associations)`

**Query Parameters**:
| Parameter | Values |
|-----------|--------|
| `associations` | `transactions`, `repaymentSchedule`, `charges` |

**Response**:
```json
{
    "id": 67890,
    "accountNo": "000000067890",
    "clientId": 1,
    "clientName": "John Doe",
    "loanProductId": 1,
    "loanProductName": "Personal Loan",
    "principal": 10000.00,
    "status": { "id": 300, "value": "Active" },
    "summary": {
        "principalDisbursed": 10000.00,
        "principalPaid": 5000.00,
        "principalOutstanding": 5000.00,
        "interestCharged": 500.00,
        "totalExpectedRepayment": 10500.00
    },
    "repaymentSchedule": {...},
    "transactions": [...]
}
```

**DTO**: `LoanWithAssociations`

---

### GET /loanproducts
**Purpose**: Get available loan products

**Service**: `LoanService.getLoanProducts()`

**Response**:
```json
[
    {
        "id": 1,
        "name": "Personal Loan",
        "principal": 10000.00,
        "interestRatePerPeriod": 12.0
    }
]
```

**DTO**: `List<LoanProduct>`

---

### POST /loans
**Purpose**: Submit loan application

**Service**: `LoanService.createLoansAccount(payload)`

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
    "interestRatePerPeriod": 12.0,
    "expectedDisbursementDate": "29 December 2025",
    "submittedOnDate": "29 December 2025",
    "locale": "en",
    "dateFormat": "dd MMMM yyyy"
}
```

**DTO**: `LoansPayload`

---

### POST /loans/{loanId}?command=withdrawnByApplicant
**Purpose**: Withdraw loan application

**Service**: `LoanService.withdrawLoanAccount(loanId, payload)`

---

### GET /loans/{loanId}/template?templateType=repayment
**Purpose**: Get loan repayment template

**Service**: `LoanService.getLoanRepaymentTemplate(loanId)`

---

### GET /loans/{loanId}/transactions/{transactionId}
**Purpose**: Get loan transaction details

**Service**: `LoanService.getLoanAccountTransaction(loanId, transactionId)`

---

## Share Account

### GET /products/share
**Purpose**: Get available share products

**Service**: `ShareAccountService.getShareProducts()`

**Response**:
```json
[
    {
        "id": 1,
        "name": "Common Shares",
        "shortName": "CS",
        "unitPrice": 100.00,
        "currency": { "code": "USD", "displaySymbol": "$" }
    }
]
```

---

### POST /shareaccounts
**Purpose**: Submit share account application

**Service**: `ShareAccountService.submitShareApplication(payload)`

---

### GET /shareaccounts/{accountId}
**Purpose**: Get share account details

**Service**: `ShareAccountService.getShareAccountDetails(accountId)`

---

## Beneficiary

### GET /beneficiaries/tpt
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

**DTO**: `Beneficiary`

---

### GET /beneficiaries/tpt/template
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

### POST /beneficiaries/tpt
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

---

### PUT /beneficiaries/tpt/{beneficiaryId}
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

---

### DELETE /beneficiaries/tpt/{beneficiaryId}
**Purpose**: Delete beneficiary

**Service**: `BeneficiaryService.deleteBeneficiary(beneficiaryId)`

---

## Transfer

### GET /accounttransfers/template
**Purpose**: Get transfer template with account options

**Service**: `SavingAccountsListService.accountTransferTemplate(fromAccountId, fromAccountType)`

**Query Parameters**:
| Parameter | Type |
|-----------|------|
| `fromAccountId` | Long |
| `fromAccountType` | Long (2 = Savings) |

**Response**:
```json
{
    "fromAccountTypeOptions": [...],
    "toAccountTypeOptions": [...],
    "fromAccountOptions": [
        {
            "accountId": 12345,
            "accountNo": "000000012345",
            "accountType": { "id": 2, "value": "Savings Account" },
            "clientId": 1,
            "clientName": "John Doe"
        }
    ],
    "toAccountOptions": [...]
}
```

**DTO**: `AccountOptionsTemplate`

---

### POST /accounttransfers
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

**DTO**: `TransferPayload`

---

## Guarantor

### GET /loans/{loanId}/guarantors
**Purpose**: Get list of guarantors for a loan

**Service**: `GuarantorService.getGuarantorList(loanId)`

---

### GET /loans/{loanId}/guarantors/template
**Purpose**: Get guarantor template

**Service**: `GuarantorService.getGuarantorTemplate(loanId)`

---

### POST /loans/{loanId}/guarantors
**Purpose**: Add guarantor to loan

**Service**: `GuarantorService.addGuarantor(loanId, payload)`

---

### PUT /loans/{loanId}/guarantors/{guarantorId}
**Purpose**: Update guarantor

**Service**: `GuarantorService.updateGuarantor(loanId, guarantorId, payload)`

---

### DELETE /loans/{loanId}/guarantors/{guarantorId}
**Purpose**: Delete guarantor

**Service**: `GuarantorService.deleteGuarantor(loanId, guarantorId)`

---

## Notification

### GET /device/registration/client/{clientId}
**Purpose**: Get notification registration ID

**Service**: `NotificationService.getUserNotificationId(clientId)`

---

### POST /device/registration
**Purpose**: Register device for notifications

**Service**: `NotificationService.registerNotification(payload)`

**Request**:
```json
{
    "clientId": 1,
    "registrationId": "fcm_token_here"
}
```

---

### PUT /device/registration/{id}
**Purpose**: Update notification registration

**Service**: `NotificationService.updateRegisterNotification(id, payload)`

---

## Charges

### GET /clients/{clientId}/charges
**Purpose**: Get client charges

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

---

### GET /loans/{loanId}/charges
**Purpose**: Get loan charges

**Service**: `ClientChargeService.getChargeList("loans", loanId)`

---

### GET /savingsaccounts/{accountId}/charges
**Purpose**: Get savings account charges

**Service**: `ClientChargeService.getChargeList("savingsaccounts", accountId)`

---

## User Settings

### PUT /user/password
**Purpose**: Update user password

**Service**: `UserService.updatePassword(payload)`

**Request**:
```json
{
    "newPassword": "newSecurePassword123",
    "confirmPassword": "newSecurePassword123"
}
```

---

## Local-Only Features

These features do not require API calls:

| Feature | Storage | Notes |
|---------|---------|-------|
| QR Code | - | Local generation/scanning |
| Location | Static | Hardcoded branch locations |
| Passcode | DataStore | Local biometric/PIN |

---

## Error Response Format

All endpoints return errors in this format:

```json
{
    "developerMessage": "Detailed error for debugging",
    "httpStatusCode": "400",
    "defaultUserMessage": "User-friendly message",
    "userMessageGlobalisationCode": "error.msg.code",
    "errors": []
}
```

See [ERROR_HANDLING.md](ERROR_HANDLING.md) for complete error handling guide.

---

## Related Files

- Quick Lookup: [API_INDEX.md](API_INDEX.md)
- Client Patterns: [CLIENT_PATTERNS.md](CLIENT_PATTERNS.md)
- Error Handling: [ERROR_HANDLING.md](ERROR_HANDLING.md)
- Design Layer: `design-spec-layer/features/*/API.md`

---

## Changelog

| Date | Change |
|------|--------|
| 2025-01-05 | Created - aggregated from design layer API.md files |

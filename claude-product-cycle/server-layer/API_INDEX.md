# Fineract Self-Service API Index

> **Purpose**: Fast API lookup with service method references
> **Pattern**: Static table first → API_REFERENCE.md for details → Design layer for source

---

## Source of Truth Hierarchy

```
┌─────────────────────────────────────────────────────────────────┐
│  Design Layer: features/*/API.md                                │
│  └─→ ULTIMATE SOURCE OF TRUTH                                   │
│  └─→ Where APIs are first designed/documented                   │
├─────────────────────────────────────────────────────────────────┤
│  Server Layer: (This directory)                                 │
│  └─→ DERIVED but COMPLETE for client layer                      │
│  ├─→ API_INDEX.md (this file) - Quick lookup                   │
│  ├─→ API_REFERENCE.md - Complete endpoint details              │
│  ├─→ CLIENT_PATTERNS.md - Service/Repository patterns          │
│  └─→ ERROR_HANDLING.md - Exception handling                    │
├─────────────────────────────────────────────────────────────────┤
│  Client Layer: core/network/, core/data/                        │
│  └─→ IMPLEMENTATION based on server layer docs                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Table of Contents
1. [Overview](#overview)
2. [Quick Lookup](#quick-lookup)
3. [By Category](#by-category)
4. [Common Patterns](#common-patterns)
5. [Lookup Strategy](#lookup-strategy)
6. [Update Flow](#update-flow)
7. [How to Add New API](#how-to-add-new-api)

---

## Overview

### Server Layer Documentation

| Document | Purpose |
|----------|---------|
| [API_INDEX.md](API_INDEX.md) | Quick lookup table (this file) |
| [endpoints/*.md](endpoints/) | **O(1) Lookup** - Category-specific endpoint docs |
| [API_REFERENCE.md](API_REFERENCE.md) | Complete endpoint overview |
| [CLIENT_PATTERNS.md](CLIENT_PATTERNS.md) | Service/Repository implementation patterns |
| [ERROR_HANDLING.md](ERROR_HANDLING.md) | Exception types and error extraction |

### Endpoint Files (O(1) Lookup)

| File | Category | Endpoints |
|------|----------|:---------:|
| [AUTH.md](endpoints/AUTH.md) | Authentication | 3 |
| [CLIENT.md](endpoints/CLIENT.md) | Client | 4 |
| [SAVINGS.md](endpoints/SAVINGS.md) | Savings Account | 7 |
| [LOAN.md](endpoints/LOAN.md) | Loan Account | 6 |
| [BENEFICIARY.md](endpoints/BENEFICIARY.md) | Beneficiary | 5 |
| [TRANSFER.md](endpoints/TRANSFER.md) | Transfer | 2 |
| [GUARANTOR.md](endpoints/GUARANTOR.md) | Guarantor | 5 |
| [NOTIFICATION.md](endpoints/NOTIFICATION.md) | Notification | 3 |
| [CHARGES.md](endpoints/CHARGES.md) | Charges | 3 |
| [SHARE.md](endpoints/SHARE.md) | Share Account | 3 |
| [USER.md](endpoints/USER.md) | User Settings | 1 |

### External References

| Resource | URL |
|----------|-----|
| **Swagger UI** | [sandbox.mifos.community/fineract-provider/swagger-ui](https://sandbox.mifos.community/fineract-provider/swagger-ui/index.html#/) |
| **Self-Service APIs** | Filter by `/self/` endpoints in Swagger |
| **Full API Docs** | [fineract.apache.org](https://fineract.apache.org/) |

### Base URL
```
https://{server}/fineract-provider/api/v1/self/
```

### Authentication
All requests require:
```
Headers:
  Authorization: Basic {base64(username:password)}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json
```

### Demo Server
- **Server**: `tt.mifos.community` or `gsoc.mifos.community`
- **Tenant**: `mobile` or `default`
- **Test User**: `maria` / `password`

---

## Quick Lookup

| Endpoint | Method | Purpose | Service Method | Docs |
|----------|--------|---------|----------------|------|
| `/authentication` | POST | Login user | `authenticate()` | [auth](../design-spec-layer/features/auth/API.md) |
| `/registration` | POST | Register client | `register()` | [auth](../design-spec-layer/features/auth/API.md) |
| `/registration/user` | POST | Verify OTP | `verifyOtp()` | [auth](../design-spec-layer/features/auth/API.md) |
| `/clients` | GET | Get client list | `clients()` | [home](../design-spec-layer/features/home/API.md) |
| `/clients/{clientId}` | GET | Get client details | `getClientDetails()` | [home](../design-spec-layer/features/home/API.md) |
| `/clients/{clientId}/images` | GET | Get client image | `getClientImage()` | [home](../design-spec-layer/features/home/API.md) |
| `/clients/{clientId}/accounts` | GET | Get all accounts | `getClientAccounts()` | [accounts](../design-spec-layer/features/accounts/API.md) |
| `/savingsaccounts/{accountId}` | GET | Savings details | `getSavingsWithAssociations()` | [savings](../design-spec-layer/features/savings-account/API.md) |
| `/savingsaccounts` | POST | Apply for savings | `submitSavingAccountApplication()` | [savings](../design-spec-layer/features/savings-account/API.md) |
| `/savingsaccounts/{id}?command=withdrawnByApplicant` | POST | Withdraw application | `submitWithdrawSavingsAccount()` | [savings](../design-spec-layer/features/savings-account/API.md) |
| `/savingsaccounts/{id}/transactions` | GET | Savings transactions | `getSavingsAccountTransactionTemplate()` | [savings](../design-spec-layer/features/savings-account/API.md) |
| `/savingsproducts` | GET | Savings products | `getSavingsProducts()` | [savings](../design-spec-layer/features/savings-account/API.md) |
| `/loans/{loanId}` | GET | Loan details | `getLoanWithAssociations()` | [loan](../design-spec-layer/features/loan-account/API.md) |
| `/loans` | POST | Apply for loan | `createLoansAccount()` | [loan](../design-spec-layer/features/loan-account/API.md) |
| `/loans/{loanId}?command=withdrawnByApplicant` | POST | Withdraw loan | `withdrawLoanAccount()` | [loan](../design-spec-layer/features/loan-account/API.md) |
| `/loans/{loanId}/transactions/{transId}` | GET | Loan transaction | `getLoanAccountTransaction()` | [loan](../design-spec-layer/features/loan-account/API.md) |
| `/loanproducts` | GET | Loan products | `getLoanProducts()` | [loan](../design-spec-layer/features/loan-account/API.md) |
| `/loans/{loanId}/template?templateType=repayment` | GET | Repayment template | `getLoanRepaymentTemplate()` | [loan](../design-spec-layer/features/loan-account/API.md) |
| `/products/share` | GET | Share products | `getShareProducts()` | [share](../design-spec-layer/features/share-account/API.md) |
| `/shareaccounts` | POST | Apply for shares | `submitShareApplication()` | [share](../design-spec-layer/features/share-account/API.md) |
| `/shareaccounts/{accountId}` | GET | Share details | `getShareAccountDetails()` | [share](../design-spec-layer/features/share-account/API.md) |
| `/beneficiaries/tpt` | GET | List beneficiaries | `beneficiaryList()` | [beneficiary](../design-spec-layer/features/beneficiary/API.md) |
| `/beneficiaries/tpt` | POST | Add beneficiary | `createBeneficiary()` | [beneficiary](../design-spec-layer/features/beneficiary/API.md) |
| `/beneficiaries/tpt/{id}` | PUT | Update beneficiary | `updateBeneficiary()` | [beneficiary](../design-spec-layer/features/beneficiary/API.md) |
| `/beneficiaries/tpt/{id}` | DELETE | Delete beneficiary | `deleteBeneficiary()` | [beneficiary](../design-spec-layer/features/beneficiary/API.md) |
| `/beneficiaries/tpt/template` | GET | Beneficiary template | `beneficiaryTemplate()` | [beneficiary](../design-spec-layer/features/beneficiary/API.md) |
| `/accounttransfers/template` | GET | Transfer template | `accountTransferTemplate()` | [transfer](../design-spec-layer/features/transfer/API.md) |
| `/accounttransfers` | POST | Execute transfer | `makeTransfer()` | [transfer](../design-spec-layer/features/transfer/API.md) |
| `/loans/{loanId}/guarantors` | GET | List guarantors | `getGuarantorList()` | [guarantor](../design-spec-layer/features/guarantor/API.md) |
| `/loans/{loanId}/guarantors/template` | GET | Guarantor template | `getGuarantorTemplate()` | [guarantor](../design-spec-layer/features/guarantor/API.md) |
| `/loans/{loanId}/guarantors` | POST | Add guarantor | `addGuarantor()` | [guarantor](../design-spec-layer/features/guarantor/API.md) |
| `/loans/{loanId}/guarantors/{id}` | PUT | Update guarantor | `updateGuarantor()` | [guarantor](../design-spec-layer/features/guarantor/API.md) |
| `/loans/{loanId}/guarantors/{id}` | DELETE | Delete guarantor | `deleteGuarantor()` | [guarantor](../design-spec-layer/features/guarantor/API.md) |
| `/device/registration/client/{clientId}` | GET | Get notification ID | `getUserNotificationId()` | [notification](../design-spec-layer/features/notification/API.md) |
| `/device/registration` | POST | Register device | `registerNotification()` | [notification](../design-spec-layer/features/notification/API.md) |
| `/device/registration/{id}` | PUT | Update registration | `updateRegisterNotification()` | [notification](../design-spec-layer/features/notification/API.md) |
| `/clients/{clientId}/charges` | GET | Client charges | `getClientChargeList()` | [client-charge](../design-spec-layer/features/client-charge/API.md) |
| `/loans/{loanId}/charges` | GET | Loan charges | `getChargeList()` | [client-charge](../design-spec-layer/features/client-charge/API.md) |
| `/savingsaccounts/{id}/charges` | GET | Savings charges | `getChargeList()` | [client-charge](../design-spec-layer/features/client-charge/API.md) |
| `/user/password` | PUT | Change password | `updatePassword()` | [settings](../design-spec-layer/features/settings/API.md) |

---

## By Category

### Authentication (3 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/authentication` | POST | `authenticate()` | Login with username/password |
| `/registration` | POST | `register()` | Register new client |
| `/registration/user` | POST | `verifyOtp()` | Verify OTP for registration |

**Service**: `AuthenticationService`
**Docs**: [auth/API.md](../design-spec-layer/features/auth/API.md)

---

### Client (3 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/clients` | GET | `clients()` | Get client list |
| `/clients/{clientId}` | GET | `getClientDetails()` | Get client details |
| `/clients/{clientId}/images` | GET | `getClientImage()` | Get client profile image |

**Service**: `ClientService`
**Docs**: [home/API.md](../design-spec-layer/features/home/API.md)

---

### Accounts (1 endpoint)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/clients/{clientId}/accounts` | GET | `getClientAccounts()` | Get all client accounts |

**Service**: `ClientService`
**Docs**: [accounts/API.md](../design-spec-layer/features/accounts/API.md)

---

### Savings Account (5 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/savingsaccounts/{accountId}` | GET | `getSavingsWithAssociations()` | Get savings details |
| `/savingsaccounts` | POST | `submitSavingAccountApplication()` | Apply for savings account |
| `/savingsaccounts/{id}?command=withdrawnByApplicant` | POST | `submitWithdrawSavingsAccount()` | Withdraw application |
| `/savingsaccounts/{id}/transactions` | GET | `getSavingsAccountTransactionTemplate()` | Get transaction history |
| `/savingsproducts` | GET | `getSavingsProducts()` | Get available products |

**Service**: `SavingAccountsService`
**Docs**: [savings-account/API.md](../design-spec-layer/features/savings-account/API.md)

---

### Loan Account (6 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/loans/{loanId}` | GET | `getLoanWithAssociations()` | Get loan details |
| `/loans` | POST | `createLoansAccount()` | Apply for loan |
| `/loans/{loanId}?command=withdrawnByApplicant` | POST | `withdrawLoanAccount()` | Withdraw application |
| `/loans/{loanId}/transactions/{transId}` | GET | `getLoanAccountTransaction()` | Get transaction details |
| `/loanproducts` | GET | `getLoanProducts()` | Get available products |
| `/loans/{loanId}/template?templateType=repayment` | GET | `getLoanRepaymentTemplate()` | Get repayment schedule |

**Service**: `LoanService`
**Docs**: [loan-account/API.md](../design-spec-layer/features/loan-account/API.md)

---

### Share Account (3 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/products/share` | GET | `getShareProducts()` | Get share products |
| `/shareaccounts` | POST | `submitShareApplication()` | Apply for shares |
| `/shareaccounts/{accountId}` | GET | `getShareAccountDetails()` | Get share details |

**Service**: `ShareAccountService`
**Docs**: [share-account/API.md](../design-spec-layer/features/share-account/API.md)

---

### Beneficiary (5 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/beneficiaries/tpt` | GET | `beneficiaryList()` | List all beneficiaries |
| `/beneficiaries/tpt` | POST | `createBeneficiary()` | Add beneficiary |
| `/beneficiaries/tpt/{id}` | PUT | `updateBeneficiary()` | Update beneficiary |
| `/beneficiaries/tpt/{id}` | DELETE | `deleteBeneficiary()` | Delete beneficiary |
| `/beneficiaries/tpt/template` | GET | `beneficiaryTemplate()` | Get template for adding |

**Service**: `BeneficiaryService`
**Docs**: [beneficiary/API.md](../design-spec-layer/features/beneficiary/API.md)

---

### Transfer (2 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/accounttransfers/template` | GET | `accountTransferTemplate()` | Get transfer template |
| `/accounttransfers` | POST | `makeTransfer()` | Execute transfer |

**Service**: `TransferService`
**Docs**: [transfer/API.md](../design-spec-layer/features/transfer/API.md)

---

### Guarantor (5 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/loans/{loanId}/guarantors` | GET | `getGuarantorList()` | List guarantors |
| `/loans/{loanId}/guarantors/template` | GET | `getGuarantorTemplate()` | Get add template |
| `/loans/{loanId}/guarantors` | POST | `addGuarantor()` | Add guarantor |
| `/loans/{loanId}/guarantors/{id}` | PUT | `updateGuarantor()` | Update guarantor |
| `/loans/{loanId}/guarantors/{id}` | DELETE | `deleteGuarantor()` | Delete guarantor |

**Service**: `GuarantorService`
**Docs**: [guarantor/API.md](../design-spec-layer/features/guarantor/API.md)

---

### Notification (3 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/device/registration/client/{clientId}` | GET | `getUserNotificationId()` | Get notification ID |
| `/device/registration` | POST | `registerNotification()` | Register device |
| `/device/registration/{id}` | PUT | `updateRegisterNotification()` | Update registration |

**Service**: `NotificationService`
**Docs**: [notification/API.md](../design-spec-layer/features/notification/API.md)

---

### Charges (3 endpoints)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/clients/{clientId}/charges` | GET | `getClientChargeList()` | Get client charges |
| `/loans/{loanId}/charges` | GET | `getChargeList()` | Get loan charges |
| `/savingsaccounts/{id}/charges` | GET | `getChargeList()` | Get savings charges |

**Service**: `ClientChargeService`
**Docs**: [client-charge/API.md](../design-spec-layer/features/client-charge/API.md)

---

### User Settings (1 endpoint)

| Endpoint | Method | Service Method | Purpose |
|----------|--------|----------------|---------|
| `/user/password` | PUT | `updatePassword()` | Change user password |

**Service**: `UserService`
**Docs**: [settings/API.md](../design-spec-layer/features/settings/API.md)

---

## Local-Only Features

These features do not require backend API calls:

| Feature | Storage | Notes |
|---------|---------|-------|
| QR Code | - | Local generation/scanning |
| Location | Static | Hardcoded branch locations |
| Passcode | DataStore | Local biometric/PIN storage |

---

## Common Patterns

### Date Format
```
Format: dd MMMM yyyy
Example: 15 January 2025
```

### Locale
```
locale=en
dateFormat=dd MMMM yyyy
```

### Error Responses

| Status | Description | Handling |
|--------|-------------|----------|
| 401 | Unauthorized | Redirect to login |
| 403 | Forbidden | Show permission error |
| 404 | Not found | Show "not found" message |
| 500 | Server error | Show generic error with retry |

### Error Response Format
```json
{
    "developerMessage": "...",
    "httpStatusCode": "...",
    "defaultUserMessage": "...",
    "userMessageGlobalisationCode": "...",
    "errors": []
}
```

### Query Parameters

Common query parameters used across endpoints:

| Parameter | Usage | Example |
|-----------|-------|---------|
| `associations` | Include related data | `?associations=transactions` |
| `command` | Execute command | `?command=withdrawnByApplicant` |
| `templateType` | Specify template | `?templateType=repayment` |
| `locale` | Response locale | `?locale=en` |
| `dateFormat` | Date format | `?dateFormat=dd MMMM yyyy` |

---

## Lookup Strategy

```
┌─────────────────────────────────────────────────────────────────┐
│  STEP 1: STATIC LOOKUP - O(1) (Fastest)                         │
│  → Know category? Read endpoints/[CATEGORY].md directly         │
│  → Don't know? Scan Endpoint Files table above                  │
│  → ~50-100 lines per file vs 600+ in single file                │
├─────────────────────────────────────────────────────────────────┤
│  STEP 2: DYNAMIC SEARCH (If Not Found in Static)                │
│  → Search codebase: core/network/services/*.kt                  │
│  → Search design layer: design-spec-layer/features/*/API.md     │
│  → Check Swagger UI for endpoint existence                      │
├─────────────────────────────────────────────────────────────────┤
│  STEP 3: AUTO-UPDATE (After Dynamic Find or Manual Implement)   │
│  → Add to endpoints/[CATEGORY].md (O(1) lookup)                 │
│  → Add row to API_INDEX.md Quick Lookup table                   │
│  → Update design-layer/*/API.md (source of truth)               │
└─────────────────────────────────────────────────────────────────┘
```

### Step 1: Static Lookup (O(1))

**Direct file access by category:**

| Need | Read This File | Lines |
|------|----------------|:-----:|
| Auth/Login/Register | `endpoints/AUTH.md` | ~90 |
| Client/Profile | `endpoints/CLIENT.md` | ~80 |
| Savings Account | `endpoints/SAVINGS.md` | ~150 |
| Loan Account | `endpoints/LOAN.md` | ~120 |
| Beneficiary | `endpoints/BENEFICIARY.md` | ~130 |
| Transfer | `endpoints/TRANSFER.md` | ~90 |
| Guarantor | `endpoints/GUARANTOR.md` | ~100 |
| Notification | `endpoints/NOTIFICATION.md` | ~70 |
| Charges | `endpoints/CHARGES.md` | ~100 |
| Share Account | `endpoints/SHARE.md` | ~90 |
| Password/Settings | `endpoints/USER.md` | ~50 |

**Then for patterns:**

| Step | File | What to Find |
|------|------|--------------|
| 1b | `CLIENT_PATTERNS.md` | Service/Repository patterns |
| 1c | `ERROR_HANDLING.md` | Exception handling |

### Step 2: Dynamic Search (If Not Found)

**Search commands:**

```bash
# Search in service files
grep -r "@GET\|@POST\|@PUT\|@DELETE" core/network/src/commonMain/kotlin/**/services/

# Search for specific endpoint
grep -r "/beneficiaries" core/network/

# Search in design layer
grep -r "Endpoint" claude-product-cycle/design-spec-layer/features/*/API.md

# List all service files
ls core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/services/
```

**Claude Glob Patterns:**
```
core/network/**/services/*.kt
design-spec-layer/features/*/API.md
```

**External Reference:**
- [Swagger UI](https://sandbox.mifos.community/fineract-provider/swagger-ui/index.html#/) - Filter by `/self/`

### Step 3: Auto-Update Rules

| Scenario | Action |
|----------|--------|
| Found in static lookup | No update needed |
| Found via dynamic search | **ADD** to server layer docs |
| Manually implemented new API | **ADD** to server layer + design layer |
| API deprecated/removed | **REMOVE** from server layer |

**Update Checklist:**
- [ ] Add row to `API_INDEX.md` Quick Lookup table
- [ ] Add row to `API_INDEX.md` By Category table
- [ ] Add full documentation to `API_REFERENCE.md`
- [ ] Update `design-spec-layer/features/[feature]/API.md` (source)

---

## Update Flow

```
┌─────────────────────────────────────────────────────────────────┐
│  NEW API DESIGNED                                               │
│  └─→ Add to design-layer/features/*/API.md (SOURCE)            │
├─────────────────────────────────────────────────────────────────┤
│  READY FOR CLIENT IMPLEMENTATION                                │
│  └─→ Add to server-layer/API_REFERENCE.md (DETAILS)            │
│  └─→ Add row to server-layer/API_INDEX.md (INDEX)              │
├─────────────────────────────────────────────────────────────────┤
│  CLIENT IMPLEMENTS                                              │
│  └─→ Uses server-layer docs for implementation                 │
├─────────────────────────────────────────────────────────────────┤
│  FOUND DYNAMICALLY (not in docs)                                │
│  └─→ UPDATE server layer docs immediately                      │
└─────────────────────────────────────────────────────────────────┘
```

---

## How to Add New API

### Format for API_INDEX.md

```markdown
| /endpoint | METHOD | Purpose | `serviceMethod()` | [feature](link) |
```

### Format for API_REFERENCE.md

```markdown
### METHOD /endpoint
**Purpose**: Description

**Service**: `ServiceName.methodName()`

**Request**:
```json
{ ... }
```

**Response**:
```json
{ ... }
```

**DTO**: `DtoName`
```

---

## Service File Locations

```
core/network/src/commonMain/kotlin/org/mifos/mobile/core/network/services/
├── AuthenticationService.kt
├── BeneficiaryService.kt
├── ClientChargeService.kt
├── ClientService.kt
├── GuarantorService.kt
├── LoanService.kt
├── NotificationService.kt
├── RegistrationService.kt
├── SavingAccountsService.kt
├── ShareAccountService.kt
├── ThirdPartyTransferService.kt
└── UserService.kt
```

---

## Related Files

### Server Layer (This Directory)
- [API_REFERENCE.md](API_REFERENCE.md) - Complete endpoint documentation
- [CLIENT_PATTERNS.md](CLIENT_PATTERNS.md) - Service/Repository patterns
- [ERROR_HANDLING.md](ERROR_HANDLING.md) - Exception handling reference

### Design Layer (Source of Truth)
- Feature API Docs: `design-spec-layer/features/*/API.md`

### Client Layer (Implementation)
- Service Implementations: `core/network/services/`
- Repository Layer: `core/data/repository/`
- Kotlin DTOs: `core/model/entity/`

---

## Changelog

| Date | Change |
|------|--------|
| 2025-01-05 | Restructured as part of modular server layer docs |
| 2025-01-05 | Created with merged content from FINERACT_API.md + feature API.md files |

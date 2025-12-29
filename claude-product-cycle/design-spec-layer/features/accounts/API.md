# Accounts - API Reference

> **Base URL**: `https://tt.mifos.community/fineract-provider/api/v1/self/`
> **Authentication**: Basic Auth with `Fineract-Platform-TenantId` header
> **Last Updated**: 2025-12-29

---

## Authentication Headers

All endpoints require:
```
Headers:
  Authorization: Basic {base64EncodedAuthenticationKey}
  Fineract-Platform-TenantId: {tenantId}
  Content-Type: application/json
```

---

## 1. Client Accounts Overview

### GET /self/clients/{clientId}/accounts

**Purpose**: Fetch all account summaries for account listing

**Request**:
```
GET /self/clients/123/accounts
```

**Optional Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| fields | String | Filter by account type: "savingsAccounts", "loanAccounts", "shareAccounts" |

**Response**:
```json
{
  "savingsAccounts": [
    {
      "id": 1001,
      "accountNo": "SA-0001234567",
      "productId": 1,
      "productName": "Wallet Savings",
      "status": {
        "id": 300,
        "code": "savingsAccountStatusType.active",
        "value": "Active",
        "submittedAndPendingApproval": false,
        "approved": true,
        "rejected": false,
        "withdrawnByApplicant": false,
        "active": true,
        "closed": false
      },
      "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$",
        "nameCode": "currency.USD",
        "displayLabel": "US Dollar ($)"
      },
      "accountBalance": 12500.00,
      "depositType": {
        "id": 100,
        "code": "depositAccountType.savingsDeposit",
        "value": "Savings"
      },
      "lastActiveTransactionDate": [2025, 12, 29]
    }
  ],
  "loanAccounts": [
    {
      "id": 2001,
      "accountNo": "LA-0009876543",
      "productId": 2,
      "productName": "Personal Loan",
      "status": {
        "id": 300,
        "code": "loanStatusType.active",
        "value": "Active",
        "active": true
      },
      "loanType": {
        "id": 1,
        "code": "accountType.individual",
        "value": "Individual"
      },
      "principal": 10000.00,
      "loanBalance": 6750.00,
      "amountPaid": 3250.00,
      "inArrears": false,
      "currency": {
        "code": "USD",
        "displaySymbol": "$"
      }
    }
  ],
  "shareAccounts": [
    {
      "id": 3001,
      "accountNo": "SH-0005678901",
      "productId": 3,
      "productName": "Equity Shares",
      "status": {
        "id": 300,
        "code": "shareAccountStatusType.active",
        "value": "Active",
        "active": true
      },
      "totalApprovedShares": 100,
      "totalPendingForApprovalShares": 0,
      "currency": {
        "code": "USD",
        "displaySymbol": "$"
      }
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class ClientAccounts(
    val loanAccounts: List<LoanAccount> = emptyList(),
    val savingsAccounts: List<SavingAccount>? = emptyList(),
    val shareAccounts: List<ShareAccount> = emptyList(),
) {
    fun recurringSavingsAccounts(): List<SavingAccount>
    fun nonRecurringSavingsAccounts(): List<SavingAccount>
}
```

**Status**: Implemented in ClientService

---

## 2. Savings Account Details with Transactions

### GET /self/savingsaccounts/{accountId}

**Purpose**: Fetch savings account details with transaction history

**Request**:
```
GET /self/savingsaccounts/1001?associations=transactions
```

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| associations | String | Include related data: "transactions", "charges", "all" |

**Response**:
```json
{
  "id": 1001,
  "accountNo": "SA-0001234567",
  "clientId": 123,
  "clientName": "John Doe",
  "savingsProductId": 1,
  "savingsProductName": "Wallet Savings",
  "status": {
    "id": 300,
    "code": "savingsAccountStatusType.active",
    "value": "Active",
    "active": true
  },
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "decimalPlaces": 2,
    "displaySymbol": "$"
  },
  "nominalAnnualInterestRate": 4.5,
  "summary": {
    "totalDeposits": 15000.00,
    "totalWithdrawals": 2500.00,
    "totalInterestEarned": 125.00,
    "accountBalance": 12625.00
  },
  "transactions": [
    {
      "id": 10001,
      "transactionType": {
        "id": 1,
        "code": "savingsAccountTransactionType.deposit",
        "value": "Deposit",
        "deposit": true,
        "withdrawal": false,
        "interestPosting": false,
        "feeDeduction": false
      },
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "date": [2025, 12, 29],
      "currency": {
        "code": "USD",
        "displaySymbol": "$"
      },
      "amount": 4500.00,
      "runningBalance": 12625.00,
      "reversed": false,
      "submittedOnDate": [2025, 12, 29]
    },
    {
      "id": 10002,
      "transactionType": {
        "id": 2,
        "code": "savingsAccountTransactionType.withdrawal",
        "value": "Withdrawal",
        "deposit": false,
        "withdrawal": true,
        "interestPosting": false,
        "feeDeduction": false
      },
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "date": [2025, 12, 28],
      "currency": {
        "code": "USD",
        "displaySymbol": "$"
      },
      "amount": 150.00,
      "runningBalance": 8125.00,
      "reversed": false
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class SavingsWithAssociations(
    val id: Long? = null,
    val accountNo: String? = null,
    val depositType: DepositType? = null,
    val externalId: String? = null,
    val clientId: Int? = null,
    val clientName: String? = null,
    val savingsProductId: Int? = null,
    val savingsProductName: String? = null,
    val status: Status? = null,
    val timeline: TimeLine? = null,
    val currency: Currency? = null,
    val nominalAnnualInterestRate: Double? = null,
    val summary: Summary? = null,
    val transactions: List<Transactions> = emptyList(),
)

@Serializable
data class Transactions(
    val id: Int? = null,
    val transactionType: TransactionType? = null,
    val accountId: Int? = null,
    val accountNo: String? = null,
    val date: List<Int> = emptyList(),
    val currency: Currency? = null,
    val paymentDetailData: PaymentDetailData? = null,
    val amount: Double? = null,
    val runningBalance: Double? = null,
    val reversed: Boolean? = null,
    val submittedOnDate: List<Int>? = null,
)
```

**Status**: Implemented in SavingAccountsListService

---

## 3. Loan Account Details with Transactions

### GET /self/loans/{loanId}

**Purpose**: Fetch loan account details with transaction history

**Request**:
```
GET /self/loans/2001?associations=transactions
```

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| associations | String | Include: "repaymentSchedule", "transactions", "all" |

**Response**:
```json
{
  "id": 2001,
  "accountNo": "LA-0009876543",
  "clientId": 123,
  "clientName": "John Doe",
  "loanProductId": 2,
  "loanProductName": "Personal Loan",
  "status": {
    "id": 300,
    "code": "loanStatusType.active",
    "value": "Active",
    "active": true
  },
  "loanType": {
    "id": 1,
    "code": "accountType.individual",
    "value": "Individual"
  },
  "currency": {
    "code": "USD",
    "displaySymbol": "$",
    "decimalPlaces": 2
  },
  "principal": 10000.00,
  "approvedPrincipal": 10000.00,
  "numberOfRepayments": 12,
  "interestRatePerPeriod": 1.0,
  "summary": {
    "principalDisbursed": 10000.00,
    "principalPaid": 3250.00,
    "principalOutstanding": 6750.00,
    "interestCharged": 600.00,
    "interestPaid": 400.00,
    "interestOutstanding": 200.00,
    "totalExpectedRepayment": 10600.00,
    "totalRepayment": 3650.00,
    "totalOutstanding": 6950.00
  },
  "transactions": [
    {
      "id": 20001,
      "officeId": 1,
      "officeName": "Head Office",
      "type": {
        "id": 2,
        "code": "loanTransactionType.repayment",
        "value": "Repayment",
        "disbursement": false,
        "repaymentAtDisbursement": false,
        "repayment": true
      },
      "date": [2025, 12, 15],
      "currency": {
        "code": "USD",
        "displaySymbol": "$"
      },
      "amount": 900.00,
      "principalPortion": 800.00,
      "interestPortion": 100.00,
      "feeChargesPortion": 0.00,
      "penaltyChargesPortion": 0.00,
      "outstandingLoanBalance": 6750.00,
      "reversed": false
    }
  ],
  "inArrears": false
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class LoanWithAssociations(
    val id: Int? = null,
    val accountNo: String? = null,
    val externalId: String? = null,
    val status: Status? = null,
    val clientId: Int? = null,
    val clientName: String? = null,
    val loanProductId: Int? = null,
    val loanProductName: String? = null,
    val loanType: LoanType? = null,
    val currency: Currency? = null,
    val principal: Double? = null,
    val approvedPrincipal: Double? = null,
    val numberOfRepayments: Int? = null,
    val interestRatePerPeriod: Double? = null,
    val timeline: Timeline? = null,
    val summary: Summary? = null,
    val repaymentSchedule: RepaymentSchedule? = null,
    val transactions: List<Transaction?>? = arrayListOf(),
    val inArrears: Boolean? = null,
)
```

**Status**: Implemented in LoanAccountsListService

---

## 4. Share Account Details

### GET /self/shareaccounts/{accountId}

**Purpose**: Fetch share account details with purchased shares history

**Request**:
```
GET /self/shareaccounts/3001?associations=all
```

**Query Parameters**:
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| associations | String | "all" | Include related data |

**Response**:
```json
{
  "id": 3001,
  "accountNo": "SH-0005678901",
  "clientId": 123,
  "clientName": "John Doe",
  "productId": 3,
  "productName": "Equity Shares",
  "status": {
    "id": 300,
    "code": "shareAccountStatusType.active",
    "value": "Active",
    "active": true
  },
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "decimalPlaces": 2,
    "displaySymbol": "$"
  },
  "timeline": {
    "submittedOnDate": [2024, 1, 15],
    "approvedDate": [2024, 1, 20],
    "activatedDate": [2024, 1, 25]
  },
  "totalApprovedShares": 100,
  "totalPendingForApprovalShares": 0,
  "purchasedShares": [
    {
      "id": 30001,
      "purchasedDate": [2024, 1, 25],
      "numberOfShares": 50,
      "purchasedPrice": 10.00,
      "status": {
        "id": 300,
        "code": "purchaseStatusType.approved",
        "value": "Approved"
      },
      "type": {
        "id": 500,
        "code": "purchaseType.purchased",
        "value": "Purchase"
      },
      "amount": 500.00,
      "chargeAmount": 5.00,
      "amountPaid": 505.00
    }
  ],
  "summary": {
    "totalApprovedShares": 100,
    "totalPendingShares": 0
  }
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class ShareAccountWithAssociations(
    val id: Long = 0,
    val accountNo: String? = null,
    val clientId: Long? = null,
    val clientName: String? = null,
    val productId: Long? = null,
    val productName: String? = null,
    val status: Status? = null,
    val currency: Currency? = null,
    val timeline: Timeline? = null,
    val totalApprovedShares: Int? = null,
    val totalPendingForApprovalShares: Int? = null,
    val purchasedShares: List<Transactions> = emptyList(),
    val summary: Summary? = null,
)

// Share Transactions
@Serializable
data class Transactions(
    val id: Long? = null,
    val purchasedDate: List<Int> = emptyList(),
    val numberOfShares: Int? = null,
    val purchasedPrice: Double? = null,
    val status: Status? = null,
    val type: EnumOptionData? = null,
    val amount: Double? = null,
    val chargeAmount: Double? = null,
    val amountPaid: Double? = null,
)
```

**Status**: Implemented in ShareAccountService

---

## 5. Recent Transactions

### GET /self/clients/{clientId}/transactions

**Purpose**: Fetch recent transactions across all accounts

**Request**:
```
GET /self/clients/123/transactions?offset=0&limit=50
```

**Query Parameters**:
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| offset | Int | 0 | Pagination offset |
| limit | Int | 50 | Number of records per page |

**Response**:
```json
{
  "totalFilteredRecords": 150,
  "pageItems": [
    {
      "id": 10001,
      "officeId": 1,
      "officeName": "Head Office",
      "type": {
        "id": 1,
        "code": "savingsAccountTransactionType.deposit",
        "value": "Deposit",
        "deposit": true,
        "withdrawal": false,
        "interestPosting": false,
        "feeDeduction": false
      },
      "date": [2025, 12, 29],
      "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$"
      },
      "amount": 4500.00,
      "submittedOnDate": [2025, 12, 29],
      "reversed": false
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class Page<T>(
    val totalFilteredRecords: Int = 0,
    val pageItems: List<T> = emptyList(),
)

@Serializable
data class Transaction(
    val id: Long? = null,
    val officeId: Long? = null,
    val officeName: String? = null,
    val type: Type,
    val date: List<Int> = emptyList(),
    val currency: Currency? = null,
    val amount: Double? = null,
    val submittedOnDate: List<Int> = emptyList(),
    val reversed: Boolean? = null,
)
```

**Status**: Implemented in RecentTransactionsService

---

## 6. Savings Transaction Details

### GET /self/savingsaccounts/{accountId}/transactions/{transactionId}

**Purpose**: Fetch detailed information about a specific savings transaction

**Request**:
```
GET /self/savingsaccounts/1001/transactions/10001
```

**Response**:
```json
{
  "id": 10001,
  "officeId": 1,
  "officeName": "Head Office",
  "transactionType": {
    "id": 1,
    "code": "savingsAccountTransactionType.deposit",
    "value": "Deposit",
    "deposit": true,
    "withdrawal": false,
    "interestPosting": false,
    "feeDeduction": false
  },
  "date": [2025, 12, 29],
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "decimalPlaces": 2,
    "displaySymbol": "$"
  },
  "amount": 4500.00,
  "accountNo": "SA-0001234567",
  "runningBalance": 12625.00,
  "reversed": false,
  "manuallyReversed": false,
  "externalId": "EXT-12345",
  "submittedOnDate": [2025, 12, 29]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class TransactionDetails(
    val id: Long? = null,
    val officeId: Long? = null,
    val officeName: String? = null,
    val type: Type? = null,
    val date: List<Int> = emptyList(),
    val currency: Currency? = null,
    val amount: Double? = null,
    val submittedOnDate: List<Int> = emptyList(),
    val reversed: Boolean? = null,
    val accountNo: String? = null,
    val manuallyReversed: Boolean? = null,
    val externalId: String? = null,
    val outstandingLoanBalance: Double? = null,
    val runningBalance: Double? = null,
    val principalPortion: Double? = null,
    val interestPortion: Double? = null,
    val feeChargesPortion: Double? = null,
    val penaltyChargesPortion: Double? = null,
) {
    val isCredit: Boolean
        get() { /* logic based on type value */ }
}
```

**Status**: Implemented in SavingAccountsListService

---

## 7. Loan Transaction Details

### GET /self/loans/{loanId}/transactions/{transactionId}

**Purpose**: Fetch detailed information about a specific loan transaction

**Request**:
```
GET /self/loans/2001/transactions/20001
```

**Response**:
```json
{
  "id": 20001,
  "officeId": 1,
  "officeName": "Head Office",
  "transactionType": {
    "id": 2,
    "code": "loanTransactionType.repayment",
    "value": "Repayment",
    "disbursement": false,
    "repayment": true
  },
  "date": [2025, 12, 15],
  "currency": {
    "code": "USD",
    "displaySymbol": "$",
    "decimalPlaces": 2
  },
  "amount": 900.00,
  "accountNo": "LA-0009876543",
  "reversed": false,
  "manuallyReversed": false,
  "externalId": "LN-EXT-20001",
  "outstandingLoanBalance": 6750.00,
  "principalPortion": 800.00,
  "interestPortion": 100.00,
  "feeChargesPortion": 0.00,
  "penaltyChargesPortion": 0.00
}
```

**Status**: Implemented in LoanAccountsListService

---

## API Summary Table

| Endpoint | Method | Service | Repository | Priority |
|----------|--------|---------|------------|----------|
| /self/clients/{id}/accounts | GET | ClientService | AccountsRepository | P0 |
| /self/savingsaccounts/{id} | GET | SavingAccountsListService | SavingsAccountRepository | P0 |
| /self/loans/{id} | GET | LoanAccountsListService | LoanRepository | P0 |
| /self/shareaccounts/{id} | GET | ShareAccountService | ShareAccountRepository | P0 |
| /self/clients/{id}/transactions | GET | RecentTransactionsService | RecentTransactionRepository | P0 |
| /self/savingsaccounts/{id}/transactions/{txId} | GET | SavingAccountsListService | SavingsAccountRepository | P1 |
| /self/loans/{id}/transactions/{txId} | GET | LoanAccountsListService | LoanRepository | P1 |

---

## Common Data Models

### Currency

```kotlin
@Serializable
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int? = null,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null,
)
```

### Status (Common pattern across account types)

```kotlin
@Serializable
data class Status(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
    val submittedAndPendingApproval: Boolean? = null,
    val approved: Boolean? = null,
    val rejected: Boolean? = null,
    val withdrawnByApplicant: Boolean? = null,
    val active: Boolean? = null,
    val closed: Boolean? = null,
    val matured: Boolean? = null,
    val overpaid: Boolean? = null,
)
```

### TransactionType

```kotlin
@Serializable
data class TransactionType(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
    val deposit: Boolean? = null,
    val dividendPayout: Boolean? = null,
    val withdrawal: Boolean? = null,
    val interestPosting: Boolean? = null,
    val feeDeduction: Boolean? = null,
    val initiateTransfer: Boolean? = null,
    val approveTransfer: Boolean? = null,
    val withdrawTransfer: Boolean? = null,
    val rejectTransfer: Boolean? = null,
    val overdraftFee: Boolean? = null,
)
```

---

## Error Responses

| Status Code | Error | Description | User Message |
|-------------|-------|-------------|--------------|
| 400 | Bad Request | Invalid request payload | "Please check your input" |
| 401 | Unauthorized | Invalid/expired token | "Please login again" |
| 403 | Forbidden | Insufficient permissions | "Access denied" |
| 404 | Not Found | Resource doesn't exist | "Account not found" |
| 500 | Server Error | Internal server error | "Service unavailable" |

**Error Response Format**:
```json
{
  "developerMessage": "Detailed error for debugging",
  "httpStatusCode": "404",
  "defaultUserMessage": "Account not found",
  "userMessageGlobalisationCode": "error.msg.account.not.found",
  "errors": []
}
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial API documentation based on existing implementation |

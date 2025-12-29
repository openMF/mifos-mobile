# Dashboard - API Reference

> **Base URL**: `https://tt.mifos.community/fineract-provider/api/v1/self/`
> **Authentication**: Basic Auth with `Fineract-Platform-TenantId` header
> **Last Updated**: 2025-12-28

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

## 1. Client Details

### GET /self/clients/{clientId}

**Purpose**: Fetch client information for dashboard greeting and profile

**Request**:
```
GET /self/clients/123
```

**Response**:
```json
{
  "id": 123,
  "accountNo": "CL-0000123",
  "status": {
    "id": 300,
    "code": "clientStatusType.active",
    "value": "Active"
  },
  "active": true,
  "activationDate": [2023, 1, 15],
  "firstname": "John",
  "middlename": "M",
  "lastname": "Doe",
  "displayName": "John M. Doe",
  "mobileNo": "1234567890",
  "emailAddress": "john.doe@example.com",
  "dateOfBirth": [1990, 5, 20],
  "gender": {
    "id": 22,
    "name": "Male"
  },
  "officeId": 1,
  "officeName": "Head Office",
  "timeline": {
    "submittedOnDate": [2023, 1, 10],
    "activatedOnDate": [2023, 1, 15]
  }
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class Client(
    @SerialName("id") val id: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("status") val status: ClientStatus? = null,
    @SerialName("active") val active: Boolean = false,
    @SerialName("activationDate") val activationDate: List<Int>? = null,
    @SerialName("firstname") val firstname: String? = null,
    @SerialName("middlename") val middlename: String? = null,
    @SerialName("lastname") val lastname: String? = null,
    @SerialName("displayName") val displayName: String? = null,
    @SerialName("mobileNo") val mobileNo: String? = null,
    @SerialName("emailAddress") val emailAddress: String? = null,
    @SerialName("dateOfBirth") val dateOfBirth: List<Int>? = null,
    @SerialName("gender") val gender: Gender? = null,
    @SerialName("officeId") val officeId: Long? = null,
    @SerialName("officeName") val officeName: String? = null,
)

@Serializable
data class ClientStatus(
    @SerialName("id") val id: Int,
    @SerialName("code") val code: String? = null,
    @SerialName("value") val value: String? = null,
)
```

**Status**: Implemented in ClientService

---

## 2. Client Accounts Overview

### GET /self/clients/{clientId}/accounts

**Purpose**: Fetch all account summaries for dashboard net worth calculation

**Request**:
```
GET /self/clients/123/accounts
```

**Response**:
```json
{
  "savingsAccounts": [
    {
      "id": 1001,
      "accountNo": "SA-0001234567",
      "productId": 1,
      "productName": "Savings Plus",
      "shortProductName": "SP",
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
      "accountBalance": 35000.00,
      "accountType": {
        "id": 100,
        "code": "accountType.individual",
        "value": "Individual"
      },
      "depositType": {
        "id": 100,
        "code": "depositAccountType.savingsDeposit",
        "value": "Savings"
      }
    }
  ],
  "loanAccounts": [
    {
      "id": 2001,
      "accountNo": "LA-0009876543",
      "productId": 2,
      "productName": "Personal Loan",
      "shortProductName": "PL",
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
      "loanCycle": 1,
      "principal": 10000.00,
      "loanBalance": 6750.00,
      "amountPaid": 3250.00,
      "inArrears": false
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
      "totalPendingForApprovalShares": 0
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class ClientAccounts(
    @SerialName("savingsAccounts") val savingsAccounts: List<SavingAccount>? = null,
    @SerialName("loanAccounts") val loanAccounts: List<LoanAccount> = emptyList(),
    @SerialName("shareAccounts") val shareAccounts: List<ShareAccount> = emptyList(),
)

@Serializable
data class SavingAccount(
    @SerialName("id") val id: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("productId") val productId: Long? = null,
    @SerialName("productName") val productName: String? = null,
    @SerialName("status") val status: SavingsStatus? = null,
    @SerialName("currency") val currency: Currency? = null,
    @SerialName("accountBalance") val accountBalance: Double? = null,
    @SerialName("depositType") val depositType: DepositType? = null,
)

@Serializable
data class LoanAccount(
    @SerialName("id") val id: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("productId") val productId: Long? = null,
    @SerialName("productName") val productName: String? = null,
    @SerialName("status") val status: LoanStatus? = null,
    @SerialName("loanType") val loanType: LoanType? = null,
    @SerialName("principal") val principal: Double? = null,
    @SerialName("loanBalance") val loanBalance: Double? = null,
    @SerialName("amountPaid") val amountPaid: Double? = null,
    @SerialName("inArrears") val inArrears: Boolean = false,
)

@Serializable
data class ShareAccount(
    @SerialName("id") val id: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("productId") val productId: Long? = null,
    @SerialName("productName") val productName: String? = null,
    @SerialName("status") val status: ShareStatus? = null,
    @SerialName("totalApprovedShares") val totalApprovedShares: Int? = null,
    @SerialName("totalPendingForApprovalShares") val totalPendingForApprovalShares: Int? = null,
)
```

**Status**: Implemented in ClientService

---

## 3. Client Profile Image

### GET /self/clients/{clientId}/images

**Purpose**: Fetch client profile image for dashboard avatar

**Request**:
```
GET /self/clients/123/images
Accept: application/octet-stream
```

**Response**:
```
Binary image data (JPEG/PNG)
```

**Alternative Response** (Base64):
```
GET /self/clients/123/images?maxWidth=200&maxHeight=200
Accept: text/plain

Response: Base64 encoded image string
```

**Kotlin Implementation**:
```kotlin
suspend fun getClientImage(clientId: Long): ByteArray? {
    return try {
        clientService.getClientImage(clientId)
    } catch (e: Exception) {
        null // Return null if no image exists
    }
}
```

**Status**: Implemented in ClientService

---

## 4. Client Transactions

### GET /self/clients/{clientId}/transactions

**Purpose**: Fetch recent transactions across all accounts

**Request**:
```
GET /self/clients/123/transactions?offset=0&limit=20
```

**Query Parameters**:
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| offset | Int | 0 | Pagination offset |
| limit | Int | 50 | Number of records |

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
      "date": [2025, 12, 28],
      "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$"
      },
      "amount": 4500.00,
      "submittedOnDate": [2025, 12, 28],
      "reversed": false,
      "submittedByUsername": "system",
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "accountType": {
        "id": 2,
        "code": "accountType.savings",
        "value": "Savings Account"
      },
      "runningBalance": 35000.00,
      "transfer": null
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class TransactionPage(
    @SerialName("totalFilteredRecords") val totalFilteredRecords: Int,
    @SerialName("pageItems") val pageItems: List<Transaction>,
)

@Serializable
data class Transaction(
    @SerialName("id") val id: Long,
    @SerialName("officeId") val officeId: Long? = null,
    @SerialName("officeName") val officeName: String? = null,
    @SerialName("type") val type: TransactionType? = null,
    @SerialName("date") val date: List<Int>? = null,
    @SerialName("currency") val currency: Currency? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("reversed") val reversed: Boolean = false,
    @SerialName("accountId") val accountId: Long? = null,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("runningBalance") val runningBalance: Double? = null,
    @SerialName("transfer") val transfer: TransferDetail? = null,
)

@Serializable
data class TransactionType(
    @SerialName("id") val id: Int,
    @SerialName("code") val code: String? = null,
    @SerialName("value") val value: String? = null,
    @SerialName("deposit") val deposit: Boolean = false,
    @SerialName("withdrawal") val withdrawal: Boolean = false,
    @SerialName("interestPosting") val interestPosting: Boolean = false,
    @SerialName("feeDeduction") val feeDeduction: Boolean = false,
)
```

**Status**: Implemented in RecentTransactionService

---

## 5. Savings Account Details

### GET /self/savingsaccounts/{accountId}

**Purpose**: Fetch detailed savings account information

**Request**:
```
GET /self/savingsaccounts/1001
```

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| associations | String | Include related data (transactions, charges) |

**Response**:
```json
{
  "id": 1001,
  "accountNo": "SA-0001234567",
  "clientId": 123,
  "clientName": "John Doe",
  "savingsProductId": 1,
  "savingsProductName": "Savings Plus",
  "fieldOfficerId": 1,
  "fieldOfficerName": "Officer Name",
  "status": {
    "id": 300,
    "code": "savingsAccountStatusType.active",
    "value": "Active",
    "active": true
  },
  "timeline": {
    "submittedOnDate": [2023, 1, 10],
    "approvedOnDate": [2023, 1, 12],
    "activatedOnDate": [2023, 1, 15]
  },
  "currency": {
    "code": "USD",
    "name": "US Dollar",
    "decimalPlaces": 2,
    "displaySymbol": "$"
  },
  "nominalAnnualInterestRate": 4.5,
  "interestCompoundingPeriodType": {
    "id": 1,
    "code": "savings.interest.period.savingsCompoundingInterestPeriodType.daily",
    "value": "Daily"
  },
  "interestPostingPeriodType": {
    "id": 4,
    "code": "savings.interest.period.savingsPostingInterestPeriodType.monthly",
    "value": "Monthly"
  },
  "interestCalculationType": {
    "id": 1,
    "code": "savingsInterestCalculationType.dailybalance",
    "value": "Daily Balance"
  },
  "interestCalculationDaysInYearType": {
    "id": 365,
    "code": "savingsInterestCalculationDaysInYearType.days365",
    "value": "365 Days"
  },
  "minRequiredOpeningBalance": 500.00,
  "withdrawalFeeForTransfers": false,
  "allowOverdraft": false,
  "summary": {
    "currency": {
      "code": "USD",
      "displaySymbol": "$"
    },
    "totalDeposits": 42500.00,
    "totalWithdrawals": 7500.00,
    "totalInterestEarned": 1250.00,
    "totalInterestPosted": 1250.00,
    "accountBalance": 35000.00,
    "availableBalance": 34500.00
  },
  "transactions": [],
  "charges": []
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class SavingsAccountDetail(
    @SerialName("id") val id: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("clientId") val clientId: Long? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("savingsProductName") val savingsProductName: String? = null,
    @SerialName("status") val status: SavingsStatus? = null,
    @SerialName("currency") val currency: Currency? = null,
    @SerialName("nominalAnnualInterestRate") val nominalAnnualInterestRate: Double? = null,
    @SerialName("minRequiredOpeningBalance") val minRequiredOpeningBalance: Double? = null,
    @SerialName("summary") val summary: SavingsSummary? = null,
    @SerialName("transactions") val transactions: List<SavingsTransaction>? = null,
    @SerialName("charges") val charges: List<Charge>? = null,
)

@Serializable
data class SavingsSummary(
    @SerialName("totalDeposits") val totalDeposits: Double? = null,
    @SerialName("totalWithdrawals") val totalWithdrawals: Double? = null,
    @SerialName("totalInterestEarned") val totalInterestEarned: Double? = null,
    @SerialName("totalInterestPosted") val totalInterestPosted: Double? = null,
    @SerialName("accountBalance") val accountBalance: Double? = null,
    @SerialName("availableBalance") val availableBalance: Double? = null,
)
```

**Status**: Implemented in SavingsAccountService

---

## 6. Loan Account Details

### GET /self/loans/{loanId}

**Purpose**: Fetch detailed loan account information

**Request**:
```
GET /self/loans/2001?associations=repaymentSchedule,transactions
```

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| associations | String | Include: repaymentSchedule, transactions, all |

**Response**:
```json
{
  "id": 2001,
  "accountNo": "LA-0009876543",
  "clientId": 123,
  "clientName": "John Doe",
  "clientOfficeId": 1,
  "loanProductId": 2,
  "loanProductName": "Personal Loan",
  "loanProductDescription": "Personal unsecured loan",
  "loanPurposeName": "Personal Use",
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
  "termFrequency": 12,
  "termPeriodFrequencyType": {
    "id": 2,
    "code": "termFrequency.periodFrequencyType.months",
    "value": "Months"
  },
  "numberOfRepayments": 12,
  "repaymentEvery": 1,
  "repaymentFrequencyType": {
    "id": 2,
    "value": "Months"
  },
  "interestRatePerPeriod": 12.0,
  "annualInterestRate": 12.0,
  "timeline": {
    "submittedOnDate": [2024, 1, 1],
    "approvedOnDate": [2024, 1, 5],
    "disbursedOnDate": [2024, 1, 10],
    "expectedMaturityDate": [2025, 1, 10]
  },
  "summary": {
    "currency": {
      "code": "USD",
      "displaySymbol": "$"
    },
    "principalDisbursed": 10000.00,
    "principalPaid": 3250.00,
    "principalOutstanding": 6750.00,
    "interestCharged": 600.00,
    "interestPaid": 400.00,
    "interestOutstanding": 200.00,
    "feeChargesCharged": 50.00,
    "feeChargesPaid": 50.00,
    "feeChargesOutstanding": 0.00,
    "totalExpectedRepayment": 10650.00,
    "totalRepayment": 3700.00,
    "totalOutstanding": 6950.00
  },
  "repaymentSchedule": {
    "currency": {
      "code": "USD"
    },
    "loanTermInDays": 365,
    "totalPrincipalDisbursed": 10000.00,
    "totalPrincipalExpected": 10000.00,
    "totalPrincipalPaid": 3250.00,
    "totalInterestCharged": 600.00,
    "totalFeeChargesCharged": 50.00,
    "totalRepaymentExpected": 10650.00,
    "totalRepayment": 3700.00,
    "totalOutstanding": 6950.00,
    "periods": []
  },
  "transactions": [],
  "inArrears": false
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class LoanAccountDetail(
    @SerialName("id") val id: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("clientId") val clientId: Long? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("loanProductName") val loanProductName: String? = null,
    @SerialName("status") val status: LoanStatus? = null,
    @SerialName("currency") val currency: Currency? = null,
    @SerialName("principal") val principal: Double? = null,
    @SerialName("approvedPrincipal") val approvedPrincipal: Double? = null,
    @SerialName("numberOfRepayments") val numberOfRepayments: Int? = null,
    @SerialName("annualInterestRate") val annualInterestRate: Double? = null,
    @SerialName("timeline") val timeline: LoanTimeline? = null,
    @SerialName("summary") val summary: LoanSummary? = null,
    @SerialName("repaymentSchedule") val repaymentSchedule: RepaymentSchedule? = null,
    @SerialName("inArrears") val inArrears: Boolean = false,
)

@Serializable
data class LoanSummary(
    @SerialName("principalDisbursed") val principalDisbursed: Double? = null,
    @SerialName("principalPaid") val principalPaid: Double? = null,
    @SerialName("principalOutstanding") val principalOutstanding: Double? = null,
    @SerialName("interestCharged") val interestCharged: Double? = null,
    @SerialName("interestPaid") val interestPaid: Double? = null,
    @SerialName("interestOutstanding") val interestOutstanding: Double? = null,
    @SerialName("totalExpectedRepayment") val totalExpectedRepayment: Double? = null,
    @SerialName("totalRepayment") val totalRepayment: Double? = null,
    @SerialName("totalOutstanding") val totalOutstanding: Double? = null,
)
```

**Status**: Implemented in LoanAccountService

---

## 7. Beneficiary List

### GET /self/beneficiaries/tpt

**Purpose**: Fetch beneficiaries for third-party transfers

**Request**:
```
GET /self/beneficiaries/tpt
```

**Response**:
```json
[
  {
    "id": 5001,
    "name": "Jane Doe",
    "officeName": "Head Office",
    "clientName": "Jane Doe",
    "accountType": {
      "id": 2,
      "code": "accountType.savings",
      "value": "Savings Account"
    },
    "accountNumber": "SA-0007654321",
    "transferLimit": 10000.00
  }
]
```

**Kotlin DTO**:
```kotlin
@Serializable
data class Beneficiary(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String? = null,
    @SerialName("officeName") val officeName: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("accountType") val accountType: AccountType? = null,
    @SerialName("accountNumber") val accountNumber: String? = null,
    @SerialName("transferLimit") val transferLimit: Double? = null,
)
```

**Status**: Implemented in BeneficiaryService

---

## 8. Transfer Template

### GET /self/accounttransfers/template

**Purpose**: Get transfer options and account templates

**Request**:
```
GET /self/accounttransfers/template?type=tpt
```

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| type | String | Transfer type: "tpt" (third-party) |
| fromAccountId | Long | Source account ID |
| fromAccountType | Int | 1=Loan, 2=Savings |

**Response**:
```json
{
  "fromAccountOptions": [
    {
      "accountId": 1001,
      "accountNo": "SA-0001234567",
      "accountType": {
        "id": 2,
        "value": "Savings Account"
      },
      "clientId": 123,
      "clientName": "John Doe",
      "officeId": 1,
      "officeName": "Head Office"
    }
  ],
  "toAccountOptions": [
    {
      "accountId": 1002,
      "accountNo": "SA-0009876543",
      "accountType": {
        "id": 2,
        "value": "Savings Account"
      },
      "clientId": 456,
      "clientName": "Jane Doe"
    }
  ],
  "transferTypes": [
    {
      "id": 1,
      "code": "accountTransferType.account.transfer",
      "value": "Account Transfer"
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class AccountTransferTemplate(
    @SerialName("fromAccountOptions") val fromAccountOptions: List<AccountOption>? = null,
    @SerialName("toAccountOptions") val toAccountOptions: List<AccountOption>? = null,
    @SerialName("transferTypes") val transferTypes: List<TransferType>? = null,
)

@Serializable
data class AccountOption(
    @SerialName("accountId") val accountId: Long,
    @SerialName("accountNo") val accountNo: String? = null,
    @SerialName("accountType") val accountType: AccountType? = null,
    @SerialName("clientId") val clientId: Long? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("officeId") val officeId: Long? = null,
    @SerialName("officeName") val officeName: String? = null,
)
```

**Status**: Implemented in ThirdPartyTransferService

---

## 9. Execute Transfer

### POST /self/accounttransfers

**Purpose**: Execute a fund transfer between accounts

**Request**:
```json
POST /self/accounttransfers

{
  "fromOfficeId": 1,
  "fromClientId": 123,
  "fromAccountType": 2,
  "fromAccountId": 1001,
  "toOfficeId": 1,
  "toClientId": 456,
  "toAccountType": 2,
  "toAccountId": 1002,
  "dateFormat": "dd MMMM yyyy",
  "locale": "en",
  "transferDate": "28 December 2025",
  "transferAmount": 500.00,
  "transferDescription": "Payment for dinner"
}
```

**Response**:
```json
{
  "savingsId": 1001,
  "resourceId": 50001,
  "changes": {
    "transferDate": "28 December 2025",
    "transferAmount": 500.00,
    "transferDescription": "Payment for dinner"
  }
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class TransferPayload(
    @SerialName("fromOfficeId") val fromOfficeId: Long,
    @SerialName("fromClientId") val fromClientId: Long,
    @SerialName("fromAccountType") val fromAccountType: Int,
    @SerialName("fromAccountId") val fromAccountId: Long,
    @SerialName("toOfficeId") val toOfficeId: Long,
    @SerialName("toClientId") val toClientId: Long,
    @SerialName("toAccountType") val toAccountType: Int,
    @SerialName("toAccountId") val toAccountId: Long,
    @SerialName("dateFormat") val dateFormat: String = "dd MMMM yyyy",
    @SerialName("locale") val locale: String = "en",
    @SerialName("transferDate") val transferDate: String,
    @SerialName("transferAmount") val transferAmount: Double,
    @SerialName("transferDescription") val transferDescription: String? = null,
)

@Serializable
data class TransferResponse(
    @SerialName("savingsId") val savingsId: Long? = null,
    @SerialName("resourceId") val resourceId: Long? = null,
    @SerialName("changes") val changes: Map<String, Any>? = null,
)
```

**Status**: Implemented in ThirdPartyTransferService

---

## 10. Third-Party Transfer

### POST /self/accounttransfers?type=tpt

**Purpose**: Execute transfer to a saved beneficiary

**Request**:
```json
POST /self/accounttransfers?type=tpt

{
  "fromAccountId": 1001,
  "fromAccountType": 2,
  "toAccountId": 5001,
  "dateFormat": "dd MMMM yyyy",
  "locale": "en",
  "transferDate": "28 December 2025",
  "transferAmount": 500.00,
  "transferDescription": "Monthly support"
}
```

**Response**: Same as standard transfer

**Status**: Implemented in ThirdPartyTransferService

---

## API Summary Table

| Endpoint | Method | Service | Repository | Priority |
|----------|--------|---------|------------|----------|
| /self/clients/{id} | GET | ClientService | ClientRepository | P0 |
| /self/clients/{id}/accounts | GET | ClientService | ClientRepository | P0 |
| /self/clients/{id}/images | GET | ClientService | ClientRepository | P1 |
| /self/clients/{id}/transactions | GET | RecentTransactionService | RecentTransactionRepository | P0 |
| /self/savingsaccounts/{id} | GET | SavingsAccountService | SavingsAccountRepository | P0 |
| /self/loans/{id} | GET | LoanAccountService | LoanAccountRepository | P0 |
| /self/beneficiaries/tpt | GET | BeneficiaryService | BeneficiaryRepository | P1 |
| /self/accounttransfers/template | GET | ThirdPartyTransferService | TransferRepository | P1 |
| /self/accounttransfers | POST | ThirdPartyTransferService | TransferRepository | P0 |

---

## Error Responses

| Status Code | Error | Description | User Message |
|-------------|-------|-------------|--------------|
| 400 | Bad Request | Invalid request payload | "Please check your input" |
| 401 | Unauthorized | Invalid/expired token | "Please login again" |
| 403 | Forbidden | Insufficient permissions | "Access denied" |
| 404 | Not Found | Resource doesn't exist | "Account not found" |
| 409 | Conflict | Business rule violation | "Insufficient balance" |
| 500 | Server Error | Internal server error | "Service unavailable" |

**Error Response Format**:
```json
{
  "developerMessage": "Detailed error for debugging",
  "httpStatusCode": "400",
  "defaultUserMessage": "User-friendly error message",
  "userMessageGlobalisationCode": "error.msg.code",
  "errors": [
    {
      "developerMessage": "Field-specific error",
      "defaultUserMessage": "Amount must be greater than 0",
      "userMessageGlobalisationCode": "validation.msg.amount.invalid",
      "parameterName": "transferAmount"
    }
  ]
}
```

---

## Rate Limiting

| Operation | Limit | Window |
|-----------|-------|--------|
| Read operations | 100 | Per minute |
| Write operations | 20 | Per minute |
| Transfers | 10 | Per hour |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-28 | Initial production-level API documentation |

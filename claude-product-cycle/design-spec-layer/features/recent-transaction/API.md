# Recent Transactions - API Reference

---

## Endpoints Required

### 1. Get Client Accounts (for Filter)

**Endpoint**: `GET /self/clients/{clientId}/accounts`

**Purpose**: Fetch list of savings accounts for the account filter dropdown

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Path Parameters:
  clientId: Long - The client ID

Query Parameters:
  accountType: String - "savingsAccounts" (constant)
```

**Response**:
```json
{
    "savingsAccounts": [
        {
            "id": 1,
            "accountNo": "000000001",
            "productName": "Savings Account",
            "productId": 1,
            "accountBalance": 5000.00,
            "totalDeposits": 10000.00,
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$",
                "displayLabel": "US Dollar ($)"
            },
            "status": {
                "id": 300,
                "code": "savingsAccountStatusType.active",
                "value": "Active",
                "active": true
            },
            "lastActiveTransactionDate": [2025, 9, 15]
        },
        {
            "id": 2,
            "accountNo": "000000002",
            "productName": "Premium Savings",
            "productId": 2,
            "accountBalance": 15000.00,
            "totalDeposits": 20000.00,
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$",
                "displayLabel": "US Dollar ($)"
            },
            "status": {
                "id": 300,
                "code": "savingsAccountStatusType.active",
                "value": "Active",
                "active": true
            },
            "lastActiveTransactionDate": [2025, 9, 10]
        }
    ],
    "loanAccounts": [],
    "shareAccounts": []
}
```

**Kotlin DTO**: Uses `ClientAccounts` from `core/model/entity/client/`

**Status**: Implemented in `AccountsService`

---

### 2. Get Savings Account with Transactions

**Endpoint**: `GET /self/savingsaccounts/{accountId}`

**Purpose**: Fetch savings account details with transaction history

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Path Parameters:
  accountId: Long - The savings account ID

Query Parameters:
  associations: String - "transactions" (to include transaction list)
```

**Response**:
```json
{
    "id": 1,
    "accountNo": "000000001",
    "clientId": 123,
    "clientName": "John Doe",
    "savingsProductId": 1,
    "savingsProductName": "Savings Account",
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
        "displaySymbol": "$",
        "displayLabel": "US Dollar ($)"
    },
    "summary": {
        "totalDeposits": 10000.00,
        "totalWithdrawals": 5000.00,
        "accountBalance": 5000.00
    },
    "transactions": [
        {
            "id": 101,
            "transactionType": {
                "id": 1,
                "code": "savingsAccountTransactionType.deposit",
                "value": "Deposit",
                "deposit": true,
                "withdrawal": false,
                "interestPosting": false,
                "feeDeduction": false
            },
            "accountId": 1,
            "accountNo": "000000001",
            "date": [2025, 9, 15],
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$"
            },
            "amount": 500.00,
            "runningBalance": 5000.00,
            "reversed": false,
            "submittedOnDate": [2025, 9, 15]
        },
        {
            "id": 100,
            "transactionType": {
                "id": 2,
                "code": "savingsAccountTransactionType.withdrawal",
                "value": "Withdrawal",
                "deposit": false,
                "withdrawal": true,
                "interestPosting": false,
                "feeDeduction": false
            },
            "accountId": 1,
            "accountNo": "000000001",
            "date": [2025, 9, 14],
            "currency": {
                "code": "USD",
                "name": "US Dollar",
                "decimalPlaces": 2,
                "displaySymbol": "$"
            },
            "amount": 150.00,
            "runningBalance": 4500.00,
            "reversed": false,
            "submittedOnDate": [2025, 9, 14]
        }
    ]
}
```

**Kotlin DTO**: Uses `SavingsWithAssociations` from `core/model/entity/accounts/savings/`

**Status**: Implemented in `SavingAccountsListService`

---

### 3. Get Recent Transactions (Legacy/Alternative)

**Endpoint**: `GET /self/clients/{clientId}/transactions`

**Purpose**: Fetch paginated transaction list across all accounts (alternative endpoint)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Path Parameters:
  clientId: Long - The client ID

Query Parameters:
  offset: Int? - Pagination offset
  limit: Int? - Number of records to fetch
```

**Response**:
```json
{
    "totalFilteredRecords": 50,
    "pageItems": [
        {
            "id": 101,
            "officeId": 1,
            "officeName": "Head Office",
            "type": {
                "id": 1,
                "value": "Deposit"
            },
            "date": [2025, 9, 15],
            "currency": {
                "code": "USD",
                "displaySymbol": "$",
                "decimalPlaces": 2
            },
            "amount": 500.00,
            "submittedOnDate": [2025, 9, 15],
            "reversed": false
        }
    ]
}
```

**Kotlin DTO**: Uses `Page<Transaction>` from `core/model/entity/`

**Status**: Implemented in `RecentTransactionsService` (not currently used by feature)

---

## Kotlin DTOs

### SavingAccount

```kotlin
@Serializable
@Parcelize
data class SavingAccount(
    val id: Long = 0,
    val accountNo: String? = null,
    val productName: String? = null,
    val productId: Int? = null,
    val overdraftLimit: Long = 0,
    val minRequiredBalance: Long = 0,
    val accountBalance: Double = 0.0,
    val totalDeposits: Double = 0.0,
    val savingsProductName: String? = null,
    val clientName: String? = null,
    val savingsProductId: String? = null,
    val nominalAnnualInterestRate: Double = 0.0,
    val status: Status? = null,
    val currency: Currency? = null,
    val depositType: DepositType? = null,
    val lastActiveTransactionDate: List<Int>? = null,
    val timeLine: TimeLine? = null,
) : Parcelable
```

### Transactions

```kotlin
@Serializable
@Parcelize
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
    val interestedPostedAsOn: Boolean? = null,
) : Parcelable
```

### TransactionType

```kotlin
@Serializable
@Parcelize
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
    val overdraftInterest: Boolean? = null,
    val writtenoff: Boolean? = null,
    val overdraftFee: Boolean? = null,
    val withholdTax: Boolean? = null,
    val escheat: Boolean? = null,
) : Parcelable
```

### Currency

```kotlin
@Serializable
@Parcelize
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int? = null,
    val inMultiplesOf: Int? = null,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null,
) : Parcelable
```

### SavingsWithAssociations

```kotlin
@Serializable
@Parcelize
data class SavingsWithAssociations(
    val id: Long? = null,
    val accountNo: String? = null,
    val depositType: DepositType? = null,
    val externalId: String? = null,
    val clientId: Int? = null,
    val clientName: String? = null,
    val savingsProductId: Int? = null,
    val savingsProductName: String? = null,
    val fieldOfficerId: Int? = null,
    val status: Status? = null,
    val timeline: TimeLine? = null,
    val currency: Currency? = null,
    val nominalAnnualInterestRate: Double? = null,
    val minRequiredOpeningBalance: Double? = null,
    val lockinPeriodFrequency: Double? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,
    val lastActiveTransactionDate: List<Int>? = null,
    val dormancyTrackingActive: Boolean? = null,
    val summary: Summary? = null,
    val transactions: List<Transactions> = emptyList(),
) : Parcelable
```

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| `/self/clients/{clientId}/accounts` | AccountsService | AccountsRepository | Implemented |
| `/self/savingsaccounts/{accountId}?associations=transactions` | SavingAccountsListService | SavingsAccountRepository | Implemented |
| `/self/clients/{clientId}/transactions` | RecentTransactionsService | RecentTransactionRepository | Implemented (legacy) |

---

## Kotlin Implementation

### Service (SavingAccountsListService.kt)

```kotlin
interface SavingAccountsListService {
    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    fun getSavingsWithAssociations(
        @Path("accountId") accountId: Long,
        @Query("associations") associationType: String?,
    ): Flow<SavingsWithAssociations>
}
```

### Repository (SavingsAccountRepository.kt)

```kotlin
interface SavingsAccountRepository {
    fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Flow<DataState<SavingsWithAssociations>>
}
```

### Legacy Service (RecentTransactionsService.kt)

```kotlin
interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    fun getRecentTransactionsList(
        @Path("clientId") clientId: Long,
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?,
    ): Flow<Page<Transaction>>
}
```

---

## Notes

- The feature currently uses `SavingsAccountRepository.getSavingsWithAssociations()` to fetch transactions per account
- Transaction filtering (All/Debit/Credit) is done client-side after fetching all transactions
- Only active savings accounts (`status.active == true`) are shown in the filter
- Date format in API response is `List<Int>` as `[year, month, day]` (e.g., `[2025, 9, 15]`)
- Currency formatting uses `CurrencyFormatter.format()` utility
- The legacy `RecentTransactionsService` provides cross-account transaction list but is not actively used in the current implementation

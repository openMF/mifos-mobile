# Loan Account - API Reference

---

## Endpoints Required

### 1. Get Loan Account Detail

**Endpoint**: `GET /self/loans/{loanId}/`

**Purpose**: Fetch basic loan account details

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
{
    "id": 123,
    "loanProductId": 1,
    "accountNo": "000000123",
    "productName": "Personal Loan",
    "clientName": "John Doe",
    "principal": 5000.00,
    "annualInterestRate": 12.0,
    "numberOfRepayments": 12,
    "loanBalance": 3500.00,
    "amountPaid": 1500.00,
    "status": {
        "id": 300,
        "code": "loanStatusType.active",
        "value": "Active",
        "active": true,
        "closed": false,
        "pendingApproval": false,
        "waitingForDisbursal": false,
        "overpaid": false
    },
    "loanType": {
        "id": 1,
        "code": "loanType.individual",
        "value": "Individual"
    },
    "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$",
        "displayLabel": "US Dollar ($)"
    },
    "inArrears": false,
    "summary": {
        "principalDisbursed": 5000.00,
        "principalPaid": 1500.00,
        "interestCharged": 600.00,
        "interestPaid": 300.00,
        "totalExpectedRepayment": 5600.00,
        "totalRepayment": 1800.00,
        "totalOutstanding": 3800.00
    },
    "timeline": {
        "submittedOnDate": [2024, 1, 15],
        "approvedOnDate": [2024, 1, 20],
        "expectedDisbursementDate": [2024, 1, 25],
        "actualDisbursementDate": [2024, 1, 25]
    }
}
```

**Kotlin DTO**: `LoanAccount` from `core/model/entity/accounts/loan/`

**Status**: Implemented in `LoanAccountsListService`

---

### 2. Get Loan With Associations (Transactions)

**Endpoint**: `GET /self/loans/{loanId}?associations=transactions`

**Purpose**: Fetch loan details with transaction history

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
Path Parameters:
  loanId: Long - The loan account ID
Query Parameters:
  associations: "transactions"
```

**Response**:
```json
{
    "id": 123,
    "accountNo": "000000123",
    "clientId": 1,
    "clientName": "John Doe",
    "loanProductId": 1,
    "loanProductName": "Personal Loan",
    "principal": 5000.00,
    "approvedPrincipal": 5000.00,
    "termFrequency": 12,
    "numberOfRepayments": 12,
    "interestRatePerPeriod": 1.0,
    "status": {
        "id": 300,
        "code": "loanStatusType.active",
        "value": "Active",
        "active": true
    },
    "loanType": {
        "id": 1,
        "code": "loanType.individual",
        "value": "Individual"
    },
    "currency": {
        "code": "USD",
        "name": "US Dollar",
        "decimalPlaces": 2,
        "displaySymbol": "$",
        "displayLabel": "US Dollar ($)"
    },
    "timeline": {
        "submittedOnDate": [2024, 1, 15],
        "approvedOnDate": [2024, 1, 20],
        "actualDisbursementDate": [2024, 1, 25]
    },
    "summary": {
        "principalDisbursed": 5000.00,
        "principalPaid": 1500.00,
        "interestCharged": 600.00,
        "interestPaid": 300.00,
        "feeChargesCharged": 50.00,
        "penaltyChargesCharged": 0.00,
        "totalExpectedRepayment": 5650.00,
        "totalRepayment": 1850.00,
        "totalOutstanding": 3800.00
    },
    "transactions": [
        {
            "id": 1,
            "type": {
                "id": 2,
                "code": "loanTransactionType.repayment",
                "value": "Repayment"
            },
            "date": [2024, 2, 15],
            "amount": 500.00,
            "principalPortion": 400.00,
            "interestPortion": 100.00,
            "submittedOnDate": [2024, 2, 15]
        }
    ]
}
```

**Kotlin DTO**: `LoanWithAssociations` from `core/model/entity/accounts/loan/`

**Status**: Implemented in `LoanAccountsListService`

---

### 3. Get Loan With Associations (Repayment Schedule)

**Endpoint**: `GET /self/loans/{loanId}?associations=repaymentSchedule`

**Purpose**: Fetch loan details with full repayment schedule

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
Path Parameters:
  loanId: Long - The loan account ID
Query Parameters:
  associations: "repaymentSchedule"
```

**Response**:
```json
{
    "id": 123,
    "accountNo": "000000123",
    "loanProductName": "Personal Loan",
    "termFrequency": 12,
    "interestRatePerPeriod": 1.0,
    "currency": {
        "code": "USD",
        "decimalPlaces": 2,
        "displaySymbol": "$"
    },
    "status": {
        "id": 300,
        "value": "Active",
        "active": true
    },
    "loanType": {
        "id": 1,
        "value": "Individual"
    },
    "timeline": {
        "submittedOnDate": [2024, 1, 15],
        "actualDisbursementDate": [2024, 1, 25]
    },
    "summary": {
        "principalDisbursed": 5000.00,
        "principalPaid": 2000.00,
        "interestCharged": 600.00,
        "interestPaid": 400.00,
        "interestWaived": 0.00,
        "feeChargesCharged": 50.00,
        "feeChargesWaived": 0.00,
        "penaltyChargesCharged": 0.00,
        "penaltyChargesWaived": 0.00,
        "totalExpectedRepayment": 5650.00,
        "totalRepayment": 2450.00,
        "totalOutstanding": 3200.00
    },
    "repaymentSchedule": {
        "currency": {
            "code": "USD",
            "decimalPlaces": 2,
            "displaySymbol": "$"
        },
        "loanTermInDays": 365,
        "totalPrincipalDisbursed": 5000.00,
        "totalPrincipalExpected": 5000.00,
        "totalPrincipalPaid": 2000.00,
        "totalInterestCharged": 600.00,
        "totalFeeChargesCharged": 50.00,
        "totalPenaltyChargesCharged": 0.00,
        "totalWaived": 0.00,
        "totalWrittenOff": 0.00,
        "totalRepaymentExpected": 5650.00,
        "totalRepayment": 2450.00,
        "totalOutstanding": 3200.00,
        "periods": [
            {
                "period": 0,
                "fromDate": [2024, 1, 25],
                "dueDate": [2024, 1, 25],
                "principalDisbursed": 5000.00,
                "complete": true
            },
            {
                "period": 1,
                "fromDate": [2024, 1, 25],
                "dueDate": [2024, 2, 25],
                "obligationsMetOnDate": [2024, 2, 25],
                "complete": true,
                "daysInPeriod": 31,
                "principalOriginalDue": 416.67,
                "principalDue": 416.67,
                "principalPaid": 416.67,
                "principalOutstanding": 0.00,
                "principalLoanBalanceOutstanding": 4583.33,
                "interestOriginalDue": 50.00,
                "interestDue": 50.00,
                "interestPaid": 50.00,
                "interestOutstanding": 0.00,
                "feeChargesDue": 0.00,
                "feeChargesPaid": 0.00,
                "penaltyChargesDue": 0.00,
                "penaltyChargesPaid": 0.00,
                "totalOriginalDueForPeriod": 466.67,
                "totalDueForPeriod": 466.67,
                "totalPaidForPeriod": 466.67,
                "totalOutstandingForPeriod": 0.00
            },
            {
                "period": 2,
                "fromDate": [2024, 2, 25],
                "dueDate": [2024, 3, 25],
                "complete": false,
                "daysInPeriod": 29,
                "principalOriginalDue": 416.67,
                "principalDue": 416.67,
                "principalPaid": 0.00,
                "principalOutstanding": 416.67,
                "interestDue": 45.83,
                "interestPaid": 0.00,
                "interestOutstanding": 45.83,
                "totalDueForPeriod": 462.50,
                "totalPaidForPeriod": 0.00,
                "totalOutstandingForPeriod": 462.50
            }
        ]
    },
    "npa": false
}
```

**Kotlin DTO**: `LoanWithAssociations` with `RepaymentSchedule` and `Periods`

**Status**: Implemented in `LoanAccountsListService`

---

### 4. Get Loan Transaction Details

**Endpoint**: `GET /self/loans/{loanId}/transactions/{transactionId}`

**Purpose**: Fetch details of a specific loan transaction

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
Path Parameters:
  loanId: Long - The loan account ID
  transactionId: Long - The transaction ID
```

**Response**:
```json
{
    "id": 1,
    "type": {
        "id": 2,
        "code": "loanTransactionType.repayment",
        "value": "Repayment"
    },
    "date": [2024, 2, 15],
    "currency": {
        "code": "USD",
        "decimalPlaces": 2,
        "displaySymbol": "$"
    },
    "amount": 500.00,
    "principalPortion": 400.00,
    "interestPortion": 100.00,
    "feeChargesPortion": 0.00,
    "penaltyChargesPortion": 0.00,
    "overpaymentPortion": 0.00,
    "outstandingLoanBalance": 3100.00,
    "submittedOnDate": [2024, 2, 15]
}
```

**Kotlin DTO**: `TransactionDetails` from `core/model/entity/`

**Status**: Implemented in `LoanAccountsListService`

---

### 5. Get Loan Template

**Endpoint**: `GET /self/loans/template?templateType=individual`

**Purpose**: Get loan application template for creating new loans

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
Query Parameters:
  templateType: "individual"
  clientId: Long - The client ID
```

**Response**:
```json
{
    "clientId": 1,
    "clientAccountNo": "000000001",
    "clientName": "John Doe",
    "clientOfficeId": 1,
    "productOptions": [
        {
            "id": 1,
            "name": "Personal Loan",
            "shortName": "PL"
        }
    ],
    "loanOfficerOptions": [
        {
            "id": 1,
            "firstname": "Jane",
            "lastname": "Smith",
            "displayName": "Jane Smith"
        }
    ],
    "loanPurposeOptions": [
        {
            "id": 1,
            "name": "Personal",
            "position": 1,
            "isActive": true
        }
    ],
    "termFrequencyTypeOptions": [
        {"id": 0, "code": "termFrequency.periodFrequencyType.days", "value": "Days"},
        {"id": 1, "code": "termFrequency.periodFrequencyType.weeks", "value": "Weeks"},
        {"id": 2, "code": "termFrequency.periodFrequencyType.months", "value": "Months"}
    ],
    "repaymentFrequencyTypeOptions": [
        {"id": 0, "code": "repaymentFrequency.periodFrequencyType.days", "value": "Days"},
        {"id": 1, "code": "repaymentFrequency.periodFrequencyType.weeks", "value": "Weeks"},
        {"id": 2, "code": "repaymentFrequency.periodFrequencyType.months", "value": "Months"}
    ],
    "amortizationTypeOptions": [
        {"id": 0, "code": "amortizationType.equal.installments", "value": "Equal installments"},
        {"id": 1, "code": "amortizationType.equal.principal", "value": "Equal principal payments"}
    ],
    "interestTypeOptions": [
        {"id": 0, "code": "interestType.declining.balance", "value": "Declining Balance"},
        {"id": 1, "code": "interestType.flat", "value": "Flat"}
    ]
}
```

**Kotlin DTO**: `LoanTemplate` from `core/model/entity/templates/loans/`

**Status**: Implemented in `LoanAccountsListService`

---

### 6. Get Loan Template By Product

**Endpoint**: `GET /self/loans/template?templateType=individual&productId={productId}`

**Purpose**: Get loan template with specific product defaults

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
Query Parameters:
  templateType: "individual"
  clientId: Long
  productId: Int
```

**Response**: Same structure as loan template with product-specific defaults populated

**Status**: Implemented in `LoanAccountsListService`

---

### 7. Create Loan Account

**Endpoint**: `POST /self/loans`

**Purpose**: Submit a new loan application

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json
Body:
{
    "clientId": 1,
    "productId": 1,
    "principal": 5000.00,
    "loanTermFrequency": 12,
    "loanTermFrequencyType": 2,
    "numberOfRepayments": 12,
    "repaymentEvery": 1,
    "repaymentFrequencyType": 2,
    "interestRatePerPeriod": 1.0,
    "amortizationType": 0,
    "interestType": 0,
    "interestCalculationPeriodType": 1,
    "transactionProcessingStrategyId": 1,
    "expectedDisbursementDate": "2024-01-25",
    "submittedOnDate": "2024-01-15",
    "locale": "en",
    "dateFormat": "yyyy-MM-dd",
    "loanPurposeId": 1
}
```

**Response**: HTTP 200 with loan ID

**Kotlin DTO**: `LoansPayload` from `core/model/entity/payload/`

**Status**: Implemented in `LoanAccountsListService`

---

### 8. Update Loan Account

**Endpoint**: `PUT /self/loans/{loanId}/`

**Purpose**: Update an existing loan application (before approval)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json
Body: Same as create loan
```

**Response**: HTTP 200

**Status**: Implemented in `LoanAccountsListService`

---

### 9. Withdraw Loan Account

**Endpoint**: `POST /self/loans/{loanId}?command=withdrawnByApplicant`

**Purpose**: Withdraw a submitted loan application

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json
Body:
{
    "withdrawnOnDate": "2024-01-20",
    "locale": "en",
    "dateFormat": "yyyy-MM-dd",
    "note": "Changed my mind"
}
```

**Response**: HTTP 200

**Kotlin DTO**: `LoanWithdraw` from `core/model/entity/accounts/loan/`

**Status**: Implemented in `LoanAccountsListService`

---

## Kotlin DTOs

### LoanAccount

```kotlin
@Serializable
@Parcelize
data class LoanAccount(
    val id: Long = 0,
    val loanProductId: Long = 0,
    val externalId: String? = null,
    val numberOfRepayments: Long = 0,
    val accountNo: String? = null,
    val productName: String? = null,
    val productId: Int? = null,
    val loanProductName: String? = null,
    val clientName: String? = null,
    val loanProductDescription: String? = null,
    val principal: Double = 0.0,
    val annualInterestRate: Double = 0.0,
    val status: Status? = null,
    val loanType: LoanType? = null,
    val loanCycle: Int? = null,
    val loanBalance: Double = 0.0,
    val amountPaid: Double = 0.0,
    val currency: Currency?,
    val inArrears: Boolean? = null,
    val summary: Summary? = null,
    val loanPurposeName: String? = null,
    val timeline: Timeline?,
) : Parcelable
```

### LoanWithAssociations

```kotlin
@Serializable
@Parcelize
data class LoanWithAssociations(
    val id: Int? = null,
    val accountNo: String? = null,
    val status: Status? = null,
    val clientId: Int? = null,
    val clientName: String? = null,
    val loanProductId: Int? = null,
    val loanProductName: String? = null,
    val loanType: LoanType? = null,
    val currency: Currency? = null,
    val principal: Double? = null,
    val approvedPrincipal: Double? = null,
    val termFrequency: Int? = null,
    val numberOfRepayments: Int? = null,
    val interestRatePerPeriod: Double? = null,
    val timeline: Timeline? = null,
    val summary: Summary? = null,
    val repaymentSchedule: RepaymentSchedule? = null,
    val transactions: List<Transaction?>? = arrayListOf(),
    val npa: Boolean? = null,
    val loanPurposeName: String? = null,
) : Parcelable
```

### Summary

```kotlin
@Serializable
@Parcelize
data class Summary(
    val principalDisbursed: Double = 0.0,
    val principalPaid: Double = 0.0,
    val interestCharged: Double = 0.0,
    val interestPaid: Double = 0.0,
    val feeChargesCharged: Double = 0.0,
    val penaltyChargesCharged: Double = 0.0,
    val penaltyChargesWaived: Double = 0.0,
    val totalExpectedRepayment: Double = 0.0,
    val interestWaived: Double = 0.0,
    val totalRepayment: Double = 0.0,
    val feeChargesWaived: Double = 0.0,
    val totalOutstanding: Double = 0.0,
    val currency: Currency? = null,
) : Parcelable
```

### Periods

```kotlin
@Serializable
@Parcelize
data class Periods(
    val period: Int? = null,
    val fromDate: List<Int> = emptyList(),
    val dueDate: List<Int> = emptyList(),
    val obligationsMetOnDate: List<Int> = emptyList(),
    val principalDisbursed: Double? = null,
    val complete: Boolean? = null,
    val daysInPeriod: Int? = null,
    val principalOriginalDue: Double? = null,
    val principalDue: Double? = null,
    val principalPaid: Double? = null,
    val principalOutstanding: Double? = null,
    val principalLoanBalanceOutstanding: Double? = null,
    val interestOriginalDue: Double? = null,
    val interestDue: Double? = null,
    val interestPaid: Double? = null,
    val interestWaived: Double? = null,
    val interestOutstanding: Double? = null,
    val feeChargesDue: Double? = null,
    val feeChargesPaid: Double? = null,
    val feeChargesWaived: Double? = null,
    val feeChargesOutstanding: Double? = null,
    val penaltyChargesDue: Double? = null,
    val penaltyChargesPaid: Double? = null,
    val penaltyChargesWaived: Double? = null,
    val penaltyChargesOutstanding: Double? = null,
    val totalOriginalDueForPeriod: Double? = null,
    val totalDueForPeriod: Double? = null,
    val totalPaidForPeriod: Double? = null,
    val totalOutstandingForPeriod: Double? = null,
    val totalOverdue: Double? = null,
) : Parcelable
```

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| `/loans/{id}/` | LoanAccountsListService | LoanRepository | Implemented |
| `/loans/{id}?associations=transactions` | LoanAccountsListService | LoanRepository | Implemented |
| `/loans/{id}?associations=repaymentSchedule` | LoanAccountsListService | LoanRepository | Implemented |
| `/loans/{id}/transactions/{txnId}` | LoanAccountsListService | LoanRepository | Implemented |
| `/loans/template` | LoanAccountsListService | LoanRepository | Implemented |
| `/loans` (POST) | LoanAccountsListService | - | Implemented |
| `/loans/{id}/` (PUT) | LoanAccountsListService | - | Implemented |
| `/loans/{id}?command=withdrawnByApplicant` | LoanAccountsListService | LoanRepository | Implemented |

---

## Kotlin Implementation

### Service (LoanAccountsListService.kt)

```kotlin
interface LoanAccountsListService {
    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    fun getLoanAccountsDetail(@Path("loanId") loanId: Long): Flow<LoanAccount>?

    @GET(ApiEndPoints.LOANS + "/{loanId}")
    fun getLoanWithAssociations(
        @Path("loanId") loanId: Long,
        @Query("associations") associationType: String?,
    ): Flow<LoanWithAssociations>

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    fun getLoanTemplate(@Query("clientId") clientId: Long?): Flow<LoanTemplate>

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    fun getLoanTemplateByProduct(
        @Query("clientId") clientId: Long?,
        @Query("productId") productId: Int?,
    ): Flow<LoanTemplate>

    @POST(ApiEndPoints.LOANS)
    suspend fun createLoansAccount(@Body loansPayload: LoansPayload?): HttpResponse

    @PUT(ApiEndPoints.LOANS + "/{loanId}/")
    suspend fun updateLoanAccount(
        @Path("loanId") loanId: Long,
        @Body loansPayload: LoansPayload?,
    ): HttpResponse

    @POST(ApiEndPoints.LOANS + "/{loanId}?command=withdrawnByApplicant")
    suspend fun withdrawLoanAccount(
        @Path("loanId") loanId: Long,
        @Body loanWithdraw: LoanWithdraw?,
    ): HttpResponse

    @GET(ApiEndPoints.LOANS + "/{loanId}/transactions/{transactionId}")
    fun getLoanTransactionDetails(
        @Path("loanId") loanId: Long,
        @Path("transactionId") transactionId: Long,
    ): Flow<TransactionDetails>
}
```

### Repository (LoanRepository.kt)

```kotlin
interface LoanRepository {
    fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Flow<DataState<LoanWithAssociations?>>

    fun getLoanTransactionDetails(
        loanId: Long,
        transactionId: Long,
    ): Flow<DataState<TransactionDetails>>

    suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): DataState<String>

    fun template(clientId: Long?): Flow<DataState<LoanTemplate?>>

    fun getLoanTemplateByProduct(
        clientId: Long?,
        productId: Int?,
    ): Flow<DataState<LoanTemplate?>>
}
```

---

## Notes

- Association types: `transactions` for payment history, `repaymentSchedule` for installment breakdown
- Currency formatting uses `CurrencyFormatter.format(amount, currencyCode, decimalPlaces)`
- Dates are returned as `List<Int>` arrays `[year, month, day]` - use `DateHelper.getDateAsString()`
- The `status` object contains boolean flags (`active`, `closed`, `pendingApproval`, etc.) for quick status checks
- The `npa` field indicates Non-Performing Asset status - used for auto-debit display
- Loan accounts are fetched through `clients/{id}/accounts` for the list view, using the `loanAccounts` property of `ClientAccounts`

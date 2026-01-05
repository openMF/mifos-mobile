# Loan Account Endpoints

> **Category**: Loan Account
> **Endpoints**: 6
> **Service**: `LoanService`

---

## GET /loans/{loanId}

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

## GET /loanproducts

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

## POST /loans

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

## POST /loans/{loanId}?command=withdrawnByApplicant

**Purpose**: Withdraw loan application

**Service**: `LoanService.withdrawLoanAccount(loanId, payload)`

**Request**:
```json
{
    "locale": "en",
    "dateFormat": "dd MMMM yyyy",
    "withdrawnOnDate": "29 December 2025",
    "note": "User requested withdrawal"
}
```

---

## GET /loans/{loanId}/template?templateType=repayment

**Purpose**: Get loan repayment template

**Service**: `LoanService.getLoanRepaymentTemplate(loanId)`

**Response**:
```json
{
    "loanId": 67890,
    "date": [2025, 1, 15],
    "amount": 875.00,
    "principalPortion": 833.33,
    "interestPortion": 41.67
}
```

**DTO**: `LoanRepaymentTemplate`

---

## GET /loans/{loanId}/transactions/{transactionId}

**Purpose**: Get loan transaction details

**Service**: `LoanService.getLoanAccountTransaction(loanId, transactionId)`

**Response**:
```json
{
    "id": 1,
    "type": { "id": 2, "value": "Repayment" },
    "date": [2025, 1, 15],
    "amount": 875.00,
    "principalPortion": 833.33,
    "interestPortion": 41.67
}
```

---

## Related

- Design Layer: [loan-account/API.md](../../design-spec-layer/features/loan-account/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)

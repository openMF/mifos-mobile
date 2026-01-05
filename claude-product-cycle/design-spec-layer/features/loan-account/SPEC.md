# Loan Account - Feature Specification

> **Purpose**: View and manage loan accounts including details, repayment schedule, summary, and transactions
> **User Value**: Complete visibility into loan status, payment schedules, and ability to make payments
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Loan Account feature provides comprehensive loan management for MifosX Self-Service users. It consists of four main screens:
1. **Loan Accounts List** - View all loan accounts with filtering capabilities
2. **Loan Account Details** - View specific loan details and access actions
3. **Repayment Schedule** - View installment breakdown and pay installments
4. **Loan Summary** - Comprehensive loan summary with all financial details

### 1.2 User Stories
- As a user, I want to see all my loan accounts so I can track my debt obligations
- As a user, I want to filter loan accounts by status (Active, Pending, Closed, etc.)
- As a user, I want to view loan details including outstanding balance and next payment
- As a user, I want to see my repayment schedule so I can plan my payments
- As a user, I want to pay an installment directly from the schedule
- As a user, I want to see a comprehensive summary of my loan including charges and waivers
- As a user, I want to view my loan transaction history
- As a user, I want to generate a QR code for my loan account

---

## 2. Screen Layouts

### 2.1 Loan Accounts List Screen

```
+------------------------------------------+
|  [<]  Loan Accounts                      |
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  |  Total Loan Balance      [eye]    |  |
|  |  $XX,XXX.XX  or  ****             |  |
|  +------------------------------------+  |
|                                          |
|  Loan Accounts             [n items]     |
|  ----------------------------------  [F] | <- Filter icon
|                                          |
|  +------------------------------------+  |
|  | [icon] Account #123456             |  |
|  |        Personal Loan               |  |
|  |        $5,000.00 | Active     [>] |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | [icon] Account #123457             |  |
|  |        Bronze Loan                 |  |
|  |        Pending Approval       [>] |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | [icon] Account #123458             |  |
|  |        Home Loan                   |  |
|  |        $150,000.00 | Active   [>] |  |
|  +------------------------------------+  |
|                                          |
+------------------------------------------+
```

### 2.2 Loan Account Details Screen

```
+------------------------------------------+
|  [<]  Account Details                    |
+------------------------------------------+
|                                          |
|  +------------------+  +---------------+ |
|  | Account Number   |  | Outstanding   | |
|  | 000000123        |  | $5,000.00     | |
|  +------------------+  +---------------+ |
|                                          |
|  +------------------+  +---------------+ |
|  | Product Type     |  | Currency      | |
|  | Personal Loan    |  | USD ($)       | |
|  +------------------+  +---------------+ |
|                                          |
|  Next Installment                        |
|  +------------------+  +---------------+ |
|  | Installment Amt  |  | Due Date      | |
|  | $250.00          |  | 2025-01-15    | |
|  +------------------+  +---------------+ |
|                                          |
|  Actions                                 |
|  +------------------------------------+  |
|  | [$$] Make Payment                  |  |
|  |     Pay your loan installment      |  |
|  +------------------------------------+  |
|  | [i] Loan Summary                   |  |
|  |     View detailed loan summary     |  |
|  +------------------------------------+  |
|  | [cal] Repayment Schedule           |  |
|  |     View payment schedule          |  |
|  +------------------------------------+  |
|  | [hist] Transactions                |  |
|  |     View transaction history       |  |
|  +------------------------------------+  |
|  | [bill] Charges                     |  |
|  |     View loan charges              |  |
|  +------------------------------------+  |
|  | [QR] QR Code                       |  |
|  |     Generate QR for account        |  |
|  +------------------------------------+  |
|                                          |
|             Powered by Mifos             |
+------------------------------------------+
```

### 2.3 Repayment Schedule Screen

```
+------------------------------------------+
|  [<]  Repayment Schedule                 |
+------------------------------------------+
|                                          |
|  +------------------+  +---------------+ |
|  | Account Number   |  | Disbursement  | |
|  | 000000123        |  | 2024-01-15    | |
|  +------------------+  +---------------+ |
|                                          |
|  +------------------+  +---------------+ |
|  | Principal Paid   |  | Installments  | |
|  | $2,000.00        |  | Paid: 8       | |
|  +------------------+  +---------------+ |
|                                          |
|  +------------------+  +---------------+ |
|  | Installments Left|  | Total         | |
|  | 4                |  | 12            | |
|  +------------------+  +---------------+ |
|                                          |
|  +------------------------------------+  |
|  | Installment #9                     |  |
|  | Due: 2025-01-15                    |  |
|  | Principal: $150.00                 |  |
|  | Interest: $25.00                   |  |
|  | Fees: $5.00                        |  |
|  | Total Due: $180.00         [PAY]  |  |
|  | Status: [ ] Pending                |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | Installment #10                    |  |
|  | Due: 2025-02-15                    |  |
|  | Total Due: $180.00         [PAY]  |  |
|  | Status: [ ] Pending                |  |
|  +------------------------------------+  |
|                                          |
|             Powered by Mifos             |
+------------------------------------------+
```

### 2.4 Loan Summary Screen

```
+------------------------------------------+
|  [<]  Loan Summary                       |
+------------------------------------------+
|                                          |
|  Account Details                         |
|  +------------------------------------+  |
|  | Account Number      | 000000123    |  |
|  | Loan Type           | Individual   |  |
|  | Scheme              | Personal     |  |
|  | Status              | Active       |  |
|  +------------------------------------+  |
|                                          |
|  Payoff Details                          |
|  +------------------------------------+  |
|  | Expected Payoff     | $6,000.00    |  |
|  | Interest Payoff     | $600.00      |  |
|  | Principal           | $5,000.00    |  |
|  | Currency            | USD ($)      |  |
|  | Interest Rate       | 12%          |  |
|  +------------------------------------+  |
|                                          |
|  Charges                                 |
|  +------------------------------------+  |
|  | Fees                | $50.00       |  |
|  | Penalties           | $0.00        |  |
|  +------------------------------------+  |
|                                          |
|  Waivers                                 |
|  +------------------------------------+  |
|  | Interest Waived     | $0.00        |  |
|  | Penalty Waived      | $0.00        |  |
|  | Fees Waived         | $0.00        |  |
|  +------------------------------------+  |
|                                          |
|  Paid Off Details                        |
|  +------------------------------------+  |
|  | Interest Paid       | $300.00      |  |
|  | Principal Paid      | $2,000.00    |  |
|  +------------------------------------+  |
|                                          |
|  Outstanding Details                     |
|  +------------------------------------+  |
|  | Total Outstanding   | $3,350.00    |  |
|  +------------------------------------+  |
|                                          |
|  Installment Details                     |
|  +------------------------------------+  |
|  | Regular Payment     | $180.00      |  |
|  | Next Payment        | 2025-01-15   |  |
|  | Months Left         | 4/12         |  |
|  | Auto Debit          | On           |  |
|  | Linked Account      | N/A          |  |
|  +------------------------------------+  |
|                                          |
|             Powered by Mifos             |
+------------------------------------------+
```

---

## 3. Sections Table

### 3.1 Loan Accounts List

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Dashboard Card | Total loan balance with visibility toggle | clients/{id}/accounts | P0 |
| 2 | Header Row | Account count and filter icon | - | P0 |
| 3 | Account List | Scrollable list of loan accounts | clients/{id}/accounts | P0 |
| 4 | Account Card | Individual account with status color | - | P0 |

### 3.2 Loan Account Details

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Details Grid | Account number, balance, product, currency | loans/{id}?associations=transactions | P0 |
| 2 | Next Installment | Installment amount and due date | loans/{id}?associations=transactions | P0 |
| 3 | Actions Grid | Action cards based on loan status | - | P0 |

### 3.3 Repayment Schedule

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Basic Details | Account number, disbursement date, installment stats | loans/{id}?associations=repaymentSchedule | P0 |
| 2 | Period List | List of all repayment periods with pay buttons | loans/{id}?associations=repaymentSchedule | P0 |

### 3.4 Loan Summary

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Account Details | Account number, type, scheme, status | loans/{id}?associations=repaymentSchedule | P0 |
| 2 | Payoff Details | Expected payoff, interest, principal, rate | loans/{id}?associations=repaymentSchedule | P0 |
| 3 | Charges | Fees and penalties charged | loans/{id}?associations=repaymentSchedule | P0 |
| 4 | Waivers | Waived amounts | loans/{id}?associations=repaymentSchedule | P1 |
| 5 | Paid Off Details | Interest and principal paid | loans/{id}?associations=repaymentSchedule | P0 |
| 6 | Outstanding Details | Total outstanding balance | loans/{id}?associations=repaymentSchedule | P0 |
| 7 | Installment Details | Payment schedule info | loans/{id}?associations=repaymentSchedule | P0 |

---

## 4. User Interactions

### 4.1 Loan Accounts List

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Toggle amount visibility | Tap eye icon | Show/hide total balance | - |
| Open filters | Tap filter icon | Show filter bottom sheet | - |
| Apply filter | Select filter option | Reload filtered list | - |
| Tap account | Tap account card | Navigate to account details | loans/{id}?associations=transactions |
| Pull refresh | Swipe down | Reload accounts | clients/{id}/accounts |
| Retry | Tap retry button | Reload accounts | clients/{id}/accounts |

### 4.2 Loan Account Details

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Navigate back | Tap back button | Pop to previous screen | - |
| Make Payment | Tap Make Payment card | Navigate to transfer screen | - |
| View Summary | Tap Loan Summary card | Navigate to summary screen | loans/{id}?associations=repaymentSchedule |
| View Schedule | Tap Repayment Schedule card | Navigate to schedule screen | loans/{id}?associations=repaymentSchedule |
| View Transactions | Tap Transactions card | Navigate to transactions screen | loans/{id}/transactions |
| View Charges | Tap Charges card | Navigate to charges screen | - |
| Generate QR | Tap QR Code card | Navigate to QR screen | - |

### 4.3 Repayment Schedule

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Navigate back | Tap back button | Pop to previous screen | - |
| Pay installment | Tap PAY button on period | Navigate to make payment | - |
| Retry | Tap retry button | Reload schedule | loans/{id}?associations=repaymentSchedule |

### 4.4 Loan Summary

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Navigate back | Tap back button | Pop to previous screen | - |
| Retry | Tap retry button | Reload summary | loans/{id}?associations=repaymentSchedule |

---

## 5. State Model

### 5.1 LoanAccountsState (List Screen)

```kotlin
data class LoanAccountsState(
    val loanAccounts: List<LoanAccount>?,
    val originalAccounts: List<LoanAccount>? = null,
    val isFilteredEmpty: Boolean = false,
    val firstLaunch: Boolean = true,
    val items: Int? = 0,
    val totalLoanAmount: String? = "",
    val currency: String? = "",
    val decimals: Int? = 2,
    val clientId: Long?,
    val dialogState: DialogState? = null,
    val selectedFilters: List<StringResource?> = emptyList(),
    val isAmountVisible: Boolean = false,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}
```

### 5.2 LoanAccountDetailsState

```kotlin
@Immutable
data class LoanAccountDetailsState(
    val accountId: Long = -1L,
    val totalOutStandingBalance: Double? = null,
    val isEmpty: Boolean = true,
    val clientName: String? = "",
    val submissionDate: String? = "",
    val accountNumber: String? = "",
    val product: String? = "",
    val displayItems: List<LabelValueItem> = emptyList(),
    val transactionList: List<LabelValueItem>? = emptyList(),
    val accountStatus: LoanStatus? = LoanStatus.ACTIVE,
    val items: ImmutableList<LoanActionItems>,
    val isUpdatable: Boolean = false,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}
```

### 5.3 RepaymentScheduleState

```kotlin
data class RepaymentScheduleState(
    val accountId: Long? = null,
    val loanWithAssociations: LoanWithAssociations? = null,
    val periods: List<Periods> = emptyList(),
    val basicDetails: Map<StringResource, String?> = emptyMap(),
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }

    val getPeriods = loanWithAssociations
        ?.repaymentSchedule
        ?.periods
        .orEmpty()
        .filter { it.period != null }
}
```

### 5.4 LoanAccountSummaryState

```kotlin
@Immutable
data class LoanAccountSummaryState(
    val accountId: Long,
    val accountDetails: List<LabelValueItem>? = emptyList(),
    val payOffDetails: List<LabelValueItem>? = emptyList(),
    val chargeDetails: List<LabelValueItem>? = emptyList(),
    val waiversDetails: List<LabelValueItem>? = emptyList(),
    val paidOffDetails: List<LabelValueItem>? = emptyList(),
    val outStandingDetails: List<LabelValueItem>? = emptyList(),
    val installmentDetails: List<LabelValueItem>? = emptyList(),
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}
```

---

## 6. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/self/clients/{clientId}/accounts` | GET | Get all loan accounts | Exists |
| `/self/loans/{loanId}?associations=transactions` | GET | Get loan with transactions | Exists |
| `/self/loans/{loanId}?associations=repaymentSchedule` | GET | Get loan with schedule | Exists |
| `/self/loans/{loanId}/transactions/{transactionId}` | GET | Get transaction details | Exists |
| `/self/loans/{loanId}` | PUT | Update loan account | Exists |
| `/self/loans/{loanId}?command=withdrawnByApplicant` | POST | Withdraw loan | Exists |
| `/self/loans/template?templateType=individual` | GET | Get loan template | Exists |

---

## 7. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Show network state | "No internet connection" + Retry |
| No loan accounts | Show empty state | "No loan accounts found" icon |
| Empty filtered results | Show filtered empty state | "No accounts match filter" |
| API error | Show error state | Error message + Retry button |
| Empty repayment periods | Filter out null periods | Skip non-period entries |
| Invalid loan status | Default to ACTIVE | Handle gracefully |

---

## 8. Components

| Component | Props | Reusable? |
|-----------|-------|-----------|
| MifosDashboardCard | isVisible, amount, currency, onToggle | Yes |
| MifosAccountCard | accountId, accountNo, type, status, color, onClick | Yes |
| MifosLabelValueCard | label, value, color | Yes |
| MifosActionCard | title, subtitle, icon, onClick | Yes |
| RepaymentScheduleItem | period, currencyCode, maxDigits, onPayClick | Yes |
| AccountSummaryCard | title, keyValuePairs | Yes |
| MifosDetailsCard | keyValuePairs | Yes |

---

## 9. Loan Status Actions Matrix

| Status | Make Payment | Summary | Schedule | Transactions | Charges | QR |
|--------|-------------|---------|----------|--------------|---------|-----|
| Submit & Pending | - | Yes | - | - | - | - |
| Approved | - | Yes | - | - | Yes | - |
| Active/Disbursed | Yes | Yes | Yes | Yes | Yes | Yes |
| Matured | - | Yes | Yes | Yes | - | - |
| Closed | - | Yes | - | Yes | - | - |
| Closed (obligations met) | - | Yes | - | Yes | - | - |
| Rejected | - | Yes | - | - | - | - |
| Withdrawn | - | Yes | - | - | - | - |
| Overpaid | - | Yes | - | Yes | - | - |

---

## 10. Filter Options

| Filter | Match Condition |
|--------|----------------|
| Active | `status.active == true` |
| Approval Pending | `status.pendingApproval == true` |
| Closed | `status.closed == true` |
| Waiting for Disburse | `status.waitingForDisbursal == true` |
| Overpaid | `status.overpaid == true` |
| In Arrears | `inArrears == true` |
| Withdrawn | `status.overpaid == true` |
| Personal | `productName contains "personal"` |
| Bronze | `productName contains "bronze"` |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial spec based on implementation code |

# Dashboard - Feature Specification

> **Purpose**: Unified account management dashboard for end users
> **User Value**: Single view of all financial accounts with quick actions
> **Last Updated**: 2025-12-28
> **Status**: Production Design

---

## 1. Overview

### 1.1 Feature Summary
The Dashboard provides a comprehensive, unified view of the user's complete financial portfolio. It aggregates savings, loans, and share accounts into an intuitive interface with real-time balances, quick actions, and seamless navigation to detailed account management.

### 1.2 User Stories
- As a user, I want to see my total net worth at a glance
- As a user, I want to quickly identify which accounts need attention
- As a user, I want to transfer money with minimal steps
- As a user, I want to track my loan repayment progress
- As a user, I want to see recent transactions across all accounts
- As a user, I want to manage beneficiaries for quick transfers

### 1.3 Design Principles
- **Clarity**: Information hierarchy prioritizes most important data
- **Speed**: One-tap access to frequent actions
- **Trust**: Clear display of financial figures with currency formatting
- **Accessibility**: WCAG 2.1 AA compliant design

---

## 2. Screen Layout

### 2.1 Main Dashboard

```
┌─────────────────────────────────────────────────────────────┐
│  ← Dashboard                                        🔔 ⚙️   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Good morning, John                                         │
│  December 28, 2025                                          │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  TOTAL NET WORTH                              👁    │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │                                               │  │   │
│  │  │         $ 45,750.00                           │  │   │
│  │  │                                               │  │   │
│  │  │    ↑ +$1,250.00 this month                    │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  │                                                      │   │
│  │  Savings    Loans      Shares                       │   │
│  │  $52,500    -$6,750    $0                           │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  QUICK ACTIONS                                       │   │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐       │   │
│  │  │   💸   │ │   📥   │ │   📊   │ │   👥   │       │   │
│  │  │Transfer│ │Deposit │ │ Invest │ │ Benef. │       │   │
│  │  └────────┘ └────────┘ └────────┘ └────────┘       │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  MY ACCOUNTS                                    View All →  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  💰 Primary Savings                    $ 35,000.00   │   │
│  │     SA-0001234567  •  Active                         │   │
│  │     Interest Rate: 4.5% p.a.                         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  💰 Emergency Fund                     $ 17,500.00   │   │
│  │     SA-0001234568  •  Active                         │   │
│  │     Interest Rate: 3.2% p.a.                         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  🏦 Personal Loan                      -$ 6,750.00   │   │
│  │     LA-0009876543  •  Active                         │   │
│  │     ████████░░  80% paid  •  Due: Jan 15             │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  RECENT ACTIVITY                            View All →      │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Today                                               │   │
│  │  ┌─────────────────────────────────────────────┐    │   │
│  │  │ ↓ Salary Credit          + $4,500.00        │    │   │
│  │  │   Primary Savings • 09:30 AM                │    │   │
│  │  └─────────────────────────────────────────────┘    │   │
│  │  ┌─────────────────────────────────────────────┐    │   │
│  │  │ ↑ Bill Payment           - $150.00          │    │   │
│  │  │   Primary Savings • 08:15 AM                │    │   │
│  │  └─────────────────────────────────────────────┘    │   │
│  │                                                      │   │
│  │  Yesterday                                           │   │
│  │  ┌─────────────────────────────────────────────┐    │   │
│  │  │ ↓ Interest Credit        + $125.50          │    │   │
│  │  │   Emergency Fund • 11:59 PM                 │    │   │
│  │  └─────────────────────────────────────────────┘    │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  [Powered by Mifos]                                         │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Account Detail View

```
┌─────────────────────────────────────────────────────────────┐
│  ← Primary Savings                              ⋮           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                                                      │   │
│  │         $ 35,000.00                                  │   │
│  │         Available Balance                            │   │
│  │                                                      │   │
│  │    Account: SA-0001234567                           │   │
│  │    Status: Active  •  Since: Jan 2023               │   │
│  │                                                      │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐          │
│  │ Transfer│ │ Withdraw│ │Statement│ │  QR Pay │          │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘          │
│                                                             │
│  ACCOUNT DETAILS                                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Product Name        Savings Plus                    │   │
│  │  Interest Rate       4.5% per annum                  │   │
│  │  Total Deposits      $ 42,500.00                     │   │
│  │  Total Withdrawals   $ 7,500.00                      │   │
│  │  Total Interest      $ 1,250.00                      │   │
│  │  Minimum Balance     $ 500.00                        │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  TRANSACTIONS                               Filter ▼        │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Dec 28  Salary Credit           + $4,500.00        │   │
│  │  Dec 27  Transfer to John        - $500.00          │   │
│  │  Dec 26  Interest Posting        + $45.50           │   │
│  │  Dec 25  ATM Withdrawal          - $200.00          │   │
│  │  Dec 24  Online Purchase         - $89.99           │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  CHARGES                                                    │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Monthly Maintenance    Due: Jan 1    $ 5.00        │   │
│  │  Annual Fee             Paid: Dec 1   $ 25.00  ✓    │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 2.3 Transfer Flow

```
┌─────────────────────────────────────────────────────────────┐
│  ← Transfer Money                                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  FROM ACCOUNT                                               │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  💰 Primary Savings                          ▼      │   │
│  │     Available: $35,000.00                           │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  TO                                                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  ○ My Account    ○ Beneficiary    ○ New Recipient   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  SELECT BENEFICIARY                                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  👤 Jane Doe                                        │   │
│  │     ****4567  •  Mifos Bank                         │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  👤 Mike Smith                                      │   │
│  │     ****8901  •  Mifos Bank                         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  AMOUNT                                                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  $                                                   │   │
│  │            500.00                                    │   │
│  │                                                      │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  REMARKS (Optional)                                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Payment for dinner                                  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              CONTINUE TO REVIEW                      │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. Components Breakdown

### 3.1 Net Worth Card

| Property | Type | Description |
|----------|------|-------------|
| totalNetWorth | Double | Sum of all accounts (savings - loans + shares) |
| monthlyChange | Double | Change from previous month |
| savingsTotal | Double | Sum of all savings account balances |
| loansTotal | Double | Sum of all loan outstanding balances |
| sharesTotal | Double | Sum of all share account values |
| isAmountVisible | Boolean | Toggle for privacy mode |
| currency | Currency | User's primary currency |

### 3.2 Account Card

| Property | Type | Description |
|----------|------|-------------|
| accountId | Long | Unique account identifier |
| accountNo | String | Display account number |
| productName | String | Account product type |
| balance | Double | Current balance |
| status | AccountStatus | ACTIVE, PENDING, CLOSED |
| accountType | AccountType | SAVINGS, LOAN, SHARE |
| interestRate | Double | Annual interest rate |
| progressPercent | Int? | Loan repayment progress (loans only) |
| nextDueDate | Date? | Next payment due (loans only) |

### 3.3 Transaction Item

| Property | Type | Description |
|----------|------|-------------|
| transactionId | Long | Unique transaction ID |
| transactionType | TransactionType | CREDIT, DEBIT |
| amount | Double | Transaction amount |
| description | String | Transaction description |
| accountName | String | Associated account |
| timestamp | DateTime | Transaction date/time |
| runningBalance | Double | Balance after transaction |

### 3.4 Quick Action

| Action | Icon | Route | Permission |
|--------|------|-------|------------|
| Transfer | 💸 | /transfer | Always |
| Deposit | 📥 | /deposit | If savings exists |
| Invest | 📊 | /share-apply | Always |
| Beneficiary | 👥 | /beneficiary | Always |
| Pay Loan | 🏦 | /loan-payment | If loan exists |
| Statement | 📄 | /statement | Always |

---

## 4. State Model

```kotlin
@Immutable
data class DashboardState(
    // User Info
    val clientId: Long = 0,
    val firstName: String = "",
    val greeting: String = "",
    val profileImage: ByteArray? = null,

    // Aggregated Balances
    val totalNetWorth: Double = 0.0,
    val monthlyChange: Double = 0.0,
    val savingsTotal: Double = 0.0,
    val loansTotal: Double = 0.0,
    val sharesTotal: Double = 0.0,
    val currency: String = "USD",
    val currencySymbol: String = "$",
    val decimalPlaces: Int = 2,

    // Account Lists
    val savingsAccounts: List<SavingAccount> = emptyList(),
    val loanAccounts: List<LoanAccount> = emptyList(),
    val shareAccounts: List<ShareAccount> = emptyList(),

    // Recent Activity
    val recentTransactions: List<Transaction> = emptyList(),

    // UI State
    val isAmountVisible: Boolean = true,
    val isRefreshing: Boolean = false,
    val notificationCount: Int = 0,
    val networkStatus: Boolean = true,
    val uiState: DashboardUiState = DashboardUiState.Loading,
    val dialogState: DialogState? = null,
)

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data object Success : DashboardUiState
    data class Error(val message: StringResource) : DashboardUiState
    data object NetworkError : DashboardUiState
    data object Empty : DashboardUiState
}

sealed interface DashboardAction {
    // Navigation
    data class OnAccountClick(val accountId: Long, val type: AccountType) : DashboardAction
    data class OnTransactionClick(val transactionId: Long) : DashboardAction
    data object OnViewAllAccounts : DashboardAction
    data object OnViewAllTransactions : DashboardAction
    data object OnNotificationClick : DashboardAction
    data object OnSettingsClick : DashboardAction

    // Quick Actions
    data object OnTransferClick : DashboardAction
    data object OnDepositClick : DashboardAction
    data object OnInvestClick : DashboardAction
    data object OnBeneficiaryClick : DashboardAction

    // Data Actions
    data object OnRefresh : DashboardAction
    data object OnRetry : DashboardAction
    data object ToggleAmountVisibility : DashboardAction
    data object DismissDialog : DashboardAction

    // Internal
    sealed interface Internal : DashboardAction {
        data class ReceiveClientData(val data: DataState<Client>) : Internal
        data class ReceiveAccountsData(val data: DataState<ClientAccounts>) : Internal
        data class ReceiveTransactionsData(val data: DataState<List<Transaction>>) : Internal
        data class ReceiveNetworkStatus(val isOnline: Boolean) : Internal
    }
}

sealed interface DashboardEvent {
    data class NavigateToAccount(val accountId: Long, val type: AccountType) : DashboardEvent
    data class NavigateToTransaction(val transactionId: Long) : DashboardEvent
    data object NavigateToAccounts : DashboardEvent
    data object NavigateToTransactions : DashboardEvent
    data object NavigateToNotifications : DashboardEvent
    data object NavigateToSettings : DashboardEvent
    data object NavigateToTransfer : DashboardEvent
    data object NavigateToDeposit : DashboardEvent
    data object NavigateToInvest : DashboardEvent
    data object NavigateToBeneficiary : DashboardEvent
    data class ShowToast(val message: StringResource) : DashboardEvent
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Priority |
|----------|--------|---------|----------|
| /self/clients/{id} | GET | Client details & greeting | P0 |
| /self/clients/{id}/accounts | GET | All account summaries | P0 |
| /self/clients/{id}/images | GET | Profile image | P1 |
| /self/clients/{id}/transactions | GET | Recent transactions | P0 |
| /self/savingsaccounts/{id} | GET | Savings details | P0 |
| /self/loans/{id} | GET | Loan details | P0 |
| /self/beneficiaries/tpt | GET | Beneficiary list | P1 |
| /self/accounttransfers/template | GET | Transfer options | P1 |
| /self/accounttransfers | POST | Execute transfer | P0 |

---

## 6. Data Aggregation Logic

### 6.1 Net Worth Calculation
```kotlin
fun calculateNetWorth(accounts: ClientAccounts): Double {
    val savingsTotal = accounts.savingsAccounts
        ?.filter { it.status?.active == true }
        ?.sumOf { it.accountBalance ?: 0.0 } ?: 0.0

    val loansTotal = accounts.loanAccounts
        .filter { it.status?.active == true }
        .sumOf { it.loanBalance ?: 0.0 }

    val sharesTotal = accounts.shareAccounts
        .filter { it.status?.active == true }
        .sumOf { (it.totalApprovedShares ?: 0) * (it.unitPrice ?: 0.0) }

    return savingsTotal - loansTotal + sharesTotal
}
```

### 6.2 Monthly Change Calculation
```kotlin
fun calculateMonthlyChange(
    currentTransactions: List<Transaction>,
    startOfMonth: Date
): Double {
    return currentTransactions
        .filter { it.date >= startOfMonth }
        .sumOf {
            if (it.transactionType?.deposit == true) it.amount ?: 0.0
            else -(it.amount ?: 0.0)
        }
}
```

### 6.3 Loan Progress Calculation
```kotlin
fun calculateLoanProgress(loan: LoanAccount): Int {
    val principal = loan.principal ?: return 0
    val outstanding = loan.loanBalance ?: return 100
    val paid = principal - outstanding
    return ((paid / principal) * 100).toInt().coerceIn(0, 100)
}
```

---

## 7. Greeting Logic

```kotlin
fun getGreeting(hour: Int, firstName: String): String {
    val timeGreeting = when (hour) {
        in 0..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..20 -> "Good evening"
        else -> "Good night"
    }
    return "$timeGreeting, $firstName"
}
```

---

## 8. Error Handling

| Scenario | UI Behavior | Recovery |
|----------|-------------|----------|
| No internet | Show cached data + banner | Auto-retry on reconnect |
| API timeout | Show error state | Pull-to-refresh |
| Auth expired | Navigate to login | Re-authenticate |
| No accounts | Show empty state + CTA | "Apply for Account" button |
| Partial load | Show available data | Silent background retry |

---

## 9. Performance Requirements

| Metric | Target | Implementation |
|--------|--------|----------------|
| First paint | < 500ms | Skeleton loading |
| Data load | < 2s | Parallel API calls |
| Interaction | < 100ms | Optimistic UI |
| Scroll | 60fps | LazyColumn with keys |
| Memory | < 50MB | Image compression |

---

## 10. Accessibility

| Feature | Implementation |
|---------|----------------|
| Screen Reader | contentDescription on all elements |
| Font Scaling | Supports 200% scaling |
| Color Contrast | WCAG AA (4.5:1 minimum) |
| Touch Targets | 48dp minimum |
| Focus Navigation | Logical tab order |
| Motion | Respects reduced motion setting |

---

## 11. Security Considerations

| Feature | Implementation |
|---------|----------------|
| Amount Masking | Toggle to hide sensitive data |
| Session Timeout | Auto-logout after 5 min inactive |
| Biometric | Required for transfers > $1000 |
| Screenshot | Blocked on sensitive screens |
| Clipboard | Auto-clear after 60 seconds |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-28 | Initial production-level specification |

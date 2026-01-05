# Recent Transactions - Feature Specification

> **Purpose**: Display transaction history for savings accounts with filtering capabilities
> **User Value**: View and filter transaction history to track financial activity
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Recent Transactions screen displays a list of transactions for the user's savings accounts. Users can filter transactions by account and by transaction type (All, Debit, Credit). The screen supports viewing transaction details by tapping on individual transactions.

### 1.2 User Stories
- As a user, I want to see my transaction history so I can track my spending and deposits
- As a user, I want to filter transactions by account so I can view activity for a specific account
- As a user, I want to filter by transaction type (credit/debit) so I can focus on specific transaction categories
- As a user, I want to see transaction details so I can understand the full context of each transaction
- As a user, I want to see the date and amount of each transaction so I can reconcile my records

---

## 2. Screen Layout

### 2.1 ASCII Mockup

```
+-------------------------------------------+
|  [<] Transaction History           [Filter]|  <- TopBar with back & filter icons
|      A/c No: 000000001                     |  <- Selected account subtitle
+-------------------------------------------+
|                                           |
|  +---------------------------------------+|
|  | CREDIT                    + $500.00   ||  <- Transaction item
|  | 15 Sep 2025                           ||
|  +---------------------------------------+|
|  |---------------------------------------|
|  +---------------------------------------+|
|  | DEBIT                     - $150.00   ||
|  | 14 Sep 2025                           ||
|  +---------------------------------------+|
|  |---------------------------------------|
|  +---------------------------------------+|
|  | CREDIT                    + $1,200.00 ||
|  | 10 Sep 2025                           ||
|  +---------------------------------------+|
|  |---------------------------------------|
|  +---------------------------------------+|
|  | DEBIT                     - $75.50    ||
|  | 8 Sep 2025                            ||
|  +---------------------------------------+|
|                                           |
+-------------------------------------------+

+-------------------------------------------+
|  FILTER BOTTOM SHEET                      |
+-------------------------------------------+
|  Filter Transactions        [Clear All]   |
|  -----------------------------------------|
|  Filter By Account:                       |
|  +---------------------------------------+|
|  | A/c No: 000000001            [v]      ||
|  | Balance: 5,000.00 USD                 ||
|  +---------------------------------------+|
|                                           |
|  Transaction Type:                        |
|  +-------+ +-------+ +-------+            |
|  | [All] | |Debits | |Credits|            |
|  +-------+ +-------+ +-------+            |
|                                           |
|  [         Apply Filters          ]       |
|                                           |
+-------------------------------------------+
```

### 2.2 Sections Table

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | TopBar | Title, back button, filter icon | - | P0 |
| 2 | Account Subtitle | Selected account number display | - | P0 |
| 3 | Transaction List | Scrollable list of transactions | savingsaccounts/{id}?associations=transactions | P0 |
| 4 | Filter Bottom Sheet | Account selector + type filter | clients/{id}/accounts | P1 |
| 5 | Empty State | Shown when no transactions | - | P0 |
| 6 | Loading State | Shown during data fetch | - | P0 |
| 7 | Error State | Shown on API failure with retry | - | P0 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Navigate back | Tap back arrow | Return to previous screen | - |
| Toggle filter | Tap filter icon | Show/hide filter bottom sheet | - |
| Select account | Tap account dropdown | Show available accounts | - |
| Change account | Select from dropdown | Load transactions for selected account | savingsaccounts/{id}?associations=transactions |
| Select transaction type | Tap All/Debits/Credits chip | Apply client-side filter | - |
| Apply filter | Tap "Apply Filters" button | Close sheet, apply filters | savingsaccounts/{id}?associations=transactions (if account changed) |
| Clear filter | Tap "Clear All" | Reset to first account, ALL type | savingsaccounts/{id}?associations=transactions |
| Tap transaction | Click transaction row | Navigate to transaction details | - |
| Retry on error | Tap "Retry" button | Reload accounts and transactions | clients/{id}/accounts |

---

## 4. State Model

```kotlin
enum class TransactionFilterType {
    ALL,
    DEBIT,
    CREDIT,
}

data class RecentTransactionUiState(
    val clientId: Long? = null,
    val viewState: ViewState,
    val transactions: List<Transactions> = emptyList(),
    val accounts: List<SavingAccount> = emptyList(),
    val selectedAccount: SavingAccount? = null,
    val filterType: TransactionFilterType = TransactionFilterType.ALL,
    val showFilter: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val canPaginate: Boolean = false,
    val isNetworkAvailable: Boolean = false,
) {
    sealed interface ViewState {
        data object Loading : ViewState
        data object Empty : ViewState
        data class Error(val message: String?) : ViewState
        data class Content(val list: List<Transactions>) : ViewState
    }
}

sealed interface RecentTransactionAction {
    data object LoadInitial : RecentTransactionAction
    data object Refresh : RecentTransactionAction
    data class LoadMore(val offset: Int) : RecentTransactionAction
    data object ToggleFilter : RecentTransactionAction
    data class ApplyFilter(val account: SavingAccount, val type: TransactionFilterType) : RecentTransactionAction
    data object ClearFilter : RecentTransactionAction
    data class OnTransactionClick(val transaction: Transactions) : RecentTransactionAction

    sealed interface Internal : RecentTransactionAction {
        data class AccountsLoaded(val accounts: List<SavingAccount>) : Internal
        data class TransactionsLoaded(val items: List<Transactions>) : Internal
        data class LoadFailed(val error: Throwable?) : Internal
    }
}

sealed interface RecentTransactionEvent {
    data class NavigateToDetails(
        val transactionId: String,
        val accountType: String,
        val accountId: Long,
    ) : RecentTransactionEvent
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/self/clients/{clientId}/accounts` | GET | Fetch savings accounts for filter | Implemented |
| `/self/savingsaccounts/{accountId}?associations=transactions` | GET | Fetch transactions for account | Implemented |

---

## 6. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Show network unavailable | isNetworkAvailable = false |
| No accounts | Set viewState to Empty | "No recent transactions found." |
| No transactions | Set viewState to Empty | "No recent transactions found." |
| API error | Set viewState to Error | Error message + Retry button |
| Filter returns empty | Set viewState to Empty | "No recent transactions found." |
| Account not selected | Skip loading transactions | Empty state |
| Only inactive accounts | Filter accounts by active status | Show only active accounts |

---

## 7. Components

| Component | Props | Reusable? |
|-----------|-------|-----------|
| TransactionItem | transaction: Transactions, onClick: () -> Unit | Yes |
| TransactionList | transactions: List<Transactions>, onTransactionClick: (Transactions) -> Unit | Yes |
| ErrorScreen | message: String, onRetry: () -> Unit | Yes |
| TransactionFilterSheetContent | accounts, currentAccount, currentFilterType, onApply, onClear | No |
| FilterOptionChip | label: String, isSelected: Boolean, onClick: () -> Unit | Yes |

---

## 8. Transaction Type Logic

The system determines if a transaction is a credit or debit based on:

```kotlin
private fun isTransactionCreditLogic(transaction: Transactions): Boolean {
    val type = transaction.transactionType?.value?.lowercase().orEmpty()

    return when {
        transaction.transactionType?.deposit == true -> true
        transaction.transactionType?.withdrawal == true -> false
        type.contains("deposit") -> true
        type.contains("interest") -> true
        type.contains("withdrawal") -> false
        type.contains("fee") -> false
        else -> false
    }
}
```

**Credit types**: deposit, interest posting, dividend payout
**Debit types**: withdrawal, fee deduction, overdraft fee, withhold tax

---

## 9. Navigation

| Route | Constant | Description |
|-------|----------|-------------|
| `recent_transaction_base_route` | RECENT_TRANSACTION_NAVIGATION_ROUTE_BASE | Base navigation route |
| `recent_transaction_screen_route` | RECENT_TRANSACTION_SCREEN_ROUTE | Main screen route |

---

## 10. Dependencies

| Dependency | Purpose |
|------------|---------|
| AccountsRepository | Fetch savings accounts list |
| SavingsAccountRepository | Fetch transactions with associations |
| NetworkMonitor | Monitor network connectivity |
| UserPreferencesRepository | Get client ID |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification based on implementation |

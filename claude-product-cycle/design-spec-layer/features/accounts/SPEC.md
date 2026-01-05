# Accounts - Feature Specification

> **Purpose**: Unified account listing with type-specific filtering for savings, loans, and shares
> **User Value**: Browse and manage all financial accounts with powerful filtering capabilities
> **Last Updated**: 2025-12-29
> **Status**: Production Design

---

## 1. Overview

### 1.1 Feature Summary
The Accounts feature provides a comprehensive view of user accounts organized by type (Savings, Loan, Share). Users can browse account lists, apply multi-criteria filters (by account type and status), view transactions history, and access detailed transaction information. The feature supports pull-to-refresh for real-time data updates and maintains filter state across sessions.

### 1.2 User Stories
- As a user, I want to view all my savings accounts in one place
- As a user, I want to filter accounts by type (wallet, bank, group, NB)
- As a user, I want to filter accounts by status (active, pending, closed, matured, approved)
- As a user, I want to see transaction history for each account
- As a user, I want to filter transactions by type (credit/debit) and duration
- As a user, I want to view detailed information about a specific transaction
- As a user, I want to refresh account data with pull-to-refresh gesture

### 1.3 Design Principles
- **Consistency**: Same filter pattern across all account types
- **Efficiency**: Quick access to filtered views with minimal taps
- **Clarity**: Clear distinction between account types and statuses
- **Responsiveness**: Real-time updates via pull-to-refresh

---

## 2. Screen Layout

### 2.1 Accounts List Screen

```
+-------------------------------------------------------------+
|  <- Savings Accounts                                    ...  |
+-------------------------------------------------------------+
|                                                             |
|  +-------------------------------------------------------+  |
|  |  [FILTER ICON]                                 Filter |  |
|  +-------------------------------------------------------+  |
|                                                             |
|  +-------------------------------------------------------+  |
|  |  Wallet Savings Account                               |  |
|  |  SA-0001234567  |  Active                             |  |
|  |  Balance: $12,500.00                                  |  |
|  |  Interest: 4.5% p.a.                                  |  |
|  +-------------------------------------------------------+  |
|                                                             |
|  +-------------------------------------------------------+  |
|  |  Bank Savings Account                                 |  |
|  |  SA-0001234568  |  Active                             |  |
|  |  Balance: $8,750.00                                   |  |
|  |  Interest: 3.2% p.a.                                  |  |
|  +-------------------------------------------------------+  |
|                                                             |
|  +-------------------------------------------------------+  |
|  |  Group Savings Account                                |  |
|  |  SA-0001234569  |  Pending                            |  |
|  |  Balance: $0.00                                       |  |
|  |  Interest: 2.5% p.a.                                  |  |
|  +-------------------------------------------------------+  |
|                                                             |
|  +-------------------------------------------------------+  |
|  |            [Powered by Mifos]                         |  |
|  +-------------------------------------------------------+  |
+-------------------------------------------------------------+
```

### 2.2 Filter Bottom Sheet

```
+-------------------------------------------------------------+
|                          Filters                             |
+-------------------------------------------------------------+
|                                                             |
|  [Reset Filters]                              [Apply]       |
|                                                             |
|  ---------------------------------------------------------  |
|                                                             |
|  Account Type                               (2 selected) v  |
|  ---------------------------------------------------------  |
|    [ ] Wallet Account                                       |
|    [x] Bank Account                                         |
|    [x] Group Account                                        |
|    [ ] NB Account                                           |
|                                                             |
|  ---------------------------------------------------------  |
|                                                             |
|  Account Status                             (1 selected) v  |
|  ---------------------------------------------------------  |
|    [x] Active                                               |
|    [ ] Pending                                              |
|    [ ] Closed                                               |
|    [ ] Matured                                              |
|    [ ] Approved                                             |
|                                                             |
+-------------------------------------------------------------+
```

### 2.3 Transaction History Screen

```
+-------------------------------------------------------------+
|  <- Transaction History                                      |
+-------------------------------------------------------------+
|                                                             |
|                                              [Filter]       |
|                                                             |
|  Today                                                      |
|  +-------------------------------------------------------+  |
|  |  v Deposit                              + $4,500.00   |  |
|  |    Dec 29, 2025                                       |  |
|  +-------------------------------------------------------+  |
|  |  ^ Withdrawal                           - $150.00     |  |
|  |    Dec 29, 2025                                       |  |
|  +-------------------------------------------------------+  |
|                                                             |
|  Yesterday                                                  |
|  +-------------------------------------------------------+  |
|  |  v Interest Posting                     + $125.50     |  |
|  |    Dec 28, 2025                                       |  |
|  +-------------------------------------------------------+  |
|  |  ^ Transfer Out                         - $500.00     |  |
|  |    Dec 28, 2025                                       |  |
|  +-------------------------------------------------------+  |
|                                                             |
|  +-------------------------------------------------------+  |
|  |            [Powered by Mifos]                         |  |
|  +-------------------------------------------------------+  |
+-------------------------------------------------------------+
```

### 2.4 Transaction Details Screen

```
+-------------------------------------------------------------+
|  <- Transaction Details                                      |
+-------------------------------------------------------------+
|                                                             |
|              +------------------+                           |
|              |       v          |                           |
|              |   (Credit Icon)  |                           |
|              +------------------+                           |
|                                                             |
|                   $4,500.00                                 |
|                    Deposit                                  |
|                                                             |
|  +-------------------------------------------------------+  |
|  |  Transaction ID           12345                       |  |
|  |  -------------------------------------------------    |  |
|  |  Date                     Dec 29, 2025                |  |
|  |  -------------------------------------------------    |  |
|  |  Status                   Success                     |  |
|  |  -------------------------------------------------    |  |
|  |  Account Ref              SA-0001234567               |  |
|  |  -------------------------------------------------    |  |
|  |  Type                     Deposit                     |  |
|  |  -------------------------------------------------    |  |
|  |  External ID              EXT-12345                   |  |
|  |  -------------------------------------------------    |  |
|  |                                                       |  |
|  |  BREAKDOWN                                            |  |
|  |  -------------------------------------------------    |  |
|  |  Principal                $4,400.00                   |  |
|  |  -------------------------------------------------    |  |
|  |  Interest                 $100.00                     |  |
|  |  -------------------------------------------------    |  |
|  |  Fees                     $0.00                       |  |
|  |  -------------------------------------------------    |  |
|  |  Penalties                $0.00                       |  |
|  |  -------------------------------------------------    |  |
|  |                                                       |  |
|  |  Balance                  $12,500.00                  |  |
|  +-------------------------------------------------------+  |
|                                                             |
+-------------------------------------------------------------+
```

---

## 3. Sections Table

| Section | Description | Components |
|---------|-------------|------------|
| Top Bar | Navigation and screen title | Back button, title (Savings/Loan/Share Accounts) |
| Filter Action | Opens filter bottom sheet | Filter button with icon |
| Account List | Scrollable list of accounts | Account cards with balance, status, interest rate |
| Filter Bottom Sheet | Multi-criteria filter dialog | Type filters, status filters, reset/apply buttons |
| Transaction List | Grouped by date | Transaction items with type, amount, date |
| Transaction Filter | Transaction-specific filters | Type (Credit/Debit), Duration (1mo/3mo/6mo/1yr/2yr) |
| Transaction Details | Full transaction information | Header, details table, breakdown section |
| Powered Footer | Branding footer | Mifos branding card |

---

## 4. User Interactions Table

| Interaction | Action | Result |
|-------------|--------|--------|
| Tap Back Button | OnNavigateBack | Navigate to previous screen |
| Tap Filter Button | ToggleFilter | Open filter bottom sheet |
| Tap Account Card | OnAccountClicked | Navigate to account details |
| Toggle Checkbox | ToggleCheckbox | Update filter selection |
| Tap Reset Filters | ResetFilters | Clear all filter selections |
| Tap Apply Filters | GetFilterResults | Apply filters and close sheet |
| Pull to Refresh | Refresh | Reload account data from API |
| Tap Transaction Item | OnTransactionClick | Navigate to transaction details |
| Expand/Collapse Filter Section | ToggleTypeExpanded/ToggleStatusExpanded | Show/hide filter options |

---

## 5. State Model

### 5.1 AccountsState (from AccountsViewModel.kt)

```kotlin
internal data class AccountsState(
    val isRefreshing: Boolean = false,
    val checkboxOptions: List<CheckboxStatus> = emptyList(),
    val selectedFilters: List<CheckboxStatus> = emptyList(),
    val accountType: AccountType = AccountType.SAVINGS,
    val toggleFilterDialog: Boolean = false,
    val accountTypeFiltersCount: Int? = 0,
    val accountStatusFiltersCount: Int? = 0,
    val refreshSignal: Long = Clock.System.now().epochSeconds,
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val isTypeExpanded: Boolean = true,
    val isStatusExpanded: Boolean = true,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Filters : DialogState
    }

    val isAnyFilterSelected = checkboxOptions.any { it.isChecked }
}
```

### 5.2 AccountTransactionState (from TransactionViewModel.kt)

```kotlin
internal data class AccountTransactionState(
    val clientId: Long,
    val accountType: String,
    val accountId: Long,
    val isRefreshing: Boolean = false,
    val data: List<UiTransaction> = emptyList(),
    val filteredData: Map<String, List<UiTransaction>> = emptyMap(),
    val dialogState: DialogState? = null,
    val checkboxOptions: List<TransactionCheckboxStatus> = emptyList(),
    val selectedFilters: List<TransactionCheckboxStatus> = emptyList(),
    val toggleFilterDialog: Boolean = false,
    val accountTypeFiltersCount: Int? = 0,
    val accountDurationFiltersCount: Int? = 0,
    val selectedRadioButton: StringResource? = null,
    val isFilteredRecordsEmpty: Boolean = false,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Filters : DialogState
    }

    val isAnyFilterSelected = checkboxOptions.any { it.isChecked } || selectedRadioButton != null
}
```

### 5.3 TransactionDetailsState (from TransactionDetailsViewModel.kt)

```kotlin
data class TransactionDetailsState(
    val transactionId: Long,
    val accountId: Long,
    val accountType: String,
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val transaction: UiTransactionDetails? = null,
)
```

### 5.4 Actions

```kotlin
// Accounts Actions
sealed interface AccountsAction {
    data object ToggleFilter : AccountsAction
    data object ToggleTypeExpanded : AccountsAction
    data object ToggleStatusExpanded : AccountsAction
    data object ResetFilters : AccountsAction
    data object DismissDialog : AccountsAction
    data object GetFilterResults : AccountsAction
    data object OnNavigateBack : AccountsAction
    data object Refresh : AccountsAction
    data object RefreshCompleted : AccountsAction
    data class OnAccountClicked(val accountId: Long, val accountType: String) : AccountsAction
    data class SetCheckboxFilterList(val checkBoxList: List<CheckboxStatus>, val accountType: String) : AccountsAction
    data class ToggleCheckbox(val label: StringResource, val type: FilterType) : AccountsAction
}

// Transaction Actions
sealed interface AccountTransactionAction {
    data object Refresh : AccountTransactionAction
    data object DismissDialog : AccountTransactionAction
    data object OnNavigateBackClick : AccountTransactionAction
    data object ToggleFilter : AccountTransactionAction
    data object ResetFilters : AccountTransactionAction
    data object GetFilterResults : AccountTransactionAction
    data class ReceiveNetworkResult(val isOnline: Boolean) : AccountTransactionAction
    data class OnTransactionClick(val id: Long?) : AccountTransactionAction
    data class ToggleCheckbox(val label: StringResource, val type: TransactionFilterType) : AccountTransactionAction
    data class ToggleRadioButton(val label: StringResource) : AccountTransactionAction
}

// Transaction Details Actions
sealed interface TransactionDetailsAction {
    data object OnNavigateBack : TransactionDetailsAction
    data object Retry : TransactionDetailsAction
}
```

### 5.5 Events

```kotlin
// Accounts Events
sealed interface AccountsEvent {
    data object NavigateBack : AccountsEvent
    data class AccountClicked(val accountId: Long, val accountType: String) : AccountsEvent
}

// Transaction Events
sealed interface AccountTransactionEvent {
    data object OnNavigateBack : AccountTransactionEvent
    data class NavigateToDetails(val id: String) : AccountTransactionEvent
}

// Transaction Details Events
sealed interface TransactionDetailsEvent {
    data object NavigateBack : TransactionDetailsEvent
}
```

---

## 6. Filter Options

### 6.1 Savings Account Filters

| Filter Type | Options |
|-------------|---------|
| Account Type | Wallet Account, Bank Account, Group Account, NB Account |
| Account Status | Active, Pending, Closed, Matured, Approved |

### 6.2 Loan Account Filters

| Filter Type | Options |
|-------------|---------|
| Account Type | Personal, Bronze |
| Account Status | Active, Approval Pending, Closed, Disburse, Overpaid, In Arrears, Withdrawn, Matured, Rejected |

### 6.3 Share Account Filters

| Filter Type | Options |
|-------------|---------|
| Account Type | Wallet Account, Bank Account |
| Account Status | Active, Approval Pending, Closed, Disburse, Overpaid, In Arrears, Withdrawn, Matured, Rejected |

### 6.4 Transaction Filters

| Filter Type | Options |
|-------------|---------|
| Transaction Type | Credit, Debit |
| Duration | Past Month, Past 3 Months, Past 6 Months, Past 1 Year, Past 2 Years |

---

## 7. API Requirements Table

| Endpoint | Method | Purpose | Priority |
|----------|--------|---------|----------|
| /self/clients/{clientId}/accounts | GET | Fetch all accounts | P0 |
| /self/savingsaccounts/{accountId} | GET | Fetch savings details with transactions | P0 |
| /self/loans/{loanId} | GET | Fetch loan details with transactions | P0 |
| /self/shareaccounts/{accountId} | GET | Fetch share account details | P0 |
| /self/clients/{clientId}/transactions | GET | Fetch recent transactions | P0 |
| /self/savingsaccounts/{accountId}/transactions/{transactionId} | GET | Fetch savings transaction details | P1 |
| /self/loans/{loanId}/transactions/{transactionId} | GET | Fetch loan transaction details | P1 |

---

## 8. Edge Cases & Error Handling

| Scenario | UI Behavior | Recovery |
|----------|-------------|----------|
| No internet | Show network error state | Retry button, auto-retry on reconnect |
| API timeout | Show error state with message | Pull-to-refresh or Retry button |
| No accounts | Show empty state | Display "No accounts found" message |
| No transactions | Show empty state | Display "No transactions found" |
| No filtered results | Show empty filtered state | Display "No matching transactions" |
| Invalid transaction ID | Show error state | Navigate back or retry |
| Auth expired | Navigate to login | Re-authenticate |
| Partial data load | Show available data | Silent background retry |

---

## 9. Performance Requirements

| Metric | Target | Implementation |
|--------|--------|----------------|
| First paint | < 500ms | Skeleton loading |
| Data load | < 2s | Parallel API calls |
| Filter apply | < 100ms | Client-side filtering |
| Scroll | 60fps | LazyColumn with keys |
| Pull-to-refresh | Responsive | Optimistic UI update |

---

## 10. Navigation Routes

```kotlin
// Account Navigation
@Serializable
data class AccountNavRoute(val accountType: String)

// Transaction Navigation
@Serializable
data class AccountTransactionsNavRoute(
    val accountId: Long,
    val accountType: String,
)

// Transaction Details Navigation
@Serializable
data class TransactionDetailsNavRoute(
    val transactionId: String,
    val accountType: String,
    val accountId: Long,
)
```

---

## 11. Data Models

### 11.1 CheckboxStatus

```kotlin
data class CheckboxStatus(
    val statusLabel: StringResource,
    val isChecked: Boolean = false,
    val type: FilterType = FilterType.ACCOUNT_STATUS,
)

enum class FilterType {
    ACCOUNT_TYPE,
    ACCOUNT_STATUS,
}
```

### 11.2 TransactionCheckboxStatus

```kotlin
data class TransactionCheckboxStatus(
    val statusLabel: StringResource,
    val isChecked: Boolean = false,
    val type: TransactionFilterType = TransactionFilterType.TRANSACTION_TYPE,
)

enum class TransactionFilterType {
    TRANSACTION_TYPE,
    DURATION,
}
```

### 11.3 UiTransaction

```kotlin
data class UiTransaction(
    val id: Long?,
    val date: List<Int>,
    val amount: Double?,
    val type: TransactionType? = null,
    val typeValue: String? = null,
    val isCredit: Boolean?,
    val currency: String,
)
```

### 11.4 UiTransactionDetails

```kotlin
data class UiTransactionDetails(
    val id: Long?,
    val date: List<Int>,
    val amount: Double?,
    val status: String,
    val typeValue: String? = null,
    val isCredit: Boolean?,
    val currency: String,
    val accountNo: String? = null,
    val principal: Double? = null,
    val interest: Double? = null,
    val fees: Double? = null,
    val penalties: Double? = null,
    val externalId: String? = null,
    val outstandingBalance: Double? = null,
)
```

---

## 12. Credit/Debit Logic

```kotlin
fun getTransactionCreditStatus(transactionType: TransactionType?): Boolean {
    return transactionType?.run {
        when {
            deposit == true -> true
            dividendPayout == true -> false
            withdrawal == true -> false
            interestPosting == true -> true
            feeDeduction == true -> false
            initiateTransfer == true -> false
            approveTransfer == true -> false
            withdrawTransfer == true -> false
            rejectTransfer == true -> true
            overdraftFee == true -> false
            else -> true
        }
    } ?: false
}
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification based on existing implementation |

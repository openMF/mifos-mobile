# Savings Account - Feature Specification

> **Purpose**: Display, manage, update, and withdraw savings accounts
> **User Value**: Complete savings account management with balance visibility and transactions
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Savings Account feature provides comprehensive management of user savings accounts. It includes a list view with filtering capabilities, detailed account information, product updates for pending accounts, and withdrawal functionality. Users can view balances, transaction history, charges, transfer funds, and generate QR codes for receiving payments.

### 1.2 User Stories
- As a user, I want to see all my savings accounts so I can track my savings
- As a user, I want to filter accounts by status (Active, Pending, Closed, Inactive) to find specific accounts
- As a user, I want to see my total savings balance at a glance
- As a user, I want to view detailed information about each savings account
- As a user, I want to update the product for pending accounts
- As a user, I want to withdraw pending savings account applications
- As a user, I want to transfer funds from my savings account
- As a user, I want to view transaction history for my accounts
- As a user, I want to generate a QR code to receive payments

---

## 2. Screen Layouts

### 2.1 Savings Account List Screen

```
+------------------------------------------+
|                                          |
|  +---------------------------------+     |
|  |  Savings Dashboard              |     |
|  |  [Total Amount] / [****]    (o) |     |  <- Amount visibility toggle
|  +---------------------------------+     |
|                                          |
|  Savings Account         [n items]   [=] |  <- Filter icon
|  ----------------------------------------|
|                                          |
|  +------------------------------------+  |
|  | [icon]  Account #12345             |  |
|  |         Product Name               |  |
|  |         $1,234.56 / Active    [>]  |  |  <- Status color-coded
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | [icon]  Account #12346             |  |
|  |         Savings Product            |  |
|  |         Pending Approval      [>]  |  |  <- Yellow for pending
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | [icon]  Account #12347             |  |
|  |         Basic Savings              |  |
|  |         Closed                [>]  |  |  <- Default color for closed
|  +------------------------------------+  |
|                                          |
+------------------------------------------+
```

### 2.2 Savings Account Details Screen

```
+------------------------------------------+
|  [<]  Account Details                    |
+------------------------------------------+
|                                   Update |  <- Enabled only for pending
+------------------------------------------+
|                                          |
|  +----------------+ +----------------+   |
|  | Account Number | | Avail. Balance |   |
|  | 000000012345   | | $1,234.56      |   |
|  +----------------+ +----------------+   |
|                                          |
|  +----------------+ +----------------+   |
|  | Status         | | Interest Rate  |   |
|  | Active         | | 5.5%           |   |
|  +----------------+ +----------------+   |
|                                          |
|  +----------------+ +----------------+   |
|  | Tot. Deposits  | | Tot. Withdraws |   |
|  | $5,000.00      | | $3,765.44      |   |
|  +----------------+ +----------------+   |
|                                          |
|  Last Transactions                       |
|  +----------------+ +----------------+   |
|  | Amount         | | Date           |   |
|  | $100.00        | | Dec 28, 2025   |   |
|  +----------------+ +----------------+   |
|                                          |
|  Actions                                 |
|  +------------------+                    |
|  | Transfer         |                    |
|  | Send money       |                    |
|  +------------------+                    |
|  +------------------+                    |
|  | Transactions     |                    |
|  | View history     |                    |
|  +------------------+                    |
|  +------------------+                    |
|  | Charges          |                    |
|  | View fees        |                    |
|  +------------------+                    |
|  +------------------+                    |
|  | QR Code          |                    |
|  | Receive payment  |                    |
|  +------------------+                    |
|                                          |
|  ---------------[ Powered by Mifos ]-----|
+------------------------------------------+
```

### 2.3 Account Update Screen

```
+------------------------------------------+
|  [<]  Update Account                     |
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  | Client Name                        |  |
|  | John Doe                           |  |
|  +------------------------------------+  |
|  | Submission Date                    |  |
|  | Dec 15, 2025                       |  |
|  +------------------------------------+  |
|  | Account Number                     |  |
|  | 000000012345                       |  |
|  +------------------------------------+  |
|  | Product                            |  |
|  | Basic Savings                      |  |
|  +------------------------------------+  |
|                                          |
|  Product                                 |
|  +------------------------------------+  |
|  | Select new product            [v] |   |  <- Dropdown
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |        Submit New Product          |  |  <- Disabled until product selected
|  +------------------------------------+  |
|                                          |
|  ---------------[ Powered by Mifos ]-----|
+------------------------------------------+
```

### 2.4 Account Withdraw Screen

```
+------------------------------------------+
|  [<]  Withdraw Account                   |
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  | Client Name                        |  |
|  | John Doe                           |  |
|  +------------------------------------+  |
|  | Submission Date                    |  |
|  | Dec 15, 2025                       |  |
|  +------------------------------------+  |
|  | Account Number                     |  |
|  | 000000012345                       |  |
|  +------------------------------------+  |
|  | Product                            |  |
|  | Basic Savings                      |  |
|  +------------------------------------+  |
|                                          |
|  Remarks                                 |
|  +------------------------------------+  |
|  |                                    |  |
|  |  [Enter withdrawal reason...]      |  |
|  |                                    |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |        Request Withdrawal          |  |  <- Disabled until remarks entered
|  +------------------------------------+  |
|                                          |
|  ---------------[ Powered by Mifos ]-----|
+------------------------------------------+
```

### 2.5 Sections Table

| # | Screen | Section | Description | API | Priority |
|---|--------|---------|-------------|-----|----------|
| 1 | List | Dashboard Card | Total savings balance | clients/{id}/accounts | P0 |
| 2 | List | Filter Bar | Account count + filter icon | - | P1 |
| 3 | List | Account Cards | List of savings accounts | clients/{id}/accounts | P0 |
| 4 | Details | Action Bar | Update button (conditional) | - | P1 |
| 5 | Details | Info Grid | Account metadata | savingsaccounts/{id} | P0 |
| 6 | Details | Transaction Summary | Last transaction | savingsaccounts/{id}?associations=transactions | P1 |
| 7 | Details | Actions | Transfer, Transactions, Charges, QR | - | P0 |
| 8 | Update | Details Card | Account info display | - | P0 |
| 9 | Update | Product Dropdown | Product selection | savingsaccounts/template | P0 |
| 10 | Withdraw | Details Card | Account info display | - | P0 |
| 11 | Withdraw | Remarks Input | Withdrawal reason | - | P0 |

---

## 3. User Interactions

| Action | Screen | Trigger | Result | API Call |
|--------|--------|---------|--------|----------|
| Toggle balance visibility | List | Tap eye icon | Show/hide amounts | - |
| Filter accounts | List | Tap filter icon | Show filter options | - |
| Tap account card | List | Click card | Navigate to details | - |
| Pull refresh | List | Swipe down | Reload accounts | clients/{id}/accounts |
| Retry on error | List | Tap retry button | Reload data | clients/{id}/accounts |
| Navigate back | All | Tap back arrow | Go to previous screen | - |
| Tap Update | Details | Click Update text | Navigate to update screen | - |
| Tap Transfer | Details | Click action card | Navigate to transfer | - |
| Tap Transactions | Details | Click action card | Navigate to transactions | - |
| Tap Charges | Details | Click action card | Navigate to charges | - |
| Tap QR Code | Details | Click action card | Navigate to QR screen | - |
| Select product | Update | Choose from dropdown | Update selection | - |
| Submit update | Update | Tap submit button | Authenticate then update | PUT savingsaccounts/{id} |
| Enter remarks | Withdraw | Type in text field | Enable submit button | - |
| Submit withdraw | Withdraw | Tap submit button | Authenticate then withdraw | POST savingsaccounts/{id}?command=withdrawnByApplicant |

---

## 4. State Models

### 4.1 Savings Account List State

```kotlin
data class SavingsAccountState(
    val savingsAccount: List<SavingAccount>?,
    val originalAccounts: List<SavingAccount>? = null,
    val firstLaunch: Boolean = true,
    val items: Int? = 0,
    val totalSavingAmount: String? = "",
    val currency: String? = "",
    val decimals: Int? = 2,
    val networkConnection: Boolean? = true,
    val clientId: Long?,
    val dialogState: DialogState?,
    val selectedFilters: List<StringResource?> = emptyList(),
    val isAmountVisible: Boolean = false,
    val isFilteredEmpty: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
    val networkStatus: Boolean = false,
    val statusOrder: List<String> = listOf(
        SavingStatus.ACTIVE.status,
        SavingStatus.SUBMIT_AND_PENDING_APPROVAL.status,
        SavingStatus.CLOSED.status,
        SavingStatus.INACTIVE.status,
    ),
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

sealed interface SavingsAccountAction {
    data object OnFirstLaunched : SavingsAccountAction
    data object OnRetry : SavingsAccountAction
    data object OnDismissDialog : SavingsAccountAction
    data object OnNavigateBack : SavingsAccountAction
    data object ToggleAmountVisible : SavingsAccountAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : SavingsAccountAction
    data class LoadAccounts(val filters: List<StringResource?>) : SavingsAccountAction
    data class OnAccountClicked(val accountId: Long, val accountType: String) : SavingsAccountAction

    sealed interface Internal : SavingsAccountAction {
        data class ReceiveSavingsAccounts(
            val filters: List<StringResource?>,
            val dataState: DataState<ClientAccounts>,
        ) : SavingsAccountAction
    }
}

sealed interface SavingsAccountsEvent {
    data class AccountClicked(val accountId: Long, val accountType: String) : SavingsAccountsEvent
    data object LoadingCompleted : SavingsAccountsEvent
    data object NavigateBack : SavingsAccountsEvent
}
```

### 4.2 Savings Account Details State

```kotlin
@Immutable
data class SavingsAccountDetailsState(
    val accountId: Long = -1L,
    val clientName: String? = "",
    val submissionDate: String? = "",
    val accountNumber: String? = "",
    val product: String? = "",
    val displayItems: List<LabelValueItem> = emptyList(),
    val transactionList: List<LabelValueItem> = emptyList(),
    val savingStatus: SavingStatus? = null,
    val isActive: Boolean = false,
    val items: ImmutableList<SavingsActionItems>,
    val isUpdatable: Boolean = false,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

val SavingStatus.allowedActions: Set<SavingsActionItems>
    get() = when (this) {
        SavingStatus.ACTIVE -> setOf(Transactions, Charges, QrCode, Transfer)
        SavingStatus.INACTIVE -> setOf(Transfer, QrCode)
        SavingStatus.CLOSED -> setOf(QrCode, Transfer, Transactions)
        SavingStatus.SUBMIT_AND_PENDING_APPROVAL -> setOf(QrCode)
    }
```

### 4.3 Account Update State

```kotlin
data class AccountUpdateState(
    val clientId: Long?,
    val accountId: Long?,
    val details: Map<StringResource, String?> = emptyMap(),
    val productOptions: Map<Long, String> = emptyMap(),
    val selectedProductId: Long? = null,
    val selectedProduct: String = "",
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

sealed interface AccountUpdateAction {
    data object OnNavigateBack : AccountUpdateAction
    data object RequestUpdate : AccountUpdateAction
    data object DismissDialog : AccountUpdateAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : AccountUpdateAction
    data object Retry : AccountUpdateAction
    data class OnProductSelected(val id: Long, val product: String) : AccountUpdateAction

    sealed interface Internal : AccountUpdateAction {
        data class ReceiveProducts(val dataState: DataState<SavingsAccountTemplate>) : Internal
        data class ReceiveUpdateRequestResult(val dataState: DataState<String>) : Internal
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal
        data object PerformUpdate : Internal
    }
}
```

### 4.4 Account Withdraw State

```kotlin
data class AccountWithdrawState(
    val accountId: Long?,
    val details: Map<StringResource, String?> = emptyMap(),
    val remark: String = "",
    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
    }
}

sealed interface AccountWithdrawAction {
    data object OnNavigateBack : AccountWithdrawAction
    data object RequestWithdraw : AccountWithdrawAction
    data class RemarkChange(val remark: String) : AccountWithdrawAction
    data object DismissDialog : AccountWithdrawAction

    sealed interface Internal : AccountWithdrawAction {
        data class ReceiveWithdrawRequestResult(val dataState: DataState<String>) : Internal
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal
        data object PerformWithdraw : Internal
    }
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/self/clients/{clientId}/accounts` | GET | Get all client accounts | Implemented |
| `/self/savingsaccounts/{accountId}` | GET | Get savings account details | Implemented |
| `/self/savingsaccounts/{accountId}?associations=transactions` | GET | Get account with transactions | Implemented |
| `/self/savingsaccounts/{accountId}/transactions/{transactionId}` | GET | Get transaction details | Implemented |
| `/self/savingsaccounts/template?clientId={id}` | GET | Get savings product options | Implemented |
| `/self/savingsaccounts/template?clientId={id}&productId={id}` | GET | Get template by product | Implemented |
| `/self/savingsaccounts/{accountId}` | PUT | Update savings account | Implemented |
| `/self/savingsaccounts/{savingsId}?command=withdrawnByApplicant` | POST | Withdraw account application | Implemented |
| `/self/accounttransfers/template` | GET | Get transfer template | Implemented |
| `/self/accounttransfers` | POST | Make transfer | Implemented |

---

## 6. Edge Cases & Error Handling

| Scenario | Screen | Behavior | UI Feedback |
|----------|--------|----------|-------------|
| No internet | All | Show network state | "No internet connection" + retry |
| No accounts | List | Show empty state | "No savings accounts" message |
| Filter returns empty | List | Show filtered empty | "No accounts found" |
| API error | All | Show error state | Error message + retry button |
| Inactive account | Details | Limited actions | Only QR/Transfer available |
| Closed account | Details | Limited actions | Only QR/Transfer/Transactions |
| Pending account | Details | Enable update | Update button active |
| Empty product options | Update | Show empty state | "No products available" |
| Authentication failed | Update/Withdraw | Cancel operation | Return to form |
| Update failed | Update | Navigate to status | Failure screen with retry |
| Withdraw failed | Withdraw | Navigate to status | Failure screen with retry |
| Empty remarks | Withdraw | Disable submit | Button disabled |

---

## 7. Components

| Component | Props | Reusable? | Location |
|-----------|-------|-----------|----------|
| MifosDashboardCard | savingsAmount, isVisible, currency, onToggle | Yes | core/ui |
| MifosAccountCard | accountId, accountNo, accountType, status, statusColor, onClick | Yes | core/ui |
| MifosLabelValueCard | label, value, color | Yes | core/ui |
| MifosActionCard | title, subTitle, icon, onClick | Yes | core/ui |
| MifosDetailsCard | keyValuePairs | Yes | core/ui |
| MifosOutlineDropdown | selectedText, items, onItemSelected, label | Yes | core/ui |
| MifosProgressIndicator | - | Yes | core/designsystem |
| MifosProgressIndicatorOverlay | - | Yes | core/ui |
| MifosErrorComponent | message, isRetryEnabled, onRetry, isNetworkConnected | Yes | core/ui |
| EmptyDataView | icon, error | Yes | core/ui |
| SavingsActionItems | title, subTitle, icon, route | No | feature/savings-account |

---

## 8. Navigation

### 8.1 Navigation Graph

```
SavingsGraphRoute
    |
    +-- SavingsAccountRoute (List)
    |       |
    |       +-- SavingsAccountDetailsRoute (Details)
    |               |
    |               +-- SavingsAccountUpdateRoute (Update)
    |               +-- SavingsAccountWithdrawRoute (Withdraw)
    |               +-- [External] TransferScreen
    |               +-- [External] TransactionsScreen
    |               +-- [External] ChargesScreen
    |               +-- [External] QrCodeScreen
```

### 8.2 Route Definitions

```kotlin
@Serializable
data object SavingsAccountRoute

@Serializable
data class SavingsAccountDetailsRoute(val accountId: Long)

@Serializable
data class SavingsAccountUpdateRoute(
    val accountId: Long,
    val accountNumber: String?,
    val clientName: String?,
    val submissionData: String?,
    val product: String?,
)
```

---

## 9. Account Status Logic

| Status | Display Color | Allowed Actions | Update Enabled |
|--------|---------------|-----------------|----------------|
| Active | Green | Transfer, Transactions, Charges, QR Code | No |
| Submitted and pending approval | Yellow | QR Code | Yes |
| Inactive | Red | Transfer, QR Code | No |
| Closed | Default | Transfer, Transactions, QR Code | No |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification based on implementation |

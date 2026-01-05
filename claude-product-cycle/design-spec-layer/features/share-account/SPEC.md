# Share Account - Feature Specification

> **Purpose**: Share account management for cooperative members
> **User Value**: View, manage, and track share holdings and transactions
> **Last Updated**: 2025-12-29
> **Status**: Production Design

---

## 1. Overview

### 1.1 Feature Summary
The Share Account feature enables cooperative members to view and manage their share holdings within the MifosX platform. Users can browse their share accounts, view detailed account information including approved and pending shares, market prices, and access related actions like viewing charges, transactions, and generating QR codes for account sharing.

### 1.2 User Stories
- As a cooperative member, I want to see all my share accounts in one place
- As a user, I want to filter share accounts by status (Active, Pending, Approved, Rejected, Closed)
- As a user, I want to view detailed information about each share account
- As a user, I want to see my approved and pending shares count
- As a user, I want to view the current market price of my shares
- As a user, I want to access share account charges and transactions
- As a user, I want to generate a QR code for my share account details

### 1.3 Design Principles
- **Clarity**: Clear display of share holdings with status indicators
- **Discoverability**: Easy access to account details and related actions
- **Consistency**: Follows the same patterns as savings and loan account screens
- **Accessibility**: WCAG 2.1 AA compliant with proper contrast and touch targets

---

## 2. Screen Layout

### 2.1 Share Accounts List Screen

```
+-------------------------------------------------------------+
|  <- Share Accounts                                           |
+-------------------------------------------------------------+
|                                                              |
|  +-------------------------------------------------------+  |
|  |  SHARE DASHBOARD                               [eye]  |  |
|  |  +---------------------------------------------------+|  |
|  |  |                                                   ||  |
|  |  |            $ 0.00                                 ||  |
|  |  |            Total Value                            ||  |
|  |  |                                                   ||  |
|  |  +---------------------------------------------------+|  |
|  +-------------------------------------------------------+  |
|                                                              |
|  Share Account                               [search] [filter]|
|  3 items                                                     |
|  -----------------------------------------------------------  |
|                                                              |
|  +-------------------------------------------------------+  |
|  | [coin]  Equity Shares                                 |  |
|  |         SH-0001234567                                 |  |
|  |         [Active]                                      |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  | [coin]  Premium Shares                                |  |
|  |         SH-0001234568                                 |  |
|  |         [Pending Approval]                            |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  | [coin]  Growth Shares                                 |  |
|  |         SH-0001234569                                 |  |
|  |         [Approved]                                    |  |
|  +-------------------------------------------------------+  |
|                                                              |
+-------------------------------------------------------------+
```

### 2.2 Share Account Details Screen

```
+-------------------------------------------------------------+
|  <- Share Account Details                              [...]  |
+-------------------------------------------------------------+
|                                                              |
|  +----------------------+  +----------------------+          |
|  | Account Number       |  | Product Name         |          |
|  | SH-0001234567        |  | Equity Shares        |          |
|  +----------------------+  +----------------------+          |
|                                                              |
|  +----------------------+  +----------------------+          |
|  | Status               |  | Currency             |          |
|  | [Active]             |  | USD ($)              |          |
|  +----------------------+  +----------------------+          |
|                                                              |
|  +----------------------+  +----------------------+          |
|  | Approved Shares      |  | Pending Shares       |          |
|  | 100                  |  | 0                    |          |
|  +----------------------+  +----------------------+          |
|                                                              |
|  +----------------------+                                    |
|  | Market Price         |                                    |
|  | $ 25.00              |                                    |
|  +----------------------+                                    |
|                                                              |
|  +----------------------+  +----------------------+          |
|  | Application Date     |  | Activation Date      |          |
|  | Jan 15, 2024         |  | Jan 20, 2024         |          |
|  +----------------------+  +----------------------+          |
|                                                              |
|  ACTIONS                                                     |
|  +-------------------------------------------------------+  |
|  |                                                        |  |
|  |  +----------+  +-------------+  +---------+           |  |
|  |  | Charges  |  | Transactions|  | QR Code |           |  |
|  |  +----------+  +-------------+  +---------+           |  |
|  |                                                        |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.3 Empty State

```
+-------------------------------------------------------------+
|  <- Share Accounts                                           |
+-------------------------------------------------------------+
|                                                              |
|                                                              |
|                                                              |
|                         [info icon]                          |
|                                                              |
|                   No Share Accounts                          |
|                                                              |
|              You don't have any share accounts               |
|                                                              |
|                                                              |
|                                                              |
+-------------------------------------------------------------+
```

### 2.4 Error State

```
+-------------------------------------------------------------+
|  <- Share Accounts                                           |
+-------------------------------------------------------------+
|                                                              |
|                                                              |
|                                                              |
|                       [error icon]                           |
|                                                              |
|                    Unable to Load                            |
|                                                              |
|           There was an error loading your                    |
|                  share accounts                              |
|                                                              |
|                     [Retry]                                  |
|                                                              |
|                                                              |
+-------------------------------------------------------------+
```

---

## 3. Sections Table

| Section | Description | Data Source |
|---------|-------------|-------------|
| Dashboard Card | Total share value with visibility toggle | Calculated from accounts |
| Account Count | Number of filtered share accounts | State.items |
| Filter Controls | Search and filter icons | User action triggers |
| Account List | LazyColumn of share account cards | State.shareAccounts |
| Account Card | Individual account with number, name, status | ShareAccount entity |
| Actions Row | Quick actions (Charges, Transactions, QR) | ShareActionItems |

---

## 4. User Interactions Table

| Element | Action | Result |
|---------|--------|--------|
| Back Arrow | Tap | Navigate to previous screen |
| Eye Icon (Dashboard) | Tap | Toggle amount visibility |
| Search Icon | Tap | Open search (not yet implemented) |
| Filter Icon | Tap | Open filter bottom sheet |
| Account Card | Tap | Navigate to account details |
| Charges Action | Tap | Navigate to charges screen |
| Transactions Action | Tap | Navigate to transactions screen |
| QR Code Action | Tap | Navigate to QR code screen |
| Retry Button | Tap | Reload account data |

---

## 5. State Model

Based on actual ViewModel implementation:

```kotlin
/**
 * State holder for the Share Accounts screen.
 */
data class ShareAccountsState(
    val shareAccounts: List<ShareAccount>?,
    val originalAccounts: List<ShareAccount>? = null,
    val isFilteredEmpty: Boolean = false,
    val firstLaunch: Boolean = true,
    val items: Int? = 0,
    val totalLoanAmount: String? = "",
    val currency: String? = "",
    val decimals: Int? = 2,
    val networkConnection: Boolean? = true,
    val clientId: Long?,
    val dialogState: DialogState? = null,
    val selectedFilters: List<StringResource?> = emptyList(),
    val isAmountVisible: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
    val networkStatus: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

/**
 * Actions for the Share Accounts screen.
 */
sealed interface ShareAccountsAction {
    data object OnFirstLaunched : ShareAccountsAction
    data object OnDismissDialog : ShareAccountsAction
    data object OnNavigateBack : ShareAccountsAction
    data object ToggleAmountVisible : ShareAccountsAction
    data class LoadAccounts(val filters: List<StringResource?>) : ShareAccountsAction
    data object OnRetry : ShareAccountsAction
    data class OnAccountClicked(val accountId: Long, val accountType: String) : ShareAccountsAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : ShareAccountsAction

    sealed interface Internal : ShareAccountsAction {
        data class ReceiveShareAccounts(
            val filters: List<StringResource?>,
            val dataState: DataState<ClientAccounts>,
        ) : Internal
    }
}

/**
 * One-time UI events for the Share Accounts screen.
 */
sealed interface ShareAccountsEvent {
    data class AccountClicked(val accountId: Long, val accountType: String) : ShareAccountsEvent
    data object LoadingCompleted : ShareAccountsEvent
    data object NavigateBack : ShareAccountsEvent
}
```

### 5.1 Share Account Details State

```kotlin
@Immutable
data class ShareAccountDetailsState(
    val accountId: Long = -1L,
    val accountNumber: String? = "",
    val clientName: String? = "",
    val isActive: Boolean = false,
    val displayItems: List<LabelValueItem> = emptyList(),
    val items: ImmutableList<ShareActionItems> = shareAccountActions,
    val allowedActions: Set<ShareActionItems> = emptySet(),
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

sealed interface ShareAccountDetailsAction {
    data object OnNavigateBack : ShareAccountDetailsAction
    data object OnRetry : ShareAccountDetailsAction
    data class OnNavigateToAction(val route: String) : ShareAccountDetailsAction
    data object DismissDialog : ShareAccountDetailsAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : ShareAccountDetailsAction

    sealed interface Internal : ShareAccountDetailsAction {
        data class ShareResultReceived(val dataState: DataState<ShareAccountWithAssociations>) : Internal
    }
}

sealed interface ShareAccountDetailsEvent {
    data object NavigateBack : ShareAccountDetailsEvent
    data class NavigateToAction(val route: String) : ShareAccountDetailsEvent
}
```

---

## 6. API Requirements

| Endpoint | Method | Purpose | Priority |
|----------|--------|---------|----------|
| /self/clients/{id}/accounts | GET | Fetch share accounts list | P0 |
| /self/shareaccounts/{id} | GET | Fetch share account details | P0 |
| /self/products/share | GET | Fetch available share products | P1 |
| /self/products/share/{id} | GET | Fetch share product details | P1 |
| /self/shareaccounts | POST | Submit share application | P1 |

---

## 7. Filter Logic

The FilterUtil enum provides filtering capabilities:

```kotlin
enum class FilterUtil(
    val label: StringResource,
    val matchCondition: (ShareAccount) -> Boolean,
) {
    ACTIVE(
        Res.string.feature_share_account_active,
        { it.status?.active == true },
    ),
    APPROVED(
        Res.string.feature_share_account_approved,
        { it.status?.approved == true },
    ),
    APPROVAL_PENDING(
        Res.string.feature_share_account_approval_pending,
        { it.status?.submittedAndPendingApproval == true },
    ),
    REJECTED(
        Res.string.feature_share_account_rejected,
        { it.status?.rejected == true },
    ),
    CLOSED(
        Res.string.feature_share_account_closed,
        { it.status?.closed == true },
    );
}
```

---

## 8. Share Account Actions

Available actions on share account details:

| Action | Icon | Route | Description |
|--------|------|-------|-------------|
| Charges | MifosIcons.Charges | Constants.CHARGES | View account charges |
| Transactions | MifosIcons.TransactionHistory | Constants.TRANSACTIONS | View share transactions |
| QR Code | MifosIcons.QrCode | Constants.QR_CODE | Generate account QR |

---

## 9. Edge Cases & Error Handling

| Scenario | UI Behavior | Recovery |
|----------|-------------|----------|
| No internet | Show network error state | Auto-retry on reconnect |
| API error | Show error with message | Retry button |
| Empty accounts | Show empty state with info icon | N/A - informational |
| Filtered empty | Show "No accounts match filter" | Clear filters |
| Loading | Show MifosProgressIndicator | N/A |
| Auth expired | Navigate to login | Re-authenticate |

---

## 10. Status Color Mapping

| Status | Color | Constant |
|--------|-------|----------|
| Active | Green | AppColors.customEnable |
| Pending Approval | Yellow | AppColors.customYellow |
| Withdrawn / Matured | Red | KptTheme.colorScheme.error |
| Other | Default | KptTheme.colorScheme.onSurface |

---

## 11. Display Items for Details

The following fields are displayed on the details screen:

| Label | Field | Source |
|-------|-------|--------|
| Account Number | accountNo | ShareAccountWithAssociations |
| Product Name | productName | ShareAccountWithAssociations |
| Status | status.value | ShareAccountWithAssociations |
| Currency | currency.displayLabel | ShareAccountWithAssociations |
| Approved Shares | summary.totalApprovedShares | ShareAccountWithAssociations |
| Pending Shares | summary.totalPendingForApprovalShares | ShareAccountWithAssociations |
| Market Price | currentMarketPrice | ShareAccountWithAssociations |
| Application Date | timeline.submittedOnDate | ShareAccountWithAssociations |
| Activation Date | timeline.activatedDate | ShareAccountWithAssociations |

---

## 12. Navigation Routes

```kotlin
@Serializable
data object ShareAccountRoute

@Serializable
data class ShareAccountDetailsRoute(
    val accountId: Long,
)

@Serializable
data object ShareGraphRoute
```

Navigation graph structure:
- ShareGraphRoute (parent)
  - ShareAccountRoute (start destination)
  - ShareAccountDetailsRoute (detail screen)

---

## 13. Performance Requirements

| Metric | Target | Implementation |
|--------|--------|----------------|
| First paint | < 500ms | Skeleton loading |
| Data load | < 2s | Network caching |
| Scroll | 60fps | LazyColumn with stable keys |
| List render | < 100ms | Efficient composables |

---

## 14. Accessibility

| Feature | Implementation |
|---------|----------------|
| Screen Reader | contentDescription on all interactive elements |
| Font Scaling | Supports 200% scaling |
| Color Contrast | WCAG AA (4.5:1 minimum) |
| Touch Targets | 48dp minimum |
| Focus Navigation | Logical tab order |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification based on implementation |

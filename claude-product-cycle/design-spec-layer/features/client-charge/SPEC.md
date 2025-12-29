# Client Charges - Feature Specification

> **Purpose**: Display and manage charges for clients, loans, savings, and share accounts
> **User Value**: View all applicable fees and charges across different account types
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Client Charges feature provides a unified view of all charges associated with a user's accounts. It supports four charge types: CLIENT (general client charges), LOAN (loan-specific charges), SAVINGS (savings account charges), and SHARE (share account charges). Users can view charge lists and details including amounts, due dates, and payment status.

### 1.2 User Stories
- As a user, I want to view all my client charges so I can understand my fees
- As a user, I want to see loan charges so I can track loan-related fees
- As a user, I want to see savings account charges so I know applicable fees
- As a user, I want to see share account charges for my share holdings
- As a user, I want to view charge details including amount and due date

---

## 2. Screen Layout

### 2.1 Charge List Screen

```
┌─────────────────────────────────────────┐
│  ← Back         [Charge Type]           │
├─────────────────────────────────────────┤
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Processing Fee                  │   │
│  │  Amount: $50.00                  │   │
│  │  Due: Jan 15, 2025               │   │
│  │  Status: Pending                 │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Annual Maintenance Fee          │   │
│  │  Amount: $25.00                  │   │
│  │  Due: Dec 31, 2024               │   │
│  │  Status: Paid                    │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Late Payment Fee                │   │
│  │  Amount: $10.00                  │   │
│  │  Due: Dec 1, 2024                │   │
│  │  Status: Waived                  │   │
│  └─────────────────────────────────┘   │
│                                         │
└─────────────────────────────────────────┘
```

### 2.2 Charge Detail Screen

```
┌─────────────────────────────────────────┐
│  ← Back      Charge Details             │
├─────────────────────────────────────────┤
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Processing Fee                  │   │
│  │                                  │   │
│  │  Amount Due       $50.00         │   │
│  │  Amount Paid      $0.00          │   │
│  │  Amount Waived    $0.00          │   │
│  │  Amount Outstanding $50.00       │   │
│  │                                  │   │
│  │  Due Date         Jan 15, 2025   │   │
│  │  Charge Time      Account Open   │   │
│  │  Charge Applies   Monthly        │   │
│  │                                  │   │
│  │  Status           Pending        │   │
│  └─────────────────────────────────┘   │
│                                         │
└─────────────────────────────────────────┘
```

---

## 3. Sections Table

| # | Section | Description | Priority |
|---|---------|-------------|----------|
| 1 | ChargeListScreen | List of charges by type | P0 |
| 2 | ChargeDetailScreen | Full charge information | P0 |
| 3 | ClientChargeItem | Individual charge card | P0 |

---

## 4. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| View charges | Load screen | Fetch charge list | GET /clients/{id}/charges |
| View loan charges | Navigate | Fetch loan charges | GET /loans/{id}/charges |
| View savings charges | Navigate | Fetch savings charges | GET /savingsaccounts/{id}/charges |
| View share charges | Navigate | Fetch share charges | GET /shareaccounts/{id}/charges |
| Tap charge | Click card | Navigate to detail | - |
| Pull refresh | Swipe down | Reload charges | Various |
| Retry | Click button | Reload on error | Various |

---

## 5. State Model

```kotlin
data class ClientChargeState(
    val networkStatus: Boolean = false,
    val clientId: Long,
    val chargeType: ChargeType,
    val chargeTypeId: Long?,
    val isOnline: Boolean,
    val isEmpty: Boolean = false,
    val topBarTitleResId: StringResource = Res.string.charges,
    val charges: List<Charge> = emptyList(),
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

// Charge Types
enum class ChargeType {
    CLIENT,
    LOAN,
    SAVINGS,
    SHARE
}

// Events
sealed interface ClientChargeEvent {
    data class ShowToast(val message: String) : ClientChargeEvent
    data object Navigate : ClientChargeEvent
    data class OnChargeClick(val charge: Charge) : ClientChargeEvent
}

// Actions
sealed interface ClientChargeAction {
    data object RefreshCharges : ClientChargeAction
    data object OnNavigate : ClientChargeAction
    data object OnDismissDialog : ClientChargeAction
    data class OnChargeClick(val charge: Charge) : ClientChargeAction
    data class ReceiveNetworkResult(val isOnline: Boolean) : ClientChargeAction
    data object Retry : ClientChargeAction
}
```

---

## 6. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| /self/clients/{clientId}/charges | GET | Get client charges | Exists |
| /self/loans/{loanId}/charges | GET | Get loan charges | Exists |
| /self/savingsaccounts/{accountId}/charges | GET | Get savings charges | Exists |
| /self/shareaccounts/{accountId}/charges | GET | Get share charges | Exists |

---

## 7. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Show network state | Retry button |
| No charges | Show empty state | "No charges found" |
| API error | Show error state | Error message + retry |

---

## 8. Navigation

```kotlin
@Serializable
data class ClientChargesRoute(
    val chargeType: String,
    val chargeTypeId: Long? = null,
)

@Serializable
data class ChargeDetailsRoute(
    val charge: Charge,
)
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial spec from codebase analysis |

# Client Charges - Feature Specification

> **Purpose**: Display and manage charges for clients, loans, savings, and share accounts
> **User Value**: View all pending and paid charges, track due dates, and access payment details
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Client Charges feature allows users to view charges associated with their client account, loan accounts, savings accounts, and share accounts. It displays a list of charges with status indicators (paid/unpaid), amounts, due dates, and provides navigation to detailed charge information. Users can see charge breakdowns including due amount, paid amount, waived amount, and outstanding balance.

### 1.2 User Stories
- As a user, I want to view all charges on my client account so I can track fees and payments
- As a user, I want to view charges on my loan account so I can see loan-related fees
- As a user, I want to view charges on my savings account so I can track savings-related fees
- As a user, I want to view charges on my share account so I can see share-related fees
- As a user, I want to see which charges are paid vs outstanding so I can prioritize payments
- As a user, I want to view charge details so I can understand the breakdown of amounts
- As a user, I want to see due dates so I can plan my payments accordingly

---

## 2. Screen Layout

### 2.1 Charges List Screen - ASCII Mockup

```
+------------------------------------------+
|  [<]  Client Charges                     |  <- TopBar (title varies by type)
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  | [DB] Loan Processing Fee         > |  |
|  |      ChargeId: 123                 |  |
|  |      2025-01-15                    |  |
|  |                         Due        |  |
|  |                     $150.00        |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | [OK] Annual Service Fee          > |  |
|  |      ChargeId: 456                 |  |
|  |      2024-12-01                    |  |
|  |                        Paid        |  |
|  |                     $50.00         |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | [DB] Late Payment Penalty        > |  |
|  |      ChargeId: 789                 |  |
|  |      2025-02-01                    |  |
|  |                         Due        |  |
|  |                     $25.00         |  |
|  +------------------------------------+  |
|                                          |
+------------------------------------------+
|         Powered by Mifos                 |
+------------------------------------------+

Legend:
[<]  = Back navigation
[DB] = Database warning icon (unpaid)
[OK] = Database checkmark icon (paid)
>    = Chevron right (navigate to details)
```

### 2.2 Charge Details Screen - ASCII Mockup

```
+------------------------------------------+
|  [<]  Charge Details                     |  <- TopBar
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  | Fee Title    | Loan Processing Fee |  |
|  +------------------------------------+  |
|  | Date         | 2025-01-15          |  |
|  +------------------------------------+  |
|  | Due          | $150.00             |  |
|  +------------------------------------+  |
|  | Paid         | $75.00              |  |
|  +------------------------------------+  |
|  | Waived       | $0.00               |  |
|  +------------------------------------+  |
|  | Outstanding  | $75.00              |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |       [PAY OUTSTANDING]            |  |  <- For unpaid charges
|  +------------------------------------+  |
|                                          |
|  Partial amount paid on: 2025-01-10      |
|                                          |
+------------------------------------------+
|         Powered by Mifos                 |
+------------------------------------------+

--- OR (for fully paid charges) ---

+------------------------------------------+
|  [<]  Charge Details                     |
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  | Fee Title    | Annual Service Fee  |  |
|  +------------------------------------+  |
|  | Date         | 2024-12-01          |  |
|  +------------------------------------+  |
|  | Due          | $50.00              |  |
|  +------------------------------------+  |
|  | Paid         | $50.00              |  |
|  +------------------------------------+  |
|  | Waived       | $0.00               |  |
|  +------------------------------------+  |
|  | Outstanding  | $0.00               |  |
|  +------------------------------------+  |
|                                          |
|  This charge has been successfully paid  |
|                                          |
|              [SUCCESS ICON]              |
|                                          |
|          Ref No: CHG-456                 |
|          Paid on: 2024-12-01             |
|                                          |
+------------------------------------------+
|         Powered by Mifos                 |
+------------------------------------------+
```

### 2.3 Sections Table

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | TopBar | Title varies: Client/Loan/Savings/Share Charges | - | P0 |
| 2 | Charge List | Scrollable list of charge items | clients/{id}/charges, loans/{id}/charges, savingsaccounts/{id}/charges | P0 |
| 3 | Charge Item | Icon, name, charge ID, date, status, amount | - | P0 |
| 4 | Empty State | "No charges" message when list is empty | - | P0 |
| 5 | Error State | Error message with retry button | - | P0 |
| 6 | Network State | No connection message | - | P0 |
| 7 | Details Card | Key-value pairs for charge details | - | P0 |
| 8 | Payment Section | Pay button or success confirmation | - | P1 |
| 9 | Footer | Powered by Mifos | - | P2 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Navigate back | Tap back arrow | Return to previous screen | - |
| View charge details | Tap charge item | Navigate to ChargeDetailScreen | - |
| Retry loading | Tap retry button | Reload charges | clients/{id}/charges |
| Pay outstanding | Tap pay button | (Future) Payment flow | - |
| Pull to refresh | Swipe down | Reload charge list | clients/{id}/charges |

---

## 4. State Model

### 4.1 Charges List Screen

```kotlin
/**
 * Represents the UI state of the Client Charges screen.
 */
data class ClientChargeState(
    val networkStatus: Boolean = false,
    val clientId: Long,
    val chargeType: ChargeType,           // CLIENT, LOAN, SAVINGS, SHARE
    val chargeTypeId: Long?,              // Account ID for LOAN/SAVINGS/SHARE
    val isOnline: Boolean,
    val isEmpty: Boolean = false,
    val topBarTitleResId: StringResource, // Dynamic based on chargeType
    val charges: List<Charge> = emptyList(),
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

/**
 * UI events emitted from the ViewModel to be handled by the UI layer.
 */
sealed interface ClientChargeEvent {
    data class ShowToast(val message: String) : ClientChargeEvent
    data object Navigate : ClientChargeEvent
    data class OnChargeClick(val charge: Charge) : ClientChargeEvent
}

/**
 * Actions dispatched from the UI or internal processes.
 */
sealed interface ClientChargeAction {
    data object RefreshCharges : ClientChargeAction
    data object OnNavigate : ClientChargeAction
    data object OnDismissDialog : ClientChargeAction
    data class OnChargeClick(val charge: Charge) : ClientChargeAction
    data class ReceiveNetworkResult(val isOnline: Boolean) : ClientChargeAction
    data object Retry : ClientChargeAction

    sealed class Internal : ClientChargeAction {
        data class ReceiveLoanOrSavingsChargesResult(
            val result: DataState<List<Charge>>
        ) : Internal()
        data class ReceiveClientChargesResult(
            val result: DataState<Page<Charge>>
        ) : Internal()
        data class ReceiveShareChargesResult(
            val result: DataState<List<Charge>>
        ) : Internal()
    }
}

/**
 * ChargeType determines which endpoint to call and what title to show.
 */
enum class ChargeType(val type: String) {
    CLIENT("clients"),
    SAVINGS("savingsaccounts"),
    LOAN("loans"),
    SHARE("shareaccounts"),
}
```

### 4.2 Charge Details Screen

```kotlin
/**
 * Represents the state of the Charge Details Screen.
 */
data class ChargeDetailsState(
    val details: Map<StringResource, String> = emptyMap(), // fee_title, date, due, paid, waived, outstanding
    val isPaid: Boolean = false,
    val refNo: String = "",
    val paidOn: String = "",
)

/**
 * Events for the Charge Details Screen.
 */
sealed interface ChargeDetailsEvent {
    data object NavigateBack : ChargeDetailsEvent
}

/**
 * Actions for the Charge Details Screen.
 */
sealed interface ChargeDetailsAction {
    data object NavigateBack : ChargeDetailsAction
    data object PayOutStanding : ChargeDetailsAction
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/clients/{clientId}/charges` | GET | Get client-level charges (paginated) | Implemented |
| `/loans/{loanId}/charges` | GET | Get loan account charges | Implemented |
| `/savingsaccounts/{savingsId}/charges` | GET | Get savings account charges | Implemented |
| `/shareaccounts/{shareId}` | GET | Get share account details (includes charges) | Implemented |

---

## 6. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Set uiState to Network | "No internet connection" with retry |
| Empty charge list | Set uiState to Empty | "No charges found" with warning icon |
| API error | Set uiState to Error | Server error message with retry |
| Invalid charge type | Default to CLIENT | - |
| Missing due date | Show "-" for date | Safe date string handling |
| Zero amounts | Display as "0.00" | Formatted currency display |
| Network restored | Automatically reload | Triggered by network monitor |
| IOException | Show network state | "No internet connection" |
| Other exceptions | Show error state | "Server error" message |

---

## 7. Components

| Component | Props | Reusable? |
|-----------|-------|-----------|
| ClientChargeItem | charge: Charge, onChargeClick: () -> Unit | Yes |
| MifosDetailsCard | keyValuePairs: Map<StringResource, String> | Yes (core:ui) |
| ChargeDetailsPaidComponent | refNo: String, paidOn: String | No |
| ChargeDetailsUnPaidComponent | amountPaidOn: String, onPayOutStanding: () -> Unit | No |
| MifosElevatedScaffold | topBarTitle, onNavigateBack, bottomBar, content | Yes (core:designsystem) |
| MifosErrorComponent | isNetworkConnected, isRetryEnabled, message, onRetry | Yes (core:ui) |
| EmptyDataView | image, error | Yes (core:ui) |
| MifosProgressIndicator | - | Yes (core:designsystem) |

---

## 8. Navigation

### 8.1 Routes

```kotlin
// Charges List Route
@Serializable
data class ClientChargesRoute(
    val chargeType: String,       // "CLIENT", "LOAN", "SAVINGS", "SHARE"
    val chargeTypeId: Long? = null, // Required for LOAN/SAVINGS/SHARE
)

// Charge Details Route
@Serializable
data class ChargesDetailsRoute(
    val title: String = "",
    val date: String = "",
    val due: String = "",
    val paid: String = "",
    val waived: String = "",
    val outstanding: String = "",
    val refNo: String = "",
    val paidOn: String = "",
    val isPaid: Boolean = false,
)
```

### 8.2 Navigation Flow

```
Home Dashboard
    |
    v
[Services Grid] -> "Charges" card
    |
    v
Client Charges Screen (chargeType = CLIENT)
    |
    v (tap charge item)
Charge Details Screen

--- OR ---

Loan Account Details
    |
    v
[View Charges] action
    |
    v
Loan Charges Screen (chargeType = LOAN, chargeTypeId = loanId)
    |
    v (tap charge item)
Charge Details Screen
```

---

## 9. Dependencies

### 9.1 Module Dependencies

```kotlin
// feature/client-charge/build.gradle.kts
plugins {
    alias(libs.plugins.convention.cmp.feature)
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
}
```

### 9.2 Repository Dependency

```kotlin
// Koin Module - ChargeModule.kt
val chargeModule = module {
    viewModelOf(::ClientChargeViewModel)
    viewModelOf(::ChargeDetailsViewModel)
}
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Updated specification with detailed ASCII mockups, complete state models from implementation, navigation routes, and component details |
| 2025-12-29 | Initial spec from codebase analysis |

# Home Dashboard - Feature Specification

> **Purpose**: Display client overview, account balances, and quick access to services
> **User Value**: Central hub for viewing financial status at a glance
> **Last Updated**: 2025-12-26

---

## 1. Overview

### 1.1 Feature Summary
The Home Dashboard is the main screen after login, displaying the client's financial overview including total savings and loan balances, quick access to services, and notification count. It serves as the navigation hub for all app features.

### 1.2 User Stories
- As a user, I want to see my total savings balance so I can track my savings
- As a user, I want to see my total loan balance so I can track my debt
- As a user, I want quick access to common services so I can navigate efficiently
- As a user, I want to see notification count so I know if there are updates

---

## 2. Screen Layout

### 2.1 ASCII Mockup

```
┌─────────────────────────────────────────┐
│  Hello, [FirstName]              🔔[n]  │  ← TopBar with notification
├─────────────────────────────────────────┤
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Total Savings        👁        │   │  ← Amount visibility toggle
│  │  [$X,XXX.XX] or [****]          │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Total Loans          👁        │   │
│  │  [$X,XXX.XX] or [****]          │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Services                        │   │
│  │  ┌─────┐ ┌─────┐ ┌─────┐        │   │
│  │  │Accts│ │Trans│ │Benef│        │   │
│  │  └─────┘ └─────┘ └─────┘        │   │
│  │  ┌─────┐ ┌─────┐ ┌─────┐        │   │
│  │  │Chrgs│ │Apply│ │More │        │   │
│  │  └─────┘ └─────┘ └─────┘        │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  [+] Apply for New Account       │   │  ← Bottom sheet trigger
│  └─────────────────────────────────┘   │
│                                         │
└─────────────────────────────────────────┘
```

### 2.2 Sections Table

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Header | Greeting + notification | clients/{id}, notifications | P0 |
| 2 | Savings Balance | Total savings amount | clients/{id}/accounts | P0 |
| 3 | Loans Balance | Total loans amount | clients/{id}/accounts | P0 |
| 4 | Services Grid | Quick access cards | - | P0 |
| 5 | Apply Button | New account application | - | P1 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| View notification | Tap bell icon | Navigate to notifications | - |
| Toggle amount | Tap eye icon | Show/hide amounts | - |
| Tap service card | Click card | Navigate to service | - |
| Pull refresh | Swipe down | Reload all data | clients/{id}/accounts |
| Apply account | Tap + button | Show bottom sheet | - |

---

## 4. State Model

```kotlin
@Immutable
data class HomeState(
    val clientId: Long? = null,
    val firstName: String? = "",
    val currency: String? = "",
    val isAccountsPresent: Boolean = true,
    val username: String = "",
    val clientAccounts: ClientAccounts? = null,
    val notificationCount: Int = 0,
    val loanAmount: String = "",
    val savingsAmount: String = "",
    val isAmountVisible: Boolean = false,
    val dialogState: DialogState? = null,
    val items: ImmutableList<ServiceItem>,
    val networkStatus: Boolean = true,
    val uiState: HomeScreenState?,
)

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data object Success : HomeScreenState
    data class Error(val message: StringResource) : HomeScreenState
    data object Network : HomeScreenState
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/clients/{clientId}` | GET | Get client details | ✅ Exists |
| `/clients/{clientId}/accounts` | GET | Get all accounts | ✅ Exists |
| `/clients/{clientId}/images` | GET | Get profile image | ✅ Exists |
| `/notifications` | GET | Get notifications | ✅ Exists |

---

## 6. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Show network state | "No internet connection" |
| No accounts | Hide balance cards | "No accounts found" |
| API error | Show error state | Error message + retry |
| Image not found | Show placeholder | Default avatar |

---

## 7. Components

| Component | Props | Reusable? |
|-----------|-------|-----------|
| BalanceCard | title, amount, isVisible, onToggle | Yes |
| ServiceCard | icon, title, onClick | Yes |
| NotificationBadge | count | Yes |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-26 | Initial spec |

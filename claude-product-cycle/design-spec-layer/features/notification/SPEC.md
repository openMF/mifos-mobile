# Notification - Feature Specification

> **Purpose**: Display and manage user notifications with read/unread status
> **User Value**: Stay informed about account activities and system updates
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Notification screen displays a chronological list of notifications for the user, sorted with unread notifications first and then by timestamp. Users can view notification messages, see when they were received, and mark notifications as read. The feature supports pull-to-refresh functionality and automatically deletes notifications older than 30 days.

### 1.2 User Stories
- As a user, I want to see all my notifications so I can stay updated on account activities
- As a user, I want to see unread notifications first so I can prioritize important updates
- As a user, I want to mark notifications as read so I can track what I have reviewed
- As a user, I want to refresh my notifications so I can see the latest updates
- As a user, I want notifications older than 30 days automatically removed so the list stays relevant

---

## 2. Screen Layout

### 2.1 ASCII Mockup

```
+------------------------------------------+
|  [<]      Notification                    |  <- TopBar with back nav
+------------------------------------------+
|                                           |
|  +--------------------------------------+ |
|  | [Bell Icon]  Notification message    | |  <- Unread (primary color)
|  |              text content here...    | |
|  |                        Dec 29, 2025  | |  <- Timestamp
|  |                             [OK]     | |  <- Dismiss button
|  +--------------------------------------+ |
|  ---------------------------------------- |  <- Divider
|  +--------------------------------------+ |
|  | [Bell Icon]  Previous notification   | |  <- Read (muted color)
|  |              message content...      | |
|  |                        Dec 28, 2025  | |
|  +--------------------------------------+ |  <- No OK button for read
|  ---------------------------------------- |
|  +--------------------------------------+ |
|  | [Bell Icon]  Another notification    | |
|  |              message here...         | |
|  |                        Dec 27, 2025  | |
|  +--------------------------------------+ |
|                                           |
|          [Pull down to refresh]           |
|                                           |
+------------------------------------------+

Empty State:
+------------------------------------------+
|  [<]      Notification                    |
+------------------------------------------+
|                                           |
|                                           |
|             [Notification Icon]           |
|                                           |
|           No notifications                |
|                                           |
|                                           |
+------------------------------------------+

Loading State:
+------------------------------------------+
|  [<]      Notification                    |
+------------------------------------------+
|                                           |
|                                           |
|            [Progress Indicator]           |
|                                           |
|                                           |
+------------------------------------------+

Error State:
+------------------------------------------+
|  [<]      Notification                    |
+------------------------------------------+
|                                           |
|             [Error Icon]                  |
|                                           |
|         Something went wrong              |
|                                           |
|             [Retry Button]                |
|                                           |
+------------------------------------------+
```

### 2.2 Sections Table

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Top Bar | Title with back navigation | - | P0 |
| 2 | Notification List | Scrollable list with pull-to-refresh | Local DB | P0 |
| 3 | Notification Item | Message, timestamp, read status | Local DB | P0 |
| 4 | Empty State | Shown when no notifications exist | - | P0 |
| 5 | Error State | Error message with retry option | - | P0 |
| 6 | Loading State | Progress indicator during load | - | P0 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Navigate back | Tap back button | Return to previous screen | - |
| Dismiss notification | Tap OK button | Mark notification as read | Local DB update |
| Pull refresh | Swipe down gesture | Reload notifications | Local DB query |
| Retry load | Tap retry button | Attempt to reload | Local DB query |
| View notification | Scroll list | Display notification content | - |

---

## 4. State Model

```kotlin
/**
 * UI State for the Notification screen
 */
internal sealed interface NotificationUiState {
    /**
     * Loading state - shown while fetching notifications
     */
    data object Loading : NotificationUiState

    /**
     * Success state - notifications loaded successfully
     * @param notifications Sorted list of notifications (unread first, then by timestamp)
     */
    data class Success(
        val notifications: List<MifosNotification>
    ) : NotificationUiState

    /**
     * Error state - failed to load notifications
     * @param errorMessage Descriptive error message for the user
     */
    data class Error(
        val errorMessage: String?
    ) : NotificationUiState

    /**
     * Empty state - no notifications available
     */
    data object Empty : NotificationUiState
}

/**
 * Domain model for a notification
 */
@Parcelize
@Serializable
data class MifosNotification(
    val timeStamp: Long,           // Unix timestamp for sorting
    val msg: String? = null,       // Notification message content
    val read: Boolean? = null,     // Read status
) : Parcelable {
    fun isRead(): Boolean = read == true
}
```

### 4.1 ViewModel State

```kotlin
class NotificationViewModel(
    private val notificationRepositoryImp: NotificationRepository,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    // UI state flow
    val notificationUiState: StateFlow<NotificationUiState>

    // Network availability state
    val isNetworkAvailable: StateFlow<Boolean>

    // Pull-to-refresh state
    val isRefreshing: StateFlow<Boolean>
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/device/registration/client/{clientId}` | GET | Get notification registration ID | Implemented |
| `/device/registration` | POST | Register device for notifications | Implemented |
| `/device/registration/{id}` | PUT | Update notification registration | Implemented |
| Local Database | - | Store and retrieve notifications | Partial* |

> *Note: The notification repository currently returns empty data as the Room DAO integration is commented out. Notifications are stored locally and managed through Firebase Cloud Messaging (FCM) on Android.

---

## 6. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Show network error state | "No internet connection" with retry |
| No notifications | Show empty state | "No notifications" with icon |
| API/DB error | Show error state | Error message with retry button |
| Old notifications | Auto-delete after 30 days | Removed from list silently |
| Load failure | Catch exception | Display error message |
| Refresh failure | Reset refresh state | Show error, keep current list |

---

## 7. Components

| Component | Props | Reusable? |
|-----------|-------|-----------|
| MifosElevatedScaffold | topBarTitle, onNavigateBack, content | Yes (core/designsystem) |
| NotificationItem | notification, dismissNotification | No (feature-specific) |
| NotificationContent | isRefreshing, notifications, dismissNotification, onRefresh | No (feature-specific) |
| MifosProgressIndicatorOverlay | - | Yes (core/ui) |
| MifosErrorComponent | message, isNetworkConnected, isRetryEnabled, onRetry | Yes (core/ui) |
| EmptyDataView | icon, error | Yes (core/ui) |
| MifosTextButton | content, onClick | Yes (core/designsystem) |
| PullToRefreshBox | state, onRefresh, isRefreshing | Yes (Material3) |

---

## 8. Navigation

```kotlin
// Route definition
@Serializable
data object NotificationRoute

// Navigation extension
fun NavController.navigateToNotificationScreen(navOptions: NavOptions? = null) {
    navigate(NotificationRoute, navOptions)
}

// NavGraph destination
fun NavGraphBuilder.notificationDestination(
    navigateBack: () -> Unit,
) {
    composableWithPushTransitions<NotificationRoute> {
        NotificationScreen(navigateBack = navigateBack)
    }
}
```

---

## 9. Sorting Logic

Notifications are sorted using the following criteria:
1. **Unread first**: Unread notifications appear before read ones
2. **By timestamp**: Within each group, newer notifications appear first

```kotlin
private fun sortNotifications(
    notifications: List<MifosNotification>
): List<MifosNotification> {
    return notifications.sortedWith(
        compareByDescending<MifosNotification> { !it.isRead() }
            .thenByDescending { it.timeStamp }
    )
}
```

---

## 10. Auto-Cleanup

Notifications older than 30 days are automatically deleted when the screen loads:

```kotlin
// 30 days in milliseconds
val thirtyDaysInMillis = 2592000000L
val cutoffTime = System.currentTimeMillis() - thirtyDaysInMillis
// Delete notifications with timeStamp < cutoffTime
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial spec created from implementation |

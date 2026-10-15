# :core:analytics module

## Overview

This module provides Project-specific analytics tracking functionality built on top of the base
analytics library (`core-base:analytics`). It offers domain-specific tracking methods, extension
functions, and Compose utilities tailored for microfinance applications.

## Enhanced Features

### 🚀 Base Analytics Enhancements (`core-base:analytics`)

#### Enhanced AnalyticsEvent

- **Builder Pattern Support**: Added `withParam()` and `withParams()` methods for fluent event
  creation
- **Parameter Validation**: Automatic validation of parameter keys (≤40 chars) and values (≤100
  chars)
- **Comprehensive Event Types**: Extended from 1 to 25+ predefined event types including:
    - Navigation events (screen_view, screen_transition)
    - User interactions (button_click, search_performed, filter_applied)
    - Form events (form_started, form_completed, form_abandoned)
    - Error tracking (error_occurred, api_error, network_error)
    - Performance metrics (app_launch, loading_time)
    - Authentication events (login_attempt, login_success, logout)

#### Enhanced AnalyticsHelper Interface

- **Convenience Methods**: Added overloaded `logEvent()` methods for simplified usage
- **Built-in Helpers**: Pre-defined methods for common events:
    - `logScreenView()`
    - `logButtonClick()`
    - `logError()`
    - `logFeatureUsed()`
- **User Management**: Support for `setUserProperty()` and `setUserId()`

#### New Extension Functions (`AnalyticsExtensions.kt`)

- **Event Builders**: Factory methods for creating common events
- **Timing Utilities**:
    - `startTiming()` and `timeExecution()` for performance tracking
    - `TimedEvent` class for manual timing control
- **Batch Processing**: `AnalyticsBatch` for efficient multiple event logging
- **Safe Parameter Creation**: Validation helpers for robust parameter handling

#### Enhanced UI Integration (`UiHelpers.kt`)

- **Compose Integration**:
    - `TrackScreenView()` composable for automatic screen tracking
    - `Modifier.trackClick()` for button click tracking
    - `TrackComposableLifecycle()` for component lifecycle tracking
    - `rememberAnalytics()` for easy analytics access

### 🎯 Mifos-Specific Features (`core:analytics`)

#### MifosAnalyticsTracker

A specialized tracker providing domain-specific methods for microfinance operations:

```kotlin
val tracker = analyticsHelper.mifosTracker()

// Track client operations
tracker.trackClientOperation("create", clientId = "12345", success = true)

// Track loan operations  
tracker.trackLoanOperation("apply", loanType = "personal", amount = "5000")

// Track savings operations
tracker.trackSavingsOperation("deposit", accountId = "67890", amount = "1000")

// Track performance metrics
tracker.trackPerformance(
    "api_call",
    duration = 250,
    additionalMetrics = mapOf("endpoint" to "/clients")
)
```

#### Mifos Extension Functions (`MifosAnalyticsExtensions.kt`)

Convenient extension methods for common Mifos workflows:

```kotlin
// Track client creation flow
analyticsHelper.trackClientCreationFlow("personal_info", success = true)

// Track API performance
analyticsHelper.trackApiCall("/api/clients", "GET", responseTime = 150, statusCode = 200)

// Track navigation patterns
analyticsHelper.trackNavigation("ClientList", "ClientDetails", "item_click")

// Track form validation
analyticsHelper.trackValidationError("ClientForm", "phoneNumber", "invalid_format")
```

#### Compose Analytics Utilities (`MifosComposeAnalytics.kt`)

Seamless integration with Jetpack Compose:

```kotlin
@Composable
fun ClientDetailsScreen(clientId: String) {
    // Automatic screen tracking with business context
    TrackMifosScreen("ClientDetails", clientId = clientId)

    // Track business flow progress
    TrackMifosFlowCompletion("client_onboarding", "step_2", totalSteps = 5)

    // Document operations tracking
    val documentTracker = rememberMifosDocumentTracker()

    Button(
        modifier = Modifier.trackClientAction("view_documents", clientId),
        onClick = { documentTracker.trackView("identity_proof") }
    ) {
        Text("View Documents")
    }
}
```

#### Comprehensive Event Definitions (`MifosAnalyticsEvents.kt`)

Over 50 predefined event types and parameter keys specific to microfinance:

- **Client Management**: `CLIENT_CREATED`, `CLIENT_PROFILE_VIEWED`, etc.
- **Loan Operations**: `LOAN_APPLICATION_STARTED`, `LOAN_DISBURSED`, `LOAN_REPAYMENT_MADE`
- **Savings Management**: `SAVINGS_DEPOSIT_MADE`, `SAVINGS_WITHDRAWAL_MADE`
- **Group Operations**: `GROUP_MEETING_CONDUCTED`, `GROUP_COLLECTION_MADE`
- **Reporting**: `REPORT_GENERATED`, `DASHBOARD_VIEWED`
- **Sync Operations**: `DATA_SYNC_COMPLETED`, `OFFLINE_TRANSACTION_SYNCED`

## Usage Examples

### Basic Event Logging

```kotlin
// Simple event
analyticsHelper.logEvent("button_clicked", "button_name" to "save_client")

// Event with builder pattern
val event = AnalyticsEvent.buttonClick("save_client", "ClientForm")
    .withParam("client_type", "individual")
    .withParam("form_step", "3")
analyticsHelper.logEvent(event)
```

### Performance Tracking

```kotlin
// Time a code block
val result = analyticsHelper.timeExecution("database_query", "table" to "clients") {
    clientRepository.getAllClients()
}

// Manual timing
val timer = analyticsHelper.startTiming("file_upload", "file_type" to "pdf")
uploadFile(file)
timer.complete(mapOf("file_size" to file.size.toString()))
```

### Batch Processing

```kotlin
analyticsHelper.batch()
    .add("client_created", "client_id" to "123")
    .add("document_uploaded", "doc_type" to "photo")
    .add("form_completed", "form_name" to "client_registration")
    .flush()
```

### Error Tracking

```kotlin
// Automatic error tracking with context
try {
    loanService.approveLoan(loanId)
} catch (e: ApiException) {
    analyticsHelper.logError(e.message, e.code, "LoanApproval")
}

// Custom error tracking
analyticsHelper.trackApiCall(
    endpoint = "/loans/approve",
    method = "POST",
    responseTime = 500,
    statusCode = 400,
    success = false
)
```

## Integration

### Dependencies

The core analytics module automatically includes the base analytics library:

```kotlin
// In your module's build.gradle.kts
dependencies {
    implementation(projects.core.analytics) // Includes base analytics
}
```

### DI Setup

```kotlin
// In your DI module
single<AnalyticsHelper> { FirebaseAnalyticsHelper(get()) }
single<MifosAnalyticsTracker> { MifosAnalyticsTracker(get()) }
```

### Compose Setup

```kotlin
@Composable
fun App() {
    CompositionLocalProvider(
        LocalAnalyticsHelper provides analyticsHelper
    ) {
        // Your app content
    }
}
```

## Best Practices

1. **Use Appropriate Granularity**: Track meaningful user actions, not every UI interaction
2. **Include Business Context**: Add relevant IDs (client_id, loan_id) to events
3. **Handle Errors Gracefully**: Use safe parameter creation for dynamic values
4. **Batch Related Events**: Use batch processing for multiple related events
5. **Respect Privacy**: Avoid logging sensitive personal or financial data
6. **Performance Conscious**: Use timing utilities to track performance bottlenecks

## Migration from Basic Analytics

If you're migrating from basic analytics usage:

```kotlin
// Before
analyticsHelper.logEvent(AnalyticsEvent("client_created", listOf(Param("client_id", "123"))))

// After - multiple options
analyticsHelper.logEvent("client_created", "client_id" to "123")
analyticsHelper.trackClientOperation("create", clientId = "123")
tracker.trackClientOperation("create", clientId = "123")
```

## Platform Support

- ✅ **Android**: Full Firebase Analytics support
- ✅ **Desktop**: Stub implementation for development
- ✅ **iOS**: Firebase Analytics support (via nonJsCommonMain)
- ✅ **Web**: Stub implementation
- ✅ **Native**: Firebase Analytics support

## Contributing

When adding new analytics events:

1. Add event type constants to `MifosEventTypes`
2. Add parameter keys to `MifosParamKeys`
3. Consider adding convenience methods to `MifosAnalyticsTracker`
4. Add Compose utilities if UI-related
5. Update this documentation
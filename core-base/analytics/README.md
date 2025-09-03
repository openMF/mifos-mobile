# :core-base:analytics module

## Overview

The base analytics library provides a comprehensive foundation for tracking user interactions,
performance metrics, and business events across all platforms in a Kotlin Multiplatform project.
This module offers type-safe analytics with extensive validation, testing utilities, and performance
tracking capabilities.

### Enhanced Analytics Events

- **Type-Safe Parameters**: Automatic validation of parameter keys (≤40 chars) and values (≤100
  chars)
- **Builder Pattern**: Fluent API for event creation with `withParam()` and `withParams()`
- **25+ Predefined Event Types**: From navigation to authentication, forms to performance tracking
- **Comprehensive Parameter Keys**: 40+ standardized parameter keys for consistent tracking

### Powerful Analytics Interface

- **Multiple Convenience Methods**: Simplified logging with `logEvent()` overloads
- **Built-in Common Events**: `logScreenView()`, `logButtonClick()`, `logError()`,`logFeatureUsed()`
- **User Management**: Support for `setUserProperty()` and `setUserId()`
- **Platform Abstraction**: Works seamlessly across Android, iOS, Desktop, and Web

### Advanced Extension Functions

- **Event Builders**: Factory methods for creating common events with validation
- **Performance Timing**:
    - `startTiming()` and `timeExecution()` for measuring operation durations
    - `TimedEvent` class for manual timing control
- **Batch Processing**: `AnalyticsBatch` for efficient multiple event logging
- **Safe Parameter Creation**: Robust validation helpers for dynamic data

### Jetpack Compose Integration

- **Declarative Tracking**: `TrackScreenView()` composable for automatic screen analytics
- **Modifier Extensions**: `Modifier.trackClick()` for effortless interaction tracking
- **Lifecycle Tracking**: `TrackComposableLifecycle()` for component enter/exit analytics
- **Helper Functions**: `rememberAnalytics()` for easy composition local access

### Performance Monitoring

- **Operation Timing**: Comprehensive timing utilities with automatic slow operation detection
- **Memory Tracking**: Real-time memory usage monitoring with automatic warnings
- **App Lifecycle**: Track app launch times, background/foreground transitions
- **Performance Statistics**: Percentile-based performance analysis (P95, P99)

### Testing & Validation

- **Test Analytics Helper**: Complete event capture and verification for unit tests
- **Mock Analytics**: Network delay and failure simulation for robust testing
- **Data Validation**: Comprehensive validation against analytics platform constraints
- **Sanitization**: Automatic data cleaning for invalid parameters

### Platform Support

- ✅ **Android**: Full Firebase Analytics integration
- ✅ **iOS**: Firebase Analytics via `nonJsCommonMain`
- ✅ **Desktop**: Development-friendly stub implementation
- ✅ **Web (JS)**: Configurable Firebase/stub implementation
- ✅ **Native**: Firebase Analytics support

## 📖 Usage Examples

### Basic Event Logging

```kotlin
// Simple event
analyticsHelper.logEvent("button_clicked", "button_name" to "save")

// Using convenience methods
analyticsHelper.logScreenView("UserProfile")
analyticsHelper.logButtonClick("edit_profile", "UserProfile")
analyticsHelper.logError("Network error", "NET_001", "UserProfile")

// Builder pattern
val event = AnalyticsEvent("form_submitted")
    .withParam("form_name", "user_registration")
    .withParam("field_count", "8")
    .withParam("completion_time", "120s")
analyticsHelper.logEvent(event)
```

### Performance Tracking

```kotlin
// Time a suspend function
val data = analyticsHelper.timePerformance("api_call") {
    apiService.fetchUserData()
}

// Manual timing
val timer = analyticsHelper.startTiming("data_processing")
processData()
timer.complete()

// Memory monitoring
val memoryTracker = analyticsHelper.memoryTracker()
memoryTracker.logMemoryUsage("after_data_load")
```

### Compose Integration

```kotlin
@Composable
fun UserProfileScreen() {
    TrackScreenView("UserProfile")

    val analytics = rememberAnalytics()

    Button(
        modifier = Modifier.trackClick("edit_profile", analytics, "UserProfile"),
        onClick = { /* edit profile */ }
    ) {
        Text("Edit Profile")
    }
}
```

### Batch Processing

```kotlin
analyticsHelper.batch()
    .add("user_registered", "user_id" to "12345")
    .add("email_verified", "verification_method" to "link")
    .add("profile_completed", "completion_percentage" to "100")
    .flush()
```

### Testing

```kotlin
@Test
fun testAnalyticsTracking() {
    val testAnalytics = createTestAnalyticsHelper()

    // Use your component with test analytics
    userService.registerUser("john@example.com", testAnalytics)

    // Verify analytics were logged
    testAnalytics.assertEventLogged(
        "user_registered",
        mapOf("email_domain" to "example.com")
    )
    testAnalytics.assertEventCount("user_registered", 1)

    // Check specific events
    assert(testAnalytics.hasEvent("email_verification_sent"))
}
```

### Data Validation

```kotlin
// Automatic validation and sanitization
val validatingAnalytics = analyticsHelper.withValidation(
    strictMode = false, // Sanitize invalid data instead of throwing
    logValidationErrors = true
)

// This will be automatically sanitized if invalid
validatingAnalytics.logEvent("user-action-with-invalid-chars", "param" to "value")

// Manual validation
val event = AnalyticsEvent("my_event", listOf(Param("key", "value")))
val result = event.validate()
if (!result.isValid) {
    println("Validation errors: ${result.errors}")
}
```

## 🏗️ Architecture

### Core Components

1. **AnalyticsEvent**: Type-safe event representation with builder pattern
2. **AnalyticsHelper**: Platform-agnostic analytics interface
3. **Platform Implementations**:
    - `FirebaseAnalyticsHelper` for production
    - `StubAnalyticsHelper` for development
    - `NoOpAnalyticsHelper` for testing
4. **Extension Functions**: Utility methods for common operations
5. **Validation Layer**: Data quality assurance
6. **Testing Utilities**: Comprehensive test support

### Design Principles

- **Type Safety**: Compile-time safety for analytics parameters
- **Platform Agnostic**: Write once, track everywhere
- **Performance Conscious**: Minimal overhead with batch processing
- **Developer Friendly**: Rich testing and debugging tools
- **Extensible**: Easy to add custom tracking methods

## 🔧 Integration

### Dependencies

```kotlin
// In your module's build.gradle.kts
dependencies {
    implementation(projects.coreBase.analytics)

    // Platform-specific dependencies are handled automatically
}
```

### Dependency Injection (Koin)

```kotlin
val analyticsModule = module {
    // The actual implementation is provided by platform-specific modules
    // Android: FirebaseAnalyticsHelper
    // Desktop: StubAnalyticsHelper
    // etc.
}
```

### Compose Setup

```kotlin
@Composable
fun App() {
    val analytics: AnalyticsHelper = koinInject()

    CompositionLocalProvider(
        LocalAnalyticsHelper provides analytics
    ) {
        // Your app content
    }
}
```

## 📋 Event Types Reference

### Navigation Events

- `SCREEN_VIEW`, `SCREEN_TRANSITION`

### User Interactions

- `BUTTON_CLICK`, `MENU_ITEM_SELECTED`, `SEARCH_PERFORMED`, `FILTER_APPLIED`

### Form Events

- `FORM_STARTED`, `FORM_COMPLETED`, `FORM_ABANDONED`, `FIELD_VALIDATION_ERROR`

### Content Events

- `CONTENT_VIEW`, `CONTENT_SHARED`, `CONTENT_LIKED`

### Error Events

- `ERROR_OCCURRED`, `API_ERROR`, `NETWORK_ERROR`

### Performance Events

- `APP_LAUNCH`, `APP_BACKGROUND`, `APP_FOREGROUND`, `LOADING_TIME`

### Authentication Events

- `LOGIN_ATTEMPT`, `LOGIN_SUCCESS`, `LOGIN_FAILURE`, `LOGOUT`, `SIGNUP_ATTEMPT`, `SIGNUP_SUCCESS`

### Feature Usage

- `FEATURE_USED`, `TUTORIAL_STARTED`, `TUTORIAL_COMPLETED`, `TUTORIAL_SKIPPED`

## 🔒 Privacy & Compliance

- **No PII Logging**: Framework prevents logging of personally identifiable information
- **Data Validation**: Automatic parameter validation prevents sensitive data leakage
- **Configurable**: Easy to disable or mock for privacy-compliant testing
- **Transparent**: All logged data is visible and controllable

## 🚀 Performance Characteristics

- **Minimal Overhead**: Event creation is lightweight with lazy validation
- **Batch Processing**: Efficient bulk event logging
- **Memory Conscious**: Automatic memory usage monitoring and warnings
- **Network Optimized**: Platform implementations handle network efficiency

## 🧪 Testing Features

- **Complete Event Capture**: Test helpers capture all analytics for verification
- **Assertion Helpers**: Rich assertion methods for common verification patterns
- **Mock Analytics**: Simulate network conditions and failures
- **Debug Output**: Pretty-print analytics events for debugging

This module provides the foundation for comprehensive analytics tracking while maintaining code
quality, performance, and developer experience across all platforms.

## 📚 API Documentation

All classes and methods in this module are comprehensively documented with KDoc. The documentation
includes:

### Core API Classes

- **[AnalyticsEvent](src/commonMain/kotlin/template/core/base/analytics/AnalyticsEvent.kt)**:
  Type-safe event representation with builder pattern and validation
- **[AnalyticsHelper](src/commonMain/kotlin/template/core/base/analytics/AnalyticsHelper.kt)**:
  Platform-agnostic analytics interface with convenience methods
- **[Param](src/commonMain/kotlin/template/core/base/analytics/AnalyticsEvent.kt)**: Validated
  parameter class with automatic constraint checking
- **[Types](src/commonMain/kotlin/template/core/base/analytics/AnalyticsEvent.kt)**: Standard event
  type constants organized by category
- **[ParamKeys](src/commonMain/kotlin/template/core/base/analytics/AnalyticsEvent.kt)**: Standard
  parameter key constants for consistency

### Extension Functions

- **[AnalyticsExtensions](src/commonMain/kotlin/template/core/base/analytics/AnalyticsExtensions.kt)
  **: Builder functions, timing utilities, and batch processing
- **[PerformanceTracker](src/commonMain/kotlin/template/core/base/analytics/PerformanceTracker.kt)
  **: Advanced performance monitoring and timing utilities
- **[UiHelpers](src/commonMain/kotlin/template/core/base/analytics/UiHelpers.kt)**: Jetpack Compose
  integration helpers

### Implementations

- *
  *[FirebaseAnalyticsHelper](src/nonJsCommonMain/kotlin/template/core/base/analytics/FirebaseAnalyticsHelper.kt)
  **: Production Firebase Analytics implementation
- **[StubAnalyticsHelper](src/commonMain/kotlin/template/core/base/analytics/StubAnalyticsHelper.kt)
  **: Development implementation with console logging
- **[NoOpAnalyticsHelper](src/commonMain/kotlin/template/core/base/analytics/NoOpAnalyticsHelper.kt)
  **: No-operation implementation for testing

### Testing & Validation

- **[TestingUtils](src/commonMain/kotlin/template/core/base/analytics/TestingUtils.kt)**:
  Comprehensive testing utilities and mock implementations
- **[ValidationUtils](src/commonMain/kotlin/template/core/base/analytics/ValidationUtils.kt)**: Data
  validation and sanitization utilities

### Documentation Features

- ✅ **Detailed Descriptions**: Every class and method has comprehensive documentation
- ✅ **Parameter Documentation**: All parameters documented with @param tags
- ✅ **Usage Examples**: @sample blocks with practical code examples
- ✅ **Cross-References**: @see tags linking related functionality
- ✅ **Platform Notes**: Platform-specific behavior and constraints documented
- ✅ **Error Conditions**: Exception throwing conditions clearly documented
- ✅ **Since Tags**: Version information for API tracking

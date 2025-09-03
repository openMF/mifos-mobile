# Platform Module Documentation

## Overview

The `platform` module provides a comprehensive abstraction layer for platform-specific
implementations across multiple targets (Android, Desktop, JS, Native, WasmJs) in a Kotlin
Multiplatform project. It follows the expect/actual pattern to create uniform APIs that work
seamlessly across platforms while maintaining native functionality.

### Core Purpose

- Isolate platform-specific code to improve maintainability
- Provide consistent APIs across platforms
- Enable platform-specific optimizations without changing client code
- Support Kotlin Multiplatform Project (KMP) architecture

## Architecture Design

### Composition Pattern

The module uses Jetpack Compose's `CompositionLocal` pattern to provide platform-specific
implementations throughout the application without explicit dependency injection. This creates a
hierarchy of providers that can be accessed from any composable function.

```
App
└── LocalManagerProvider
    ├── LocalAppReviewManager
    ├── LocalIntentManager
    └── LocalAppUpdateManager
```

### Platform Implementation Strategy

For each platform, three levels of abstraction are implemented:

1. **Common Interfaces**: Defined in `commonMain` using `expect` declarations
2. **Platform Interfaces**: Platform-specific abstractions in respective source sets
3. **Concrete Implementations**: Platform-specific implementations of the interfaces

```
commonMain
├── Interfaces (expect)
├── Models
└── Utilities
    
androidMain
├── Concrete implementations
└── Android-specific utilities

desktopMain/jsMain/nativeMain/wasmJsMain
└── Placeholder implementations
```

## Common Interfaces and Types

### AppContext

```kotlin
// Platform-agnostic representation of context
expect abstract class AppContext

// Access to current context
expect val LocalContext: ProvidableCompositionLocal<AppContext>

// Access to current activity
expect val AppContext.activity: Any
```

The `AppContext` provides a platform-agnostic way to access contextual information needed for
platform operations:
> Use `LocalContext.current` to get AppContext Aka `android.content.Context`
- `AppContext`: Represents the platform's context (e.g., `android.content.Context`)
- `LocalContext`: Provides access to the current `AppContext` through Compose's `CompositionLocal`
- `AppContext.activity`: Provides access to the current activity (e.g., `android

### Manager Providers

```kotlin
@Composable
expect fun LocalManagerProvider(
    context: AppContext,
    content: @Composable () -> Unit,
)
```

This composable function sets up the platform-specific managers and provides them through
`CompositionLocalProvider`. It's designed to wrap your app's content and make all managers available
to child composables.

### IntentManager

```kotlin
interface IntentManager {
   // Launch a platform-specific intent
   fun startActivity(intent: Any)

   // Open a URI in an appropriate app
   fun launchUri(uri: String)

   // Share text with platform sharing mechanism
   fun shareText(text: String)

   // Share a file with appropriate MIME type
   fun shareFile(fileUri: String, mimeType: MimeType)

   // Extract shared data from incoming intents
   fun getShareDataFromIntent(intent: Any): ShareData?

   // Create an intent for document creation
   fun createDocumentIntent(fileName: String): Any

   // Launch application settings
   fun startApplicationDetailsSettingsActivity()

   // Open default email application
   fun startDefaultEmailApplication()

   // Data wrapper for incoming shared content
   sealed class ShareData {
      data class TextSend(val subject: String?, val text: String) : ShareData()
      // Extensible for future share types (images, files, etc.)
   }
}
```

The `IntentManager` provides platform-agnostic operations for working with platform-specific intents
and sharing mechanisms. It handles:

- Activity and URI launching
- Content sharing
- Settings navigation
- Document creation
- Handling incoming shared content

### AppReviewManager

```kotlin
interface AppReviewManager {
    // Trigger platform's native review prompt
    fun promptForReview()

    // Launch custom review implementation
    fun promptForCustomReview()
}
```

The `AppReviewManager` abstracts in-app review functionality:

- On Android: Uses Google Play In-App Review API
- On other platforms: Provides placeholder implementations for future extension

### AppUpdateManager

```kotlin
interface AppUpdateManager {
    // Check for available updates
    fun checkForAppUpdate()

    // Resume interrupted update processes
    fun checkForResumeUpdateState()
}
```

The `AppUpdateManager` handles update checking and flow management:

- On Android: Implements Google Play In-App Update API
- On other platforms: Provides placeholder implementations

### MimeType

```kotlin
enum class MimeType(val value: String, vararg val extensions: String) {
    // Images
    IMAGE_JPEG("image/jpeg", "jpg", "jpeg"),
    IMAGE_PNG("image/png", "png"),
    // ... many more types

    // Default for unknown types
    UNKNOWN("application/octet-stream"),

    companion object {
        // Maps file extensions to MimeType
        private val extensionToMimeType = mutableMapOf<String, MimeType>()

        init {
            // Populate the map during initialization
            entries.forEach { mimeType ->
                mimeType.extensions.forEach { extension ->
                    extensionToMimeType[extension] = mimeType
                }
            }
        }

        // Get MimeType from file extension
        fun fromExtension(extension: String): MimeType

        // Get MimeType from filename
        fun fromFileName(fileName: String): MimeType
    }
}
```

The `MimeType` enum provides a comprehensive catalog of MIME types with:

- String representation for platform APIs
- Associated file extensions
- Helper methods for determining types from filenames or extensions
- Organized categories (images, videos, audio, documents, archives)

## Android Implementation

### AppContext (Android)

```kotlin
actual typealias AppContext = android.content.Context

actual val LocalContext: ProvidableCompositionLocal<AppContext>
    get() = androidx.compose.ui.platform.LocalContext

actual val AppContext.activity: Any
    @Composable
    get() = requireNotNull(LocalActivity.current)
```

The Android implementation:

- Maps `AppContext` directly to Android's `Context`
- Uses Compose UI's `LocalContext` for provider
- Returns the current activity from `LocalActivity`

### IntentManagerImpl (Android)

```kotlin
class IntentManagerImpl(private val context: Context) : IntentManager {
    // Implementation details
}
```

Key implementation features:

1. **URI Handling**:
    - Handles `androidapp://` scheme for app store links
    - Normalizes schemes for web URLs
    - Handles platform-specific intents

2. **Activity Starting**:
    - Uses Android's `startActivity` to launch intents
    - Catches `ActivityNotFoundException` to prevent crashes

3. **Sharing**:
    - Creates `ACTION_SEND` intents for text sharing
    - Handles file sharing with appropriate MIME types
    - Adds promotional text to file shares

4. **Intent Processing**:
    - Extracts text content from incoming share intents
    - Creates document intents with appropriate MIME types

5. **Settings Navigation**:
    - Opens application details settings
    - Launches default email application

6. **Play Store Interaction**:
    - Constructs Play Store URIs for app installations
    - Falls back to Play Store when direct app launch fails

### AppReviewManagerImpl (Android)

```kotlin
class AppReviewManagerImpl(private val activity: Activity) : AppReviewManager {
    override fun promptForReview() {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo)
            } else {
                Log.e("Failed to launch review flow.", task.exception?.message.toString())
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d("ReviewManager", "Prompting for review")
        }
    }

    override fun promptForCustomReview() {
        // TODO:: Implement custom review flow
    }
}
```

Implementation details:

- Uses Google Play Core library's `ReviewManagerFactory`
- Handles asynchronous flow with callbacks
- Logs failures for debugging
- Includes debug logging for development builds
- Placeholder for custom review implementation

### AppUpdateManagerImpl (Android)

```kotlin
class AppUpdateManagerImpl(private val activity: Activity) : AppUpdateManager {
    private val manager = AppUpdateManagerFactory.create(activity)
    private val updateOptions = AppUpdateOptions
        .newBuilder(AppUpdateType.IMMEDIATE)
        .setAllowAssetPackDeletion(false)
        .build()

    // Implementation details
}
```

Key features:

- Uses Google Play Core library's `AppUpdateManagerFactory`
- Configures for immediate update type
- Skips update checks in debug builds
- Checks for and handles interrupted update flows
- Uses success/failure listeners for async operations
- Implements request code handling for update flow

### LocalManagerProviders (Android)

```kotlin
@Composable
actual fun LocalManagerProvider(
    context: AppContext,
    content: @Composable () -> Unit,
) {
    val activity = context.activity as Activity
    CompositionLocalProvider(
        LocalAppReviewManager provides AppReviewManagerImpl(activity),
        LocalIntentManager provides IntentManagerImpl(activity),
        LocalAppUpdateManager provides AppUpdateManagerImpl(activity),
    ) {
        content()
    }
}
```

This implementation:

- Extracts the Android `Activity` from the context
- Creates concrete Android implementations of each manager
- Provides them through `CompositionLocalProvider`

## Non-Android Platform Implementations

For Desktop, JS, Native, and WasmJs platforms, the implementations follow similar patterns:

### Context Implementation

```kotlin
actual abstract class AppContext private constructor() {
    companion object {
        val INSTANCE = object : AppContext() {}
    }
}

actual val LocalContext: ProvidableCompositionLocal<AppContext>
    get() = staticCompositionLocalOf { AppContext.INSTANCE }

actual val AppContext.activity: Any
    @Composable
    get() = AppContext.INSTANCE
```

The non-Android implementations:

- Use a singleton pattern via companion object
- Provide the same instance for both context and activity

### Manager Implementations

```kotlin
class IntentManagerImpl : IntentManager {
    override fun startActivity(intent: Any) {
        // TODO("Not yet implemented")
    }

    // Other methods with TODO placeholders
}

class AppReviewManagerImpl : AppReviewManager {
    override fun promptForReview() {
        // Empty implementation
    }

    override fun promptForCustomReview() {
        // TODO:: Implement custom review flow
    }
}

class AppUpdateManagerImpl : AppUpdateManager {
    override fun checkForAppUpdate() {
        // Empty implementation
    }

    override fun checkForResumeUpdateState() {
        // Empty implementation
    }
}
```

These implementations:

- Provide empty or placeholder implementations
- Use TODO comments to mark future implementation points
- Return default values for required return types

## Advanced Usage Examples

### Using IntentManager for Deep Linking

```kotlin
@Composable
fun DeepLinkHandler(uri: String?) {
    val intentManager = LocalIntentManager.current

    LaunchedEffect(uri) {
        uri?.let {
            intentManager.launchUri(it)
        }
    }
}
```

### Implementing Custom Review Flow

```kotlin
class MyCustomReviewManager(
    private val appReviewManager: AppReviewManager = LocalAppReviewManager.current
) {
    fun showReviewAfterSuccessfulOperation(operationCount: Int) {
        // Track usage and show review at appropriate times
        if (operationCount > 5 && shouldShowReview()) {
            appReviewManager.promptForReview()
            markReviewShown()
        }
    }

    private fun shouldShowReview(): Boolean {
        // Your custom logic
        return true
    }

    private fun markReviewShown() {
        // Your tracking logic
    }
}
```

### Handling Incoming Shared Content

```kotlin
@Composable
fun ShareReceiver(intent: Any) {
    val intentManager = LocalIntentManager.current
    val shareData = intentManager.getShareDataFromIntent(intent)

    when (shareData) {
        is IntentManager.ShareData.TextSend -> {
            // Handle received text
            Text("Received: ${shareData.text}")
        }
        else -> {
            // Handle other types or null
            Text("No sharable content found")
        }
    }
}
```

### Managing App Updates

```kotlin
@Composable
fun UpdateCheckScreen() {
    val updateManager = LocalAppUpdateManager.current
    val networkAvailable = rememberNetworkState()

    LaunchedEffect(networkAvailable) {
        if (networkAvailable) {
            updateManager.checkForAppUpdate()
        }
    }

    // UI content
}
```

## Integration Patterns

### Basic Setup in App Root

```kotlin
@Composable
fun App() {
    val context = LocalContext.current

    LocalManagerProvider(context) {
        AppNavigation()
    }
}
```

### With Navigation Component

```kotlin
@Composable
fun AppWithNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    LocalManagerProvider(context) {
        NavHost(navController, startDestination = "home") {
            composable("home") { HomeScreen() }
            composable("settings") { SettingsScreen() }
            // Other destinations
        }
    }
}
```

### In Activities with Manual Initialization

```kotlin
class MainActivity : ComponentActivity() {
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerImpl(this)

        setContent {
            val context = LocalContext.current

            LocalManagerProvider(context) {
                // App content
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.checkForResumeUpdateState()
    }
}
```

## Best Practices

### 1. Manager Access

- Use `LocalX.current` within composables
- Inject managers via parameters for testability
- Don't store managers in view models unless necessary

```kotlin
// Good practice
@Composable
fun MyScreen(
    intentManager: IntentManager = LocalIntentManager.current
) {
    // Use intentManager
}

// For testing
@Test
fun testMyScreen() {
    val mockIntentManager = MockIntentManager()
    composeTestRule.setContent {
        MyScreen(intentManager = mockIntentManager)
    }
}
```

### 2. Platform-Specific Code

- Keep platform-specific code within manager implementations
- Use conditional compilation for minor platform differences
- Create separate high-level abstractions for major platform differences

### 3. Error Handling

- Handle platform-specific exceptions within manager implementations
- Provide consistent error reporting across platforms
- Log detailed errors in debug builds

### 4. Testing

- Create test fakes or mocks of manager interfaces
- Test platform-specific implementations separately
- Use dependency injection for testability

## Extending the Platform Module

### Adding New Manager Types

1. Define the interface in `commonMain`:
   ```kotlin
   interface MyNewManager {
       fun doSomething()
   }
   ```

2. Create the `CompositionLocal` provider:
   ```kotlin
   val LocalMyNewManager: ProvidableCompositionLocal<MyNewManager> = compositionLocalOf {
       error("CompositionLocal MyNewManager not present")
   }
   ```

3. Implement for each platform:
   ```kotlin
   // Android
   class MyNewManagerImpl(private val context: Context) : MyNewManager {
       override fun doSomething() {
           // Android implementation
       }
   }
   
   // Other platforms
   class MyNewManagerImpl : MyNewManager {
       override fun doSomething() {
           // Implementation or placeholder
       }
   }
   ```

4. Update `LocalManagerProvider` for each platform:
   ```kotlin
   @Composable
   actual fun LocalManagerProvider(
       context: AppContext,
       content: @Composable () -> Unit,
   ) {
       // Existing providers
       CompositionLocalProvider(
           // Existing providers
           LocalMyNewManager provides MyNewManagerImpl(context),
       ) {
           content()
       }
   }
   ```

### Adding New Platform Targets

1. Create the appropriate source set in `build.gradle.kts`
2. Implement the required `actual` declarations
3. Create platform-specific manager implementations

## Troubleshooting

### Common Issues

1. **CompositionLocal errors**:
   ```
   java.lang.IllegalStateException: CompositionLocal LocalIntentManager not present
   ```

   **Solution**: Ensure your composable is called within the scope of a `LocalManagerProvider`.

2. **Context casting errors**:
   ```
   java.lang.ClassCastException: android.content.Context cannot be cast to android.app.Activity
   ```

   **Solution**: Ensure you're using an Activity context when required.

3. **Permissions issues**:
   ```
   java.lang.SecurityException: Permission Denial: starting Intent
   ```

   **Solution**: Verify required permissions are declared in AndroidManifest.xml.

### Platform-Specific Issues

**Android**:

- In-App Review not showing: Google limits frequency of review prompts
- Update flow interruptions: Handle onActivityResult and resume the flow

**Desktop/Web/Native**:

- Placeholder implementations: Replace TODOs with actual implementations

## Design Philosophy

The platform module follows several key design principles:

1. **Separation of Concerns**: Isolates platform-specific code
2. **Interface Segregation**: Each manager has a focused responsibility
3. **Dependency Inversion**: High-level modules depend on abstractions
4. **Composition Over Inheritance**: Uses composition for flexibility

This approach allows for:

- Platform-specific optimizations
- Easy extensibility
- Testability
- Code reuse across platforms

## Relation to Project Architecture

In the overall architecture:

1. UI components depend on platform managers via CompositionLocal
2. Managers abstract platform-specific functionality
3. The common module provides cross-platform interfaces
4. Each platform module provides concrete implementations

This creates a clean dependency flow:

```
UI → Managers (Interface) → Platform Implementation
```
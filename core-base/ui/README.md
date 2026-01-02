# Documentation: core-base/ui Module

The `core-base/ui` module serves as the foundation for building consistent, cross-platform user
interfaces in Kotlin Multiplatform projects. This documentation explores the inner workings,
implementation details, and best practices for each component.

## Architectural Foundation

This module implements the unidirectional data flow pattern within the MVVM architecture, which
creates a predictable and testable application structure:

1. **State flows down**: UI receives immutable state snapshots
2. **Actions flow up**: User interactions are sent as discrete actions
3. **Events are one-shot**: Navigation and notifications occur once, not continuously

This pattern helps prevent common UI bugs like inconsistent state, navigation loops, and race
conditions by enforcing a strict cycle of state updates.

## 1. BaseViewModel Implementation (`BaseViewModel.kt`)

### Core Mechanics

The `BaseViewModel` serves as the cornerstone for UI state management, using Kotlin coroutines and
channels to manage the application's data flow:

```kotlin
abstract class BaseViewModel<S, E, A>(initialState: S) : ViewModel() {
    protected val mutableStateFlow: MutableStateFlow<S> = MutableStateFlow(initialState)
    private val eventChannel: Channel<E> = Channel(capacity = Channel.UNLIMITED)
    private val internalActionChannel: Channel<A> = Channel(capacity = Channel.UNLIMITED)

    // Public immutable interfaces
    val stateFlow: StateFlow<S> = mutableStateFlow.asStateFlow()
    val eventFlow: Flow<E> = eventChannel.receiveAsFlow()
    val actionChannel: SendChannel<A> = internalActionChannel

    // Initialize action processing
    init {
        viewModelScope.launch {
            internalActionChannel
                .consumeAsFlow()
                .collect { action -> handleAction(action) }
        }
    }

    protected abstract fun handleAction(action: A)
}
```

The `Channel.UNLIMITED` capacity ensures that actions and events won't be dropped if they're emitted
faster than they can be processed, which is crucial for maintaining UI integrity.

### Advanced Usage Patterns

Beyond the basic implementation, effective `BaseViewModel` usage includes:

**1. State Splitting**

For complex screens, consider splitting state into logical subgroups:

```kotlin
data class ProfileState(
    val userData: UserDataState = UserDataState(),
    val settings: SettingsState = SettingsState(),
    val interaction: InteractionState = InteractionState()
)

data class UserDataState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

data class SettingsState(
    val notifications: Boolean = true,
    val darkMode: Boolean = false
)

data class InteractionState(
    val selectedTab: Tab = Tab.PROFILE,
    val isEditMode: Boolean = false
)
```

This approach makes it easier to update only relevant portions of state and prevents unnecessary
recompositions.

**2. Action Chaining**

For complex operations that require multiple state updates:

```kotlin
override fun handleAction(action: ProfileAction) {
    when (action) {
        is ProfileAction.UpdateProfile -> {
            mutableStateFlow.value = state.copy(
                userData = state.userData.copy(isLoading = true)
            )

            viewModelScope.launch {
                try {
                    val updatedUser = userRepository.updateProfile(action.updates)
                    sendAction(ProfileAction.ProfileUpdateSuccess(updatedUser))
                } catch (e: Exception) {
                    sendAction(ProfileAction.ProfileUpdateFailure(e.message ?: "Unknown error"))
                }
            }
        }

        is ProfileAction.ProfileUpdateSuccess -> {
            mutableStateFlow.value = state.copy(
                userData = state.userData.copy(
                    isLoading = false,
                    user = action.user,
                    error = null
                )
            )
            sendEvent(ProfileEvent.ShowSuccessMessage("Profile updated successfully"))
        }

        is ProfileAction.ProfileUpdateFailure -> {
            mutableStateFlow.value = state.copy(
                userData = state.userData.copy(
                    isLoading = false,
                    error = action.message
                )
            )
            sendEvent(ProfileEvent.ShowErrorMessage(action.message))
        }
    }
}
```

**3. Shared Actions**

For actions that need to be processed by multiple ViewModels, define them in a shared location and
have each ViewModel handle the subset it cares about:

```kotlin
sealed class AppAction {
    object LogOut : AppAction()
    data class NetworkStatusChanged(val isConnected: Boolean) : AppAction()
    data class ThemeChanged(val isDarkMode: Boolean) : AppAction()
}

// Then in ViewModels, handle relevant actions:
override fun handleAction(action: AppAction) {
    when (action) {
        is AppAction.ThemeChanged -> {
            // Only handle theme changes in this ViewModel
            mutableStateFlow.value = state.copy(isDarkMode = action.isDarkMode)
        }
        else -> {
            // Ignore other AppActions
        }
    }
}
```

## 2. Events System (`BackgroundEvent.kt`, `EventsEffect.kt`)

### Understanding the Event Flow

The events system has several critical components working together:

1. `eventChannel`: A backing `Channel` that buffers events
2. `eventFlow`: Public `Flow` for consuming events once
3. `EventsEffect`: A composable that consumes events with lifecycle awareness
4. `BackgroundEvent`: A marker interface for events that bypass lifecycle checks

### Implementation Details

The `EventsEffect` composable uses a `LaunchedEffect` to safely collect events within the
composition lifecycle:

```kotlin
@Composable
fun <E> EventsEffect(
    viewModel: BaseViewModel<*, E, *>,
    lifecycleOwner: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    handler: suspend (E) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow
            .filter {
                it is BackgroundEvent ||
                    lifecycleOwner.currentState.isAtLeast(Lifecycle.State.RESUMED)
            }
            .onEach { handler.invoke(it) }
            .launchIn(this)
    }
}
```

The `filter` operator is crucial here—it ensures that events are only processed when:

- The event implements the `BackgroundEvent` interface, OR
- The screen is currently visible (in the `RESUMED` state)

This prevents navigation events from triggering multiple times during configuration changes or when
returning to a screen from the background.

### Types of Events

Events typically fall into four categories:

1. **Navigation Events**: Direct the user to a new screen
   ```kotlin
   data class NavigateTo(val route: String, val popUpTo: String? = null) : UiEvent
   ```

2. **Message Events**: Show transient UI like toasts or snackbars
   ```kotlin
   data class ShowMessage(val message: String, val type: MessageType) : UiEvent
   ```

3. **Dialog Events**: Display modal UI elements
   ```kotlin
   data class ShowDialog(val title: String, val message: String) : UiEvent
   ```

4. **System Events**: Interact with system components like camera or permissions
   ```kotlin
   object RequestCameraPermission : UiEvent, BackgroundEvent
   ```

## 3. Lifecycle Observer (`LifecycleEventEffect.kt`)

The `LivecycleEventEffect` composable provides a clean way to observe and respond to Android
lifecycle events within compositions. It uses the `DisposableEffect` API to ensure proper cleanup.

### Implementation Analysis

The implementation uses a clever combination of `rememberUpdatedState` and `DisposableEffect`:

```kotlin
@Composable
fun LivecycleEventEffect(
    onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit,
) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}
```

The `rememberUpdatedState` calls are essential: they ensure that if `onEvent` or the`lifecycleOwner`
changes during composition, the observer always uses the most current versions without needing to
resubscribe.

### Advanced Lifecycle Handling

When working with complex screens that may have their own internal composition lifecycles:

```kotlin
@Composable
fun ComplexScreenWithTabs(viewModel: ComplexViewModel) {
    var currentTab by remember { mutableStateOf(Tab.HOME) }

    // Main screen lifecycle
    LivecycleEventEffect { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.trySendAction(ComplexAction.ScreenResumed)
                Analytics.logScreenView("ComplexScreen")
            }
            Lifecycle.Event.ON_PAUSE -> {
                viewModel.trySendAction(ComplexAction.ScreenPaused)
            }
            Lifecycle.Event.ON_DESTROY -> {
                viewModel.trySendAction(ComplexAction.SaveState)
            }
            else -> { /* Ignore other events */
            }
        }
    }

    // Tab-specific behavior
    when (currentTab) {
        Tab.HOME -> HomeTab(
            onEnter = { /* Tab-specific enter logic */ },
            onExit = { /* Tab-specific exit logic */ }
        )
        Tab.PROFILE -> ProfileTab(
            onEnter = { /* Tab-specific enter logic */ },
            onExit = { /* Tab-specific exit logic */ }
        )
    }
}
```

This pattern allows separation of screen-level lifecycle events from tab-specific behavior.

## 4. Navigation Extensions (`NavGraphBuilderExtensions.kt`, `Transition.kt`)

The navigation system provides a rich set of transition patterns that create a cohesive,
motion-driven navigation experience.

### Transition Animation Details

Each transition type is carefully timed and coordinated:

**Slide Transitions (450ms)**:

- Slide content from bottom to top (enter) or top to bottom (exit)
- Used for modal dialogs and bottom sheets

**Push Transitions (350ms)**:

- Horizontal sliding with synchronized fading
- Content slides in from right/out to left for forward navigation
- Content slides in from left/out to right when going back
- Includes a subtle overlap timing to create a natural feeling of depth

**Stay Transitions**:

- No visible movement to maintain context
- Maintains visibility for the duration of other concurrent transitions
- Uses fade transitions with minimal alpha changes (from 1.0 to 0.99) to keep Compose from
  optimizing away the animation

### Intelligent Transition Handling

The most sophisticated aspect is the handling of nested navigation:

```kotlin
val AnimatedContentTransitionScope<NavBackStackEntry>.isSameGraphNavigation: Boolean
    get() = initialState.destination.parent == targetState.destination.parent
```

This property checks if we're navigating between destinations within the same parent graph, and
transitions will only apply within the same graph, allowing for hierarchical navigation patterns.

```kotlin
val fadeIn: EnterTransitionProvider = {
    RootTransitionProviders.Enter
        .fadeIn(this)
        .takeIf { isSameGraphNavigation }
}
```

By returning `null` when navigating between different graphs, this allows parent navigators to
define transitions for cross-graph navigation while child navigators handle transitions within their
scope.

## 5. Image Loading (`ImageLoaderExt.kt`)

The image loading system abstracts Coil's capabilities across platforms while providing sensible
defaults and optimization.

### Memory Management

The system intelligently manages memory based on platform constraints:

```kotlin
internal fun rememberDefaultImageLoader(context: PlatformContext): ImageLoader {
    return remember(context) {
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)  // Use 25% of available memory
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }
}
```

The `maxSizePercent` call is crucial—it adapts the cache size to the device's available memory,
ensuring efficient resource usage across a wide range of devices.

### Common Use Patterns

For profile pictures and avatars:

```kotlin
@Composable
fun CircularProfileImage(url: String, size: Dp = 48.dp) {
    val imageLoader = rememberImageLoader()

    AsyncImage(
        model = rememberImageRequest(context, url),
        contentDescription = "Profile picture",
        imageLoader = imageLoader,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder_profile),
        error = painterResource(R.drawable.error_profile)
    )
}
```

For background images:

```kotlin
@Composable
fun BackgroundImage(url: String, overlay: Color = Color.Black.copy(alpha = 0.3f)) {
    val imageLoader = rememberImageLoader()

    Box {
        AsyncImage(
            model = rememberImageRequest(context, url),
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlay)
        )
    }
}
```

## 6. Performance Monitoring (`JankStatsExtensions.kt`)

Performance monitoring is crucial for delivering smooth UIs. The JankStats integration helps
identify and address UI performance issues.

### Jank Detection Mechanism

"Jank" refers to frames that take longer than 16.67ms (for 60fps) to render, causing visible
stuttering. The Android-specific implementation uses the Metrics API:

```kotlin
@Composable
actual fun TrackScrollJank(scrollableState: ScrollableState, stateName: String) {
    TrackJank(scrollableState) { metricsHolder ->
        snapshotFlow { scrollableState.isScrollInProgress }.collect { isScrollInProgress ->
            metricsHolder.state?.apply {
                if (isScrollInProgress) {
                    putState(stateName, "Scrolling=true")
                } else {
                    removeState(stateName)
                }
            }
        }
    }
}
```

When scrolling starts, the system marks the current frames with the provided state name. This allows
the performance tools to attribute jank to specific UI interactions.

### Performance Optimization Strategies

To minimize jank in scrolling lists:

1. **Minimize composition cost**: Use `key` for list items to prevent unnecessary recomposition
2. **Avoid nested scrolling**: Nested scrollable containers can compound performance issues
3. **Lazy loading**: Only load visible items and maintain a reasonable buffer
4. **Pre-compute complex layouts**: Calculate layout parameters ahead of time
5. **Bitmap caching**: For complex images, pre-compute and cache bitmaps
6. **Avoid allocation in scroll**: Don't create new objects during scrolling

Example of a performance-optimized list:

```kotlin
@Composable
fun OptimizedList(items: List<ListItem>) {
    val listState = rememberLazyListState()

    // Track scrolling performance
    TrackScrollJank(listState, "main_list")

    // Pre-compute expensive stuff
    val coloredItems = remember(items) {
        items.map { it.copy(color = calculateComplexColor(it)) }
    }

    LazyColumn(state = listState) {
        items(
            items = coloredItems,
            key = { it.id } // Stable key for efficient updates
        ) { item ->
            // Cached layout calculation
            val layoutInfo = remember(item.id) {
                calculateLayout(item)
            }

            ListItemRow(
                item = item,
                layoutInfo = layoutInfo,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}
```

## 7. Cross-Platform Sharing (`ShareUtils.kt`)

The `ShareUtils` object provides a unified API for sharing content, with platform-specific
implementations handling the technical details.

### Platform Implementation Details

**Android Implementation**:

- Uses Android's `Intent` system with `ACTION_SEND`
- For images, first saves to cache directory, then creates a `FileProvider` URI
- Requires a valid Activity context from `activityProvider`

**iOS (Native) Implementation**:

- Uses `UIActivityViewController` for sharing
- Requires the root view controller from `UIApplication.sharedApplication()`

**Desktop/JS/WASM Implementations**:

- Use `FileKit` to save content to disk since direct sharing is less standardized
- For images, converts `ImageBitmap` to pixel data before saving

### Security Considerations

The Android implementation includes important security features:

```kotlin
private suspend fun saveImage(image: Bitmap, context: Context): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val imagesFolder = File(context.cacheDir, "images")
            // ... save image ...

            // Use FileProvider for secure content sharing
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: IOException) {
            Log.d("saving bitmap", "saving bitmap error ${e.message}")
            null
        }
    }
}
```

Using `FileProvider` instead of direct file URIs is essential for API 24+ compatibility and
security. The provider creates content URIs that grant temporary access to the files being shared,
without exposing file system paths.

## 8. String Extensions (`StringExt.kt`)

The `capitalizeEachWord` extension demonstrates how simple utility functions can improve code
clarity and consistency.

```kotlin
val String.capitalizeEachWord: String
    get() = this.split(" ").joinToString(" ") { word ->
        word.takeIf { it.isNotEmpty() }
            ?.let { it.first().uppercase() + it.substring(1).lowercase() }
            ?: ""
    }
```

This implementation handles edge cases like:

- Empty strings
- Words containing only a single character
- Strings with multiple consecutive spaces

For multi-lingual applications, consider extending this with locale-aware capitalization:

```kotlin
fun String.capitalizeEachWordWithLocale(locale: Locale): String {
    return this.split(" ").joinToString(" ") { word ->
        word.takeIf { it.isNotEmpty() }
            ?.let { it.replaceFirstChar { char -> char.titlecase(locale) } }
            ?: ""
    }
}
```

## 9. Reporting Drawn State (`ReportDrawnExt.kt`)

The `ReportDrawnWhen` composable is an important performance optimization that tells the system when
content is considered meaningfully drawn.

### Platform-Specific Implementations

On Android, the implementation delegates to the Android Compose implementation:

```kotlin
@Composable
actual fun ReportDrawnWhen(block: () -> Boolean) {
    androidx.activity.compose.ReportDrawnWhen { block() }
}
```

On other platforms, the implementation is a no-op, preserving the API surface without requiring
platform-specific functionality:

```kotlin
@Composable
actual fun ReportDrawnWhen(block: () -> Boolean) {
    // No-op implementation
}
```

### Performance Impact

This composable has a significant performance impact on initial screen rendering. Android uses this
signal to:

1. Mark the activity as drawn for launcher animations
2. Complete "warm start" timing measurements
3. Report performance metrics to developer tools

A common pattern is to report drawn status once critical content is visible, even if background
loading continues:

```kotlin
@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state by viewModel.stateFlow.collectAsState()

    Column {
        TopBar()

        when (val currentState = state) {
            is Loading -> LoadingIndicator()
            is Success -> {
                NewsList(currentState.headlines)

                // Asynchronously load recommended stories
                LaunchedEffect(Unit) {
                    viewModel.trySendAction(LoadRecommendations)
                }
            }
            is Error -> ErrorView(currentState.message)
        }
    }

    // Report as drawn once headlines are loaded, even if recommendations are still loading
    ReportDrawnWhen {
        state is Success
    }
}
```

## 10. Shared Element Transitions (`SharedElementExt.kt`)

The shared element transition system enables smooth visual continuity between screens using Material
3's shared element transitions.

### Implementation Details

The system uses composition locals to provide access to animation scopes:

```kotlin
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
```

These locals enable any composable in the hierarchy to participate in transitions regardless of
their depth in the UI tree.

### Advanced Transition Patterns

Beyond basic image transitions, shared elements can be used for:

1. **Expanding Cards**: A card expands into a full screen detail view
   ```kotlin
   Card(
       modifier = Modifier
           .sharedElement(
               scope = sharedTransitionScope,
               state = rememberSharedContentState(key = "card-${item.id}")
           )
   ) {
       // Card content
   }
   
   // In detail screen:
   Surface(
       modifier = Modifier
           .fillMaxSize()
           .sharedElement(
               scope = sharedTransitionScope,
               state = rememberSharedContentState(key = "card-${item.id}")
           )
   ) {
       // Detail content
   }
   ```

2. **Text Transitions**: Text elements that move and resize
   ```kotlin
   Text(
       text = item.title,
       style = MaterialTheme.typography.titleMedium,
       modifier = Modifier.sharedElement(
           scope = sharedTransitionScope,
           state = rememberSharedContentState(key = "title-${item.id}")
       )
   )
   
   // In detail screen:
   Text(
       text = item.title,
       style = MaterialTheme.typography.headlineLarge,
       modifier = Modifier.sharedElement(
           scope = sharedTransitionScope,
           state = rememberSharedContentState(key = "title-${item.id}")
       )
   )
   ```

3. **Color Transitions**: Smoothly changing colors between screens
   ```kotlin
   Box(
       modifier = Modifier
           .background(item.color)
           .sharedElement(
               scope = sharedTransitionScope,
               state = rememberSharedContentState(key = "color-${item.id}")
           )
   )
   ```

## Comprehensive Testing Strategy

A robust testing strategy ensures the module's reliability across platforms.

### Unit Testing ViewModels

Test ViewModels by verifying state changes, action handling, and event emission:

```kotlin
@Test
fun `when profile loaded successfully, state updated and success event emitted`() = runTest {
        // Given
        val repository = FakeUserRepository()
        val viewModel = ProfileViewModel(repository)
        val events = mutableListOf<ProfileEvent>()
        val job = launch { viewModel.eventFlow.collect { events.add(it) } }

        // When
        viewModel.trySendAction(ProfileAction.LoadProfile("user123"))

        // Then
        assertEquals(false, viewModel.stateFlow.value.userData.isLoading)
        assertNotNull(viewModel.stateFlow.value.userData.user)
        assertEquals("user123", viewModel.stateFlow.value.userData.user?.id)
        assertEquals(1, events.size)
        assertTrue(events[0] is ProfileEvent.ProfileLoaded)

        job.cancel()
    }
```

### Testing Composables

Use the Compose testing library to verify UI behavior:

```kotlin
@Test
fun profileScreen_showsUserData_whenProvided() {
    // Given
    val user = User("123", "Jane Doe", "jane@example.com")
    val state = ProfileState(userData = UserDataState(user = user))

    // When
    composeTestRule.setContent {
        MaterialTheme {
            ProfileScreen(state = state, onAction = {})
        }
    }

    // Then
    composeTestRule.onNodeWithText("Jane Doe").assertIsDisplayed()
    composeTestRule.onNodeWithText("jane@example.com").assertIsDisplayed()
}
```

### Integration Testing

Test component interactions using fake implementations:

```kotlin
@Test
fun navigationEvents_triggerCorrectNavigation() {
    // Given
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    composeTestRule.setContent {
        NavigationTestHost(navController = navController) {
            val viewModel = ProfileViewModel(FakeUserRepository())
            ProfileScreen(viewModel = viewModel)

            // Set up event observation
            EventsEffect(viewModel) { event ->
                when (event) {
                    is ProfileEvent.NavigateToSettings -> {
                        navController.navigate("settings")
                    }
                }
            }
        }
    }

    // When - click settings button
    composeTestRule.onNodeWithContentDescription("Settings").performClick()

    // Then - verify navigation occurred
    assertEquals("settings", navController.currentDestination?.route)
}
```

By combining these testing approaches, you can ensure the core-base/ui module functions correctly
across all supported platforms and integration points.
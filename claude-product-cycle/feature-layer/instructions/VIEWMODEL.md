# ViewModel Patterns

## Table of Contents
1. [BaseViewModel](#baseviewmodel)
2. [State Definition](#state-definition)
3. [ScreenState Definition](#screenstate-definition)
4. [Event Definition](#event-definition)
5. [Action Definition](#action-definition)
6. [Internal Actions](#internal-actions)
7. [handleAction Pattern](#handleaction-pattern)
8. [State Updates](#state-updates)
9. [Async Operations](#async-operations)
10. [Complete Example](#complete-example)

---

## BaseViewModel

All ViewModels extend `BaseViewModel<State, Event, Action>`:

```kotlin
internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
) : BaseViewModel<[Feature]State, [Feature]Event, [Feature]Action>(
    initialState = [Feature]State()
) {
    override fun handleAction(action: [Feature]Action) {
        // Handle user actions
    }
}
```

**Generic Parameters:**
- `State` - UI state data class
- `Event` - One-shot effects (navigation, toasts)
- `Action` - User interactions

---

## State Definition

Immutable data class holding UI state:

```kotlin
@Immutable
data class [Feature]State(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val selectedItem: Item? = null,
    val dialogState: DialogState? = null,
    val uiState: [Feature]ScreenState = [Feature]ScreenState.Loading,
) {
    // Derived properties
    val hasItems: Boolean get() = items.isNotEmpty()
    val isButtonEnabled: Boolean get() = selectedItem != null

    // Nested sealed interface for dialogs
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data class Confirm(val item: Item) : DialogState
    }
}
```

**Rules:**
- Always use `@Immutable` annotation
- Use default values for all properties
- Derived properties via `get()` for computed values
- Nested `DialogState` for dialog management

---

## ScreenState Definition

Loading/Success/Error states:

```kotlin
sealed interface [Feature]ScreenState {
    data object Loading : [Feature]ScreenState
    data object Success : [Feature]ScreenState
    data object Empty : [Feature]ScreenState
    data class Error(val message: StringResource) : [Feature]ScreenState
    data object Network : [Feature]ScreenState
}
```

**Standard States:**
| State | Purpose |
|-------|---------|
| `Loading` | Initial load or refresh |
| `Success` | Data loaded successfully |
| `Empty` | No data available |
| `Error` | Error with message |
| `Network` | No internet connection |

---

## Event Definition

One-shot effects (navigation, toasts):

```kotlin
sealed interface [Feature]Event {
    data object NavigateBack : [Feature]Event
    data class NavigateToDetail(val id: Long) : [Feature]Event
    data class ShowToast(val message: String) : [Feature]Event
    data object ShowSuccess : [Feature]Event
}
```

**When to Use Events:**
- Navigation between screens
- Showing snackbars/toasts
- One-time UI effects
- NOT for state changes (use State instead)

---

## Action Definition

User interactions:

```kotlin
sealed interface [Feature]Action {
    // User-initiated actions
    data object OnRefresh : [Feature]Action
    data object OnRetry : [Feature]Action
    data object OnNavigateBack : [Feature]Action
    data class OnItemClick(val id: Long) : [Feature]Action
    data class OnSearchQuery(val query: String) : [Feature]Action
    data object OnDismissDialog : [Feature]Action
    data class OnDeleteItem(val id: Long) : [Feature]Action

    // Internal actions for async results
    sealed interface Internal : [Feature]Action {
        data class ReceiveData(val result: DataState<Data>) : Internal
        data class ReceiveDeleteResult(val result: DataState<Unit>) : Internal
    }
}
```

**Naming Convention:**
- Prefix with `On` for user actions
- Use present tense (`OnClick`, not `Clicked`)

---

## Internal Actions

Handle async operation results:

```kotlin
sealed interface Internal : [Feature]Action {
    data class ReceiveData(val result: DataState<Data>) : Internal
    data class ReceiveDeleteResult(val result: DataState<Unit>) : Internal
    data class ReceiveUpdateResult(val result: DataState<Item>) : Internal
}
```

**Purpose:**
- Decouple async operations from state updates
- Testable state transitions
- Clear separation of concerns

**Usage in ViewModel:**
```kotlin
private fun loadData() {
    viewModelScope.launch {
        repository.getData().collect { result ->
            sendAction([Feature]Action.Internal.ReceiveData(result))
        }
    }
}

override fun handleAction(action: [Feature]Action) {
    when (action) {
        is [Feature]Action.Internal.ReceiveData -> handleDataResult(action.result)
        // ...
    }
}
```

---

## handleAction Pattern

Central action handler:

```kotlin
override fun handleAction(action: [Feature]Action) {
    when (action) {
        // Navigation actions
        is [Feature]Action.OnNavigateBack -> {
            sendEvent([Feature]Event.NavigateBack)
        }

        // Data actions
        is [Feature]Action.OnRefresh -> loadData()
        is [Feature]Action.OnRetry -> loadData()

        // Item actions
        is [Feature]Action.OnItemClick -> {
            sendEvent([Feature]Event.NavigateToDetail(action.id))
        }
        is [Feature]Action.OnDeleteItem -> deleteItem(action.id)

        // Dialog actions
        is [Feature]Action.OnDismissDialog -> {
            updateState { it.copy(dialogState = null) }
        }

        // Internal actions
        is [Feature]Action.Internal.ReceiveData -> {
            handleDataResult(action.result)
        }
        is [Feature]Action.Internal.ReceiveDeleteResult -> {
            handleDeleteResult(action.result)
        }
    }
}
```

**Best Practices:**
- Keep `when` branches concise
- Delegate complex logic to private functions
- Group related actions together

---

## State Updates

Use `updateState` for immutable updates:

```kotlin
private fun updateState(update: ([Feature]State) -> [Feature]State) {
    mutableStateFlow.update(update)
}

// Simple update
updateState { it.copy(isLoading = true) }

// Multiple properties
updateState { state ->
    state.copy(
        items = newItems,
        uiState = [Feature]ScreenState.Success,
        isLoading = false
    )
}

// Conditional update
updateState { state ->
    state.copy(
        uiState = if (items.isEmpty()) {
            [Feature]ScreenState.Empty
        } else {
            [Feature]ScreenState.Success
        }
    )
}
```

---

## Async Operations

### Basic Pattern

```kotlin
private var loadJob: Job? = null

private fun loadData() {
    loadJob?.cancel()
    loadJob = viewModelScope.launch {
        updateState { it.copy(uiState = [Feature]ScreenState.Loading) }

        repository.getData()
            .collect { result ->
                sendAction([Feature]Action.Internal.ReceiveData(result))
            }
    }
}
```

### Handling Results

```kotlin
private fun handleDataResult(result: DataState<List<Item>>) {
    when (result) {
        is DataState.Loading -> {
            updateState { it.copy(uiState = [Feature]ScreenState.Loading) }
        }
        is DataState.Success -> {
            updateState { state ->
                state.copy(
                    items = result.data,
                    uiState = if (result.data.isEmpty()) {
                        [Feature]ScreenState.Empty
                    } else {
                        [Feature]ScreenState.Success
                    }
                )
            }
        }
        is DataState.Error -> {
            updateState { state ->
                state.copy(
                    uiState = [Feature]ScreenState.Error(
                        Res.string.error_loading_data
                    )
                )
            }
        }
    }
}
```

### Multiple Operations

```kotlin
private var loadJob: Job? = null
private var deleteJob: Job? = null

private fun deleteItem(id: Long) {
    deleteJob?.cancel()
    deleteJob = viewModelScope.launch {
        updateState { it.copy(isDeleting = true) }

        repository.deleteItem(id)
            .collect { result ->
                sendAction([Feature]Action.Internal.ReceiveDeleteResult(result))
            }
    }
}
```

---

## Complete Example

```kotlin
// [Feature].kt - State/Event/Action definitions

@Immutable
data class [Feature]State(
    val items: List<Item> = emptyList(),
    val selectedItem: Item? = null,
    val dialogState: DialogState? = null,
    val uiState: [Feature]ScreenState = [Feature]ScreenState.Loading,
) {
    val hasItems: Boolean get() = items.isNotEmpty()

    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data class ConfirmDelete(val item: Item) : DialogState
    }
}

sealed interface [Feature]ScreenState {
    data object Loading : [Feature]ScreenState
    data object Success : [Feature]ScreenState
    data object Empty : [Feature]ScreenState
    data class Error(val message: StringResource) : [Feature]ScreenState
}

sealed interface [Feature]Event {
    data object NavigateBack : [Feature]Event
    data class NavigateToDetail(val id: Long) : [Feature]Event
    data class ShowToast(val message: String) : [Feature]Event
}

sealed interface [Feature]Action {
    data object OnRefresh : [Feature]Action
    data object OnRetry : [Feature]Action
    data class OnItemClick(val id: Long) : [Feature]Action
    data class OnDeleteClick(val item: Item) : [Feature]Action
    data object OnConfirmDelete : [Feature]Action
    data object OnDismissDialog : [Feature]Action

    sealed interface Internal : [Feature]Action {
        data class ReceiveData(val result: DataState<List<Item>>) : Internal
        data class ReceiveDeleteResult(val result: DataState<Unit>) : Internal
    }
}
```

```kotlin
// [Feature]ViewModel.kt

internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
) : BaseViewModel<[Feature]State, [Feature]Event, [Feature]Action>(
    initialState = [Feature]State()
) {
    private var loadJob: Job? = null
    private var deleteJob: Job? = null

    init {
        loadData()
    }

    override fun handleAction(action: [Feature]Action) {
        when (action) {
            is [Feature]Action.OnRefresh -> loadData()
            is [Feature]Action.OnRetry -> loadData()

            is [Feature]Action.OnItemClick -> {
                sendEvent([Feature]Event.NavigateToDetail(action.id))
            }

            is [Feature]Action.OnDeleteClick -> {
                updateState {
                    it.copy(dialogState = [Feature]State.DialogState.ConfirmDelete(action.item))
                }
            }

            is [Feature]Action.OnConfirmDelete -> {
                val item = (state.dialogState as? [Feature]State.DialogState.ConfirmDelete)?.item
                item?.let { deleteItem(it.id) }
                updateState { it.copy(dialogState = null) }
            }

            is [Feature]Action.OnDismissDialog -> {
                updateState { it.copy(dialogState = null) }
            }

            is [Feature]Action.Internal.ReceiveData -> {
                handleDataResult(action.result)
            }

            is [Feature]Action.Internal.ReceiveDeleteResult -> {
                handleDeleteResult(action.result)
            }
        }
    }

    private fun loadData() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getData().collect { result ->
                sendAction([Feature]Action.Internal.ReceiveData(result))
            }
        }
    }

    private fun deleteItem(id: Long) {
        deleteJob?.cancel()
        deleteJob = viewModelScope.launch {
            repository.deleteItem(id).collect { result ->
                sendAction([Feature]Action.Internal.ReceiveDeleteResult(result))
            }
        }
    }

    private fun handleDataResult(result: DataState<List<Item>>) {
        when (result) {
            is DataState.Loading -> {
                updateState { it.copy(uiState = [Feature]ScreenState.Loading) }
            }
            is DataState.Success -> {
                updateState { state ->
                    state.copy(
                        items = result.data,
                        uiState = if (result.data.isEmpty()) {
                            [Feature]ScreenState.Empty
                        } else {
                            [Feature]ScreenState.Success
                        }
                    )
                }
            }
            is DataState.Error -> {
                updateState {
                    it.copy(uiState = [Feature]ScreenState.Error(Res.string.error_loading))
                }
            }
        }
    }

    private fun handleDeleteResult(result: DataState<Unit>) {
        when (result) {
            is DataState.Loading -> { /* Optional: show loading */ }
            is DataState.Success -> {
                sendEvent([Feature]Event.ShowToast("Item deleted"))
                loadData() // Refresh list
            }
            is DataState.Error -> {
                updateState {
                    it.copy(dialogState = [Feature]State.DialogState.Error(result.message))
                }
            }
        }
    }
}
```

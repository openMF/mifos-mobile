# Implementation Patterns - Mifos Mobile

> **Purpose**: Reference patterns for consistent implementation across features
> **Last Updated**: 2025-12-26

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                       MIFOS MOBILE ARCHITECTURE                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌───────────────┐    ┌───────────────┐    ┌───────────────┐        │
│  │    FEATURE    │    │     DATA      │    │   NETWORK     │        │
│  │    (UI)       │───▶│  (Repository) │───▶│   (Service)   │        │
│  └───────────────┘    └───────────────┘    └───────────────┘        │
│         │                    │                    │                  │
│         ▼                    ▼                    ▼                  │
│   ┌──────────┐        ┌──────────┐         ┌──────────┐             │
│   │ViewModel │        │Repository│         │  Ktorfit │             │
│   │  (MVI)   │        │  Impl    │         │  Service │             │
│   └──────────┘        └──────────┘         └──────────┘             │
│         │                    │                    │                  │
│         ▼                    ▼                    ▼                  │
│   ┌──────────┐        ┌──────────┐         ┌──────────┐             │
│   │  Screen  │        │DataState │         │ Fineract │             │
│   │(Compose) │        │  Flow    │         │   API    │             │
│   └──────────┘        └──────────┘         └──────────┘             │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 1. MVI ViewModel Pattern

### Base Structure

```kotlin
internal class FeatureViewModel(
    private val repository: FeatureRepository,
    private val userPreferences: UserPreferencesRepository,
) : BaseViewModel<FeatureState, FeatureEvent, FeatureAction>(
    initialState = FeatureState()
) {

    init {
        loadInitialData()
    }

    override fun handleAction(action: FeatureAction) {
        when (action) {
            is FeatureAction.Retry -> loadInitialData()
            is FeatureAction.OnItemClick -> handleItemClick(action.id)
            is FeatureAction.OnRefresh -> refreshData()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getData()
                .collect { dataState ->
                    handleDataState(dataState)
                }
        }
    }

    private fun handleDataState(dataState: DataState<Data>) {
        when (dataState) {
            is DataState.Loading -> updateState {
                it.copy(uiState = FeatureScreenState.Loading)
            }
            is DataState.Success -> updateState {
                it.copy(
                    uiState = FeatureScreenState.Success,
                    data = dataState.data
                )
            }
            is DataState.Error -> updateState {
                it.copy(uiState = FeatureScreenState.Error(Res.string.error_message))
            }
        }
    }

    private fun handleItemClick(id: Long) {
        sendEvent(FeatureEvent.NavigateToDetail(id))
    }

    private fun updateState(update: (FeatureState) -> FeatureState) {
        mutableStateFlow.update(update)
    }
}
```

### State Definition

```kotlin
@Immutable
data class FeatureState(
    val clientId: Long? = null,
    val data: List<Item> = emptyList(),
    val isRefreshing: Boolean = false,
    val uiState: FeatureScreenState = FeatureScreenState.Loading,
)

sealed interface FeatureScreenState {
    data object Loading : FeatureScreenState
    data object Success : FeatureScreenState
    data class Error(val message: StringResource) : FeatureScreenState
    data object Empty : FeatureScreenState
}
```

### Event Definition (One-time actions)

```kotlin
sealed interface FeatureEvent {
    data class NavigateToDetail(val id: Long) : FeatureEvent
    data object NavigateBack : FeatureEvent
    data class ShowSnackbar(val message: StringResource) : FeatureEvent
}
```

### Action Definition (User interactions)

```kotlin
sealed interface FeatureAction {
    data object Retry : FeatureAction
    data object OnRefresh : FeatureAction
    data class OnItemClick(val id: Long) : FeatureAction

    // Internal actions (from system, not user)
    sealed interface Internal : FeatureAction {
        data class ReceiveData(val dataState: DataState<Data>) : Internal
    }
}
```

---

## 2. Repository Pattern

### Interface

```kotlin
interface FeatureRepository {
    fun getData(): Flow<DataState<List<Data>>>
    fun getById(id: Long): Flow<DataState<Data>>
    suspend fun create(payload: CreatePayload): DataState<Unit>
    suspend fun update(id: Long, payload: UpdatePayload): DataState<Unit>
    suspend fun delete(id: Long): DataState<Unit>
}
```

### Implementation

```kotlin
class FeatureRepositoryImpl(
    private val service: FeatureService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FeatureRepository {

    override fun getData(): Flow<DataState<List<Data>>> = flow {
        emit(DataState.Loading)
        try {
            val result = service.fetchData().first()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(dispatcher)

    override suspend fun create(payload: CreatePayload): DataState<Unit> {
        return withContext(dispatcher) {
            try {
                service.create(payload)
                DataState.Success(Unit)
            } catch (e: Exception) {
                DataState.Error(e.message ?: "Failed to create")
            }
        }
    }
}
```

---

## 3. Service Pattern (Ktorfit)

```kotlin
interface FeatureService {

    @GET(ApiEndPoints.FEATURE)
    fun getData(): Flow<List<DataDto>>

    @GET(ApiEndPoints.FEATURE + "/{id}")
    fun getById(@Path("id") id: Long): Flow<DataDto>

    @GET(ApiEndPoints.FEATURE + "/{id}")
    fun getWithAssociations(
        @Path("id") id: Long,
        @Query("associations") associations: String?,
    ): Flow<DataWithAssociations>

    @POST(ApiEndPoints.FEATURE)
    suspend fun create(@Body payload: CreatePayload): HttpResponse

    @PUT(ApiEndPoints.FEATURE + "/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body payload: UpdatePayload,
    ): HttpResponse

    @DELETE(ApiEndPoints.FEATURE + "/{id}")
    suspend fun delete(@Path("id") id: Long): HttpResponse

    companion object {
        const val ID = "id"
    }
}
```

---

## 4. Screen Pattern

```kotlin
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is FeatureEvent.NavigateBack -> onNavigateBack()
                is FeatureEvent.NavigateToDetail -> onNavigateToDetail(event.id)
            }
        }
    }

    FeatureScreenContent(
        state = state,
        onAction = viewModel::sendAction,
    )
}

@Composable
private fun FeatureScreenContent(
    state: FeatureState,
    onAction: (FeatureAction) -> Unit,
) {
    Scaffold(
        topBar = {
            MifosTopAppBar(
                title = stringResource(Res.string.feature_title),
                onNavigationClick = { onAction(FeatureAction.OnNavigateBack) },
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state.uiState) {
                is FeatureScreenState.Loading -> LoadingContent()
                is FeatureScreenState.Success -> SuccessContent(
                    data = state.data,
                    onItemClick = { onAction(FeatureAction.OnItemClick(it)) }
                )
                is FeatureScreenState.Error -> ErrorContent(
                    message = state.uiState.message,
                    onRetry = { onAction(FeatureAction.Retry) }
                )
                is FeatureScreenState.Empty -> EmptyContent()
            }
        }
    }
}
```

---

## 5. Navigation Pattern

```kotlin
// Route definition
@Serializable
data object FeatureRoute

// Or with parameters
@Serializable
data class FeatureDetailRoute(val id: Long)

// Navigation extension
fun NavGraphBuilder.featureScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    composable<FeatureRoute> {
        FeatureScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

// NavController extension
fun NavController.navigateToFeature() {
    navigate(FeatureRoute)
}

fun NavController.navigateToFeatureDetail(id: Long) {
    navigate(FeatureDetailRoute(id))
}
```

---

## 6. DI Module Pattern

```kotlin
val featureModule = module {
    viewModelOf(::FeatureViewModel)
}
```

---

## 7. DataState Pattern

```kotlin
sealed interface DataState<out T> {
    data object Loading : DataState<Nothing>
    data class Success<T>(val data: T) : DataState<T>
    data class Error(val message: String) : DataState<Nothing>
}
```

---

## 8. Error Handling Pattern

```kotlin
// In ViewModel
private fun handleError(exception: Exception) {
    val errorMessage = when (exception) {
        is HttpException -> when (exception.response.status.value) {
            401 -> Res.string.error_unauthorized
            404 -> Res.string.error_not_found
            500 -> Res.string.error_server
            else -> Res.string.error_unknown
        }
        is IOException -> Res.string.error_network
        else -> Res.string.error_unknown
    }
    updateState { it.copy(uiState = FeatureScreenState.Error(errorMessage)) }
}
```

---

## 9. Pull-to-Refresh Pattern

```kotlin
@Composable
private fun FeatureContent(
    state: FeatureState,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn {
            items(state.data) { item ->
                ItemCard(item = item)
            }
        }
    }
}
```

---

## 10. Form Validation Pattern

```kotlin
// In ViewModel
private fun validateForm(): Boolean {
    val errors = mutableListOf<ValidationError>()

    if (state.amount <= 0) {
        errors.add(ValidationError.InvalidAmount)
    }
    if (state.accountId == null) {
        errors.add(ValidationError.AccountRequired)
    }

    if (errors.isNotEmpty()) {
        updateState { it.copy(validationErrors = errors) }
        return false
    }
    return true
}

sealed interface ValidationError {
    data object InvalidAmount : ValidationError
    data object AccountRequired : ValidationError
}
```

---

## Best Practices

1. **State Immutability**: Always use `@Immutable` on state classes
2. **Single Source of Truth**: State lives in ViewModel only
3. **Unidirectional Data Flow**: UI → Action → ViewModel → State → UI
4. **Separation of Concerns**: Keep layers independent
5. **Error Handling**: Always handle Loading, Success, Error states
6. **Resource Strings**: Use `StringResource` for all user-facing text
7. **Flow Collection**: Use `collectAsStateWithLifecycle()` in Compose
8. **Internal Visibility**: Use `internal` for feature-internal classes

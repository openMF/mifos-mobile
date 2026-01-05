# Dependency Injection Patterns

## Table of Contents
1. [Koin Module](#koin-module)
2. [ViewModel Registration](#viewmodel-registration)
3. [Multiple ViewModels](#multiple-viewmodels)
4. [Module Registration](#module-registration)
5. [Accessing Dependencies](#accessing-dependencies)
6. [Complete Example](#complete-example)

---

## Koin Module

Create a Koin module for each feature:

```kotlin
// feature/[name]/di/[Feature]Module.kt

package org.mifos.mobile.feature.[name].di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.[name].[Feature]ViewModel

val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

---

## ViewModel Registration

### Single ViewModel

```kotlin
val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

### ViewModel with SavedStateHandle

SavedStateHandle is automatically injected:

```kotlin
// ViewModel constructor
internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
    savedStateHandle: SavedStateHandle,  // Auto-injected
) : BaseViewModel<...>(...) {

    init {
        // Access navigation arguments
        val id = savedStateHandle.get<Long>("id")
    }
}

// Module - no special registration needed
val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

---

## Multiple ViewModels

Register all ViewModels in a feature module:

```kotlin
val AuthModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::OtpAuthenticationViewModel)
    viewModelOf(::RecoverPasswordViewModel)
    viewModelOf(::SetPasswordViewModel)
}
```

---

## Module Registration

### Step 1: Export Module

```kotlin
// feature/[name]/di/[Feature]Module.kt
val [Feature]Module = module {
    viewModelOf(::[Feature]ViewModel)
}
```

### Step 2: Register in KoinModules

```kotlin
// cmp-navigation/src/.../di/KoinModules.kt

val featureModules = module {
    includes(
        // Core modules
        AuthModule,
        HomeModule,
        AccountsModule,

        // Account modules
        SavingsAccountModule,
        LoanModule,
        ShareAccountModule,

        // Transaction modules
        BeneficiaryModule,
        TransferProcessModule,
        ThirdPartyTransferModule,

        // Utility modules
        NotificationModule,
        SettingsModule,
        QrModule,

        // Add your module here
        [Feature]Module,
    )
}
```

---

## Accessing Dependencies

### In ViewModel

Dependencies are constructor-injected:

```kotlin
internal class [Feature]ViewModel(
    private val repository: [Feature]Repository,
    private val userPreferences: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<[Feature]State, [Feature]Event, [Feature]Action>(
    initialState = [Feature]State()
) {
    // Use injected dependencies
    private fun loadData() {
        viewModelScope.launch {
            repository.getData().collect { ... }
        }
    }
}
```

### In Composable

Use `koinViewModel()`:

```kotlin
@Composable
internal fun [Feature]Screen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: [Feature]ViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    // ...
}
```

### With ViewModel Key

For multiple instances of same ViewModel:

```kotlin
@Composable
fun [Feature]Screen(
    accountId: Long,
    viewModel: [Feature]ViewModel = koinViewModel(
        key = "account_$accountId"
    ),
) {
    // Unique ViewModel instance per accountId
}
```

---

## Complete Example

### Module Definition

```kotlin
// feature/beneficiary/di/BeneficiaryModule.kt

package org.mifos.mobile.feature.beneficiary.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.beneficiary.list.BeneficiaryListViewModel
import org.mifos.mobile.feature.beneficiary.detail.BeneficiaryDetailViewModel
import org.mifos.mobile.feature.beneficiary.add.AddBeneficiaryViewModel
import org.mifos.mobile.feature.beneficiary.edit.EditBeneficiaryViewModel

val BeneficiaryModule = module {
    viewModelOf(::BeneficiaryListViewModel)
    viewModelOf(::BeneficiaryDetailViewModel)
    viewModelOf(::AddBeneficiaryViewModel)
    viewModelOf(::EditBeneficiaryViewModel)
}
```

### ViewModel with Dependencies

```kotlin
// feature/beneficiary/list/BeneficiaryListViewModel.kt

internal class BeneficiaryListViewModel(
    private val beneficiaryRepository: BeneficiaryRepository,
) : BaseViewModel<BeneficiaryListState, BeneficiaryListEvent, BeneficiaryListAction>(
    initialState = BeneficiaryListState()
) {
    init {
        loadBeneficiaries()
    }

    override fun handleAction(action: BeneficiaryListAction) {
        // ...
    }

    private fun loadBeneficiaries() {
        viewModelScope.launch {
            beneficiaryRepository.getBeneficiaries().collect { result ->
                // ...
            }
        }
    }
}
```

### Screen with ViewModel

```kotlin
// feature/beneficiary/list/BeneficiaryListScreen.kt

@Composable
internal fun BeneficiaryListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryListViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is BeneficiaryListEvent.NavigateBack -> onNavigateBack()
            is BeneficiaryListEvent.NavigateToDetail -> onNavigateToDetail(event.id)
        }
    }

    BeneficiaryListContent(
        state = state,
        onAction = remember(viewModel) { { viewModel.trySendAction(it) } },
        modifier = modifier,
    )
}
```

### Registration in KoinModules

```kotlin
// cmp-navigation/src/.../di/KoinModules.kt

val featureModules = module {
    includes(
        AuthModule,
        HomeModule,
        AccountsModule,
        BeneficiaryModule,  // Registered here
        // ...
    )
}
```

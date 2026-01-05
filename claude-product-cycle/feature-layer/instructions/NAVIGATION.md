# Navigation Patterns

## Table of Contents
1. [Route Definition](#route-definition)
2. [NavController Extensions](#navcontroller-extensions)
3. [NavGraphBuilder Extensions](#navgraphbuilder-extensions)
4. [Transitions](#transitions)
5. [Arguments](#arguments)
6. [Type-Safe Destinations](#type-safe-destinations)
7. [Navigation Graph](#navigation-graph)
8. [Complete Example](#complete-example)

---

## Route Definition

Use `@Serializable` data objects for type-safe navigation:

### Simple Route

```kotlin
@Serializable
data object [Feature]Route
```

### Route with Arguments

```kotlin
@Serializable
data class [Feature]DetailRoute(
    val id: Long,
)

@Serializable
data class [Feature]EditRoute(
    val id: Long,
    val title: String? = null,  // Optional argument
)
```

---

## NavController Extensions

Create extension functions for navigation:

### Simple Navigation

```kotlin
fun NavController.navigateTo[Feature]Screen(navOptions: NavOptions? = null) {
    navigate([Feature]Route, navOptions)
}
```

### Navigation with Arguments

```kotlin
fun NavController.navigateTo[Feature]Detail(id: Long) {
    navigate([Feature]DetailRoute(id))
}

fun NavController.navigateTo[Feature]Edit(id: Long, title: String? = null) {
    navigate([Feature]EditRoute(id, title))
}
```

### Navigation with Pop Behavior

```kotlin
fun NavController.navigateTo[Feature]Screen() {
    navigate([Feature]Route) {
        popUpTo([Previous]Route) {
            inclusive = true
        }
    }
}
```

---

## NavGraphBuilder Extensions

### Simple Screen

```kotlin
fun NavGraphBuilder.[feature]Screen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    composableWithStayTransitions<[Feature]Route> {
        [Feature]Screen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}
```

### Screen with Arguments

```kotlin
fun NavGraphBuilder.[feature]DetailScreen(
    onNavigateBack: () -> Unit,
) {
    composableWithStayTransitions<[Feature]DetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<[Feature]DetailRoute>()
        [Feature]DetailScreen(
            id = route.id,
            onNavigateBack = onNavigateBack,
        )
    }
}
```

### Screen with Optional Arguments

```kotlin
fun NavGraphBuilder.[feature]EditScreen(
    onNavigateBack: () -> Unit,
    onSaveComplete: () -> Unit,
) {
    composableWithStayTransitions<[Feature]EditRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<[Feature]EditRoute>()
        [Feature]EditScreen(
            id = route.id,
            title = route.title,
            onNavigateBack = onNavigateBack,
            onSaveComplete = onSaveComplete,
        )
    }
}
```

---

## Transitions

### Standard Transitions

```kotlin
// Default stay transitions (recommended)
composableWithStayTransitions<[Feature]Route> {
    [Feature]Screen(...)
}
```

### Custom Transitions

```kotlin
composable<[Feature]Route>(
    enterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        )
    },
    exitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(300)
        )
    },
    popEnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(300)
        )
    },
    popExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        )
    },
) {
    [Feature]Screen(...)
}
```

### Fade Transitions

```kotlin
composable<[Feature]Route>(
    enterTransition = { fadeIn(animationSpec = tween(300)) },
    exitTransition = { fadeOut(animationSpec = tween(300)) },
) {
    [Feature]Screen(...)
}
```

---

## Arguments

### Primitive Types

```kotlin
@Serializable
data class [Feature]Route(
    val id: Long,
    val name: String,
    val isEnabled: Boolean = true,
    val count: Int = 0,
)
```

### Nullable Arguments

```kotlin
@Serializable
data class [Feature]Route(
    val id: Long,
    val title: String? = null,  // Optional
)
```

### Enum Arguments

```kotlin
@Serializable
enum class AccountType {
    SAVINGS, LOAN, SHARE
}

@Serializable
data class AccountRoute(
    val accountType: AccountType,
)
```

### Accessing Arguments

```kotlin
composableWithStayTransitions<[Feature]DetailRoute> { backStackEntry ->
    val route = backStackEntry.toRoute<[Feature]DetailRoute>()

    [Feature]DetailScreen(
        id = route.id,
        title = route.title ?: "Default Title",
    )
}
```

---

## Type-Safe Destinations

### Sealed Class Pattern

```kotlin
sealed class [Feature]Destination {
    data object List : [Feature]Destination()
    data class Detail(val id: Long) : [Feature]Destination()
    data class Edit(val id: Long) : [Feature]Destination()
    data object Create : [Feature]Destination()
}

typealias [Feature]Navigator = ([Feature]Destination) -> Unit
```

### Usage in Screen

```kotlin
@Composable
internal fun [Feature]Screen(
    onNavigate: [Feature]Navigator,
    onNavigateBack: () -> Unit,
) {
    // Navigate to detail
    onNavigate([Feature]Destination.Detail(itemId))

    // Navigate to create
    onNavigate([Feature]Destination.Create)
}
```

### Usage in NavGraph

```kotlin
fun NavGraphBuilder.[feature]Screen(
    navController: NavController,
) {
    composableWithStayTransitions<[Feature]Route> {
        [Feature]Screen(
            onNavigate = { destination ->
                when (destination) {
                    is [Feature]Destination.Detail -> {
                        navController.navigateTo[Feature]Detail(destination.id)
                    }
                    is [Feature]Destination.Create -> {
                        navController.navigateTo[Feature]Create()
                    }
                    // ...
                }
            },
            onNavigateBack = { navController.popBackStack() },
        )
    }
}
```

---

## Navigation Graph

### Feature Navigation Graph

```kotlin
// feature/[name]/navigation/[Feature]Navigation.kt

@Serializable
data object [Feature]GraphRoute

fun NavGraphBuilder.[feature]NavGraph(
    navController: NavController,
) {
    navigation<[Feature]GraphRoute>(
        startDestination = [Feature]ListRoute,
    ) {
        [feature]ListScreen(
            onNavigateToDetail = { id ->
                navController.navigateTo[Feature]Detail(id)
            },
        )

        [feature]DetailScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToEdit = { id ->
                navController.navigateTo[Feature]Edit(id)
            },
        )

        [feature]EditScreen(
            onNavigateBack = { navController.popBackStack() },
        )
    }
}
```

### Registering in Main Graph

```kotlin
// cmp-navigation/.../MainNavGraph.kt

NavHost(
    navController = navController,
    startDestination = HomeRoute,
) {
    homeScreen(...)

    [feature]NavGraph(navController)

    // OR individual screens
    [feature]Screen(...)
    [feature]DetailScreen(...)
}
```

---

## Complete Example

```kotlin
// feature/beneficiary/navigation/BeneficiaryNavigation.kt

package org.mifos.mobile.feature.beneficiary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.beneficiary.list.BeneficiaryListScreen
import org.mifos.mobile.feature.beneficiary.detail.BeneficiaryDetailScreen
import org.mifos.mobile.feature.beneficiary.add.AddBeneficiaryScreen

// Routes
@Serializable
data object BeneficiaryListRoute

@Serializable
data class BeneficiaryDetailRoute(val id: Long)

@Serializable
data object AddBeneficiaryRoute

// NavController Extensions
fun NavController.navigateToBeneficiaryList(navOptions: NavOptions? = null) {
    navigate(BeneficiaryListRoute, navOptions)
}

fun NavController.navigateToBeneficiaryDetail(id: Long) {
    navigate(BeneficiaryDetailRoute(id))
}

fun NavController.navigateToAddBeneficiary() {
    navigate(AddBeneficiaryRoute)
}

// NavGraphBuilder Extensions
fun NavGraphBuilder.beneficiaryListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
) {
    composableWithStayTransitions<BeneficiaryListRoute> {
        BeneficiaryListScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToAdd = onNavigateToAdd,
        )
    }
}

fun NavGraphBuilder.beneficiaryDetailScreen(
    onNavigateBack: () -> Unit,
) {
    composableWithStayTransitions<BeneficiaryDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<BeneficiaryDetailRoute>()
        BeneficiaryDetailScreen(
            beneficiaryId = route.id,
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavGraphBuilder.addBeneficiaryScreen(
    onNavigateBack: () -> Unit,
    onBeneficiaryAdded: () -> Unit,
) {
    composableWithStayTransitions<AddBeneficiaryRoute> {
        AddBeneficiaryScreen(
            onNavigateBack = onNavigateBack,
            onBeneficiaryAdded = onBeneficiaryAdded,
        )
    }
}
```

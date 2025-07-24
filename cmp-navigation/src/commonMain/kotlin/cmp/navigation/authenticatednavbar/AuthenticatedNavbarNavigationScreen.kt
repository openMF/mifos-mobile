/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.authenticatednavbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import cmp.navigation.ui.MifosScaffold
import cmp.navigation.ui.ScaffoldNavigationData
import cmp.navigation.ui.rememberMifosNavController
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.ui.RootTransitionProviders
import org.mifos.mobile.core.ui.navigation.NavigationItem
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.home.navigation.HomeRoute
import org.mifos.mobile.feature.home.navigation.homeDestination
import org.mifos.mobile.feature.home.navigation.navigateToHomeScreen
import org.mifos.mobile.feature.user.profile.navigation.navigateToUserProfileGraph
import org.mifos.mobile.feature.user.profile.navigation.userprofileNavGraph
import org.mifos.mobile.navigation.generated.resources.Res
import org.mifos.mobile.navigation.generated.resources.not_connected

@Composable
internal fun AuthenticatedNavbarNavigationScreen(
    navigateToNotificationScreen: () -> Unit,
    navigateToAccountsScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberMifosNavController(
        name = "AuthenticatedNavbarScreen",
    ),
    viewModel: AuthenticatedNavbarNavigationViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()

    EventsEffect(eventFlow = viewModel.eventFlow) { event ->
        navController.apply {
            when (event) {
                // TODO Add navigation to respective screens
                AuthenticatedNavBarEvent.NavigateToHomeScreen -> {
                    navigateToTabOrRoot(tabToNavigateTo = event.tab) {
                        navigateToHomeScreen(navOptions = it)
                    }
                }

                AuthenticatedNavBarEvent.NavigateToUserProfileScreen -> {
                    navigateToTabOrRoot(tabToNavigateTo = event.tab) {
                        navigateToUserProfileGraph(navOptions = it)
                    }
                }
            }
        }
    }

    val message = stringResource(Res.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = Indefinite,
                )
            }
        }
    }

    AuthenticatedNavbarNavigationScreenContent(
        navController = navController,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        navigateToNotificationScreen = navigateToNotificationScreen,
        navigateToAccountsScreen = { navigateToAccountsScreen(it) },
    )
}

@Composable
internal fun AuthenticatedNavbarNavigationScreenContent(
    navController: NavHostController,
    navigateToNotificationScreen: () -> Unit,
    navigateToAccountsScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (AuthenticatedNavBarAction) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navigationItems = persistentListOf<NavigationItem>(
        AuthenticatedNavBarTabItem.HomeTab,
        AuthenticatedNavBarTabItem.ProfileTab,
    )

    MifosScaffold(
        contentWindowInsets = WindowInsets(0.dp),
        navigationData = ScaffoldNavigationData(
            navigationItems = navigationItems,
            selectedNavigationItem = navigationItems.find {
                navBackStackEntry.isCurrentRoute(route = it.graphRoute)
            },
            onNavigationClick = { navigationItem ->
                // TODO navigate to respective screens
                when (navigationItem) {
                    is AuthenticatedNavBarTabItem.HomeTab -> {
                        onAction(AuthenticatedNavBarAction.HomeTabClick)
                    }

                    is AuthenticatedNavBarTabItem.ProfileTab -> {
                        onAction(AuthenticatedNavBarAction.ProfileTabClick)
                    }
                }
            },
            shouldShowNavigation = navigationItems.any {
                navBackStackEntry.isCurrentRoute(route = it.startDestinationRoute)
            },
        ),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier,
    ) {
        // Because this Scaffold has a bottom navigation bar, the NavHost will:
        // - consume the vertical navigation bar insets.
        // - consume the IME insets.
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            enterTransition = RootTransitionProviders.Enter.fadeIn,
            exitTransition = RootTransitionProviders.Exit.fadeOut,
            popEnterTransition = RootTransitionProviders.Enter.fadeIn,
            popExitTransition = RootTransitionProviders.Exit.fadeOut,
        ) {
            // TODO Add top level destination screens

            homeDestination(
//                navigateToDestinationScreen = {
//                    navController.navigate(
//                        it
//                    )
//                },
                navigateToAccountsScreen = { navigateToAccountsScreen(it) },
                navigateToNotificationScreen = navigateToNotificationScreen,
            )

            userprofileNavGraph(navController, {})
        }
    }
}

private fun NavController.navigateToTabOrRoot(
    tabToNavigateTo: AuthenticatedNavBarTabItem,
    navigate: (NavOptions) -> Unit,
) {
    if (tabToNavigateTo.startDestinationRoute == currentDestination?.route) {
        // We are at the start destination already, so nothing to do.
        return
    } else if (currentDestination?.parent?.route == tabToNavigateTo.graphRoute) {
        // We are not at the start destination but we are in the correct graph,
        // so lets pop up to the start destination.
        popBackStack(route = tabToNavigateTo.startDestinationRoute, inclusive = false)
    } else {
        // We are not in correct graph at all, so navigate there.
        navigate(
            navOptions {
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            },
        )
    }
}

private fun NavBackStackEntry?.isCurrentRoute(route: String): Boolean =
    this
        ?.destination
        ?.hierarchy
        ?.any { it.route == route } == true

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.rootnav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import cmp.navigation.authenticated.AuthenticatedGraphRoute
import cmp.navigation.authenticated.authenticatedGraph
import cmp.navigation.authenticated.navigateToAuthenticatedGraph
import cmp.navigation.navigation.passcodeNavGraph
import cmp.navigation.splash.SplashRoute
import cmp.navigation.splash.navigateToSplash
import cmp.navigation.splash.splashDestination
import cmp.navigation.ui.rememberMifosNavController
import cmp.navigation.utils.toObjectNavigationRoute
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.library.passcode.navigateToPasscodeScreen
import org.mifos.mobile.core.ui.NonNullEnterTransitionProvider
import org.mifos.mobile.core.ui.NonNullExitTransitionProvider
import org.mifos.mobile.core.ui.RootTransitionProviders
import org.mifos.mobile.feature.auth.navigation.AuthGraphRoute
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph
import org.mifos.mobile.feature.auth.navigation.navigateToAuthGraph
import org.mifos.mobile.feature.onboarding.language.navigation.OnboardingLanguageRoute
import org.mifos.mobile.feature.onboarding.language.navigation.navigateToOnboardingLanguage
import org.mifos.mobile.feature.onboarding.language.navigation.onBoardingLanguageDestination

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun RootNavScreen(
    modifier: Modifier = Modifier,
    viewModel: RootNavViewModel = koinViewModel(),
    navController: NavHostController = rememberMifosNavController(name = "RootNavScreen"),
    onSplashScreenRemoved: () -> Unit = {},
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val previousStateReference = remember { mutableStateOf(state) }

    val isNotSplashScreen = state != RootNavState.Splash
    LaunchedEffect(isNotSplashScreen) {
        if (isNotSplashScreen) onSplashScreenRemoved()
    }

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
        enterTransition = { toEnterTransition()(this) },
        exitTransition = { toExitTransition()(this) },
        popEnterTransition = { toEnterTransition()(this) },
        popExitTransition = { toExitTransition()(this) },
    ) {
        splashDestination()
        onBoardingLanguageDestination()
        authenticationNavGraph(navController, navController::navigateToPasscodeScreen)
        authenticatedGraph(navController)
//        userUnlockDestination()
        passcodeNavGraph(navController)
    }

    val targetRoute = when (state) {
        RootNavState.SetLanguage -> OnboardingLanguageRoute
        RootNavState.Auth -> AuthGraphRoute
        RootNavState.Splash -> SplashRoute
//        RootNavState.UserLocked -> UserUnlockRoute.Standard
        RootNavState.UserLocked -> { }
        is RootNavState.UserUnlocked -> AuthenticatedGraphRoute
    }
    val currentRoute = navController.currentDestination?.rootLevelRoute()

    // Don't navigate if we are already at the correct root. This notably happens during process
    // death. In this case, the NavHost already restores state, so we don't have to navigate.
    // However, if the route is correct but the underlying state is different, we should still
    // proceed in order to get a fresh version of that route.
    if (currentRoute == targetRoute.toObjectNavigationRoute() &&
        previousStateReference.value == state
    ) {
        previousStateReference.value == state
        return
    }
    previousStateReference.value = state

    // When state changes, navigate to different root navigation state
    val rootNavOptions = navOptions {
        // When changing root navigation state, pop everything else off the back stack:
        popUpTo(navController.graph.id) {
            inclusive = false
            saveState = false
        }
        launchSingleTop = true
        restoreState = false
    }

    // Use a LaunchedEffect to ensure we don't navigate too soon when the app first opens. This
    // avoids a bug that first appeared in Compose Material3 1.2.0-rc01 that causes the initial
    // transition to appear corrupted.
    LaunchedEffect(state) {
        when (state) {
            RootNavState.Splash -> navController.navigateToSplash(rootNavOptions)
            RootNavState.Auth -> navController.navigateToAuthGraph(rootNavOptions)
            RootNavState.SetLanguage -> navController.navigateToOnboardingLanguage(rootNavOptions)
//            RootNavState.UserLocked -> navController.navigateToUserUnlock(rootNavOptions)
            RootNavState.UserLocked -> { }
            is RootNavState.UserUnlocked -> navController.navigateToAuthenticatedGraph(
                navOptions = rootNavOptions,
            )
        }
    }
}

private fun NavDestination?.rootLevelRoute(): String? = when {
    this == null -> null
    parent?.route == null -> route
    else -> parent.rootLevelRoute()
}

@Suppress("MaxLineLength")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.toEnterTransition(): NonNullEnterTransitionProvider =
    when (targetState.destination.rootLevelRoute()) {
        SplashRoute.toObjectNavigationRoute() -> RootTransitionProviders.Enter.none
        else -> RootTransitionProviders.Enter.fadeIn
    }

@Suppress("MaxLineLength")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.toExitTransition(): NonNullExitTransitionProvider {
    return when (initialState.destination.rootLevelRoute()) {
        // Disable transitions when coming from the splash screen
        SplashRoute.toObjectNavigationRoute() -> RootTransitionProviders.Exit.none
        else -> RootTransitionProviders.Exit.fadeOut
    }
}

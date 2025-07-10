
@file:Suppress("MatchingDeclarationName")

package cmp.navigation.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavGraphBuilder.splashDestination() {
    composable<SplashRoute> { SplashScreen() }
}

fun NavController.navigateToSplash(
    navOptions: NavOptions? = null,
) {
    navigate(SplashRoute, navOptions)
}

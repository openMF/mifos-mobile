package org.mifos.mobile.feature.location.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.location.LocationsScreen

fun NavController.navigateToLocationsScreen() {
    navigate(LocationsNavigation.LocationsScreen.route)
}

fun NavGraphBuilder.locationsNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = LocationsNavigation.LocationsScreen.route,
        route = LocationsNavigation.LocationsBase.route,
    ) {
        locationsScreenRoute()
    }
}

fun NavGraphBuilder.locationsScreenRoute() {
    composable(
        route = LocationsNavigation.LocationsScreen.route,
    ) {
        LocationsScreen()
    }
}
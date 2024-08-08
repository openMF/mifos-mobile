package org.mifos.mobile.feature.location.navigation

// Constants for Routes
const val LOCATIONS_NAVIGATION_ROUTE_BASE = "locations_base_route"
const val LOCATIONS_SCREEN_ROUTE = "locations_screen_route"

// Sealed class for Navigation Routes
sealed class LocationsNavigation(val route: String) {
    data object LocationsBase : LocationsNavigation(route = LOCATIONS_NAVIGATION_ROUTE_BASE)
    data object LocationsScreen : LocationsNavigation(route = LOCATIONS_SCREEN_ROUTE)
}
package org.mifos.mobile.feature.help.navigation

// Constants for Routes
const val HELP_NAVIGATION_ROUTE_BASE = "help_base_route"
const val HELP_SCREEN_ROUTE = "help_screen_route"

// Sealed class for Navigation Routes
sealed class HelpNavigation(val route: String) {
    data object HelpBase : HelpNavigation(route = HELP_NAVIGATION_ROUTE_BASE)
    data object HelpScreen : HelpNavigation(route = HELP_SCREEN_ROUTE)
}

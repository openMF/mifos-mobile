package org.mifos.mobile.feature.update_password.navigation

const val UPDATE_PASSWORD_NAVIGATION_ROUTE_BASE = "update_password_base_route"
const val UPDATE_PASSWORD_SCREEN_ROUTE = "update_password_screen_route"

sealed class UpdatePasswordNavigation(val route: String) {
    data object UpdatePasswordBase : UpdatePasswordNavigation(route = UPDATE_PASSWORD_NAVIGATION_ROUTE_BASE)
    data object UpdatePasswordScreen : UpdatePasswordNavigation(route = UPDATE_PASSWORD_SCREEN_ROUTE)
}


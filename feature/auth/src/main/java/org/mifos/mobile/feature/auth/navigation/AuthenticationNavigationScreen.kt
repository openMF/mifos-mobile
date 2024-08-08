package org.mifos.mobile.feature.auth.navigation

const val AUTH_NAVIGATION_ROUTE = "auth_route"
const val LOGIN_SCREEN_ROUTE = "login_screen"
const val REGISTRATION_SCREEN_ROUTE = "registration_screen"
const val REGISTRATION_VERIFICATION_SCREEN_ROUTE = "registration_verification_screen"

sealed class AuthenticationNavigation(val route: String) {
    data object AuthenticationBase: AuthenticationNavigation(route = AUTH_NAVIGATION_ROUTE)
    data object Login: AuthenticationNavigation(route = LOGIN_SCREEN_ROUTE)
    data object Registration : AuthenticationNavigation(route = REGISTRATION_SCREEN_ROUTE)
    data object RegistrationVerification : AuthenticationNavigation(route = REGISTRATION_VERIFICATION_SCREEN_ROUTE)
}
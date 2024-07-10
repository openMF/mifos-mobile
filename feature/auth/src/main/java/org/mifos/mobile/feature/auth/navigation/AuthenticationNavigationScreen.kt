package org.mifos.mobile.feature.auth.navigation

import org.mifos.mobile.feature.auth.navigation.AuthenticationRoute.LOGIN_SCREEN_ROUTE
import org.mifos.mobile.feature.auth.navigation.AuthenticationRoute.REGISTRATION_SCREEN_ROUTE
import org.mifos.mobile.feature.auth.navigation.AuthenticationRoute.REGISTRATION_VERIFICATION_SCREEN_ROUTE

sealed class AuthenticationNavigation(val route: String) {
    data object Login: AuthenticationNavigation(route = LOGIN_SCREEN_ROUTE)
    data object Registration : AuthenticationNavigation(route = REGISTRATION_SCREEN_ROUTE)
    data object RegistrationVerification : AuthenticationNavigation(route = REGISTRATION_VERIFICATION_SCREEN_ROUTE)
}
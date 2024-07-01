package org.mifos.mobile.feature.registration.navigation

import org.mifos.mobile.core.common.Constants.REGISTRATION_SCREEN_ROUTE_BASE
import org.mifos.mobile.core.common.Constants.REGISTRATION_VERIFICATION_SCREEN_ROUTE_BASE

sealed class RegistrationScreen(val route: String) {

    data object Registration : RegistrationScreen(
        route = REGISTRATION_SCREEN_ROUTE_BASE
    )

    data object RegistrationVerification : RegistrationScreen(
        route = REGISTRATION_VERIFICATION_SCREEN_ROUTE_BASE
    )
}
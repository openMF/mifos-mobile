/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.navigation

const val LOGIN_SCREEN_ROUTE = "login_screen"
const val REGISTRATION_SCREEN_ROUTE = "registration_screen"
const val REGISTRATION_VERIFICATION_SCREEN_ROUTE = "registration_verification_screen"

sealed class AuthenticationNavigation(val route: String) {
    data object Login : AuthenticationNavigation(route = LOGIN_SCREEN_ROUTE)
    data object Registration : AuthenticationNavigation(route = REGISTRATION_SCREEN_ROUTE)
    data object RegistrationVerification :
        AuthenticationNavigation(route = REGISTRATION_VERIFICATION_SCREEN_ROUTE)
}

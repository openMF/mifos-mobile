/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.update.password.navigation

const val UPDATE_PASSWORD_NAVIGATION_ROUTE_BASE = "update_password_base_route"
const val UPDATE_PASSWORD_SCREEN_ROUTE = "update_password_screen_route"

internal sealed class UpdatePasswordNavigation(val route: String) {
    data object UpdatePasswordBase : UpdatePasswordNavigation(
        route = UPDATE_PASSWORD_NAVIGATION_ROUTE_BASE,
    )

    data object UpdatePasswordScreen : UpdatePasswordNavigation(
        route = UPDATE_PASSWORD_SCREEN_ROUTE,
    )
}

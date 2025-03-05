/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.help.navigation

// Constants for Routes
const val HELP_NAVIGATION_ROUTE_BASE = "help_base_route"
const val HELP_SCREEN_ROUTE = "help_screen_route"

// Sealed class for Navigation Routes
sealed class HelpNavigation(val route: String) {
    data object HelpBase : HelpNavigation(route = HELP_NAVIGATION_ROUTE_BASE)
    data object HelpScreen : HelpNavigation(route = HELP_SCREEN_ROUTE)
}

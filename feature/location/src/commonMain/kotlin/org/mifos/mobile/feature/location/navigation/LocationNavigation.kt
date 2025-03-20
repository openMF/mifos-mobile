/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.location.navigation

// Constants for Routes
const val LOCATIONS_NAVIGATION_ROUTE_BASE = "locations_base_route"
const val LOCATIONS_SCREEN_ROUTE = "locations_screen_route"

// Sealed class for Navigation Routes
sealed class LocationsNavigation(val route: String) {
    data object LocationsBase : LocationsNavigation(route = LOCATIONS_NAVIGATION_ROUTE_BASE)
    data object LocationsScreen : LocationsNavigation(route = LOCATIONS_SCREEN_ROUTE)
}

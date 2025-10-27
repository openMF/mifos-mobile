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
/**
 * Base route for the locations feature navigation graph.
 */
const val LOCATIONS_NAVIGATION_ROUTE_BASE = "locations_base_route"

/**
 * Route for the locations screen.
 */
const val LOCATIONS_SCREEN_ROUTE = "locations_screen_route"

/**
 * Sealed class representing the navigation routes for the locations feature.
 *
 * @property route The route string for the navigation destination.
 */
sealed class LocationsNavigation(val route: String) {
    /**
     * Represents the base navigation route for the locations feature.
     */
    data object LocationsBase : LocationsNavigation(route = LOCATIONS_NAVIGATION_ROUTE_BASE)

    /**
     * Represents the screen for displaying locations.
     */
    data object LocationsScreen : LocationsNavigation(route = LOCATIONS_SCREEN_ROUTE)
}

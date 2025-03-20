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

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.location.LocationsScreen

fun NavController.navigateToLocationsScreen() {
    navigate(LocationsNavigation.LocationsScreen.route)
}

fun NavGraphBuilder.locationsNavGraph() {
    navigation(
        startDestination = LocationsNavigation.LocationsScreen.route,
        route = LocationsNavigation.LocationsBase.route,
    ) {
        locationsScreenRoute()
    }
}

fun NavGraphBuilder.locationsScreenRoute() {
    composable(
        route = LocationsNavigation.LocationsScreen.route,
    ) {
        LocationsScreen()
    }
}

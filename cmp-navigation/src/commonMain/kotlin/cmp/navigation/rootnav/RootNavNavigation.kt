/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.rootnav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * The route for the root navigation screen.
 */
const val ROOT_ROUTE: String = "root"

/**
 * Add the root navigation screen to the nav graph.
 */
fun NavGraphBuilder.rootNavDestination(
    onSplashScreenRemoved: () -> Unit,
) {
    composable(route = ROOT_ROUTE) {
        RootNavScreen(onSplashScreenRemoved = onSplashScreenRemoved)
    }
}

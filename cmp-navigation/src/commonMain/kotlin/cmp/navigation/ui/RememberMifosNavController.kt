/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger

@Composable
fun rememberMifosNavController(
    name: String,
    vararg navigators: Navigator<out NavDestination>,
): NavHostController =
    rememberNavController(navigators = navigators).apply {
        this.addOnDestinationChangedListener { _, destination, _ ->
            val graph = destination.parent?.route?.let { " in $it" }.orEmpty()
            Logger.d("$name destination changed: ${destination.route}$graph")
        }
    }

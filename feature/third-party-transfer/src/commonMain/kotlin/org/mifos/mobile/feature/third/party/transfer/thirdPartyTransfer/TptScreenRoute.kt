/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer.thirdPartyTransfer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.third.party.transfer.navigation.TptNavigator

@Serializable
data object TptScreenRoute

fun NavController.navigateToTptScreen(navOptions: NavOptions? = null) {
    this.navigate(TptScreenRoute, navOptions)
}

fun NavGraphBuilder.tptScreenDestination(
    onNavigate: TptNavigator,
) {
    composableWithSlideTransitions<TptScreenRoute> {
        TptScreen(
            onNavigate = onNavigate,
        )
    }
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.charge.screens.ChargeDetailScreen

@Serializable
data object ChargesDetailsRoute

fun NavGraphBuilder.chargesDetailsDestination(
    navigateToQrScreen: () -> Unit,
) {
    composableWithStayTransitions<ChargesDetailsRoute> {
        ChargeDetailScreen()
    }
}

fun NavController.navigateToChargesDetailsScreen() {
    this.navigate(ChargesDetailsRoute)
}

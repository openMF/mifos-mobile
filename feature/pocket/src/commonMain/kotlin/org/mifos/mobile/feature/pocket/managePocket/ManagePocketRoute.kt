/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.managePocket

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data object ManagePocketRoute

/** Navigates to the Pocket account-management screen. */
fun NavController.navigateToManagePocketScreen() {
    navigate(ManagePocketRoute)
}

/** Adds the Pocket account-management destination to the navigation graph. */
fun NavGraphBuilder.managePocketDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<ManagePocketRoute> {
        ManagePocketScreen(
            navigateBack = navigateBack,
        )
    }
}

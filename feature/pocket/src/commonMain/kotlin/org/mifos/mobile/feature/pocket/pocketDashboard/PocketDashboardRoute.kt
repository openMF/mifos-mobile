/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.pocketDashboard

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data object PocketDashboardRoute

fun NavController.navigateToPocketDashboardScreen() {
    navigate(PocketDashboardRoute)
}

fun NavGraphBuilder.pocketDashboardDestination(
    navigateBack: () -> Unit,
    navigateToManagePocket: () -> Unit,
    navigateToLoanAccountDetail: (Long) -> Unit,
    navigateToShareAccountDetail: (Long) -> Unit,
    navigateToSavingsAccountDetail: (Long) -> Unit,
) {
    composableWithSlideTransitions<PocketDashboardRoute> {
        PocketDashboardScreen(
            navigateBack = navigateBack,
            navigateToManagePocket = navigateToManagePocket,
            navigateToLoanAccountDetail = navigateToLoanAccountDetail,
            navigateToShareAccountDetail = navigateToShareAccountDetail,
            navigateToSavingsAccountDetail = navigateToSavingsAccountDetail,
        )
    }
}

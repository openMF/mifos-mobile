/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.pocket.pocketDashboard.PocketDashboardRoute
import org.mifos.mobile.feature.pocket.pocketDashboard.pocketDashboardDestination

@Serializable
data object PocketGraphRoute

fun NavController.navigateToPocketGraph() {
    navigate(PocketGraphRoute)
}

fun NavGraphBuilder.pocketNavGraph(
    navigateBack: () -> Unit,
    navigateToManagePocket: () -> Unit,
    navigateToLoanAccountDetail: (Long) -> Unit,
    navigateToShareAccountDetail: (Long) -> Unit,
    navigateToSavingsAccountDetail: (Long) -> Unit,
) {
    navigation<PocketGraphRoute>(
        startDestination = PocketDashboardRoute,
    ) {
        pocketDashboardDestination(
            navigateBack = navigateBack,
            navigateToManagePocket = navigateToManagePocket,
            navigateToLoanAccountDetail = navigateToLoanAccountDetail,
            navigateToShareAccountDetail = navigateToShareAccountDetail,
            navigateToSavingsAccountDetail = navigateToSavingsAccountDetail,
        )
    }
}

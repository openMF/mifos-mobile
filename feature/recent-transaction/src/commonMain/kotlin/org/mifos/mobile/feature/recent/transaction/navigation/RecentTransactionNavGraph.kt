/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.recent.transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.recent.transaction.screen.RecentTransactionScreen

@Serializable
data object RecentTransactionRoute

fun NavController.navigateToRecentTransactionScreen() {
    this.navigate(RecentTransactionRoute)
}

fun NavGraphBuilder.recentTransactionDestination(
    navigateBack: () -> Unit,
    navigateToDetails: (String, String, Long) -> Unit,
) {
    composableWithSlideTransitions<RecentTransactionRoute> {
        RecentTransactionScreen(
            navigateBack = navigateBack,
            navigateToDetails = navigateToDetails,
        )
    }
}

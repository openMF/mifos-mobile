/*
 * Copyright 2026 Mifos Initiative
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
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.recent.transaction.screen.RecentTransactionScreen

/**
 * Navigation configuration for the recent transactions feature.
 *
 * Defines the type-safe [RecentTransactionRoute] and provides extension functions
 * to handle navigation and graph registration.
 */

@Serializable
data object RecentTransactionRoute

/**
 * Navigates to the [RecentTransactionRoute].
 */
fun NavController.navigateToRecentTransactionScreen() {
    this.navigate(RecentTransactionRoute)
}

/**
 * Adds the recent transaction destination to the [NavGraphBuilder].
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToDetails Callback to navigate to transaction details, providing
 * account ID, type, and transaction ID.
 */
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

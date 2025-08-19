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

package org.mifos.mobile.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.home.HomeScreen

@Serializable
data object HomeRoute

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) =
    navigate(HomeRoute, navOptions)

sealed class HomeNavigationDestination {
    // Add more as needed
    object Notification : HomeNavigationDestination()
    object Charge : HomeNavigationDestination()
    object Faq : HomeNavigationDestination()
    object Beneficiary : HomeNavigationDestination()
    object Transaction : HomeNavigationDestination()
    object ApplyLoan : HomeNavigationDestination()
    object ApplySavings : HomeNavigationDestination()
    object ApplyShare : HomeNavigationDestination()
    data class AccountsWithType(val type: String) : HomeNavigationDestination()
}

typealias HomeNavigator = (HomeNavigationDestination) -> Unit

fun NavGraphBuilder.homeDestination(
    onNavigate: HomeNavigator,
) {
    composableWithStayTransitions<HomeRoute> {
        HomeScreen(
            onNavigate = onNavigate,
        )
    }
}

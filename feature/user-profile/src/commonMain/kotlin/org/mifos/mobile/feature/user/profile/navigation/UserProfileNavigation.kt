/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.user.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.user.profile.screens.UserProfileScreen

const val USER_PROFILE_NAVIGATION_ROUTE_BASE = "user_profile_base_route"
const val USER_PROFILE_SCREEN_ROUTE = "user_profile_screen_route"

internal sealed class UserProfileNavigation(val route: String) {
    data object UserProfileBase : UserProfileNavigation(route = USER_PROFILE_NAVIGATION_ROUTE_BASE)
    data object UserProfileScreen : UserProfileNavigation(route = USER_PROFILE_SCREEN_ROUTE)
}

// TODO move this user profile route after designing profile screen

@Serializable
data object ProfileRoute

fun NavController.navigateToUserProfileScreen() {
    navigate(UserProfileNavigation.UserProfileBase.route)
}

private fun NavGraphBuilder.userProfileDestination(
    navigateBack: () -> Unit,
    navigateToChangePassword: () -> Unit,
) {
    composableWithStayTransitions<ProfileRoute> {
        UserProfileScreen(
            navigateBack = navigateBack,
            changePassword = navigateToChangePassword,
        )
    }
}

@Serializable
data object ProfileGraphRoute

fun NavController.navigateToUserProfileGraph(navOptions: NavOptions? = null) {
    this.navigate(ProfileGraphRoute, navOptions)
}

fun NavGraphBuilder.userprofileNavGraph(
    navController: NavController,
    navigateToChangePassword: () -> Unit,
) {
    navigation<ProfileGraphRoute>(
        startDestination = ProfileRoute,
    ) {
        userProfileDestination(
            navigateBack = { navController.popBackStack() },
            navigateToChangePassword = navigateToChangePassword,
        )
    }
}

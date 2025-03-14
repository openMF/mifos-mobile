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
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.user.profile.screens.UserProfileScreen

fun NavController.navigateToUserProfile() {
    navigate(UserProfileNavigation.UserProfileBase.route)
}

fun NavGraphBuilder.userProfileNavGraph(
    navigateBack: () -> Unit,
    navigateToChangePassword: () -> Unit,
) {
    navigation(
        startDestination = UserProfileNavigation.UserProfileScreen.route,
        route = UserProfileNavigation.UserProfileBase.route,
    ) {
        userProfileRoute(
            navigateBack = navigateBack,
            navigateToChangePassword = navigateToChangePassword,
        )
    }
}

private fun NavGraphBuilder.userProfileRoute(
    navigateBack: () -> Unit,
    navigateToChangePassword: () -> Unit,
) {
    composable(
        route = UserProfileNavigation.UserProfileScreen.route,
    ) {
        UserProfileScreen(
            navigateBack = navigateBack,
            changePassword = navigateToChangePassword,
        )
    }
}

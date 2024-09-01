/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.mifos.mobile.feature.notification.NotificationScreen

fun NavController.navigateToNotificationScreen() {
    navigate(NotificationNavigation.NotificationScreen.route)
}

fun NavGraphBuilder.notificationNavGraph(
    navigateBack: () -> Unit,
) {
    navigation(
        startDestination = NotificationNavigation.NotificationScreen.route,
        route = NotificationNavigation.NotificationBase.route,
    ) {
        notificationScreenRoute(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.notificationScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = NotificationNavigation.NotificationScreen.route,
    ) {
        NotificationScreen(
            navigateBack = navigateBack,
        )
    }
}

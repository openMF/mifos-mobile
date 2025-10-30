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

package org.mifos.mobile.feature.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.notification.NotificationScreen

/**
 * A type-safe navigation destination for the Notification Screen. Using a serializable object
 * like this ensures that navigation is robust and less prone to runtime errors.
 */
@Serializable
data object NotificationRoute

/**
 * An extension function on [NavController] that provides a convenient and type-safe way to
 * navigate to the Notification Screen.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation action. This can be used
 *   to control aspects like the back stack and animations.
 */
fun NavController.navigateToNotificationScreen(navOptions: NavOptions? = null) {
    navigate(NotificationRoute, navOptions)
}

/**
 * An extension function on [NavGraphBuilder] that defines the Notification Screen destination
 * within the navigation graph. This is where the screen's composable is associated with its
 * route, and where transitions and arguments can be configured.
 *
 * @param navigateBack A lambda function that will be invoked when the user navigates back from
 *   the Notification Screen. This is typically used to pop the back stack.
 */
fun NavGraphBuilder.notificationDestination(
    navigateBack: () -> Unit,
) {
    composableWithPushTransitions<NotificationRoute> {
        NotificationScreen(
            navigateBack = navigateBack,
        )
    }
}

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

@Serializable
data object NotificationRoute

fun NavController.navigateToNotificationScreen(navOptions: NavOptions? = null) {
    navigate(NotificationRoute, navOptions)
}

fun NavGraphBuilder.notificationDestination(
    navigateBack: () -> Unit,
) {
    composableWithPushTransitions<NotificationRoute> {
        NotificationScreen(
            navigateBack = navigateBack,
        )
    }
}

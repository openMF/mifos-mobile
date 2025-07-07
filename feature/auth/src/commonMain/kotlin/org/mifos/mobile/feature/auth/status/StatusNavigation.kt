/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.auth.status

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.auth.otpAuthentication.EventType
import org.mifos.mobile.feature.auth.otpAuthentication.OtpAuthenticationRoute

@Serializable
data class StatusNavigationRoute(
    val eventType: EventType,
    val eventDestination: String,
    val buttonText: String = "Continue",
    val title: String = "Success",
    val subtitle: String = "You have completed the action.",
)

fun NavController.navigateToStatusScreen(
    eventType: EventType,
    eventDestination: String,
    buttonText: String = "Continue",
    title: String = "Success",
    subtitle: String = "You have completed the action.",
) {
    this.navigate(
        StatusNavigationRoute(
            eventType = eventType,
            eventDestination = eventDestination,
            buttonText = buttonText,
            title = title,
            subtitle = subtitle,
        ),
    ) {
        popUpTo(OtpAuthenticationRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.statusDestination(
    navigateToDestination: (String) -> Unit,
) {
    composableWithStayTransitions<StatusNavigationRoute> { backStackEntry ->
        val route: StatusNavigationRoute = backStackEntry.toRoute()
        StatusScreen(
            eventType = route.eventType,
            eventDestination = route.eventDestination,
            buttonText = route.buttonText,
            title = route.title,
            subtitle = route.subtitle,
            navigateToDestination = navigateToDestination,
        )
    }
}

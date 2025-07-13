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

package org.mifos.mobile.feature.auth.otpAuthentication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.auth.status.StatusNavigationRoute

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class OtpAuthenticationRoute(
    val nextRoute: String = StatusNavigationRoute.serializer().descriptor.serialName,
)

@OptIn(ExperimentalSerializationApi::class)
fun NavController.navigateToOtpAuthScreen(
    nextRoute: String = StatusNavigationRoute.serializer().descriptor.serialName,
    navOptions: NavOptions? = null,
) {
    this.navigate(OtpAuthenticationRoute(nextRoute), navOptions)
}

fun NavGraphBuilder.otpAuthenticationDestination(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (EventType, String, String, String, String) -> Unit,
    navigateToSetPasswordScreen: () -> Unit,
) {
    composableWithStayTransitions<OtpAuthenticationRoute> {
        OtpAuthenticationScreen(
            navigateBack = navigateBack,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateToSetPasswordScreen = navigateToSetPasswordScreen,
        )
    }
}

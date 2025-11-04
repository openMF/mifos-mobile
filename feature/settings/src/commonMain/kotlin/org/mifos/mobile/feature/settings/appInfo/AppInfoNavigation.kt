/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.appInfo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * Navigates to the App Info screen. This is an extension function on [NavController]
 * that simplifies the process of navigating to the app info destination.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation,
 *   allowing for customization of aspects like launch modes and animations.
 */
internal fun NavController.navigateToAppInfo(navOptions: NavOptions? = null) =
    navigate(SettingsItems.AppInfo, navOptions)

/**
 * Defines the composable destination for the "App Info" screen within the navigation graph.
 * This sets up the route and the content to be displayed, along with screen transitions.
 *
 * @param onBackClick A lambda function to be invoked when the user initiates a back action.
 * @param navigateToPrivacyPolicy A lambda function to navigate to the Privacy Policy screen.
 * @param navigateToTermsAndConditions A lambda function to navigate to the Terms and Conditions screen.
 */
internal fun NavGraphBuilder.appInfoDestination(
    onBackClick: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToTermsAndConditions: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.AppInfo> {
        AppInfoScreen(
            onBackClick = onBackClick,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy,
            navigateToTermsAndConditions = navigateToTermsAndConditions,
        )
    }
}

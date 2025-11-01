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
 * Navigates to the App Info screen.
 *
 * @param navOptions Optional [NavOptions] to configure the navigation action.
 */

internal fun NavController.navigateToAppInfo(navOptions: NavOptions? = null) =
    navigate(SettingsItems.AppInfo, navOptions)

/**
 * Defines the composable destination for the "App Info" screen within the navigation graph.
 * This sets up the route and the content to be displayed when navigating to the app info screen.
 *
 * @param onBackClick A lambda function to be invoked when the back button is clicked.
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

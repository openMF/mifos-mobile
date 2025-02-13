/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.about.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.AboutUsListItemId
import org.mifos.mobile.feature.about.openUrl
import org.mifos.mobile.feature.about.ui.AboutUsScreen
import org.mifos.mobile.feature.about.ui.PrivacyPolicyScreen

fun NavController.navigateToAboutUsScreen() {
    navigate(AboutUsNavigation.AboutUsScreen.route)
}

fun NavGraphBuilder.aboutUsNavGraph(
    navController: NavController,
    navigateToOssLicense: () -> Unit,
) {
    navigation(
        startDestination = AboutUsNavigation.AboutUsScreen.route,
        route = AboutUsNavigation.AboutUsBase.route,
    ) {
        aboutUsScreenRoute(
            navigateToPrivacyPolicy = {
                navController.navigate(AboutUsNavigation.PrivacyPolicyScreen.route)
            },
            navigateToOssLicense = navigateToOssLicense,
        )

        privacyPolicyScreenRoute(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.aboutUsScreenRoute(
    navigateToPrivacyPolicy: () -> Unit,
    navigateToOssLicense: () -> Unit,
) {
    composable(
        route = AboutUsNavigation.AboutUsScreen.route,
    ) {
        AboutUsScreen(
            navigateToItem = {
                navigateToItem(
                    aboutUsItem = it.itemId,
                    navigateToOssLicense = navigateToOssLicense,
                    navigateToPrivacyPolicy = navigateToPrivacyPolicy,
                )
            },
        )
    }
}

fun NavGraphBuilder.privacyPolicyScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = AboutUsNavigation.PrivacyPolicyScreen.route,
    ) {
        PrivacyPolicyScreen(
            navigateBack = navigateBack,
        )
    }
}

private fun navigateToItem(
    aboutUsItem: AboutUsListItemId,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToOssLicense: () -> Unit,
) {
    when (aboutUsItem) {
        AboutUsListItemId.OFFICE_WEBSITE -> {
            openUrl(Constants.WEBSITE_LINK)
        }

        AboutUsListItemId.LICENSES -> {
            openUrl(Constants.LICENSE_LINK)
        }

        AboutUsListItemId.PRIVACY_POLICY -> {
            navigateToPrivacyPolicy()
        }

        AboutUsListItemId.SOURCE_CODE -> {
            openUrl(Constants.SOURCE_CODE_LINK)
        }

        AboutUsListItemId.LICENSES_STRING_WITH_VALUE -> {
            navigateToOssLicense()
        }

        AboutUsListItemId.APP_VERSION_TEXT -> Unit
    }
}

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

package org.mifos.mobile.feature.shareaccount.shareAccountDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class ShareAccountDetailsRoute(
    val accountId: Long,
)

fun NavController.navigateToShareAccountDetailsScreen(
    accountId: Long,
    navOptions: NavOptions? = null,
) = navigate(ShareAccountDetailsRoute(accountId), navOptions)

fun NavGraphBuilder.shareAccountDetailsDestination(
    navigateBack: () -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToShareAccountTransactionScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
) {
    composableWithSlideTransitions<ShareAccountDetailsRoute> {
        ShareAccountDetailsScreen(
            navigateBack = navigateBack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToShareAccountTransactionScreen = navigateToShareAccountTransactionScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
        )
    }
}

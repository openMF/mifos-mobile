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

package org.mifos.mobile.feature.savings.application.fillApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class SavingsFillApplicationRoute(
    val savingsProductId: Long,
    val fieldOfficerId: Long,
    val fieldOfficerName: String,
)

fun NavController.navigateToSavingsFillApplicationScreen(
    savingsProductId: Long,
    fieldOfficerId: Long,
    fieldOfficerName: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(SavingsFillApplicationRoute(savingsProductId, fieldOfficerId, fieldOfficerName), navOptions)
}

fun NavGraphBuilder.savingsFillApplicationDestination(
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsFillApplicationRoute> {
        SavingsFillApplicationScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}

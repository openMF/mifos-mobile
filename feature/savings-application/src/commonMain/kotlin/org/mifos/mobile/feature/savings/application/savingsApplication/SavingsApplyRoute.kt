/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.savingsApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data object SavingsApplyRoute

fun NavController.navigateToSavingsApplyScreen(
    navOptions: NavOptions? = null,
) =
    navigate(SavingsApplyRoute, navOptions)

fun NavGraphBuilder.savingsApplyDestination(
    navigateToFillDetailsScreen: (Long, Long, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsApplyRoute> {
        SavingsApplyScreen(
            navigateBack = navigateBack,
            navigateToFillDetailsScreen = navigateToFillDetailsScreen,
        )
    }
}

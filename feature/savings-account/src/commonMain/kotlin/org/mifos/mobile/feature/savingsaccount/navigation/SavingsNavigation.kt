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

package org.mifos.mobile.feature.savingsaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.savingsaccount.SavingsAccountScreen

@Serializable
@SerialName(Constants.SAVINGS_ACCOUNT)
data object SavingsAccountRoute

fun NavController.navigateToSavingsAccountScreen(navOptions: NavOptions? = null) =
    navigate(SavingsAccountRoute, navOptions)

fun NavGraphBuilder.savingsAccountDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountRoute> {
        SavingsAccountScreen(
            navigateBack = navigateBack,
        )
    }
}

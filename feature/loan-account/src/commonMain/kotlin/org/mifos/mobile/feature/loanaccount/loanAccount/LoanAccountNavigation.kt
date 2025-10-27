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

package org.mifos.mobile.feature.loanaccount.loanAccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A route for the loan account screen.
 */
@Serializable
data object LoanAccountRoute

/**
 * Navigates to the loan account screen.
 *
 * @param navOptions The navigation options.
 */
fun NavController.navigateToLoanAccountScreen(navOptions: NavOptions? = null) =
    navigate(LoanAccountRoute, navOptions)

/**
 * Defines the loan account screen destination in the navigation graph.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 */
fun NavGraphBuilder.loanAccountDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<LoanAccountRoute> {
        LoanAccountScreen(
            navigateBack = navigateBack,
        )
    }
}

/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.onboarding.language.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.onboarding.language.OnboardingLanguageScreen

@Serializable
data object OnboardingLanguageRoute

fun NavController.navigateToOnboardingLanguage(navOptions: NavOptions? = null) {
    this.navigate(route = OnboardingLanguageRoute, navOptions = navOptions)
}

@Suppress("UnusedParameter")
fun NavGraphBuilder.onBoardingLanguageDestination(navOptions: NavOptions? = null) {
    composableWithStayTransitions<OnboardingLanguageRoute> {
        OnboardingLanguageScreen()
    }
}

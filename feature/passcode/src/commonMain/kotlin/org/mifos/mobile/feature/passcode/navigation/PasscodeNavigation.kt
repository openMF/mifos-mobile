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

package org.mifos.mobile.feature.passcode.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithRootPushTransitions
import org.mifos.mobile.feature.passcode.PasscodeScreen

sealed class PasscodeRoute {
    @Serializable
    data object Standard : PasscodeRoute()

    @Serializable
    data object Biometric : PasscodeRoute()

    @Serializable
    data object Pin : PasscodeRoute()
}

fun NavController.navigateToPasscodeScreen(navOptions: NavOptions? = null) {
    navigate(PasscodeRoute.Standard, navOptions)
}

fun NavGraphBuilder.passcodeDestination(
    onPasscodeConfirm: (String, String, String, String, String) -> Unit,
) {
    composableWithRootPushTransitions<PasscodeRoute.Standard> {
        PasscodeScreen(
            onPasscodeConfirm = onPasscodeConfirm,
        )
    }
}

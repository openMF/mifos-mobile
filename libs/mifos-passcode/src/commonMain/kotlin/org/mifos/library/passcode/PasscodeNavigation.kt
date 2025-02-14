/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val PASSCODE_SCREEN = "passcode_screen"

fun NavGraphBuilder.passcodeRoute(
    onForgotButton: () -> Unit,
    onSkipButton: () -> Unit,
    onPasscodeConfirm: (String) -> Unit,
    onPasscodeRejected: () -> Unit,
) {
    composable(
        route = PASSCODE_SCREEN,
    ) {
        PasscodeScreen(
            onForgotButton = onForgotButton,
            onSkipButton = onSkipButton,
            onPasscodeConfirm = onPasscodeConfirm,
            onPasscodeRejected = onPasscodeRejected,
        )
    }
}

fun NavController.navigateToPasscodeScreen(options: NavOptions? = null) {
    navigate(PASSCODE_SCREEN, options)
}

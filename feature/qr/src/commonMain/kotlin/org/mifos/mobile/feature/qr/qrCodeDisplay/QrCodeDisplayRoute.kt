/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeDisplay

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithPushTransitions

@Serializable
data class QrCodeDisplayRoute(val qrString: String = "")

fun NavController.navigateToQrDisplayScreen(qrString: String, navOptions: NavOptions? = null) {
    this.navigate(QrCodeDisplayRoute(qrString), navOptions)
}

fun NavGraphBuilder.qrDisplayDestination(
    navigateBack: () -> Unit,
) {
    composableWithPushTransitions<QrCodeDisplayRoute> {
        QrCodeDisplayScreen(
            navigateBack = navigateBack,
        )
    }
}

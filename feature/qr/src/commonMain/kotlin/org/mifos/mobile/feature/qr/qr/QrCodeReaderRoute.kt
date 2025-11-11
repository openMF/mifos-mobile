/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qr

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.composableWithPushTransitions

/**
 * Represents the type-safe navigation route for the QR Code Reader screen.
 */
@Serializable
data object QrCodeReaderRoute

/**
 * Navigates to the QR Code Reader screen.
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToQrReaderScreen(navOptions: NavOptions? = null) {
    this.navigate(QrCodeReaderRoute, navOptions)
}

/**
 * Defines the composable destination for the QR Code Reader screen in the navigation graph.
 *
 * @param navigateBack Callback to navigate to the previous screen.
 * @param navigateToQrImportScreen Callback to navigate to the QR import screen.
 * @param openBeneficiaryApplication Callback to open the beneficiary application screen
 * with the parsed beneficiary data.
 */
fun NavGraphBuilder.qrReaderDestination(
    navigateBack: () -> Unit,
    navigateToQrImportScreen: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
) {
    composableWithPushTransitions<QrCodeReaderRoute> {
        QrCodeReaderScreen(
            navigateBack = navigateBack,
            openBeneficiaryApplication = openBeneficiaryApplication,
            navigateToQrImportScreen = navigateToQrImportScreen,
        )
    }
}

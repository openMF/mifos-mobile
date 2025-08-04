/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.qr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.qr.qr.qrReaderDestination
import org.mifos.mobile.feature.qr.qrCodeDisplay.QrCodeDisplayRoute
import org.mifos.mobile.feature.qr.qrCodeDisplay.qrDisplayDestination
import org.mifos.mobile.feature.qr.qrCodeImport.navigateToQrImportScreen
import org.mifos.mobile.feature.qr.qrCodeImport.qrImportDestination

@Serializable
data object QrGraphRoute

fun NavController.navigateToQrGraph(navOptions: NavOptions? = null) {
    this.navigate(QrGraphRoute, navOptions)
}

fun NavGraphBuilder.qrNavGraph(
    navController: NavController,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
) {
    navigation<QrGraphRoute>(
        startDestination = QrCodeDisplayRoute(),
    ) {
        qrReaderDestination(
            navigateBack = navController::popBackStack,
            openBeneficiaryApplication = openBeneficiaryApplication,
            navigateToQrImportScreen = navController::navigateToQrImportScreen,
        )

        qrDisplayDestination(
            navigateBack = navController::popBackStack,
        )

        qrImportDestination(
            navigateBack = navController::popBackStack,
            openBeneficiaryApplication = openBeneficiaryApplication,
        )
    }
}

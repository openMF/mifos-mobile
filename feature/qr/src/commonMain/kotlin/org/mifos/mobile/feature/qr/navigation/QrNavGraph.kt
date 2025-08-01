/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.qr.qr.QrCodeReaderScreen
import org.mifos.mobile.feature.qr.qrCodeDisplay.QrCodeDisplayScreen
import org.mifos.mobile.feature.qr.qrCodeImport.QrCodeImportScreen

@Serializable
data object QrGraphRoute

@Serializable
data object QrReaderScreenRoute

@Serializable
data class QrDisplayScreenRoute(val qrString: String = "")

@Serializable
data object QrImportScreenRoute

fun NavController.navigateToQrReaderScreen(navOptions: NavOptions? = null) {
    this.navigate(QrReaderScreenRoute, navOptions)
}

fun NavController.navigateToQrImportScreen(navOptions: NavOptions? = null) {
    this.navigate(QrImportScreenRoute, navOptions)
}

fun NavController.navigateToQrDisplayScreen(qrString: String, navOptions: NavOptions? = null) {
    this.navigate(QrDisplayScreenRoute(qrString), navOptions)
}

fun NavGraphBuilder.qrNavGraph(
    navController: NavController,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
) {
    navigation<QrGraphRoute>(
        startDestination = QrDisplayScreenRoute(),
    ) {
        readerRoute(
            navigateBack = navController::popBackStack,
            openBeneficiaryApplication = openBeneficiaryApplication,
        )

        displayRoute(
            navigateBack = navController::popBackStack,
        )

        importRoute(
            navigateBack = navController::popBackStack,
            openBeneficiaryApplication = openBeneficiaryApplication,
        )
    }
}

fun NavGraphBuilder.readerRoute(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
) {
    composable<QrReaderScreenRoute> {
        QrCodeReaderScreen(
            navigateBack = navigateBack,
            openBeneficiaryApplication = openBeneficiaryApplication,
        )
    }
}

fun NavGraphBuilder.displayRoute(
    navigateBack: () -> Unit,
) {
    composable<QrDisplayScreenRoute> {
        QrCodeDisplayScreen(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.importRoute(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
) {
    composable<QrImportScreenRoute> {
        QrCodeImportScreen(
            navigateBack = navigateBack,
            openBeneficiaryApplication = openBeneficiaryApplication,
        )
    }
}

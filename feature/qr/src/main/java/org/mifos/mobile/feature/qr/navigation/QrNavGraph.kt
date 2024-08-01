package org.mifos.mobile.feature.qr.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.qr.navigation.QrRoute.QR_NAVIGATION_ROUTE
import org.mifos.mobile.feature.qr.qr.QrCodeReaderScreen
import org.mifos.mobile.feature.qr.qr_code_display.QrCodeDisplayScreen
import org.mifos.mobile.feature.qr.qr_code_import.QrCodeImportScreen

fun NavGraphBuilder.qrNavGraph(
    startDestination: String,
    navController: NavHostController,
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
    qrData: String?
) {
    navigation(
        startDestination = startDestination,
        route = QR_NAVIGATION_ROUTE
    ) {
        readerRoute(
            navigateBack = navigateBack,
            openBeneficiaryApplication = openBeneficiaryApplication
        )

        displayRoute(
            navigateBack = navigateBack,
            qrData = qrData
        )

        importRoute(
            navigateBack = { navController.popBackStack() },
            openBeneficiaryApplication = openBeneficiaryApplication
        )
    }
}

fun NavGraphBuilder.readerRoute(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: ( Beneficiary, BeneficiaryState ) -> Unit,
) {
    composable(route = QrNavigation.Reader.route) {
        QrCodeReaderScreen(
            navigateBack= navigateBack,
            openBeneficiaryApplication= openBeneficiaryApplication
        )
    }
}

fun NavGraphBuilder.displayRoute(
    navigateBack: () -> Unit,
    qrData: String?
) {
    composable(route = QrNavigation.Display.route){
        QrCodeDisplayScreen(
            navigateBack = navigateBack,
            qrData = qrData
        )
    }
}

fun NavGraphBuilder.importRoute(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit
) {
    composable(route = QrNavigation.Import.route){
        QrCodeImportScreen(
            navigateBack= navigateBack,
            openBeneficiaryApplication = openBeneficiaryApplication
        )
    }
}
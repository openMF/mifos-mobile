package org.mifos.mobile.feature.beneficiary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.BeneficiaryApplicationNavRoute
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.manualBeneficiaryAddDestination
import org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation.beneficiaryAddConfirmationDestination
import org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation.navigateToBeneficiaryApplicationAddConfirmationScreen

data object BeneficiaryNavRoute


fun NavController.navigateToBeneficiaryNavGraph(navOptions: NavOptions? = null) =
    navigate(BeneficiaryNavRoute, navOptions)

fun NavGraphBuilder.beneficiaryNavGraph(
    navController: NavController,
    navigateToQR:()->Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    navigation<BeneficiaryNavRoute>(
        startDestination = BeneficiaryApplicationNavRoute(),
    ) {
        manualBeneficiaryAddDestination(
            navigateBack = navController::popBackStack,
            navigateToConfirmationScreen =
                navController::navigateToBeneficiaryApplicationAddConfirmationScreen,
            navigateToQR = navigateToQR
        )
        beneficiaryAddConfirmationDestination(
            navigateBack = navController::popBackStack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}


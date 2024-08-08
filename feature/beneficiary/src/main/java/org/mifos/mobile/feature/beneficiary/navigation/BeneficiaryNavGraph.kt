package org.mifos.mobile.feature.beneficiary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.beneficiary_application.BeneficiaryApplicationScreen
import org.mifos.mobile.feature.beneficiary.beneficiary_detail.BeneficiaryDetailScreen
import org.mifos.mobile.feature.beneficiary.beneficiary_list.BeneficiaryListScreen
import org.mifos.mobile.feature.beneficiary.presentation.BeneficiaryScreen

fun NavController.navigateToBeneficiaryListScreen() {
    navigate(BeneficiaryNavigation.BeneficiaryList.route)
}

fun NavController.navigateToAddBeneficiaryScreen() {
    navigate(BeneficiaryNavigation.AddBeneficiary.route)
}

fun NavController.navigateToBeneficiaryApplicationScreen(beneficiary: Beneficiary?, beneficiaryState: BeneficiaryState) {
    navigate(
        BeneficiaryNavigation.BeneficiaryApplication.passArguments(
            beneficiaryId = beneficiary?.id ?: -1,
            beneficiaryState = beneficiaryState
        )
    )
}

fun NavGraphBuilder.beneficiaryNavGraph(
    navController: NavHostController,
    openQrReaderScreen: () -> Unit,
    openQrImportScreen: () -> Unit
) {
    navigation(
        startDestination = BeneficiaryNavigation.BeneficiaryList.route,
        route = BeneficiaryNavigation.BeneficiaryBaseRoute.route
    ) {
        beneficiaryListRoute(
            navigateBack = navController::popBackStack,
            addBeneficiary = {
                navController.navigate(BeneficiaryNavigation.AddBeneficiary.route)
            },
            showBeneficiaryDetail = { beneficiaryId ->
                navController.navigate(BeneficiaryNavigation.BeneficiaryDetail.passArguments(beneficiaryId = beneficiaryId))
            }
        )

        addBeneficiaryRoute(
            navigateBack = navController::popBackStack,
            addBeneficiaryManually = {
                navController.navigate(
                    BeneficiaryNavigation.BeneficiaryApplication.passArguments(-1, BeneficiaryState.CREATE_MANUAL)
                )
            },
            openQrScanner = openQrReaderScreen,
            uploadQrCode = openQrImportScreen
        )

        beneficiaryDetailRoute(
            navigateBack = navController::popBackStack,
            updateBeneficiary = { beneficiary ->
                navController.navigate(
                   BeneficiaryNavigation.BeneficiaryApplication.passArguments(beneficiary?.id ?: -1, BeneficiaryState.UPDATE)
                )
            }
        )

        beneficiaryApplicationRoute(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.beneficiaryListRoute(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    showBeneficiaryDetail: (Int) -> Unit
) {
    composable(route = BeneficiaryNavigation.BeneficiaryList.route) {
        BeneficiaryListScreen(
            navigateBack = navigateBack,
            addBeneficiaryClicked = addBeneficiary,
            onBeneficiaryItemClick = showBeneficiaryDetail
        )
    }
}

fun NavGraphBuilder.addBeneficiaryRoute(
    navigateBack: () -> Unit,
    addBeneficiaryManually: () -> Unit,
    openQrScanner: () -> Unit,
    uploadQrCode: () -> Unit
) {
    composable(route = BeneficiaryNavigation.AddBeneficiary.route) {
        BeneficiaryScreen(
            topAppbarNavigateback = navigateBack,
            addiconClicked = addBeneficiaryManually,
            scaniconClicked = openQrScanner,
            uploadIconClicked = uploadQrCode
        )
    }
}

fun NavGraphBuilder.beneficiaryDetailRoute(
    navigateBack: () -> Unit,
    updateBeneficiary: (beneficiary: Beneficiary?) -> Unit,
) {
    composable(
        route = BeneficiaryNavigation.BeneficiaryDetail.route,
        arguments = listOf(
            navArgument(name = BENEFICIARY_ID) { type = NavType.IntType }
        )
    ) {
        BeneficiaryDetailScreen(
            navigateBack = navigateBack,
            updateBeneficiary = updateBeneficiary,
        )
    }
}

fun NavGraphBuilder.beneficiaryApplicationRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = BeneficiaryNavigation.BeneficiaryApplication.route,
        arguments = listOf(
            navArgument(name = BENEFICIARY_ID) { type = NavType.IntType },
            navArgument(name = Constants.BENEFICIARY_STATE) { type = NavType.EnumType(BeneficiaryState::class.java) }
        )
    ) {
        BeneficiaryApplicationScreen(
            navigateBack = navigateBack,
        )
    }
}
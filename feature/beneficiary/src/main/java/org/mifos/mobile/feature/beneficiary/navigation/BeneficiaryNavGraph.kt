package org.mifos.mobile.feature.beneficiary.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.beneficiary_application.BeneficiaryApplicationScreen
import org.mifos.mobile.feature.beneficiary.beneficiary_detail.BeneficiaryDetailScreen
import org.mifos.mobile.feature.beneficiary.beneficiary_list.BeneficiaryListScreen
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute.BENEFICIARY_LIST_ROUTE
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute.BENEFICIARY_NAVIGATION_ROUTE
import org.mifos.mobile.feature.beneficiary.presentation.BeneficiaryScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.beneficiaryNavGraph(
    startDestination: String,
    navController: NavHostController,
    navigateBack: () -> Unit,
    openQrReaderScreen: () -> Unit,
    openQrImportScreen: () -> Unit
) {
    navigation(
        startDestination = startDestination,
        route = BENEFICIARY_NAVIGATION_ROUTE
    ) {
        beneficiaryListRoute(
            navigateBack = navigateBack,
            addBeneficiary = {
                navController.navigate(BeneficiaryNavigation.AddBeneficiary.route)
             },
            showBeneficiaryDetail = { beneficiaryId, beneficiaryList ->
                navController.navigate(
                    BeneficiaryNavigation.BeneficiaryDetail(
                        beneficiaryList[beneficiaryId]
                    ).route
                )
            }
        )

        addBeneficiaryRoute(
            navigateBack= { navController.popBackStack() },
            addBeneficiaryManually = {
                navController.navigate(
                    BeneficiaryNavigation.BeneficiaryApplication(
                        BeneficiaryState.CREATE_MANUAL,
                        null,
                    ).route
                )
             },
            openQrScanner = openQrReaderScreen,
            uploadQrCode = openQrImportScreen
        )

        beneficiaryDetailRoute(
            navigateBack = { navController.popBackStack() },
            updateBeneficiary = { beneficiary ->
                navController.navigate(
                    BeneficiaryNavigation.BeneficiaryApplication(
                        BeneficiaryState.UPDATE,
                        beneficiary
                    ).route
                )
            }
        )

        beneficiaryApplicationRoute(
            navigateBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.beneficiaryListRoute(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    showBeneficiaryDetail: ( Int, List<Beneficiary>) -> Unit
) {
    composable(route = BeneficiaryNavigation.BeneficiaryList.route) {
        BeneficiaryListScreen(
            navigateBack = navigateBack,
            addBeneficiaryClicked = addBeneficiary,
            onBeneficiaryItemClick = { beneficiaryId, beneficiaryList ->
                showBeneficiaryDetail(beneficiaryId, beneficiaryList)
            },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.addBeneficiaryRoute(
    navigateBack: () -> Unit,
    addBeneficiaryManually: () -> Unit,
    openQrScanner: () -> Unit,
    uploadQrCode: () -> Unit
) {
    composable(route = BeneficiaryNavigation.AddBeneficiary.route) {
        BeneficiaryScreen(
            topAppbarNavigateback= navigateBack,
            addiconClicked= addBeneficiaryManually,
            scaniconClicked= openQrScanner,
            uploadIconClicked = uploadQrCode
        )
    }
}

fun NavGraphBuilder.beneficiaryDetailRoute(
    navigateBack: () -> Unit,
    updateBeneficiary: ( beneficiary : Beneficiary? ) -> Unit,
    beneficiary : Beneficiary? = null
) {
    composable(route = BeneficiaryNavigation.BeneficiaryDetail(beneficiary).route) {
        BeneficiaryDetailScreen(
            navigateBack = navigateBack,
            updateBeneficiary = updateBeneficiary,
            beneficiary = beneficiary
        )
    }
}

fun NavGraphBuilder.beneficiaryApplicationRoute(
    navigateBack: () -> Unit,
    beneficiary : Beneficiary? = null,
    beneficiaryState: BeneficiaryState? = null
) {
    composable(route = BeneficiaryNavigation.BeneficiaryApplication(beneficiaryState, beneficiary).route) {
        BeneficiaryApplicationScreen(
            navigateBack = navigateBack,
            beneficiary = beneficiary,
            beneficiaryState = beneficiaryState
        )
    }
}
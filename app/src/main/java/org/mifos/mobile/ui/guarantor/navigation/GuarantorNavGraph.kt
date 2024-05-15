package org.mifos.mobile.ui.guarantor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mifos.mobile.ui.guarantor.guarantor_add.AddGuarantorScreen
import org.mifos.mobile.ui.guarantor.guarantor_details.GuarantorDetailScreen
import org.mifos.mobile.ui.guarantor.guarantor_list.GuarantorListScreen
import org.mifos.mobile.utils.Constants.INDEX
import org.mifos.mobile.utils.Constants.LOAN_ID

@Composable
fun SetUpGuarantorNavGraph(
    startDestination: String, navController: NavHostController, loanId: Long, navigateBack: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {

        addGuarantorRoute(
            navigateBack = { navController.popBackStack() }
        )

        listGuarantorRoute(
            loanId = loanId,
            navigateBack = navigateBack,
            addGuarantor = {
                navController.navigate(GuarantorScreen.GuarantorAdd.passArguments(-1, loanId))
            },
            onGuarantorClicked = { index->
                navController.navigate(GuarantorScreen.GuarantorDetails.passArguments(index = index, loanId = loanId))
            }
        )

        detailGuarantorRoute(
            navigateBack = { navController.popBackStack() },
            updateGuarantor = { index, loanId ->
                navController.navigate(GuarantorScreen.GuarantorAdd.passArguments(index = index, loanId = loanId))
            }
        )
    }
}

fun NavGraphBuilder.listGuarantorRoute(
    loanId: Long,
    navigateBack: () -> Unit,
    addGuarantor: (Long) -> Unit,
    onGuarantorClicked: (Int) -> Unit
) {
    composable(
        route = GuarantorScreen.GuarantorList.route,
        arguments = listOf(
            navArgument(name = LOAN_ID) { type = NavType.LongType; defaultValue = loanId }
        )
    ) {
        GuarantorListScreen(
            navigateBack = navigateBack,
            addGuarantor = { addGuarantor(loanId) },
            onGuarantorClicked = onGuarantorClicked
        )
    }
}

fun NavGraphBuilder.detailGuarantorRoute(
    navigateBack: () -> Unit,
    updateGuarantor: (index: Int, loanId: Long) -> Unit
) {
    composable(
        route = GuarantorScreen.GuarantorDetails.route,
        arguments = listOf(
            navArgument(name = INDEX) { type = NavType.IntType },
            navArgument(name = LOAN_ID) { type = NavType.LongType }
        )
    ) {
        GuarantorDetailScreen(
            navigateBack = navigateBack,
            updateGuarantor = updateGuarantor
        )
    }
}

fun NavGraphBuilder.addGuarantorRoute(
    navigateBack: () -> Unit
) {
    composable(
        route = GuarantorScreen.GuarantorAdd.route,
        arguments = listOf(
            navArgument(name = INDEX) { type = NavType.IntType; defaultValue = -1 },
            navArgument(name = LOAN_ID) { type = NavType.LongType }
        )
    ) {
        AddGuarantorScreen(
            navigateBack = navigateBack,
        )
    }
}


package org.mifos.mobile.feature.transfer.process.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.gson.Gson
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.transfer.process.TransferProcessScreen

// Navigation Setup
fun NavController.navigateToTransferProcessScreen(transferPayload: ReviewTransferPayload, transferType: TransferType) {
    navigate(
        TransferProcessNavigation.TransferProcessScreen.passArguments(
            transferType = transferType,
            payload = transferPayload.convertToTransferPayloadString()
        )
    )
}

fun NavGraphBuilder.transferProcessNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = TransferProcessNavigation.TransferProcessScreen.route,
        route = TransferProcessNavigation.TransferProcessBase.route,
    ) {
        transferProcessScreenRoute(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.transferProcessScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = TransferProcessNavigation.TransferProcessScreen.route,
        arguments = listOf(
            navArgument(name = Constants.PAYLOAD) { type = NavType.StringType },
            navArgument(name = Constants.TRANSFER_TYPE) { type = NavType.EnumType(TransferType::class.java) }
        )
    ) {
        TransferProcessScreen(
            navigateBack = navigateBack,
        )
    }
}


fun ReviewTransferPayload.convertToTransferPayloadString(): String {
    val payload = this
    val transferPayload = TransferPayload().apply {
        fromAccountId = payload.payFromAccount?.accountId
        fromClientId = payload.payFromAccount?.clientId
        fromAccountType = payload.payFromAccount?.accountType?.id
        fromOfficeId = payload.payFromAccount?.officeId
        toOfficeId = payload.payFromAccount?.officeId
        toAccountId = payload.payToAccount?.accountId
        toClientId = payload.payToAccount?.clientId
        toAccountType = payload.payToAccount?.accountType?.id
        transferDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, getTodayFormatted())
        transferAmount = payload.amount.toDoubleOrNull()
        transferDescription = payload.review
        fromAccountNumber = payload.payFromAccount?.accountNo
        toAccountNumber = payload.payToAccount?.accountNo
    }

    val gson = Gson()
    return gson.toJson(transferPayload)
}
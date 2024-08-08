package org.mifos.mobile.feature.savings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.ACCOUNT_ID
import org.mifos.mobile.core.common.Constants.OUTSTANDING_BALANCE
import org.mifos.mobile.core.common.Constants.SAVINGS_ID
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_FROM
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.feature.savings.savings_account.SavingsAccountDetailScreen
import org.mifos.mobile.feature.savings.savings_account_application.SavingsAccountApplicationScreen
import org.mifos.mobile.feature.savings.savings_account_transaction.SavingsAccountTransactionScreen
import org.mifos.mobile.feature.savings.savings_account_withdraw.SavingsAccountWithdrawScreen
import org.mifos.mobile.feature.savings.savings_make_transfer.ReviewTransferPayload
import org.mifos.mobile.feature.savings.savings_make_transfer.SavingsMakeTransferScreen

fun NavController.navigateToSavingsMakeTransfer(accountId: Long, outstandingBalance: Double? = null, transferType: String) {
    navigate(SavingsNavigation.SavingsMakeTransfer.passArguments(accountId, (outstandingBalance ?: 0.0).toString(), transferType))
}

fun NavController.navigateToSavingsDetailScreen(savingsId: Long) {
    navigate(SavingsNavigation.SavingsDetail.passArguments(savingsId = savingsId))
}

fun NavController.navigateToSavingsApplicationScreen() {
    navigate(
        SavingsNavigation.SavingsApplication.passArguments(
            savingsId = -1L,
            savingsAccountState = SavingsAccountState.CREATE
        )
    )
}

fun NavGraphBuilder.savingsNavGraph(
    navController: NavController,
    viewQrCode: (String) -> Unit,
    viewCharges: (ChargeType) -> Unit,
    reviewTransfer: (ReviewTransferPayload) -> Unit,
    callHelpline: () -> Unit
) {
    navigation(
        startDestination = SavingsNavigation.SavingsDetail.route,
        route = SavingsNavigation.SavingsBase.route,
    ) {
        savingsDetailRoute(
            callUs = callHelpline,
            deposit = { navController.navigateToSavingsMakeTransfer(accountId = it, transferType = TRANSFER_PAY_TO) },
            makeTransfer = { navController.navigateToSavingsMakeTransfer(accountId = it, transferType = TRANSFER_PAY_FROM) },
            navigateBack = navController::popBackStack,
            updateSavingsAccount = { navController.navigate(SavingsNavigation.SavingsApplication.passArguments(savingsId = it, savingsAccountState = SavingsAccountState.UPDATE)) },
            viewCharges = { viewCharges(ChargeType.SAVINGS) },
            viewQrCode = viewQrCode,
            viewTransaction = { navController.navigate(SavingsNavigation.SavingsTransaction.passArguments(it)) },
            withdrawSavingsAccount = { navController.navigate(SavingsNavigation.SavingsWithdraw.passArguments(it)) }
        )

        savingsApplication(
            navigateBack = navController::popBackStack,
        )

        savingsTransaction(
            navigateBack = navController::popBackStack,
        )

        savingsWithdraw(
            navigateBack = navController::popBackStack,
        )

        savingsMakeTransfer(
            navigateBack = navController::popBackStack,
            reviewTransfer = reviewTransfer
        )
    }
}

fun NavGraphBuilder.savingsDetailRoute(
    navigateBack: () -> Unit,
    updateSavingsAccount: (Long) -> Unit,
    withdrawSavingsAccount: (Long) -> Unit,
    makeTransfer: (Long) -> Unit,
    viewTransaction: (Long) -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (String) -> Unit,
    callUs: () -> Unit,
    deposit: (Long) -> Unit
) {
    composable(
        route = SavingsNavigation.SavingsDetail.route,
        arguments = listOf(
            navArgument(name = SAVINGS_ID) { type = NavType.LongType },
        )
    ) {
        SavingsAccountDetailScreen(
            navigateBack = navigateBack,
            updateSavingsAccount = updateSavingsAccount,
            withdrawSavingsAccount = withdrawSavingsAccount,
            makeTransfer = makeTransfer,
            viewTransaction = viewTransaction,
            viewCharges = viewCharges,
            viewQrCode = viewQrCode,
            callUs = callUs,
            deposit = deposit,
        )
    }
}

fun NavGraphBuilder.savingsApplication(
    navigateBack: () -> Unit
) {
    composable(
        route = SavingsNavigation.SavingsApplication.route,
        arguments = listOf(
            navArgument(name = SAVINGS_ID) { type = NavType.LongType },
            navArgument(Constants.SAVINGS_ACCOUNT_STATE) { type = NavType.EnumType(SavingsAccountState::class.java) },
        )
    ) {
        SavingsAccountApplicationScreen(
            navigateBack = navigateBack,
            submit = { _, _, _ -> },
        )
    }
}

fun NavGraphBuilder.savingsTransaction(
    navigateBack: () -> Unit,
) {
    composable(
        route = SavingsNavigation.SavingsTransaction.route,
        arguments = listOf(navArgument(name = SAVINGS_ID) { type = NavType.LongType })
    ) {
        SavingsAccountTransactionScreen(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.savingsWithdraw(
    navigateBack: () -> Unit,
) {
    composable(
        route = SavingsNavigation.SavingsWithdraw.route,
        arguments = listOf(navArgument(SAVINGS_ID) { type = NavType.LongType })
    ) {
        SavingsAccountWithdrawScreen(
            navigateBack = { navigateBack() },
        )
    }
}

fun NavGraphBuilder.savingsMakeTransfer(
    navigateBack: () -> Unit,
    reviewTransfer: (ReviewTransferPayload) -> Unit
) {
    composable(
        route = SavingsNavigation.SavingsMakeTransfer.route,
        arguments = listOf(
            navArgument(name = ACCOUNT_ID) { type = NavType.LongType },
            navArgument(name = OUTSTANDING_BALANCE) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(name = TRANSFER_TYPE) { type = NavType.StringType },
        )
    ) {
        SavingsMakeTransferScreen(
            navigateBack = navigateBack,
            onCancelledClicked = navigateBack,
            reviewTransfer = reviewTransfer
        )
    }
}

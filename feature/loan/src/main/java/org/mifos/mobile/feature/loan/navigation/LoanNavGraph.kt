package org.mifos.mobile.feature.loan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.loan_account.LoanAccountDetailScreen
import org.mifos.mobile.feature.loan.loan_account_application.LoanApplicationScreen
import org.mifos.mobile.feature.loan.loan_account_summary.LoanAccountSummaryScreen
import org.mifos.mobile.feature.loan.loan_account_transaction.LoanAccountTransactionScreen
import org.mifos.mobile.feature.loan.loan_account_withdraw.LoanAccountWithdrawScreen
import org.mifos.mobile.feature.loan.loan_repayment_schedule.LoanRepaymentScheduleScreen
import org.mifos.mobile.feature.loan.loan_review.ReviewLoanApplicationScreen


fun NavController.navigateToLoanDetailScreen(loanId: Long) {
    navigate(LoanNavigation.LoanDetail.passArguments(loanId = loanId))
}

fun NavController.navigateToLoanApplication() {
    navigate(LoanNavigation.LoanApplication.passArguments(loanId = -1L, loanState = LoanState.CREATE))
}

fun NavController.navigateToLoanReview(loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) {
    navigate(
        LoanNavigation.LoanReview.passArguments(
            accountNo = accountNo,
            loanId = loanId,
            loanState = loanState,
            loansPayload = loansPayloadString,
            loanName = loanName
        )
    )
}

fun NavGraphBuilder.loanNavGraph(
    navController: NavController,
    viewGuarantor: (Long) -> Unit,
    viewCharges: (ChargeType) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (accountId: Long, outstandingBalance: Double?, transferType: String) -> Unit,
) {
    navigation(
        startDestination = LoanNavigation.LoanDetail.route,
        route = LoanNavigation.LoanBase.route,
    ) {
        loanDetailRoute(
            navigateBack = navController::popBackStack,
            viewGuarantor = viewGuarantor,
            updateLoan = { navController.navigate(LoanNavigation.LoanApplication.passArguments(it, LoanState.UPDATE)) },
            withdrawLoan = { navController.navigate(LoanNavigation.LoanWithdraw.passArguments(it)) },
            viewLoanSummary = { navController.navigate(LoanNavigation.LoanSummary.passArguments(it)) },
            viewCharges = { viewCharges(ChargeType.LOAN) },
            viewRepaymentSchedule = { navController.navigate(LoanNavigation.LoanSchedule.passArguments(it)) },
            viewTransactions = { navController.navigate(LoanNavigation.LoanTransaction.passArguments(it)) },
            viewQr = viewQr,
            makePayment = makePayment
        )

        loanApplication(
            navigateBack = navController::popBackStack,
            reviewNewLoanApplication = navController::navigateToLoanReview,
            submitUpdateLoanApplication = navController::navigateToLoanReview
        )

        loanSummary(
            navigateBack = navController::popBackStack,
        )

        loanTransaction(
            navigateBack = navController::popBackStack,
        )

        loanWithdraw(
            navigateBack = navController::popBackStack,
        )

        loanRepaymentSchedule(
            navigateBack = navController::popBackStack,
        )

        loanReview(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.loanDetailRoute(
    navigateBack: () -> Unit,
    viewGuarantor: (Long) -> Unit,
    updateLoan: (Long) -> Unit,
    withdrawLoan: (Long) -> Unit,
    viewLoanSummary: (Long) -> Unit,
    viewCharges: () -> Unit,
    viewRepaymentSchedule: (Long) -> Unit,
    viewTransactions: (Long) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (accountId: Long, outstandingBalance: Double?, transferType: String) -> Unit
) {
    composable(
        route = LoanNavigation.LoanDetail.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType })
    ) {
        LoanAccountDetailScreen(
            navigateBack = navigateBack,
            viewGuarantor = viewGuarantor,
            updateLoan = updateLoan,
            withdrawLoan = withdrawLoan,
            viewLoanSummary = viewLoanSummary,
            viewCharges = viewCharges,
            viewRepaymentSchedule = viewRepaymentSchedule,
            viewTransactions = viewTransactions,
            viewQr = viewQr,
            makePayment = makePayment
        )
    }
}

fun NavGraphBuilder.loanApplication(
    navigateBack: () -> Unit,
    reviewNewLoanApplication: (loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) -> Unit,
    submitUpdateLoanApplication: (loanState: LoanState, loansPayloadString: String, loanId: Long?, loanName: String, accountNo: String) -> Unit,
) {
    composable(
        route = LoanNavigation.LoanApplication.route,
        arguments = listOf(
            navArgument(Constants.LOAN_ID) { type = NavType.LongType },
            navArgument(Constants.LOAN_STATE) { type = NavType.EnumType(LoanState::class.java) },
        )
    ) {
        LoanApplicationScreen(
            navigateBack = navigateBack,
            reviewNewLoanApplication = reviewNewLoanApplication,
            submitUpdateLoanApplication = submitUpdateLoanApplication
        )
    }
}

fun NavGraphBuilder.loanSummary(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanSummary.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType })
    ) {
        LoanAccountSummaryScreen(
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.loanTransaction(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanTransaction.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType })
    ) {
        LoanAccountTransactionScreen(
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.loanWithdraw(
    navigateBack: () -> Unit
) {
    composable(
        route = LoanNavigation.LoanWithdraw.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType })
    ) {
        LoanAccountWithdrawScreen (
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.loanRepaymentSchedule(
    navigateBack: () -> Unit
) {
    composable(
        route = LoanNavigation.LoanSchedule.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType })
    ) {
        LoanRepaymentScheduleScreen(
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.loanReview(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanReview.route,
        arguments = listOf(
            navArgument(Constants.LOAN_ID) { type = NavType.LongType },
            navArgument(Constants.LOANS_PAYLOAD) { type = NavType.StringType },
            navArgument(Constants.LOAN_NAME) { type = NavType.StringType },
            navArgument(Constants.ACCOUNT_NUMBER) { type = NavType.StringType },
            navArgument(Constants.LOAN_STATE) { type = NavType.EnumType(LoanState::class.java) }
        )
    ) {
        ReviewLoanApplicationScreen(
            navigateBack = { navigateBack() },
        )
    }
}
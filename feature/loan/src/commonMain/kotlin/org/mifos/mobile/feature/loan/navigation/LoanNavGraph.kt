/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.TransferArgs
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.loanAccount.LoanAccountDetailScreen
import org.mifos.mobile.feature.loan.loanAccountApplication.LoanApplicationScreen
import org.mifos.mobile.feature.loan.loanAccountSummary.LoanAccountSummaryScreen
import org.mifos.mobile.feature.loan.loanAccountTransaction.LoanAccountTransactionScreen
import org.mifos.mobile.feature.loan.loanAccountWithdraw.LoanAccountWithdrawScreen
import org.mifos.mobile.feature.loan.loanRepaymentSchedule.LoanRepaymentScheduleScreen
import org.mifos.mobile.feature.loan.loanReview.ReviewLoanApplicationScreen

fun NavController.navigateToLoanDetailScreen(loanId: Long) {
    navigate(LoanNavigation.LoanDetail.passArguments(loanId = loanId))
}

fun NavController.navigateToLoanApplication() {
    navigate(
        LoanNavigation.LoanApplication.passArguments(
            loanId = -1L,
            loanState = LoanState.CREATE,
        ),
    )
}

fun NavController.navigateToLoanReview(args: LoanReviewArgs) {
    navigate(LoanNavigation.LoanReview.passArguments(args))
}

fun NavGraphBuilder.loanNavGraph(
    navController: NavController,
    viewGuarantor: (Long) -> Unit,
    viewCharges: (ChargeType, Long) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (
        TransferArgs,
    ) -> Unit,
) {
    navigation(
        startDestination = LoanNavigation.LoanDetail.route,
        route = LoanNavigation.LoanBase.route,
    ) {
        loanDetailRoute(
            navigateBack = navController::popBackStack,
            viewGuarantor = viewGuarantor,
            updateLoan = {
                navController.navigate(
                    LoanNavigation.LoanApplication.passArguments(
                        it,
                        LoanState.UPDATE,
                    ),
                )
            },
            withdrawLoan = { navController.navigate(LoanNavigation.LoanWithdraw.passArguments(it)) },
            viewLoanSummary = { navController.navigate(LoanNavigation.LoanSummary.passArguments(it)) },
            viewCharges = { _, chargeTypeId -> viewCharges(ChargeType.LOAN, chargeTypeId) },
            viewRepaymentSchedule = {
                navController.navigate(
                    LoanNavigation.LoanSchedule.passArguments(
                        it,
                    ),
                )
            },
            viewTransactions = {
                navController.navigate(
                    LoanNavigation.LoanTransaction.passArguments(
                        it,
                    ),
                )
            },
            viewQr = viewQr,
            makePayment = makePayment,
        )

        loanApplication(
            navigateBack = navController::popBackStack,
            reviewNewLoanApplication = { loanState, loansPayload, loanId, loanName, accountNo ->
                navController.navigateToLoanReview(
                    LoanReviewArgs(
                        loanState = loanState,
                        loanId = loanId,
                        loanName = loanName,
                        accountNo = accountNo,
                        loansPayloadJson = Json.encodeToString(loansPayload),
                    ),
                )
            },
            submitUpdateLoanApplication = { loanState, loansPayload, loanId, loanName, accountNo ->
                navController.navigateToLoanReview(
                    LoanReviewArgs(
                        loanState = loanState,
                        loanId = loanId,
                        loanName = loanName,
                        accountNo = accountNo,
                        loansPayloadJson = Json.encodeToString(loansPayload),
                    ),
                )
            },
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
    viewCharges: (ChargeType, Long) -> Unit,
    viewRepaymentSchedule: (Long) -> Unit,
    viewTransactions: (Long) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (
        TransferArgs,
    ) -> Unit,
) {
    composable(
        route = LoanNavigation.LoanDetail.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType }),
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
            makePayment = makePayment,
        )
    }
}

fun NavGraphBuilder.loanApplication(
    navigateBack: () -> Unit,
    reviewNewLoanApplication: (
        loanState: LoanState,
        loansPayloadString: LoansPayload,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
    submitUpdateLoanApplication: (
        loanState: LoanState,
        loansPayloadString: LoansPayload,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
) {
    composable(
        route = LoanNavigation.LoanApplication.route,
        arguments = listOf(
            navArgument(Constants.LOAN_ID) { type = NavType.LongType },
            navArgument(Constants.LOAN_STATE) { type = NavType.StringType },

        ),
    ) {
        LoanApplicationScreen(
            navigateBack = navigateBack,
            reviewNewLoanApplication = reviewNewLoanApplication,
            submitUpdateLoanApplication = submitUpdateLoanApplication,
        )
    }
}

fun NavGraphBuilder.loanSummary(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanSummary.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType }),
    ) {
        LoanAccountSummaryScreen(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.loanTransaction(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanTransaction.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType }),
    ) {
        LoanAccountTransactionScreen(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.loanWithdraw(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanWithdraw.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType }),
    ) {
        LoanAccountWithdrawScreen(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.loanRepaymentSchedule(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanSchedule.route,
        arguments = listOf(navArgument(Constants.LOAN_ID) { type = NavType.LongType }),
    ) {
        LoanRepaymentScheduleScreen(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.loanReview(
    navigateBack: () -> Unit,
) {
    composable(
        route = LoanNavigation.LoanReview.route,
        arguments = listOf(navArgument(LoanRoute.LOAN_REVIEW_ARGS) { type = NavType.StringType }),
    ) { backStackEntry ->
        val jsonArgs = backStackEntry.arguments?.getString(LoanRoute.LOAN_REVIEW_ARGS)
        val loanReviewArgs = jsonArgs?.let { LoanReviewArgs.fromJson(it) }

        loanReviewArgs?.let {
            ReviewLoanApplicationScreen(
                navigateBack = { navigateBack() },
            )
        }
    }
}

@Serializable
data class LoanReviewArgs(
    val loanState: LoanState,
    val loanId: Long?,
    val loanName: String,
    val accountNo: String,
    val loansPayloadJson: String?,
) {
    val loansPayload: LoansPayload?
        get() = loansPayloadJson?.let { Json.decodeFromString<LoansPayload>(it) }

    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): LoanReviewArgs = Json.decodeFromString(json)
    }
}

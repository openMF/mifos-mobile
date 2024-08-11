package org.mifos.mobile.feature.loan.navigation

import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.ACCOUNT_NUMBER
import org.mifos.mobile.core.common.Constants.LOANS_PAYLOAD
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.common.Constants.LOAN_NAME
import org.mifos.mobile.core.common.Constants.LOAN_STATE
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_APPLICATION_SCREEN_ROUTE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_DETAIL_SCREEN_ROUTE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_NAVIGATION_ROUTE_BASE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_REVIEW_SCREEN_ROUTE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_SCHEDULE_SCREEN_ROUTE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_SUMMARY_SCREEN_ROUTE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_TRANSACTION_SCREEN_ROUTE
import org.mifos.mobile.feature.loan.navigation.LoanRoute.LOAN_WITHDRAW_SCREEN_ROUTE

sealed class LoanNavigation(val route: String) {
    data object LoanBase: LoanNavigation(route = LOAN_NAVIGATION_ROUTE_BASE)

    data object LoanDetail: LoanNavigation(route = "$LOAN_DETAIL_SCREEN_ROUTE/{$LOAN_ID}") {
        fun passArguments(loanId: Long) = "$LOAN_DETAIL_SCREEN_ROUTE/${loanId}"
    }

    data object LoanApplication: LoanNavigation(route = "$LOAN_APPLICATION_SCREEN_ROUTE/{$LOAN_ID}/{$LOAN_STATE}") {
        fun passArguments(loanId: Long, loanState: LoanState) = "$LOAN_APPLICATION_SCREEN_ROUTE/${loanId}/${loanState}"
    }

    data object LoanSummary: LoanNavigation(route = "$LOAN_SUMMARY_SCREEN_ROUTE/{$LOAN_ID}") {
        fun passArguments(loanId: Long) = "$LOAN_SUMMARY_SCREEN_ROUTE/${loanId}"
    }

    data object LoanTransaction: LoanNavigation(route = "$LOAN_TRANSACTION_SCREEN_ROUTE/{$LOAN_ID}") {
        fun passArguments(loanId: Long) = "$LOAN_TRANSACTION_SCREEN_ROUTE/${loanId}"
    }

    data object LoanWithdraw: LoanNavigation(route = "$LOAN_WITHDRAW_SCREEN_ROUTE/{$LOAN_ID}") {
        fun passArguments(loanId: Long) = "$LOAN_WITHDRAW_SCREEN_ROUTE/${loanId}"
    }

    data object LoanSchedule: LoanNavigation(route = "$LOAN_SCHEDULE_SCREEN_ROUTE/{$LOAN_ID}") {
        fun passArguments(loanId: Long) = "$LOAN_SCHEDULE_SCREEN_ROUTE/${loanId}"
    }

    data object LoanReview : LoanNavigation(
        route = "$LOAN_REVIEW_SCREEN_ROUTE/{$LOAN_STATE}/{${LOANS_PAYLOAD}}/{$LOAN_ID}/{$LOAN_NAME}/{$ACCOUNT_NUMBER}"
    ) {
        fun passArguments(
            loanState: LoanState,
            loansPayload: String,
            loanId: Long? = null,
            loanName: String,
            accountNo: String
        ): String {
            return "$LOAN_REVIEW_SCREEN_ROUTE/$loanState/$loansPayload/$loanId/$loanName/$accountNo"
        }
    }
}

object LoanRoute {
    const val LOAN_NAVIGATION_ROUTE_BASE = "loan_route"
    const val LOAN_DETAIL_SCREEN_ROUTE = "loan_detail_screen_route"
    const val LOAN_APPLICATION_SCREEN_ROUTE = "loan_application_screen_route"
    const val LOAN_SUMMARY_SCREEN_ROUTE = "loan_summary_screen_route"
    const val LOAN_TRANSACTION_SCREEN_ROUTE = "loan_transaction_screen_route"
    const val LOAN_WITHDRAW_SCREEN_ROUTE = "loan_withdraw_screen_route"
    const val LOAN_SCHEDULE_SCREEN_ROUTE = "loan_schedule_screen_route"
    const val LOAN_REVIEW_SCREEN_ROUTE = "loan_review_screen_route"
}
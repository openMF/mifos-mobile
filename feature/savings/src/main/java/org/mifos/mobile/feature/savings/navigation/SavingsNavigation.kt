package org.mifos.mobile.feature.savings.navigation

import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.ACCOUNT_ID
import org.mifos.mobile.core.common.Constants.OUTSTANDING_BALANCE
import org.mifos.mobile.core.common.Constants.SAVINGS_ACCOUNT_STATE
import org.mifos.mobile.core.common.Constants.SAVINGS_ID
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.model.enums.SavingsAccountState

const val SAVINGS_NAVIGATION_ROUTE_BASE = "savings_route"
const val SAVINGS_DETAIL_SCREEN_ROUTE = "savings_detail_screen_route"
const val SAVINGS_APPLICATION_SCREEN_ROUTE = "savings_application_screen_route"
const val SAVINGS_TRANSACTION_SCREEN_ROUTE = "savings_transaction_screen_route"
const val SAVINGS_WITHDRAW_SCREEN_ROUTE = "savings_withdraw_screen_route"
const val SAVINGS_MAKE_TRANSFER_SCREEN_ROUTE = "savings_make_transfer_screen_route"

sealed class SavingsNavigation(val route: String) {
    data object SavingsBase : SavingsNavigation(
        route = SAVINGS_NAVIGATION_ROUTE_BASE
    )

    data object SavingsDetail : SavingsNavigation(
        route = "$SAVINGS_DETAIL_SCREEN_ROUTE/{$SAVINGS_ID}"
    ) {
        fun passArguments(savingsId: Long) = "$SAVINGS_DETAIL_SCREEN_ROUTE/$savingsId"
    }

    data object SavingsApplication : SavingsNavigation(
        route = "$SAVINGS_APPLICATION_SCREEN_ROUTE/{${SAVINGS_ID}}/{${SAVINGS_ACCOUNT_STATE}}") {
        fun passArguments(savingsId: Long, savingsAccountState: SavingsAccountState) = "$SAVINGS_APPLICATION_SCREEN_ROUTE/${savingsId}/${savingsAccountState}"
    }

    data object SavingsTransaction : SavingsNavigation(
        route = "$SAVINGS_TRANSACTION_SCREEN_ROUTE/{${SAVINGS_ID}}"
    ) {
        fun passArguments(savingsId: Long): String {
            return "$SAVINGS_TRANSACTION_SCREEN_ROUTE/$savingsId"
        }
    }

    data object SavingsWithdraw : SavingsNavigation(
        route = "$SAVINGS_WITHDRAW_SCREEN_ROUTE/{${SAVINGS_ID}}"
    ) {
        fun passArguments(savingsId: Long): String {
            return "$SAVINGS_WITHDRAW_SCREEN_ROUTE/$savingsId"
        }
    }

    data object SavingsMakeTransfer : SavingsNavigation(
        route = "$SAVINGS_MAKE_TRANSFER_SCREEN_ROUTE/{$ACCOUNT_ID}/{$OUTSTANDING_BALANCE}/{$TRANSFER_TYPE}"
    ) {
        fun passArguments(accountId: Long, outstandingBalance: String? = null, transferType: String): String {
            return "$SAVINGS_MAKE_TRANSFER_SCREEN_ROUTE/$accountId/$outstandingBalance/$transferType"
        }
    }
}

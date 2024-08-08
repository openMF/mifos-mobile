package org.mifos.mobile.feature.account.navigation

import org.mifos.mobile.core.common.Constants.ACCOUNT_TYPE
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.model.enums.ChargeType


// Constants for Routes
const val CLIENT_ACCOUNTS_NAVIGATION_ROUTE_BASE = "client_accounts_base_route"
const val CLIENT_ACCOUNTS_SCREEN_ROUTE = "client_accounts_screen_route"

// Sealed class for Navigation Routes
sealed class ClientAccountsNavigation(val route: String) {
    data object ClientAccountsBase : ClientAccountsNavigation(route = CLIENT_ACCOUNTS_NAVIGATION_ROUTE_BASE)

    data object ClientAccountsScreen : ClientAccountsNavigation(route = "$CLIENT_ACCOUNTS_SCREEN_ROUTE/{$ACCOUNT_TYPE}") {
        fun passArguments(accountType: AccountType) = "$CLIENT_ACCOUNTS_SCREEN_ROUTE/${accountType.name}"
    }
}
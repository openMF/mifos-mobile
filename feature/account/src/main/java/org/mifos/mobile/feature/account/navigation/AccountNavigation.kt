/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.navigation

import org.mifos.mobile.core.common.Constants.ACCOUNT_TYPE
import org.mifos.mobile.core.model.enums.AccountType

// Constants for Routes
const val CLIENT_ACCOUNTS_NAVIGATION_ROUTE_BASE = "client_accounts_base_route"
const val CLIENT_ACCOUNTS_SCREEN_ROUTE = "client_accounts_screen_route"

// Sealed class for Navigation Routes
sealed class ClientAccountsNavigation(val route: String) {
    data object ClientAccountsBase :
        ClientAccountsNavigation(route = CLIENT_ACCOUNTS_NAVIGATION_ROUTE_BASE)

    data object ClientAccountsScreen :
        ClientAccountsNavigation(route = "$CLIENT_ACCOUNTS_SCREEN_ROUTE/{$ACCOUNT_TYPE}") {
        fun passArguments(accountType: AccountType) =
            "$CLIENT_ACCOUNTS_SCREEN_ROUTE/${accountType.name}"
    }
}

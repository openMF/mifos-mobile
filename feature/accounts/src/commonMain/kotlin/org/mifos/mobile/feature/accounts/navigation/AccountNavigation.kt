/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.navigation

import org.mifos.mobile.core.common.Constants.ACCOUNT_TYPE
import org.mifos.mobile.core.model.enums.AccountType

// Constants for Routes
const val ACCOUNTS_NAVIGATION_ROUTE_BASE = "accounts_base_route"
const val ACCOUNT_SCREEN_ROUTE = "account_screen_route"

// Sealed class for navigation route
sealed class AccountsNavigation(val route: String) {
    data object AccountsBase : AccountsNavigation(route = ACCOUNTS_NAVIGATION_ROUTE_BASE)
    data object AccountsScreen : AccountsNavigation(route = "ACCOUNT_SCREEN_ROUTE/{$ACCOUNT_TYPE}") {
        fun passArguments(accountType: AccountType) =
            "$ACCOUNT_SCREEN_ROUTE/${accountType.name}"
    }
}

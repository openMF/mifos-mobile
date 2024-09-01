/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transaction.navigation

const val RECENT_TRANSACTION_NAVIGATION_ROUTE_BASE = "recent_transaction_base_route"
const val RECENT_TRANSACTION_SCREEN_ROUTE = "recent_transaction_screen_route"

sealed class RecentTransactionNavigation(val route: String) {
    data object RecentTransactionBase : RecentTransactionNavigation(
        route = RECENT_TRANSACTION_NAVIGATION_ROUTE_BASE,
    )

    data object RecentTransactionScreen : RecentTransactionNavigation(
        route = RECENT_TRANSACTION_SCREEN_ROUTE,
    )
}

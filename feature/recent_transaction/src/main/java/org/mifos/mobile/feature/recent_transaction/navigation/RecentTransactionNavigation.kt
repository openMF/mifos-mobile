package org.mifos.mobile.feature.recent_transaction.navigation

const val RECENT_TRANSACTION_NAVIGATION_ROUTE_BASE = "recent_transaction_base_route"
const val RECENT_TRANSACTION_SCREEN_ROUTE = "recent_transaction_screen_route"

sealed class RecentTransactionNavigation(val route: String) {
    data object RecentTransactionBase : RecentTransactionNavigation(route = RECENT_TRANSACTION_NAVIGATION_ROUTE_BASE)
    data object RecentTransactionScreen : RecentTransactionNavigation(route = RECENT_TRANSACTION_SCREEN_ROUTE)
}

package org.mifos.mobile.feature.transfer.process.navigation

// Constants for Routes
const val TRANSFER_PROCESS_NAVIGATION_ROUTE_BASE = "transfer_process_base_route"
const val TRANSFER_PROCESS_SCREEN_ROUTE = "transfer_process_screen_route"

// Sealed class for Navigation Routes
sealed class TransferProcessNavigation(val route: String) {
    data object TransferProcessBase : TransferProcessNavigation(route = TRANSFER_PROCESS_NAVIGATION_ROUTE_BASE)
    data object TransferProcessScreen : TransferProcessNavigation(route = TRANSFER_PROCESS_SCREEN_ROUTE)
}
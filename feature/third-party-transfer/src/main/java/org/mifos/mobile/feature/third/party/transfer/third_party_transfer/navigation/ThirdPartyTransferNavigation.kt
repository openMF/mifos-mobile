package org.mifos.mobile.feature.third.party.transfer.third_party_transfer.navigation

const val THIRD_PARTY_TRANSFER_NAVIGATION_ROUTE_BASE = "third_party_transfer_base_route"
const val THIRD_PARTY_TRANSFER_SCREEN_ROUTE = "third_party_transfer_screen_route"

sealed class ThirdPartyTransferNavigation(val route: String) {
    data object ThirdPartyTransferBase : ThirdPartyTransferNavigation(route = THIRD_PARTY_TRANSFER_NAVIGATION_ROUTE_BASE)
    data object ThirdPartyTransferScreen : ThirdPartyTransferNavigation(route = THIRD_PARTY_TRANSFER_SCREEN_ROUTE)
}

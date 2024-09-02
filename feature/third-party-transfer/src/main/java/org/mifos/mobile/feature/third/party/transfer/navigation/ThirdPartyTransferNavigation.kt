/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer.navigation

const val THIRD_PARTY_TRANSFER_NAVIGATION_ROUTE_BASE = "third_party_transfer_base_route"
const val THIRD_PARTY_TRANSFER_SCREEN_ROUTE = "third_party_transfer_screen_route"

sealed class ThirdPartyTransferNavigation(val route: String) {
    data object ThirdPartyTransferBase : ThirdPartyTransferNavigation(
        route = THIRD_PARTY_TRANSFER_NAVIGATION_ROUTE_BASE,
    )

    data object ThirdPartyTransferScreen : ThirdPartyTransferNavigation(
        route = THIRD_PARTY_TRANSFER_SCREEN_ROUTE,
    )
}

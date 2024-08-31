/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.navigation

import org.mifos.mobile.core.common.Constants.CHARGE_TYPE
import org.mifos.mobile.core.model.enums.ChargeType

// Constants for Routes
const val CLIENT_CHARGE_NAVIGATION_ROUTE_BASE = "client_charge_base_route"
const val CLIENT_CHARGE_SCREEN_ROUTE = "client_charge_screen_route"

// Sealed class for Navigation Routes
sealed class ClientChargeNavigation(var route: String) {
    data object ClientChargeBase : ClientChargeNavigation(
        route = "$CLIENT_CHARGE_NAVIGATION_ROUTE_BASE/{$CHARGE_TYPE}",
    ) {
        fun passArguments(chargeType: ChargeType) =
            "$CLIENT_CHARGE_NAVIGATION_ROUTE_BASE/${chargeType.name}"
    }

    data object ClientChargeScreen : ClientChargeNavigation(
        route = "$CLIENT_CHARGE_SCREEN_ROUTE/{$CHARGE_TYPE}",
    ) {
        fun passArguments(chargeType: ChargeType) = "$CLIENT_CHARGE_SCREEN_ROUTE/${chargeType.name}"
    }
}

package org.mifos.mobile.feature.client_charge.navigation

import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE
import org.mifos.mobile.core.model.enums.ChargeType

// Constants for Routes
const val CLIENT_CHARGE_NAVIGATION_ROUTE_BASE = "client_charge_base_route"
const val CLIENT_CHARGE_SCREEN_ROUTE = "client_charge_screen_route"

// Sealed class for Navigation Routes
sealed class ClientChargeNavigation(var route: String) {
    data object ClientChargeBase : ClientChargeNavigation(route = "$CLIENT_CHARGE_NAVIGATION_ROUTE_BASE/{$CHARGE_TYPE}") {
        fun passArguments(chargeType: ChargeType) = "$CLIENT_CHARGE_NAVIGATION_ROUTE_BASE/${chargeType.name}"
    }

    data object ClientChargeScreen : ClientChargeNavigation(route = "$CLIENT_CHARGE_SCREEN_ROUTE")
}
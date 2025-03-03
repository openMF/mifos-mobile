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
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE_ID
import org.mifos.mobile.core.model.enums.ChargeType

// Constants for Routes
const val CHARGE_ROUTE_BASE = "charge_route_base"
const val CHARGE_ROUTE_SCREEN = "charge_route_screen"

// Sealed class for Navigation Routes
sealed class ChargeNavigation(var route: String) {

    data object ChargeRouteBase : ChargeNavigation(
        route = "$CHARGE_ROUTE_BASE/{$CHARGE_TYPE}/${CHARGE_TYPE_ID}",
    )

    data object ChargeRouteScreen : ChargeNavigation(
        route = "$CHARGE_ROUTE_SCREEN/{$CHARGE_TYPE}/{$CHARGE_TYPE_ID}",
    ) {
        fun passArguments(chargeType: ChargeType, chargeTypeId: Long?): String {
            return "$CHARGE_ROUTE_SCREEN/${chargeType.name}/$chargeTypeId"
        }
    }
}

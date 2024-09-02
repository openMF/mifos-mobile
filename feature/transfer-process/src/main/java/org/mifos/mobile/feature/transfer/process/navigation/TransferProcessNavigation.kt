/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.navigation

import org.mifos.mobile.core.common.Constants.PAYLOAD
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.model.enums.TransferType

// Constants for Routes
const val TRANSFER_PROCESS_NAVIGATION_ROUTE_BASE = "transfer_process_base_route"
const val TRANSFER_PROCESS_SCREEN_ROUTE = "transfer_process_screen_route"

// Sealed class for Navigation Routes
internal sealed class TransferProcessNavigation(val route: String) {
    data object TransferProcessBase : TransferProcessNavigation(
        route = TRANSFER_PROCESS_NAVIGATION_ROUTE_BASE,
    )

    data object TransferProcessScreen : TransferProcessNavigation(
        route = "$TRANSFER_PROCESS_SCREEN_ROUTE/{$PAYLOAD}/{$TRANSFER_TYPE}",
    ) {
        fun passArguments(
            payload: String,
            transferType: TransferType,
        ) = "$TRANSFER_PROCESS_SCREEN_ROUTE/$payload/$transferType"
    }
}

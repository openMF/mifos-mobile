package org.mifos.mobile.feature.transfer.process.navigation

import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.PAYLOAD
import org.mifos.mobile.core.common.Constants.TRANSFER_TYPE
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType

// Constants for Routes
const val TRANSFER_PROCESS_NAVIGATION_ROUTE_BASE = "transfer_process_base_route"
const val TRANSFER_PROCESS_SCREEN_ROUTE = "transfer_process_screen_route"

// Sealed class for Navigation Routes
sealed class TransferProcessNavigation(val route: String) {
    data object TransferProcessBase : TransferProcessNavigation(route = TRANSFER_PROCESS_NAVIGATION_ROUTE_BASE)
    data object TransferProcessScreen : TransferProcessNavigation(route = "$TRANSFER_PROCESS_SCREEN_ROUTE/{${PAYLOAD}}/{$TRANSFER_TYPE}") {
        fun passArguments(payload: String, transferType: TransferType) = "$TRANSFER_PROCESS_SCREEN_ROUTE/${payload}/${transferType}"
    }
}
package org.mifos.mobile.feature.qr.navigation

import org.mifos.mobile.feature.qr.navigation.QrRoute.QR_DISPLAY_SCREEN_ROUTE
import org.mifos.mobile.feature.qr.navigation.QrRoute.QR_IMPORT_SCREEN_ROUTE
import org.mifos.mobile.feature.qr.navigation.QrRoute.QR_READER_SCREEN_ROUTE

sealed class QrNavigation(val route: String) {
    data object Reader : QrNavigation(route = QR_READER_SCREEN_ROUTE)
    data object Display : QrNavigation(route = QR_DISPLAY_SCREEN_ROUTE)
    data object Import : QrNavigation(route = QR_IMPORT_SCREEN_ROUTE)
}
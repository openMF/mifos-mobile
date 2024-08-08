package org.mifos.mobile.feature.qr.navigation

const val QR_NAVIGATION_ROUTE = "qr_route"
const val QR_READER_SCREEN_ROUTE = "qr_reader_screen"
const val QR_DISPLAY_SCREEN_ROUTE = "qr_display_screen"
const val QR_IMPORT_SCREEN_ROUTE = "qr_import_screen"
const val QR_ARGS = "qr_args"

sealed class QrNavigation(val route: String) {
    data object QrBase: QrNavigation(route = QR_NAVIGATION_ROUTE)
    data object QrReaderScreen : QrNavigation(route = QR_READER_SCREEN_ROUTE)
    data object QrDisplayScreen : QrNavigation(route = "$QR_DISPLAY_SCREEN_ROUTE/{$QR_ARGS}") {
        fun passArguments(qrString: String) = "$QR_DISPLAY_SCREEN_ROUTE/{$qrString}"
    }
    data object QrImportScreen : QrNavigation(route = QR_IMPORT_SCREEN_ROUTE)
}
/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.navigation

const val QR_NAVIGATION_ROUTE = "qr_route"
const val QR_READER_SCREEN_ROUTE = "qr_reader_screen"
const val QR_DISPLAY_SCREEN_ROUTE = "qr_display_screen"
const val QR_IMPORT_SCREEN_ROUTE = "qr_import_screen"
const val QR_ARGS = "qr_args"

sealed class QrNavigation(val route: String) {
    data object QrBase : QrNavigation(route = QR_NAVIGATION_ROUTE)
    data object QrReaderScreen : QrNavigation(route = QR_READER_SCREEN_ROUTE)
    data object QrDisplayScreen : QrNavigation(route = "$QR_DISPLAY_SCREEN_ROUTE/{$QR_ARGS}") {
        fun passArguments(qrString: String) = "$QR_DISPLAY_SCREEN_ROUTE/{$qrString}"
    }

    data object QrImportScreen : QrNavigation(route = QR_IMPORT_SCREEN_ROUTE)
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.qr.qr.QrCodeReaderViewModel
import org.mifos.mobile.feature.qr.qrCodeDisplay.QrCodeDisplayViewModel
import org.mifos.mobile.feature.qr.qrCodeImport.QrCodeImportViewModel

val QrModule = module {
    viewModelOf(::QrCodeImportViewModel)
    viewModelOf(::QrCodeReaderViewModel)
    viewModelOf(::QrCodeDisplayViewModel)
}

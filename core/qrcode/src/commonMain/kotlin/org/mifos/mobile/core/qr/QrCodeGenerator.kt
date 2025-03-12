/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary

/**
 * Generate a QR Code and return platform-specific representation.
 * @param str Data to be stored in QR Code.
 * @return Platform-specific QR Code representation.
 */
expect fun generateQrCode(str: String): ImageBitmap?

expect fun decodeQrCode(bitmap: ImageBitmap): String?

fun getAccountDetailsInString(
    accountNumber: Int?,
    officeName: String?,
    accountType: String,
): String {
    val payload = Beneficiary(accountNumber, officeName, accountType)
    return Json.encodeToString(payload)
}

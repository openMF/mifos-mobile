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
import org.mifos.mobile.core.model.entity.templates.account.AccountType

expect fun decodeQrCode(bitmap: ImageBitmap): String?

fun getAccountDetailsInString(
    clientName: String,
    accountNumber: String,
    accountType: AccountType,
    officeName: String,
): String {
    val payload = Beneficiary(
        clientName = clientName,
        accountNumber = accountNumber,
        accountType = accountType,
        officeName = officeName,
    )
    return Json.encodeToString(payload)
}

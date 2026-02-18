/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.network.dto.payloads.TransferPayloadDto

fun TransferPayload.toDto(): TransferPayloadDto =
    TransferPayloadDto(
        fromOfficeId = fromOfficeId,
        fromClientId = fromClientId,
        fromAccountType = fromAccountType,
        fromAccountId = fromAccountId,
        toOfficeId = toOfficeId,
        toClientId = toClientId,
        toAccountType = toAccountType,
        toAccountId = toAccountId,
        transferDate = transferDate,
        transferAmount = transferAmount,
        transferDescription = transferDescription,
        dateFormat = dateFormat,
        locale = locale,
    )

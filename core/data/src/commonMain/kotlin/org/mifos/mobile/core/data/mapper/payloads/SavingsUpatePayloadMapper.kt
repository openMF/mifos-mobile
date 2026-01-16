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

import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountUpdatePayloadDto

fun SavingsAccountUpdatePayload.toDto(): SavingsAccountUpdatePayloadDto =
    SavingsAccountUpdatePayloadDto(
        clientId = clientId,
        productId = productId,
    )

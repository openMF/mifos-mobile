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

import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryCreatePayloadDto

fun BeneficiaryPayload.toDto(): BeneficiaryCreatePayloadDto =
    BeneficiaryCreatePayloadDto(
        locale = locale,
        name = name,
        accountNumber = accountNumber,
        accountType = accountType,
        transferLimit = transferLimit,
        officeName = officeName,
    )

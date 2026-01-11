/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.beneficiary

import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.network.dto.beneficiary.BeneficiaryListResponseDto
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

fun BeneficiaryListResponseDto.toModel(): Beneficiary =
    Beneficiary(
        id = id,
        name = name,
        officeName = officeName,
        clientName = clientName,
        accountType = accountType?.toAccountType(),
        accountNumber = accountNumber,
        transferLimit = transferLimit,
    )

fun TypeResponseDto.toAccountType(): AccountType =
    AccountType(
        id = id,
        code = code,
        value = value,
    )

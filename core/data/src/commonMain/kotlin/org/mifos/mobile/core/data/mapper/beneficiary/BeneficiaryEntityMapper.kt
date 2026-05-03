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

import org.mifos.mobile.core.database.entity.BeneficiaryEntity
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType

fun BeneficiaryEntity.toModel(): Beneficiary =
    Beneficiary(
        id = beneficiaryId,
        name = name,
        officeName = officeName,
        clientName = null,
        accountType = AccountType(value = accountType),
        accountNumber = accountNumber,
        transferLimit = transferLimit,
    )

fun Beneficiary.toEntity(): BeneficiaryEntity =
    BeneficiaryEntity(
        beneficiaryId = id ?: 0L,
        name = name.orEmpty(),
        officeName = officeName.orEmpty(),
        accountType = accountType?.value.orEmpty(),
        accountNumber = accountNumber.orEmpty(),
        transferLimit = transferLimit ?: 0.0,
        lastFetchedAt = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    )

/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.pocket

import org.mifos.mobile.core.database.entity.PocketAccountEntity
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType

fun PocketAccountEntity.toDomain() = PocketAccount(
    id = this.id,
    pocketId = this.pocketId,
    accountId = this.accountId,
    accountType = AccountType.valueOf(this.accountType),
    accountNumber = this.accountNumber,
)

fun PocketAccount.toEntity() = PocketAccountEntity(
    id = this.id,
    pocketId = this.pocketId,
    accountId = this.accountId,
    accountType = this.accountType.name,
    accountNumber = this.accountNumber,
)

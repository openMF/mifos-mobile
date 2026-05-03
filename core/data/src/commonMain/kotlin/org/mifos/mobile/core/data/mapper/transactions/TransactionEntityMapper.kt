/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.transactions

import org.mifos.mobile.core.database.entity.TransactionEntity
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.client.Type
import kotlin.time.Clock

fun TransactionEntity.toModel(): Transaction =
    Transaction(
        id = transactionId,
        officeId = null,
        officeName = null,
        type = Type(value = type),
        date = date.split("-").mapNotNull { it.toIntOrNull() },
        currency = null,
        amount = amount,
        submittedOnDate = emptyList(),
        reversed = false,
    )

fun Transaction.toEntity(clientId: Long): TransactionEntity =
    TransactionEntity(
        transactionId = id ?: 0L,
        clientId = clientId,
        accountId = 0L,
        amount = amount ?: 0.0,
        date = date.joinToString("-"),
        type = type.value.orEmpty(),
        currencyCode = currency?.code.orEmpty(),
        accountType = "",
        lastFetchedAt = Clock.System.now().toEpochMilliseconds(),
    )

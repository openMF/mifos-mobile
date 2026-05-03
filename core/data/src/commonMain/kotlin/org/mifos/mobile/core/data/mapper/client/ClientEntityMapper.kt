/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.client

import org.mifos.mobile.core.database.entity.ClientEntity
import org.mifos.mobile.core.model.entity.client.Client
import kotlin.time.Clock

fun ClientEntity.toModel(): Client =
    Client(
        id = clientId.toInt(),
        accountNo = accountNo,
        displayName = displayName,
        officeName = officeName,
        isImagePresent = imagePresent,
        active = active,
    )

fun Client.toEntity(): ClientEntity =
    ClientEntity(
        clientId = id.toLong(),
        accountNo = accountNo.orEmpty(),
        displayName = displayName.orEmpty(),
        officeName = officeName.orEmpty(),
        imagePresent = isImagePresent,
        externalId = null,
        status = "Active",
        active = true,
        activationDate = null,
        lastFetchedAt = Clock.System.now().toEpochMilliseconds(),
    )

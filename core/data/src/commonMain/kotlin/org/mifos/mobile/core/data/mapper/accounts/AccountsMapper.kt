/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.accounts

import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto

fun AccountsResponseDto.toModel(): ClientAccounts =
    ClientAccounts(
        loanAccounts = loanAccounts.map { it.toModel() },
        savingsAccounts = savingsAccounts?.map { it.toModel() },
        shareAccounts = shareAccounts.map { it.toModel() },
    )

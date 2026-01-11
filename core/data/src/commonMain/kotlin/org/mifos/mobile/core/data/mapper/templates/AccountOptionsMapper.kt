/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.templates

import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsTemplateResponseDto

fun AccountOptionsTemplateResponseDto.toModel(): AccountOptionsTemplate =
    AccountOptionsTemplate(
        fromAccountOptions = fromAccountOptions.map { it.toModel() },
        toAccountOptions = toAccountOptions.map { it.toModel() },
    )

fun AccountOptionsResponseDto.toModel(): AccountOption =
    AccountOption(
        accountId = accountId,
        accountNo = accountNo,
        accountType = accountType?.toAccountType(),
        clientId = clientId,
        clientName = clientName,
        officeId = officeId,
        officeName = officeName,
    )

fun TypeResponseDto.toAccountType(): AccountType =
    AccountType(
        id = id,
        code = code,
        value = value,
    )

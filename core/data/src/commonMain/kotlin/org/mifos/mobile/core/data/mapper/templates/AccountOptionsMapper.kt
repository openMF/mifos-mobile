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
        toAccountOptions = toAccountOptions.map { it.toModel() }
    )

fun AccountOptionsResponseDto.toModel(): AccountOption =
    AccountOption(
        accountId = accountId,
        accountNo = accountNo,
        accountType = accountType?.toAccountType(),
        clientId = clientId,
        clientName = clientName,
        officeId = officeId,
        officeName = officeName
    )

fun TypeResponseDto.toAccountType(): AccountType =
    AccountType(
        id = id,
        code = code,
        value = value
    )
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
        transferLimit = transferLimit
    )

fun TypeResponseDto.toAccountType(): AccountType =
    AccountType(
        id = id,
        code = code,
        value = value
    )
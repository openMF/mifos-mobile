package org.mifos.mobile.core.data.mapper.templates

import org.mifos.mobile.core.model.entity.templates.beneficiary.AccountTypeOption
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.templates.beneficiary.BeneficiaryTemplateDto

fun BeneficiaryTemplateDto.toModel(): BeneficiaryTemplate =
    BeneficiaryTemplate(
        accountTypeOptions = accountTypeOptions?.map { it.toAccountTypeOption() }
    )


fun TypeResponseDto.toAccountTypeOption(): AccountTypeOption =
    AccountTypeOption(
        id = id,
        code = code,
        value = value
    )
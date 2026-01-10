package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryCreatePayloadDto

fun BeneficiaryPayload.toDto() : BeneficiaryCreatePayloadDto =
    BeneficiaryCreatePayloadDto(
        locale = locale,
        name = name,
        accountNumber = accountNumber,
        accountType = accountType,
    )
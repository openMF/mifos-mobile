package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.network.dto.payloads.GuarantorApplicationPayloadDto

fun GuarantorApplicationPayload.toDto() : GuarantorApplicationPayloadDto =
    GuarantorApplicationPayloadDto(
        guarantorTypeId = guarantorTypeId,
        firstName = firstName,
        lastName = lastName,
        city = city,
    )
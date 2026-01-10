package org.mifos.mobile.core.data.mapper.guarantor

import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorType
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.guarantor.GuarantorTemplateResponseDto

fun GuarantorTemplateResponseDto.toModel() : GuarantorTemplatePayload =
    GuarantorTemplatePayload(
        guarantorType = guarantorType?.toModel(),
        guarantorTypeOptions = guarantorTypeOptions
            ?.map { it.toModel() }
            ?.let { ArrayList(it) }
    )


fun TypeResponseDto.toModel() : GuarantorType =
    GuarantorType(
        id = id?.toLong(),
        value = value,
        code = code
    )

package org.mifos.mobile.core.network.dto.guarantor


import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class GuarantorTemplateResponseDto(
    val guarantorType: TypeResponseDto? = null,
    val guarantorTypeOptions: ArrayList<TypeResponseDto>? = null,
)

package org.mifos.mobile.core.network.dto.templates.beneficiary

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class BeneficiaryTemplateDto(
    val accountTypeOptions: List<TypeResponseDto>? = null,
)
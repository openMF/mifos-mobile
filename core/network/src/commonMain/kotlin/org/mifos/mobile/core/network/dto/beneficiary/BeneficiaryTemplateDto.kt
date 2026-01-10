package org.mifos.mobile.core.network.dto.beneficiary

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class BeneficiaryTemplateDto(
    val accountTypeOptions: List<TypeResponseDto>? = null,
)
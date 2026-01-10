package org.mifos.mobile.core.network.dto.beneficiary

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class BeneficiaryListResponseDto(
    val id: Long? = null,

    val name: String? = null,

    val officeName: String? = null,

    val clientName: String? = null,

    val accountType: TypeResponseDto? = null,

    val accountNumber: String? = null,

    val transferLimit: Double? = null,
)

package org.mifos.mobile.core.network.dto.guarantor

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.RawValue

@Serializable
data class GuarantorListResponseDto(

    val id: Long? = 0,

    val city: String? = null,

    val lastname: String? = null,

    val guarantorType: @RawValue GuarantorTypeResponseDto? = null,

    val firstname: String? = null,

    val joinedDate: List<Int>? = null,

    val loanId: Long? = null,

    val status: Boolean? = true,
)

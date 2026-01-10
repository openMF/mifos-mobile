package org.mifos.mobile.core.network.dto.shareAccount

import kotlinx.serialization.Serializable

@Serializable
data class ShareStatusResponseDto(

    val id: Int? = null,

    val code: String? = null,

    val value: String? = null,

    val submittedAndPendingApproval: Boolean? = null,

    val approved: Boolean? = null,

    val rejected: Boolean? = null,

    val active: Boolean? = null,

    val closed: Boolean? = null,

)
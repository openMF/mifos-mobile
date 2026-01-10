package org.mifos.mobile.core.network.dto.shareAccount

import kotlinx.serialization.Serializable

@Serializable
data class ShareTimelineResponseDto(

    val submittedOnDate: List<Int>? = null,

    val submittedByUsername: String? = null,

    val submittedByFirstname: String? = null,

    val submittedByLastname: String? = null,

    val approvedDate: List<Int>? = null,

    val approvedByUsername: String? = null,

    val approvedByFirstname: String? = null,

    val approvedByLastname: String? = null,

    val activatedDate: List<Int>? = null,

    val activatedByUsername: String? = null,

    val activatedByFirstname: String? = null,

    val activatedByLastname: String? = null,

)
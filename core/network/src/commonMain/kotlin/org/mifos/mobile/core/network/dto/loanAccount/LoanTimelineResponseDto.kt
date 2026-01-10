package org.mifos.mobile.core.network.dto.loanAccount

import kotlinx.serialization.Serializable

@Serializable
data class LoanTimelineResponseDto(
    val submittedOnDate: List<Int>? = null,

    val submittedByUsername: String?,

    val submittedByFirstname: String?,

    val submittedByLastname: String?,

    val approvedOnDate: List<Int>? = null,

    val approvedByUsername: String?,

    val approvedByFirstname: String?,

    val approvedByLastname: String?,

    val expectedDisbursementDate: List<Int>? = null,

    val actualDisbursementDate: List<Int>? = null,

    val disbursedByUsername: String?,

    val disbursedByFirstname: String?,

    val disbursedByLastname: String?,

    val closedOnDate: List<Int>? = null,

    val expectedMaturityDate: List<Int>? = null,

    val withdrawnOnDate: List<Int>? = null,

)
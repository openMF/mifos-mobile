package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable

@Serializable
data class SavingsTimeLineResponseDto(
    val submittedOnDate: List<Int> = emptyList(),

    val submittedByUsername: String?,

    val submittedByFirstname: String?,

    val submittedByLastname: String?,

    val approvedOnDate: List<Int> = emptyList(),

    val approvedByUsername: String?,

    val approvedByFirstname: String?,

    val approvedByLastname: String?,

    val activatedOnDate: List<Int>? = null,

    val activatedByUsername: String?,

    val activatedByFirstname: String?,

    val activatedByLastname: String?,

    val closedOnDate: List<Int> = emptyList(),

)
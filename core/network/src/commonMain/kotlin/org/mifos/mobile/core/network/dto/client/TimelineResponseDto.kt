package org.mifos.mobile.core.network.dto.client

import kotlinx.serialization.Serializable

@Serializable
data class TimelineResponseDto(
    val submittedOnDate: List<Int> = emptyList(),
    val submittedByUsername: String? = null,
    val submittedByFirstname: String? = null,
    val submittedByLastname: String? = null,
    val activatedOnDate: List<Int> = emptyList(),
    val activatedByUsername: String? = null,
    val activatedByFirstname: String? = null,
    val activatedByLastname: String? = null,
    val closedOnDate: List<Int> = emptyList(),
    val closedByUsername: String? = null,
    val closedByFirstname: String? = null,
    val closedByLastname: String? = null,
)
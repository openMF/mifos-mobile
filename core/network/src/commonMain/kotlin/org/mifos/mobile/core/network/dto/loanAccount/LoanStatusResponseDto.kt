package org.mifos.mobile.core.network.dto.loanAccount

import kotlinx.serialization.Serializable

@Serializable
data class LoanStatusResponseDto(
    val id: Int? = null,

    val code: String? = null,

    val value: String? = null,

    val pendingApproval: Boolean? = null,

    val waitingForDisbursal: Boolean? = null,

    val active: Boolean? = null,

    val closedObligationsMet: Boolean? = null,

    val closedWrittenOff: Boolean? = null,

    val closedRescheduled: Boolean? = null,

    val closed: Boolean? = null,

    val overpaid: Boolean? = null,

)
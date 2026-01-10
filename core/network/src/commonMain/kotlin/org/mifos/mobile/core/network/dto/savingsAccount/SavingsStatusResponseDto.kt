package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable

@Serializable
data class SavingsStatusResponseDto(
    val id: Int? = null,
    val code: String? = null,

    val value: String? = null,

    val submittedAndPendingApproval: Boolean? = null,

    val approved: Boolean? = null,

    val rejected: Boolean? = null,

    val withdrawnByApplicant: Boolean? = null,

    val active: Boolean? = null,

    val closed: Boolean? = null,

    val prematureClosed: Boolean? = null,

    internal val transferInProgress: Boolean? = null,

    internal val transferOnHold: Boolean? = null,

    val matured: Boolean? = null,

)

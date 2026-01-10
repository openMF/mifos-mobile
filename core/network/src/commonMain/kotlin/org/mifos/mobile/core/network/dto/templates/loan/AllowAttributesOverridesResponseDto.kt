package org.mifos.mobile.core.network.dto.templates.loan

import kotlinx.serialization.Serializable

@Serializable
data class AllowAttributesOverridesResponseDto(

    val amortizationType: Boolean? = null,

    val interestType: Boolean? = null,

    val transactionProcessingStrategyId: Boolean? = null,

    val interestCalculationPeriodType: Boolean? = null,

    val inArrearsTolerance: Boolean? = null,

    val repaymentEvery: Boolean? = null,

    val graceOnPrincipalAndInterestPayment: Boolean? = null,

    val graceOnArrearsAgeing: Boolean? = null,

)

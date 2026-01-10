package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable


@Serializable
data class LoanAccountApplicationPayloadDto(

    val clientId: Int? = null,

    val productId: Int? = null,

    val productName: String? = null,

    val principal: Double? = null,

    val loanTermFrequency: Int? = null,

    val loanTermFrequencyType: Int? = null,

    val loanType: String? = null,

    val numberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: Int? = null,

    val interestRatePerPeriod: Double? = null,

    val amortizationType: Int? = null,

    val interestType: Int? = null,

    val interestCalculationPeriodType: Int? = null,

    val transactionProcessingStrategyId: Int? = null,

    val transactionProcessingStrategyCode: String? = null,

    val expectedDisbursementDate: String? = null,

    val submittedOnDate: String? = null,

    val linkAccountId: Int? = null,

    val loanPurposeId: Int? = null,

    val loanPurpose: String? = null,

    val maxOutstandingLoanBalance: Double? = null,

    val currency: String? = null,

    val dateFormat: String? = null,

    val locale: String? = null,
)

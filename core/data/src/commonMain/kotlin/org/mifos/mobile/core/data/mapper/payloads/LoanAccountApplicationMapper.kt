package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.network.dto.payloads.LoanAccountApplicationPayloadDto

fun LoansPayload.toDto(): LoanAccountApplicationPayloadDto =
    LoanAccountApplicationPayloadDto(
        clientId = clientId,
        productId = productId,
        productName = productName,
        principal = principal,
        loanTermFrequency = loanTermFrequency,
        loanTermFrequencyType = loanTermFrequencyType,
        loanType = loanType,
        numberOfRepayments = numberOfRepayments,
        repaymentEvery = repaymentEvery,
        repaymentFrequencyType = repaymentFrequencyType,
        interestRatePerPeriod = interestRatePerPeriod,
        amortizationType = amortizationType,
        interestType = interestType,
        interestCalculationPeriodType = interestCalculationPeriodType,
        transactionProcessingStrategyId = transactionProcessingStrategyId,
        transactionProcessingStrategyCode = transactionProcessingStrategyCode,
        expectedDisbursementDate = expectedDisbursementDate,
        submittedOnDate = submittedOnDate,
        linkAccountId = linkAccountId,
        loanPurposeId = loanPurposeId,
        loanPurpose = loanPurpose,
        maxOutstandingLoanBalance = maxOutstandingLoanBalance,
        currency = currency,
        dateFormat = dateFormat,
        locale = locale
    )

/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
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
        locale = locale,
    )

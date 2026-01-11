/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.accounts

import org.mifos.mobile.core.model.entity.accounts.loan.Currency
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.loan.LoanType
import org.mifos.mobile.core.model.entity.accounts.loan.Status
import org.mifos.mobile.core.model.entity.accounts.loan.Summary
import org.mifos.mobile.core.model.entity.accounts.loan.Timeline
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanAccountResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanStatusResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanSummaryResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanTimelineResponseDto

fun LoanAccountResponseDto.toModel(): LoanAccount =
    LoanAccount(
        id = id,
        loanProductId = loanProductId,
        externalId = externalId,
        numberOfRepayments = numberOfRepayments,
        accountNo = accountNo,
        productName = productName,
        productId = productId,
        loanProductName = loanProductName,
        clientName = clientName,
        loanProductDescription = loanProductDescription,
        principal = principal,
        annualInterestRate = annualInterestRate,
        status = status?.toModel(),
        loanType = loanType?.toModel(),
        loanCycle = loanCycle,
        loanBalance = loanBalance,
        amountPaid = amountPaid,
        currency = currency?.toLoanCurrencyModel(),
        inArrears = inArrears,
        summary = summary?.toModel(),
        loanPurposeName = loanPurposeName,
        timeline = timeline?.toModel(),
    )

fun LoanStatusResponseDto.toModel(): Status =
    Status(
        id = id,
        code = code,
        value = value,
        pendingApproval = pendingApproval,
        waitingForDisbursal = waitingForDisbursal,
        active = active,
        closedObligationsMet = closedObligationsMet,
        closedWrittenOff = closedWrittenOff,
        closedRescheduled = closedRescheduled,
        closed = closed,
        overpaid = overpaid,
    )

fun TypeResponseDto.toModel(): LoanType =
    LoanType(
        id = id,
        code = code,
        value = value,
    )

fun LoanSummaryResponseDto.toModel(): Summary =
    Summary(
        principalDisbursed = principalDisbursed,
        principalPaid = principalPaid,
        interestCharged = interestCharged,
        interestPaid = interestPaid,
        feeChargesCharged = feeChargesCharged,
        penaltyChargesCharged = penaltyChargesCharged,
        penaltyChargesWaived = penaltyChargesWaived,
        totalExpectedRepayment = totalExpectedRepayment,
        interestWaived = interestWaived,
        totalRepayment = totalRepayment,
        feeChargesWaived = feeChargesWaived,
        totalOutstanding = totalOutstanding,
        currency = currency?.toLoanCurrencyModel(),
    )

fun LoanTimelineResponseDto.toModel(): Timeline =
    Timeline(
        submittedOnDate = submittedOnDate,
        submittedByUsername = submittedByUsername,
        submittedByFirstname = submittedByFirstname,
        submittedByLastname = submittedByLastname,
        approvedOnDate = approvedOnDate,
        approvedByUsername = approvedByUsername,
        approvedByFirstname = approvedByFirstname,
        approvedByLastname = approvedByLastname,
        expectedDisbursementDate = expectedDisbursementDate,
        actualDisbursementDate = actualDisbursementDate,
        disbursedByUsername = disbursedByUsername,
        disbursedByFirstname = disbursedByFirstname,
        disbursedByLastname = disbursedByLastname,
        closedOnDate = closedOnDate,
        expectedMaturityDate = expectedMaturityDate,
        withdrawnOnDate = withdrawnOnDate,
    )

fun CurrencyResponseDto.toLoanCurrencyModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces.toDouble(),
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

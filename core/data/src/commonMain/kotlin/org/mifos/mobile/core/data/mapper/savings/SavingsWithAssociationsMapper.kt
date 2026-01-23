/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.savings

import org.mifos.mobile.core.model.entity.accounts.savings.Currency
import org.mifos.mobile.core.model.entity.accounts.savings.PaymentDetailData
import org.mifos.mobile.core.model.entity.accounts.savings.PaymentType
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.core.model.entity.accounts.savings.Summary
import org.mifos.mobile.core.model.entity.accounts.savings.TimeLine
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.model.entity.client.DepositType
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsStatusResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsSummaryResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsTimeLineResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsTransactionTypeResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.transaction.PaymentDetailsResponseDto
import org.mifos.mobile.core.network.dto.transaction.PaymentTypeResponseDto
import org.mifos.mobile.core.network.dto.transaction.SavingsTransactionResponseDto

fun SavingsWithAssociationsResponseDto.toModel(): SavingsWithAssociations =
    SavingsWithAssociations(
        id = id,
        accountNo = accountNo,
        depositType = depositType?.toDepositType(),
        externalId = externalId,
        clientId = clientId,
        clientName = clientName,
        savingsProductId = savingsProductId,
        savingsProductName = savingsProductName,
        fieldOfficerId = fieldOfficerId,
        status = status?.toSavingsStatus(),
        timeline = timeline?.toSavingsTimeline(),
        currency = currency?.toSavingsCurrencyModel(),
        nominalAnnualInterestRate = nominalAnnualInterestRate,
        minRequiredOpeningBalance = minRequiredOpeningBalance,
        lockinPeriodFrequency = lockinPeriodFrequency,
        withdrawalFeeForTransfers = withdrawalFeeForTransfers,
        allowOverdraft = allowOverdraft,
        enforceMinRequiredBalance = enforceMinRequiredBalance,
        withHoldTax = withHoldTax,
        lastActiveTransactionDate = lastActiveTransactionDate,
        dormancyTrackingActive = dormancyTrackingActive,
        summary = summary?.toSavingsSummary(),
        transactions = transactions.map { it.toSavingsTransaction() },
    )

fun TypeResponseDto.toDepositType(): DepositType =
    DepositType(
        id = id,
        code = code,
        value = value,
    )

fun SavingsStatusResponseDto.toSavingsStatus(): Status =
    Status(
        id = id,
        code = code,
        value = value,
        submittedAndPendingApproval = submittedAndPendingApproval,
        approved = approved,
        rejected = rejected,
        withdrawnByApplicant = withdrawnByApplicant,
        active = active,
        closed = closed,
        prematureClosed = prematureClosed,
        matured = matured,
    )

fun SavingsTimeLineResponseDto.toSavingsTimeline(): TimeLine =
    TimeLine(
        submittedOnDate = submittedOnDate,
        submittedByUsername = submittedByUsername,
        submittedByFirstname = submittedByFirstname,
        submittedByLastname = submittedByLastname,
        approvedOnDate = approvedOnDate,
        approvedByUsername = approvedByUsername,
        approvedByFirstname = approvedByFirstname,
        approvedByLastname = approvedByLastname,
        activatedOnDate = activatedOnDate,
        activatedByUsername = activatedByUsername,
        activatedByFirstname = activatedByFirstname,
        activatedByLastname = activatedByLastname,
        closedOnDate = closedOnDate,
    )

fun SavingsSummaryResponseDto.toSavingsSummary(): Summary =
    Summary(
        currency = currency?.toSavingsCurrencyModel(),
        totalDeposits = totalDeposits,
        totalWithdrawals = totalWithdrawals,
        totalInterestEarned = totalInterestEarned,
        totalInterestPosted = totalInterestPosted,
        accountBalance = accountBalance,
        totalOverdraftInterestDerived = totalOverdraftInterestDerived,
        interestNotPosted = interestNotPosted,
        lastInterestCalculationDate = lastInterestCalculationDate,
    )

fun SavingsTransactionResponseDto.toSavingsTransaction(): Transactions =
    Transactions(
        id = id,
        transactionType = transactionType?.toSavingsTransactionType(),
        accountId = accountId,
        accountNo = accountNo,
        date = date,
        currency = currency?.toSavingsCurrencyModel(),
        paymentDetailData = paymentDetailData?.toPaymentDetails(),
        amount = amount,
        runningBalance = runningBalance,
        reversed = reversed,
        submittedOnDate = submittedOnDate,
        interestedPostedAsOn = interestedPostedAsOn,
    )

fun PaymentDetailsResponseDto.toPaymentDetails(): PaymentDetailData =
    PaymentDetailData(
        id = id,
        paymentType = paymentType.toModel(),
        accountNumber = accountNumber,
        checkNumber = checkNumber,
        routingCode = routingCode,
        receiptNumber = receiptNumber,
        bankNumber = bankNumber,
    )

fun CurrencyResponseDto.toSavingsCurrencyModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf.toInt(),
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun PaymentTypeResponseDto.toModel(): PaymentType =
    PaymentType(
        id = id,
        name = name,
    )

fun SavingsTransactionTypeResponseDto.toSavingsTransactionType(): TransactionType =
    TransactionType(
        id = id,
        code = code,
        value = value,
        deposit = deposit,
        dividendPayout = dividendPayout,
        withdrawal = withdrawal,
        interestPosting = interestPosting,
        feeDeduction = feeDeduction,
        initiateTransfer = initiateTransfer,
        approveTransfer = approveTransfer,
        withdrawTransfer = withdrawTransfer,
        rejectTransfer = rejectTransfer,
        overdraftInterest = overdraftInterest,
        writtenoff = writtenoff,
        overdraftFee = overdraftFee,
        withholdTax = withholdTax,
        escheat = escheat,
    )

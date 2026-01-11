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

import org.mifos.mobile.core.model.entity.accounts.savings.Currency
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.core.model.entity.accounts.savings.TimeLine
import org.mifos.mobile.core.model.entity.client.DepositType
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsAccountResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsDepositTypeResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsStatusResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsTimeLineResponseDto

fun SavingsAccountResponseDto.toModel(): SavingAccount =
    SavingAccount(
        id = id,
        accountNo = accountNo,
        productName = productName,
        productId = productId,
        overdraftLimit = overdraftLimit,
        minRequiredBalance = minRequiredBalance,
        accountBalance = accountBalance,
        totalDeposits = totalDeposits,
        savingsProductName = savingsProductName,
        clientName = clientName,
        savingsProductId = savingsProductId,
        nominalAnnualInterestRate = nominalAnnualInterestRate,
        status = status?.toModel(),
        currency = currency?.toSavingsCurrencyModel(),
        depositType = depositType?.toModel(),
        lastActiveTransactionDate = lastActiveTransactionDate,
        timeLine = timeLine?.toModel(),
    )

fun SavingsStatusResponseDto.toModel(): Status =
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

fun SavingsDepositTypeResponseDto.toModel(): DepositType =
    DepositType(
        id = id,
        code = code,
        value = value,
    )

fun SavingsTimeLineResponseDto.toModel(): TimeLine =
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

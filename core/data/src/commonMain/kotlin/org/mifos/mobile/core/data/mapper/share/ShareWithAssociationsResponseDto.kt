/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.share

import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.ChargeCalculationType
import org.mifos.mobile.core.model.entity.ChargeTimeType
import org.mifos.mobile.core.model.entity.accounts.share.Currency
import org.mifos.mobile.core.model.entity.accounts.share.EnumOptionData
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccountWithAssociations
import org.mifos.mobile.core.model.entity.accounts.share.Status
import org.mifos.mobile.core.model.entity.accounts.share.Summary
import org.mifos.mobile.core.model.entity.accounts.share.Timeline
import org.mifos.mobile.core.model.entity.accounts.share.Transactions
import org.mifos.mobile.core.network.dto.charges.ChargeResponseDto
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareStatusResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareSummaryResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareTimelineResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.transaction.ShareTransactionResponseDto
import org.mifos.mobile.core.model.entity.Currency as ModelCurrency

fun ShareWithAssociationsResponseDto.toModel(): ShareAccountWithAssociations =
    ShareAccountWithAssociations(
        id = id,
        accountNo = accountNo,
        clientId = clientId,
        clientName = clientName,
        productId = productId,
        productName = productName,
        status = status?.toModel(),
        currency = currency?.toModel(),
        timeline = timeline?.toModel(),
        summary = summary?.toModel(),
        currentMarketPrice = currentMarketPrice,
        savingsAccountId = savingsAccountId,
        savingsAccountNumber = savingsAccountNumber,
        allowDividendCalculationForInactiveClients =
        allowDividendCalculationForInactiveClients,
        lockinPeriod = lockinPeriod,
        lockPeriodTypeEnum = lockPeriodTypeEnum?.toEnumOptionData(),
        minimumActivePeriod = minimumActivePeriod,
        minimumActivePeriodTypeEnum =
        minimumActivePeriodTypeEnum?.toEnumOptionData(),
        charges = charges.map { it.toShareChargeModel() },
        purchasedShares = purchasedShares.map { it.toModel() },
        dividends = dividends,
    )

fun ShareStatusResponseDto.toModel(): Status =
    Status(
        id = id,
        code = code,
        value = value,
        submittedAndPendingApproval = submittedAndPendingApproval,
        approved = approved,
        rejected = rejected,
        active = active,
        closed = closed,
    )

fun CurrencyResponseDto.toModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun ShareTimelineResponseDto.toModel(): Timeline =
    Timeline(
        submittedOnDate = submittedOnDate,
        submittedByUsername = submittedByUsername,
        submittedByFirstname = submittedByFirstname,
        submittedByLastname = submittedByLastname,
        approvedDate = approvedDate,
        approvedByUsername = approvedByUsername,
        approvedByFirstname = approvedByFirstname,
        approvedByLastname = approvedByLastname,
        activatedDate = activatedDate,
        activatedByUsername = activatedByUsername,
        activatedByFirstname = activatedByFirstname,
        activatedByLastname = activatedByLastname,
    )

fun ShareSummaryResponseDto.toModel(): Summary =
    Summary(
        id = id,
        accountNo = accountNo,
        productId = productId,
        productName = productName,
        status = status?.toModel(),
        currency = currency?.toModel(),
        timeline = timeline?.toModel(),
        totalApprovedShares = totalApprovedShares,
        totalPendingForApprovalShares = totalPendingForApprovalShares,
    )

fun TypeResponseDto.toEnumOptionData(): EnumOptionData =
    EnumOptionData(
        id = id?.toLong(),
        code = code,
        value = value,
    )

fun TypeResponseDto.toChargeTimeType(): ChargeTimeType =
    ChargeTimeType(
        id = id ?: 0,
        code = code,
        value = value,
    )

fun TypeResponseDto.toChargeCalculationType(): ChargeCalculationType =
    ChargeCalculationType(
        id = id ?: 0,
        code = code,
        value = value,
    )

fun ChargeResponseDto.toShareChargeModel(): Charge =
    Charge(
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = ArrayList(dueDate),
        chargeTimeType = chargeTimeType?.toChargeTimeType(),
        chargeCalculationType = chargeCalculationType?.toChargeCalculationType(),
        currency = currency?.toModelCurrency(),
        amount = amount,
        amountPaid = amountPaid,
        amountWaived = amountWaived,
        amountWrittenOff = amountWrittenOff,
        amountOutstanding = amountOutstanding,
        penalty = penalty,
        isActive = isActive,
        isChargePaid = isChargePaid,
        isChargeWaived = isChargeWaived,
        paid = paid,
        waived = waived,
    )

fun CurrencyResponseDto.toModelCurrency(): ModelCurrency =
    ModelCurrency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun ShareTransactionResponseDto.toModel(): Transactions =
    Transactions(
        accountId = accountId,
        amount = amount,
        amountPaid = amountPaid,
        chargeAmount = chargeAmount,
        id = id,
        numberOfShares = numberOfShares,
        purchasedDate = purchasedDate,
        purchasedPrice = purchasedPrice,
        status = status?.toEnumOptionData(),
        type = type?.toEnumOptionData(),
    )

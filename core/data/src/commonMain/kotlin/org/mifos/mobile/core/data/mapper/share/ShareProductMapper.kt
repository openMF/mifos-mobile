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

import org.mifos.mobile.core.model.entity.templates.shares.AccountingRule
import org.mifos.mobile.core.model.entity.templates.shares.Currency
import org.mifos.mobile.core.model.entity.templates.shares.LockPeriodTypeEnum
import org.mifos.mobile.core.model.entity.templates.shares.MinimumActivePeriodForDividendsTypeEnum
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductResponseDto

fun ShareProductResponseDto.toModel(): ShareProduct =
    ShareProduct(
        accountingRule = accountingRule?.toAccountingRule(),
        allowDividendCalculationForInactiveClients =
        allowDividendCalculationForInactiveClients,
        currency = currency?.toShareCurrency(),
        description = description,
        id = id,
        lockPeriodTypeEnum = lockPeriodTypeEnum?.toLockPeriodTypeEnum(),
        lockinPeriod = lockinPeriod,
        maximumShares = maximumShares,
        minimumActivePeriod = minimumActivePeriod,
        minimumActivePeriodForDividendsTypeEnum =
        minimumActivePeriodForDividendsTypeEnum?.toMinimumActivePeriodForDividendsTypeEnum(),
        minimumShares = minimumShares,
        name = name,
        nominalShares = nominalShares,
        shareCapital = shareCapital,
        shortName = shortName,
        totalShares = totalShares,
        totalSharesIssued = totalSharesIssued,
        unitPrice = unitPrice,
    )

fun TypeResponseDto.toAccountingRule(): AccountingRule =
    AccountingRule(
        id = id,
        code = code,
        value = value,
    )

fun TypeResponseDto.toLockPeriodTypeEnum(): LockPeriodTypeEnum =
    LockPeriodTypeEnum(
        id = id,
        code = code,
        value = value,
    )

fun TypeResponseDto.toMinimumActivePeriodForDividendsTypeEnum(): MinimumActivePeriodForDividendsTypeEnum =
    MinimumActivePeriodForDividendsTypeEnum(
        id = id,
        code = code,
        value = value,
    )

fun CurrencyResponseDto.toShareCurrency(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf.toInt(),
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

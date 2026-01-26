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

import org.mifos.mobile.core.model.entity.templates.shareProductDetails.AccountingItem
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.AccountingMappings
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.Charge
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.Currency
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.EnumOption
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.MarketPrice
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.SavingsAccountSummary
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.ShareProductDetails
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.products.share.AccountingItemResponseDto
import org.mifos.mobile.core.network.dto.products.share.AccountingMappingsResponseDto
import org.mifos.mobile.core.network.dto.products.share.MarketPriceResponseDto
import org.mifos.mobile.core.network.dto.products.share.SavingsAccountSummaryResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductChargeResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductDetailsResponseDto

fun ShareProductDetailsResponseDto.toModel(): ShareProductDetails =
    ShareProductDetails(
        id = id,
        name = name,
        shortName = shortName,
        description = description,
        currency = currency?.toShareProductCurrencyModel(),
        totalShares = totalShares,
        totalSharesIssued = totalSharesIssued,
        unitPrice = unitPrice,
        shareCapital = shareCapital,
        nominalShares = nominalShares,
        marketPrice = marketPrice?.map { it.toModel() } ?: emptyList(),
        charges = charges?.map { it.toModel() } ?: emptyList(),
        allowDividendCalculationForInactiveClients =
        allowDividendCalculationForInactiveClients,
        lockinPeriod = lockinPeriod,
        lockPeriodTypeEnum = lockPeriodTypeEnum?.toEnumOption(),
        minimumActivePeriod = minimumActivePeriod,
        minimumActivePeriodForDividendsTypeEnum =
        minimumActivePeriodForDividendsTypeEnum?.toEnumOption(),
        accountingRule = accountingRule?.toEnumOption(),
        accountingMappings = accountingMappings?.toModel(),
        clientSavingsAccounts =
        clientSavingsAccounts?.map { it.toModel() } ?: emptyList(),
    )

fun CurrencyResponseDto.toShareProductCurrencyModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf.toInt(),
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun TypeResponseDto.toEnumOption(): EnumOption =
    EnumOption(
        id = id,
        code = code,
        value = value,
    )

fun SavingsAccountSummaryResponseDto.toModel(): SavingsAccountSummary =
    SavingsAccountSummary(
        id = id,
        savingsProductName = savingsProductName,
    )

fun MarketPriceResponseDto.toModel(): MarketPrice =
    MarketPrice(
        id = id,
        fromDate = fromDate,
        shareValue = shareValue,
    )

fun AccountingMappingsResponseDto.toModel(): AccountingMappings =
    AccountingMappings(
        shareReferenceId = shareReferenceId?.toModel(),
        incomeFromFeeAccountId = incomeFromFeeAccountId?.toModel(),
        shareEquityId = shareEquityId?.toModel(),
        shareSuspenseId = shareSuspenseId?.toModel(),
    )

fun AccountingItemResponseDto.toModel(): AccountingItem =
    AccountingItem(
        id = id,
        name = name,
        glCode = glCode,
    )

fun ShareProductChargeResponseDto.toModel(): Charge =
    Charge(
        id = id,
        name = name,
        amount = amount,
        active = active,
        penalty = penalty,
        currency = currency?.toShareProductCurrencyModel(),
    )

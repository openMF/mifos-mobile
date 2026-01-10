package org.mifos.mobile.core.data.mapper.transactions

import org.mifos.mobile.core.model.entity.Currency
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.client.Type
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.transaction.TransactionResponseDto

fun TransactionResponseDto.toRecentTransactionModel() : Transaction =
    Transaction(
        id = id,
        officeId = officeId,
        officeName = officeName,
        type = type.toClientType(),
        date = date,
        currency = currency?.toTransactionCurrency(),
        amount = amount,
        submittedOnDate = submittedOnDate,
        reversed = reversed
    )

fun TypeResponseDto.toClientType(): Type =
    Type(
        id = id,
        code = code,
        value = value
    )

fun CurrencyResponseDto.toTransactionCurrency(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel
    )

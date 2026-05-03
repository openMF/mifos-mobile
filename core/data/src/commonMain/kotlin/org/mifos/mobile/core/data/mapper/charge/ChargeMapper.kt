/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.charge

import org.mifos.mobile.core.database.entity.ChargeCalculationTypeEntity
import org.mifos.mobile.core.database.entity.ChargeEntity
import org.mifos.mobile.core.database.entity.ChargeTimeTypeEntity
import org.mifos.mobile.core.database.entity.CurrencyEntity
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.ChargeCalculationType
import org.mifos.mobile.core.model.entity.ChargeTimeType
import org.mifos.mobile.core.model.entity.Currency
import org.mifos.mobile.core.network.dto.charges.ChargeResponseDto
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

fun ChargeResponseDto.toModel(): Charge =
    Charge(
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = ArrayList(dueDate),
        chargeTimeType = chargeTimeType?.toChargeTimeType(),
        chargeCalculationType = chargeCalculationType?.toChargeCalculationType(),
        currency = currency?.toChargeCurrencyModel(),
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

fun CurrencyResponseDto.toChargeCurrencyModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

// Entity → Domain
fun ChargeEntity.toModel(): Charge =
    Charge(
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = dueDate,
        chargeTimeType = chargeTimeType?.toModel(),
        chargeCalculationType = chargeCalculationType?.toModel(),
        currency = currency?.toModel(),
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

// Domain → Entity
fun Charge.toEntity(): ChargeEntity =
    ChargeEntity(
        id = chargeId,
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = dueDate,
        chargeTimeType = chargeTimeType?.toEntity(),
        chargeCalculationType = chargeCalculationType?.toEntity(),
        currency = currency?.toEntity(),
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

// DTO → Entity (for Store 5 writer)
fun ChargeResponseDto.toEntity(clientId: Int?): ChargeEntity =
    ChargeEntity(
        id = chargeId,
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = dueDate,
        chargeTimeType = chargeTimeType?.toChargeTimeTypeEntity(),
        chargeCalculationType = chargeCalculationType?.toChargeCalculationTypeEntity(),
        currency = currency?.toCurrencyEntity(),
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

fun ChargeTimeTypeEntity.toModel(): ChargeTimeType =
    ChargeTimeType(id = id, code = code, value = value)

fun ChargeTimeType.toEntity(): ChargeTimeTypeEntity =
    ChargeTimeTypeEntity(id = id, code = code, value = value)

fun TypeResponseDto.toChargeTimeTypeEntity(): ChargeTimeTypeEntity =
    ChargeTimeTypeEntity(id = id ?: 0, code = code, value = value)

fun ChargeCalculationTypeEntity.toModel(): ChargeCalculationType =
    ChargeCalculationType(id = id, code = code, value = value)

fun ChargeCalculationType.toEntity(): ChargeCalculationTypeEntity =
    ChargeCalculationTypeEntity(id = id, code = code, value = value)

fun TypeResponseDto.toChargeCalculationTypeEntity(): ChargeCalculationTypeEntity =
    ChargeCalculationTypeEntity(id = id ?: 0, code = code, value = value)

fun CurrencyEntity.toModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = 0.0,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun Currency.toEntity(): CurrencyEntity =
    CurrencyEntity(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun CurrencyResponseDto.toCurrencyEntity(): CurrencyEntity =
    CurrencyEntity(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

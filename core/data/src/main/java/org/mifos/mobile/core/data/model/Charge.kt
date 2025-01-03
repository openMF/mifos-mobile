/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.model

import org.mifos.mobile.core.database.entity.ChargeCalculationTypeEntity
import org.mifos.mobile.core.database.entity.ChargeEntity
import org.mifos.mobile.core.database.entity.ChargeTimeTypeEntity
import org.mifos.mobile.core.database.entity.CurrencyEntity
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.ChargeCalculationType
import org.mifos.mobile.core.model.entity.ChargeTimeType
import org.mifos.mobile.core.model.entity.Currency

fun ChargeEntity.toCharge(): Charge {
    return Charge(
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = dueDate,
        chargeTimeType = ChargeTimeType(
            id = chargeTimeType?.id ?: 0,
            code = chargeTimeType?.code,
            value = chargeTimeType?.value,
        ),
        chargeCalculationType = ChargeCalculationType(
            id = chargeCalculationType?.id ?: 0,
            code = chargeCalculationType?.code,
            value = chargeCalculationType?.value,
        ),
        currency = Currency(
            code = currency?.code,
            name = currency?.name,
            decimalPlaces = currency?.decimalPlaces ?: 0,
            displaySymbol = currency?.displaySymbol,
            nameCode = currency?.nameCode,
            displayLabel = currency?.displayLabel,
        ),
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
}

fun Charge.toChargeEntity(): ChargeEntity {
    return ChargeEntity(
        clientId = clientId,
        chargeId = chargeId,
        name = name,
        dueDate = dueDate,
        chargeTimeType = ChargeTimeTypeEntity(
            id = chargeTimeType?.id ?: 0,
            code = chargeTimeType?.code,
            value = chargeTimeType?.value,
        ),
        chargeCalculationType = ChargeCalculationTypeEntity(
            id = chargeCalculationType?.id ?: 0,
            code = chargeCalculationType?.code,
            value = chargeCalculationType?.value,
        ),
        currency = CurrencyEntity(
            code = currency?.code,
            name = currency?.name,
            decimalPlaces = currency?.decimalPlaces ?: 0,
            displaySymbol = currency?.displaySymbol,
            nameCode = currency?.nameCode,
            displayLabel = currency?.displayLabel,
        ),
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
}

/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.utils

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.database.entity.ChargeCalculationTypeEntity
import org.mifos.mobile.core.database.entity.ChargeTimeTypeEntity
import org.mifos.mobile.core.database.entity.CurrencyEntity

class ChargeTypeConverters {

    @TypeConverter
    fun fromIntList(value: String): ArrayList<Int?> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toIntList(list: ArrayList<Int?>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromChargeTimeType(value: ChargeTimeTypeEntity?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toChargeTimeType(value: String?): ChargeTimeTypeEntity? {
        return value?.let { Json.decodeFromString(ChargeTimeTypeEntity.serializer(), it) }
    }

    @TypeConverter
    fun fromChargeCalculationType(value: ChargeCalculationTypeEntity?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toChargeCalculationType(value: String?): ChargeCalculationTypeEntity? {
        return value?.let { Json.decodeFromString(ChargeCalculationTypeEntity.serializer(), it) }
    }

    @TypeConverter
    fun fromCurrency(value: CurrencyEntity?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toCurrency(value: String?): CurrencyEntity? {
        return value?.let { Json.decodeFromString(CurrencyEntity.serializer(), it) }
    }
}

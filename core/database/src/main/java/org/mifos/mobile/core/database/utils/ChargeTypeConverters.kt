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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.mifos.mobile.core.model.entity.ChargeCalculationType
import org.mifos.mobile.core.model.entity.ChargeTimeType
import org.mifos.mobile.core.model.entity.Currency

class ChargeTypeConverters {

    @TypeConverter
    fun fromIntList(value: String): ArrayList<Int?> {
        val listType = object : TypeToken<ArrayList<Int?>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toIntList(list: ArrayList<Int?>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromChargeTimeType(value: ChargeTimeType?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toChargeTimeType(value: String?): ChargeTimeType? {
        return Gson().fromJson(value, ChargeTimeType::class.java)
    }

    @TypeConverter
    fun fromChargeCalculationType(value: ChargeCalculationType?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toChargeCalculationType(value: String?): ChargeCalculationType? {
        return Gson().fromJson(value, ChargeCalculationType::class.java)
    }

    @TypeConverter
    fun fromCurrency(value: Currency?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toCurrency(value: String?): Currency? {
        return Gson().fromJson(value, Currency::class.java)
    }
}

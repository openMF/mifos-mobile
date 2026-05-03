/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.utils

import androidx.room3.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.database.entity.ChargeCalculationTypeEntity
import org.mifos.mobile.core.database.entity.ChargeTimeTypeEntity
import org.mifos.mobile.core.database.entity.CurrencyEntity
import template.core.base.security.FieldEncryptor

class ChargeTypeConverters {

    private fun encrypt(value: String): String {
        val enc = encryptor ?: return value
        return "ENC:${enc.encrypt(value)}"
    }

    private fun decrypt(value: String): String {
        val enc = encryptor ?: return value
        return if (value.startsWith("ENC:")) enc.decrypt(value.removePrefix("ENC:")) else value
    }

    @TypeConverter
    fun fromIntList(value: String): ArrayList<Int?> {
        return Json.decodeFromString(decrypt(value))
    }

    @TypeConverter
    fun toIntList(list: ArrayList<Int?>): String {
        return encrypt(Json.encodeToString(list))
    }

    @TypeConverter
    fun fromChargeTimeType(value: ChargeTimeTypeEntity?): String? {
        return value?.let { encrypt(Json.encodeToString(ChargeTimeTypeEntity.serializer(), it)) }
    }

    @TypeConverter
    fun toChargeTimeType(value: String?): ChargeTimeTypeEntity? {
        return value?.let { Json.decodeFromString(ChargeTimeTypeEntity.serializer(), decrypt(it)) }
    }

    @TypeConverter
    fun fromChargeCalculationType(value: ChargeCalculationTypeEntity?): String? {
        return value?.let { encrypt(Json.encodeToString(ChargeCalculationTypeEntity.serializer(), it)) }
    }

    @TypeConverter
    fun toChargeCalculationType(value: String?): ChargeCalculationTypeEntity? {
        return value?.let { Json.decodeFromString(ChargeCalculationTypeEntity.serializer(), decrypt(it)) }
    }

    @TypeConverter
    fun fromCurrency(value: CurrencyEntity?): String? {
        return value?.let { encrypt(Json.encodeToString(CurrencyEntity.serializer(), it)) }
    }

    @TypeConverter
    fun toCurrency(value: String?): CurrencyEntity? {
        return value?.let { Json.decodeFromString(CurrencyEntity.serializer(), decrypt(it)) }
    }

    companion object {
        @kotlin.concurrent.Volatile
        private var encryptor: FieldEncryptor? = null

        fun install(fieldEncryptor: FieldEncryptor) {
            encryptor = fieldEncryptor
        }
    }
}

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)

package org.mifos.library.passcode.data

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import org.mifos.library.passcode.model.PasscodePreferencesProto

private const val PASSCODE_INFO_KEY = "passcodeInfo"

class PasscodePreferencesDataSource(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val passcodeSettings = MutableStateFlow(
        settings.decodeValue(
            key = PASSCODE_INFO_KEY,
            serializer = PasscodePreferencesProto.serializer(),
            defaultValue = settings.decodeValueOrNull(
                key = PASSCODE_INFO_KEY,
                serializer = PasscodePreferencesProto.serializer(),
            ) ?: PasscodePreferencesProto.DEFAULT,
        ),
    )

    val passcode = passcodeSettings.map { it.passcode }
    val hasPasscode = passcodeSettings.map { it.hasPasscode }

    suspend fun updatePasscodeSettings(passcodePreferences: PasscodePreferencesProto) {
        withContext(dispatcher) {
            settings.putPasscodePreference(passcodePreferences)
            passcodeSettings.value = passcodePreferences
        }
    }

    suspend fun clearInfo() {
        withContext(dispatcher) {
            settings.remove(PASSCODE_INFO_KEY)
        }
    }
}

private fun Settings.putPasscodePreference(preference: PasscodePreferencesProto) {
    encodeValue(
        key = PASSCODE_INFO_KEY,
        serializer = PasscodePreferencesProto.serializer(),
        value = preference,
    )
}

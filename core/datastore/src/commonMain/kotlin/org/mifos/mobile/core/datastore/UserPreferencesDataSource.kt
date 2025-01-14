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

package org.mifos.mobile.core.datastore

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
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.AppTheme
import org.mifos.mobile.core.datastore.model.UserData

private const val USER_DATA = "userData"
private const val APP_SETTINGS = "appSettings"

class UserPreferencesDataSource(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
) {

    private val _userInfo = MutableStateFlow(
        settings.decodeValue(
            key = USER_DATA,
            serializer = UserData.serializer(),
            defaultValue = settings.decodeValueOrNull(
                key = USER_DATA,
                serializer = UserData.serializer(),
            ) ?: UserData.DEFAULT,
        ),
    )

    private val _settingsInfo = MutableStateFlow(
        settings.decodeValue(
            key = APP_SETTINGS,
            serializer = AppSettings.serializer(),
            defaultValue = settings.decodeValueOrNull(
                key = APP_SETTINGS,
                serializer = AppSettings.serializer(),
            ) ?: AppSettings.DEFAULT,
        ),
    )

    val token = _userInfo.map {
        it.base64EncodedAuthenticationKey
    }

    val userInfo = _userInfo

    val settingsInfo = _settingsInfo

    val clientId = _userInfo.map { it.clientId }

    val appTheme = _settingsInfo.map { it.appTheme }

    suspend fun updateSettingsInfo(appSettings: AppSettings) {
        withContext(dispatcher) {
            settings.putSettingsPreference(appSettings)
            _settingsInfo.value = appSettings
        }
    }

    suspend fun updateUserInfo(user: UserData) {
        withContext(dispatcher) {
            settings.putUserPreference(user)
            _userInfo.value = user
        }
    }

    suspend fun updateToken(token: String) {
        withContext(dispatcher) {
            settings.putUserPreference(
                UserData.DEFAULT.copy(
                    base64EncodedAuthenticationKey = token,
                ),
            )
            _userInfo.value = UserData.DEFAULT.copy(
                base64EncodedAuthenticationKey = token,
            )
        }
    }

    suspend fun updateTheme(theme: AppTheme) {
        withContext(dispatcher) {
            settings.putSettingsPreference(
                AppSettings.DEFAULT.copy(
                    appTheme = theme,
                ),
            )
            _settingsInfo.value = AppSettings.DEFAULT.copy(
                appTheme = theme,
            )
        }
    }

    fun updateProfileImage(image: String) {
        settings.putString(PROFILE_IMAGE, image)
    }

    fun getProfileImage(): String? {
        return settings.getString(PROFILE_IMAGE, "").ifEmpty { null }
    }

    suspend fun clearInfo() {
        withContext(dispatcher) {
            settings.clear()
        }
    }

    companion object {
        private const val PROFILE_IMAGE = "preferences_profile_image"
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun Settings.putUserPreference(user: UserData) {
    encodeValue(
        key = USER_DATA,
        serializer = UserData.serializer(),
        value = user,
    )
}

private fun Settings.putSettingsPreference(settings: AppSettings) {
    encodeValue(
        key = APP_SETTINGS,
        serializer = AppSettings.serializer(),
        value = settings,
    )
}

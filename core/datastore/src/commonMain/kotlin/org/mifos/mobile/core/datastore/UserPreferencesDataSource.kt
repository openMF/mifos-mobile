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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import okio.ByteString.Companion.encodeUtf8
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.model.MifosThemeConfig

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

    val officeName = _userInfo.map { it.officeName }

    val passcode = _settingsInfo.map { it.passcode }

    val observeLanguage: Flow<LanguageConfig>
        get() = _settingsInfo.map { it.language }

    val observeDynamicColorPreference: Flow<Boolean>
        get() = _settingsInfo.map { it.useDynamicColor }

    val observeDarkThemeConfig: Flow<MifosThemeConfig>
        get() = _settingsInfo.map { it.appTheme }

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

    suspend fun updateToken(password: String) {
        val username = _userInfo.value.userName
        val authenticationToken = "Basic " + "$username:$password".encodeToBase64()
        withContext(dispatcher) {
            val updatedClient = userInfo.value.copy(
                userId = _userInfo.value.userId,
                userName = _userInfo.value.userName,
                officeName = _userInfo.value.officeName,
                clientId = userInfo.value.clientId,
                isAuthenticated = _userInfo.value.isAuthenticated,
                base64EncodedAuthenticationKey = authenticationToken,
            )
            settings.putUserPreference(updatedClient)
            _userInfo.value = updatedClient
        }
    }

    suspend fun updateClientId(clientId: Long) {
        withContext(dispatcher) {
            val updatedClient = userInfo.value.copy(
                userId = _userInfo.value.userId,
                userName = _userInfo.value.userName,
                clientId = clientId,
                officeName = _userInfo.value.officeName,
                isAuthenticated = _userInfo.value.isAuthenticated,
                base64EncodedAuthenticationKey = _userInfo.value.base64EncodedAuthenticationKey,
            )
            settings.putUserPreference(updatedClient)
            _userInfo.value = updatedClient
        }
    }

    suspend fun updateTheme(theme: MifosThemeConfig) {
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(appTheme = theme)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
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
            settings.putUserPreference(UserData.DEFAULT)
            val cleared = settings.getSettingsPreference().copy(
                isAuthenticated = false,
            )
            settings.putSettingsPreference(cleared)
            _settingsInfo.value = cleared
        }
    }

    suspend fun setSentTokenToServer(sent: Boolean) {
        withContext(dispatcher) {
            val updatedSettings = _settingsInfo.value.copy(sentTokenToServer = sent)
            settings.putSettingsPreference(updatedSettings)
            _settingsInfo.value = updatedSettings
        }
    }

    fun isSentTokenToServer(): Boolean {
        return _settingsInfo.value.sentTokenToServer
    }

    suspend fun saveGcmToken(token: String?) {
        withContext(dispatcher) {
            val updatedSettings = _settingsInfo.value.copy(gcmToken = token)
            settings.putSettingsPreference(updatedSettings)
            _settingsInfo.value = updatedSettings
        }
    }

    fun getGcmToken(): String? {
        return _settingsInfo.value.gcmToken
    }

    suspend fun setLanguage(language: LanguageConfig) =
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(language = language)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setShowOnboarding(showOnboarding: Boolean) =
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(showOnboarding = showOnboarding)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setFirstTimeState(firstTimeState: Boolean) =
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(firstTimeState = firstTimeState)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setIsAuthenticated(isAuthenticated: Boolean) =
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(isAuthenticated = isAuthenticated)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setIsUnlocked(isUnlocked: Boolean) =
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(isUnlocked = isUnlocked)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setPasscode(passcode: String) =
        withContext(dispatcher) {
            val newPreference = settings.getSettingsPreference().copy(passcode = passcode)
            settings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    companion object {
        private const val PROFILE_IMAGE = "preferences_profile_image"
    }
}

private fun String.encodeToBase64(): String {
    return this.encodeUtf8().base64()
}

@OptIn(ExperimentalSerializationApi::class)
private fun Settings.putUserPreference(user: UserData) {
    encodeValue(
        key = USER_DATA,
        serializer = UserData.serializer(),
        value = user,
    )
}

private fun Settings.getSettingsPreference(): AppSettings {
    return decodeValue(
        key = APP_SETTINGS,
        serializer = AppSettings.serializer(),
        defaultValue = AppSettings.DEFAULT,
    )
}

private fun Settings.putSettingsPreference(settings: AppSettings) {
    encodeValue(
        key = APP_SETTINGS,
        serializer = AppSettings.serializer(),
        value = settings,
    )
}

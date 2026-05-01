/*
 * Copyright 2026 Mifos Initiative
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
import org.mifos.mobile.core.datastore.model.TimeBasedTheme
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.model.MifosThemeConfig
import template.core.base.security.FieldEncryptor

private const val USER_DATA = "userData"
private const val APP_SETTINGS = "appSettings"

@Suppress("TooManyFunctions")
class UserPreferencesDataSource(
    private val plainSettings: Settings,
    private val secureSettings: Settings,
    private val dispatcher: CoroutineDispatcher,
    val fieldEncryptor: FieldEncryptor,
) {

    init {
        // Migration: move userData from plain to secure if it exists in plain
        val plainUserData = plainSettings.decodeValueOrNull(
            key = USER_DATA,
            serializer = UserData.serializer(),
        )
        if (plainUserData != null) {
            secureSettings.encodeValue(
                key = USER_DATA,
                serializer = UserData.serializer(),
                value = plainUserData,
            )
            plainSettings.remove(USER_DATA)
        }
    }

    private val _userInfo = MutableStateFlow(
        secureSettings.decodeValue(
            key = USER_DATA,
            serializer = UserData.serializer(),
            defaultValue = secureSettings.decodeValueOrNull(
                key = USER_DATA,
                serializer = UserData.serializer(),
            ) ?: UserData.DEFAULT,
        ),
    )

    private val _settingsInfo = MutableStateFlow(
        plainSettings.decodeValue(
            key = APP_SETTINGS,
            serializer = AppSettings.serializer(),
            defaultValue = plainSettings.decodeValueOrNull(
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

    val observeTimeBasedThemeConfig: Flow<TimeBasedTheme>
        get() = _settingsInfo.map { it.timeBasedTheme }

    suspend fun updateSettingsInfo(appSettings: AppSettings) {
        withContext(dispatcher) {
            plainSettings.putSettingsPreference(appSettings)
            _settingsInfo.value = appSettings
        }
    }

    suspend fun updateUserInfo(user: UserData) {
        withContext(dispatcher) {
            secureSettings.putUserPreference(user)
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
            secureSettings.putUserPreference(updatedClient)
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
            secureSettings.putUserPreference(updatedClient)
            _userInfo.value = updatedClient
        }
    }

    suspend fun updateTheme(theme: MifosThemeConfig) {
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(appTheme = theme)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }
    }

    suspend fun updateTimeBasedTheme(timeBasedTheme: TimeBasedTheme) {
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(timeBasedTheme = timeBasedTheme)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }
    }

    fun updateProfileImage(image: String) {
        plainSettings.putString(PROFILE_IMAGE, image)
    }

    fun getProfileImage(): String? {
        return plainSettings.getString(PROFILE_IMAGE, "").ifEmpty { null }
    }

    suspend fun clearInfo() {
        withContext(dispatcher) {
            secureSettings.putUserPreference(UserData.DEFAULT)
            _userInfo.value = UserData.DEFAULT
            val cleared = plainSettings.getSettingsPreference().copy(
                isAuthenticated = false,
            )
            plainSettings.putSettingsPreference(cleared)
            _settingsInfo.value = cleared
        }
    }

    suspend fun setSentTokenToServer(sent: Boolean) {
        withContext(dispatcher) {
            val updatedSettings = _settingsInfo.value.copy(sentTokenToServer = sent)
            plainSettings.putSettingsPreference(updatedSettings)
            _settingsInfo.value = updatedSettings
        }
    }

    fun isSentTokenToServer(): Boolean {
        return _settingsInfo.value.sentTokenToServer
    }

    suspend fun saveGcmToken(token: String?) {
        withContext(dispatcher) {
            val updatedSettings = _settingsInfo.value.copy(gcmToken = token)
            plainSettings.putSettingsPreference(updatedSettings)
            _settingsInfo.value = updatedSettings
        }
    }

    fun getGcmToken(): String? {
        return _settingsInfo.value.gcmToken
    }

    suspend fun setLanguage(language: LanguageConfig) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(language = language)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setShowOnboarding(showOnboarding: Boolean) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(showOnboarding = showOnboarding)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setFirstTimeState(firstTimeState: Boolean) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(firstTimeState = firstTimeState)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setIsAuthenticated(isAuthenticated: Boolean) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(isAuthenticated = isAuthenticated)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setIsUnlocked(isUnlocked: Boolean) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(isUnlocked = isUnlocked)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setPasscode(passcode: String) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference().copy(passcode = passcode)
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    suspend fun setSelectedServices(selectedServices: Set<String>?) =
        withContext(dispatcher) {
            val newPreference = plainSettings.getSettingsPreference()
                .copy(selectedServices = selectedServices ?: emptySet())
            plainSettings.putSettingsPreference(newPreference)
            _settingsInfo.value = newPreference
        }

    fun saveSelectedServicesDirectly(services: Set<String>?) {
        if (services == null) {
            plainSettings.remove(SELECTED_SERVICES_KEY)
        } else {
            plainSettings.putString(SELECTED_SERVICES_KEY, services.joinToString(","))
        }
        val newPreference = plainSettings.getSettingsPreference().copy(selectedServices = services ?: emptySet())
        _settingsInfo.value = newPreference
    }

    fun getSelectedServicesDirectly(): Set<String>? {
        val directString = plainSettings.getStringOrNull(SELECTED_SERVICES_KEY)
        return if (directString == null) {
            null
        } else if (directString.isBlank()) {
            emptySet()
        } else {
            directString.split(",").filter { it.isNotBlank() }.toSet()
        }
    }

    companion object {
        private const val PROFILE_IMAGE = "preferences_profile_image"
        private const val SELECTED_SERVICES_KEY = "selected_services_list"
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

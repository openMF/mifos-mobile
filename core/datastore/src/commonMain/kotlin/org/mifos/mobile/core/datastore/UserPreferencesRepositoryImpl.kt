/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.TimeBasedTheme
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.model.MifosThemeConfig

class UserPreferencesRepositoryImpl(
    private val preferenceManager: UserPreferencesDataSource,
//    private val ioDispatcher: CoroutineDispatcher,
    unconfinedDispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {
    private val unconfinedScope = CoroutineScope(unconfinedDispatcher)

    override val userInfo: StateFlow<UserData>
        get() = preferenceManager.userInfo

    override val settingsInfo: StateFlow<AppSettings>
        get() = preferenceManager.settingsInfo

    override val appTheme: StateFlow<MifosThemeConfig>
        get() = preferenceManager.appTheme.stateIn(
            scope = unconfinedScope,
            initialValue = MifosThemeConfig.FOLLOW_SYSTEM,
            started = SharingStarted.Eagerly,
        )
    override val token: StateFlow<String?>
        get() = preferenceManager.token.stateIn(
            scope = unconfinedScope,
            initialValue = null,
            started = SharingStarted.Eagerly,
        )

    override val clientId: StateFlow<Long?>
        get() = preferenceManager.clientId.stateIn(
            scope = unconfinedScope,
            initialValue = null,
            started = SharingStarted.Eagerly,
        )

    override val profileImage: String?
        get() = preferenceManager.getProfileImage()

    override val sentTokenToServer: StateFlow<Boolean>
        get() = preferenceManager.settingsInfo.map { it.sentTokenToServer }
            .stateIn(unconfinedScope, SharingStarted.Eagerly, false)

    override val gcmToken: StateFlow<String?>
        get() = preferenceManager.settingsInfo.map { it.gcmToken }
            .stateIn(unconfinedScope, SharingStarted.Eagerly, null)

    override val observeLanguage: Flow<LanguageConfig>
        get() = preferenceManager.observeLanguage

    override val observeDarkThemeConfig: Flow<MifosThemeConfig>
        get() = preferenceManager.observeDarkThemeConfig

    override val observeTimeBasedThemeConfig: Flow<TimeBasedTheme>
        get() = preferenceManager.observeTimeBasedThemeConfig

    override val observeDynamicColorPreference: Flow<Boolean>
        get() = preferenceManager.observeDynamicColorPreference

    override val passcode: Flow<String>
        get() = preferenceManager.passcode

    override suspend fun updateToken(password: String): DataState<Unit> {
        return try {
            val result = preferenceManager.updateToken(password)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateTheme(theme: MifosThemeConfig): DataState<Unit> {
        return try {
            val result = preferenceManager.updateTheme(theme)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateTimeBasedTheme(theme: TimeBasedTheme): DataState<Unit> {
        return try {
            val result = preferenceManager.updateTimeBasedTheme(theme)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateUser(user: UserData): DataState<Unit> {
        return try {
            val result = preferenceManager.updateUserInfo(user)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateSettings(appSettings: AppSettings): DataState<Unit> {
        return try {
            val result = preferenceManager.updateSettingsInfo(appSettings)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateProfileImage(image: String): DataState<Unit> {
        return try {
            val result = preferenceManager.updateProfileImage(image)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateClientId(clientId: Long?): DataState<Unit> {
        return try {
            val result = preferenceManager.updateClientId(clientId!!)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun setSentTokenToServer(sent: Boolean): DataState<Unit> {
        return try {
            preferenceManager.setSentTokenToServer(sent)
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun saveGcmToken(token: String?): DataState<Unit> {
        return try {
            preferenceManager.saveGcmToken(token)
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun setFirstTimeState(firstTimeState: Boolean) {
        preferenceManager.setFirstTimeState(firstTimeState)
    }

    override suspend fun setShowOnboarding(showOnboarding: Boolean) {
        preferenceManager.setShowOnboarding(showOnboarding)
    }

    override suspend fun setLanguage(language: LanguageConfig) {
        preferenceManager.setLanguage(language)
    }

    override suspend fun setIsAuthenticated(isAuthenticated: Boolean) {
        preferenceManager.setIsAuthenticated(isAuthenticated)
    }

    override suspend fun setIsUnlocked(isUnlocked: Boolean) {
        preferenceManager.setIsUnlocked(isUnlocked)
    }

    override suspend fun setPasscode(passcode: String) {
        preferenceManager.setPasscode(passcode)
    }

    override suspend fun setSelectedServices(selectedServices: Set<String>?) {
        preferenceManager.setSelectedServices(selectedServices)
    }

    override val selectedServices: Set<String>?
        get() = preferenceManager.getSelectedServicesDirectly()

    override fun saveSelectedServices(services: Set<String>?) {
        preferenceManager.saveSelectedServicesDirectly(services)
    }

    override suspend fun logOut() {
        preferenceManager.clearInfo()
    }
}

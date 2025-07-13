/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.AppTheme
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.DarkThemeConfig
import org.mifos.mobile.core.model.LanguageConfig

interface UserPreferencesRepository {
    val userInfo: StateFlow<UserData>

    val settingsInfo: StateFlow<AppSettings>

    val token: StateFlow<String?>

    val clientId: StateFlow<Long?>

    val appTheme: StateFlow<AppTheme>

    val profileImage: String?

    val sentTokenToServer: StateFlow<Boolean>

    val gcmToken: StateFlow<String?>

    val observeLanguage: Flow<LanguageConfig>

    val observeDarkThemeConfig: Flow<DarkThemeConfig>

    val observeDynamicColorPreference: Flow<Boolean>

    suspend fun updateToken(password: String): DataState<Unit>

    suspend fun updateTheme(theme: AppTheme): DataState<Unit>

    suspend fun updateUser(user: UserData): DataState<Unit>

    suspend fun updateSettings(appSettings: AppSettings): DataState<Unit>

    suspend fun updateProfileImage(image: String): DataState<Unit>

    suspend fun updateClientId(clientId: Long?): DataState<Unit>

    suspend fun setSentTokenToServer(sent: Boolean): DataState<Unit>

    suspend fun saveGcmToken(token: String?): DataState<Unit>

    suspend fun setIsAuthenticated(isAuthenticated: Boolean)

    suspend fun setIsUnlocked(isUnlocked: Boolean)

    suspend fun setShowOnboarding(showOnboarding: Boolean)

    suspend fun setFirstTimeState(firstTimeState: Boolean)

    suspend fun setLanguage(language: LanguageConfig)

    suspend fun logOut(): Unit
}

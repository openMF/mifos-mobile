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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.AppTheme
import org.mifos.mobile.core.datastore.model.UserData
import org.mifospay.core.common.DataState

class UserPreferencesRepositoryImpl(
    private val preferenceManager: UserPreferencesDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    unconfinedDispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {
    private val unconfinedScope = CoroutineScope(unconfinedDispatcher)

    override val userInfo: Flow<UserData>
        get() = preferenceManager.userInfo

    override val settingsInfo: Flow<AppSettings>
        get() = preferenceManager.settingsInfo

    override val appTheme: StateFlow<AppTheme?>
        get() = preferenceManager.appTheme.stateIn(
            scope = unconfinedScope,
            initialValue = null,
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

    override suspend fun updateToken(token: String): DataState<Unit> {
        return try {
            val result = preferenceManager.updateToken(token)
            DataState.Success(result)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun updateTheme(theme: AppTheme): DataState<Unit> {
        return try {
            val result = preferenceManager.updateTheme(theme)
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

    override suspend fun logOut() {
        preferenceManager.clearInfo()
    }
}

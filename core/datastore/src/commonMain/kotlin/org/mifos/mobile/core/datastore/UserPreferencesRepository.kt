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
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.AppTheme
import org.mifos.mobile.core.datastore.model.UserData
import org.mifospay.core.common.DataState

interface UserPreferencesRepository {
    val userInfo: Flow<UserData>

    val settingsInfo: Flow<AppSettings>

    val token: StateFlow<String?>

    val clientId: StateFlow<Long?>

    val appTheme: StateFlow<AppTheme?>

    val profileImage: String?

    suspend fun updateToken(token: String): DataState<Unit>

    suspend fun updateTheme(theme: AppTheme): DataState<Unit>

    suspend fun updateUser(user: UserData): DataState<Unit>

    suspend fun updateSettings(appSettings: AppSettings): DataState<Unit>

    suspend fun updateProfileImage(image: String): DataState<Unit>

    suspend fun logOut(): Unit
}

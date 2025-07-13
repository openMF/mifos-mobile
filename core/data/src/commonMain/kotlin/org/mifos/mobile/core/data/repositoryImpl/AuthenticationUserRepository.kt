/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.Dispatcher
import org.mifos.mobile.core.common.MifosDispatchers
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.model.AuthState
import org.mifos.mobile.core.model.UserData

class AuthenticationUserRepository(
    private val preferencesHelper: UserPreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(MifosDispatchers.Unconfined)
    private val unconfinedDispatcher: CoroutineDispatcher,
) : UserDataRepository {

    private val unconfinedScope = CoroutineScope(unconfinedDispatcher)

    override val activeUserId: Long?
        get() = preferencesHelper.userInfo.value.userId

    override val userData: Flow<DataState<UserData>> = flow {
        try {
            val userData = UserData(
                isAuthenticated = !preferencesHelper.token.value.isNullOrEmpty(),
                userName = preferencesHelper.userInfo.firstOrNull()?.userName ?: "",
                clientId = preferencesHelper.clientId.value ?: 0,
            )
            emit(DataState.Success(userData))
        } catch (e: Exception) {
            emit(DataState.Error(e, null))
        }
    }.flowOn(ioDispatcher)

    override suspend fun logOut(): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                preferencesHelper.logOut()
            }
            DataState.Success("User logged out Successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override val authState: StateFlow<AuthState>
        get() = preferencesHelper.userInfo.zip(preferencesHelper.settingsInfo) { account, settings ->
            when {
                account.isAuthenticated && settings.isAuthenticated &&
                    account.base64EncodedAuthenticationKey != null ->
                    account.base64EncodedAuthenticationKey

                else -> null
            }
        }.map {
            if (it != null) AuthState.Authenticated(it) else AuthState.Unauthenticated
        }.stateIn(
            scope = unconfinedScope,
            started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
            initialValue = AuthState.Unauthenticated,
        )

    override val settingsState: StateFlow<AppSettings>
        get() = preferencesHelper.settingsInfo
}

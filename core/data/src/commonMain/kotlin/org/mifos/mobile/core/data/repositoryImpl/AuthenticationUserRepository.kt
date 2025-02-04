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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.UserData

class AuthenticationUserRepository(
    private val preferencesHelper: UserPreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : UserDataRepository {

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
}

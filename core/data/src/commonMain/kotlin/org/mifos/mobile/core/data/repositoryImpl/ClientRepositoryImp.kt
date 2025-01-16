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

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.encodeUtf8
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.ClientRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.KtorfitClient
import org.mifos.mobile.core.network.utils.BaseURL
import org.mifos.mobile.core.network.utils.KtorInterceptorRe
import org.mifospay.core.common.DataState
import org.mifospay.core.common.asDataStateFlow

class ClientRepositoryImp(
    private val dataManager: DataManager,
    private val preferencesHelper: UserPreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ClientRepository {

    override fun loadClient(): Flow<DataState<Page<Client>>> {
        return dataManager.clientsApi.clients()
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    /**
     * Save the authentication token from the server and the user ID.
     * The authentication token would be used for accessing the authenticated
     * APIs.
     *
     * @param user - The user that is to be saved.
     */
    override suspend fun saveAuthenticationTokenForSession(user: User): DataState<Unit> {
        return try {
            val authToken = Constants.BASIC + user.base64EncodedAuthenticationKey
            preferencesHelper.updateToken(authToken)
            reInitializeService()
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    override suspend fun setClientId(clientId: Long?): DataState<Unit> {
        return try {
            withContext(ioDispatcher) {
                preferencesHelper.updateClientId(clientId)
            }
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override fun reInitializeService() {
        val client = HttpClient {
            install(KtorInterceptorRe) {
                repository = preferencesHelper
            }
        }
        KtorfitClient.builder()
            .baseURL(BaseURL().url)
            .httpClient(client)
            .build()
    }

    override suspend fun updateAuthenticationToken(password: String): DataState<Unit> {
        return try {
            withContext(ioDispatcher) {
                val username = preferencesHelper.userInfo.firstOrNull()?.userName
                val authenticationToken = "Basic " + "$username:$password".encodeToBase64()
                preferencesHelper.updateToken(authenticationToken)
            }
            reInitializeService()
            DataState.Success(Unit)
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }
    private fun String.encodeToBase64(): String {
        return this.encodeUtf8().base64()
    }
}

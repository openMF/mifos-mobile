/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositories

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repositoryImpl.AccountsRepositoryImp
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.services.ClientService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class AccountsRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var accountsRepositoryImp: AccountsRepository

    open class StubClientService : ClientService {
        override fun clients(): Flow<PageResponseDto<ClientResponseDto>> = TODO()
        override fun getClientForId(clientId: Long): Flow<ClientResponseDto> = TODO()
        override fun getClientImage(clientId: Long): Flow<HttpResponse> = TODO()
        override fun getClientAccounts(clientId: Long): Flow<AccountsResponseDto> = TODO()
        override fun getAccounts(clientId: Long, accountType: String?): Flow<AccountsResponseDto> = TODO()
    }

    @BeforeTest
    fun setUp() {
        // Initialization moved to individual tests to allow custom client service behavior
    }

    @Test
    fun testLoadAccounts_Successful() = runTest(testDispatcher) {
        val mockAccounts = AccountsResponseDto()
        var passedClientId: Long? = null
        var passedAccountType: String? = null

        val clientService = object : StubClientService() {
            override fun getAccounts(clientId: Long, accountType: String?): Flow<AccountsResponseDto> {
                passedClientId = clientId
                passedAccountType = accountType
                return flowOf(mockAccounts)
            }
        }
        val dataManager = object : DataManager() {
            override val clientsApi: ClientService = clientService
        }
        accountsRepositoryImp = AccountsRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = accountsRepositoryImp.loadAccounts(123L, "savings")
        val item = resultFlow.drop(1).first()

        assertIs<DataState.Success<ClientAccounts>>(item)
        assertEquals(123L, passedClientId)
        assertEquals("savings", passedAccountType)
    }

    @Test
    fun testLoadAccounts_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        var passedClientId: Long? = null
        var passedAccountType: String? = null

        val clientService = object : StubClientService() {
            override fun getAccounts(clientId: Long, accountType: String?): Flow<AccountsResponseDto> {
                passedClientId = clientId
                passedAccountType = accountType
                throw Exception(errorMsg)
            }
        }
        val dataManager = object : DataManager() {
            override val clientsApi: ClientService = clientService
        }
        accountsRepositoryImp = AccountsRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = accountsRepositoryImp.loadAccounts(123L, "savings")
        val item = resultFlow.drop(1).first()

        val error = assertIs<DataState.Error<ClientAccounts>>(item)
        assertEquals(errorMsg, error.exception.message)
        assertEquals(123L, passedClientId)
        assertEquals("savings", passedAccountType)
    }
}

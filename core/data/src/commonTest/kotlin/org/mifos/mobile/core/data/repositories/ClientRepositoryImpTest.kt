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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ClientRepository
import org.mifos.mobile.core.data.repositoryImpl.ClientRepositoryImp
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.services.ClientService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class ClientRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var clientRepositoryImp: ClientRepository

    @BeforeTest
    fun setUp() {
        dataManager = object : DataManager() {}
        clientRepositoryImp = ClientRepositoryImp(dataManager, testDispatcher)
    }

    @Test
    fun testLoadClient_SuccessResponseReceivedFromDataManager_ReturnsClientPageSuccessfully() =
        runTest(testDispatcher) {
            val mockClientDto = ClientResponseDto()
            val successResponse = PageResponseDto(
                5,
                List(5) { mockClientDto },
            )

            dataManager = object : DataManager() {
                override val clientsApi: ClientService
                    get() = object : BaseFakeClientService() {
                        override fun clients(): Flow<PageResponseDto<ClientResponseDto>> {
                            return flowOf(successResponse)
                        }
                    }
            }
            clientRepositoryImp = ClientRepositoryImp(dataManager, testDispatcher)

            val resultFlow = clientRepositoryImp.loadClient()
            val item = resultFlow.drop(1).first()

            assertIs<DataState.Success<Page<Client>>>(item)
        }

    @Test
    fun testLoadClient_ErrorResponseReceivedFromDataManager_ReturnsError() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        dataManager = object : DataManager() {
            override val clientsApi: ClientService
                get() = object : BaseFakeClientService() {
                    override fun clients(): Flow<PageResponseDto<ClientResponseDto>> {
                        return flow { throw Exception(errorMsg) }
                    }
                }
        }
        clientRepositoryImp = ClientRepositoryImp(dataManager, testDispatcher)

        val resultFlow = clientRepositoryImp.loadClient()
        val item = resultFlow.drop(1).first()

        val error = assertIs<DataState.Error<Page<Client>>>(item)
        assertEquals(errorMsg, error.exception.message)
    }
}

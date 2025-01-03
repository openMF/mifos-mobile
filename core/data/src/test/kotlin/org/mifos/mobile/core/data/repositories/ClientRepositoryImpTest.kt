/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositories

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.Credentials
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repositoryImpl.ClientRepositoryImp
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.FakeRemoteDataSource
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit

@RunWith(MockitoJUnitRunner::class)
class ClientRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    @Mock
    lateinit var preferencesHelper: PreferencesHelper

    @Mock
    lateinit var retrofit: Retrofit

    @Mock
    lateinit var retrofitBuilder: Retrofit.Builder

    private var mockClientPage: Page<Client?>? = null
    private lateinit var clientRepositoryImp: ClientRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientRepositoryImp = ClientRepositoryImp(
            dataManager,
            preferencesHelper,
            retrofit,
        )
        mockClientPage = FakeRemoteDataSource.clients
    }

    @Test
    fun testLoadClient_SuccessResponseReceivedFromDataManager_ReturnsClientPageSuccessfully() =
        runTest {
            Dispatchers.setMain(Dispatchers.Unconfined)
            val successResponse: Page<Client> = Page(
                5,
                List(5) {
                    (mock(Client::class.java) as Client)
                },
            )
            `when`(
                dataManager.clients(),
            ).thenReturn(successResponse)

            val resultFlow = clientRepositoryImp.loadClient()
            resultFlow.test {
                Assert.assertEquals(successResponse, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).clients()
            Dispatchers.resetMain()
        }

    @Test(expected = Exception::class)
    fun testLoadClient_ErrorResponseReceivedFromDataManager_ReturnsError() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        Mockito.`when`(
            dataManager.clients(),
        ).thenThrow(Exception("Error occurred"))

        val result = clientRepositoryImp.loadClient()
        result.test {
            assert(Throwable("Error occurred") == awaitError())
        }
        Mockito.verify(dataManager).clients()
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateAuthenticationToken() {
        val mockPassword = "testPassword"
        val mockUsername = "testUsername"
        `when`(preferencesHelper.userName).thenReturn(mockUsername)
        `when`(retrofit.newBuilder()).thenReturn(retrofitBuilder)
        `when`(retrofitBuilder.client(any())).thenReturn(retrofitBuilder)
        clientRepositoryImp.updateAuthenticationToken(mockPassword)
        val authenticationToken = Credentials.basic(preferencesHelper.userName!!, mockPassword)

        Mockito.verify(preferencesHelper).saveToken(authenticationToken)
    }
}

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
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repositoryImpl.ThirdPartyTransferRepositoryImp
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ThirdPartyTransferRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var transferRepositoryImp: ThirdPartyTransferRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        transferRepositoryImp = ThirdPartyTransferRepositoryImp(dataManager)
    }

    @Test
    fun testThirdPartyTransferTemplate_Successful() = runTest {
        val response = Mockito.mock(AccountOptionsTemplate::class.java)

        `when`(dataManager.thirdPartyTransferTemplate()).thenReturn(response)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()
        result.test {
            assertEquals(response, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testThirdPartyTransferTemplate_Unsuccessful() = runTest {
        `when`(dataManager.thirdPartyTransferTemplate())
            .thenThrow(Exception("Error"))
        val result = transferRepositoryImp.thirdPartyTransferTemplate()
        result.test {
            assertEquals(Throwable("Error"), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repositoryImpl.RecentTransactionRepositoryImp
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecentTransactionRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var recentTransactionRepositoryImp: RecentTransactionRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        recentTransactionRepositoryImp = RecentTransactionRepositoryImp(dataManager)
    }

    @Test
    fun recentTransaction_successful_response_from_dataManger() = runTest {
        val success = mock(Page<Transaction>()::class.java)
        val offset = 0
        val limit = 50

        Mockito.`when`(dataManager.getRecentTransactions(offset, limit)).thenReturn(success)

        val result = recentTransactionRepositoryImp.recentTransactions(offset, limit)
        result.test {
            assertEquals(success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).getRecentTransactions(offset, limit)
    }

    @Test(expected = Exception::class)
    fun recentTransaction_unsuccessful_response_from_dataManger() = runTest {
        val offset = 0
        val limit = 50

        Mockito.`when`(dataManager.getRecentTransactions(offset, limit))
            .thenThrow(Exception("Error Occured in fetching recent transactions"))

        val result = recentTransactionRepositoryImp.recentTransactions(offset, limit)
        result.test {
            assertEquals(Throwable("Error Occured in fetching recent transactions"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).getRecentTransactions(offset, limit)
    }
}

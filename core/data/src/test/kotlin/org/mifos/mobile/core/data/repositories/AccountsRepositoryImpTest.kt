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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repositoryImpl.AccountsRepositoryImp
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class AccountsRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var accountsRepositoryImp: AccountsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountsRepositoryImp = AccountsRepositoryImp(dataManager)
    }

    @Test
    fun loadAccounts_Success() = runTest {
        val mockAccountType = "savings"
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(dataManager.getAccounts(mockAccountType)).thenReturn((mockClientAccounts))

        val resultFlow = accountsRepositoryImp.loadAccounts(mockAccountType)
        resultFlow.test {
            assertEquals(mockClientAccounts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun loadAccounts_Error() = runTest {
        val mockAccountType = "savings"
        `when`(dataManager.getAccounts(mockAccountType))
            .thenThrow(Exception("Error occurred"))
        val result = accountsRepositoryImp.loadAccounts(mockAccountType)
        result.test {
            assert(Throwable("Error occurred") == awaitError())
        }
    }
}

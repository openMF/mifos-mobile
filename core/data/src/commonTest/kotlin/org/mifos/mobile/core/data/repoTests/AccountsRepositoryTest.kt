/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repoTests

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.fakeServices.FakeClientService
import org.mifos.mobile.core.data.fakeServices.FakeDataManager
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repositoryImpl.AccountsRepositoryImp
import org.mifos.mobile.core.data.utils.TestFixtures
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: AccountsRepository
    private lateinit var accounts: ClientAccounts

    @BeforeTest
    fun setup() {
        accounts = TestFixtures.clientAccounts

        val fakeClientService = FakeClientService(
            accounts = accounts,
        )

        val apiProvider = FakeDataManager(
            clientsApi = fakeClientService,
        )

        repository = AccountsRepositoryImp(
            dataManager = apiProvider,
            ioDispatcher = dispatcher,
        )
    }

    @Test
    fun `load accounts emits Loading then Success`() = runTest {
        repository.loadAccounts(1L, Constants.SAVINGS_ACCOUNT)
            .test {
                assertTrue(awaitItem() is DataState.Loading)

                val success = awaitItem() as DataState.Success
                assertEquals(
                    accounts.savingsAccounts,
                    success.data.savingsAccounts,
                )

                assertEquals(
                    accounts.loanAccounts,
                    success.data.loanAccounts,
                )

                assertEquals(
                    accounts.shareAccounts,
                    success.data.shareAccounts,
                )

                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `load accounts emits Loading then Success with empty data`() = runTest {
        val emptyAccounts = ClientAccounts(
            loanAccounts = emptyList(),
            savingsAccounts = emptyList(),
            shareAccounts = emptyList(),
        )

        val emptyService = FakeClientService(
            accounts = emptyAccounts,
        )

        val apiProvider = FakeDataManager(
            clientsApi = emptyService,
        )

        repository = AccountsRepositoryImp(
            dataManager = apiProvider,
            ioDispatcher = dispatcher,
        )

        repository.loadAccounts(1L, Constants.SAVINGS_ACCOUNT)
            .test {
                assertTrue(awaitItem() is DataState.Loading)

                val success = awaitItem() as DataState.Success
                val data = success.data

                assertEquals(data.savingsAccounts?.isEmpty(), true)
                assertTrue(data.loanAccounts.isEmpty())
                assertTrue(data.shareAccounts.isEmpty())

                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `load accounts emits Loading then Error when service fails`() = runTest {
        val failingService = FakeClientService(
            shouldFail = true,
        )

        val apiProvider = FakeDataManager(
            clientsApi = failingService,
        )

        repository = AccountsRepositoryImp(
            dataManager = apiProvider,
            ioDispatcher = dispatcher,
        )

        repository.loadAccounts(1L, Constants.SAVINGS_ACCOUNT)
            .test {
                assertTrue(awaitItem() is DataState.Loading)

                val error = awaitItem()
                assertTrue(error is DataState.Error)

                cancelAndIgnoreRemainingEvents()
            }
    }
}

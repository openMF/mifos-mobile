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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repositoryImpl.RecentTransactionRepositoryImp
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.dto.transaction.TransactionResponseDto
import org.mifos.mobile.core.network.services.RecentTransactionsService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class RecentTransactionRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var recentTransactionRepositoryImp: RecentTransactionRepositoryImp

    @BeforeTest
    fun setUp() {
        dataManager = object : DataManager() {}
        recentTransactionRepositoryImp = RecentTransactionRepositoryImp(dataManager, testDispatcher)
    }

    @Test
    fun recentTransaction_successful_response_from_dataManger() = runTest(testDispatcher) {
        val success = PageResponseDto<TransactionResponseDto>()
        val offset = 0
        val limit = 50
        val clientId = 1L

        var apiCalled = false

        val apiFake = object : BaseFakeRecentTransactionsService() {
            override fun getRecentTransactionsList(
                clientId: Long,
                offset: Int?,
                limit: Int?,
            ): kotlinx.coroutines.flow.Flow<PageResponseDto<TransactionResponseDto>> {
                apiCalled = true
                return flowOf(success)
            }
        }

        dataManager = object : DataManager() {
            override val recentTransactionsApi: RecentTransactionsService
                get() = apiFake
        }
        recentTransactionRepositoryImp = RecentTransactionRepositoryImp(dataManager, testDispatcher)

        val result = recentTransactionRepositoryImp.recentTransactions(clientId, offset, limit)

        val item = result.drop(1).first()
        assertIs<DataState.Success<Page<Transaction>>>(item)
        assertIs<Boolean>(apiCalled)
        assertEquals(true, apiCalled)
    }

    @Test
    fun recentTransaction_unsuccessful_response_from_dataManger() = runTest(testDispatcher) {
        val offset = 0
        val limit = 50
        val clientId = 1L
        val errorMessage = "Error Occured in fetching recent transactions"

        var apiCalled = false
        val apiFake = object : BaseFakeRecentTransactionsService() {
            override fun getRecentTransactionsList(
                clientId: Long,
                offset: Int?,
                limit: Int?,
            ): kotlinx.coroutines.flow.Flow<PageResponseDto<TransactionResponseDto>> {
                apiCalled = true
                return flow { throw Exception(errorMessage) }
            }
        }

        dataManager = object : DataManager() {
            override val recentTransactionsApi: RecentTransactionsService
                get() = apiFake
        }
        recentTransactionRepositoryImp = RecentTransactionRepositoryImp(dataManager, testDispatcher)

        val result = recentTransactionRepositoryImp.recentTransactions(clientId, offset, limit)

        val item = result.drop(1).first()
        val error = assertIs<DataState.Error<Page<Transaction>>>(item)
        assertEquals(errorMessage, error.exception.message)
        assertIs<Boolean>(apiCalled)
        assertEquals(true, apiCalled)
    }
}

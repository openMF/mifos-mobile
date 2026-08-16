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

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.repositoryImpl.LoanRepositoryImp
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.loanAccount.LoanWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.payloads.LoanWithdrawPayloadDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanTemplateResponseDto
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class LoanRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var loanRepositoryImp: LoanRepository

    @BeforeTest
    fun setUp() {
        dataManager = object : DataManager() {}
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)
    }

    @Test
    fun testGetLoanWithAssociations_Successful() = runTest(testDispatcher) {
        val success = LoanWithAssociationsResponseDto()

        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override fun getLoanWithAssociations(loanId: Long, associationType: String?) = flowOf(success)
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.getLoanWithAssociations(
            "associationType",
            1,
        )
        val item = result.first()
        assertIs<DataState.Success<LoanWithAssociations?>>(item)
    }

    @Test
    fun testGetLoanWithAssociations_Unsuccessful() = runTest(testDispatcher) {
        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override fun getLoanWithAssociations(
                    loanId: Long,
                    associationType: String?,
                ) = throw Exception("Error occurred")
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.getLoanWithAssociations(
            "associationType",
            1,
        )
        val item = result.first()
        val error = assertIs<DataState.Error<LoanWithAssociations?>>(item)
        assertEquals("Error occurred", error.exception.message)
    }

    @Test
    fun testWithdrawLoanAccount_Successful() = runTest(testDispatcher) {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond("Success")
                }
            }
        }
        val successResponse = client.get("")

        val loanWithdraw = LoanWithdraw()

        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override suspend fun withdrawLoanAccount(
                    loanId: Long,
                    loanWithdraw: LoanWithdrawPayloadDto?,
                ) = successResponse
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.withdrawLoanAccount(1, loanWithdraw)

        val item = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", item.data)
    }

    @Test
    fun testWithdrawLoanAccount_Unsuccessful() = runTest(testDispatcher) {
        val loanWithdraw = LoanWithdraw()
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { respond("Error occurred", HttpStatusCode.BadRequest) }
            }
        }
        val errorResponse = client.get("")

        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override suspend fun withdrawLoanAccount(
                    loanId: Long,
                    loanWithdraw: LoanWithdrawPayloadDto?,
                ) = throw ClientRequestException(errorResponse, "Error occurred")
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.withdrawLoanAccount(1, loanWithdraw)

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals("Error occurred", error.exception.message)
    }

    @Test
    fun testTemplate_Successful() = runTest(testDispatcher) {
        val success = LoanTemplateResponseDto()

        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override fun getLoanTemplate(clientId: Long?) = flowOf(success)
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.template(1)

        val item = result.drop(1).first()
        assertIs<DataState.Success<LoanTemplate?>>(item)
    }

    @Test
    fun testTemplate_Unsuccessful() = runTest(testDispatcher) {
        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override fun getLoanTemplate(clientId: Long?) = throw Exception("Error occurred")
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.template(1)
        val item = result.drop(1).first()
        val error = assertIs<DataState.Error<LoanTemplate?>>(item)
        assertEquals("Error occurred", error.exception.message)
    }

    @Test
    fun testGetLoanTemplateByProduct_Successful() = runTest(testDispatcher) {
        val success = LoanTemplateResponseDto()

        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override fun getLoanTemplateByProduct(clientId: Long?, productId: Int?) = flowOf(success)
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.getLoanTemplateByProduct(1, 1)
        val item = result.drop(1).first()
        assertIs<DataState.Success<LoanTemplate?>>(item)
    }

    @Test
    fun testGetLoanTemplateByProduct_Unsuccessful() = runTest(testDispatcher) {
        dataManager = object : DataManager() {
            override val loanAccountsListApi = object : BaseFakeLoanAccountsListService() {
                override fun getLoanTemplateByProduct(
                    clientId: Long?,
                    productId: Int?,
                ) = throw Exception("Error occurred")
            }
        }
        loanRepositoryImp = LoanRepositoryImp(dataManager, testDispatcher)

        val result = loanRepositoryImp.getLoanTemplateByProduct(1, 1)
        val item = result.drop(1).first()
        val error = assertIs<DataState.Error<LoanTemplate?>>(item)
        assertEquals("Error occurred", error.exception.message)
    }
}

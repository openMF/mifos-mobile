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
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repositoryImpl.TransferRepositoryImp
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.payloads.TransferPayloadDto
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TransferRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var transferProcessImp: TransferRepositoryImp

    @BeforeTest
    fun setUp() {
        dataManager = object : DataManager() {}
        transferProcessImp = TransferRepositoryImp(dataManager, testDispatcher)
    }

    private suspend fun createMockHttpResponse(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ): HttpResponse {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(content, status, headersOf("Content-Type", "application/json"))
                }
            }
        }
        return client.get("/")
    }

    @Test
    fun makeThirdPartyTransfer_successful() = runTest(testDispatcher) {
        val successResponse = createMockHttpResponse("""{"resourceId": 123}""")

        val transferPayload = TransferPayload(
            fromOfficeId = 1,
            fromClientId = 2,
            fromAccountType = 3,
            fromAccountId = "4",
            toOfficeId = 5,
            toClientId = 6,
            toAccountType = 7,
            toAccountId = "8",
            transferDate = "06 July 2023",
            transferAmount = 100.0,
            transferDescription = "Transfer",
            dateFormat = "dd MMMM yyyy",
            locale = "en",
        )

        dataManager = object : DataManager() {
            override val thirdPartyTransferApi = object : BaseFakeThirdPartyTransferService() {
                override suspend fun makeTransfer(transferPayload: TransferPayloadDto?): HttpResponse {
                    return successResponse
                }
            }
        }
        transferProcessImp = TransferRepositoryImp(dataManager, testDispatcher)

        val result = transferProcessImp.makeTransfer(
            transferPayload,
            TransferType.TPT,
        )

        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("123", data.data)
    }

    @Test
    fun makeSavingsTransfer_successful() = runTest(testDispatcher) {
        val successResponse = createMockHttpResponse("""{"resourceId": 456}""")

        val transferPayload = TransferPayload(
            fromOfficeId = 1,
            fromClientId = 2,
            fromAccountType = 3,
            fromAccountId = "4",
            toOfficeId = 5,
            toClientId = 6,
            toAccountType = 7,
            toAccountId = "8",
            transferDate = "06 July 2023",
            transferAmount = 100.0,
            transferDescription = "Transfer",
            dateFormat = "dd MMMM yyyy",
            locale = "en",
        )

        dataManager = object : DataManager() {
            override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                override suspend fun makeTransfer(transferPayload: TransferPayloadDto?): HttpResponse {
                    return successResponse
                }
            }
        }
        transferProcessImp = TransferRepositoryImp(dataManager, testDispatcher)

        val result = transferProcessImp.makeTransfer(
            transferPayload,
            TransferType.SELF,
        )

        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("456", data.data)
    }

    @Test
    fun makeThirdPartyTransfer_unsuccessful() = runTest(testDispatcher) {
        val transferPayload = TransferPayload(
            fromOfficeId = 1,
            fromClientId = 2,
            fromAccountType = 3,
            fromAccountId = "4",
            toOfficeId = 5,
            toClientId = 6,
            toAccountType = 7,
            toAccountId = "8",
            transferDate = "06 July 2023",
            transferAmount = 100.0,
            transferDescription = "Transfer",
            dateFormat = "dd MMMM yyyy",
            locale = "en",
        )

        dataManager = object : DataManager() {
            override val thirdPartyTransferApi = object : BaseFakeThirdPartyTransferService() {
                override suspend fun makeTransfer(transferPayload: TransferPayloadDto?): HttpResponse {
                    throw ClientRequestException(
                        createMockHttpResponse("Error occurred", HttpStatusCode.BadRequest),
                        "Error occurred",
                    )
                }
            }
        }
        transferProcessImp = TransferRepositoryImp(dataManager, testDispatcher)

        val result = transferProcessImp.makeTransfer(
            transferPayload,
            TransferType.TPT,
        )

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals("Error occurred", error.exception.message)
    }

    @Test
    fun makeSavingsTransfer_unsuccessful() = runTest(testDispatcher) {
        val transferPayload = TransferPayload(
            fromOfficeId = 1,
            fromClientId = 2,
            fromAccountType = 3,
            fromAccountId = "4",
            toOfficeId = 5,
            toClientId = 6,
            toAccountType = 7,
            toAccountId = "8",
            transferDate = "06 July 2023",
            transferAmount = 100.0,
            transferDescription = "Transfer",
            dateFormat = "dd MMMM yyyy",
            locale = "en",
        )

        dataManager = object : DataManager() {
            override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                override suspend fun makeTransfer(transferPayload: TransferPayloadDto?): HttpResponse {
                    throw ClientRequestException(
                        createMockHttpResponse("Error occurred", HttpStatusCode.BadRequest),
                        "Error occurred",
                    )
                }
            }
        }
        transferProcessImp = TransferRepositoryImp(dataManager, testDispatcher)

        val result = transferProcessImp.makeTransfer(
            transferPayload,
            TransferType.SELF,
        )

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals("Error occurred", error.exception.message)
    }
}

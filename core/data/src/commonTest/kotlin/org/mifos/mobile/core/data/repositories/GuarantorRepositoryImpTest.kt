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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.data.repositoryImpl.GuarantorRepositoryImp
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.guarantor.GuarantorListResponseDto
import org.mifos.mobile.core.network.dto.guarantor.GuarantorTemplateResponseDto
import org.mifos.mobile.core.network.dto.payloads.GuarantorApplicationPayloadDto
import org.mifos.mobile.core.network.services.GuarantorService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class GuarantorRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var guarantorRepositoryImp: GuarantorRepository

    private suspend fun getFakeHttpResponse(
        content: String = "Success",
        status: HttpStatusCode = HttpStatusCode.OK,
    ): HttpResponse {
        val client = HttpClient(
            MockEngine {
                respond(content, status)
            },
        )
        return client.get("/")
    }

    @BeforeTest
    fun setUp() {
        // Initialization moved to individual tests
    }

    @Test
    fun testGetGuarantorTemplate_Successful() = runTest(testDispatcher) {
        val success = GuarantorTemplateResponseDto()

        val guarantorService = object : BaseFakeGuarantorService() {
            override fun getGuarantorTemplate(loanId: Long): Flow<GuarantorTemplateResponseDto> {
                return flowOf(success)
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.getGuarantorTemplate(123L)

        val item = result.drop(1).first()
        assertIs<DataState.Success<GuarantorTemplatePayload?>>(item)
    }

    @Test
    fun testGetGuarantorTemplate_Unsuccessful() = runTest(testDispatcher) {
        val guarantorService = object : BaseFakeGuarantorService() {
            override fun getGuarantorTemplate(loanId: Long): Flow<GuarantorTemplateResponseDto> {
                return flow { throw Exception("Error") }
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.getGuarantorTemplate(123L)
        val item = result.drop(1).first()
        val error = assertIs<DataState.Error<GuarantorTemplatePayload?>>(item)
        assertEquals("Error", error.exception.message)
    }

    @Test
    fun testCreateGuarantor_Successful() = runTest(testDispatcher) {
        val payload = GuarantorApplicationPayload()
        val guarantorService = object : BaseFakeGuarantorService() {
            override suspend fun createGuarantor(loanId: Long, payload: GuarantorApplicationPayloadDto?): HttpResponse {
                return getFakeHttpResponse("Success")
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.createGuarantor(123L, payload)
        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testCreateGuarantor_Unsuccessful() = runTest(testDispatcher) {
        val payload = GuarantorApplicationPayload()
        val guarantorService = object : BaseFakeGuarantorService() {
            override suspend fun createGuarantor(loanId: Long, payload: GuarantorApplicationPayloadDto?): HttpResponse {
                throw ClientRequestException(
                    getFakeHttpResponse(
                        content = "{\"defaultUserMessage\":\"Error\"}",
                        status = HttpStatusCode.BadRequest,
                    ),
                    "{\"defaultUserMessage\":\"Error\"}",
                )
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.createGuarantor(123L, payload)
        val error = assertIs<DataState.Error<String>>(result)
        assertEquals("Error", error.exception.message)
    }

    @Test
    fun testUpdateGuarantor_Successful() = runTest(testDispatcher) {
        val payload = GuarantorApplicationPayload()
        val guarantorService = object : BaseFakeGuarantorService() {
            override suspend fun updateGuarantor(
                payload: GuarantorApplicationPayloadDto?,
                loanId: Long,
                guarantorId: Long,
            ): HttpResponse {
                return getFakeHttpResponse("Success")
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)
        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful() = runTest(testDispatcher) {
        val payload = GuarantorApplicationPayload()
        val guarantorService = object : BaseFakeGuarantorService() {
            override suspend fun updateGuarantor(
                payload: GuarantorApplicationPayloadDto?,
                loanId: Long,
                guarantorId: Long,
            ): HttpResponse {
                throw ClientRequestException(
                    getFakeHttpResponse(
                        content = "{\"defaultUserMessage\":\"Error\"}",
                        status = HttpStatusCode.BadRequest,
                    ),
                    "{\"defaultUserMessage\":\"Error\"}",
                )
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)
        val error = assertIs<DataState.Error<String>>(result)
        assertEquals("Error", error.exception.message)
    }

    @Test
    fun testDeleteGuarantor_Successful() = runTest(testDispatcher) {
        val guarantorService = object : BaseFakeGuarantorService() {
            override suspend fun deleteGuarantor(loanId: Long, guarantorId: Long): HttpResponse {
                return getFakeHttpResponse("Success")
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.deleteGuarantor(1L, 2L)
        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testDeleteGuarantor_Unsuccessful() = runTest(testDispatcher) {
        val guarantorService = object : BaseFakeGuarantorService() {
            override suspend fun deleteGuarantor(loanId: Long, guarantorId: Long): HttpResponse {
                throw ClientRequestException(
                    getFakeHttpResponse(
                        content = "{\"defaultUserMessage\":\"Error\"}",
                        status = HttpStatusCode.BadRequest,
                    ),
                    "{\"defaultUserMessage\":\"Error\"}",
                )
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.deleteGuarantor(1L, 2L)
        val error = assertIs<DataState.Error<String>>(result)
        assertEquals("Error", error.exception.message)
    }

    @Test
    fun testGetGuarantorList_Successful() = runTest(testDispatcher) {
        val guarantorService = object : BaseFakeGuarantorService() {
            override fun getGuarantorList(loanId: Long): Flow<List<GuarantorListResponseDto>> {
                return flowOf(List(4) { GuarantorListResponseDto() })
            }
        }
        val dataManager = object : DataManager() {
            override val guarantorApi: GuarantorService = guarantorService
        }
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager, testDispatcher)

        val result = guarantorRepositoryImp.getGuarantorList(123L)
        val item = result.drop(1).first()
        val data = assertIs<DataState.Success<List<GuarantorPayload?>?>>(item)
        assertEquals(4, data.data?.size)
    }
}

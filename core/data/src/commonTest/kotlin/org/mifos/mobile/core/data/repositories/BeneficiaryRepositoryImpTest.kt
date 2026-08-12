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
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repositoryImpl.BeneficiaryRepositoryImp
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.beneficiary.BeneficiaryListResponseDto
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryCreatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryUpdatePayloadDto
import org.mifos.mobile.core.network.dto.templates.beneficiary.BeneficiaryTemplateDto
import org.mifos.mobile.core.network.services.BeneficiaryService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class BeneficiaryRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()

    private suspend fun fakeHttpResponse(content: String = "Success"): HttpResponse {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { respond(content, HttpStatusCode.OK) }
            }
        }
        return client.get("")
    }

    private fun createRepository(fakeService: BeneficiaryService): BeneficiaryRepositoryImp {
        val dataManager = object : DataManager() {
            override val beneficiaryApi: BeneficiaryService = fakeService
        }
        return BeneficiaryRepositoryImp(dataManager, testDispatcher)
    }

    @Test
    fun testBeneficiaryTemplate_Successful() = runTest(testDispatcher) {
        val success = BeneficiaryTemplateDto()
        val fakeService = object : BaseFakeBeneficiaryService() {
            override fun beneficiaryTemplate() = flowOf(success)
        }
        val repository = createRepository(fakeService)

        val resultFlow = repository.beneficiaryTemplate()
        val item = resultFlow.drop(1).first()
        assertIs<DataState.Success<BeneficiaryTemplate>>(item)
    }

    @Test
    fun testBeneficiaryTemplate_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        val fakeService = object : BaseFakeBeneficiaryService() {
            override fun beneficiaryTemplate(): kotlinx.coroutines.flow.Flow<BeneficiaryTemplateDto> {
                throw Exception(errorMsg)
            }
        }
        val repository = createRepository(fakeService)

        val resultFlow = repository.beneficiaryTemplate()
        val item = resultFlow.drop(1).first()
        val error = assertIs<DataState.Error<BeneficiaryTemplate>>(item)
        assertEquals(errorMsg, error.exception.message)
    }

    @Test
    fun testCreateBeneficiary_Successful() = runTest(testDispatcher) {
        val beneficiaryPayload = BeneficiaryPayload()
        val fakeService = object : BaseFakeBeneficiaryService() {
            override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryCreatePayloadDto?): HttpResponse {
                return fakeHttpResponse("Success")
            }
        }
        val repository = createRepository(fakeService)

        val result = repository.createBeneficiary(beneficiaryPayload)
        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testCreateBeneficiary_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        val beneficiaryPayload = BeneficiaryPayload()
        val fakeService = object : BaseFakeBeneficiaryService() {
            override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryCreatePayloadDto?): HttpResponse {
                throw Exception(errorMsg)
            }
        }
        val repository = createRepository(fakeService)

        val result = repository.createBeneficiary(beneficiaryPayload)
        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMsg, error.exception.message)
    }

    @Test
    fun testUpdateBeneficiary_Successful() = runTest(testDispatcher) {
        val beneficiaryUpdatePayload = BeneficiaryUpdatePayload()
        val fakeService = object : BaseFakeBeneficiaryService() {
            override suspend fun updateBeneficiary(
                beneficiaryId: Long,
                payload: BeneficiaryUpdatePayloadDto?,
            ): HttpResponse {
                return fakeHttpResponse("Success")
            }
        }
        val repository = createRepository(fakeService)

        val result = repository.updateBeneficiary(123L, beneficiaryUpdatePayload)
        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testUpdateBeneficiary_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        val beneficiaryUpdatePayload = BeneficiaryUpdatePayload()
        val fakeService = object : BaseFakeBeneficiaryService() {
            override suspend fun updateBeneficiary(
                beneficiaryId: Long,
                payload: BeneficiaryUpdatePayloadDto?,
            ): HttpResponse {
                throw Exception(errorMsg)
            }
        }
        val repository = createRepository(fakeService)

        val result = repository.updateBeneficiary(123L, beneficiaryUpdatePayload)
        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMsg, error.exception.message)
    }

    @Test
    fun testDeleteBeneficiary_Successful() = runTest(testDispatcher) {
        val fakeService = object : BaseFakeBeneficiaryService() {
            override suspend fun deleteBeneficiary(beneficiaryId: Long): HttpResponse {
                return fakeHttpResponse("Success")
            }
        }
        val repository = createRepository(fakeService)

        val result = repository.deleteBeneficiary(123L)
        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testDeleteBeneficiary_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        val fakeService = object : BaseFakeBeneficiaryService() {
            override suspend fun deleteBeneficiary(beneficiaryId: Long): HttpResponse {
                throw Exception(errorMsg)
            }
        }
        val repository = createRepository(fakeService)

        val result = repository.deleteBeneficiary(123L)
        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMsg, error.exception.message)
    }

    @Test
    fun testBeneficiaryList_Successful() = runTest(testDispatcher) {
        val success = List(5) { BeneficiaryListResponseDto() }
        val fakeService = object : BaseFakeBeneficiaryService() {
            override fun beneficiaryList() = flowOf(success)
        }
        val repository = createRepository(fakeService)

        val resultFlow = repository.beneficiaryList()
        val item = resultFlow.drop(1).first()
        val data = assertIs<DataState.Success<List<Beneficiary>>>(item)
        assertEquals(5, data.data.size)
    }

    @Test
    fun testBeneficiaryList_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"
        val fakeService = object : BaseFakeBeneficiaryService() {
            override fun beneficiaryList(): kotlinx.coroutines.flow.Flow<List<BeneficiaryListResponseDto>> {
                throw Exception(errorMsg)
            }
        }
        val repository = createRepository(fakeService)

        val resultFlow = repository.beneficiaryList()
        val item = resultFlow.drop(1).first()
        val error = assertIs<DataState.Error<List<Beneficiary>>>(item)
        assertEquals(errorMsg, error.exception.message)
    }
}

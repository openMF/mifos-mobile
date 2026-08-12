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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.repositoryImpl.SavingsAccountRepositoryImp
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountUpdatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountWithdrawPayloadDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsAccountApplicationPayloadDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsTemplateResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsAccountTemplateResponseDto
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class SavingsAccountRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var savingsAccountRepositoryImp: SavingsAccountRepository
    private val mockAccountId = 1L
    private val mockAssociationType = Constants.TRANSACTIONS
    private val mockClientId = 1L

    private suspend fun getFakeHttpResponse(content: String = "Success"): HttpResponse {
        val client = HttpClient(
            MockEngine {
                respond(content, HttpStatusCode.OK)
            },
        )
        return client.get("/")
    }

    @BeforeTest
    fun setUp() {
    }

    @Test
    fun testGetSavingsWithAssociations_SuccessResponseReceivedFromDataManager_ReturnsSuccess() =
        runTest(testDispatcher) {
            val response = SavingsWithAssociationsResponseDto()
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override fun getSavingsWithAssociations(
                        accountId: Long,
                        associationType: String?,
                    ): Flow<SavingsWithAssociationsResponseDto> {
                        // Cast is safe in context of mocked generic response structure
                        @Suppress("UNCHECKED_CAST")
                        return flowOf(response as SavingsWithAssociationsResponseDto)
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result = savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType,
            )

            val item = result.drop(1).first()
            assertIs<DataState.Success<SavingsWithAssociations>>(item)
        }

    @Test
    fun testGetSavingsWithAssociations_ErrorResponseReceivedFromDataManager_ReturnsError() =
        runTest(testDispatcher) {
            val errorMessage = "Error occurred"
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override fun getSavingsWithAssociations(
                        accountId: Long,
                        associationType: String?,
                    ): Flow<SavingsWithAssociationsResponseDto> {
                        throw Exception(errorMessage)
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result = savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType,
            )

            val item = result.drop(1).first()
            val error = assertIs<DataState.Error<SavingsWithAssociations>>(item)
            assertEquals(errorMessage, error.exception.message)
        }

    @Test
    fun testGetSavingsAccountApplicationTemplate_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest(testDispatcher) {
            val response = SavingsAccountTemplateResponseDto()
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override fun getSavingsAccountApplicationTemplate(
                        clientId: Long?,
                    ): Flow<SavingsAccountTemplateResponseDto> {
                        @Suppress("UNCHECKED_CAST")
                        return flowOf(response as SavingsAccountTemplateResponseDto)
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result =
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)

            val item = result.drop(1).first()
            assertIs<DataState.Success<SavingsAccountTemplate>>(item)
        }

    @Test
    fun testGetSavingsAccountApplicationTemplate_ErrorResponseFromDataManager_ReturnsError() =
        runTest(testDispatcher) {
            val errorMessage = "Error occurred"
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override fun getSavingsAccountApplicationTemplate(
                        clientId: Long?,
                    ): Flow<SavingsAccountTemplateResponseDto> {
                        throw Exception(errorMessage)
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result =
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)

            val item = result.drop(1).first()
            val error = assertIs<DataState.Error<SavingsAccountTemplate>>(item)
            assertEquals(errorMessage, error.exception.message)
        }

    @Test
    fun testSubmitSavingAccountApplication_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest(testDispatcher) {
            val mockPayload = SavingsAccountApplicationPayload(
                locale = "en",
                dateFormat = "dd MMMM yyyy",
                monthDayFormat = "dd MMM",
            )
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override suspend fun submitSavingAccountApplication(
                        payload: SavingsAccountApplicationPayloadDto?,
                    ): HttpResponse {
                        return getFakeHttpResponse("Success")
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result = savingsAccountRepositoryImp.submitSavingAccountApplication(
                mockPayload,
            )

            val data = assertIs<DataState.Success<String>>(result)
            assertEquals("Success", data.data)
        }

    @Test
    fun testSubmitSavingAccountApplication_ErrorResponseFromDataManager_ReturnsError() =
        runTest(testDispatcher) {
            val mockPayload = SavingsAccountApplicationPayload(
                locale = "en",
                dateFormat = "dd MMMM yyyy",
                monthDayFormat = "dd MMM",
            )
            val errorMessage = "Error occurred"

            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override suspend fun submitSavingAccountApplication(
                        payload: SavingsAccountApplicationPayloadDto?,
                    ): HttpResponse {
                        throw Exception(errorMessage)
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result = savingsAccountRepositoryImp.submitSavingAccountApplication(
                mockPayload,
            )

            val error = assertIs<DataState.Error<String>>(result)
            assertEquals(errorMessage, error.exception.message)
        }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromDataManager_ReturnsSuccess() = runTest(testDispatcher) {
        val mockPayload = SavingsAccountUpdatePayload()
        val dataManager = object : DataManager() {
            override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                override suspend fun updateSavingsAccountUpdate(
                    accountsId: Long,
                    payload: SavingsAccountUpdatePayloadDto?,
                ): HttpResponse {
                    return getFakeHttpResponse("Success")
                }
            }
        }
        savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

        val result = savingsAccountRepositoryImp.updateSavingsAccount(
            mockAccountId,
            mockPayload,
        )

        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Success", data.data)
    }

    @Test
    fun testUpdateSavingsAccount_ErrorResponseFromDataManager_ReturnsError() = runTest(testDispatcher) {
        val mockPayload = SavingsAccountUpdatePayload()
        val errorMessage = "Error occurred"

        val dataManager = object : DataManager() {
            override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                override suspend fun updateSavingsAccountUpdate(
                    accountsId: Long,
                    payload: SavingsAccountUpdatePayloadDto?,
                ): HttpResponse {
                    throw Exception(errorMessage)
                }
            }
        }
        savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

        val result = savingsAccountRepositoryImp.updateSavingsAccount(
            mockAccountId,
            mockPayload,
        )

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMessage, error.exception.message)
    }

    @Test
    fun testSubmitWithdrawSavingsAccount_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest(testDispatcher) {
            val mockPayload = SavingsAccountWithdrawPayload()
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override suspend fun submitWithdrawSavingsAccount(
                        savingsId: Long,
                        payload: SavingsAccountWithdrawPayloadDto?,
                    ): HttpResponse {
                        return getFakeHttpResponse("Success")
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                mockAccountId,
                mockPayload,
            )

            val data = assertIs<DataState.Success<String>>(result)
            assertEquals("Success", data.data)
        }

    @Test
    fun testSubmitWithdrawSavingsAccount_ErrorResponseFromDataManager_ReturnsError() = runTest(testDispatcher) {
        val mockPayload = SavingsAccountWithdrawPayload()
        val errorMessage = "Error occurred"

        val dataManager = object : DataManager() {
            override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                override suspend fun submitWithdrawSavingsAccount(
                    savingsId: Long,
                    payload: SavingsAccountWithdrawPayloadDto?,
                ): HttpResponse {
                    throw Exception(errorMessage)
                }
            }
        }
        savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

        val result = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
            mockAccountId,
            mockPayload,
        )

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMessage, error.exception.message)
    }

    @Test
    fun testLoanAccountTransferTemplate_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest(testDispatcher) {
            val response = AccountOptionsTemplateResponseDto()
            val dataManager = object : DataManager() {
                override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                    override fun accountTransferTemplate(
                        accountId: Long?,
                        accountType: Long?,
                    ): Flow<AccountOptionsTemplateResponseDto> {
                        return flowOf(response)
                    }
                }
            }
            savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

            val result = savingsAccountRepositoryImp.accountTransferTemplate(1L, 2L)

            val item = result.drop(1).first()
            assertIs<DataState.Success<AccountOptionsTemplate>>(item)
        }

    @Test
    fun testLoanAccountTransferTemplate_ErrorResponseFromDataManager_ReturnsError() = runTest(testDispatcher) {
        val errorMessage = "Error occurred"
        val dataManager = object : DataManager() {
            override val savingAccountsListApi = object : BaseFakeSavingAccountsListService() {
                override fun accountTransferTemplate(
                    accountId: Long?,
                    accountType: Long?,
                ): Flow<AccountOptionsTemplateResponseDto> {
                    throw Exception(errorMessage)
                }
            }
        }
        savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager, testDispatcher)

        val result = savingsAccountRepositoryImp.accountTransferTemplate(1L, 2L)

        val item = result.drop(1).first()
        val error = assertIs<DataState.Error<AccountOptionsTemplate>>(item)
        assertEquals(errorMessage, error.exception.message)
    }
}

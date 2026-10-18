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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.repositoryImpl.ClientChargeRepositoryImp
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.charges.ChargeResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareWithAssociationsResponseDto
import org.mifos.mobile.core.network.services.ClientChargeService
import org.mifos.mobile.core.network.services.ShareAccountService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class ClientChargeRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var clientChargeRepositoryImp: ClientChargeRepository

    @Test
    fun testGetClientCharges_Successful() = runTest(testDispatcher) {
        val chargeResponseMock = ChargeResponseDto()
        val successResponse = PageResponseDto(
            5,
            List(5) { chargeResponseMock },
        )

        val fakeClientChargeService = object : BaseFakeClientChargeService() {
            override fun getClientChargeList(
                clientId: Long,
            ): kotlinx.coroutines.flow.Flow<PageResponseDto<ChargeResponseDto>> {
                return flowOf(successResponse)
            }
        }

        dataManager = object : DataManager() {
            override val clientChargeApi: ClientChargeService
                get() = fakeClientChargeService
        }

        clientChargeRepositoryImp = ClientChargeRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = clientChargeRepositoryImp.getCharges(123L)
        val item = resultFlow.first()
        assertIs<DataState.Success<Page<Charge>>>(item)
    }

    @Test
    fun testGetClientCharges_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"

        val fakeClientChargeService = object : BaseFakeClientChargeService() {
            override fun getClientChargeList(
                clientId: Long,
            ): kotlinx.coroutines.flow.Flow<PageResponseDto<ChargeResponseDto>> {
                return flow { throw Exception(errorMsg) }
            }
        }

        dataManager = object : DataManager() {
            override val clientChargeApi: ClientChargeService
                get() = fakeClientChargeService
        }

        clientChargeRepositoryImp = ClientChargeRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = clientChargeRepositoryImp.getCharges(123L)
        val item = resultFlow.first()
        val error = assertIs<DataState.Error<Page<Charge>>>(item)
        assertEquals(errorMsg, error.exception.message)
    }

    @Test
    fun testGetLoanOrSavingsCharges_Successful() = runTest(testDispatcher) {
        val chargeResponseMock = ChargeResponseDto()
        val successResponse = List(5) { chargeResponseMock }

        val fakeClientChargeService = object : BaseFakeClientChargeService() {
            override fun getChargeList(
                chargeType: String,
                chargeTypeId: Long,
            ): kotlinx.coroutines.flow.Flow<List<ChargeResponseDto>> {
                return flowOf(successResponse)
            }
        }

        dataManager = object : DataManager() {
            override val clientChargeApi: ClientChargeService
                get() = fakeClientChargeService
        }

        clientChargeRepositoryImp = ClientChargeRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = clientChargeRepositoryImp.getLoanOrSavingsCharges(ChargeType.LOAN, 123L)
        val item = resultFlow.first()
        assertIs<DataState.Success<List<Charge>>>(item)
    }

    @Test
    fun testGetLoanOrSavingsCharges_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"

        val fakeClientChargeService = object : BaseFakeClientChargeService() {
            override fun getChargeList(
                chargeType: String,
                chargeTypeId: Long,
            ): kotlinx.coroutines.flow.Flow<List<ChargeResponseDto>> {
                return flow { throw Exception(errorMsg) }
            }
        }

        dataManager = object : DataManager() {
            override val clientChargeApi: ClientChargeService
                get() = fakeClientChargeService
        }

        clientChargeRepositoryImp = ClientChargeRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = clientChargeRepositoryImp.getLoanOrSavingsCharges(ChargeType.LOAN, 123L)
        val item = resultFlow.first()
        val error = assertIs<DataState.Error<List<Charge>>>(item)
        assertEquals(errorMsg, error.exception.message)
    }

    @Test
    fun testGetShareAccountCharges_Successful() = runTest(testDispatcher) {
        val shareAccountDetailsResponseDto = ShareWithAssociationsResponseDto()

        val fakeShareAccountService = object : BaseFakeShareAccountService() {
            override fun getShareAccountDetails(
                accountId: Long,
                associations: String,
            ): kotlinx.coroutines.flow.Flow<ShareWithAssociationsResponseDto> {
                return flowOf(shareAccountDetailsResponseDto)
            }
        }

        dataManager = object : DataManager() {
            override val shareAccountApi: ShareAccountService
                get() = fakeShareAccountService
        }

        clientChargeRepositoryImp = ClientChargeRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = clientChargeRepositoryImp.getShareAccountCharges(123L)
        val item = resultFlow.first()
        assertIs<DataState.Success<List<Charge>>>(item)
    }

    @Test
    fun testGetShareAccountCharges_Unsuccessful() = runTest(testDispatcher) {
        val errorMsg = "Error occurred"

        val fakeShareAccountService = object : BaseFakeShareAccountService() {
            override fun getShareAccountDetails(
                accountId: Long,
                associations: String,
            ): kotlinx.coroutines.flow.Flow<ShareWithAssociationsResponseDto> {
                return flow { throw Exception(errorMsg) }
            }
        }

        dataManager = object : DataManager() {
            override val shareAccountApi: ShareAccountService
                get() = fakeShareAccountService
        }

        clientChargeRepositoryImp = ClientChargeRepositoryImp(
            dataManager = dataManager,
            ioDispatcher = testDispatcher,
        )

        val resultFlow = clientChargeRepositoryImp.getShareAccountCharges(123L)
        val item = resultFlow.first()
        val error = assertIs<DataState.Error<List<Charge>>>(item)
        assertEquals(errorMsg, error.exception.message)
    }
}

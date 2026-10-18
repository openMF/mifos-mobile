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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repositoryImpl.ThirdPartyTransferRepositoryImp
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsTemplateResponseDto
import org.mifos.mobile.core.network.services.ThirdPartyTransferService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class ThirdPartyTransferRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var transferRepositoryImp: ThirdPartyTransferRepositoryImp

    @BeforeTest
    fun setUp() {
        dataManager = object : DataManager() {}
        transferRepositoryImp = ThirdPartyTransferRepositoryImp(dataManager, testDispatcher)
    }

    @Test
    fun testThirdPartyTransferTemplate_Successful() = runTest(testDispatcher) {
        val response = AccountOptionsTemplateResponseDto()

        dataManager = object : DataManager() {
            override val thirdPartyTransferApi: ThirdPartyTransferService
                get() = object : BaseFakeThirdPartyTransferService() {
                    override fun accountTransferTemplate(): Flow<AccountOptionsTemplateResponseDto> {
                        return flowOf(response)
                    }
                }
        }
        transferRepositoryImp = ThirdPartyTransferRepositoryImp(dataManager, testDispatcher)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        val item = result.drop(1).first()
        assertIs<DataState.Success<AccountOptionsTemplate>>(item)
    }

    @Test
    fun testThirdPartyTransferTemplate_Unsuccessful() = runTest(testDispatcher) {
        val errorMessage = "Error occurred"

        dataManager = object : DataManager() {
            override val thirdPartyTransferApi: ThirdPartyTransferService
                get() = object : BaseFakeThirdPartyTransferService() {
                    override fun accountTransferTemplate(): Flow<AccountOptionsTemplateResponseDto> {
                        throw Exception(errorMessage)
                    }
                }
        }
        transferRepositoryImp = ThirdPartyTransferRepositoryImp(dataManager, testDispatcher)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        val item = result.drop(1).first()
        val error = assertIs<DataState.Error<AccountOptionsTemplate>>(item)
        assertEquals(errorMessage, error.exception.message)
    }
}

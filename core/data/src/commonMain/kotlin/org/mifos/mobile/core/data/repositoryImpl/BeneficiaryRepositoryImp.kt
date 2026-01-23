/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.beneficiary.toModel
import org.mifos.mobile.core.data.mapper.payloads.toDto
import org.mifos.mobile.core.data.mapper.templates.toModel
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.extractErrorMessage
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.DataManager

class BeneficiaryRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : BeneficiaryRepository {
    override fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>> = flow {
        try {
            dataManager.beneficiaryApi.beneficiaryTemplate()
                .map { it.toModel() }
                .collect { response ->
                    emit(DataState.Success(response))
                }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }.flowOn(ioDispatcher)

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    dataManager.beneficiaryApi.createBeneficiary(beneficiaryPayload?.toDto())
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    dataManager.beneficiaryApi.updateBeneficiary(beneficiaryId!!, payload?.toDto())
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.beneficiaryApi.deleteBeneficiary(beneficiaryId!!)

                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override fun beneficiaryList(): Flow<DataState<List<Beneficiary>>> = flow {
        try {
            dataManager.beneficiaryApi.beneficiaryList()
                .collect { response ->
                    emit(DataState.Success(response.map { it.toModel() }))
                }
        } catch (e: Exception) {
            emit(DataState.Error(e, null))
        }
    }.flowOn(ioDispatcher)
}

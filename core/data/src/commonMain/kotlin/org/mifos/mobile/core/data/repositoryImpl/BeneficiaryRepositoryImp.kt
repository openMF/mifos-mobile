/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
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
                val response = dataManager.beneficiaryApi.createBeneficiary(beneficiaryPayload)
                if (response.status.value != 200) {
                    return@withContext DataState.Error(
                        Exception("API Error: ${response.status.value}"),
                        response.status.description,
                    )
                }

                DataState.Success("Created successfully")
            } catch (e: Exception) {
                DataState.Error(e, null)
            }
        }
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.beneficiaryApi.updateBeneficiary(beneficiaryId!!, payload)
                if (response.status.value != 200) {
                    return@withContext DataState.Error(
                        Exception("API Error: ${response.status.value}"),
                        response.status.description,
                    )
                }

                DataState.Success("Updated successfully")
            } catch (e: Exception) {
                DataState.Error(e, null)
            }
        }
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.beneficiaryApi.deleteBeneficiary(beneficiaryId!!)
            }
            DataState.Success("Deleted successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override fun beneficiaryList(): Flow<DataState<List<Beneficiary>>> = flow {
        try {
            dataManager.beneficiaryApi.beneficiaryList()
                .collect { response ->
                    emit(DataState.Success(response))
                }
        } catch (e: Exception) {
            emit(DataState.Error(e, null))
        }
    }.flowOn(ioDispatcher)
}

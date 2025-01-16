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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifospay.core.common.DataState
import org.mifospay.core.common.asDataStateFlow

class BeneficiaryRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : BeneficiaryRepository {

    override fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>> {
        return dataManager.beneficiaryApi.beneficiaryTemplate()
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.beneficiaryApi.createBeneficiary(beneficiaryPayload)
            }
            DataState.Success("Created successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.beneficiaryApi.updateBeneficiary(beneficiaryId!!, payload)
            }
            DataState.Success("Updated successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
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

    override suspend fun beneficiaryList(): Flow<DataState<List<Beneficiary>>> {
        return dataManager.beneficiaryApi.beneficiaryList()
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}

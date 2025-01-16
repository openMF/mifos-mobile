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
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.network.DataManager
import org.mifospay.core.common.DataState
import org.mifospay.core.common.asDataStateFlow

class GuarantorRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : GuarantorRepository {

    override fun getGuarantorTemplate(loanId: Long?): Flow<DataState<GuarantorTemplatePayload?>> {
        return dataManager.guarantorApi.getGuarantorTemplate(loanId!!)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.guarantorApi.createGuarantor(loanId!!, payload)
            }
            DataState.Success("Created successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.guarantorApi.updateGuarantor(payload, loanId!!, guarantorId!!)
            }
            DataState.Success("Created successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun deleteGuarantor(loanId: Long?, guarantorId: Long?): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.guarantorApi.deleteGuarantor(loanId!!, guarantorId!!)
            }
            DataState.Success("Created successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override fun getGuarantorList(loanId: Long): Flow<DataState<List<GuarantorPayload?>?>> {
        return dataManager.guarantorApi.getGuarantorList(loanId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}

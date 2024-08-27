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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class BeneficiaryRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : BeneficiaryRepository {

    override suspend fun beneficiaryTemplate(): Flow<BeneficiaryTemplate> {
        return flow {
            emit(dataManager.beneficiaryTemplate())
        }
    }

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Flow<ResponseBody> {
        return flow {
            emit(dataManager.createBeneficiary(beneficiaryPayload))
        }
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): Flow<ResponseBody> {
        return flow {
            emit(dataManager.updateBeneficiary(beneficiaryId, payload))
        }
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): Flow<ResponseBody> {
        return flow {
            emit(dataManager.deleteBeneficiary(beneficiaryId))
        }
    }

    override suspend fun beneficiaryList(): Flow<List<Beneficiary>> {
        return flow {
            emit(dataManager.beneficiaryList())
        }
    }
}

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

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.beneficiary.toModel
import org.mifos.mobile.core.data.mapper.payloads.toDto
import org.mifos.mobile.core.data.mapper.templates.toModel
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.DataManager

class BeneficiaryRepositoryImp(
    private val dataManager: DataManager,
    ioDispatcher: CoroutineDispatcher,
) : BaseRepository(ioDispatcher), BeneficiaryRepository {
    override fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>> =
        dataManager.beneficiaryApi.beneficiaryTemplate()
            .map { it.toModel() }
            .asDataState()

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): DataState<String> = safeCall {
        dataManager.beneficiaryApi
            .createBeneficiary(beneficiaryPayload?.toDto())
            .bodyAsText()
    }
    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String> = safeCall {
        dataManager.beneficiaryApi
            .updateBeneficiary(beneficiaryId!!, payload?.toDto())
            .bodyAsText()
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): DataState<String> = safeCall {
        dataManager.beneficiaryApi
            .deleteBeneficiary(beneficiaryId!!)
            .bodyAsText()
    }

    override fun beneficiaryList(): Flow<DataState<List<Beneficiary>>> =
        dataManager.beneficiaryApi.beneficiaryList()
            .map { response -> response.map { it.toModel() } }
            .asDataState()
}

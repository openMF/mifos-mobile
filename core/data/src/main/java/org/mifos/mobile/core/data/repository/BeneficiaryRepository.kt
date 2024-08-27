/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate

interface BeneficiaryRepository {

    suspend fun beneficiaryTemplate(): Flow<BeneficiaryTemplate>

    suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Flow<ResponseBody>

    suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): Flow<ResponseBody>

    suspend fun deleteBeneficiary(beneficiaryId: Long?): Flow<ResponseBody>

    suspend fun beneficiaryList(): Flow<List<Beneficiary>>
}
